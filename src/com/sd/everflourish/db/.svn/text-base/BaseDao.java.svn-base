package com.sd.everflourish.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BaseDao {
	static DatabaseHelper util=null;
	static SQLiteDatabase db = null;
	
	public static SQLiteDatabase getDb(){
		return db;
	}
	public BaseDao() {
	}
	
	public  BaseDao(Context context) {
		BaseDao.createDB(context);
	}
	
	public static SQLiteDatabase createDB(Context context){
		util = new DatabaseHelper(context, "bag_info.db", null, 1);
		try {
		db = util.getWritableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
			//数据库不能执行添/删/改
			db = util.getReadableDatabase();
		}
		return db;
	}
	public static void closeDB(SQLiteDatabase db){
		try {
		if (db != null) {
			db.close();
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
