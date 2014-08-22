package com.zhijianlife.common;

import com.zhijianlife.core.model.UserSeller;

import android.app.Application;

/**
 * 存放全局变量
 * @author wyl
 *
 */
public class MyCount extends Application{
	
//	private String URL = "http://www.zhijianlife.com";
//	private String URL = "http://218.244.146.86:58083";
//	private String URL = "http://192.168.1.136:8080/icsp-phone";
	private String URL = "http://192.168.1.108:8087/icsp-phone";
//	private String URL = "http://jbcdh.vicp.net:58085/icsp-phone";
//	private String URL = "http://192.168.1.111:58085/icsp-phone";
	
	private UserSeller seller;

	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public UserSeller getSeller() {
		return seller;
	}
	public void setSeller(UserSeller seller) {
		this.seller = seller;
	}
}
