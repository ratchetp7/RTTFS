package com.rachit.deamon.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.rachit.deamon.NameNode;
import com.rachit.models.DataNodeStatus;
import com.rachit.models.FSTableKey;
import com.rachit.models.FSTableValue;
import com.rachit.models.NNBlockMetaData;
import com.rachit.models.NNFileMetaData;
import com.rachit.utils.Logger;

public class NameNodeRequestHandlerThread implements Runnable {
	private Socket clientSocket = null;
	private NameNode nameNode;

	private final String TAG = "NameNodeRequestHandlerThread";

	public NameNodeRequestHandlerThread(NameNode nameNode ,Socket clientSocket) {
		this.clientSocket = clientSocket;
		this.nameNode = nameNode;
	}
	@Override
	public void run() {
		Logger.getInstance().display(TAG + " new client request");
		//TODO check whether read or write
		String fileName = null;
		ObjectOutputStream outToClient = null;

		try {
			//create input stream attached to socket
			ObjectInputStream inFromClient = new ObjectInputStream (clientSocket.getInputStream());
			//create output stream attached to socket
			outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
			//remove :Event clientSentence = (Event)inFromClient.readObject();
			ArrayList<String> ipList = processWrite(new FSTableKey(fileName));
			if(ipList != null) {
				outToClient.writeObject(ipList);
				outToClient.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(outToClient != null)
				try {
					outToClient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}


	}

	private void processRead() {

	}

	private ArrayList<String> processWrite(FSTableKey fileName) {
		FSTableValue fileMetaData = nameNode.fsImage.get(fileName);
		if(fileMetaData == null) {
			ArrayList<DataNodeStatus> aliveNodes = nameNode.getAliveNodes();
			int aliveNodeCount = aliveNodes.size();

			int bitvector = (int)Math.pow(2, aliveNodeCount) - 1;
			//generate three random positions
			int[] threeRandom = new int[3];
			for(int i = 0; i < 3; i++) {
				int select = (int) (Math.random() * aliveNodeCount);
				if(((bitvector >> select) & 1) == 0) {
					i--;
				}else {
					bitvector = (1 << select) & bitvector;
					threeRandom[i] = select;
				}
			}
			
			ArrayList<String> newIpList = new ArrayList<String>();
			for(int i : threeRandom) {
				newIpList.add(aliveNodes.get(i).getInetAddress());
			}
			
			FSTableValue newTableValue = new FSTableValue(0, true);
			ArrayList<NNBlockMetaData> newMetaDataList = newTableValue.getBlockList();
			
			NNBlockMetaData newBlockMetaData = new NNBlockMetaData(0,1,newIpList);
			newMetaDataList.add(newBlockMetaData);
			//TODO populate the entry for the table
			
			return newIpList;
		}else {
			try {
				NNFileMetaData lastBlockMetadata = fileMetaData.getBlockList().get(fileMetaData.getLastBlock());
				return lastBlockMetadata.getAddressList();
			}catch(IndexOutOfBoundsException ioe) {
				Logger.getInstance().log(TAG, "Error in metadata for " + fileName.getFileName() + "IndexOutOfBound");
				ioe.printStackTrace();
			}catch(NullPointerException npe) {
				Logger.getInstance().log(TAG, "Error in metadata for " + fileName.getFileName() + "NullPointerException");
			}
			return null;
		}
	}
}
