package com.uslotter.mode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 终端维护实体类
 * @author liu
 *
 */
public class Terminal_maintenance implements Serializable {
	private static final long serialVersionUID = -183648767712665463L;
	/**服务类别*/
	private int category ;
	
	/**终端型号*/
	private int model ;
	
	/**整机序号*/
	private String machineNumber;
	
	/**处理方式*/
	private int processMode ;
	
	/**故障描述*/
	private String fault ;
	
	/**部件名称 */
	private int unitName;
	
	/**部件编号*/
	private String unitNumber ;
	
	/**图片路径*/
	private String picPath; 
	
	private String modelValue;
	
	public String getModelValue() {
		return modelValue;
	}

	public void setModelValue(String modelValue) {
		this.modelValue = modelValue;
	}

	/**动态加载维护部件*/
	private ArrayList<MaintenanceInfo> infos = new ArrayList<MaintenanceInfo>() ;
	
	public ArrayList<MaintenanceInfo> getInfos() {
		return infos;
	}

	public void setInfos(ArrayList<MaintenanceInfo> infos) {
		this.infos = infos;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public String getMachineNumber() {
		return machineNumber;
	}

	public void setMachineNumber(String machineNumber) {
		this.machineNumber = machineNumber;
	}

	public int getProcessMode() {
		return processMode;
	}

	public void setProcessMode(int processMode) {
		this.processMode = processMode;
	}

	public String getFault() {
		return fault;
	}

	public void setFault(String fault) {
		this.fault = fault;
	}

	public int getUnitName() {
		return unitName;
	}

	public void setUnitName(int unitName) {
		this.unitName = unitName;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
