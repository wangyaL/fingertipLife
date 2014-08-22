package com.zhijianlife.core.activity;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhijianlife.R;
import com.zhijianlife.common.MyCount;
import com.zhijianlife.common.Paging;
import com.zhijianlife.core.model.ResultBean;
import com.zhijianlife.core.model.UserSeller;
import com.zhijianlife.util.ActionUtil;
import com.zhijianlife.util.HttpClientUtil;

public class HomeActivity extends Activity {
	private Context thisContext = HomeActivity.this;
	private String TAG = "HomeActivity";
	private MyCount myCount;
	private RelativeLayout neworder,orderlist,goodslist,infomation,complaint;
	private UserSeller seller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		myCount = (MyCount) getApplication();
//		Intent getIntent = getIntent();
//		seller = (UserSeller) getIntent.getSerializableExtra("seller");
		seller = myCount.getSeller();
		
		neworder = (RelativeLayout) findViewById(R.id.home_block_neworder);
		neworder.setOnClickListener(listener_newOrder);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	/**
	 * 点击新订单监听
	 */
	private OnClickListener listener_newOrder = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final String url = myCount.getURL() + ActionUtil.ORDER_LIST;
			final Paging paging = new Paging(1, 10);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("userId", seller.getUserId());
			map.put("curPage", paging.getPageNum());
			map.put("pageSize", paging.getPageSize());
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
						int allNum = jsonResult.getInt("allNum");
						int allPage = jsonResult.getInt("allPage");
						paging.setAllNum(allNum);
						paging.setAllPage(allPage);
						if(resultBean.isFlag()){
		//					if(resultBean.isSuccess()){
							if(allNum != 0){
								Bundle data = new Bundle();
								data.putSerializable("result", result);
								data.putSerializable("paging", paging);
								Intent intent = new Intent(thisContext, OrderListActivity.class);
								intent.putExtras(data);
								startActivity(intent);
							} else {
								Toast.makeText(thisContext, resultBean.getInfo(), Toast.LENGTH_SHORT).show();
							}
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
	};
}
