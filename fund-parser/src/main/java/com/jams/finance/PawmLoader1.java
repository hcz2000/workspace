package com.jams.finance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.htmlunit.BrowserVersion;
import org.htmlunit.SilentCssErrorHandler;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlSpan;
import org.htmlunit.html.HtmlTextInput;
import org.htmlunit.javascript.SilentJavaScriptErrorListener;

public class PawmLoader1 extends BaseLoader {
	private WebClient webClient;

	public PawmLoader1() {
		super();
		//webClient = new WebClient(BrowserVersion.FIREFOX);
		webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setTimeout(10000);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.setCssErrorHandler(new SilentCssErrorHandler());
		webClient.setJavaScriptErrorListener(new MyJavaScriptErrorListener());
		webClient.getCache().setMaxSize(200);
		webClient.getOptions().setHistorySizeLimit(30);
		webClient.setScriptPreProcessor((htmlPage, sourceCode, sourceName, lineNumber, htmlElement) -> {
		    if (StringUtils.contains(sourceName, "index.js")) {
		        sourceCode = sourceCode.replace("Intl.Collator.supportedLocalesOf([\"zh-CN\"]).length", "1");
		    }
		    //if (StringUtils.contains(sourceName, "tools.js")) {
		    {
		    	sourceCode = sourceCode.replace("async ", StringUtils.EMPTY);
		    	sourceCode = sourceCode.replace("await ", StringUtils.EMPTY);
		    }
		    return sourceCode;
		});
		Logger.getLogger("org.htmlunit").setLevel(Level.SEVERE);
	}

	public List<NetValue> fetchUpdate(String code, String url, String value_type) throws Exception {
		NetValue lastRecord = getLastRecord(code);
		String last_sync_date = lastRecord.getDate();
		double last_net_value = lastRecord.getValue();
		System.out.println("##" + last_sync_date + "  " + last_net_value);
		System.out.println(url);
		HtmlPage page=webClient.getPage(url);
		webClient.waitForBackgroundJavaScript(2000);
		
		List<NetValue> reverseOrderedValues = new ArrayList<NetValue>();
		boolean stop = false;
		int pageNo = 1;
		while (!stop) {
			List<NetValue> pageValues = getOnePage(page, code, pageNo);
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
				double value=reverseOrderedValues.get(i).getValue();
				accumulated_revenue=accumulated_revenue+(1.0+accumulated_revenue)*value;
                returnValues.add(new NetValue(code,date, 1.0+accumulated_revenue));
			}else {
				returnValues.add(reverseOrderedValues.get(i));
			}
		}

		return returnValues;
	}
	
	private List<NetValue> getOnePage(HtmlPage page, String code, int pageNo) throws IOException,NoMoreDataException {
		webClient.waitForBackgroundJavaScript(1000);
		List<HtmlElement> menuList = page.getByXPath("//div[@role='tab']");
		System.out.println("pageNo:"+pageNo+"--"+page.getTitleText());
		for (HtmlElement menu : menuList) {
			System.out.print("Menu :"+menu.getVisibleText());
			if ("净值表现".equals(menu.getVisibleText())) {
				System.out.print("Clicking :"+menu.getVisibleText());
				menu.click();
				webClient.waitForBackgroundJavaScript(1000);
				break;
			}
		}
		
		for (int i = pageNo; i > 1; i--) {
			HtmlElement next=page.getFirstByXPath("//li[@title='下一页']");
			System.out.println(next.asXml());
			System.out.println("下一页");
			if(next.getAttribute("aria-disabled")==null) {
				next.click();
				webClient.waitForBackgroundJavaScript(2000);
			}else {
				throw new NoMoreDataException("End of Next page");
			}
			
		}


		List<NetValue> netValues = new ArrayList<NetValue>();
		
        List<HtmlElement> rows=page.getByXPath("//div[@class='ant-table-body']/table/tbody/tr");
        
		for (HtmlElement row : rows) {
			List<HtmlElement> cols=row.getByXPath("./td");
			String rpt_date = cols.get(1).getVisibleText();
        	Double net_value = Double.parseDouble(cols.get(2).getVisibleText());
			System.out.println(rpt_date+": "+net_value);
			NetValue onerow = new NetValue(code, rpt_date, net_value);
			netValues.add(onerow);
			//HtmlElement next=page.getFirstByXPath("//li[@title='下一页']");
			//next.click();
			//webClient.waitForBackgroundJavaScript(2000);
		}

		return netValues;
	}

	
	@Override
	public void close() throws Exception {
		if (webClient != null) {
			webClient.close();
		}
		super.close();
	}

	@Override
	public String getVendor() {
		return "pinganwm";//平安
	}
	
	@Override
	public void refreshCatalog() {
		for(ProductConfig cfg: config.values()) {
			if(!cfg.getCode().startsWith("PA"))
				continue;
			try {
				System.out.println ("-----Updating "+cfg.getCode());
				List<NetValue> netValues = fetchUpdate(cfg.getCode(),cfg.getUrl(),cfg.getType());
				for(NetValue item: netValues)
					System.out.println(item.getCode()+"  "+item.getDate()+"  "+String.valueOf(item.getValue()));
				write2DB(netValues);
			}catch(Exception e){
				System.out.println ("-----Update "+cfg.getCode()+ "Failed");
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try (PawmLoader1 boc = new PawmLoader1()) {
			boc.refreshCatalog();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
