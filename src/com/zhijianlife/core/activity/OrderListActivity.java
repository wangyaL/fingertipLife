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
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhijianlife.R;
import com.zhijianlife.common.MyCount;
import com.zhijianlife.common.Paging;
import com.zhijianlife.core.model.OrderBean;
import com.zhijianlife.core.model.ResultBean;
import com.zhijianlife.util.ActionUtil;
import com.zhijianlife.util.HttpClientUtil;

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
		Log.i(TAG, paging.getPageNum()+"=当前第几页");
		Log.i(TAG, paging.getPageSize()+"=每页数量");
		Log.i(TAG, paging.getAllNum()+"=总共多少条");
		Log.i(TAG, paging.getAllPage()+"=总共多少页");
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
					final String bizid = listMap.get(arg2).get("bizid").toString();
					final String username = listMap.get(arg2).get("username").toString();
					final String url = myCount.getURL() + ActionUtil.ORDER_INFO;
					final HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("orderId", listMap.get(arg2).get("bizid"));
					final String params = HttpClientUtil.mapToJsonString(map, null);

					new Thread(new Runnable() {
						@Override
						public void run() {
							Looper.prepare();
							String result = HttpClientUtil.post(params, url, thisContext);
							Log.i(TAG, result);
							try {
//								JSONObject jsonResult = new JSONObject(result);
								Gson gson = new Gson();
								ResultBean resultBean = gson.fromJson(result, ResultBean.class);
								
								if(resultBean.isFlag()){
									Intent intent = new Intent(OrderListActivity.this, OrderInfoActivity.class);
									intent.putExtra("result", result)
										.putExtra("bizid", bizid).putExtra("buyername", username);
									startActivity(intent);
								} else {
									Toast.makeText(thisContext, resultBean.getMessage(), Toast.LENGTH_SHORT).show();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							Looper.loop();
						}
					}).start();
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
