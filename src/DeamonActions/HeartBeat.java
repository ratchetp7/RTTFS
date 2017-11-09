package DeamonActions;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.rachit.utils.Constants;
import com.rachit.utils.Logger;



/**
 * 
 * @author rachit
 * A heartbeat generator thread invoked every Constants.sleepTime interval
 */
public class HeartBeat implements Runnable{
	private Logger logger = Logger.getInstance();
	private Socket socket = null;
	private String message;
	private int id;
	private String host;
	private int port;
	private int counter;
	private boolean enableHeartBeat = true;
	private final String TAG = "HeartBeat";

	public HeartBeat(String host, int port, int id, String message) {
		this.message = message;
		this.id = id;		
		this.host = host;
		this.port = port;
	}

	@Override
	public void run() {
		while(enableHeartBeat) {
			try {
				socket = new Socket(host, port);
			} catch (UnknownHostException e) {
				logger.log(TAG, "Host not found: " + host + ":" + port);
				//e.printStackTrace();
			} catch (IOException e) {
				logger.log(TAG,"IO exception while opening socket connection");
				//e.printStackTrace();
			}

			try {
				Thread.sleep(Constants.SLEEP_TIME);
				if(socket != null)
					sendHeartBeat();
			
			}catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				if(socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * sends the periodic heartbeat message to the NameNode
	 */
	private void sendHeartBeat(){
		try {
			counter++;
			//create output stream attached to socket
			PrintWriter outToServer;
			outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			//send msg to server
			outToServer.println(counter + ":" + id + ":" + message + '\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void stopHeartBeat() {
		enableHeartBeat = false;
	}

}

