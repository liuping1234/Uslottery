package com.sd.everflourish.lot.scan;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.sd.everflourish.BaseSdActivity;
import com.uslotter.util.HttpUtil;

public class LotBagNoScanHandleActivity extends BaseSdActivity {
	final String TAG = "--SaveLotNoActivity--";
	ProgressDialog pBar =null;
	String[] b=new String[5];
	String sid="";
	int errorCode=0;//没有错误
	//String title="17888";
	@Override
	public void onCreate(Bundle savedInstanceState){
		sid=getIntent().getStringExtra("sid");
		showProgressDialog(this,"提示信息","提交彩票包号中，请稍后...",ProgressDialog.STYLE_SPINNER);
		super.onCreate(savedInstanceState);
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what==1){
					showTipMsg("扫描并处理成功！");
			   }else{
				   if(errorCode==-1){//
					   showTipMsg("包号已经存在！");
				   }else  if(errorCode==-2) {
					   showTipMsg("保存包号时，发生其他异常！");
				   }else  if(errorCode==-3) {
					   showTipMsg("不是即开彩票的序号！");
				   }else  if(errorCode==-4) {
					   showTipMsg("网络连接失败，请重试！");
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
				pBar.cancel();
				handler.sendMessage(m);
			}
		}.start();
	
	}
	
	 
  private int saveLotBagNo(String sid){
	  	//int errorCode=0;
		String code=sid.substring(0,2);
		if(!code.equals("35")){//不是即开彩票的序号号
			this.errorCode=-3;
			return this.errorCode;
		}
		String playId=String.valueOf(sid.substring(2,6));
		String info=String.valueOf(sid.substring(6,13));
		String bagNo=code+"-"+playId+"-"+info;
		String url=HttpUtil.BASE_URL;
		Map<String ,String> rawParams=new HashMap<String,String>();
		rawParams.put("oper", "14");
		rawParams.put("bagNo", bagNo);
		rawParams.put("title", title);
		rawParams.put("playId", playId);
		rawParams.put("cid", lotApp.getCid());
		try {
			JSONArray jsonArray=new JSONArray(HttpUtil.postRequest(url,rawParams));		
			for(int i=0;i<jsonArray.length();i++){
				JSONObject o=(JSONObject)jsonArray.get(i);
				this.errorCode=(Integer)o.get("errorCode");
			}
		}catch (Exception e) {
			Log.e("saveLotBagNo", e.getMessage());
			this.errorCode=-4;
		}
		return this.errorCode;
	 }
	
	
	//显示等待对话框
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
	
	//即开彩票验证
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
