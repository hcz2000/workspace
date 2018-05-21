package com.jams.weixin.qy.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jams.weixin.core.Token;
import com.jams.weixin.qy.pojo.UserInfo;

/**
 * �߼��ӿڹ�����
 * 
 * @author HCZ
 * @date 2015-11-9
 */
public class ComUtil {
	private static Logger log = LoggerFactory.getLogger(ComUtil.class);
	

	/**
	 * ����https����
	 * 
	 * @param requestUrl
	 *            �����ַ
	 * @param requestMethod
	 *            ����ʽ��GET��POST��
	 * @param outputStr
	 *            �ύ������
	 * @return JSONObject(ͨ��JSONObject.get(key)�ķ�ʽ��ȡjson���������ֵ)
	 */
	public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		try {
			// ����SSLContext���󣬲�ʹ������ָ�������ι�������ʼ��
			TrustManager myTrustManager= new X509TrustManager(){
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				// ����������֤��
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				// ���������ε�X509֤������
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				
			};
			TrustManager[] tm = { myTrustManager };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// ������SSLContext�����еõ�SSLSocketFactory����
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// ��������ʽ��GET/POST��
			conn.setRequestMethod(requestMethod);

			// ��outputStr��Ϊnullʱ�������д����
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// ע������ʽ
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// ����������ȡ��������
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			// �ͷ���Դ
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("���ӳ�ʱ��{}", ce);
		} catch (Exception e) {
			log.error("https�����쳣��{}", e);
		}
		return jsonObject;
	}

	/**
	 * ��ȡ�ӿڷ���ƾ֤
	 * 
	 * @param corpid
	 * @param corpsecret
	 * @return
	 */
	public static Token getToken(String corpid, String corpsecret) {
		String token_url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=CORPID&corpsecret=CORPSECRET";

		Token token = null;
		String requestUrl = token_url.replace("CORPID", corpid).replace("CORPSECRET", corpsecret);
		// ����GET�����ȡƾ֤
		JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);

		if (null != jsonObject) {
			try {
				token = new Token();
				token.setAccessToken(jsonObject.getString("access_token"));
				token.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				token = null;
				// ��ȡtokenʧ��
				log.error("��ȡtokenʧ�� errcode:{} errmsg:{}", jsonObject.getInt("errcode"),
						jsonObject.getString("errmsg"));
			}
		}
		return token;
	}

	/**
	 * ��ȡ�ӿڷ���ƾ֤
	 * @param access_token
	 * @param agentId  OAuth��Ȩ�ص�state�ֶΣ�agentId��
	 * @param code   OAuth��Ȩ�ص� code
	 * @return
	 */
	
	public static UserInfo getOAuthUser(String access_token, String code) {
		String userId = "";
		String deviceId="";
		String userTicket="";
		UserInfo userInfo = null;		
		String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", access_token).replace("CODE", code);
		JSONObject jsonObject = ComUtil.httpsRequest(requestUrl, "GET", null);
		log.debug("MSG:" + jsonObject);

		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if (errorCode==0) {
				userId = jsonObject.getString("UserId");
				deviceId = jsonObject.getString("DeviceId");
				userTicket= jsonObject.getString("user_ticket");
				log.debug("userID:" + userId);
				log.debug("deviceID:" + deviceId);
				userInfo=getUserInfo(access_token,userTicket);
			}else
				log.error("��ȡ�û� IDʧ�� errcode:{} errmsg:{}", errorCode, errorMsg);
		} else {
			log.error("Get userID failed��������");
		}
		return userInfo;
	}
	
	public static UserInfo getUserInfo(String accessToken, String userTicket) {
		UserInfo userInfo = null;
		
		String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserdetail?access_token=ACCESS_TOKEN";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);

		String jsonMsg = String.format("{\"user_ticket\":\"%s\"}",userTicket );

		JSONObject jsonObject = ComUtil.httpsRequest(requestUrl, "POST", jsonMsg);
		
		System.out.println("USERINFO:" + jsonObject);

		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if (errorCode == 0) {
				userInfo = new UserInfo();
				userInfo.setUserId(jsonObject.getString("userid"));
				userInfo.setName(jsonObject.getString("name"));
				userInfo.setDepartment(	JSONArray.toList(jsonObject.getJSONArray("department"), List.class));
				if (jsonObject.containsKey("position")) {
					userInfo.setPosition(jsonObject.getString("position"));
				}
				if (jsonObject.containsKey("mobile")) {
					userInfo.setMobile(jsonObject.getString("mobile"));
				}
				if (jsonObject.containsKey("email")) {
					userInfo.setEmail(jsonObject.getString("email"));
				}
				if (jsonObject.containsKey("weixinid")) {
					userInfo.setWeixinId(jsonObject.getString("weixinid"));
				}
				if (jsonObject.containsKey("gender")) {
					userInfo.setGender(jsonObject.getString("gender"));
				}
			} else {
				log.error("��ȡuserʧ�� errcode:{} errmsg:{}", errorCode, errorMsg);
			}
		}
		return userInfo;
	}
	
	public static void main(String args[]) {

		Token myToken = getToken(com.jams.weixin.qy.myapp.Constants.CORPID, com.jams.weixin.qy.myapp.Constants.CORPSECRECT);
		System.out.println(myToken.getExpiresIn());
		System.out.println(myToken.getAccessToken());
		
	}
}
