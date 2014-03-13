package com.uslotter.mode;

import java.io.Serializable;

public class DateInfo implements Serializable {
	private static final long serialVersionUID = -183648767712665463L;
	private String name;
	private String number;
	private int index;
	private String mapValue;
	
	public String getMapValue() {
		return mapValue;
	}

	public void setMapValue(String mapValue) {
		this.mapValue = mapValue;
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
