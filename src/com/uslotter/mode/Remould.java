package com.uslotter.mode;

import java.io.Serializable;

public class Remould implements Serializable {
	private static final long serialVersionUID = -183648767712665463L;
	private String number ;
	private String dateDicker;
	private String noto; 
	private String price ;
	private String picPath;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDateDicker() {
		return dateDicker;
	}

	public void setDateDicker(String dateDicker) {
		this.dateDicker = dateDicker;
	}

	public String getNoto() {
		return noto;
	}

	public void setNoto(String noto) {
		this.noto = noto;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
}
