package com.zhijianlife.common;

import android.app.Application;

/**
 * 存放全局变量
 * @author wyl
 *
 */
public class MyCount extends Application{
	
//	private String URL = "http://www.zhijianlife.com";
//	private String URL = "http://218.244.146.86:58083";
	private String URL = "http://192.168.1.136:8080/icsp-phone";
//	private String URL = "http://jbcdh.vicp.net:58085/icsp-phone";
//	private String URL = "http://192.168.1.111:58085/icsp-phone";

	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
}
