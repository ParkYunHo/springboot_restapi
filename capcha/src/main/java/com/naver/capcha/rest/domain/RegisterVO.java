package com.naver.capcha.rest.domain;

public class RegisterVO {
	private int sn;
	private String appName;
	private String clientID;
	private String clientSecret;
	private String clientKey;
	private int IsIssued;
	
	public int getIsIssued() {
		return IsIssued;
	}
	public void setIsIssued(int isIssued) {
		IsIssued = isIssued;
	}
	public int getSn() {
		return sn;
	}
	public void setSn(int sn) {
		this.sn = sn;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getClientKey() {
		return clientKey;
	}
	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}
}
