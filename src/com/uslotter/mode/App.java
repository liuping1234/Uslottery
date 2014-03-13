package com.uslotter.mode;

import java.io.Serializable;
import java.util.ArrayList;

public class App implements Serializable {

	private static final long serialVersionUID = -183648767712665463L;

	private int id;
	private String wdbh = "";
	private String fwdh = "";
	private String fwrq = "";
	private String fwyb = "";
	private String df = "";
	private String kfx = "";
	private String state = "";
	private String wdzp = "";
	private String userId = "";
	// 完成时间
	private String wcrq = "";
	// 经办人
	private String jbr = "";
	// 服务评价
	private String fwpj = "";
	// 网 点 意 见 :
	private String wdyj = "";
	// 总结分析
	private String zjfx = "";
	// 专管员建议
	private String zgyyj = "";
	private String network_satus = "";
	private String remark = "";
	// 违规照片
	private String wgzp = "";

	// 网点违规信息
	private ArrayList<WdwgInfo> info = new ArrayList<WdwgInfo>();

	// 录单多选控制
	private ArrayList<Multiple> multiples;

	// 培训活动
	private ArrayList<Train> trains;

	// 终端维护
	private ArrayList<Terminal_maintenance> maintenanceList;

	// 会议
	private ArrayList<Meeting> meetingList;

	// 宣传促销
	private ArrayList<Publicity> publicities;

	// 资料派发
	private ArrayList<DataAllot> dataAllots;

	// 其它服务
	private ArrayList<OtherServe> otherServes;

	// 网点改造
	private ArrayList<Remould> remoulds;

	// 建站勘察
	private ArrayList<NewProspect> prospects;

	// 移站勘察
	private ArrayList<MoveProspect> moveProspects;

	// 验收勘察
	private ArrayList<TestProspect> testProspects;

	public ArrayList<TestProspect> getTestProspects() {
		return testProspects;
	}

	public void setTestProspects(ArrayList<TestProspect> testProspects) {
		this.testProspects = testProspects;
	}

	public ArrayList<MoveProspect> getMoveProspects() {
		return moveProspects;
	}

	public void setMoveProspects(ArrayList<MoveProspect> moveProspects) {
		this.moveProspects = moveProspects;
	}

	public ArrayList<NewProspect> getProspects() {
		return prospects;
	}

	public void setProspects(ArrayList<NewProspect> prospects) {
		this.prospects = prospects;
	}

	public ArrayList<Remould> getRemoulds() {
		return remoulds;
	}

	public void setRemoulds(ArrayList<Remould> remoulds) {
		this.remoulds = remoulds;
	}

	public ArrayList<OtherServe> getOtherServes() {
		return otherServes;
	}

	public void setOtherServes(ArrayList<OtherServe> otherServes) {
		this.otherServes = otherServes;
	}

	public ArrayList<DataAllot> getDataAllots() {
		return dataAllots;
	}

	public void setDataAllots(ArrayList<DataAllot> dataAllots) {
		this.dataAllots = dataAllots;
	}

	public ArrayList<Publicity> getPublicities() {
		return publicities;
	}

	public void setPublicities(ArrayList<Publicity> publicities) {
		this.publicities = publicities;
	}

	public ArrayList<Meeting> getMeetingList() {
		return meetingList;
	}

	public void setMeetingList(ArrayList<Meeting> meetingList) {
		this.meetingList = meetingList;
	}

	public ArrayList<Terminal_maintenance> getMaintenanceList() {
		return maintenanceList;
	}

	public void setMaintenanceList(
			ArrayList<Terminal_maintenance> maintenanceList) {
		this.maintenanceList = maintenanceList;
	}

	public ArrayList<Train> getTrains() {
		return trains;
	}

	public void setTrains(ArrayList<Train> trains) {
		this.trains = trains;
	}

	public ArrayList<Multiple> getMultiples() {
		return multiples;
	}

	public void setMultiples(ArrayList<Multiple> multiples) {
		this.multiples = multiples;
	}

	public String getWgzp() {
		return wgzp;
	}

	public void setWgzp(String wgzp) {
		this.wgzp = wgzp;
	}

	public ArrayList<WdwgInfo> getInfo() {
		return info;
	}

	public void setInfo(ArrayList<WdwgInfo> info) {
		this.info = info;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNetwork_satus() {
		return network_satus;
	}

	public void setNetwork_satus(String network_satus) {
		this.network_satus = network_satus;
	}

	public String getWcrq() {
		return wcrq;
	}

	public void setWcrq(String wcrq) {
		this.wcrq = wcrq;
	}

	public String getJbr() {
		return jbr;
	}

	public void setJbr(String jbr) {
		this.jbr = jbr;
	}

	public String getFwpj() {
		return fwpj;
	}

	public void setFwpj(String fwpj) {
		this.fwpj = fwpj;
	}

	public String getWdyj() {
		return wdyj;
	}

	public void setWdyj(String wdyj) {
		this.wdyj = wdyj;
	}

	public String getZjfx() {
		return zjfx;
	}

	public void setZjfx(String zjfx) {
		this.zjfx = zjfx;
	}

	public String getZgyyj() {
		return zgyyj;
	}

	public void setZgyyj(String zgyyj) {
		this.zgyyj = zgyyj;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWdbh() {
		return wdbh;
	}

	public void setWdbh(String wdbh) {
		this.wdbh = wdbh;
	}

	public String getFwdh() {
		return fwdh;
	}

	public void setFwdh(String fwdh) {
		this.fwdh = fwdh;
	}

	public String getFwrq() {
		return fwrq;
	}

	public void setFwrq(String fwrq) {
		this.fwrq = fwrq;
	}

	public String getFwyb() {
		return fwyb;
	}

	public void setFwyb(String fwyb) {
		this.fwyb = fwyb;
	}

	public String getDf() {
		return df;
	}

	public void setDf(String df) {
		this.df = df;
	}

	public String getKfx() {
		return kfx;
	}

	public void setKfx(String kfx) {
		this.kfx = kfx;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getWdzp() {
		return wdzp;
	}

	public void setWdzp(String wdzp) {
		this.wdzp = wdzp;
	}

	@Override
	public String toString() {
		return wdbh + "," + fwdh + "," + fwrq + "," + fwyb + "," + df + ","
				+ kfx + "," + state + "," + wdzp;
	}

}
