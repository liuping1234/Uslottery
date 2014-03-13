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
	int errorCode=0;//û�д���
	//String title="17888";
	@Override
	public void onCreate(Bundle savedInstanceState){
		sid=getIntent().getStringExtra("sid");
		showProgressDialog(this,"��ʾ��Ϣ","�ύ��Ʊ�����У����Ժ�...",ProgressDialog.STYLE_SPINNER);
		super.onCreate(savedInstanceState);
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what==1){
					showTipMsg("ɨ�貢����ɹ���");
			   }else{
				   if(errorCode==-1){//
					   showTipMsg("�����Ѿ����ڣ�");
				   }else  if(errorCode==-2) {
					   showTipMsg("�������ʱ�����������쳣��");
				   }else  if(errorCode==-3) {
					   showTipMsg("���Ǽ�����Ʊ����ţ�");
				   }else  if(errorCode==-4) {
					   showTipMsg("��������ʧ�ܣ������ԣ�");
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
		if(!code.equals("35")){//���Ǽ�����Ʊ����ź�
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
	
	
	//��ʾ�ȴ��Ի���
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
//	   	builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){
//		    	  @Override
//				  public void onClick(DialogInterface dialog, int which){
//				 		goToDigilinxActivity();
//				  }
//	   	});		
//	   	builder.create().show(); 
		goToDigilinxActivity(message);
	}
	
	//������Ʊ��֤
	public void goToDigilinxActivity(String message) {
		 Intent intent = new Intent();
		 intent.putExtra("message", message);
		 intent.setClass(this, LotBagNoScanActivity.class);
	     startActivity(intent);
	     finish();
	}
	
	  @Override	 
		 public boolean onKeyDown(int keyCode, KeyEvent event) {
		  //���¼����Ϸ��ذ�ť //
		  if(keyCode == KeyEvent.KEYCODE_BACK){ 
			  goToDigilinxActivity(null);
			  return true;
		  }else{  
		   return super.onKeyDown(keyCode, event);	 
		  }	 
		 }
}
