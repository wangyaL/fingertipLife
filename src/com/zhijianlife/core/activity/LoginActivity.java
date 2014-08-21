package com.zhijianlife.core.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhijianlife.R;
import com.zhijianlife.common.MyCount;
import com.zhijianlife.util.ActionUtil;
import com.zhijianlife.util.HttpClientUtil;

public class LoginActivity extends Activity {
	private EditText loginName,password;
	private Button loginBtn;
	private String TAG = "LoginActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		loginName = (EditText) findViewById(R.id.login_name);
		password = (EditText) findViewById(R.id.login_pwd);
		loginBtn = (Button) findViewById(R.id.login_btn);
		
		String name = loginName.getText().toString();
		String pwd = password.getText().toString();
		
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("username", name);
		map.put("password", pwd);
		final String params = HttpClientUtil.mapToJsonString(map, null);
		MyCount myCount = (MyCount) getApplication();
		final String url = myCount.getURL()+ActionUtil.SELLER_LOGIN;
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String result = HttpClientUtil.post(params, url, LoginActivity.this);
				try {
					JSONObject jsonResult = new JSONObject(result);
					Log.i(TAG, result);
					Log.i(TAG, "===flag===>"+jsonResult.get("flag"));
					Log.i(TAG, "===message===>"+jsonResult.get("message"));
					Log.i(TAG, "===isSuccess===>"+jsonResult.get("isSuccess"));
					Log.i(TAG, "===info===>"+jsonResult.get("info"));
					
					Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
