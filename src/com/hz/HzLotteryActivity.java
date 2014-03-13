package com.hz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.UslotteryActivity;
import com.uslotter.UslotteryMainMenu2;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;
import com.uslotter.util.Util;

public class HzLotteryActivity extends Activity {
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	ProgressDialog pBar =null;
	int errorCode=0;
	List<String[]> dataList=new ArrayList<String[]>();
	
	private Button btn_exit = null;
	//String title = "";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_hz);
        btn_exit = (Button) this.findViewById(R.id.main_hz_qdback);
        btn_exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HzLotteryActivity.this.finish();
			}
		});
 
          final TextView data=(TextView)findViewById(R.id.mainhz_data);
       
        showProgressDialog(this,"提示信息","获取数据中，请稍后...",ProgressDialog.STYLE_SPINNER);
        final Handler hdler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what==1){
					showList();
			   }else{
				   data.setText("获取信息失败！");
			   }
			};
			
		};
		
		//启动线程来连接网络并读取开奖号码
		new Thread(){
			public void run(){
				boolean flag=findXcPro();
				Message m=new Message();
				if(flag){
					m.what=1;
				}else{
					m.what=0;
				}
				pBar.cancel();
				hdler.sendMessage(m);
			}
		}.start();
    }
    
    private boolean findXcPro(){
    	boolean flag=false;
    	try{
    		//checkNet(Context context)
			String url=HttpUrl.URL+HttpUrl.query;
			Map<String ,String> rawParams=new HashMap<String,String>();
			rawParams.put("oper", "1");
			rawParams.put("cid", Util.getSharedPrefercencesString(this, "cid"));
			//rawParams.put("cid", "17");
		//	Toast.makeText(this,UslotteryActivity.CenterID+"..", Toast.LENGTH_LONG).show();
		//	Log.i("tag", UslotteryActivity.CenterID+";;;");
			JSONArray jsonArray=new JSONArray(HttpUtil.postRequest(url,rawParams));
			for(int i=0;i<jsonArray.length();i++){
				JSONObject o=(JSONObject)jsonArray.get(i);
				errorCode=(Integer)o.get("errorCode");
				JSONArray jsonArrayXc=(JSONArray)o.get("xcinfo");
				for(int j=0;j<jsonArrayXc.length();j++){
					JSONObject obj=jsonArrayXc.getJSONObject(j);
					String b[]=new String[4];
					int tmp=(Integer)obj.get("id");
					b[0]=tmp+"";
					b[1]=(String)obj.get("Name");
					b[2]=((String)obj.get("Btime")).substring(0,10);
					b[3]=((String)obj.get("Etime")).substring(0,10);
					dataList.add(b);
				}
			}
			flag= true;
		}catch(Exception e){
			Log.e("findXcPro: ", e.getMessage()+"--");
		}
    	return flag;
    }

  	private void showList(){
  		// 创建一个BaseAdapter对象，该对象负责提供Gallery所显示的图片
  		BaseAdapter adapter = new BaseAdapter(){
  			@Override
  			public int getCount(){
  				return dataList.size();
  			}
  			@Override
  			public Object getItem(int position){
  				return position;
  			}
  			@Override
  			public long getItemId(int position){
  				return position;
  			}

  			// 该方法的返回的View就是代表了每个列表项
  			@Override
  			public View getView(int position, View convertView, ViewGroup parent){
  				final String b[]=dataList.get(position);
  				
  				//TableLayout table=new TableLayout(HzLotteryActivity.this);
				//table.setStretchAllColumns(true);
				LinearLayout ll=new LinearLayout(HzLotteryActivity.this);
				ll.setOrientation(LinearLayout.VERTICAL);
				//table.setBackgroundColor(R.color.white);
			//	TableRow row=new TableRow(HzLotteryActivity.this);
				TextView textView=new TextView(HzLotteryActivity.this);
				textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.android_list_idex, 0);
				LayoutParams lp =new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				lp.setMargins(10, 5, 10, 5);
				textView.setLayoutParams(lp);
  				//textView.setPadding(10, 10, 0, 0);
  				textView.setTextSize(20);
  				textView.setTextColor(Color.BLACK);
  				textView.setText(b[1]);
				//row.addView(textView);
				//table.addView(row);
  				ll.addView(textView);
				
				//row=new TableRow(HzLotteryActivity.this);
				textView=new TextView(HzLotteryActivity.this);
 				textView.setPadding(10, 0, 0, 0);
 				textView.setTextSize(17);
 				textView.setText("时间： "+b[2]+" 至 "+b[3]);
 				textView.setTextColor(Color.BLACK);
				//row.addView(textView);
				//table.addView(row);
				ll.addView(textView);
//				row=new TableRow(HzLotteryActivity.this);
//				 textVew=new TextView(HzLotteryActivity.this);
//  				textVew.setPadding(10, 0, 0, 0);
//  				textVew.setTextSize(17);
//  				textVew.setText("开始日期:"+b[2]);
//  				textVew.setTextColor(Color.BLACK);
//				row.addView(textVew);
//				table.addView(row);
//				
//				row=new TableRow(HzLotteryActivity.this);
//				 textVew=new TextView(HzLotteryActivity.this);
//  				textVew.setPadding(10, 0, 0, 0);
//  				textVew.setTextSize(17);
//  				textVew.setTextColor(Color.BLACK);
//  				textVew.setText("结束日期:"+b[3]);
//				row.addView(textVew);
//				table.addView(row);
				
				ll.setOnClickListener(new View.OnClickListener(){
					public void onClick(View v) {
						//if(checkUser()){
							goReadCardActivity(b[0],b[1],b[2]+" 至 "+b[3]);
						//}else{
						//	showDialog("请设置手机号码！");
						//}		
					}
				});
  				
  				
				return ll;
  			}
  		};	
  		
  		ListView list = (ListView)findViewById(R.id.mainhz_xclist);
  		//为ListView设置Adapter
  		//list.setBackgroundColor(R.color.white);
  		if(dataList.size()==0){
  			TextView data=(TextView)findViewById(R.id.mainhz_data);
  
  			data.setText("当前无活动");
  		}
  		list.setAdapter(adapter);
  	}
	//显示等待对话框
	private void showProgressDialog(Context ctx,String title, String msg,int style){
		pBar = new ProgressDialog(ctx);
		pBar.setTitle(title);
		pBar.setMessage(msg);
		pBar.setProgressStyle(style);
		pBar.show();
	}
	
	//查询网点
	public void goReadCardActivity(String xcid,String title,String time) {
		 Intent intent = new Intent();
		 intent.putExtra("xcid", xcid);
		 intent.putExtra("title",title );
		 intent.putExtra("time" ,time );
		 intent.putExtra("msg" ,"" );
		 intent.setAction("android.intent.action.READCARD");
	     startActivity(intent);
	     HzLotteryActivity.this.finish();
	}
	
//	public boolean checkUser(){	
//		String mobile=lotApp.getMobile();
//		if(mobile.equals("")){
//			return false;
//		}
//		return true;
//	}
	
	public void showDialog(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示信息");
	   	builder.setMessage(message).setCancelable(false);
	   	builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){		    	
				  public void onClick(DialogInterface dialog, int which){
				 		goToConfigActivity();
				 		
				  }
	   	});		
	   	builder.create().show(); 
	}
	
	
	public void goToConfigActivity() {
		 Intent intent = new Intent();
		 intent.setAction("android.intent.action.HZCONFIG");
	     startActivity(intent);
	     HzLotteryActivity.this.finish();
	}
	
  @Override	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  //按下键盘上返回按钮 //
	  if(keyCode == KeyEvent.KEYCODE_BACK){ 
		  Intent intent = new Intent();
          intent.setClass(HzLotteryActivity.this, UslotteryMainMenu2.class);
          startActivity(intent);
          HzLotteryActivity.this.finish();
          HzLotteryActivity.this.overridePendingTransition(
					R.anim.push_right_in, R.anim.push_right_out);
		  return true;
	  }else{  
	   return super.onKeyDown(keyCode, event);	 
	  }	 
	 }
}