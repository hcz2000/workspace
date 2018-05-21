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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
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
public class IMUtil {
	private static Logger log = LoggerFactory.getLogger(IMUtil.class);
	
	public static void createChat( String accessToken,String chatId, String chatName,String owner, List<String> userList){
		 	
		 	StringBuffer userSB=new StringBuffer("[");
		 	int i=0;
		 	for(Iterator itr=userList.iterator();itr.hasNext();i++){
		 		if(i==0)
		 			userSB.append("\"").append(itr.next()).append("\"");
		 		else 
		 			userSB.append(",\"").append(itr.next()).append("\"");
		 	}
		 	userSB.append("]");
			// ƴ�������ַ
			String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/chat/create?access_token=ACCESS_TOKEN";
			requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
			String jsonMsg = "{\"chatid\":\"%s\",\"name\":\"%s\",\"owner\":\"%s\",\"userlist\":%s}";
			String message=String.format(jsonMsg, chatId, chatName,owner, userSB.toString());
			log.debug(message);
			// ���Ϳͷ���Ϣ
			JSONObject jsonObject = ComUtil.httpsRequest(requestUrl, "POST", message);

			if (null != jsonObject) {
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				if (0 == errorCode) {
					log.info("�Ự�����ɹ�");
				} else {
					log.error("�Ự����ʧ�� errcode:{} errmsg:{}", errorCode, errorMsg);
				}
			}

	 }
	
	public static void getChatInfo( String accessToken,String chatId){
	 	
	 	// ƴ�������ַ
		String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/chat/get?access_token=ACCESS_TOKEN&chatid=CHATID";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
		requestUrl = requestUrl.replace("CHATID", chatId);
		// ���Ϳͷ���Ϣ
		JSONObject jsonObject = ComUtil.httpsRequest(requestUrl, "GET",null);

		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if (0 == errorCode) {
				JSONObject chatInfo=jsonObject.getJSONObject("chat_info");
				String name=chatInfo.getString("name");
				String owner=chatInfo.getString("owner");
				JSONArray userList=chatInfo.getJSONArray("userlist");
				log.info(userList.toString());
			} else {
				log.error("�Ự����ʧ�� errcode:{} errmsg:{}", errorCode, errorMsg);
			}
		}

	}
	
	public static void quitChat( String accessToken,String chatId,String user){
	 	
	 	// ƴ�������ַ
		String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/chat/quit?access_token=ACCESS_TOKEN";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
	
		// ���Ϳͷ���Ϣ
		String jsonMsg = "{\"chatid\":\"%s\",\"op_user\":\"%s\"}";
		String message=String.format(jsonMsg, chatId, user);
		log.debug(message);
		// ���Ϳͷ���Ϣ
		JSONObject jsonObject = ComUtil.httpsRequest(requestUrl, "POST", message);

		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if (0 == errorCode) {
				log.info("User "+user+ " quit");
			} else {
				log.error("�˳��ػ� ʧ�� errcode:{} errmsg:{}", errorCode, errorMsg);
			}
		}

	}

	
	public static void updateChat( String accessToken,String chatId,String opUser, String chatName,String owner, List<String> addUserList, List<String> delUserList){
		 	
			String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/chat/update?access_token=ACCESS_TOKEN";
			requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
			StringBuffer messageBuf=new StringBuffer(String.format("{\"chatid\":\"%s\",\"op_user\":\"%s\"", chatId,opUser));
			if(chatName!=null){
				messageBuf.append(",\"name\":\"").append(chatName).append("\"");
			}
			if(owner!=null){
				messageBuf.append(",\"owner\":\"").append(owner).append("\"");
			}
			if(addUserList!=null ){
				StringBuffer users=new StringBuffer("[");
			 	int i=0;
			 	for(Iterator itr=addUserList.iterator();itr.hasNext();i++){
			 		if(i==0)
			 			users.append("\"").append(itr.next()).append("\"");
			 		else 
			 			users.append(",\"").append(itr.next()).append("\"");
			 	}
			 	users.append("]");
			 	messageBuf.append(",\"add_user_list\":").append(users);
			} 
		 	
			if(delUserList!=null ){
				StringBuffer users=new StringBuffer("[");
			 	int i=0;
			 	for(Iterator itr=delUserList.iterator();itr.hasNext();i++){
			 		if(i==0)
			 			users.append("\"").append(itr.next()).append("\"");
			 		else 
			 			users.append(",\"").append(itr.next()).append("\"");
			 	}
			 	users.append("]");
			 	messageBuf.append(",\"del_user_list\":").append(users);
			} 
			// ƴ�������ַ

			String message=messageBuf.append("}").toString();
			log.debug(message);
			// ���Ϳͷ���Ϣ
			JSONObject jsonObject = ComUtil.httpsRequest(requestUrl, "POST", message);

			if (null != jsonObject) {
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				if (0 == errorCode) {
					log.info("�Ự���³ɹ� errcode:{} errmsg:{}", errorCode, errorMsg);
				} else {
					log.error("�Ự����ʧ�� errcode:{} errmsg:{}", errorCode, errorMsg);
				}
			}

	 }
	
	public static void sendText2Group( String accessToken,String chatId, String sender,String text){
	 	
	// ƴ�������ַ
		String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/chat/send?access_token=ACCESS_TOKEN";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
		String jsonMsg ="{\"receiver\":{\"type\": \"group\",\"id\": \"%s\"},\"sender\":\"%s\",\"msgtype\": \"text\",\"text\":{\"content\": \"%s\"}}";
		String message=String.format(jsonMsg, chatId, sender, text);
		log.debug(message);
		// ���Ϳͷ���Ϣ
		JSONObject jsonObject = ComUtil.httpsRequest(requestUrl, "POST", message);

		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if (0 == errorCode) {
				log.info("��Ϣ���ͳɹ�");
			} else {
				log.error("��Ϣ����ʧ�� errcode:{} errmsg:{}", errorCode, errorMsg);
			}
		}

	}
	
	public static void sendText2User( String accessToken,String receiver, String sender,String text){
	 	
	// ƴ�������ַ
		String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/chat/send?access_token=ACCESS_TOKEN";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
		String jsonMsg ="{\"receiver\":{\"type\": \"single\",\"id\": \"%s\"},\"sender\":\"%s\",\"msgtype\": \"text\",\"text\":{\"content\": \"%s\"}}";
		String message=String.format(jsonMsg, receiver, sender, text);
		log.debug(message);
		// ���Ϳͷ���Ϣ
		JSONObject jsonObject = ComUtil.httpsRequest(requestUrl, "POST", message);

		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if (0 == errorCode) {
				log.info("��Ϣ���ͳɹ�");
			} else {
				log.error("��Ϣ����ʧ�� errcode:{} errmsg:{}", errorCode, errorMsg);
			}
		}

	}


	
	public static void main(String args[]) {

		String accessToken = ComUtil.getToken(com.jams.weixin.qy.imapp.Constants.CORPID, com.jams.weixin.qy.imapp.Constants.CORPSECRECT).getAccessToken();
		/*
		List<String> userList=new ArrayList<String>();
		userList.add("hcz13825089890");
		userList.add("cgq");
		userList.add("User1");
		createChat(accessToken, "1","Group Chat", "hcz13825089890",userList);*/
		//getChatInfo(accessToken, "1");
		//updateChat(accessToken, "1","hcz13825089890","Ⱥ��Ự", "hcz13825089890",null,null);
		//sendText2Group(accessToken, "1","hcz13825089890","������Ϣ");
		//sendText2User(accessToken, "cgq","hcz13825089890","������Ϣ");
		quitChat(accessToken, "1","cgq");
	}
}
