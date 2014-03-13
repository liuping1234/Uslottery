package com.sd.everflourish.lot.scan;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.sd.everflourish.BaseSdActivity;
import com.uslotter.util.HttpUtil;

public class PrizeActivity extends BaseSdActivity {
	final static String PRIZE__CATEGORY="com.everflourish.lot.scan.intent.category.PRIZE_CATEGORY";
	public static List<Activity> activityList = new ArrayList<Activity>();
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		String message=getIntent().getStringExtra("message");
		if(message!=null){
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		}
	      //设置为全屏
	      requestWindowFeature(Window.FEATURE_NO_TITLE); 
	      goToDigilinxActivity();  
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	//打开二维码扫描
	public void goToDigilinxActivity() {
		 Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		 intent.addCategory(PrizeActivity.PRIZE__CATEGORY);
	     startActivityForResult(intent, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//即开票验证
		if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {	    	  
		    	 if( HttpUtil.checkNet(this)){
		    		 String contents = intent.getStringExtra("SCAN_RESULT");
		    		 intent.putExtra("sid", contents);
			    	 intent.setAction("android.intent.action.PRIZEHANDLE");
					 startActivity(intent);
		    	 }else{
		    		 showDialog("网络不能使用，请确认网络是否打开？"); 
		    	 }
		      } else if (resultCode == RESULT_CANCELED){
		    	  showDialog("二维码解析失败，请重试！！"); 
		      }
		  }
	}
	
	public void showDialog(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);	    
	   	builder.setMessage(message).setCancelable(false);
	   	builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){
		    	  @Override
				  public void onClick(DialogInterface dialog, int which){
				 		goToDigilinxActivity();
				  }
	   	});		
	   	builder.create().show(); 
	}
}
