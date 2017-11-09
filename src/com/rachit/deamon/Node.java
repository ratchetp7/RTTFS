package com.rachit.deamon;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.rachit.models.Event;

public class Node  {
	public int logicalClock;
	public int logicalCount;
	public int physicalClock;
	
	
	Node()
	{
		logicalClock = 0;
		logicalCount = 0;
		physicalClock = getPhysicalTime();
	}
	
	public Node(Node node) {
		this.logicalClock = node.logicalClock;
		this.logicalCount = node.logicalClock;
		this.physicalClock = node.physicalClock;
	}
	public static int getPhysicalTime()
	{
		 int currentTime = LocalDateTime.now().getSecond();
		 
		 return currentTime;
	}
	
	public void updateNodeTimes(int logicalClock, int logicalCount)
	{
		this.logicalClock = logicalClock;
		this.logicalCount = logicalCount;
		this.physicalClock = getPhysicalTime();
		
	}
	
	
	public <T> void timeStampSend(Event<T> message)
	{
		this.updateNodeTimes(this.logicalClock, this.logicalCount);
		Node tempNode = new Node(this);
		
		
		this.logicalClock = Math.max(tempNode.logicalClock,this.physicalClock);
		
		if(tempNode.logicalClock == this.physicalClock){
			this.logicalCount = this.logicalCount + 1;
			
		}else{
			this.logicalCount = 0;
		}
		
		message.applyTimeStamp(logicalClock, logicalCount, physicalClock);
		this.updateNodeTimes(this.logicalClock, this.logicalCount);
		
		/*if(message instanceof String) {
			return new Event<String>(this.logicalClock,this.logicalCount,this.physicalClock,(String) message,write);
			
		}else {
			return new Event<ArrayList<String>>(this.logicalClock,this.logicalCount,this.physicalClock,(ArrayList<String>) message,write);
		}	*/ 
	}
	
	public void timeStampReceive(Event<?> rEvent)
	{
		this.updateNodeTimes(this.logicalClock, this.logicalCount);
		
		Node tempNode = new Node(this);
		this.logicalClock = Math.max(tempNode.logicalClock,rEvent.logicalClock);
		this.logicalClock = Math.max(this.logicalClock,this.physicalClock);
		
		if( (this.logicalClock == tempNode.logicalClock) && (this.logicalClock == rEvent.logicalClock))
				{
			this.logicalCount = Math.max(tempNode.logicalCount, rEvent.logicalCount) + 1;
				}
		else if(this.logicalClock == tempNode.logicalClock)
		{
			this.logicalCount = tempNode.logicalCount + 1;
		}
		else if(this.logicalClock == rEvent.logicalClock)
		{
			this.logicalCount = rEvent.logicalCount + 1;
		}
		else
		{
			this.logicalCount = 0;
		}
		this.updateNodeTimes(this.logicalClock, this.logicalCount);
	}

	 
	

	 
	
   

}
