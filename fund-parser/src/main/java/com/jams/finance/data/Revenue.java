package com.jams.finance.data;

public class Revenue {
	
	private String code;
	private String date;
	private double revenue;
	
	public Revenue(String code,String date,double revenue) {
		this.code=code;
		this.date=date;
		this.revenue=revenue;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}
}
