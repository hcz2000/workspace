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
 * 高级接口工具类
 * 
 * @author HCZ
 * @date 2015-11-9
 */
public class MessageUtil {
	private static Logger log = LoggerFactory.getLogger(MessageUtil.class);
	
	
	private static String makeTextMessage(String agentId, String userId, String content) {
		// 对消息内容中的双引号进行转义
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
			// 拼接请求地址
			String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";
			requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
			// 发送客服消息
			JSONObject jsonObject = ComUtil.httpsRequest(requestUrl, "POST", message);

			if (null != jsonObject) {
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				if (0 == errorCode) {
					log.info("客服消息发送成功 errcode:{} errmsg:{}", errorCode, errorMsg);
				} else {
					log.error("客服消息发送失败 errcode:{} errmsg:{}", errorCode, errorMsg);
				}
			}

	 }

	public static void sendText(String agentId, String accessToken, String userId, String content) {
		String jsonMsg = makeTextMessage(agentId, userId, content);
		log.info("消息内容：{}", jsonMsg);
		sendMessage(agentId,accessToken,userId,jsonMsg);
	}
	
	public static void sendImage(String agentId, String accessToken, String userId, String mediaId) {
		String jsonMsg = makeImageMessage(agentId, userId, mediaId);
		log.info("消息内容：{}", jsonMsg);
		sendMessage(agentId,accessToken,userId,jsonMsg);
	}
	
	public static void sendVoice(String agentId, String accessToken, String userId, String mediaId) {
		String jsonMsg = makeVoiceMessage(agentId, userId, mediaId);
		log.info("消息内容：{}", jsonMsg);
		sendMessage(agentId,accessToken,userId,jsonMsg);
	}
	
	public static void sendVideo(String agentId, String accessToken, String userId, String mediaId) {
		String jsonMsg = makeVideoMessage(agentId, userId, mediaId);
		log.info("消息内容：{}", jsonMsg);
		sendMessage(agentId,accessToken,userId,jsonMsg);
	}
	
	public static void sendFile(String agentId, String accessToken, String userId, String mediaId) {
		String jsonMsg = makeFileMessage(agentId, userId, mediaId);
		log.info("消息内容：{}", jsonMsg);
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
		// 拼装请求地址
		String uploadMediaUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
		uploadMediaUrl = uploadMediaUrl.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);

		// 定义数据分隔符
		String boundary = "----------sunlight";
		try {
			URL uploadUrl = new URL(uploadMediaUrl);
			HttpURLConnection uploadConn = (HttpURLConnection) uploadUrl.openConnection();
			uploadConn.setDoOutput(true);
			uploadConn.setDoInput(true);
			uploadConn.setRequestMethod("POST");
			// 设置请求头Content-Type
			uploadConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			// 获取媒体文件上传的输出流（往微信服务器写数据）
			OutputStream outputStream = uploadConn.getOutputStream();

			

			String contentType =  "application/octet-stream";
			
			outputStream.write(("--" + boundary + "\r\n").getBytes());
			outputStream.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"%s\"\r\n", file.getName()).getBytes());
			outputStream.write(String.format("Content-Type: %s\r\n\r\n", contentType).getBytes());
			
			
			// 获取媒体文件的输入流（读取文件）
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = in.read(buf)) != -1) {
				// 将媒体文件写到输出流（往微信服务器写数据）
				outputStream.write(buf, 0, size);
			}
			// 请求体结束
			outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
			outputStream.close();
			in.close();

			// 获取媒体文件上传的输入流（从微信服务器读数据）
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
			// 释放资源
			inputStream.close();
			inputStream = null;
			uploadConn.disconnect();

			// 使用JSON-lib解析返回结果
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
			log.error("上传媒体文件失败：{}", e);
		}
		return media;
	}

	/**
	 * 下载媒体文件
	 * 
	 * @param accessToken 接口访问凭证
	 * @param mediaId 媒体文件标识
	 * @param savePath 文件在服务器上的存储路径
	 * @return
	 */
	public static String getMedia(String accessToken, String mediaId, String savePath) {
		String filePath = null;
		// 拼接请求地址
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
			// 根据内容类型获取扩展名
			String fileExt = getFileExt(conn.getHeaderField("Content-Type"));
			// 将mediaId作为文件名
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
			log.info("下载媒体文件成功，filePath=" + filePath);
		} catch (Exception e) {
			filePath = null;
			log.error("下载媒体文件失败：{}", e);
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
