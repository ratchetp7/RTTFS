package com.rachit.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.rachit.models.AppConfiguration;


/**
 * 
 * @author ratch
 * Configuration reader class. Specified by the conf.properties file
 */
public class ConfReader {
	private final String TAG = "ConfReader";
	private static ConfReader instance = null;
	private File configFile;
		
	public void readConfiguration(AppConfiguration config){
		try {
				configFile = new File(Constants.CONF_FILE_NAME);
				FileReader reader = new FileReader(configFile);
				Properties props = new Properties();
				String tempProperty = null;
				props.load(reader);
				
				tempProperty = props.getProperty("datanodeserver");
				config.setDataNodeServer (tempProperty != null ? tempProperty:Constants.DATANODE_SERVER);

				tempProperty = props.getProperty("namenodeserver");
				config.setNameNodeServer (tempProperty != null ? tempProperty:Constants.NAMENODE_SERVER);
				
				tempProperty = props.getProperty("mode");
				config.setMode (tempProperty != null ? Integer.parseInt(tempProperty):Constants.MODE);
				
				tempProperty = props.getProperty("namenodeport");
				config.setNameNodePort (tempProperty != null ? Integer.parseInt(tempProperty):Constants.NAMENODE_PORT);
				
				tempProperty = props.getProperty("datanodeport");
				config.setDataNodePort (tempProperty != null ? Integer.parseInt(tempProperty):Constants.DATANODE_PORT);
				
				tempProperty = props.getProperty("applicationclientport");
				config.setApplicationClientPort (tempProperty != null ? Integer.parseInt(tempProperty):Constants.APPLICATION_CLIENT_PORT);
				
				tempProperty = props.getProperty("applicationclientserver");
				config.setApplicationClientServer (tempProperty != null ? tempProperty: Constants.APPLICATION_CLIENT_SERVER);
				
				tempProperty = props.getProperty("namenodeheartbeatport");
				config.setNameNodeHeartBeatport (tempProperty != null ? Integer.parseInt(tempProperty): Constants.NAME_NODE_HEARTBEAT_PORT);
				
				tempProperty = props.getProperty("datanodeid");
				config.setDataNodeId (tempProperty != null ? Integer.parseInt(tempProperty): Constants.DATA_NODE_ID);
				
				tempProperty = props.getProperty("datanodetimeout");
				config.setDataNodeTimeOut (tempProperty != null ? Long.parseLong(tempProperty): Constants.DATA_NODE_TIMEOUT);
		
				reader.close();
			} catch (FileNotFoundException fne) {
				Logger.getInstance().log(TAG, "Configuration file not found. Setting default values");
				setDefaultValues(config);
			} catch (IOException ioe) {
				Logger.getInstance().log(TAG, "Error while Configuration file reading. Setting default values");
				setDefaultValues(config);
			}
		Logger.getInstance().display(TAG + ":Data Node- " + config.getDataNodeServer() + ":" + config.getDataNodePort());
		Logger.getInstance().display(TAG + ":Name Node- " + config.getNameNodeServer() + ":" + config.getNameNodePort());
	}
	
	
	//create as a singleton class
	public static ConfReader getInstance() {
		if(instance == null) {
			instance = new ConfReader();
		}
		return instance;
	}
	
	private void setDefaultValues(AppConfiguration config) {
		config.setDataNodePort(Constants.DATANODE_PORT);
		config.setDataNodeServer(Constants.DATANODE_SERVER);
		config.setNameNodePort(Constants.NAMENODE_PORT);
		config.setNameNodeServer(Constants.NAMENODE_SERVER);
	}
}
