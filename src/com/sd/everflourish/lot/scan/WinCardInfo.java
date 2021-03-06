package com.sd.everflourish.lot.scan;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sd.everflourish.db.SqliteHandle;
import com.cr.uslotter.R;

public class WinCardInfo extends Activity{
	List<Map<String,String>> items;
	 SqliteHandle sqliteHandle=null;
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allwinnum);
		String grade=getIntent().getStringExtra("grade");
		sqliteHandle = new SqliteHandle(this);
		items =SqliteHandle.FindAllwinno(grade);
		SimpleAdapter adapter=new SimpleAdapter(this, 
				 items,
				 R.layout.allwinnum_item,
				 new String[]{"id","Sid","state","day"}, 
				 new int[]{R.id.mShunxu,R.id.mLotteryNum1,R.id.mLotteryGrade1,R.id.mLotteryTime1});
		ListView list=(ListView) findViewById(R.id.Wingrade);
		list.setAdapter(adapter);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
