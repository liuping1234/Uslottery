package com.uslotter.mode;

public class ListResult {
private String id;
private String resid;
private String reDesc;
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getResid() {
	return resid;
}
public void setResid(String resid) {
	this.resid = resid;
}
public String getReDesc() {
	return reDesc;
}
public void setReDesc(String reDesc) {
	this.reDesc = reDesc;
}

@Override
public String toString() {
	String str="id="+id+"    resid="+resid+"      reDesc"+reDesc;
	return str;
}
}
