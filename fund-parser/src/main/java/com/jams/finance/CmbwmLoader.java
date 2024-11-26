package com.jams.finance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.htmlunit.SilentCssErrorHandler;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;


public class CmbwmLoader extends BaseLoader {
	private WebClient webClient;

	public CmbwmLoader() {
		super();
		//webClient = new WebClient(BrowserVersion.FIREFOX);
		webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setTimeout(10000);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.setCssErrorHandler(new SilentCssErrorHandler());
		webClient.setJavaScriptErrorListener(new MyJavaScriptErrorListener());
		webClient.getCache().setMaxSize(200);
		webClient.getOptions().setHistorySizeLimit(30);
		Logger.getLogger("org.htmlunit").setLevel(Level.SEVERE);
	}

	public List<NetValue> fetchUpdate(String code, String url, String value_type) throws Exception {
		NetValue lastRecord = getLastRecord(code);
		String last_sync_date = lastRecord.getDate();
		double last_net_value = lastRecord.getValue();
		System.out.println("##" + last_sync_date + "  " + last_net_value);
		System.out.println(url+"?prodTradeCode="+code+"&prodClcMode=01&finType=P");
		HtmlPage page=webClient.getPage(url+"?prodTradeCode="+code+"&prodClcMode=01&finType=P");
		webClient.waitForBackgroundJavaScript(2000);

		HtmlDivision contentDiv=page.getFirstByXPath("//div[@class='proTabList']");
		
		System.out.println(contentDiv.asXml());
		List<HtmlElement> menuList = contentDiv.getByXPath("//div[@class='item text-14 flex']");
		System.out.println(menuList.size());
		for (HtmlElement menu : menuList) {
			if ("产品净值".equals(menu.getVisibleText())) {
				menu.click();
				webClient.waitForBackgroundJavaScript(1000);
				HtmlElement sub_menu = page.getFirstByXPath("//div[@class='proValue_end proValue_btn text-14']");
				sub_menu.click();
				webClient.waitForBackgroundJavaScript(2000);
			}
		}
		

		List<NetValue> netvalues = new ArrayList<NetValue>();
		
        List<HtmlElement> rows=contentDiv.getByXPath("//table[@class='el-table__body']/tbody/tr");
        for(HtmlElement row : rows) {
        	List<HtmlElement> cols=row.getByXPath("./td/div");
			String rpt_date = cols.get(0).getVisibleText();
        	Double net_value = Double.parseDouble(cols.get(1).getVisibleText());

            if (rpt_date.compareTo(last_sync_date)>0) {
    			NetValue onerow = new NetValue(code, rpt_date, net_value);
    			netvalues.add(onerow);
    			//System.out.println(rpt_date + "  " + net_value);
            }else
                break;
        }         
       
        Collections.reverse(netvalues);
		return netvalues;

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
		return "cmbwm";//招银
	}

	public static void main(String[] args) {
		try (CmbwmLoader boc = new CmbwmLoader()) {
			boc.refreshCatalog();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
