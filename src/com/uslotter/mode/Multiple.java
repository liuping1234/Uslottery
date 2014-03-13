package com.uslotter.mode;

import java.io.Serializable;

public class Multiple implements Serializable{
	private static final long serialVersionUID = 2998494812743472027L;
	private String mulIndex;
	private String mulState;

	public String getMulIndex() {
	    return mulIndex;
	}

	public void setMulIndex(String mulIndex) {
	    this.mulIndex = mulIndex;
	}

	public String getMulState() {
	    return mulState;
	}

	public void setMulState(String mulState) {
	    this.mulState = mulState;
	}
}
