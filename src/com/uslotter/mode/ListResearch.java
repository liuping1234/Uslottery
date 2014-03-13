package com.uslotter.mode;

public class ListResearch {
	private String id;
	private String topic;
	private String subid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getSubid() {
		return subid;
	}
	public void setSubid(String subid) {
		this.subid = subid;
	}
	
	@Override
	public String toString() {
		String str="id="+id+"    topic="+topic+"      subid"+subid;
		return str;
	}
	
}
