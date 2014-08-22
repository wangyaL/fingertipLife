package com.zhijianlife.core.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.zhijianlife.R;
import com.zhijianlife.common.MyCount;
import com.zhijianlife.common.Paging;
import com.zhijianlife.core.model.OrderBean;

public class OrderListActivity extends Activity{
	private Context thisContext = OrderListActivity.this;
	private String TAG = "OrderListActivity";
	private MyCount myCount;
	private ListView listView;
	private SimpleAdapter adapter;
	private Paging paging;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);

		myCount = (MyCount) getApplication();
		listView = (ListView) findViewById(R.id.listview_order);

		Intent getIntent = getIntent();
		String result = (String) getIntent.getSerializableExtra("result");
		paging = (Paging) getIntent.getSerializableExtra("paging");
		Log.i(TAG, paging.getPageNum()+"");
		Log.i(TAG, paging.getPageSize()+"");
		Log.i(TAG, paging.getAllNum()+"");
		Log.i(TAG, paging.getAllPage()+"");
		try {
			JSONObject jsonResult = new JSONObject(result);
			Gson gson = new Gson();
			JSONArray arrayJson = jsonResult.getJSONArray("detail");
			final List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
			for(int i=0; i<arrayJson.length(); i++){
				OrderBean orderBean = gson.fromJson(arrayJson.get(i).toString(), OrderBean.class);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("bizid", orderBean.getBizId());
				map.put("username", orderBean.getUserName());
				map.put("phone", orderBean.getPhone());
				map.put("time", orderBean.getBizTime());
				map.put("address", orderBean.getAddress());
				listMap.add(map);
			}
			String[] map_keys = new String[]{"bizid", "username", "phone", "time", "address"};
			int[] id_keys = new int[]{R.id.order_list_bizid, R.id.order_list_username, R.id.order_list_phone,
					R.id.order_list_time_show, R.id.order_list_address};
			adapter = new SimpleAdapter(thisContext, listMap, R.layout.order_list,
					map_keys, id_keys);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					Intent intent = new Intent(OrderListActivity.this, HomeActivity.class);
					intent.putExtra("bizId", listMap.get(arg2).get("bizid")+"");
					startActivity(intent);
				}
			});
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
