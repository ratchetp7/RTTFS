package com.rachit.deamon;

import com.rachit.models.AppConfiguration;
import com.rachit.utils.ConfReader;
import com.rachit.utils.Constants;
import com.rachit.utils.Logger;

public class RTFSService {
	protected int modes;
	protected String host = null;
	protected int port;
	
	
	public AppConfiguration globalConf;
	
	DataNode dataNode = null;
	NameNode nameNode = null;
	NameNode standbyNameNode = null;
	
	

	public static void main(String[] args) {
		//initialize all services
		RTFSService motherService = new RTFSService();
		motherService.initDaemons();	

	}

	private void initDaemons() {
		//Load configuration for the application
		globalConf = new AppConfiguration();
		ConfReader.getInstance().readConfiguration(globalConf);
		modes = globalConf.getMode();
		//initialize the required services
		//lsb is for NameNode
		if(((modes) & 1) == 1) {
			nameNode = new NameNode(this);
			Thread nameNodeThread = new Thread(nameNode);
			nameNodeThread.start();
			Logger.getInstance().display("Name Node Started");
		}
		
		//second lsb is for standby NameNode
		if(((modes >> 1) & 1) == 1) {
			standbyNameNode = new NameNode(this);
			Thread standByNameNodeThread = new Thread(nameNode);
			standByNameNodeThread.start();
			Logger.getInstance().display("Standby Name Node Started");
		}
		
		//third lsb is for dataNode
		if(((modes >> 2) & 1) == 1) {
			dataNode = new DataNode(this);
			Thread dataNodeThread = new Thread(dataNode);
			dataNodeThread.start();
			Logger.getInstance().display("Data Node Started with id:" + globalConf.getDataNodeId());
		}
		
		//fourth lsb is for ApplicationClient Node
		if(((modes >> 3) & 1) == 1) {
			ApplicationClient applicationClient = new ApplicationClient(this);
			Thread applicationClientThread = new Thread(applicationClient);
			applicationClientThread.start();
			Logger.getInstance().display("Application Client Started");
		}
		
		
	}

}
