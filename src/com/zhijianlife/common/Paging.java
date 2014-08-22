package com.zhijianlife.common;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Paging implements Serializable{
	private int pageNum;
	private int pageSize;
	private int allPage;
	private int allNum;
	public Paging(int pageNum, int pageSize) {
		super();
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}
	public Paging(int pageNum, int pageSize, int allPage) {
		super();
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.allPage = allPage;
	}
	public Paging(int pageNum, int pageSize, int allPage, int allNum) {
		super();
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.allPage = allPage;
		this.allNum = allNum;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getAllPage() {
		return allPage;
	}
	public void setAllPage(int allPage) {
		this.allPage = allPage;
	}
	public int getAllNum() {
		return allNum;
	}
	public void setAllNum(int allNum) {
		this.allNum = allNum;
	}
}
