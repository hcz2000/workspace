package com.jams.finance;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.yaml.snakeyaml.Yaml;

public abstract class BaseLoader{
	protected Map<String,ProductConfig> config=new HashMap<String,ProductConfig>();
	
	public BaseLoader() {
        Yaml yaml = new Yaml();
        try {
        	FileInputStream inputStream = new FileInputStream("/home/hcz/work/pywork/Tagui/wm.yaml");
            Map<String, Map<String,Object>> yamlMap = yaml.load(inputStream);
            String myCatalog=getCatalog();
            yamlMap.forEach((key,value)->{
            	String catalog=(String) value.get("catalog");
            	if(myCatalog.equals(catalog)){
            		List<Map<String,String>> prds=(List<Map<String, String>>) value.get("products");
            		for(Map<String,String> prd: prds ) {
            			ProductConfig cfg=new ProductConfig();
            			cfg.setCode(prd.get("code"));
            			cfg.setUrl(prd.get("url"));
            			cfg.setType(prd.get("value_type"));
            			config.put(cfg.getCode(),cfg);
            		}
            	}
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void refreshCatalog() {
		preFetch();		
		config.forEach((key,cfg)->{
			System.out.println(key+"---"+cfg.getUrl());
			List<NetValue> netValues=fetchUpdate(cfg.getCode(),cfg.getUrl(),cfg.getType());
			for(NetValue item: netValues)
				System.out.println(item.getCode()+":"+item.getDate()+"--"+String.valueOf(item.getValue()));

		});
		postFetch();
	}
	
	abstract public List<NetValue> fetchUpdate(String code,String url,String value_type);
	abstract public void preFetch();
	abstract public void postFetch();
	abstract public String getCatalog();
	
}
