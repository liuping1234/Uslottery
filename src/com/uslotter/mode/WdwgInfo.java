package com.uslotter.mode;

import java.io.Serializable;

public class WdwgInfo implements Serializable{
	private static final long serialVersionUID = 2998494812743472027L;

	// ����
	private int index = 0;
	// Υ��ѡ��
	private String wgxx = "";
	// Υ������
	private String wgnr = "";

	public int getIndex() {
	    return index;
	}

	public void setIndex(int index) {
	    this.index = index;
	}

	public String getWgxx() {
	    return wgxx;
	}

	public void setWgxx(String wgxx) {
	    this.wgxx = wgxx;
	}

	public String getWgnr() {
	    return wgnr;
	}

	public void setWgnr(String wgnr) {
	    this.wgnr = wgnr;
	}
}
