package com.rachit.deamon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.rachit.deamon.threads.ApplicationServiceRequestHandler;
import com.rachit.deamon.threads.DataTransferThread;
import com.rachit.models.ApplicationData;
import com.rachit.models.Event;
import com.rachit.utils.Logger;

public class ApplicationClient extends Node implements Runnable{
	private RTFSService motherService; 
	private final String TAG = "ApplicationClient";
	
	public int portNumber;
	public DataTransferThread dtThread; ;
	public ApplicationServiceRequestHandler nnThread;
	public ApplicationData appData;
	


	public ApplicationClient(RTFSService motherService)
	{
		this.motherService = motherService;
		logicalClock = 0;
		logicalCount = 0;
		physicalClock = getPhysicalTime();
	}

	@Override
	public void run() {

		boolean isActive = true;
		ServerSocket servSocket = null;
		while(isActive) {
			try {
				//while(isApplicationAlive) {
				servSocket = new ServerSocket(motherService.globalConf.getApplicationClientPort());
				Socket fromClientSocket = servSocket.accept();
				//ObjectInputStream inStream = new ObjectInputStream(fromClientSocket.getInputStream());
				//appData = (ApplicationData) inStream.readObject();
				BufferedReader reader = new BufferedReader(new InputStreamReader(fromClientSocket.getInputStream()));
				String input = reader.readLine();
				String []lol = input.split(":");
				ApplicationData appData = new ApplicationData(lol[0], lol[1]);
				Logger.getInstance().log(TAG, "Message received: " + appData.data);
				
				Event<String> controlData = new Event<String>(appData.zone + ".txt", false);
				timeStampSend(controlData);

				Thread threadControl = new Thread(new ApplicationServiceRequestHandler(motherService, controlData ,this));

				threadControl.start();


			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException npe){
				npe.printStackTrace();
			}finally {
				if(servSocket != null) {
					try {
						servSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	public void writeNReadData(ArrayList<String> IPs ,boolean write)
	{
		
		/*Thread threadData = new Thread(new DataTransferThread(dataEvent,this,IPs));
		threadData.start();
*/
	}

}
