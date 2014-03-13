package com.uslotter.mode;

import java.io.Serializable;
import java.util.ArrayList;

public class DataAllot implements Serializable {
	private static final long serialVersionUID = -183648767712665463L;
	private int DataName;
	private String number;
	private String noto;
	private String picPath;
	private ArrayList<DateInfo> dateInfos = new ArrayList<DateInfo>();

	public ArrayList<DateInfo> getDateInfos() {
		return dateInfos;
	}

	public void setDateInfos(ArrayList<DateInfo> dateInfos) {
		this.dateInfos = dateInfos;
	}

	public int getDataName() {
		return DataName;
	}

	public void setDataName(int dataName) {
		DataName = dataName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNoto() {
		return noto;
	}

	public void setNoto(String noto) {
		this.noto = noto;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
}
