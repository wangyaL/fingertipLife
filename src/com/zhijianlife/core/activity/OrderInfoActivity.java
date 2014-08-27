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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhijianlife.R;
import com.zhijianlife.common.MyCount;
import com.zhijianlife.core.model.ResultBean;
import com.zhijianlife.util.ActionUtil;
import com.zhijianlife.util.HttpClientUtil;

public class OrderInfoActivity extends Activity {
	private Context thisContext = OrderInfoActivity.this;
	private String TAG = "OrderInfoActivity";
	private MyCount myCount;
	
	private TextView title,total,bMsg;
	private EditText sMsg;
	private Button acceptBtn,refuseBtn;
	
	private String sellerMsg, bizid;
	
	private ListView listView;
	private SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_info);
		
		myCount = (MyCount) getApplication();
		
		title = (TextView) findViewById(R.id.orderinfo_title);
		total = (TextView) findViewById(R.id.orderinfo_total);
		bMsg = (TextView) findViewById(R.id.orderinfo_buyer_msg);
		sMsg = (EditText) findViewById(R.id.orderinfo_seller_msg);
		acceptBtn = (Button) findViewById(R.id.orderinfo_accept_btn);
		refuseBtn = (Button) findViewById(R.id.orderinfo_refuse_btn);
		listView = (ListView) findViewById(R.id.orderinfo_listview);
		
		Intent getIntent = getIntent();
		int accept = getIntent.getIntExtra("accept", 0);
		Log.i(TAG, accept+"=当前订单状态");
		if(1 == accept){
			acceptBtn.setText("发货");
		}else{
			acceptBtn.setText("接收");
		}
		bizid = getIntent.getStringExtra("bizid");
		String buyername = getIntent.getStringExtra("buyername");
		String titleStr = bizid + "["+buyername+"]";
		String result = getIntent.getStringExtra("result");
		Double allMoney = 0.0;
		int allNum = 0;
		try {
			JSONObject jsonResult = new JSONObject(result);
			JSONArray arrayJson = jsonResult.getJSONArray("detail");
			final List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
			for(int i=0; i<arrayJson.length(); i++){
				JSONObject object = (JSONObject) arrayJson.get(i);
				Double totalMoney = object.getDouble("totalMoney");
				int num = object.getInt("num");
				allMoney += totalMoney;
				allNum += num;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("goodsName", object.get("goodsName"));
				map.put("num", "×"+num);
				map.put("sellPrice", "￥"+object.get("sellPrice"));
				listMap.add(map);
			}
			String[] map_keys = new String[]{"goodsName", "num", "sellPrice"};
			int[] id_keys = new int[]{R.id.order_info_goodsname, R.id.order_info_num, R.id.order_info_price};
			
			adapter = new SimpleAdapter(thisContext, listMap, R.layout.order_info,
					map_keys, id_keys);
			listView.setAdapter(adapter);
			
			title.setText(titleStr);
			total.setText("  共计："+allMoney+"元，数量："+allNum);
			bMsg.setText(jsonResult.getString("msg"));
			
			setListVHeight();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		sMsgDefault = sMsg.getText().toString();
//		//添加卖家附加信息输入框，获取焦点和失去焦点事件
//		sMsg.setOnFocusChangeListener(new OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				sellerMsg = sMsg.getText().toString();
//				if(hasFocus && sMsgDefault.equals(sellerMsg)){
//					sMsg.setText("");
//				}else if(!hasFocus && sellerMsg == null){
//					sMsg.setText(sMsgDefault);
//				}
//			}
//		});
		
		acceptBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String btnText = acceptBtn.getText().toString();
				Log.d(TAG, btnText);
				if(btnText.equals("接收")){
					btnOnClick(myCount.getURL()+ActionUtil.ACCEPT);
				}else{
					btnOnClick(myCount.getURL()+ActionUtil.DELIVERY);
				}
			}
		});
		refuseBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				btnOnClick(myCount.getURL()+ActionUtil.ACCEPTNOT);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_info, menu);
		return true;
	}
	/**
	 * 根据项数的大小自动改变高度
	 */
	private void setListVHeight(){
		ListAdapter listAdapter = listView.getAdapter();
		if(listAdapter == null){
			return;
		}
		
		int totalHeight = 0;
		for (int i=0; i<listAdapter.getCount(); i++){
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
//			System.out.println("====listItem.getMeasuredHeight()==>>"+listItem.getMeasuredHeight());
//			System.out.println("====listItem.getMeasuredHeight()+20==>>"+listItem.getMeasuredHeight()+20);
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()-1));
//		((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
//		((MarginLayoutParams)params).setMargins(0, 0, 0, 10);
		listView.setLayoutParams(params);
	}
	/**
	 * 按钮事件
	 * @param url 请求路径
	 */
	private void btnOnClick(final String url){
		sellerMsg = sMsg.getText().toString();
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("bizIds", bizid);
				map.put("sellerMsg", sellerMsg);
				String params = HttpClientUtil.mapToJsonString(map, null);
				String result = HttpClientUtil.post(params, url, thisContext);
				Log.i(TAG, result);
				try {
					Gson gson = new Gson();
					ResultBean bean = gson.fromJson(result, ResultBean.class);
					if(bean.isSuccess()){
						OrderInfoActivity.this.finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
