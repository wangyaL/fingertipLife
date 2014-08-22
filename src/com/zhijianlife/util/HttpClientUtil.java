package com.zhijianlife.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

/**
 * HttpClient请求
 * @author wyl
 *
 */
public class HttpClientUtil {
	
	/**
	 * post请求
	 * @param params 请求参数,通过mapToJsonString()方法转换的json格式的字符串
	 * @param url 请求路径
	 * @param context 当前位置activity.this
	 * @return json格式的字符串
	 */
	public static String post(final String params, final String url, final Context context){
		String resultString = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		HttpResponse response;
		try {
			post.setEntity(new StringEntity(params));
			response = httpClient.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				resultString = EntityUtils.toString(response.getEntity());
			} else {
				Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultString;
	}

	/**
	 * post方式时，将传入参数转换为json格式
	 */
	@SuppressWarnings("rawtypes")
	public static String mapToJsonString(HashMap<String, Object> map, 
			List<HashMap<String, Object>> childParams){
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject json = new JSONObject();
		try {
			Iterator iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry pairs = (Map.Entry) iter.next();
				String key = (String) pairs.getKey();
				Object value = (Object) pairs.getValue();
				jsonObject.put(key, value);
			}
			if (childParams != null) {
				for (int i = 0; i < childParams.size(); i++) {
					Iterator childIter = childParams.get(i).entrySet().iterator();
					JSONObject childJsonObject = new JSONObject();
					while (childIter.hasNext()) {
						Map.Entry pairs = (Map.Entry) childIter.next();
						String key = (String) pairs.getKey();
						Object value = (Object) pairs.getValue();
						childJsonObject.put(key, value);
					}
					jsonArray.put(i, childJsonObject);
				}
				jsonObject.put("detail", jsonArray);
			}
			json.put("jsonString", jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
}
