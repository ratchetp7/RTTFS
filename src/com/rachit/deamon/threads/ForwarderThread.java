package com.rachit.deamon.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.rachit.deamon.DataNode;
import com.rachit.deamon.RTFSService;
import com.rachit.models.ApplicationData;
import com.rachit.models.DataPushEvent;
import com.rachit.models.Event;
import com.rachit.utils.Logger;

public class ForwarderThread implements Runnable{

	private final String TAG = "ForwarderThread";
	DataPushEvent<ApplicationData> message;
	RTFSService motherService;
	DataNode currNode;



	public ForwarderThread(RTFSService motherService, DataNode currNode, DataPushEvent<ApplicationData> message) {
		this.message = message;
		this.motherService = motherService;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Socket socket = null;
		ArrayList<String> ipList = message.ipList;
		//dataEvent.ipList = (ArrayList<String>) ipList.subList(1, ipList.size());
		int count = ipList.size();
		int i = 0;
		while(count > 0){
			try {
				String IPToSend = ipList.get(i);
				ArrayList<String> currentList = new ArrayList<String>(ipList);
				currentList.remove(i);
				message.ipList = currentList;
				i++;
				socket = new Socket(IPToSend, motherService.globalConf.getDataNodePort());
				//System.out.println("Connected");
				ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
				outputStream.writeObject(message);
				ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
				//socket.setSoTimeout(500);
				Event<String> receivedAck;

				try {
					receivedAck = (Event<String>) inStream.readObject();
					currNode.timeStampReceive(receivedAck);
					String response = receivedAck.message;

					if(response.equals("SUCCESS"))
					{
						break;
					}
					else
					{
						count--;
					}


				}catch(IOException e)
				{
					count--;
				}



			} catch (SocketException se) {
				se.printStackTrace();
				// System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				if(socket != null)
				{
					try {
						socket.close();
					} catch (IOException e) {
						Logger.getInstance().log(TAG, "Error in closing Application client socket");
						e.printStackTrace();
					}
				}
			}

			if(count == 0) {
				//TODO flag no write
				Logger.getInstance().log(TAG, "No datanode responding");
			}

		}
	}

}
