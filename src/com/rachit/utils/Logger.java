package com.rachit.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class Logger { 
	
	public static Logger instance = null;
	private PrintWriter logFile = null;
	
	public static Logger getInstance() {
		if(instance == null) {
			instance = new Logger();
		}
		return instance;
	}
	
	public Logger() {
		this(Constants.DEFAULT_LOG_FILE_NAME);
	}
	
	public Logger(String fileName) {
		try {
			logFile = new PrintWriter(fileName);
		}catch(IOException ioe) {
			System.out.println("");
		}
	}
	//TODO log the debugging info
	public void log(String tag, String msg) {
		if(logFile == null)
			return;
		LocalDateTime time = LocalDateTime.now(); 
		display(tag + ":" + msg + " at " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
		logFile.println(tag + ":" + msg + " at " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
	}
	
	public void display(String msg) {
		if(logFile == null)
			return;
		System.out.println(msg);
	}
	
	public void destroy() {
		if(logFile !=null) {
			logFile.close();
			System.out.println("Log File closed");
		}
	}
}
