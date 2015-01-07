package com.zhijianlife.core.activity;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhijianlife.R;
import com.zhijianlife.common.MyCount;
import com.zhijianlife.core.model.ResultBean;
import com.zhijianlife.core.model.UserSeller;
import com.zhijianlife.util.ActionUtil;
import com.zhijianlife.util.HttpClientUtil;
import com.zhijianlife.util.KeyValueUtil;

public class LoginActivity extends Activity {
	private Context thisContext = LoginActivity.this;
	private MyCount myCount;
	private String TAG = "LoginActivity";
	private EditText loginName,password;
	private Button loginBtn;
	private CheckBox rem_pw, auto_login;
	private SharedPreferences sp;
	
//	private RelativeLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Intent intent = getIntent();  
        String scheme = intent.getScheme();  
        Uri uri = intent.getData();  
        System.out.println("scheme:"+scheme);
        if (uri != null) {  
            String host = uri.getHost();  
            String dataString = intent.getDataString();  
            String id = uri.getQueryParameter("id");  
            String path = uri.getPath();  
            String path1 = uri.getEncodedPath();  
            String queryString = uri.getQuery();  
            System.out.println("host:"+host);  
            System.out.println("dataString:"+dataString);  
            System.out.println("id:"+id);  
            System.out.println("path:"+path);  
            System.out.println("path1:"+path1);  
            System.out.println("queryString:"+queryString);  
        }
        
		//设置背景色
//		layout = (RelativeLayout) findViewById(R.id.activity_login);
//		int mycolor = getResources().getColor(R.color.floralwhite);
//		layout.setBackgroundColor(mycolor);
		
		myCount = (MyCount) getApplication();
		loginName = (EditText) findViewById(R.id.login_name);
		password = (EditText) findViewById(R.id.login_pwd);
		loginBtn = (Button) findViewById(R.id.login_btn);
		
		sp = this.getSharedPreferences(KeyValueUtil.FILE_NAME, Context.MODE_WORLD_READABLE);
		rem_pw = (CheckBox) findViewById(R.id.cb_mima);
		auto_login = (CheckBox) findViewById(R.id.cb_auto);
		
		//判断记住密码多选框的状态
		if(sp.getBoolean(KeyValueUtil.ISCHECK, false)){
			//设置默认是记录密码状态
			rem_pw.setChecked(true);
			loginName.setText(sp.getString(KeyValueUtil.USERNAME, ""));
			password.setText(sp.getString(KeyValueUtil.PASSWORD, ""));
			myCount.setURL(sp.getString(KeyValueUtil.URL, ""));

			//判断自动登录多选框状态
			if(sp.getBoolean(KeyValueUtil.AUTO_ISCHECK, false)){
				//设置默认是自动登录状态
				auto_login.setChecked(true);
				new Thread(new Runnable() {
					@Override
					public void run() {
						userLogin(sp.getString(KeyValueUtil.USERNAME, ""),sp.getString(KeyValueUtil.PASSWORD, ""),sp.getString(KeyValueUtil.URL, ""));
					}
				}).start();
			}
		}
		
		loginBtn.setOnClickListener(userLoginListener);
		
		rem_pw.setOnClickListener(remember);
		auto_login.setOnClickListener(autoLogin);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	/**
	 * 菜单事件
	 */
	public boolean onOptionsItemSelected(MenuItem item){
//		String URL = myCount.getURL();
		String URL = sp.getString(KeyValueUtil.URL, "");
		switch (item.getItemId()) {
			case R.id.login_settings:
				final String[] strs = new String[]{"测试服务111",
						"本机服务108",
						"本机服务136",
						"测试服务器bup2"};
				final String[] values = new String[]{
						"http://192.168.1.111:58085/icsp-phone",
						"http://192.168.1.108:8087/icsp-phone",
						"http://192.168.1.136:8080/icsp-phone",
						"http://218.244.146.86:58083/icsp-phone-bup2"
					};
				int index = 0;
				for(int i=0; i<strs.length; i++){
					if(values[i].equals(URL)){
						index = i;
					}
				}
				new AlertDialog.Builder(this)
					.setTitle("单选框")
					.setIcon(android.R.drawable.btn_star_big_on)
					.setSingleChoiceItems(strs, index, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							myCount.setURL(values[which]);
							sp.edit().putString(KeyValueUtil.URL, values[which]).commit();
							dialog.dismiss();
						}
					})
					.setNegativeButton("取消", null)
					.show();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}
	/**
	 * 登录方法
	 * @return
	 */
	private String userLogin(){
		String name = loginName.getText().toString();
		String pwd = password.getText().toString();
		String url = myCount.getURL()+ActionUtil.SELLER_LOGIN;

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("username", name);
		map.put("password", pwd);
		String params = HttpClientUtil.mapToJsonString(map, null);
		
		String result = HttpClientUtil.post(params, url, thisContext);
		Log.i(TAG, result);
		
		try {
			JSONObject jsonResult = new JSONObject(result);
			Gson gson = new Gson();
			String detail = jsonResult.getString("detail");
			UserSeller seller = gson.fromJson(detail, UserSeller.class);
			myCount.setSeller(seller);
			
			//登录成功和记住密码框为选中状态才保存用户信息
			if(rem_pw.isChecked()){
				//记住用户名、密码
				Editor editor = sp.edit();
				editor.putString(KeyValueUtil.USERNAME, name);
				editor.putString(KeyValueUtil.PASSWORD, pwd);
				editor.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	/**
	 * 自动登录调用的方法
	 * @return
	 */
	private String userLogin(String name, String pwd, String url){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("username", name);
		map.put("password", pwd);
		String params = HttpClientUtil.mapToJsonString(map, null);
		
		String result = HttpClientUtil.post(params, url+ActionUtil.SELLER_LOGIN, thisContext);
		Log.i(TAG, result);
		
		try {
			JSONObject jsonResult = new JSONObject(result);
			Gson gson = new Gson();
			String detail = jsonResult.getString("detail");
			UserSeller seller = gson.fromJson(detail, UserSeller.class);
			myCount.setSeller(seller);
			//跳转界面
			Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
			LoginActivity.this.startActivity(intent);
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	/**
	 * 用户登录监听
	 */
	private OnClickListener userLoginListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String result = userLogin();
					Looper.prepare();
					
					Gson gson = new Gson();
					ResultBean resultBean = gson.fromJson(result, ResultBean.class);
					
					if(resultBean.isFlag()){
						if(resultBean.isSuccess()){
							
							Intent intent = new Intent(thisContext, HomeActivity.class);
							startActivity(intent);
							finish();
						} else {
							Toast.makeText(thisContext, resultBean.getInfo(), Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(thisContext, resultBean.getMessage(), Toast.LENGTH_SHORT).show();
					}
					
					Looper.loop();
				}
			}).start();
		}
	};
	/**
	 * 监听记住密码
	 */
	private OnClickListener remember = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(rem_pw.isChecked()){
//				System.out.println("记住密码已选中");
//				sp.edit().putBoolean("ISCHECK", true).commit();
				sp.edit().putBoolean(KeyValueUtil.ISCHECK, true).commit();
			}else {
//				System.out.println("记住密码没有选中");
				sp.edit().putBoolean(KeyValueUtil.ISCHECK, false).commit();
			}
		}
	};
	/**
	 * 监听自动登录
	 */
	private OnClickListener autoLogin = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(auto_login.isChecked()){
//				System.out.println("自动登录已选中");
//				sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
				sp.edit().putBoolean(KeyValueUtil.AUTO_ISCHECK, true).commit();
			}else {
//				System.out.println("自动登录没有选中");
				sp.edit().putBoolean(KeyValueUtil.AUTO_ISCHECK, false).commit();
			}
		}
	};
}
