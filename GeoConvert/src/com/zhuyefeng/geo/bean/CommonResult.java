package com.zhuyefeng.geo.bean;

import java.util.List;

public class CommonResult {
	private String status;
	private String message;
	private List<Point> result;


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Point> getResult() {
		return result;
	}

	public void setResult(List<Point> result) {
		this.result = result;
	}
	
	public boolean isSuccessful() {
		return "0".equals(status);
	}
}
