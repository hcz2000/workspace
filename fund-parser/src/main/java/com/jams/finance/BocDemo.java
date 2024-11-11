package com.jams.finance;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.omg.CORBA.portable.InputStream;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.yaml.snakeyaml.Yaml;

public class BocDemo{
	private Map<String,ProductConfig> configMap=new HashMap();
	
	public BocDemo() {
        Yaml yaml = new Yaml();
        try {
        	FileInputStream inputStream = new FileInputStream("/home/hcz/work/pywork/Tagui/wm.yaml");
            Map<String, Map<String,List<Map<String,String>>>> yamlMap = yaml.load(inputStream);
            List<Map<String,String>> prds=yamlMap.get("bocwm").get("products");
            for(Map<String,String> prd: prds ) {
            	ProductConfig cfg=new ProductConfig();
            	cfg.setCode(prd.get("code"));
            	cfg.setUrl(prd.get("url"));
            	cfg.setType(prd.get("value_type"));
            	configMap.put(cfg.getCode(),cfg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void refreshAll() {
		
		configMap.forEach((key,cfg)->{
			List<NetValue> netValues=fetchUpdate(cfg.getCode(),cfg.getUrl(),cfg.getType());
		
		for(NetValue item: netValues)
		     System.out.println(item.getCode()+":"+item.getDate()+"--"+String.valueOf(item.getValue()));
			}
		);
	}
	
	public List<NetValue> fetchUpdate(String code,String url,String value_type){
		FirefoxOptions opts=new FirefoxOptions();
		opts.setCapability("pageLoadStrategy", "none");
		FirefoxDriver driver=new FirefoxDriver(opts);
		
	
        String last_sync_date = "2024-09-30";
        double last_net_value = 1.00;
        		
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
	
		WebElement contentDiv=driver.findElement(By.id("content"));
		List<WebElement> rows = contentDiv.findElements(By.xpath("//table[@class='layui-table']/tbody/tr"));

		List<NetValue> netValues=new ArrayList();
		if("net_value".equals(value_type)){
			 for(WebElement row : rows) {
				 List<WebElement> cols=row.findElements(By.tagName("td"));
			     String rpt_date=cols.get(6).getText();
			     double net_value=Double.parseDouble(cols.get(2).getText());
			     if(rpt_date.compareTo(last_sync_date)<=0)
			    	 break;
			    	 
			     netValues.add(new NetValue(code,rpt_date,net_value));
			 }
			 Collections.reverse(netValues);
		}else {
			 List<Revenue> revenues=new ArrayList();
			 for( WebElement row : rows) {
				 List<WebElement> cols = row.findElements(By.tagName("td"));
				 String rpt_date=cols.get(6).getText();
			     double revenue = Double.parseDouble(cols.get(4).getText())/10000;
			     if(rpt_date.compareTo(last_sync_date)<=0)
			    	 break;
			     Revenue item=new Revenue(code,rpt_date,revenue);
			     revenues.add(item);
			 }
			 
			 double accumulated_revenue=last_net_value-1.00;
			 Collections.reverse(revenues);
			 for( Revenue item : revenues) {
				 accumulated_revenue=accumulated_revenue+(1.0+accumulated_revenue)*item.getRevenue();
				 NetValue newItem=new NetValue(item.getCode(),item.getDate(),1+accumulated_revenue);
				 netValues.add(newItem);
			 }
		}
		
		driver.close();

		return netValues;
	}

	public static void main(String[] args) {
			
		BocDemo boc=new BocDemo();

		boc.refreshAll();
		/*
		List<NetValue> netValues=boc.fetchUpdate("HXTT01",
					"https://www.bocwm.cn/html/1/netWorth/4114.html",
					"revenue");
		
		for(NetValue item: netValues)
		     System.out.println(item.getCode()+":"+item.getDate()+"--"+String.valueOf(item.getValue()));
		     */
	}
}
