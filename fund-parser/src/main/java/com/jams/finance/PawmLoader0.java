package com.jams.finance;

import java.io.IOException;
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

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class PawmLoader0 extends BaseLoader{
	private FirefoxDriver driver;
	
	public PawmLoader0() {
        super();
        System.setProperty("webdriver.gecko.driver", "../../pywork/Tagui/geckodriver.exe");
		FirefoxOptions opts=new FirefoxOptions();
		opts.setCapability("pageLoadStrategy", "none");
		driver=new FirefoxDriver(opts);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
	}
	
	public List<NetValue> fetchUpdate(String code,String url,String value_type) throws Exception{
		NetValue lastRecord = getLastRecord(code);
		String last_sync_date = lastRecord.getDate();
		double last_net_value = lastRecord.getValue();
		System.out.println("##" + last_sync_date + "  " + last_net_value);
		driver.get(url);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='tab']")));
        
		List<WebElement> menuList=driver.findElements(By.xpath("//div[@role='tab']"));
		WebElement valueMenu=menuList.get(2);
		//System.out.println(valueMenu.findElement(By.xpath("./div")).getText());
		valueMenu.click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='ant-table-body']/table/tbody/tr")));

		List<NetValue> reverseOrderedValues = new ArrayList<NetValue>();
		boolean stop = false;
		int pageNo = 1;
		while (!stop) {
			List<NetValue> pageValues = getOnePage(code,pageNo);
			for (NetValue pageRow : pageValues) {
				if (pageRow.getDate().compareTo(last_sync_date) > 0) {
					reverseOrderedValues.add(pageRow);
				} else {
					stop = true;
				}
			}
			pageNo++;
		}

		List<NetValue> returnValues = new ArrayList<NetValue>();
		double accumulated_revenue=last_net_value-1.00;
		for (int i = reverseOrderedValues.size() - 1; i >= 0; i--) {
			
			if(value_type.equals("revenue")){
				String date=reverseOrderedValues.get(i).getDate();
				double value=reverseOrderedValues.get(i).getValue()/10000;
				accumulated_revenue=accumulated_revenue+(1.0+accumulated_revenue)*value;
                returnValues.add(new NetValue(code,date, 1.0+accumulated_revenue));
			}else {
				returnValues.add(reverseOrderedValues.get(i));
			}
		}

		return returnValues;
		
		
	}
	
	private List<NetValue> getOnePage(String code,int pageNo) throws IOException,NoMoreDataException {

		List<NetValue> netValues = new ArrayList<NetValue>();
		
        List<WebElement> rows=driver.findElements(By.xpath("//div[@class='ant-table-body']/table/tbody/tr"));
        
		for (WebElement row : rows) {
			List<WebElement> cols=row.findElements(By.xpath("./td"));
			String rpt_date = cols.get(1).getText();
        	Double net_value = Double.parseDouble(cols.get(2).getText());
			NetValue onerow = new NetValue(code, rpt_date, net_value);
			netValues.add(onerow);
		}
		
		WebElement next=driver.findElement(By.xpath("//li[@title='下一页']"));
		if(next.getAttribute("aria-disabled")==null) {
			next.click();
		}else {
			throw new NoMoreDataException("End of Next page");
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
		return "pinganwm";//平安
	}

	public static void main(String[] args) {
		try(PawmLoader0 boc=new PawmLoader0()){
			boc.refreshCatalog();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
