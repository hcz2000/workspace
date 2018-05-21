package com.jams.weixin.qy.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jams.weixin.core.Media;

/**
 * �߼��ӿڹ�����
 * 
 * @author HCZ
 * @date 2015-11-9
 */
public class MessageUtil {
	private static Logger log = LoggerFactory.getLogger(MessageUtil.class);
	
	
	private static String makeTextMessage(String agentId, String userId, String content) {
		// ����Ϣ�����е�˫���Ž���ת��
		content = content.replace("\"", "\\\"");
		String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"text\",\"agentid\":\"%s\",\"text\":{\"content\":\"%s\"}}";
		return String.format(jsonMsg, userId, agentId, content);
	}
	
	 private static String makeImageMessage(String agentId, String userId, String mediaId) {  
		 String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"image\",\"agentid\":\"%s\",\"image\":{\"media_id\":\"%s\"}}";
		 return String.format(jsonMsg, userId, agentId, mediaId);
	 }  
	 
	 private static String makeVoiceMessage(String agentId, String userId, String mediaId) {  
		 String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"voice\",\"agentid\":\"%s\",\"voice\":{\"media_id\":\"%s\"}}";
		 return String.format(jsonMsg, userId, agentId, mediaId);
	 }
	 
	 private static String makeVideoMessage(String agentId, String userId, String mediaId) {  
		 String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"video\",\"agentid\":\"%s\",\"video\":{\"media_id\":\"%s\"}}";
		 return String.format(jsonMsg, userId, agentId, mediaId);
	 }
	 
	 private static String makeFileMessage(String agentId, String userId, String mediaId) {  
		 String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"file\",\"agentid\":\"%s\",\"file\":{\"media_id\":\"%s\"}}";
		 return String.format(jsonMsg, userId, agentId, mediaId);
	 }
	 
	 
	 private static void sendMessage(String agentId, String accessToken, String userId, String message){
			// ƴ�������ַ
			String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";
			requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
			// ���Ϳͷ���Ϣ
			JSONObject jsonObject = ComUtil.httpsRequest(requestUrl, "POST", message);

			if (null != jsonObject) {
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				if (0 == errorCode) {
					log.info("�ͷ���Ϣ���ͳɹ� errcode:{} errmsg:{}", errorCode, errorMsg);
				} else {
					log.error("�ͷ���Ϣ����ʧ�� errcode:{} errmsg:{}", errorCode, errorMsg);
				}
			}

	 }

	public static void sendText(String agentId, String accessToken, String userId, String content) {
		String jsonMsg = makeTextMessage(agentId, userId, content);
		log.info("��Ϣ���ݣ�{}", jsonMsg);
		sendMessage(agentId,accessToken,userId,jsonMsg);
	}
	
	public static void sendImage(String agentId, String accessToken, String userId, String mediaId) {
		String jsonMsg = makeImageMessage(agentId, userId, mediaId);
		log.info("��Ϣ���ݣ�{}", jsonMsg);
		sendMessage(agentId,accessToken,userId,jsonMsg);
	}
	
	public static void sendVoice(String agentId, String accessToken, String userId, String mediaId) {
		String jsonMsg = makeVoiceMessage(agentId, userId, mediaId);
		log.info("��Ϣ���ݣ�{}", jsonMsg);
		sendMessage(agentId,accessToken,userId,jsonMsg);
	}
	
	public static void sendVideo(String agentId, String accessToken, String userId, String mediaId) {
		String jsonMsg = makeVideoMessage(agentId, userId, mediaId);
		log.info("��Ϣ���ݣ�{}", jsonMsg);
		sendMessage(agentId,accessToken,userId,jsonMsg);
	}
	
	public static void sendFile(String agentId, String accessToken, String userId, String mediaId) {
		String jsonMsg = makeFileMessage(agentId, userId, mediaId);
		log.info("��Ϣ���ݣ�{}", jsonMsg);
		sendMessage(agentId,accessToken,userId,jsonMsg);
	}
	
	
	private static String getFileExt(String contentType) {
		String fileExt = "";
		if ("image/jpeg".equals(contentType))
			fileExt = ".jpg";
		else if ("audio/mpeg".equals(contentType))
			fileExt = ".mp3";
		else if ("audio/amr".equals(contentType))
			fileExt = ".amr";
		else if ("video/mp4".equals(contentType))
			fileExt = ".mp4";
		else if ("video/mpeg4".equals(contentType))
			fileExt = ".mp4";
		return fileExt;
	}
	
	
	public static Media uploadMedia(String accessToken, String type, File file) {
		Media media = null;
		// ƴװ�����ַ
		String uploadMediaUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
		uploadMediaUrl = uploadMediaUrl.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);

		// �������ݷָ���
		String boundary = "----------sunlight";
		try {
			URL uploadUrl = new URL(uploadMediaUrl);
			HttpURLConnection uploadConn = (HttpURLConnection) uploadUrl.openConnection();
			uploadConn.setDoOutput(true);
			uploadConn.setDoInput(true);
			uploadConn.setRequestMethod("POST");
			// ��������ͷContent-Type
			uploadConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			// ��ȡý���ļ��ϴ������������΢�ŷ�����д���ݣ�
			OutputStream outputStream = uploadConn.getOutputStream();

			

			String contentType =  "application/octet-stream";
			
			outputStream.write(("--" + boundary + "\r\n").getBytes());
			outputStream.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"%s\"\r\n", file.getName()).getBytes());
			outputStream.write(String.format("Content-Type: %s\r\n\r\n", contentType).getBytes());
			
			
			// ��ȡý���ļ�������������ȡ�ļ���
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = in.read(buf)) != -1) {
				// ��ý���ļ�д�����������΢�ŷ�����д���ݣ�
				outputStream.write(buf, 0, size);
			}
			// ���������
			outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
			outputStream.close();
			in.close();

			// ��ȡý���ļ��ϴ�������������΢�ŷ����������ݣ�
			InputStream inputStream = uploadConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// �ͷ���Դ
			inputStream.close();
			inputStream = null;
			uploadConn.disconnect();

			// ʹ��JSON-lib�������ؽ��
			JSONObject jsonObject = JSONObject.fromObject(buffer.toString());
			if (null != jsonObject) {
				log.debug(jsonObject.toString());
				media = new Media();
				media.setType(jsonObject.getString("type"));
				media.setMediaId(jsonObject.getString("media_id"));
				media.setCreatedAt(jsonObject.getInt("created_at"));
			}
		} catch (Exception e) {
			media = null;
			log.error("�ϴ�ý���ļ�ʧ�ܣ�{}", e);
		}
		return media;
	}

	/**
	 * ����ý���ļ�
	 * 
	 * @param accessToken �ӿڷ���ƾ֤
	 * @param mediaId ý���ļ���ʶ
	 * @param savePath �ļ��ڷ������ϵĴ洢·��
	 * @return
	 */
	public static String getMedia(String accessToken, String mediaId, String savePath) {
		String filePath = null;
		// ƴ�������ַ
		String requestUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
		System.out.println(requestUrl);
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");

			if (!savePath.endsWith("/")) {
				savePath += "/";
			}
			// �����������ͻ�ȡ��չ��
			String fileExt = getFileExt(conn.getHeaderField("Content-Type"));
			// ��mediaId��Ϊ�ļ���
			filePath = savePath + mediaId + fileExt;

			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);
			fos.close();
			bis.close();

			conn.disconnect();
			log.info("����ý���ļ��ɹ���filePath=" + filePath);
		} catch (Exception e) {
			filePath = null;
			log.error("����ý���ļ�ʧ�ܣ�{}", e);
		}
		return filePath;
	}
	

	

	
	public static void main(String args[]) {

		String accessToken = ComUtil.getToken(com.jams.weixin.qy.myapp.Constants.CORPID, com.jams.weixin.qy.myapp.Constants.CORPSECRECT).getAccessToken();
		
		
		sendText("1", accessToken, "hcz13825089890", "Hello HCZ");

		File file=new File("D:/tmp/penguins.jpg");
		Media media=uploadMedia(accessToken, "image", file);
		if(media!=null)
			sendImage("1",accessToken,"hcz13825089890",media.getMediaId());

		/*
		File file=new File("D:/tmp/qy.docx");
		Media media=uploadMedia(accessToken, "file", file);
		if(media!=null)
			sendFile("1",accessToken,"hcz13825089890",media.getMediaId());
		*/
 
		sendImage("1",accessToken,"hcz13825089890","1B6Aq3_Z6Ni4pph8Ysmi-MvYtNYWQwTQe7K92O3e3FYfaC6_fF8n6UwqkpLbzE5zmW7hkjxe8XSUT0mR3aHLIzw");
	}
}
