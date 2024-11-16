package com.jams.finance.test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;


public class BocwmTest {
	@Test
    public void searchTest() {
    	
		FirefoxOptions opts=new FirefoxOptions();
		opts.setCapability("pageLoadStrategy", "none");
		FirefoxDriver driver=new FirefoxDriver(opts);

		
		String code = "YLFFB2201";
		String url = "https://www.bocwm.cn/html/1/netWorth/5173.html";
		String value_type="value_type";
        String last_sync_date = "2024-10-31";
        		
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
	
		WebElement contentDiv=driver.findElement(By.id("content"));
		List<WebElement> rows = contentDiv.findElements(By.xpath("//table[@class='layui-table']/tbody/tr"));

		if("net_value".equals(value_type)){
			 for(WebElement row : rows) {
				 List<WebElement> cols=row.findElements(By.tagName("td"));
			     String rpt_date=cols.get(6).getText();
			     String net_value=cols.get(2).getText();
			     if(rpt_date.compareTo(last_sync_date)>0)
			         //net_values.append((rpt_date, net_value))
			    	 System.out.println(rpt_date+":"+net_value);
			     else
			         break;
			     //net_values=net_values[::-1]
			 }
		}else {
			 //revenues=[]
			 for( WebElement row : rows) {
				 List<WebElement> cols = row.findElements(By.tagName("td"));
				 String rpt_date=cols.get(6).getText();
			     double revenue = Double.parseDouble(cols.get(4).getText())/10000;
			     if(rpt_date.compareTo(last_sync_date)>0)
			         //revenues.append((rpt_date, revenue))
			    	 System.out.println(rpt_date+":"+String.valueOf(revenue));
			     else
			         break;
/*
			            last_net_value=float(last_record[1])
			            accumulated_revenue=last_net_value-1.00
			            for row in revenues[::-1]:
			                accumulated_revenue=accumulated_revenue+(1.0+accumulated_revenue)*row[1]
			                (rpt_date,net_value)=(row[0],1.0+accumulated_revenue)
			                net_values.append((rpt_date, net_value))

			            #print(cols[0].text,cols[1].text,cols[2].text,cols[4].text,cols[6].text)
 */
			     }
		}
   
    }
}
