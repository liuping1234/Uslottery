package com.hz;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.UslotteryActivity;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;
import com.uslotter.util.Util;
public class ConfigActivity extends Activity {
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	ProgressDialog pBar =null;
	 int errorCode=0;
	@Override
	public void onCreate(Bundle savedInstanceState){	
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.configlist_hz);	 
		// 获取只能被本应用程序读、写的SharedPreferences对象
		preferences = getSharedPreferences("hzLotteryConfig", MODE_WORLD_READABLE);
		editor = preferences.edit();
		String mobile=preferences.getString("mobile", "");
		
		final EditText mobileText=(EditText)findViewById(R.id.mobile);
		mobileText.setText(mobile);
		
		Button btn = (Button)findViewById(R.id.btn);
		btn.setOnClickListener(new  android.view.View.OnClickListener(){
			@Override
			public void onClick(View v){
				String m= mobileText.getText().toString();
				//editor.putString("mobile",m);
				//editor.commit();
				//Toast.makeText(ConfigActivity.this, "手机号码设置成功！", Toast.LENGTH_LONG).show();
				checkMobile(m);
			}
		});	
	}
	
	 @Override	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  //按下键盘上返回按钮 //
	  if(keyCode == KeyEvent.KEYCODE_BACK){ 
		  Intent intent = new Intent();
			 intent.setAction("android.intent.action.HZLOTTERY");
		     startActivity(intent);
		     this.finish();
		     return true;
	  }else{  
	   return super.onKeyDown(keyCode, event);	 
	  }	 
	 }
	 
	 public void checkMobile(final String mobile){
		 if(mobile.equals("")){
			 Toast.makeText(ConfigActivity.this, "请输入手机号码!", Toast.LENGTH_LONG).show();
			 return ;
		 }
		showProgressDialog(this,"提示信息","获取数据中，请稍后...",ProgressDialog.STYLE_SPINNER);
   		final Handler handler = new Handler(){
 			@Override
 			public void handleMessage(Message msg){
 				if(msg.what==1){
 					//String m= mobileText.getText().toString();
 					editor.putString("mobile",mobile);
 					editor.commit();
 					//Toast.makeText(ConfigActivity.this, "手机号码设置成功！", Toast.LENGTH_LONG).show();
 					showDialog("手机号码设置成功！");
 			   }else{
 				  // editor.putString("mobile","");
				   //editor.commit();
 				   if(errorCode==-1){
 					  Toast.makeText(ConfigActivity.this, "该手机号码没有权限！\n请联系管理员授权", Toast.LENGTH_LONG).show();
 				   }else{
 					  Toast.makeText(ConfigActivity.this, "该手机号设置失败！", Toast.LENGTH_LONG).show();
 			   	}
 			   }
 			};
 			
 		};
 		new Thread(){
 			public void run(){
 				boolean flag=submitCheck(mobile);
 				Message m=new Message();
 				if(flag){
 					m.what=1;
 				}else{
 					m.what=0;
 				}
 				pBar.cancel();
 				handler.sendMessage(m);
 			}
 		}.start();
	 }
	 
	 public boolean submitCheck(String mobile){
		// int errorCode=0;
		 try{
				String url=HttpUrl.URL+HttpUrl.query;
				Map<String ,String> rawParams=new HashMap<String,String>();
				rawParams.put("oper", "3");
				rawParams.put("cid", Util.getSharedPrefercencesString(ConfigActivity.this,"cid"));
				rawParams.put("mobile", mobile);
				JSONArray jsonArray=new JSONArray(HttpUtil.postRequest(url,rawParams));
				for(int i=0;i<jsonArray.length();i++){
					JSONObject o=(JSONObject)jsonArray.get(i);
					errorCode=(Integer)o.get("errorCode");
				}
			}catch(Exception e){
				errorCode=-3;
			}
		 if(errorCode==0){
			 return true;
		 }else{
			 return false;
		 }
	 }
	//显示等待对话框
	private void showProgressDialog(Context ctx,String title, String msg,int style){
		pBar = new ProgressDialog(ctx);
		pBar.setTitle(title);
		pBar.setMessage(msg);
		pBar.setProgressStyle(style);
		pBar.show();
	}
	
	public void showDialog(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示信息");
	   	builder.setMessage(message).setCancelable(false);
	   	builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){		    	
				  public void onClick(DialogInterface dialog, int which){
					  Intent intent = new Intent();
						 intent.setAction("android.intent.action.HZLOTTERY");
					     startActivity(intent);
				  }
	   	});		
	   	builder.create().show(); 
	}
}