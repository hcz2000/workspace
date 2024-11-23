package com.jams.finance;

import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

public class MyJavaScriptErrorListener implements JavaScriptErrorListener {

	@Override
	public void scriptException(HtmlPage page, ScriptException scriptException) {
		System.out.println("ScriptException: "+scriptException.getMessage());
		//scriptException.printStackTrace();
	}

	@Override
	public void timeoutError(HtmlPage page, long allowedTime, long executionTime) {
		System.out.println("Timeout");
	}

	@Override
	public void malformedScriptURL(HtmlPage page, String url, MalformedURLException malformedURLException) {
		System.out.println("MalformedURLException: "+malformedURLException.getMessage());
	}

	@Override
	public void loadScriptError(HtmlPage page, URL scriptUrl, Exception exception) {
		System.out.println(exception.getMessage());
	}

	@Override
	public void warn(String message, String sourceName, int line, String lineSource, int lineOffset) {
		// TODO Auto-generated method stub
		
	}

}
