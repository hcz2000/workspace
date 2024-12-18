package com.jams.finance.loader;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.htmlunit.options.HtmlUnitDriverOptions;
import org.openqa.selenium.htmlunit.options.HtmlUnitOption;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.jams.finance.data.NetValue;

import org.htmlunit.BrowserVersion;
import org.htmlunit.html.HtmlElement;


public class CmbwmLoader extends BaseLoader{
	private WebDriver driver;
	
	public CmbwmLoader() {
        super();
       
        //System.setProperty("webdriver.gecko.driver", "../../pywork/Tagui/geckodriver.exe");
		FirefoxOptions opts=new FirefoxOptions();
		opts.setCapability("pageLoadStrategy", "none");
		driver=new FirefoxDriver(opts);
		
        /*
        HtmlUnitDriverOptions opts = new HtmlUnitDriverOptions(BrowserVersion.FIREFOX);;
		opts.setCapability(HtmlUnitOption.optJavaScriptEnabled,true);
		opts.setCapability(HtmlUnitOption.optPrintContentOnFailingStatusCode,true);
		opts.setCapability(HtmlUnitOption.optThrowExceptionOnScriptError, false);
		opts.setCapability(HtmlUnitOption.optCssEnabled,false);
		driver=new HtmlUnitDriver(opts);
		*/

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
	}
	
	public List<NetValue> fetchUpdate(String code,String url,String value_type) throws Exception{
		NetValue lastRecord=getLastRecord(code);
        String last_sync_date = lastRecord.getDate();
        double last_net_value = lastRecord.getValue();
        System.out.println("##" + last_sync_date + "  " + last_net_value);
		driver.get(url+"?prodTradeCode="+code+"&prodClcMode=01&finType=P");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[@class='flex-sub']/div[2]/div[1]/span[1]"),"销售代码："+code));

		WebElement contentDiv=driver.findElement(By.xpath("//div[@class='proTabList']"));
		List<WebElement> menuList = contentDiv.findElements(By.xpath("//div[@class='item text-14 flex']"));
		for (WebElement menu : menuList) {
			if ("产品净值".equals(menu.getText())) {
				menu.click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='proValue_end proValue_btn text-14']")));
				WebElement sub_menu = contentDiv.findElement(By.xpath("//div[@class='proValue_end proValue_btn text-14']"));
				sub_menu.click();
			}
		}
		

		List<NetValue> netValues=new ArrayList<NetValue>();
	
		List<WebElement> rows=contentDiv.findElements(By.xpath("//table[@class='el-table__body']/tbody/tr"));
        for(WebElement row : rows) {
        	List<WebElement> cols=row.findElements(By.xpath("./td/div"));
			String rpt_date = cols.get(0).getText();
        	Double net_value = Double.parseDouble(cols.get(1).getText());
            if (rpt_date.compareTo(last_sync_date)>0) {
    			NetValue onerow = new NetValue(code, rpt_date, net_value);
    			netValues.add(onerow);
    			//System.out.println(rpt_date + "  " + net_value);
            }else
                break;
        }         
		Collections.reverse(netValues);
	
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
		return "cmbwm";//招银
	}

	public static void main(String[] args) {
		try(CmbwmLoader boc=new CmbwmLoader()){
			boc.refreshCatalog();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("----招银理财 jobs done!----");
	}

}
