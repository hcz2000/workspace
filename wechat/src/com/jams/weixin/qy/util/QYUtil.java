package com.jams.weixin.qy.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jams.weixin.qy.myapp.Constants;
import com.jams.weixin.qy.pojo.AppInfo;
import com.jams.weixin.qy.pojo.UserInfo;

/**
 * 高级接口工具类
 * 
 * @author HCZ
 * @date 2013-11-9
 */
public class QYUtil {
	private static Logger log = LoggerFactory.getLogger(QYUtil.class);
	

	public static AppInfo getAppInfo(String accessToken, String agentId) {
		String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/agent/get?access_token=ACCESS_TOKEN&agentid=AGENTID";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("AGENTID", agentId);
		JSONObject jsonObject = ComUtil.httpsRequest(requestUrl, "GET", null);
		log.debug(jsonObject.toString());

		AppInfo appInfo = new AppInfo();
		if (null != jsonObject) {
			int errorcode = jsonObject.getInt("errcode");
			String errmsg = jsonObject.getString("errmsg");
			if (errorcode == 0) {
				appInfo.setAgentId(agentId);
				appInfo.setName(jsonObject.getString("name"));
				appInfo.setDescription(jsonObject.getString("description"));
				appInfo.setRedirectDomain(jsonObject.getString("redirect_domain"));
				JSONObject partys = (JSONObject) jsonObject.get("allow_partys");
				appInfo.setAllowPartys(JSONArray.toList(partys.getJSONArray("partyid"), List.class));
				JSONObject users = (JSONObject) jsonObject.get("allow_userinfos");
				appInfo.setAllowUsers(JSONArray.toList(users.getJSONArray("user"), List.class));
			} else {
				log.error("获取应用信息失败  errcode:{} errmsg:{}", errorcode, errmsg);
			}
		} else {
			log.error("Get Appinfo failed，。。。");
		}
		return appInfo;
	}

	public static List<UserInfo> getUsers(String accessToken) {
		String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD&status=STATUS";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("DEPARTMENT_ID", "1")
				.replace("FETCH_CHILD", "1").replace("STATUS", "0");

		JSONObject jsonObject = ComUtil.httpsRequest(requestUrl, "GET", null);
		System.out.println("MSG:" + jsonObject);

		List<UserInfo> userList = new ArrayList<UserInfo>();

		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if (errorCode == 0) {
				JSONArray array = jsonObject.getJSONArray("userlist");
				for (int i = 0; i < array.size(); i++) {
					JSONObject jsonElement = (JSONObject) array.get(i);
					UserInfo userInfo = new UserInfo();
					userInfo.setUserId(jsonElement.getString("userid"));
					userInfo.setName(jsonElement.getString("name"));
					userInfo.setDepartment(	JSONArray.toList(jsonElement.getJSONArray("department"), List.class));
					if (jsonElement.containsKey("position")) {
						userInfo.setPosition(jsonElement.getString("position"));
					}
					if (jsonElement.containsKey("mobile")) {
						userInfo.setMobile(jsonElement.getString("mobile"));
					}
					if (jsonElement.containsKey("email")) {
						userInfo.setEmail(jsonElement.getString("email"));
					}
					if (jsonElement.containsKey("weixinid")) {
						userInfo.setWeixinId(jsonElement.getString("weixinid"));
					}
					if (jsonElement.containsKey("gender")) {
						userInfo.setGender(jsonElement.getString("gender"));
					}
					userList.add(userInfo);
				}
			} else {
				System.out.println("ErrCode:" + errorCode + "———" + "Errmsg:" + errorMsg);
			}
		} else {
			System.out.println("Get UserList failed，。。。");
		}

		return userList;

	}

	public static String getOpenId(String accessToken, String userId) {

		String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/convert_to_openid?access_token=ACCESS_TOKEN";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);

		String jsonMsg = String.format("{\"userid\":\"%s\"}", userId);

		JSONObject jsonObject = ComUtil.httpsRequest(requestUrl, "POST", jsonMsg);
		log.debug(jsonObject.toString());

		String openId = null;
		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if (errorCode == 0) {
				openId = jsonObject.getString("openid");
				log.debug("openId:" + openId);
			} else {
				log.error("获取openId失败 errcode:{} errmsg:{}", errorCode, errorMsg);
			}
		}

		return openId;
	}

	/**
	 * 通过网页授权获取用户信息
	 * 
	 * @param accessToken
	 *            网页授权接口调用凭证
	 * @param openId
	 *            用户标识
	 * @return SNSUserInfo
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static UserInfo getUserInfo(String accessToken, String userId) {
		UserInfo userInfo = null;
		// 拼接请求地址
		String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID";

		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("USERID", userId);
		// 通过网页授权获取用户信息
		JSONObject jsonObject = ComUtil.httpsRequest(requestUrl, "GET", null);
		System.out.println("USERINFO:" + jsonObject);

		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if (errorCode == 0) {
				userInfo = new UserInfo();
				userInfo.setUserId(userId);
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
				log.error("获取user失败 errcode:{} errmsg:{}", errorCode, errorMsg);
			}
		}
		return userInfo;
	}

	public static void main(String args[]) {

		String accessToken = ComUtil.getToken(Constants.CORPID, Constants.CORPSECRECT).getAccessToken();


	
		List<Integer> partys = getAppInfo(accessToken,"1").getAllowPartys();
		 
		System.out.println(partys.get(0));
		 
		List<UserInfo> users = getUsers(accessToken);
		 
		for (Iterator<UserInfo> itr = users.iterator(); itr.hasNext();) {
			UserInfo user = itr.next(); 
			System.out.println(user.getUserId() + ":" + user.getName() + ":" + user.getName() + ":" + user.getDepartment() + ":" + user.getEmail() + ":" + user.getMobile() + ":" + user.getWeixinId()); 
		 }
		 
		String openId = getOpenId(accessToken, "hcz13825089890");
		System.out.println("Open id:"+openId);
		UserInfo user = getUserInfo(accessToken, "hcz13825089890");
		System.out.println("User name:"+user.getName());
		System.out.println("User dept:"+user.getDepartment().get(0));
		System.out.println("User mobile:"+user.getMobile());

	}
}
