package com.jams.weixin.qy.myapp;

public class Constants {
	//微信企业号定义的企业ID
	public static String CORPID = "wx8042b22041aa4f39";
	//微信企业号定义的管理组的Secrect
	public static String CORPSECRECT = "QSuWeKJxafY8g6e-tewlg0mYVJ15n0wPRQAnf57joQ1r9nKZm1G80u8adY7twdQA";
	//微信企业号定义的企业应用的ID
	public static String AGENTID="1";
	//微信企业号定义的企业应用的OAUTH回调URL
	public static String OAUTH_REDIRECT_URL="http://hczapp.applinzi.com/oauthServlet";
	//微信企业号针对每个启用API接收的的应用所定义的TOKEN,用于公众平台服务器与企业公众帐号服务器之间认证(指定应用)
	public static String TOKEN = "wechattoken";
	//微信企业号针对每个启用API接收的的应用所定义的 EncodingAESKey,用于公众平台服务器与企业公众帐号服务器之间报文加密(指定应用)
	public static String ENCODINGAESKEY = "275WzITX1SujuzUhxim11ypGBblzCQf3A4OSKA4grvS";
}
