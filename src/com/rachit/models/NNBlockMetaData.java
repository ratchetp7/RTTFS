package com.rachit.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Model class for storing file attributes
 * @author ratch
 *
 */
public class NNBlockMetaData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 33L;
	
	private int repFactor = 3;
	private int blockNumber;
	private int size;
	private ArrayList<String> addressList;
	
	
	public NNBlockMetaData(int blockNumber, int size, ArrayList<String> addressList) {
		this.blockNumber = blockNumber;
		this.size = size;
		addressList = new ArrayList<String>(addressList);
	}
	
	public int getRepFactor() {
		return repFactor;
	}

	public void setRepFactor(int repFactor) {
		this.repFactor = repFactor;
	}

	public int getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(int blockNumber) {
		this.blockNumber = blockNumber;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public ArrayList<String> getAddressList() {
		return addressList;
	}

	public void setAddressList(ArrayList<String> addressList) {
		this.addressList = addressList;
	}

}
