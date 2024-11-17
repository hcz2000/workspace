package com.jams.finance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlButton;


public class CgbwmLoader extends BaseLoader{
	private WebClient webClient;
	
	public CgbwmLoader() {
        super();
        webClient = new WebClient(BrowserVersion.FIREFOX);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setTimeout(10000);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.setCssErrorHandler(new SilentCssErrorHandler());
		webClient.getCache().setMaxSize(200);
		webClient.getOptions().setHistorySizeLimit(30);
		Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.SEVERE);
	}
	
	public List<NetValue> fetchUpdate(String code,String url,String value_type) throws Exception{
		NetValue lastRecord=getLastRecord(code);
        String last_sync_date = lastRecord.getDate();
        double last_net_value = lastRecord.getValue();
        System.out.println("  "+last_sync_date+":"+last_net_value);
        HtmlPage page = webClient.getPage(url);
        webClient.waitForBackgroundJavaScript(5000);
        
        List<HtmlElement> menus = page.getByXPath("//li[@class='parentMenuItem']/span");
        for(HtmlElement menu : menus) {
        	if("信息披露".equals(menu.getVisibleText())){
                if(menu.getAttribute("class").contains("has-child-down")) {
                    menu.click();
                    webClient.waitForBackgroundJavaScript(2000);
                }
        	}
        }
        
        List<HtmlElement> sub_menus = page.getByXPath("//li[@class='childMenuItem']/span");
        for(HtmlElement sub_menu : sub_menus) {
            if("产品公告搜索".equals(sub_menu.getVisibleText())) {
                  if(sub_menu.getAttribute("class").contains("has-child-up2")) {
                       sub_menu.click();
                       webClient.waitForBackgroundJavaScript(5000);
                  }else{
                       sub_menu.click();
                       webClient.waitForBackgroundJavaScript(5000);
                       sub_menu.click();
                       webClient.waitForBackgroundJavaScript(5000);
                  }
                  break;
            }
        }  


        
        HtmlTextInput search_input = page.getFirstByXPath("//input[@class='el-input__inner']");
        HtmlButton search_button = page.getFirstByXPath("//div[@class='el-input-group__append']/button");
        search_input.reset();
        search_input.type(code);
        search_button.click();
        webClient.waitForBackgroundJavaScript(2000);

		List<NetValue> netValues=new ArrayList<NetValue>();
        List<HtmlDivision> outputList = page.getByXPath("//div[@class='outList']");
        HtmlDivision newest_report = outputList.get(0);
        String newest_release_date = ((HtmlDivision)newest_report.getFirstByXPath("//div[@class='myDate']")).getVisibleText();
        if(last_sync_date.compareTo(newest_release_date)>0) {
             System.out.println("No update");
             return netValues;
        }
		//System.out.println("----"+outputList.get(0).asXml());

		
        while(true) {
        	for(HtmlDivision row: outputList) {
        		String title=((HtmlSpan)row.getFirstByXPath("./div[@class='myTitleTwo']/span[1]")).getVisibleText();
                String catalog=((HtmlSpan)row.getFirstByXPath("./div[@class='myTitleTwo']/span[2]")).getVisibleText();
                System.out.println(title);
                if(!catalog.startsWith("净值公告"))
                    continue;
                String release_date=((HtmlDivision)row.getFirstByXPath("//div[@class='myDate']")).getVisibleText();
                if(release_date.compareTo(last_sync_date)>=0) {
                	row.click();
                    webClient.waitForBackgroundJavaScript(1000);
                    List<HtmlElement> cols=page.getByXPath("//div[@id='news_content_id']/table/tbody/tr[2]/td/span");
           	        Double net_value=Double.parseDouble(cols.get(4).getVisibleText());
                    String rpt_date=cols.get(6).getVisibleText();
                    System.out.println(rpt_date+"-"+net_value);
                    if(rpt_date.compareTo(last_sync_date)>0){
                    	System.out.println(rpt_date+"-"+net_value);
                    	NetValue onerow=new NetValue(code,rpt_date,net_value);
                        netValues.add(onerow);
                    }
                    HtmlDivision back=page.getFirstByXPath("//div[@class='headBack']");
                    back.click();
                    webClient.waitForBackgroundJavaScript(2000);
                }else {
                	break;
                }
        	}
        	
        	HtmlDivision oldest_report = outputList.get(outputList.size()-1);
        	//System.out.println(oldest_report.asXml());
            String oldest_release_date = ((HtmlDivision)oldest_report.getFirstByXPath("//div[@class='myDate']")).getVisibleText();
            if(oldest_release_date.compareTo(last_sync_date)>=0) {
            	HtmlButton next_button= page.getFirstByXPath("//button[@class='btn-next']");
                if(!next_button.isDisabled()) {
                    System.out.println("Now "+oldest_release_date+">last_sync_date("+last_sync_date+"), Next page ...");
                	next_button.click();
                    webClient.waitForBackgroundJavaScript(5000);
                    outputList = page.getByXPath("//div[@class='outList']");
                    continue;
                }
            }else            
            	break;
            
        }
        
        List<NetValue> returnValues=new ArrayList<NetValue>();
        for(int i=netValues.size()-1;i>=0;i--)
        	returnValues.add(netValues.get(i));
        
        return returnValues;

	}

	@Override
	public void close() throws Exception{
		if(webClient!=null) {
			webClient.close();
		}
		super.close();
	}

	@Override
	public String getCatalog() {
		return "广银理财";
	}

	public static void main(String[] args) {
		try(CgbwmLoader boc=new CgbwmLoader()){
			boc.refreshCatalog();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
