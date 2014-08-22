package com.zhijianlife.util;

public class ActionUtil {
	/**
	 * 商家登录接口
	 * @param String username;
	 * @param String password;
	 */
	public static String SELLER_LOGIN = "/thirdpart/login.action";
	/**
	 * 分页新订单列表
	 * @param int userId; 商家ID
	 * @param int curPage; 页码
	 * @param int pageSize; 每页数
	 */
	public static String ORDER_LIST = "/thirdpart/getOrderInfo.action";
	/**
	 * 新订单详情
	 * @param int orderId; 订单ID
	 */
	public static String ORDER_INFO = "/thirdpart/getOrderExt.action";
}
