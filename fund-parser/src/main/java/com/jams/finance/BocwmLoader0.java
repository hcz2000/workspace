package com.jams.finance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BocwmLoader0 extends BaseLoader{
	private FirefoxDriver driver;
	
	public BocwmLoader0() {
        super();
		FirefoxOptions opts=new FirefoxOptions();
		opts.setCapability("pageLoadStrategy", "none");
		driver=new FirefoxDriver(opts);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
	}
	
	public List<NetValue> fetchUpdate(String code,String url,String value_type) throws Exception{
		NetValue lastRecord=getLastRecord(code);
        String last_sync_date = lastRecord.getDate();
        double last_net_value = lastRecord.getValue();
        //System.out.println("  "+last_sync_date+":"+last_net_value);
		driver.get(url);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//table[@class='layui-table']/tbody/tr[1]/td[1]"),code));

		WebElement contentDiv=driver.findElement(By.id("content"));
		List<WebElement> rows = contentDiv.findElements(By.xpath("//table[@class='layui-table']/tbody/tr"));

		List<NetValue> netValues=new ArrayList<NetValue>();
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
			 List<Revenue> revenues=new ArrayList<Revenue>();
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
		
		return netValues;
	}


	@Override
	public void close() throws Exception{
		if(driver!=null) {
			driver.close();
		}
		super.close();
	}

	@Override
	public String getVendor() {
		return "bocwm";//中银
	}

	public static void main(String[] args) {
		try(BocwmLoader0 boc=new BocwmLoader0()){
			boc.refreshCatalog();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
