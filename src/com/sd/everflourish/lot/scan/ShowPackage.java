package com.sd.everflourish.lot.scan;
//动态刷新问题未解决
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class ShowPackage extends Activity {
	SqliteHandle sqliteHandle=null;
	CustomListAdapter adapter=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sqliteHandle = new SqliteHandle(this);
		 setContentView(R.layout.showpackage);
		Button In=(Button) findViewById(R.id.BInsert);
		ListView list=(ListView) findViewById(R.id.myList);
		adapter=new CustomListAdapter(this);
		list.setAdapter(adapter);
		In.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("android.intent.action.LOTSCAN");
				startActivity(intent);
				finish();
			}
		});
	}
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // super.handleMessage(msg);
            switch (msg.what) {
            case 1:
            	adapter.notifyDataSetChanged();
           	 break;
            }
        }
	};
	
	 class CustomListAdapter extends BaseAdapter { 
		 public TextView Tid;
		 public TextView Tnum;
		 public Button Bdel;
		 private LayoutInflater inflater;  
		// private ViewHolder vh = null;  
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
				 convertView = inflater.inflate(R.layout.list_item_package, null); 
			 }
			/*	
				 convertView.setTag(vh);  
			}else{  
				convertView = convertView.getTag(); 
			} */
			Tid=(TextView) findViewById(R.id.BId);
			Tnum=(TextView) findViewById(R.id.BNum);
			Bdel=((Button)findViewById(R.id.BSel));
		  	String id=data.get(position).get("id");
		  	String bagNum=data.get(position).get("BagNumber");
		  	//Toast.makeText(ShowPackage.this, id+"  "+bagNum, Toast.LENGTH_SHORT).show();
		  	((TextView)convertView.findViewById(R.id.BId)).setText(id);  
		  	((TextView)convertView.findViewById(R.id.BNum)).setText(bagNum);  
			((Button)convertView.findViewById(R.id.BSel)).setOnClickListener( new OnClickListener() {  
			@Override  
			public void onClick(View v) {  
					delDialog(ShowPackage.this,(String)data.get(position).get("BagNumber"));  
				}  
			});  
			return convertView;  
		  } 
		 

		  public void delDialog(Context context,final String BagNum){  
			  new AlertDialog.Builder(context)  
			  .setTitle("删除")  
			  .setMessage("你确定要删除"+BagNum+"？")  
			  .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
				  @Override  
				  public void onClick(DialogInterface dialog, int which) {  
					 int isDelete= SqliteHandle.delete(BagNum);
					 if(isDelete==1){
						 new AlertDialog.Builder(ShowPackage.this)  
						  .setTitle("提示！")  
						  .setMessage("删除成功").
						  setPositiveButton("确定",    
			                        new DialogInterface.OnClickListener() {   
			   
			                            public void onClick(DialogInterface dialog,   
			                                    int which) { 
			                            	handler.obtainMessage(1);
			                            	goToDigilinxActivity(null);
			                            }
						  }).
						  create().show();  
					 }else{
						 new AlertDialog.Builder(ShowPackage.this)  
						  .setTitle("提示！")  
						  .setMessage("错误:删除失败！").
						  setPositiveButton("确定", null).
						  create().show();
					 }
				  }  
			  })  
			  .show();  
		  }  

		  public List<Map<String, String>> getData(){  
				 List<Map<String, String>> list=SqliteHandle.findAllPerson();
				 return list;
				 
		  }
	 }
	 public void goToDigilinxActivity(String message) {
		 Intent intent = new Intent();
		 intent.setClass(this, LotMainActivity.class);
	     startActivity(intent);
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
