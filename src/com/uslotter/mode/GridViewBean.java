package com.uslotter.mode;

import java.io.Serializable;

import android.graphics.Bitmap;


/**
 * @author liu 
 *path ��Ƭ·��
 *checkState ͼƬ�Ƿ�Ϊ��ѡ��״̬
 *bm λͼ
 *state ״̬
 */
public class GridViewBean implements Serializable {

    private static final long serialVersionUID = 4243041560675350230L;

    private boolean checkState;

    private String path;

    private Bitmap bm;

    private int states;

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public Bitmap getBm() {
	return bm;
    }

    public void setBm(Bitmap bm) {
	this.bm = bm;
    }

    public boolean isCheckState() {
	return checkState;
    }

    public void setCheckState(boolean checkState) {
	this.checkState = checkState;
    }

	public int getStates() {
		return states;
	}

	public void setStates(int states) {
		this.states = states;
	}

   
}
