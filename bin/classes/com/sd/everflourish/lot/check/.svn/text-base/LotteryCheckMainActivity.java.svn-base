package com.sd.everflourish.lot.check;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import com.sd.everflourish.BaseSdActivity;
import com.sd.everflourish.LotMainActivity;
import com.uslotter.util.Config;
import com.uslotter.util.HttpUtil;
/**
 * 体彩助手的主Activity
 * @author Administrator
 *
 */
public class LotteryCheckMainActivity extends BaseSdActivity {
	final static String LOTTERY_CATEGORY="com.everflourish.lot.intent.category.LOTTERY_CATEGORY";
	public static List<Activity> activityList = new ArrayList<Activity>();
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		  //activityList.add(this);
	      //设置为全屏
	      requestWindowFeature(Window.FEATURE_NO_TITLE); 
		  //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,   WindowManager.LayoutParams.FLAG_FULLSCREEN);
		  //设置横屏模式
		  //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);	
	     goToDigilinxActivity();

	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	//即开彩票验证
	public void goToDigilinxActivity() {
		 Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		 intent.addCategory(LotteryCheckMainActivity.LOTTERY_CATEGORY);
	     startActivityForResult(intent, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//即开票验证
		Log.d("requestCode", requestCode+"  "+resultCode+"   ");
		if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {	    	  
		    	 if( HttpUtil.checkNet(this)){
		    		 String contents = intent.getStringExtra("SCAN_RESULT");
		    		 Log.d("二维码信息", contents);
		    		 intent.putExtra("sid", contents);
			    	 intent.setAction("android.intent.action.VIEWCHECKLOTRES");
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

	
	  @Override	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  //按下键盘上返回按钮 //
	  if(keyCode == KeyEvent.KEYCODE_BACK){ 
		  Intent intent = new Intent();
		  intent.setClass(this, LotMainActivity.class);
		  startActivity(intent);
		  finish();
		  return true;
	  }else{  
	   return super.onKeyDown(keyCode, event);	 
	  }	 
	 }
	 /**
	 * 启动线程检查是否有新版本
	 * 
	 */
	private void checkUpdate(){
		if(HttpUtil.checkNet(this)){
			if(Config.hasNewVersion(this)){
				  Intent intent = new Intent();
				  intent.putExtra("newVerName", Config.newVerName);
				  intent.putExtra("updateDesc", Config.updateDesc);
				  intent.setAction("android.intent.action.CHECKUPDATEAPK");
				  //intent.addCategory(LotteryCheckMainActivity.UDATE_CATEGORY);
			      startActivity(intent);
			}
		}     
	}

}
