package com.zhijianlife.core.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.google.gson.Gson;
import com.zhijianlife.R;
import com.zhijianlife.common.MyCount;
import com.zhijianlife.core.model.OrderBean;
import com.zhijianlife.core.model.UserSeller;

public class OrderListActivity extends Activity {
	private Context thisContext = OrderListActivity.this;
	private String TAG = "OrderListActivity";
	private MyCount myCount;
	private List<OrderBean> orderlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);
		
		myCount = (MyCount) getApplication();
		Intent getIntent = getIntent();
//		orderlist = getIntent.getParcelableArrayListExtra("orderlist");
		String result = (String) getIntent.getSerializableExtra("result");
		try {
			List<OrderBean> orderlist = new ArrayList<OrderBean>();
			JSONObject jsonResult = new JSONObject(result);
			Gson gson = new Gson();
			JSONArray arrayJson = jsonResult.getJSONArray("detail");
			for(int i=0; i<arrayJson.length(); i++){
				OrderBean orderBean = gson.fromJson(arrayJson.get(i).toString(), OrderBean.class);
				orderlist.add(orderBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.goods_list, menu);
		return true;
	}

}
