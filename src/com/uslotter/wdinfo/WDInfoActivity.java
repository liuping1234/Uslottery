package com.uslotter.wdinfo;

import com.cr.uslotter.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
/*
 * 查询条件，暂时屏蔽
 */
public class WDInfoActivity extends Activity implements OnClickListener{
	Button back=null;
	Button select=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wdinfo);
		back=(Button) findViewById(R.id.wdinfoback);
		select=(Button) findViewById(R.id.wdid_select_button);
		back.setOnClickListener(this);
		select.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wdinfoback:
			finish();
			break;
		case R.id.wdid_select_button:
			Intent intent=new Intent();
			intent.setAction("android.intent.action.SelectByIDActivity");
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	@Override	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  //按下键盘上返回按钮 //
	  if(keyCode == KeyEvent.KEYCODE_BACK){ 
		  finish();
		  return true;
	 
	  }else{  
	   return super.onKeyDown(keyCode, event);	 
	  }	 
	 }
}