package com.jams.finance;

public class ProductConfig {
	private String code;
	private String desc;
	private String url;
	private String type;
	
	public ProductConfig() {
		
	}

	public ProductConfig(String code, String desc, String url, String type) {
		super();
		this.code = code;
		this.desc = desc;
		this.url = url;
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
