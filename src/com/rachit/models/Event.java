package com.rachit.models;

import java.io.Serializable;

public class Event <T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 12L;
	public int logicalClock;
	public int logicalCount;
	public int physicalClock;
	public T message;
	public boolean write;
	
	public Event(T msg, boolean wr){
		this.message = msg;
		this.write = wr;
	}
	
	public void applyTimeStamp(int lC, int lCnt, int pC) {
		this.logicalClock = lC;
		this.logicalCount = lCnt;
		this.physicalClock = pC;
	}
	
	public String timestampToString() {
		return "[" + logicalClock + "," + logicalCount + "," + physicalClock + "]";
	}
	 

}
