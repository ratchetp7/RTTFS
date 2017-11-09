package com.rachit.deamon.listener;


import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.rachit.utils.Constants;

import DeamonActions.HeartBeat;

public class ApplicationListener implements Runnable{
	/*int portNumber;
	String fileName;
	HeartBeat nodeLife;
	int dataNodePort;

	public ApplicationListener(int port,String fileName)
	{
		this.portNumber = Constants.NAMENODE_PORT;
		this.fileName = fileName;
		//this.nodeLife = hb;
		//this.dataNodePort = dataPort;
	}
*/
public void run(){
}
/*	@Override
	public void run() {
		try{
			while(true)
			{
				String str;
				ServerSocket servSocket = new ServerSocket(portNumber);
				Socket fromClientSocket = servSocket.accept();
				PrintWriter pw = new PrintWriter(fromClientSocket.getOutputStream(), true);

				BufferedReader br = new BufferedReader(new InputStreamReader(fromClientSocket.getInputStream()));

				while ((str = br.readLine()) != null) {
					System.out.println("The message: " + str);
					writeString(str);
					HashMap<String,Integer> aliveNodes = nodeLife.sendAliveNodes();
					for(String S: aliveNodes.keySet()){
						pushToNode(S,dataNodePort,str);
					}
				}
				pw.close();
				br.close();
			}
		}catch(Exception e){
			
		}


	}
	public void writeString(String line)
	{
		try
		{

			FileWriter fw = new FileWriter(fileName,true); //the true will append the new data
			fw.write(line);//appends the string to the file
			fw.close();
		}
		catch(IOException ioe)
		{
			System.err.println("IOException: " + ioe.getMessage());
		}

	}
	public void pushToNode(String IP, int portNumber, String data) throws IOException
	{
		Socket socket = null;
		try {
			socket = new Socket(IP, portNumber);
			OutputStream outstream = socket.getOutputStream(); 
			PrintWriter out = new PrintWriter(outstream);
			out.print(data);

		}catch (UnknownHostException e) {
			System.err.print(e);
		} finally {
			socket.close();
		}
	}*/
}