package com.jams.fund;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class Demo{

	public static void main(String[] args) {
		FirefoxOptions opts=new FirefoxOptions();
		opts.setCapability("pageLoadStrategy", "none");
		FirefoxDriver driver=new FirefoxDriver(opts);
		
		/*
		String code = "YLFFB2201";
		String url = "https://www.bocwm.cn/html/1/netWorth/5173.html";
		String value_type="net_value";
        String last_sync_date = "2024-09-30";
        double last_net_value = 1.00;
        */
		
		String code = "HXTT01";
		String url = "https://www.bocwm.cn/html/1/netWorth/4114.html";
		String value_type="revenue";
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

		for(NetValue item: netValues)
		     System.out.println(item.getCode()+":"+item.getDate()+"--"+String.valueOf(item.getValue()));
	}
}
