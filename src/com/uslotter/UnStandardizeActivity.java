package com.uslotter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.util.DialogUtil;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;
import com.uslotter.util.Util;

public class UnStandardizeActivity extends Activity {
	private List<String> wdbhs = null;
	UnStandAdapter adapter = null;
	private ListView lv = null;
	private Button exit,save;
	ProgressDialog pd = null;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 0:
			//	Toast.makeText(UnStandardizeActivity.this, wdbhs+"", Toast.LENGTH_SHORT).show();
				pd.cancel();
				if(wdbhs.size()<=0){
					DialogUtil.showDialog(UnStandardizeActivity.this, "没有未标准化网点");
					return;
				}
				adapter = new UnStandAdapter(UnStandardizeActivity.this,wdbhs);
				lv.setAdapter(adapter);
				
				break;
			case 1:
				pd.cancel();
				Toast.makeText(UnStandardizeActivity.this, "出现未知异常", Toast.LENGTH_SHORT).show();
				
				break;
			case -2:
				pd.cancel();
				Toast.makeText(UnStandardizeActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
				break;
			}
			
		};	
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.requestWindowFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setContentView(R.layout.unstandardlize);
	
		lv =(ListView)this.findViewById(R.id.unstandlize_lv);
		exit = (Button)this.findViewById(R.id.unstandlize__back);
		save= (Button)this.findViewById(R.id.unstandlize_save);
		exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(UnStandardizeActivity.this,UslotteryRecord.class);
				startActivity(intent);
				UnStandardizeActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			 	
				finish();
				
			}
		});
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
			}
		});
		final String cid = Util.getSharedPrefercencesString(this, "cid");
		final String uid = Util.getSharedPrefercencesString(this, "uid");
		pd = ProgressDialog.show(UnStandardizeActivity.this, "提示", "正在与服务器交互，请稍等...", true,true);
		pd.show();
		new Thread(){
			public void run(){
				request(cid,uid);
			}
		}.start();
	}


public void request(String cid ,String uid){
	List<Map<String,String>> list=new ArrayList<Map<String,String>>();
	int result=0;
	if(HttpUtil.checkNet(UnStandardizeActivity.this)){
		 Map<String,String> map=new HashMap<String,String>();
		 map.put("cid", cid);
		 map.put("uid", uid);
		 try {
			String post=HttpUtil.postRequest(HttpUrl.URL+HttpUrl.findNoCheckWd,map);
			JSONArray jsonArray = new JSONArray(post);
				JSONArray js1 = jsonArray.getJSONArray(0);
			//	int length=js1.length();
			//	String stri=	js1.get(2).toString();
			//JSONObject obj = js1.getJSONObject(0);
				wdbhs = new ArrayList<String>();
				for(int i = 0;i < js1.length();i++){
				String str = js1.get(i).toString();
				wdbhs.add(str);
				}
				handler.sendEmptyMessage(0);
		} catch (ClientProtocolException e) {
			handler.sendEmptyMessage(1);
			e.printStackTrace();
		} catch (JSONException e) {
			handler.sendEmptyMessage(1);
			e.printStackTrace();
		} catch (IOException e) {
			handler.sendEmptyMessage(1);
			e.printStackTrace();
		}catch (Exception e) {
			handler.sendEmptyMessage(1);
			e.printStackTrace();
		}
     }else {
    	 handler.sendEmptyMessage(-2);
     }
}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	switch(keyCode){
	case  KeyEvent.KEYCODE_BACK:
		Intent intent =new Intent(UnStandardizeActivity.this,UslotteryRecord.class);
		startActivity(intent);
		UnStandardizeActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	 	
		finish();
		break;
	}
	return super.onKeyDown(keyCode, event);
}

	//主菜单通知列表适配器
	class UnStandAdapter extends BaseAdapter {

	//	private LayoutInflater inflater;
		private Context context;
		private List<String> wdbhs;

		@Override
		public int getCount() {
			return wdbhs.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public UnStandAdapter(Context context,List<String> wdbhs) {
			this.context = context;
		//	this.inflater = LayoutInflater.from(context);
			this.wdbhs = wdbhs;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {		
			 LinearLayout llb = new LinearLayout(context);
			    llb.setOrientation(LinearLayout.HORIZONTAL);
			  //  LayoutParams lp_llb = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
			//	lp_llb.setMargins(5, 5, 5, 5);
			//	llb.setLayoutParams(lp_llb);
//				
			 TextView tv_a = new TextView(context);
			    LayoutParams lp_tv_a = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			    tv_a.setText("网点编号:");
			    tv_a.setTextColor(Color.BLACK);
			    tv_a.setTextSize(16);
			 //   lp_tv_a.weight =1;
			   tv_a.setLayoutParams(lp_tv_a);
			    
		 
			    TextView tv_b = new TextView(context);
			    LayoutParams lp_tv_b = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			    tv_b.setText(wdbhs.get(position));
			    tv_b.setTextColor(Color.BLACK);
			    tv_b.setTextSize(20);
			   // lp_tv_b.weight =1;
			    tv_b.setLayoutParams(lp_tv_b);

			    llb.addView(tv_a);
			    llb.addView(tv_b);
			return llb;
		}
	}
}