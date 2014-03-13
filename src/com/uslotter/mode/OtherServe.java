package com.uslotter.mode;

import java.io.Serializable;

/**
 * 其它服务实体类
 * 
 * @author liu
 * 
 */
public class OtherServe implements Serializable {
	private static final long serialVersionUID = -183648767712665463L;
	
	private String number ;
	private String name ;
	private String details ; 
	private String noto ;
	private String picPath;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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
