package com.uslotter.mode;

import java.io.Serializable;

public class MaintenanceInfo implements Serializable {
	private static final long serialVersionUID = 2998494812743472027L;
	private String name;
	private String number;
	private int index;
	private String nameValue;

	public String getNameValue() {
		return nameValue;
	}

	public void setNameValue(String nameValue) {
		this.nameValue = nameValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
