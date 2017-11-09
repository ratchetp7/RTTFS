package com.rachit.deamon.listener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;

import com.rachit.utils.Logger;

public class DataNodeHeartBeatListener {
	
	private ServerSocket welcomeSocket;
	
	HashMap<String,Integer> database = new HashMap<String, Integer>();
	public static void main (String args[]) throws Exception{
		new DataNodeHeartBeatListener();
	}

	public DataNodeHeartBeatListener() throws Exception{
		//create welcoming socket at port 6789
		welcomeSocket = new ServerSocket(7223);



		//block on welcoming socket for contact by a client
		Socket connectionSocket = welcomeSocket.accept();
		// create thread for client
		new Connection(connectionSocket).start();

	}

	public HashMap<String,Integer> sendAliveNodes()
	{
		HashMap<String,Integer> aliveOnes= new HashMap<String, Integer>();
		for(String s : database.keySet()){
			int currentTime = LocalDateTime.now().getSecond();
			int nodeTime = database.get(s);
			if((currentTime - nodeTime) < 5000)
			{
				aliveOnes.put(s,LocalDateTime.now().getSecond());
			}
		}
		return aliveOnes;

	}
	class Connection extends Thread{

		private static final String TAG = "DdataNodeHeartBeatListener.Connection";
		
		Socket connectionSocket;
		Connection(Socket _connectionSocket){
			connectionSocket = _connectionSocket;
			//this.start();
		}
		public void run(){
			try{
				//create input stream attached to socket


				BufferedReader inFromClient = new BufferedReader(new InputStreamReader (connectionSocket.getInputStream()));
				//create output stream attached to socket
				//PrintWriter outToClient = new PrintWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));


				String clientSentence;
				while ((clientSentence = inFromClient.readLine()) != null) {
					String ipClient = connectionSocket.getInetAddress().toString();
					
					synchronized (database) {
						int timeVal = LocalDateTime.now().getSecond();
						database.put(ipClient,timeVal);
						Logger.getInstance().display("DataNode: " + ipClient + " alive at: " + timeVal);
					}

				}

			}catch(Exception e){
				Logger.getInstance().log(TAG, "Exception occured at DataNode HeartBeat "+ e.toString());
			}
		}
	}


}
