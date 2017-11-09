package com.rachit.models;

import java.util.ArrayList;

public class FSTableValue {
	private int lastBlock;
	private boolean locked;
	private ArrayList<NNBlockMetaData> blockList;
	
	
	public FSTableValue(int lastBlock, boolean locked) {
		this.lastBlock = lastBlock;
		this.locked = locked;
		blockList = new ArrayList<NNBlockMetaData>();
	}
	
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public int getLastBlock() {
		return lastBlock;
	}
	public void setLastBlock(int lastBlock) {
		this.lastBlock = lastBlock;
	}
	public ArrayList<NNBlockMetaData> getBlockList() {
		return blockList;
	}
	public void setBlockList(ArrayList<NNBlockMetaData> blockList) {
		this.blockList = blockList;
	}
}
