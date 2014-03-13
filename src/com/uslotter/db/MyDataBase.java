package com.uslotter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDataBase extends SQLiteOpenHelper {
	private Context c = null;

	public MyDataBase(Context context) {
		super(context, "yj.db", null, 2);
		c = context;
		
	}
	
	public MyDataBase(Context context,String name,int version) {
		super(context, name, null, version);
		c = context;
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Application.Record.SQL.create);
		db.execSQL(Application.Obj.Sql.createObj);
		//Toast.makeText(c,Application.Record.SQL.create, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		 db.execSQL("DROP TABLE IF EXISTS record");         
		 db.execSQL("DROP TABLE IF EXISTS obj");         
	     onCreate(db);
	}
}