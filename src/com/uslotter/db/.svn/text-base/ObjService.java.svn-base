package com.uslotter.db;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class ObjService {
	private SQLiteDatabase db = null;
	private MyDataBase mdb = null;
	private Context c =null;
	public ObjService(Context c){
		mdb = new MyDataBase(c);
		this.c= c;
	}
	/**
	 * query
	 * @param db
	 * @param sql
	 * @return
	 */
	public   List<Obj> getAllAppsFromUserId(String userId){
		List<Obj>  lists = new ArrayList<Obj>();
		if(userId.equals("")||userId == null){
			return lists;
		}
		Cursor cursor = null;
		try{
		db = mdb.getReadableDatabase();
		cursor =db.query(Application.Obj.tName, null, Application.Obj.USERID+"=?", new String[]{userId}, null, null, Application.Obj.ID+" desc");
//		c =db.query(Application.Record.tableName, null, null, null, null, null, null);
		
		//c= db.rawQuery(Application.SQL.queryAllFromSameUserId,null);	 		
		 	if(cursor.moveToFirst()){
	 		do{
			Obj obj =new Obj();
			int id = cursor.getInt(cursor.getColumnIndex(Application.Obj.ID));
			String msg = cursor.getString(cursor.getColumnIndex(Application.Obj.MSG));
			//String userId = cursor.getString(cursor.getColumnIndex(Application.Obj.USERID));
			String state = cursor.getString(cursor.getColumnIndex(Application.Obj.STATE));
			String type =cursor.getString(cursor.getColumnIndex(Application.Obj.TYPE));
			String value = cursor.getString(cursor.getColumnIndex(Application.Obj.VALUE));

				obj.set_id(id);
			if(msg !=null){
			        obj.setMsg(msg);
			}else{
				obj.setMsg("");
			}
			
			if(state !=null){
				obj.setState(state);
			}else{
				obj.setState("");
				}
			
			if(type !=null){
				obj.setType(type); 
			}else{
				obj.setType("");
			}
			
			if(value!= null){
				obj.setValue(value);
			}else{
				obj.setValue("");
			}
			
			
			if(userId != null ||userId.equals("")){
				obj.setUserId(userId);
			}
			lists.add(obj);
			}while(cursor.moveToNext());
	 	}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cursor!=null){
			if(!cursor.isClosed()){
				cursor.close();
				cursor = null;
			}
			if(db.isOpen()){
			db.close();
			db = null;
			}
			}
		}
		return lists;
	}
	
	
	/**
	 * delete
	 */
	public  void deleteApp(int id){
		db = mdb.getWritableDatabase();;
		try{
		int r =db.delete(Application.Obj.tName, Application.Obj.ID+"=?", new String[]{""+id});
		if(r == 1){
		   // Toast.makeText(this.c, "删除成功",Toast.LENGTH_SHORT).show();
		}else if(r<=0){
		//	Toast.makeText(this.c, "删除失败",Toast.LENGTH_SHORT).show();	
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db.isOpen())
			db.close();
		}
		}

	
	
	/**
	 * insert
	 * @param db
	 * @param app
	 */
	public  long insert(Obj obj){
		try{
			db = mdb.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
	//	cv.put(Application.Obj.ID, obj.get_id());
		cv.put(Application.Obj.MSG, obj.getMsg());
		cv.put(Application.Obj.STATE, obj.getState());
		cv.put(Application.Obj.TYPE, obj.getType());
		cv.put(Application.Obj.USERID, obj.getUserId());
		cv.put(Application.Obj.VALUE, obj.getValue());
	    long r = db.insert(Application.Obj.tName, null, cv);
	    
		Toast.makeText(this.c, r+"插入数据成功",Toast.LENGTH_SHORT).show();
		return r;
		}catch(Exception e){	
			e.printStackTrace();
			return -1;
		}finally{
			if(db.isOpen())
			db.close();
		}
	}
}