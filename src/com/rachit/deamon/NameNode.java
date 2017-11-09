package com.rachit.deamon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rachit.deamon.threads.NameNodeRequestHandlerThread;
import com.rachit.models.DataNodeStatus;
import com.rachit.models.FSTableKey;
import com.rachit.models.FSTableValue;
import com.rachit.utils.Constants;

import DeamonActions.HeartBeatListener;

public class NameNode extends Node implements Runnable {
	@SuppressWarnings("unused")
	//private String id = Constants.ID;
	private final String TAG = "NameNode";
	private RTFSService motherService;
	private ExecutorService executor;
	
	//file system image is in this table
	public Hashtable <FSTableKey, FSTableValue> fsImage;
	
	//this table stores the data for alive data nodes
	public Hashtable <Integer,DataNodeStatus> connectedDataNodes;
	
	private boolean isNamenodeActive = true;
	
	public boolean isNamenodeActive() {
		return isNamenodeActive;
	}


	public synchronized void setNamenodeActive(boolean isNamenodeActive) {
		this.isNamenodeActive = isNamenodeActive;
	}


	public NameNode(RTFSService motherService){
		this.motherService = motherService;
		executor = Executors.newFixedThreadPool(Constants.NTHREDS);
		fsImage = new Hashtable<FSTableKey, FSTableValue>();
		connectedDataNodes = new Hashtable<Integer,DataNodeStatus>();
	 }
	
	
	/**
	 * 
	 * @return the singleton instance of the DataNode class
	 *//*
	public static NameNode getInstance(){
		if(instance == null) {
			try {
				instance = new NameNode();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}*/




	@Override
	public void run() {
		while(isNamenodeActive) {
			
			//initialize the heartbeat Listener
			HeartBeatListener hbListener = new HeartBeatListener(motherService.globalConf.getNameNodeHeartBeatport(), connectedDataNodes);
			new Thread(hbListener).start();
			
			ServerSocket dnServerSocket = null;
			try {
				dnServerSocket = new ServerSocket(motherService.globalConf.getNameNodePort());
				Socket clientSocket = dnServerSocket.accept();
				Runnable serviceThread = new NameNodeRequestHandlerThread(this, clientSocket);
				executor.execute(serviceThread);				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if (dnServerSocket != null) {
					try {
						dnServerSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	public ArrayList<DataNodeStatus> getAliveNodes() {
		Set<Integer> keySet = connectedDataNodes.keySet();
		ArrayList<DataNodeStatus> result = new ArrayList<DataNodeStatus>();
		long currentTime = System.currentTimeMillis();
		for(Integer i: keySet) {
			DataNodeStatus thisStatus = connectedDataNodes.get(i);
			if((currentTime - thisStatus.getLastHBTime()) < motherService.globalConf.getDataNodeTimeOut()) {
				result.add(thisStatus);
			}
		}
		return result;
	}
}
