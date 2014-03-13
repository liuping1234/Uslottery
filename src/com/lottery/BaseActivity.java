package com.lottery;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cr.uslotter.R;


public class BaseActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){	
		  super.onCreate(savedInstanceState);
		//  LotteryMainActivity.activityList.add(this);
	}
	
	public void goLogout(){
		  new AlertDialog.Builder(this)
		   // .setIcon(R.drawable.logo)
		    .setTitle(R.string.dialog_top_title)
		    .setMessage(R.string.quit_description)
		    .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
			     @Override
			     public void onClick(DialogInterface dialog, int which) {
			     }
		  })
		  .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog, int whichButton) {
		    	// ActivityManager am = (ActivityManager)getSystemService (Context.ACTIVITY_SERVICE);	    	 
		    	// am.restartPackage(getPackageName());
		    	// android.os.Process.killProcess(android.os.Process.myPid()) ;//获取PID 
		    	// System.exit(0); 
		    	 exitClient(BaseActivity.this);

		     }
		    }).show();
	}
	
	/**
     * 退出客户端。
     * 
     * @param context 上下文
     */
    public  void exitClient(Context context){
       // Log.d(TAG, "----- exitClient -----");
        // 关闭所有Activity
//        for (int i = 0; i < LotteryMainActivity.activityList.size(); i++){
//            if (null != LotteryMainActivity.activityList.get(i)){
//            	LotteryMainActivity.activityList.get(i).finish();
//            }
//        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.restartPackage(getPackageName());
        System.exit(0);//Android的程序只是让Activity finish()掉,而单纯的finish掉,退出并不完全
    }

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0, 0, 0, "设置");
		menu.add(0, 1, 0, "关于");
		menu.add(0, 2, 0, "退出");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	// 菜单项被单击后的回调方法
	public boolean onOptionsItemSelected(MenuItem mi){
		//判断单击的是哪个菜单项，并针对性的作出响应。
		switch (mi.getItemId()){
			case 0:
				goConfigActivity();
				break;
			case 1:
				Toast toast = Toast.makeText(this
						, "广东长荣科技有限公司开发" , Toast.LENGTH_SHORT);
					toast.show();
				break;
			case 2:
				goLogout();
				break;
		}
		return true;
	}
	*/
	//系统配置
	public void goConfigActivity() {
		 Intent intent = new Intent();
		 intent.setAction("android.intent.action.CONFIG");
	     startActivity(intent);
	}
}