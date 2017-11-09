package com.rachit.models;

import java.io.Serializable;

public class ApplicationData implements Serializable {
	
	public String zone;
	
	public String data;
	
	public ApplicationData(String z, String d) {
		// TODO Auto-generated constructor stub
		this.zone = z;
		this.data = d;
	}

}
