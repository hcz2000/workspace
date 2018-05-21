package com.jams.weixin.qy.myapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

/**
 * ������������
 * 
 * @author HCZ
 * 
 */
public class CoreServlet extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;

	/**
	 * ȷ����������΢�ŷ�����
	 * 
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// ΢�ż���ǩ��
		String sVerifyMsgSig = request.getParameter("msg_signature");
		// ʱ���
		String sVerifyTimeStamp = request.getParameter("timestamp");
		// �����
		String sVerifyNonce = request.getParameter("nonce");
		// ����ַ���
		String sVerifyEchoStr = request.getParameter("echostr");
		String sEchoStr; // ��Ҫ���ص�����
		PrintWriter out = response.getWriter();
		WXBizMsgCrypt wxcpt;
		try {
			wxcpt = new WXBizMsgCrypt(Constants.TOKEN, Constants.ENCODINGAESKEY, Constants.CORPID);
			sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sVerifyEchoStr);
			// ��֤URL�ɹ�����sEchoStr����
			out.print(sEchoStr);
		} catch (AesException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * ����΢�ŷ�������������Ϣ
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// ΢�ż���ǩ��
		String signature = request.getParameter("msg_signature");
		// ʱ���
		String timestamp = request.getParameter("timestamp");
		// �����
		String nonce = request.getParameter("nonce");

		InputStream is = request.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		String buffer = null;
		StringBuffer sb = new StringBuffer();
		while ((buffer = br.readLine()) != null) {
			sb.append(buffer);
		}
		String notifyMessage = sb.toString();

		try {
			WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(Constants.TOKEN, Constants.ENCODINGAESKEY, Constants.CORPID);
			String msg = wxcpt.DecryptMsg(signature, timestamp, nonce, notifyMessage);
			System.out.println(msg);
			String responseMsg = process(msg);
			String encryptedMsg = wxcpt.EncryptMsg(responseMsg, timestamp, nonce);
			// System.out.println(encryptedMsg);
			PrintWriter out = response.getWriter();
			out.print(encryptedMsg);
			out.close();
		} catch (AesException e) {
			e.printStackTrace();
		}
	}

	private String process(String xml) {

		@SuppressWarnings("unchecked")
		Map<String, String> map = parseXml(xml);
		String msgType = map.get("MsgType");
		if (msgType.equals("text")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String times = format.format(new Date());
			Document document = DocumentHelper.createDocument(); // ����document����
			Element root = document.addElement("xml");
			root.addElement("ToUserName").addCDATA(map.get("FromUserName"));
			;
			root.addElement("FromUserName").addCDATA(map.get("ToUserName"));
			root.addElement("CreateTime").setText(times);
			root.addElement("MsgType").addCDATA("text");
			root.addElement("Content").addCDATA("Your Msg:" + map.get("Content"));
			String result = document.asXML();
			System.out.println(result);
			return result;
		}
		// ͼƬ��Ϣ
		else if (msgType.equals("image")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String times = format.format(new Date());
			Document document = DocumentHelper.createDocument(); // ����document����
			Element root = document.addElement("xml");
			root.addElement("ToUserName").addCDATA(map.get("FromUserName"));
			;
			root.addElement("FromUserName").addCDATA(map.get("ToUserName"));
			root.addElement("CreateTime").setText(times);
			root.addElement("MsgType").addCDATA("image");
			root.addElement("Image").addElement("MediaId").addCDATA(map.get("MediaId"));

			String result = document.asXML();
			System.out.println(result);
			return result;
		}
		// ������Ϣ
		else if (msgType.equals("voice")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String times = format.format(new Date());
			Document document = DocumentHelper.createDocument(); // ����document����
			Element root = document.addElement("xml");
			root.addElement("ToUserName").addCDATA(map.get("FromUserName"));
			;
			root.addElement("FromUserName").addCDATA(map.get("ToUserName"));
			root.addElement("CreateTime").setText(times);
			root.addElement("MsgType").addCDATA("voice");
			root.addElement("Voice").addElement("MediaId").addCDATA(map.get("MediaId"));
			String result = document.asXML();
			System.out.println(result);
			return result;
		}
		// ��Ƶ��Ϣ
		else if (msgType.equals("video")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String times = format.format(new Date());
			Document document = DocumentHelper.createDocument(); // ����document����
			Element root = document.addElement("xml");
			root.addElement("ToUserName").addCDATA(map.get("FromUserName"));
			;
			root.addElement("FromUserName").addCDATA(map.get("ToUserName"));
			root.addElement("CreateTime").setText(times);
			root.addElement("MsgType").addCDATA("video");
			Element video = root.addElement("Video");
			video.addElement("MediaId").addCDATA(map.get("MediaId"));
			video.addElement("Title").addCDATA("my video");
			video.addElement("Description").addCDATA("my video description");
			String result = document.asXML();
			// System.out.println(result);
			return result;
		}
		// ����λ��
		else if (msgType.equals("location")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String times = format.format(new Date());
			Document document = DocumentHelper.createDocument(); // ����document����
			Element root = document.addElement("xml");
			root.addElement("ToUserName").addCDATA(map.get("FromUserName"));
			;
			root.addElement("FromUserName").addCDATA(map.get("ToUserName"));
			root.addElement("CreateTime").setText(times);
			root.addElement("MsgType").addCDATA("text");
			root.addElement("Content").addCDATA("Your location:" + map.get("Location_X") + " " + map.get("Location_Y")
					+ " Label:" + map.get("Label"));
			String result = document.asXML();
			// System.out.println(result);
			return result;
		}
		// �¼�����
		else if (msgType.equals("event")) {
			String eventType = map.get("Event");
			if (eventType.equals("click")) {
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				String times = format.format(new Date());
				Document document = DocumentHelper.createDocument(); // ����document����
				Element root = document.addElement("xml");
				root.addElement("ToUserName").addCDATA(map.get("FromUserName"));
				;
				root.addElement("FromUserName").addCDATA(map.get("ToUserName"));
				root.addElement("CreateTime").setText(times);
				root.addElement("MsgType").addCDATA("text");
				if ("1".equals(map.get("EventKey")))
					root.addElement("Content").addCDATA("�������Ų���");
				else if ("2".equals(map.get("EventKey")))
					root.addElement("Content").addCDATA("�������Ų���");
				else {
					try {
						String encoded_url = URLEncoder.encode(Constants.OAUTH_REDIRECT_URL,
								StandardCharsets.UTF_8.name());
						String redirectToApp = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constants.CORPID
								+ "&redirect_uri=" + encoded_url + "&response_type=code&scope=snsapi_userinfo&agentid="
								+ Constants.AGENTID + "&state=sunlight#wechat_redirect";
						/*
						root.addElement("Content").addCDATA(
								"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8042b22041aa4f39&redirect_uri=http%3A%2F%2Fhczapp.applinzi.com%2FoauthServlet&response_type=code&scope=snsapi_userinfo&agentid=1&state=sunlight#wechat_redirect");
						*/
						root.addElement("Content").addCDATA(redirectToApp);
					} catch (UnsupportedEncodingException e) {
						root.addElement("Content").addCDATA("URL��������");
					}
				}
				String result = document.asXML();
				System.out.println(result);
				return result;
			}
		}
		return null;
	}

	private Map parseXml(String xml) {
		Map<String, String> map = new HashMap<String, String>();

		// ��ȡ������
		Document document = null;
		try {
			document = DocumentHelper.parseText(xml);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		// �õ�xml��Ԫ��
		Element root = document.getRootElement();

		// �õ���Ԫ�ص������ӽڵ�
		List<Element> elementList = root.elements();
		// ���������ӽڵ�
		for (Element e : elementList) {
			// ����CDATA�����ڵ����ݣ�XML�������򲻻ᴦ������ֱ��ԭ�ⲻ���������
			map.put(e.getName(), e.getText());
		}
		return map;
	}

}