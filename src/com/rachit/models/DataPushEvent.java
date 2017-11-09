package com.rachit.models;

import java.io.Serializable;
import java.util.ArrayList;

public class DataPushEvent<T> extends Event<T>{
	
	public ArrayList<String> ipList;
	
	public DataPushEvent(T msg, boolean wr) {
		super(msg, wr);
	}

	public DataPushEvent(Event<? extends T> event) {
		super(event.message, event.write);		
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;
	
}
