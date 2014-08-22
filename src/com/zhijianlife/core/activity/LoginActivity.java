package com.zhijianlife.core.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhijianlife.R;
import com.zhijianlife.common.MyCount;
import com.zhijianlife.core.model.ResultBean;
import com.zhijianlife.core.model.UserSeller;
import com.zhijianlife.util.ActionUtil;
import com.zhijianlife.util.HttpClientUtil;

public class LoginActivity extends Activity {
	private Context thisContext = LoginActivity.this;
	private MyCount myCount;
	private String TAG = "LoginActivity";
	private EditText loginName,password;
	private Button loginBtn;
	
//	private RelativeLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//设置背景色
//		layout = (RelativeLayout) findViewById(R.id.activity_login);
//		int mycolor = getResources().getColor(R.color.floralwhite);
//		layout.setBackgroundColor(mycolor);
		
		myCount = (MyCount) getApplication();
		loginName = (EditText) findViewById(R.id.login_name);
		password = (EditText) findViewById(R.id.login_pwd);
		loginBtn = (Button) findViewById(R.id.login_btn);
		
		final String url = myCount.getURL()+ActionUtil.SELLER_LOGIN;
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String name = loginName.getText().toString();
				String pwd = password.getText().toString();
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("username", name);
				map.put("password", pwd);
				final String params = HttpClientUtil.mapToJsonString(map, null);
				new Thread(new Runnable() {
					@Override
					public void run() {
						Looper.prepare();
						String result = HttpClientUtil.post(params, url, thisContext);
						Log.i(TAG, result);
						try {
							JSONObject jsonResult = new JSONObject(result);
							Gson gson = new Gson();
							ResultBean resultBean = gson.fromJson(result, ResultBean.class);
							
							if(resultBean.isFlag()){
								if(resultBean.isSuccess()){
									String detail = jsonResult.getString("detail");
									UserSeller seller = gson.fromJson(detail, UserSeller.class);
									myCount.setSeller(seller);
//							Bundle data = new Bundle();
//							data.putSerializable("seller", seller);
									Intent intent = new Intent(thisContext, HomeActivity.class);
//							intent.putExtras(data);
									startActivity(intent);
									finish();
								} else {
									
									Toast.makeText(thisContext, resultBean.getInfo(), Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(thisContext, resultBean.getMessage(), Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Looper.loop();
					}
				}).start();
			}
		});
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
		String URL = myCount.getURL();
		switch (item.getItemId()) {
			case R.id.login_settings:
				final String[] strs = new String[]{"测试服务111",
						"本机服务136",
						"服务器"};
				final String[] values = new String[]{
						"http://192.168.1.111:58085/icsp-phone",
						"http://192.168.1.136:8080/icsp-phone",
						"http://218.244.146.86:58083"
					};
				int index = 0;
				for(int i=0; i<strs.length; i++){
					if(values[i] == URL){
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
}
