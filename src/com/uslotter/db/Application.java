package com.uslotter.db;

public interface Application {
	interface Record{
		 public static final String tableName = "record";
		 public static final String ID = "_id";
		 public static final String USERID ="userId";
    	 public static final String WDBH = "wdbh";//������
    	 public static final String FWDH = "fwdh";//���񵥺�
    	 public static final String FWRQ = "fwrq";//��������
    	 public static final String PATH = "path";//����ԭ��
    	 public static final String  DF = "df";//�÷�
    	 public static final String  KFX= "kfx";//�۷���
    	 public static final String  STATE= "state";//����״̬
    	 public static final String  WDZP= "wdzp";//������Ƭ	
    	 public static final String  WCSJ= "wcsj";//���ʱ��
    	 public static final String  JBR= "jbr";//������	
    	 public static final String  FWPJ= "fwpj";//��������	
    	 public static final String  WDYJ= "wdyj";//�� �� �� �� :
    	 public static final String  ZJFX= "zjfx";//�ܽ����
    	 public static final String  ZGYJY= "zgyjy";//ר��Ա����
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