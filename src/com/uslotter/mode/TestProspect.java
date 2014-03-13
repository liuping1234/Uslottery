package com.uslotter.mode;

import java.io.Serializable;

public class TestProspect implements Serializable {
	private static final long serialVersionUID = -183648767712665463L;
	private String siteValue;
	private int siteItem;
	private String site ;
	private String branch;
	private String number;
	private int typeItem;
	private int verify1;
	private int verify2;
	private String explain1;
	private String explain2;
	private String noto;
	private String picPath;
	
	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}


	public String getSiteValue() {
		return siteValue;
	}

	public void setSiteValue(String siteValue) {
		this.siteValue = siteValue;
	}

	public int getSiteItem() {
		return siteItem;
	}

	public void setSiteItem(int siteItem) {
		this.siteItem = siteItem;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getTypeItem() {
		return typeItem;
	}

	public void setTypeItem(int typeItem) {
		this.typeItem = typeItem;
	}

	public int getVerify1() {
		return verify1;
	}

	public void setVerify1(int verify1) {
		this.verify1 = verify1;
	}

	public int getVerify2() {
		return verify2;
	}

	public void setVerify2(int verify2) {
		this.verify2 = verify2;
	}

	public String getExplain1() {
		return explain1;
	}

	public void setExplain1(String explain1) {
		this.explain1 = explain1;
	}

	public String getExplain2() {
		return explain2;
	}

	public void setExplain2(String explain2) {
		this.explain2 = explain2;
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
