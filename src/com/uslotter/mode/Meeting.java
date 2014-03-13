package com.uslotter.mode;

import java.io.Serializable;

public class Meeting implements Serializable{
	 private static final long serialVersionUID = -183648767712665463L;
	 private int themeItem ;
	 private String name ;
	 private String details;
	 private String noto ;
	 private String picPath;
	public int getThemeItem() {
		return themeItem;
	}
	public void setThemeItem(int themeItem) {
		this.themeItem = themeItem;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
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
