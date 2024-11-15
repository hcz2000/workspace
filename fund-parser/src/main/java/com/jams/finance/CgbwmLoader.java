package com.jams.finance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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
        webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setTimeout(10000);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.setCssErrorHandler(new SilentCssErrorHandler());
		//Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.SEVERE);
		Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.ALL);
	}
	
	public List<NetValue> fetchUpdate(String code,String url,String value_type) throws Exception{
		NetValue lastRecord=getLastRecord(code);
        String last_sync_date = lastRecord.getDate();
        double last_net_value = lastRecord.getValue();
        //System.out.println("  "+last_sync_date+":"+last_net_value);
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
                       webClient.waitForBackgroundJavaScript(2000);
                  }else{
                       sub_menu.click();
                       webClient.waitForBackgroundJavaScript(2000);
                       sub_menu.click();
                       webClient.waitForBackgroundJavaScript(2000);
                  }
                  break;
            }
        }  

        //HtmlDivision outputDiv=page.getFirstByXPath("//div[@class='ui-flex is-vertical']");
        //System.out.println("-----------------before--------------------------\n"+outputDiv.asXml());
        
        HtmlTextInput search_input = page.getFirstByXPath("//input[@class='el-input__inner']");
        search_input.setText(code);
        HtmlButton search_button = page.getFirstByXPath("//div[@class='el-input-group__append']/button");
        search_button.click();
        webClient.waitForBackgroundJavaScript(5000);

        HtmlDivision outputDiv=page.getFirstByXPath("//div[@class='ui-flex is-vertical']");
        System.out.println("-----------------after--------------------------\n"+outputDiv.asXml());

		List<NetValue> netValues=new ArrayList<NetValue>();
        List<HtmlDivision> outputList = page.getByXPath("//div[@class='outList']");
        HtmlDivision newest_report = outputList.get(0);
        String newest_release_date = ((HtmlDivision)newest_report.getFirstByXPath("//div[@class='myDate']")).getVisibleText();
        if(last_sync_date.compareTo(newest_release_date)>=0) {
             System.out.println("No update");
             return netValues;
        }
		//System.out.println("----"+outputList.get(0).asXml());

		
        while(!outputList.isEmpty()) {
        	HtmlDivision oldest_report = outputList.get(outputList.size()-1);
        	//System.out.println(oldest_report.asXml());
            String oldest_release_date = ((HtmlDivision)oldest_report.getFirstByXPath("//div[@class='myDate']")).getVisibleText();
            if(oldest_release_date.compareTo(last_sync_date)>=0) {
            	HtmlButton next_button= page.getFirstByXPath("//button[@class='btn-next']");
                if(!next_button.isDisabled()) {
                	System.out.println(oldest_release_date+"------->Next page");
                    next_button.click();
                    webClient.waitForBackgroundJavaScript(5000);
                    outputList = page.getByXPath("//div[@class='outList']");
                    continue;
                }
            }
            break;
        }
        
        
        while(true) {
            for(int i = outputList.size()-1;i>=0;i--){
            	HtmlDivision row=outputList.get(i);
                String title=((HtmlDivision)row.getFirstByXPath("./div[@class='myTitleTwo']/span[1]")).getVisibleText();
                String catalog=((HtmlDivision)row.getFirstByXPath("./div[@class='myTitleTwo']/span[2]")).getVisibleText();
                if(!catalog.startsWith("净值公告"))
                    continue;
                String release_date=((HtmlDivision)row.getFirstByXPath("//div[@class='myDate']")).getVisibleText();
                if(release_date.compareTo(last_sync_date)>0) {
                    row.click();
                    webClient.waitForBackgroundJavaScript(1000);
                    List<HtmlElement> cols=page.getByXPath("//div[@id='news_content_id']/table/tbody/tr[2]/td/span");
           	        Double net_value=Double.parseDouble(cols.get(4).getVisibleText());
                    String rpt_date=cols.get(6).getVisibleText();
                    if(rpt_date.compareTo(last_sync_date)>0){
                    	NetValue onerow=new NetValue(code,rpt_date,net_value);
                        netValues.add(onerow);
                    }
                    page.getEnclosingWindow().getHistory().back();
                    webClient.waitForBackgroundJavaScript(2000);
                }
            }

            HtmlButton prev_button=page.getFirstByXPath("//button[@class='btn-prev']");
            if(!prev_button.isDisabled()){
                prev_button.click();
                //page.executeJavaScript("arguments[0].click()", prev_button)
                webClient.waitForBackgroundJavaScript(2000);
                outputList = page.getByXPath("//div[@class='outList']");
            }else
                break;
  
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
