package com.sd.everflourish.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


public class SqliteHandle extends BaseDao{
	SimpleDateFormat sdf;
	public SqliteHandle(Context ctxt) {
		//BaseDAO(Context);
		super(ctxt);
	}
	
	public static List<Map<String,String>> findAllPerson(){
		List<Map<String,String>> allInfo = 
				new ArrayList<Map<String,String>>();
		String sql="select * from BagNum;";
		Cursor cursor =db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			 Map<String, String> paritem = new HashMap<String, String>();
			 int id = cursor.getInt
						(cursor.getColumnIndex("id"));
			 String bagNum=cursor.getString(cursor.getColumnIndex("BagNumber"));
			 paritem.put("id", id+"");
			 paritem.put("BagNumber", bagNum+"");
			 allInfo.add(paritem);
		}
		cursor.close();
		return allInfo;
		
	}
	
	
	public static List<Map<String, String>> findLotteryNum(String sql){
		List<Map<String, String>> list=new ArrayList<Map<String,String>>();
		Cursor cursor =db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			 Map<String, String> paritem = new HashMap<String, String>();
			int gradeNo=cursor.getInt
					(cursor.getColumnIndex("gradeNo"));
			String gradeName=cursor.getString(cursor.getColumnIndex("gradeName"));
			int drawCount=cursor.getInt
					(cursor.getColumnIndex("drawCount"));
			String drawName=cursor.getString(cursor.getColumnIndex("drawName"));
			String drawNo=cursor.getString(cursor.getColumnIndex("drawNo"));
			int outCount=cursor.getInt
			(cursor.getColumnIndex("outCount"));
			int Count=cursor.getInt
					(cursor.getColumnIndex("Count"));
			
			paritem.put("gradeNo", gradeNo+"");
			paritem.put("gradeName", gradeName);
			paritem.put("drawCount", drawCount+"");
			paritem.put("drawName", drawName);
			paritem.put("drawNo", drawNo);
			paritem.put("outCount", outCount+"");
			paritem.put("Count", Count+"");
			
			list.add(paritem);
		}
		cursor.close();
		//db.close();
		return list;
	}
	
	public static void Count(String count,String gradename){
		int sum=Integer.parseInt(count);
		sum++;
		ContentValues values=new ContentValues();
		values.put("Count", sum);
		String where="gradeName='"+gradename+"'";
		db.update("cr_drawgrade", values, where, null);
	}
	
	
	public static int findBynum(String num,String sql){//按num查询
			Cursor cursor = db.rawQuery(sql, new String[]{num});
			if(cursor.moveToFirst()==false){
				cursor.close();
				return 0;
			}
			cursor.close();
			//db.close();
		return -1;
		}
	
	public static int Insert(String pack,ContentValues values){//插入数据
		/*ContentValues values = new ContentValues();
		values.put("BagNumber", num);*/
		long i=db.insert(pack, null, values);
		
		if(i!=-1){
			return 0;
		}else{
			return -3;
		}
	}
	
	public static int delete(String num){//删除数据
		int m=0;
		int i=db.delete("BagNum", "BagNumber=?", new String[]{num});
		if(i>0){
			m=1;
		}else{
			m=-1;
		}
		return m;
	}
	
	public static List<Map<String,String>> queryForList(String sql,String num){
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		Cursor cursor = db.rawQuery(sql, 
				new String[]{num});
		 while(cursor.moveToNext())
         {
			 Map<String, String> paritem = new HashMap<String, String>();
			 paritem.put("id", cursor.getString(0));
			 paritem.put("Bagnumber", cursor.getString(1));
			 list.add(paritem);
         }
         cursor.close();
         return list;
     }
	
	public static int upDateState(String sid){
		int state=-2;
		ContentValues values=new ContentValues();
		values.put("state", 1);
		String where="Sid = ?";
		String sql="select state from cr_winNum where Sid=?;";
		Cursor cursor=db.rawQuery(sql, new String[]{sid});
		while(cursor.moveToNext()){
			state=cursor.getInt(0);
		}	
		if(state==0){
			db.update("cr_winNum", values, where, new String[]{sid});
			cursor.close();
			return 0;
		}
		else{
			cursor.close();
			return 1;
		}
	}
	
	public static int RetCount(String sql){
		int count=0;
		Cursor cursor =db.rawQuery(sql, null);
		while(cursor.moveToNext()){
			 count=cursor.getInt
						(cursor.getColumnIndex("outCount"));
		}
		return count;
	}
	
	public static void updateState1(String cardId){
		ContentValues values=new ContentValues();
		values.put("state", 1);
		String where="Sid="+cardId;
		db.update("cr_winNum", values, where, null);
	}
	
	
	public static void updateState2(String cardId){
		ContentValues values=new ContentValues();
		values.put("state", 0);
		String where="Sid="+cardId;
		db.update("cr_winNum", values, where, null);
	}
	
	
	public static void upDataOutCount(int count,int gradeNo){//每次中奖OutCount就加1直到等于奖品数
		ContentValues values=new ContentValues();
		count++;
		values.put("outCount", count);
		String where="gradeNo="+gradeNo;
		db.update("cr_drawgrade", values, where, null);
	}
	
	public static long Prize(Map<Integer,String> map){
		ContentValues values=new ContentValues();
		for(int i=1;i<=6;i++){
			values.put("drawName", map.get(i));
		}
		long i=db.insert("cr_drawgrade", null, values);
		return i;
	}
	
	public static int InsertDrawGrade(Map<String,String> maps){
		int error=0;
		int count=0;
		String num="0";
		String range="0";
		try{
			ContentValues values1=new ContentValues();
			num=maps.get("grade1");
			range="0"+","+num;
			count=num.equals("")?0:Integer.parseInt(num);
			
			values1.put("drawCount", maps.get("grade1"));
			values1.put("drawName", maps.get("drawname1"));
			values1.put("drawNo",range);
			db.update("cr_drawgrade", values1, "gradeNo=1;", null);
			
			ContentValues values2=new ContentValues();
			num=maps.get("grade2");
			int count2=num.equals("")?0:Integer.parseInt(num)+count;
			range=count+","+count2;
			if(count2!=0){
				values2.put("drawCount", maps.get("grade2"));
				values2.put("drawName", maps.get("drawname2"));
				values2.put("drawNo",range);
				db.update("cr_drawgrade", values2, "gradeNo=2;", null);
			}
			ContentValues values3=new ContentValues();
			num=maps.get("grade3");
			int count3=num.equals("")?0:Integer.parseInt(num)+count2;
			range=count2+","+count3;
			if(count3!=0){
				values3.put("drawCount", maps.get("grade3"));
				values3.put("drawName", maps.get("drawname3"));
				values3.put("drawNo",range);
				db.update("cr_drawgrade", values3, "gradeNo=3;", null);
			}
			ContentValues values4=new ContentValues();
			num=maps.get("grade4");
			int count4=num.equals("")?0:Integer.parseInt(num)+count3;
			range=count3+","+count4;
			if(count4!=0){
				values4.put("drawCount", maps.get("grade4"));
				values4.put("drawName", maps.get("drawname4"));
				values4.put("drawNo",range);
				db.update("cr_drawgrade", values4, "gradeNo=4;", null);
			}
			ContentValues values5=new ContentValues();
			num=maps.get("grade5");
			int count5=num.equals("")?0:Integer.parseInt(num)+count4;
			range=count4+","+count5;
			if(count5!=0){
				values5.put("drawCount", maps.get("grade5"));
				values5.put("drawName", maps.get("drawname5"));
				values5.put("drawNo",range);
				db.update("cr_drawgrade", values5, "gradeNo=5;", null);
			}
			ContentValues values6=new ContentValues();
			num=maps.get("grade6");
			int count6=num.equals("")?0:Integer.parseInt(num)+count5;
			range=count5+","+count6;
			if(count6!=0){
				values6.put("drawCount", maps.get("grade6"));
				values6.put("drawName", maps.get("drawname6"));
				values6.put("drawNo",range);
				db.update("cr_drawgrade", values6, "gradeNo=6;", null);
			}
		}catch(Exception e){
			error=-1;
		}
		return error;
	}
	
	public static void deleteTable(){
		String sql="DROP TABLE BagNum";
		String sql1="DROP TABLE CardNum";
		String sql2="DROP TABLE cr_drawgrade";
		String sql3="DROP TABLE cr_winNum";
		String sql4="create table BagNum (id integer primary key,BagNumber text);";
		String sql5="create table CardNum (id integer primary key,Sid text,Date datetime);";
		
		String sql6="create table cr_drawgrade(gradeNo integer primary key," +
				"gradeName text,drawCount integer,drawName text,drawNo text,outCount integer,Count integer);";
		String sql7="create table cr_winNum(id integer primary key,gradeNo integer," +
				"state integer,Sid text,gradeName text,drawName text,day datetime);";
		String sql8="insert into cr_drawgrade(gradeName,drawCount,drawName,drawNo,outCount,Count) values('一等奖',0,null,null,0,0);";
		String sql9="insert into cr_drawgrade(gradeName,drawCount,drawName,drawNo,outCount,Count) values('二等奖',0,null,null,0,0);";
		String sql10="insert into cr_drawgrade(gradeName,drawCount,drawName,drawNo,outCount,Count) values('三等奖',0,null,null,0,0);";
		String sql11="insert into cr_drawgrade(gradeName,drawCount,drawName,drawNo,outCount,Count) values('四等奖',0,null,null,0,0);";
		String sql12="insert into cr_drawgrade(gradeName,drawCount,drawName,drawNo,outCount,Count) values('五等奖',0,null,null,0,0);";
		String sql13="insert into cr_drawgrade(gradeName,drawCount,drawName,drawNo,outCount,Count) values('六等奖',0,null,null,0,0);";
		
		db.execSQL(sql);
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.execSQL(sql3);
		db.execSQL(sql4);
		db.execSQL(sql5);
		db.execSQL(sql6);
		db.execSQL(sql7);
		db.execSQL(sql8);
		db.execSQL(sql9);
		db.execSQL(sql10);
		db.execSQL(sql11);
		db.execSQL(sql12);
		db.execSQL(sql13);
		
	}
	
	public static List<Map<String, String>> findAllNum(){
		List<Map<String, String>> list=new ArrayList<Map<String,String>>();
		String sql="select id,Sid,Date from CardNum;";
		Cursor cursor =db.rawQuery(sql, null);
		/*if(cursor.moveToFirst()==false){
			Map<String, String> paritem = new HashMap<String, String>();
			paritem.put("id", "空");
			 paritem.put("Sid", "空");
			 paritem.put("time", "空");
			 list.add(paritem);
		}
		else{*/
			while(cursor.moveToNext()){
				Map<String, String> paritem = new HashMap<String, String>();
				 int id = cursor.getInt
							(cursor.getColumnIndex("id"));
				 String Sid=cursor.getString(cursor.getColumnIndex("Sid"));
				 String time=cursor.getString(cursor.getColumnIndex("Date"));
				 paritem.put("id", id+"");
				 paritem.put("Sid", Sid);
				 paritem.put("time", time);
				 list.add(paritem);
			//}
		}
		cursor.close();
		return list;
	}
	public static List<Map<String,String>> FindGrade(){//等级获取设奖个数和等级
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		String sql="select gradeName,drawCount,outCount,Count from cr_drawgrade where drawCount !=0;";
		Cursor cursor =db.rawQuery(sql, null);
		
		while(cursor.moveToNext()){
			Map<String,String> maps=new HashMap<String, String>();
			String name=cursor.getString(cursor.getColumnIndex("gradeName"));
			int dcount = cursor.getInt
					(cursor.getColumnIndex("drawCount"));
			int scount=cursor.getInt
					(cursor.getColumnIndex("Count"));
			if(dcount==0){
				break;
			}
			int ocount= cursor.getInt
				(cursor.getColumnIndex("outCount"));
			maps.put("gradeName", name);
			maps.put("drawCount", dcount+"");
			maps.put("outCount", ocount+"");
			maps.put("Count", scount+"");
			list.add(maps);
		}
		cursor.close();
		return list;
	}
	
	public static List<Map<String,String>> FindAllwinno(String grade){//查看中奖号码信息
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		String sql="select id,Sid,day, state from cr_winNum where gradeName='"+grade+"'";
		Cursor cursor =db.rawQuery(sql, null);
		
		while(cursor.moveToNext()){
			Map<String,String> maps=new HashMap<String, String>();
			int id=cursor.getInt
					(cursor.getColumnIndex("id"));
			int state=cursor.getInt
					(cursor.getColumnIndex("state"));
			String Sid=cursor.getString(cursor.getColumnIndex("Sid"));
			String datetime=cursor.getString(cursor.getColumnIndex("day"));
			if(state==0){
				maps.put("state", "否");
			}else{
				maps.put("state", "是");
			}
			maps.put("id", id+"");
			maps.put("Sid", Sid);
			maps.put("day", datetime);
			list.add(maps);
		}
		cursor.close();
		return list;
		
	}
	
	public static List<Map<String,String>> FindIsWin(String sql){
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		Cursor cursor =db.rawQuery(sql, null);
		Map<String,String> maps=new HashMap<String, String>();
		while(cursor.moveToNext()){
			
			String Sid=cursor.getString(cursor.getColumnIndex("Sid"));
			String gradeName=cursor.getString(cursor.getColumnIndex("gradeName"));
			
			String drawName=cursor.getString(cursor.getColumnIndex("drawName"));
			int gradeNo=cursor.getInt
					(cursor.getColumnIndex("gradeNo"));
			maps.put("Sid", Sid);
			maps.put("drawName", drawName);
			maps.put("gradeName", gradeName);
			maps.put("gradeNo", gradeNo+"");
			list.add(maps);
		}
		cursor.close();
		return list;
	}
	
	public static List<Map<String,String>> PostData(String title,String act,int first,int last){
		String sql="select * from cr_winNum where id between "+first + " and " +last;
		List<Map<String,String>> params = new ArrayList<Map<String,String>>();
		Cursor cursor =db.rawQuery(sql, null);
		while(cursor.moveToNext()){
			Map<String,String> maps=new HashMap<String, String>();
			String Sid=cursor.getString(cursor.getColumnIndex("Sid"));
			String code=Sid.substring(0,2);//国家编码
		  	String playId=String.valueOf(Sid.substring(2,6));//玩法
			String info=String.valueOf(Sid.substring(6,13));//包号信息
			String serialNo=String.valueOf(Sid.substring(13,16));//张序列号
			String sidStr=code+"-"+playId+"-"+info+"-"+serialNo;
			
			String day=cursor.getString(cursor.getColumnIndex("day"));
			String gradeName=cursor.getString(cursor.getColumnIndex("gradeName"));
			String drawName=cursor.getString(cursor.getColumnIndex("drawName"));
			
			maps.put("wdTitle", title);
			maps.put("act", act);
			maps.put("sidStr", sidStr);
			maps.put("gradeName", gradeName);
			maps.put("drawName", drawName);
			maps.put("day",day);
			maps.put("oper","1");
			params.add(maps);
		}
		cursor.close();
		return params;
	}
	
	public static List<Map<String,String>> PostData1(){
		String sql="select Sid from cr_winNum;";
		List<Map<String,String>> params = new ArrayList<Map<String,String>>();
		Cursor cursor =db.rawQuery(sql, null);
		while(cursor.moveToNext()){
			Map<String,String> maps=new HashMap<String, String>();
			String Sid=cursor.getString(cursor.getColumnIndex("Sid"));
			maps.put("sid", Sid);
			params.add(maps);
		}
		cursor.close();
		return params;
		
	}
	
	/*public static void Close(){
		BaseDao.closeDB(db);
	}*/
}
