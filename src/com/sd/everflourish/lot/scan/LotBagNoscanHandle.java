package com.sd.everflourish.lot.scan;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.sd.everflourish.BaseSdActivity;
import com.sd.everflourish.db.SqliteHandle;
public class LotBagNoscanHandle extends BaseSdActivity {
	//储存包号
	String sid="";
	int errorCode=0;//没有错误 
	ProgressDialog pBar =null;
	SqliteHandle sqliteHandle=null;
	@Override
	public void onCreate(Bundle savedInstanceState){
		sid=getIntent().getStringExtra("sid");
		sqliteHandle = new SqliteHandle(this);
		showProgressDialog(this,"提示信息","提交彩票包号中，请稍后...",ProgressDialog.STYLE_SPINNER);
		super.onCreate(savedInstanceState);
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				Log.d("errorCOde", errorCode+"");
				if(msg.what==1){
					showTipMsg("扫描并处理成功！");
			   }else{
				   if(errorCode==-1){//
					   showTipMsg("错误：包号已经存在！");
				   }else  if(errorCode==-2) {
					   showTipMsg("错误：不是有效包号！");
				   }else if(errorCode==-3){
					   showTipMsg("错误：包号识别异常，请重新扫描！");
				   }
			   }
			};	
		};
		
		new Thread(){
			public void run(){
				int flag=saveLotBagNo(sid);
				Message m=new Message();
				if(flag==0){
					m.what=1;
				}else{
					m.what=0;
				}
				//SqliteHandle.Close();
				pBar.cancel();
				handler.sendMessage(m);
			}
		}.start();
	}
	
	private int saveLotBagNo(String sid){//保存包号
		String code=sid.substring(0,2);
		if(!code.equals("35")){//不是即开彩票的序号号
			this.errorCode=-2;
			return this.errorCode;
		}
		String playId=String.valueOf(sid.substring(2,6));
		String info=String.valueOf(sid.substring(6,13));
		String bagNo=code+"-"+playId+"-"+info;
		String sql="select * from BagNum where BagNumber = ?;";
		
		int i=SqliteHandle.findBynum(bagNo,sql);
		ContentValues values = new ContentValues();
		values.put("BagNumber", bagNo);
		String pack="BagNum";
		//String sql1="insert into BagNum values('"+bagNo+"');";
		if(i==0){
			errorCode=SqliteHandle.Insert(pack,values);
		}else{
			errorCode=-1;
		}
		return errorCode;
	}
	
	

	private void showProgressDialog(Context ctx,String title, String msg,int style){
		pBar = new ProgressDialog(ctx);
		pBar.setTitle(title);
		pBar.setMessage(msg);
		pBar.setProgressStyle(style);
		pBar.show();
	}
	
	public void showTipMsg(String message){
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);	    
//	   	builder.setMessage(message).setCancelable(false);
//	   	builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){
//		    	  @Override
//				  public void onClick(DialogInterface dialog, int which){
//				 		goToDigilinxActivity();
//				  }
//	   	});		
//	   	builder.create().show(); 
		goToDigilinxActivity(message);
	}
	
	public void goToDigilinxActivity(String message) {
		 Intent intent = new Intent();
		 intent.putExtra("message", message);
		 intent.setClass(this, LotBagNoScanActivity.class);
	     startActivity(intent);
	     finish();
	}
	
	@Override	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  //按下键盘上返回按钮 //
	  if(keyCode == KeyEvent.KEYCODE_BACK){ 
		  goToDigilinxActivity(null);
		  return true;
	 
	  }else{  
	   return super.onKeyDown(keyCode, event);	 
	  }	 
	 }
}
