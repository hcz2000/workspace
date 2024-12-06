package com.jams.finance.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.htmlunit.html.HtmlPage;
import org.htmlunit.SilentCssErrorHandler;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlTable;
import org.htmlunit.html.HtmlTableBody;
import org.htmlunit.html.HtmlTableRow;

import com.jams.finance.data.NetValue;
import com.jams.finance.data.Revenue;


public class BocwmLoader1 extends BaseLoader{
	private WebClient webClient;
	
	public BocwmLoader1() {
        super();
        webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setTimeout(10000);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.setCssErrorHandler(new SilentCssErrorHandler());
		Logger.getLogger("org.htmlunit").setLevel(Level.SEVERE);
	}
	
	public List<NetValue> fetchUpdate(String code,String url,String value_type) throws Exception{
		NetValue lastRecord=getLastRecord(code);
        String last_sync_date = lastRecord.getDate();
        double last_net_value = lastRecord.getValue();
        //System.out.println("  "+last_sync_date+":"+last_net_value);
        HtmlPage page = webClient.getPage(url);
        webClient.waitForBackgroundJavaScript(5000);
        HtmlDivision contentDiv=(HtmlDivision)(page.getHtmlElementById("content"));
		HtmlTable table = contentDiv.getFirstByXPath("//table[@class='layui-table']");
		//System.out.println("----"+table.asXml());
		HtmlTableBody tbody=table.getBodies().get(0);
		List<NetValue> netValues=new ArrayList<NetValue>();
		
		if("net_value".equals(value_type)){
			 for(HtmlTableRow row: tbody.getRows()) {
				 String rpt_date=row.getCell(6).getVisibleText();
			     double net_value=Double.parseDouble(row.getCell(2).getVisibleText());
			     if(rpt_date.compareTo(last_sync_date)<=0)
			    	 break;
			     netValues.add(new NetValue(code,rpt_date,net_value));
			 }
			 Collections.reverse(netValues);
		}else {
			 List<Revenue> revenues=new ArrayList<Revenue>();
			 for(HtmlTableRow row: tbody.getRows()) {
				 String rpt_date=row.getCell(6).getVisibleText();
			     double revenue = Double.parseDouble(row.getCell(4).getVisibleText())/10000;
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
		if(webClient!=null) {
			webClient.close();
		}
		super.close();
	}

	@Override
	public String getVendor() {
		return "bocwm";//中银
	}

	public static void main(String[] args) {
		try(BocwmLoader1 boc=new BocwmLoader1()){
			boc.refreshCatalog();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
