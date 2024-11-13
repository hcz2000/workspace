package com.jams.finance;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.yaml.snakeyaml.Yaml;

import com.jams.finance.core.DatabaseConfig;
import com.jams.finance.core.DbEngine;

public abstract class BaseLoader{
	protected Map<String,ProductConfig> config=new HashMap<String,ProductConfig>();
	private DbEngine engine;
	
	public BaseLoader() {
		ApplicationContext ctx=new AnnotationConfigApplicationContext(DatabaseConfig.class);
		engine=(DbEngine) ctx.getBean("dbEngine");
		
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
		try {
			config.forEach((key,cfg)->{
				System.out.println(key+"---"+cfg.getUrl());
				List<NetValue> netValues=fetchUpdate(cfg.getCode(),cfg.getUrl(),cfg.getType());
				for(NetValue item: netValues)
					System.out.println(item.getCode()+":"+item.getDate()+"--"+String.valueOf(item.getValue()));

			});
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			postFetch();
		}
	}
	
	protected NetValue getLastRecord(String code) {
		List<Map<String, Object>> rows=engine.queryForList("select rpt_date,value from netvalue where code='"
																+code+"' order by rpt_date desc");
		
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoYearsAgo = now.minus(2, ChronoUnit.YEARS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
		NetValue lastRecord=new NetValue(code,twoYearsAgo.format(formatter),1.0000);
		if(rows.size()>0) {
			lastRecord.setCode(code);
			Map<String, Object> firstRow=rows.get(0);
			lastRecord.setDate((String)(firstRow.get("rpt_date")));
			lastRecord.setValue((Double)(firstRow.get("value")));
		}
		
		return lastRecord;
	}
	
	abstract public List<NetValue> fetchUpdate(String code,String url,String value_type);
	abstract public void preFetch();
	abstract public void postFetch();
	abstract public String getCatalog();
	
}
