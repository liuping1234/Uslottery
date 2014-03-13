package com.sd.everflourish.lot.scan;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.sd.everflourish.BaseSdActivity;
import com.uslotter.util.HttpUtil;

public class DrawScanHandleActivity extends BaseSdActivity {
	final String TAG = "--DrawScanHandleActivity--";
	ProgressDialog pBar =null;
	String sid="";
	int errorCode=0;//没有错误
	//String title="17888";
	String draw[]=new String[4];
	@Override
	public void onCreate(Bundle savedInstanceState){
		sid=getIntent().getStringExtra("sid");
		showProgressDialog(this,"提示信息","提交数据并抽奖中，请稍后...",ProgressDialog.STYLE_SPINNER);
		super.onCreate(savedInstanceState);
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what==1){
					if(draw[0].equals("1")){
						showDialog("恭喜您，中奖啦！\n"+draw[2]+"\n"+draw[3]);
					}else{
						showTipMsg("很抱歉，没有中奖！");
					}
					
			   }else{
				   if(errorCode==-1){//
					   showTipMsg("该张彩票不能参与抽奖");
				   }else  if(errorCode==-2) {
					   showTipMsg("本张彩票，已经参加过抽奖！");
				   }else  if(errorCode==-3) {
					   showTipMsg("抽奖发生异常，请重试！");
					  
				   }else  if(errorCode==-5) {
					   showTipMsg("请扫描即开彩票的有效二维码区域！！");
					  
				   }else  if(errorCode==-6) {
					   showTipMsg("很抱歉，本次抽奖活动已经结束！");
					  
				   }else  if(errorCode==-4) {
					   showTipMsg("网络连接失败，请重试！");
				   }
				 
			   }
			};	
		};
		
		new Thread(){
			public void run(){
				int flag=handle(sid);
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
	
	 
  private int handle(String sid){
	  	//int errorCode=0;
		String code=sid.substring(0,2);
		if(!code.equals("35")){//不是即开彩票的序号号
			this.errorCode=-5;
			return this.errorCode;
		}
		String playId=String.valueOf(sid.substring(2,6));
		String info=String.valueOf(sid.substring(6,13));
		String serialNo=String.valueOf(sid.substring(13,16));
		String bagNo=code+"-"+playId+"-"+info;
		
		String url=HttpUtil.BASE_URL;
		Map<String ,String> rawParams=new HashMap<String,String>();
		rawParams.put("oper", "15");
		rawParams.put("sid", sid);
		rawParams.put("title", title);
		rawParams.put("cid", lotApp.getCid());
		try {
			JSONArray jsonArray=new JSONArray(HttpUtil.postRequest(url,rawParams));
			Log.d("jsonarray", jsonArray+"");
			for(int i=0;i<jsonArray.length();i++){
				JSONObject obj=(JSONObject)jsonArray.get(i);
				JSONArray  json=(JSONArray)obj.get("draw");
				int size=json.length();
				for(int j=0;j<size;j++){
  					JSONObject o=(JSONObject)json.get(i);
  					 draw[0]=(String)o.get("status");
  					 draw[1]=(String)o.get("gradeNo");
  					 draw[2]=(String)o.get("gradeName");
  					 draw[3]=(String)o.get("drawName");
  					
  				}
				
				this.errorCode=(Integer)obj.get("errorCode");
			}
		}catch (Exception e) {
			Log.e("handle", e.getMessage());
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
		 intent.setClass(this, DrawScanActivity.class);
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
	  
	  public void showDialog(String message){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);	    
		   	builder.setMessage(message).setCancelable(false);
		   	builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){
			    	  @Override
					  public void onClick(DialogInterface dialog, int which){
					 		goToDigilinxActivity(null);
					  }
		   	});		
		   	builder.create().show(); 
		}
	  
	  
}
