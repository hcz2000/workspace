package com.jams.finance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.htmlunit.BrowserVersion;
import org.htmlunit.SilentCssErrorHandler;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlSpan;
import org.htmlunit.html.HtmlTextInput;
import org.htmlunit.html.HtmlButton;

public class CgbwmLoader extends BaseLoader {
	private WebClient webClient;

	public CgbwmLoader() {
		super();
		webClient = new WebClient(BrowserVersion.FIREFOX);
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
		HtmlPage page = webClient.getPage(url);
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
		for (int i = reverseOrderedValues.size() - 1; i >= 0; i--)
			returnValues.add(reverseOrderedValues.get(i));

		return returnValues;

	}

	private List<NetValue> getOnePage(HtmlPage page, String code, int pageNo) throws IOException {
		List<HtmlElement> menus = page.getByXPath("//li[@class='parentMenuItem']/span");
		for (HtmlElement menu : menus) {
			if ("信息披露".equals(menu.getVisibleText())) {
				if (menu.getAttribute("class").contains("has-child-down")) {
					menu.click();
					webClient.waitForBackgroundJavaScript(1000);
				}
			}
		}

		List<HtmlElement> sub_menus = page.getByXPath("//li[@class='childMenuItem']/span");
		for (HtmlElement sub_menu : sub_menus) {
			if ("产品公告搜索".equals(sub_menu.getVisibleText())) {
				if (sub_menu.getAttribute("class").contains("has-child-up2")) {
					sub_menu.click();
					webClient.waitForBackgroundJavaScript(2000);
				} else {
					sub_menu.click();
					webClient.waitForBackgroundJavaScript(1000);
					sub_menu.click();
					webClient.waitForBackgroundJavaScript(1000);
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

		for (int i = pageNo; i > 1; i--) {
			HtmlButton next_button = page.getFirstByXPath("//button[@class='btn-next']");
			if (!next_button.isDisabled()) {
				next_button.click();
				webClient.waitForBackgroundJavaScript(2000);
			}
		}

		List<NetValue> netValues = new ArrayList<NetValue>();
		List<HtmlDivision> outputList = page.getByXPath("//div[@class='outList']");
		
		for (int cnt=0;cnt<outputList.size();cnt++) {
			HtmlDivision row=outputList.get(cnt);
			String title = ((HtmlSpan) row.getFirstByXPath("./div[@class='myTitleTwo']/span[1]")).getVisibleText();
			String catalog = ((HtmlSpan) row.getFirstByXPath("./div[@class='myTitleTwo']/span[2]")).getVisibleText();
			System.out.println(title);
			if (!catalog.startsWith("净值公告"))
				continue;

			row.click();
			webClient.waitForBackgroundJavaScript(1000);
			List<HtmlElement> cols = page.getByXPath("//div[@id='news_content_id']/table/tbody/tr[2]/td/span");
			if(cols.size()>0) {
				Double net_value = Double.parseDouble(cols.get(4).getVisibleText());
				String rpt_date = cols.get(6).getVisibleText();
				System.out.println(rpt_date + "  " + net_value);
				NetValue onerow = new NetValue(code, rpt_date, net_value);
				netValues.add(onerow);
			}else {
				throw new DataException("Data Error");
			}
			webClient.getWebWindows().get(0).getHistory().back();
			webClient.waitForBackgroundJavaScript(2000);
			outputList = page.getByXPath("//div[@class='outList']");
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
		return "cgbwm";//广发
	}

	public static void main(String[] args) {
		try (CgbwmLoader boc = new CgbwmLoader()) {
			boc.refreshCatalog();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
