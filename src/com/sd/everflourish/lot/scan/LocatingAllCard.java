package com.sd.everflourish.lot.scan;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sd.everflourish.BaseSdActivity;
import com.sd.everflourish.LotMainActivity;
import com.sd.everflourish.db.SqliteHandle;
import com.cr.uslotter.R;

public class LocatingAllCard extends BaseSdActivity{
	 List<Map<String,String>> items;
	 SqliteHandle sqliteHandle=null;
	public void onCreate(android.os.Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 sqliteHandle = new SqliteHandle(this);
		 setContentView(R.layout.lotterynum);
		 items =SqliteHandle.findAllNum();
		 SimpleAdapter adapter=new SimpleAdapter(this, 
				 items,
				 R.layout.list_item,
				 new String[]{"id","Sid","time"}, 
				 new int[]{R.id.mId,R.id.mNumber,R.id.mTime});
		 ListView list=(ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		/*Button bt= (Button) findViewById(R.id.lBut);
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent intent =new Intent();
                 intent.setClass(LocatingAllCard.this, LotMainActivity.class);
                 startActivity(intent);
                 finish();
			}
		});*/
	}
	public void goToDigilinxActivity(String message) {
		 Intent intent = new Intent();
		 intent.setClass(this, LotMainActivity.class);
	     startActivity(intent);
	    // SqliteHandle.Close();
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
