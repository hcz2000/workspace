package com.jams.weixin.qy.pojo;

import java.util.List;

/**
 * 通过网页授权获取的用户信息
 * 
 * @author HCZ
 * @date 2013-11-09
 */
public class AppInfo {

	private String agentId;
	private String name;
	private String description;
	private List<String> allowUsers;
	private List<Integer> allowPartys;	
	private String redirectDomain;
	

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRedirectDomain() {
		return redirectDomain;
	}

	public void setRedirectDomain(String redirectDomain) {
		this.redirectDomain = redirectDomain;
	}

	public List<Integer> getAllowPartys() {
		return allowPartys;
	}

	public void setAllowPartys(List<Integer> allowPartys) {
		this.allowPartys = allowPartys;
	}

	public List<String> getAllowUsers() {
		return allowUsers;
	}

	public void setAllowUsers(List<String> allowUsers) {
		this.allowUsers = allowUsers;
	}


}
