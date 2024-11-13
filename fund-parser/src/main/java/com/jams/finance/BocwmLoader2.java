package com.jams.finance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;


public class BocwmLoader2 extends BaseLoader{
	private WebClient webClient;
	
	public BocwmLoader2() {
        super();
	}
	
	public List<NetValue> fetchUpdate(String code,String url,String value_type) throws Exception{
		NetValue lastRecord=getLastRecord(code);
        String last_sync_date = lastRecord.getDate();
        double last_net_value = lastRecord.getValue();
        System.out.println("  "+last_sync_date+":"+last_net_value);
        HtmlPage page = webClient.getPage(url);
        HtmlDivision contentDiv=(HtmlDivision) page.getHtmlElementById("content");
		HtmlTable table = contentDiv.getFirstByXPath("//table[@class='layui-table']");

		List<NetValue> netValues=new ArrayList<NetValue>();
		if("net_value".equals(value_type)){
			 for(int i=1,rowcount=table.getRowCount();i<rowcount;i++) {
				 HtmlTableRow row = table.getRow(i);
				 String rpt_date=row.getCell(6).getVisibleText();
			     double net_value=Double.parseDouble(row.getCell(2).getVisibleText());
			     if(rpt_date.compareTo(last_sync_date)<=0)
			    	 break;
			    	 
			     netValues.add(new NetValue(code,rpt_date,net_value));
			 }
			 Collections.reverse(netValues);
		}else {
			 List<Revenue> revenues=new ArrayList<Revenue>();
			 for(int i=1,rowcount=table.getRowCount();i<rowcount;i++) {
				 HtmlTableRow row = table.getRow(i);
				 String rpt_date=row.getCell(6).getVisibleText();
			     double revenue = Double.parseDouble(row.getCell(6).getVisibleText())/10000;
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

	public static void main(String[] args) {
			
		BocwmLoader2 boc=new BocwmLoader2();

		boc.refreshCatalog();
		/*
		List<NetValue> netValues=boc.fetchUpdate("HXTT01",
					"https://www.bocwm.cn/html/1/netWorth/4114.html",
					"revenue");
		
		for(NetValue item: netValues)
		     System.out.println(item.getCode()+":"+item.getDate()+"--"+String.valueOf(item.getValue()));
		     */
	}

	@Override
	public void preFetch() {
		webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.setCssErrorHandler(new SilentCssErrorHandler());
	}

	@Override
	public void postFetch() {
		if(webClient!=null) {
			webClient.close();
		}
	}

	@Override
	public String getCatalog() {
		return "中银理财";
	}



}
