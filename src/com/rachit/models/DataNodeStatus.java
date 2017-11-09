package com.rachit.models;

public class DataNodeStatus {
	private long lastHBTime;
	
	public DataNodeStatus(long lastHBTime, String inetAddress){
		this.lastHBTime = lastHBTime;
		this.inetAddress = inetAddress;
	}
	
	public long getLastHBTime() {
		return lastHBTime;
	}
	public void setLastHBTime(long lastHBTime) {
		this.lastHBTime = lastHBTime;
	}
	public String getInetAddress() {
		return inetAddress;
	}
	public void setInetAddress(String inetAddress) {
		this.inetAddress = inetAddress;
	}
	private String inetAddress;
}
