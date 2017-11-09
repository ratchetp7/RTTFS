package com.rachit.deamon.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.rachit.deamon.ApplicationClient;
import com.rachit.deamon.RTFSService;
import com.rachit.models.ApplicationData;
import com.rachit.models.DataPushEvent;
import com.rachit.models.Event;
import com.rachit.utils.Logger;

public class ApplicationServiceRequestHandler implements Runnable{

	private static final String TAG = "ApplicationRequestHandler";
	private RTFSService motherService;
	public Event<?> controlEvent;
	public DataPushEvent<ApplicationData> dataEvent;
	public ApplicationClient currNode;
	

	public ApplicationServiceRequestHandler(RTFSService motherService, Event<?> cEvent,ApplicationClient currNode) {
		this.motherService = motherService;
		controlEvent = cEvent;
		this.currNode = currNode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Socket socket = null;
		try
		{
			socket = new Socket(motherService.globalConf.getNameNodeServer(), motherService.globalConf.getNameNodePort());
			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(this.controlEvent);
			ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
			
			Event<ArrayList<String>> receivedControlEvent = (Event<ArrayList<String>>) inStream.readObject();
			currNode.timeStampReceive(receivedControlEvent);
			try {
				@SuppressWarnings("unchecked")
				ArrayList<String>  IPList =  (ArrayList<String>)receivedControlEvent.message;
				//prepare the data message
				dataEvent = new DataPushEvent<ApplicationData>(currNode.appData, true);
				currNode.timeStampSend(dataEvent);
				pushToDataNode(IPList);
			    //currNode.writeNReadData(IPList,true);
				
			}catch(ClassCastException cce) {
				cce.printStackTrace();
			}
			

		} catch (SocketException se) {
			se.printStackTrace();
			// System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	
	/*private void pushToDataNodePipeline(ArrayList<String> ipList) {
		Socket socket = null;
		String dataNodeIp = ipList.get(0);
		
		try {
			socket = new Socket(dataNodeIp, motherService.globalConf.getDataNodePort());
			dataEvent.ipList = (ArrayList<String>) ipList.subList(1, ipList.size());
			
			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(dataEvent);
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
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}*/
	
	
	@SuppressWarnings("unchecked")
	private void pushToDataNode(ArrayList<String> ipList) {
		Socket socket = null;
		//dataEvent.ipList = (ArrayList<String>) ipList.subList(1, ipList.size());
		int count = ipList.size();
		int i = 0;
		while(count > 0)
		{
			try {
				String IPToSend = ipList.get(i);
				ArrayList<String> currentList = new ArrayList<String>(ipList);
				currentList.remove(i);
				dataEvent.ipList = currentList;
				i++;
				socket = new Socket(IPToSend, motherService.globalConf.getDataNodePort());
				//System.out.println("Connected");
				ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
				outputStream.writeObject(dataEvent);
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
