package com.uslotter.mode;

import java.io.Serializable;

public class Train implements Serializable{
	private static final long serialVersionUID = 2998494812743472027L;
	
	/**��ѵ��������*/
	private int trainThemeItem = 0 ;
	
	/**��ѵ�����*/
	private int trainObjectItem = 0 ;
	
	/**��ѵ��������*/
	private String trainTheme ;
	
	/**��ѵ��������*/
	private String trainObject ;
	
	/**��ע����*/
	private String remarks ;
	
	/**ͼƬ��ַ*/
	private String picPath ;

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public int getTrainThemeItem() {
		return trainThemeItem;
	}

	public void setTrainThemeItem(int trainThemeItem) {
		this.trainThemeItem = trainThemeItem;
	}

	public int getTrainObjectItem() {
		return trainObjectItem;
	}

	public void setTrainObjectItem(int trainObjectItem) {
		this.trainObjectItem = trainObjectItem;
	}

	public String getTrainTheme() {
		return trainTheme;
	}

	public void setTrainTheme(String trainTheme) {
		this.trainTheme = trainTheme;
	}

	public String getTrainObject() {
		return trainObject;
	}

	public void setTrainObject(String trainObject) {
		this.trainObject = trainObject;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
