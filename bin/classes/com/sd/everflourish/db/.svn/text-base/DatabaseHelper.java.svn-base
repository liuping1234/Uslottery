package com.sd.everflourish.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	String sql="create table BagNum (id integer primary key,BagNumber text);";//创建扫描包表
	String sql1="create table CardNum (id integer primary key,Sid text,Date datetime);";//抽过奖的彩票序号表
	String sql2="create table cr_drawgrade(gradeNo integer primary key," +
			"gradeName text,drawCount integer,drawName text,drawNo text,outCount integer,Count integer);";
	String sql3="insert into cr_drawgrade(gradeName,drawCount,drawName,drawNo,outCount,Count) values('一等奖',0,null,null,0,0);";
	String sql4="insert into cr_drawgrade(gradeName,drawCount,drawName,drawNo,outCount,Count) values('二等奖',0,null,null,0,0);";
	String sql5="insert into cr_drawgrade(gradeName,drawCount,drawName,drawNo,outCount,Count) values('三等奖',0,null,null,0,0);";
	String sql6="insert into cr_drawgrade(gradeName,drawCount,drawName,drawNo,outCount,Count) values('四等奖',0,null,null,0,0);";
	String sql7="insert into cr_drawgrade(gradeName,drawCount,drawName,drawNo,outCount,Count) values('五等奖',0,null,null,0,0);";
	String sql8="insert into cr_drawgrade(gradeName,drawCount,drawName,drawNo,outCount,Count) values('六等奖',0,null,null,0,0);";
	String sql13="create table cr_winNum(id integer primary key,gradeNo integer,state integer,Sid text,gradeName text,drawName text,day datetime);";//中奖的彩票序号
	
	public DatabaseHelper(Context context,String name, CursorFactory factory,int version) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql);
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.execSQL(sql3);
		db.execSQL(sql4);
		db.execSQL(sql5);
		db.execSQL(sql6);
		db.execSQL(sql7);
		db.execSQL(sql8);
		
		db.execSQL(sql13);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS bag_info");
		onCreate(db);	
	}
}
