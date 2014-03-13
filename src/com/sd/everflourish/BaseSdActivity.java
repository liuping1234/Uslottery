package com.sd.everflourish;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import com.sd.everflourish.lot.check.LotteryCheckMainActivity;
import com.cr.uslotter.R;


public class BaseSdActivity extends Activity {
	public LotApplication lotApp=null;
	public String title="";
	@Override
	public void onCreate(Bundle savedInstanceState){	
		  super.onCreate(savedInstanceState);
		  LotteryCheckMainActivity.activityList.add(this);
		//  lotApp=(LotApplication)getApplication();
		  lotApp = new LotApplication();
		  Toast.makeText(this, "...", Toast.LENGTH_SHORT).show();
		  title=lotApp.getTitle();
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
		    	// android.os.Process.killProcess(android.os.Process.myPid()) ;//��ȡPID 
		    	// System.exit(0); 
		    	 exitClient(BaseSdActivity.this);

		     }
		    }).show();
	}
	
	/**
     * �˳��ͻ��ˡ�
     * 
     * @param context ������
     */
    public  void exitClient(Context context){
       // Log.d(TAG, "----- exitClient -----");
        // �ر�����Activity
        for (int i = 0; i < LotteryCheckMainActivity.activityList.size(); i++){
            if (null != LotteryCheckMainActivity.activityList.get(i)){
            	LotteryCheckMainActivity.activityList.get(i).finish();
            }
        }
        
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.restartPackage(getPackageName());
        System.exit(0);//Android�ĳ���ֻ����Activity finish()��,��������finish��,�˳�������ȫ
    }

//    @Override	 
//	 public boolean onKeyDown(int keyCode, KeyEvent event) {
//	  //���¼����Ϸ��ذ�ť //
//	  if(keyCode == KeyEvent.KEYCODE_BACK){
//		  goLogout();
//		  return true;
//	 
//	  }else{  
//	   return super.onKeyDown(keyCode, event);	 
//	  }	 
//	 }
    
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu){
//		SubMenu szmenu=menu.addSubMenu("ѡ��");
//		menu.add(0, 1, 0, "����");
//		menu.add(0, 2, 0, "�˳�");
//		//szmenu.add(0,3,0,"�鿴����");
//		//szmenu.add(0,4,0,"�鿴�н�����");
//		//szmenu.add(0,5,0,"���ý�Ʒ");
//		return super.onCreateOptionsMenu(menu);
//	}
	
//	@Override
//	// �˵��������Ļص�����
//	public boolean onOptionsItemSelected(MenuItem mi){
//		//�жϵ��������ĸ��˵��������Ե�������Ӧ��
//		switch (mi.getItemId()){
//			case 1:
//				Toast toast = Toast.makeText(this
//						, "�㶫���ٿƼ����޹�˾����" , Toast.LENGTH_SHORT);
//					toast.show();
//				break;
//			case 2:
//				goLogout();
//				break;
//			case 3:
//				//SelectBag();
//				break;
//			case 4:
//				break;
//			case 5:
//				//InsertPrize();
//				break;
//		}
//		return true;
//	}
}