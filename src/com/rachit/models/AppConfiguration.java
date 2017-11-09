package com.rachit.models;

/**
 * 
 * @author ratch
 * model class for the application configuration
 */
public class AppConfiguration {
	private int mode;
	private int dataNodeId;
	private int NameNodePort;
	private int DataNodePort;
	private int applicationClientPort;
	private int nameNodeHeartBeatport;
	private String NameNodeServer;
	private String DataNodeServer;
	private String applicationClientServer;
	private long dataNodeTimeOut;
	

	public int getNameNodeHeartBeatport() {
		return nameNodeHeartBeatport;
	}
	public void setNameNodeHeartBeatport(int nameNodeHeartBeatport) {
		this.nameNodeHeartBeatport = nameNodeHeartBeatport;
	}
	
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public int getNameNodePort() {
		return NameNodePort;
	}
	public void setNameNodePort(int nameNodePort) {
		NameNodePort = nameNodePort;
	}
	public int getDataNodePort() {
		return DataNodePort;
	}
	public void setDataNodePort(int dataNodePort) {
		DataNodePort = dataNodePort;
	}
	public String getNameNodeServer() {
		return NameNodeServer;
	}
	public void setNameNodeServer(String nameNodeServer) {
		NameNodeServer = nameNodeServer;
	}
	public String getDataNodeServer() {
		return DataNodeServer;
	}
	public void setDataNodeServer(String dataNodeServer) {
		DataNodeServer = dataNodeServer;
	}
	
	public int getApplicationClientPort() {
		return applicationClientPort;
	}
	public void setApplicationClientPort(int applicationClientPort) {
		this.applicationClientPort = applicationClientPort;
	}
	public String getApplicationClientServer() {
		return applicationClientServer;
	}
	public void setApplicationClientServer(String applicationClientServer) {
		this.applicationClientServer = applicationClientServer;
	}
	public int getDataNodeId() {
		return dataNodeId;
	}
	public void setDataNodeId(int dataNodeId) {
		this.dataNodeId = dataNodeId;
	}
	public long getDataNodeTimeOut() {
		return dataNodeTimeOut;
	}
	public void setDataNodeTimeOut(long dataNodeTimeOut) {
		this.dataNodeTimeOut = dataNodeTimeOut;
	}
	
}
