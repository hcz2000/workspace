package com.jams.finance.data;

public class NetValue {
	private String code;
	private String date;
	private double value;
	
	public NetValue(String code,String date,double value) {
		this.code=code;
		this.date=date;
		this.value=value;
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

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
