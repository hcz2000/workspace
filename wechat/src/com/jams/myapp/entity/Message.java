package com.jams.myapp.entity;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{  
    
    private Date createTime;  
    private String sender;
    private String receiver;
    private String message;
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date create_time) {
		this.createTime = create_time;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
    
}  