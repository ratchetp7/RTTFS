package com.rachit.deamon.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.rachit.deamon.ApplicationClient;
import com.rachit.models.Event;
import com.rachit.utils.Logger;


public class DataTransferThread implements Runnable{
	private static final String TAG = "DataTransferThread";
	ApplicationClient currNode;
	Event<ArrayList<String>> dataEvent;
	ArrayList<String> IPs;
	
	public DataTransferThread(Event<ArrayList<String>> eve,ApplicationClient node,ArrayList<String>IP) {
		this.dataEvent = eve;
		this.currNode = node;
		this.IPs = IP;		
	}

	@Override
	public void run() {
			
		}
		

}
