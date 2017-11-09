package DeamonActions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

import com.rachit.models.DataNodeStatus;
import com.rachit.utils.Logger;

public class HeartBeatListener implements Runnable{
	private boolean heartBeatListenerEnabled;
	private int heartBeatListenerPort;
	private final String TAG = "HeartBeatListener";
	private Hashtable<Integer,DataNodeStatus> logTable;
	
	public HeartBeatListener(int port, Hashtable<Integer,DataNodeStatus> logTable) {
		heartBeatListenerEnabled = true;
		heartBeatListenerPort = port;
		this.logTable = logTable;
	}
	public boolean isHeartBeatListenerEnabled() {
		return heartBeatListenerEnabled;
	}

	public void setHeartBeatListenerEnabled(boolean heartBeatListenerEnabled) {
		this.heartBeatListenerEnabled = heartBeatListenerEnabled;
	}

	@Override
	public void run() {
		while(heartBeatListenerEnabled) {
			ServerSocket heartBeatListenerSocket  = null;
			try {
				heartBeatListenerSocket = new ServerSocket(heartBeatListenerPort);
				Socket clientHeartBeatSocket = heartBeatListenerSocket.accept();
				BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientHeartBeatSocket.getInputStream()));
				String[] dataNodeValues = clientReader.readLine().split(":");
				
				Logger.getInstance().log(TAG, "Data Node " + dataNodeValues[1] + " is alive");
				logTable.put(Integer.parseInt(dataNodeValues[1]), new DataNodeStatus(System.currentTimeMillis(), clientHeartBeatSocket.getInetAddress().toString()));
			} catch (IOException e) {
				Logger.getInstance().log(TAG, "Error in opening heartbeat Listener port");
				//e.printStackTrace();
			} finally{
				if(heartBeatListenerSocket != null) {
					try {
						heartBeatListenerSocket.close();
					} catch (IOException e) {
						Logger.getInstance().log(TAG, "Error in closing heartbeat listener socket");
						//e.printStackTrace();
					}
					
				}
			}
			
			
		}
	}
	

}
