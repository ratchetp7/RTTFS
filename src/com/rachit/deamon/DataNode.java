package com.rachit.deamon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

import com.rachit.deamon.threads.ForwarderThread;
import com.rachit.models.ApplicationData;
import com.rachit.models.DataPushEvent;
import com.rachit.utils.Constants;
import com.rachit.utils.Logger;

import DeamonActions.HeartBeat;

public class DataNode extends Node implements Runnable{
	private int id;
	private RTFSService motherService;
	private final String MESSSAGE = "DN_ALIVE";
	private Hashtable<String, FileAttributes> fileTable;
	private HeartBeat hbMsg = null;
	private Thread hBThread; 
	private final String TAG = "DataNode";

	
	//public constructor
	public DataNode(RTFSService motherService){
		//start the heart beat message to inform the nameNode
		this.motherService = motherService; 
		this.id = motherService.globalConf.getDataNodeId();
		hbMsg = new HeartBeat(motherService.globalConf.getDataNodeServer(), motherService.globalConf.getNameNodeHeartBeatport(), id, MESSSAGE);
		hBThread = new Thread(hbMsg);
		hBThread.start();
		//initiate the file tables
		fileTable = new Hashtable<String, FileAttributes>();
	}


	@Override
	public void run() {
		ServerSocket welcomeSocket = null ;
		try {
			welcomeSocket = new ServerSocket(motherService.globalConf.getDataNodePort());
			//block on welcoming socket for contact by a client
			while(true) {
				Socket connectionSocket = welcomeSocket.accept();
				new Thread(new FileHandler(this, connectionSocket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(welcomeSocket != null) {
				try {
					welcomeSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * Updates the socket to
	 */
	public void updateSocketOnNameNodeFailure(String inetAddr, int port) {
		hbMsg.stopHeartBeat();
		try {
			hBThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		motherService.globalConf.setDataNodeServer(inetAddr);
		hbMsg = new HeartBeat(inetAddr, Constants.NAMENODE_PORT, id, MESSSAGE);
		hBThread = new Thread(hbMsg);
		hBThread.start();
	}
	
	class FileAttributes{
		String filename;
		String dateCreated;
		String zoneName;
		long size;
		public FileAttributes(String fileName, String dateCreated, String zoneName, int size) {
			this.filename = fileName;
			this.dateCreated = dateCreated;
			this.zoneName = zoneName;
			this.size = size;
		}
		
	}
	
	
	/**
	 * Thread class to handle file operation client request
	 * @author ratch
	 *
	 */
	class FileHandler implements Runnable{
		private Socket connectionSocket;
		private DataNode currentNode; 
		
		public FileHandler(DataNode currentNode, Socket connectionSocket) {
			this.connectionSocket = connectionSocket;
			this.currentNode = currentNode;
		}

		@Override
		public void run() {
			int operation = Constants.NO_OP;
			String fileName = null;
			String line = null;
			PrintWriter outToClient = null;
			try{
				//create input stream attached to socket
				ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
				//create output stream attached to socket
				outToClient = new PrintWriter(new ObjectOutputStream(connectionSocket.getOutputStream()));
				@SuppressWarnings("unchecked")
				DataPushEvent<ApplicationData> clientData = (DataPushEvent<ApplicationData>)inFromClient.readObject();
				currentNode.timeStampReceive(clientData);
				
				ApplicationData data = clientData.message;
				String response;
				if(clientData.write) {
					if(writeToFile(data.zone + ".txt", data.data + clientData.timestampToString())) {
						response = "Success";
						Logger.getInstance().log(TAG, "fileupdate success-" + data.zone + ".txt" + ":" + data.data);
					}else {
						response = "failure";
					}
				}else {
					readFromFile(data.zone + ".txt");
					response = "failure";
				}
				
				//String request[] = clientSentence.split(":");
				// System.out.println("Client sent: "+clientSentence);
				/*while ((clientSentence = inFromClient.readLine()) != null) {
					//String ipClient = connectionSocket.getInetAddress().toString();
					System.out.println("Client sent: "+clientSentence);
					//process

					//update database //


					String capitalizedSentence = clientSentence.toUpperCase() + '\n';
					//write out line to socket
					outToClient.print(capitalizedSentence);
					outToClient.flush();
				}
				//process
				String capitalizedSentence = clientSentence.toUpperCase() + '\n';
				//write out line to socket*/
				outToClient.print(response);
				outToClient.flush();
				if(response == "Success") {
					Thread forwardMessage = new Thread(new ForwarderThread(motherService, currentNode, clientData));
					forwardMessage.start();
				}else {
					Logger.getInstance().log(TAG, "Update failed at DataNode:");
				}
				
			}catch(Exception e){
				Logger.getInstance().log(TAG, "Error in reading client's request");
				e.printStackTrace();
				operation = Constants.NO_OP;
			}
			
			/*if(outToClient != null) {
				switch(operation) {
				case Constants.NO_OP: Logger.getInstance().log(TAG, "No operation");
				
				break;
				case Constants.WRITE_OP: writeToFile(fileName, line);
				break;
				case Constants.READ_OP: readFromFile(fileName);
				break;
				case Constants.CREATE_FILE_OP: 
					if(createNewFile(fileName)) {
						String date = LocalDateTime.now().getDayOfMonth() + "/" + LocalDateTime.now().getMonthValue() + "/" + LocalDateTime.now().getYear();
						fileTable.put(fileName, new FileAttributes(fileName, date, fileName, 0));
					}
				break;
				default: Logger.getInstance().log(TAG, "No Valid Operation on DataNode");
				}
			}else {
				//TODO create a timeout to client
			}*/
		}

		private boolean writeToFile(String fileName, String line) {
			PrintWriter inputStream = null;
			try {
				inputStream = new PrintWriter(new FileWriter(fileName));
				inputStream.println();
			}catch(IOException ioerr) {
				return false;
			}finally {
				if(inputStream != null ){
					inputStream.close();
				}
			}
			return true;
		}
		
		private String readFromFile(String fileName) {
			return "";
		}
		
		
		private boolean createNewFile(String fileName) {
			boolean fileCreated = false;
			try {
				File file = new File(fileName);
				return file.createNewFile();
			}catch(IOException ioe) {
				Logger.getInstance().log(TAG, "File Creation error: " + ioe.toString());
			}
			return fileCreated;
		}
	}
}
