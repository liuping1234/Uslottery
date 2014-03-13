package com.uslotter.data;

public class Option {
	private String id = null;
	private String childnum = null;
	private String score = null;
	private String intro = null;
	public Option(){}
	public Option(String id,String childnum,String score,String intro){
		this.id = id;
		this.childnum = childnum;
		this.score = score;
		this.intro = intro;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChildnum() {
		return childnum;
	}
	public void setChildnum(String childnum) {
		this.childnum = childnum;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	
}
