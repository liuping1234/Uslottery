package com.uslotter.db;

public interface Application {
	interface Record{
		 public static final String tableName = "record";
		 public static final String ID = "_id";
		 public static final String USERID ="userId";
    	 public static final String WDBH = "wdbh";//网点编号
    	 public static final String FWDH = "fwdh";//服务单号
    	 public static final String FWRQ = "fwrq";//服务日期
    	 public static final String PATH = "path";//服务原表
    	 public static final String  DF = "df";//得分
    	 public static final String  KFX= "kfx";//扣分项
    	 public static final String  STATE= "state";//网点状态
    	 public static final String  WDZP= "wdzp";//网点照片	
    	 public static final String  WCSJ= "wcsj";//完成时间
    	 public static final String  JBR= "jbr";//经办人	
    	 public static final String  FWPJ= "fwpj";//服务评价	
    	 public static final String  WDYJ= "wdyj";//网 点 意 见 :
    	 public static final String  ZJFX= "zjfx";//总结分析
    	 public static final String  ZGYJY= "zgyjy";//专管员建议
    	 public static final String  NETWORK_STATUS = "network_satus";
    	 public static final String  REMARK = "remark";
    	 public static final String  WGNR = "wgnr";
    	 public static final String  WGXX = "wgxx";
    	 public static final String  INDEXS = "indexs";
     	
    	 class SQL{
    		 public static final String create = "create table if not exists record (" +
    	    	 		" _id integer primary key autoincrement ," +
    	    			"userId varchar(5),"+
    	    	 		" path varchar(50)," +
    	    	 		"wdbh varchar(8)," +
    	    	 		"fwdh varchar(10)," +
    	    	 		"fwrq varchar(30)," +
    	    	 		"df varchar(5)," +
    	    	 		"kfx varchar(1000)," +
    	    	 		"state varchar(10)," +
    	    	 		"wdzp varchar(1000)," +
    	    	 		"wcsj varchar(30)," +
    	    	 		"jbr varchar(10)," +
    	    	 		"fwpj varchar(50)," +
    	    	 		"wdyj varchar(400)," +
    	    	 		"zjfx varchar(400)," +
    	    	 		"zgyjy varchar(400),"+
    	    	 		"network_satus varchar(4),"+
    	    	 		"remark varchar(40)," +
    	    	 		"wgnr varchar(1000),"+
    	    	 		"wgxx varchar(1000),"+
    	    	 		"indexs varchar(100));";
    	 }
	}
	
	interface Obj{
		 public static final String tName = "obj";
		 public static final String ID = "_id";
		 public static final String TYPE = "type";
		 public static final String STATE = "state";
		 public static final String VALUE = "value";
		 public static final String USERID = "userId";
		 public static final String MSG = "msg";	
		 class Sql {
			 public static final String createObj = "create table if not exists obj (" +
		    	 		" _id integer primary key autoincrement ," +
		    			"type varchar(5),"+
		    	 		"state varchar(50)," +
		    	 		"value varchar(8)," +
		    	 		"userId varchar(10)," +
		    	 		"msg varchar(30));"; 
		 }
	}
}