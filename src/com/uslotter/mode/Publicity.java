package com.uslotter.mode;

import java.io.Serializable;

/**
 * 宣传促销实体类
 * @author liu
 *
 */
public class Publicity implements Serializable{
	private static final long serialVersionUID = -183648767712665463L;
	private int themeItem ;
	private String noto ;
	private String picPath;
	public int getThemeItem() {
		return themeItem;
	}
	public void setThemeItem(int themeItem) {
		this.themeItem = themeItem;
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
