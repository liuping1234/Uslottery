/*package com.everflourish.lot.scan;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.everflourish.LotMainActivity;
import com.everflourish.R;
import com.everflourish.db.SqliteHandle;
import com.everflourish.lot.scan.ShowPackage.CustomListAdapter;

public class LocatingWinCard extends Activity{
	SqliteHandle sqliteHandle=null;
	CustListAdapter adapter=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sqliteHandle = new SqliteHandle(this);
		setContentView(R.layout.win_grade_1);
		ListView list=(ListView) findViewById(R.id.list_grade);
		adapter=new CustListAdapter(this);
		list.setAdapter(adapter);
	}
	
	class CustListAdapter extends BaseAdapter {
		public TextView tGrade;
		public TextView tNum;
		public TextView tCount;
		public Button tLook;
		private LayoutInflater inflater;  
		private List<Map<String, String>> data;  
		private Context context;
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		public CustListAdapter(Context context) { 
			 this.context = context;  
			 this.inflater = LayoutInflater.from(context);  
			 data = getData();  
		 }
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null){  
				 convertView = inflater.inflate(R.layout.list_grade_1, null); 
			 }
			tGrade=(TextView) findViewById(R.id.mGrade);
			tNum=(TextView) findViewById(R.id.mLottery);
			tCount=(TextView) findViewById(R.id.mAcount);
			tLook=(Button) findViewById(R.id.mBtXiangqing);
			
			String grade=data.get(position).get("gradeName");
			String lotNum=data.get(position).get("drawCount");
			String Acount=data.get(position).get("outCount");
			((TextView)convertView.findViewById(R.id.mGrade)).setText(grade);
			((TextView)convertView.findViewById(R.id.mLottery)).setText(lotNum);
			((TextView)convertView.findViewById(R.id.mAcount)).setText(Acount);
			((Button)convertView.findViewById(R.id.mBtXiangqing)).setOnClickListener( new OnClickListener() {  
				@Override  
				public void onClick(View v) {  
						Toast.makeText(LocatingWinCard.this, "哈哈哈哈哈哈哈", Toast.LENGTH_SHORT);  
					}  
				});  
			return convertView;
		} 
		
		 public List<Map<String, String>> getData(){
			 List<Map<String,String>> list =SqliteHandle.FindGrade();
			 return list;
			 
	  }
		
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
*/




package com.sd.everflourish.lot.scan;
//动态刷新问题未解决
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sd.everflourish.LotMainActivity;
import com.sd.everflourish.db.SqliteHandle;
import com.cr.uslotter.R;

public class LocatingWinCard extends Activity {
	CustomListAdapter adapter=null;
	SqliteHandle sqliteHandle =null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sqliteHandle = new SqliteHandle(this);
		 setContentView(R.layout.win_grade_1);
		ListView list=(ListView) findViewById(R.id.list_grade);
		adapter=new CustomListAdapter(this);
		list.setAdapter(adapter);
	}
	
	 class CustomListAdapter extends BaseAdapter {
		 public TextView Tid;
		 public TextView Tnum;
		 public TextView Tcount;
		 public Button Bsel;
		 private LayoutInflater inflater;  
		 private List<Map<String, String>> data;  
		 private Context context;
		 @Override
		 public int getCount() {
				// TODO Auto-generated method stub
				return data.size();
			}
		 @Override
		 public Object getItem(int position) {
			 // TODO Auto-generated method stub
			 return position;
		 }
		 @Override
		 public long getItemId(int position) {
			 // TODO Auto-generated method stub
			 return position;
		 }
		 public CustomListAdapter(Context context) { 
			 this.context = context;  
			 this.inflater = LayoutInflater.from(context);  
			 data = getData();  
		 }
		  
		 public View getView(final int position, View convertView, ViewGroup parent) {  
			 if(convertView == null){  
				 convertView = inflater.inflate(R.layout.list_grade_1, null); 
			 }
			/*	
				 convertView.setTag(vh);  
			}else{  
				convertView = convertView.getTag(); 
			} */
			Tid=(TextView) findViewById(R.id.mGrade);
			Tnum=(TextView) findViewById(R.id.mLottery);
			Tcount=(TextView) findViewById(R.id.mAcount);
			Bsel=((Button)findViewById(R.id.mBtXiangqing));
			
		  	final String grade=data.get(position).get("gradeName");
		  	String outcount=data.get(position).get("outCount");
		  	String drawcount=data.get(position).get("drawCount");
		  	String sumcount=data.get(position).get("Count");
		  	
		  	//Toast.makeText(ShowPackage.this, id+"  "+bagNum, Toast.LENGTH_SHORT).show();
		  	((TextView)convertView.findViewById(R.id.mGrade)).setText(grade);
		  	((TextView)convertView.findViewById(R.id.mAcount)).setText(drawcount);
		  	((TextView)convertView.findViewById(R.id.mLottery)).setText(sumcount); 
			((Button)convertView.findViewById(R.id.mBtXiangqing))
			.setOnClickListener( new OnClickListener() {  
					@Override  
					public void onClick(View v) {  
							goToIntent(grade);
						}  
					});  
					return convertView;  
			  } 

	public List<Map<String, String>> getData(){  
		 List<Map<String, String>> list=SqliteHandle.FindGrade();
		 return list;
				 
		  }
	 }
	 public void goToDigilinxActivity(String message) {
		 Intent intent = new Intent();
		 intent.setClass(this, LotMainActivity.class);
	     startActivity(intent);
	    // SqliteHandle.Close();
	     finish();
	}
	 public void goToIntent(String grade){
		 Intent intent=new Intent();
		 intent.putExtra("grade", grade);
		 //intent.setAction("android.intent.action.FINDLOTWININFO");
		 intent.setClass(this, WinCardInfo.class);
	     startActivity(intent);
         //finish();
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
