package com.uslotter.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class MySqliteHelp extends SQLiteOpenHelper {
	public static final String DB_NAME="zlpf.db";
	public static final String TABLE_NAME="record";
	public static final String DETAIL="mname";
	public static final int DETAIL_NUM=0;
	
	public static  SQLiteDatabase database;
	public static MySqliteHelp help;
	 
	public MySqliteHelp(Context context){
		super ( context , DB_NAME , null , 1 );
		
		
	}
	
	public static MySqliteHelp getInstance(Context context){
		if(help==null){
			help=new MySqliteHelp ( context );
		}
		if(database==null){
			database=help.getWritableDatabase ( );
		}
		return help;
	}
	

	
	public void close(){
		if(database!=null&&database.isOpen ( )){
			database.close ( );
		}
		if(help!=null){
			try {
	            help.clone ( );
            }
            catch ( CloneNotSupportedException e ) {
	            e.printStackTrace();
            }
		}
	}
	
	public long insert(String content){
		ContentValues cv=new ContentValues ( );
		cv.put ( DETAIL , content );
		return database.insertOrThrow ( TABLE_NAME , null , cv );
				
	}
	
	public ArrayList < String > getList(){
		Cursor cursor = database.query ( TABLE_NAME , new String []{DETAIL} , "cid=?" , new String[]{"17"} , null , null , null );
		ArrayList <  String  > list=new ArrayList < String > ( );
		while(cursor.moveToNext ( )){
			list.add ( cursor.getString ( DETAIL_NUM ) );
		}
		return list;
	}
	
	
	
	
	
	public MySqliteHelp ( Context context , String name , CursorFactory factory , int version ) {
	    super ( context , name , factory , version );
	}

	@Override
	public void onCreate ( SQLiteDatabase db ) {
		String create_table_world="create table "+TABLE_NAME+" ( "+DETAIL+" text not null "+")";
		db.execSQL ( create_table_world );
	
		
	}
	
	@Override
	public void onUpgrade ( SQLiteDatabase db , int oldVersion , int newVersion ) {
		db.execSQL ( "drop table if exists "+TABLE_NAME );
		onCreate ( db );
		
	}
	
}
