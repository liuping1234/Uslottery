package com.lottery;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cr.uslotter.R;
import com.uslotter.util.DialogUtil;
import com.uslotter.util.HttpUtil;

public class WdXlActivity extends  BaseActivity {
	final String TAG = "--WdXlActivity--";
	ProgressDialog pBar =null;
	String gameNameArr[]={"����͸","����3","����5","���ǲ�","22ѡ5","36ѡ7","ʤ����","��ȫ��","�����"};
	List<String[]> xlList=null;
	String title=null;
	String date=null;
	int errorCode=0;//�����ж�
	NumberFormat   formater   =   new   DecimalFormat( "###,###.## "); 
	private Button btn_exit = null;
	@Override
	public void onCreate(Bundle savedInstanceState){
		title=getIntent().getStringExtra("title");
		date=getIntent().getStringExtra("date");
		showProgressDialog(this,"��������","��ȡ�����У����Ժ�...",ProgressDialog.STYLE_SPINNER);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.requestWindowFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daywdxllist);
		TextView wd=(TextView)findViewById(R.id.wd);
		wd.setText(title);
		TextView querydate=(TextView)findViewById(R.id.querydate);
		btn_exit = (Button)this.findViewById(R.id.daywdxllist_back);
		btn_exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		querydate.setText(date);
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what==1){
					showList();
			   }else{
				  if(errorCode==-1){
					   DialogUtil.showDialog(WdXlActivity.this, R.string.dialog_top_title, R.string.result_wd_error, true);
				   }else if(errorCode==-2){
					   DialogUtil.showDialog(WdXlActivity.this, R.string.dialog_top_title, R.string.result_xl_error, true);
				   }else if(errorCode == 0){
					   DialogUtil.showDialog(WdXlActivity.this, R.string.dialog_top_title, R.string.result_xl_err, true);	  
				   }else{
					   DialogUtil.showDialog(WdXlActivity.this, R.string.dialog_top_title, R.string.result_net_error, true);		    
				   }
			   }
			};
			
		};
		
		//�����߳����������粢��ȡ��������
		new Thread(){
			public void run(){
				boolean flag=findDayWdxlArray(title,date);
				Message m=new Message();
				if(flag){
					m.what=1;
				}else{
					m.what=0;
				}
				pBar.cancel();
				handler.sendMessage(m);
			}
		}.start();
	}
	
	//��ʾ��������
	private void showList(){
		Button btnpre = (Button)findViewById(R.id.btnpre);
		Button btnnext = (Button)findViewById(R.id.btnnext);
		btnpre.setOnClickListener(new BtnClickLister(-1));
		btnnext.setOnClickListener(new BtnClickLister(1));

		
		// ����һ��BaseAdapter���󣬸ö������ṩGallery����ʾ��ͼƬ
		BaseAdapter adapter = new BaseAdapter(){
			@Override
			public int getCount(){
				return xlList.size();
			}
			@Override
			public Object getItem(int position){
				return position;
			}
			@Override
			public long getItemId(int position){
				return position;
			}

			// �÷����ķ��ص�View���Ǵ�����ÿ���б���
			@Override
			public View getView(int position, View convertView, ViewGroup parent){
				String b[]=xlList.get(position);
				LinearLayout ll=new LinearLayout(WdXlActivity.this);
				ll.setOrientation(0);
				TableLayout table=new TableLayout(WdXlActivity.this);
				table.setStretchAllColumns(true);
				TableRow row=new TableRow(WdXlActivity.this);
				TextView textVew=new TextView(WdXlActivity.this);
				textVew.setTextSize(15);
				textVew.setText(b[0]);
				textVew.setPadding(10, 5, 10, 0);
				textVew.setTextColor(Color.BLACK);
				row.addView(textVew);
				textVew=new TextView(WdXlActivity.this);
				textVew.setTextSize(15);
				textVew.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
				textVew.setPadding(0, 0, 10, 0);
				textVew.setText(b[1]);
				textVew.setTextColor(Color.BLACK);
				row.addView(textVew);
				table.addView(row);
				ll.addView(table);
				return ll;
				
			}
		};		
		ListView list = (ListView)findViewById(R.id.daywdxllist);
		//ΪListView����Adapter
		list.setAdapter(adapter);
	}
	
	class BtnClickLister implements View.OnClickListener{
		int num=0;
		public BtnClickLister(int num){
			this.num=num;
		}
		public void onClick(View v){
			Calendar c=Calendar.getInstance();
			TextView querydate=(TextView)findViewById(R.id.querydate);
			String qdate=querydate.getText().toString();
			SimpleDateFormat dateFomat=new SimpleDateFormat("yyyy-MM-dd");
			try {
				c.setTime(dateFomat.parse(qdate));
				c.add(Calendar.DAY_OF_MONTH, num);
				Intent intent = new Intent();
				intent.putExtra("title", title);
				intent.putExtra("date", dateFomat.format(c.getTime()));
				intent.setAction("android.intent.action.VIEWWDXL");
			    startActivity(intent);
			    finish();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
	//�������粢��ѯ����������
	/**
	 * 
	 * @param title ��������
	 * @param date ��������
	 * @return
	 */
	private boolean findDayWdxlArray(String title,String date){
		try{
			String url=HttpUtil.BASE_URL;
			Map<String ,String> rawParams=new HashMap<String,String>();
			rawParams.put("title", title);
			rawParams.put("date", date);
			rawParams.put("oper", "5");
			JSONArray jsonArray=new JSONArray(HttpUtil.postRequest(url,rawParams));	
			errorCode=isCheckError(jsonArray);
			if(errorCode<0){
				return false;
			}
			xlList=new ArrayList<String[]>();
			int len=jsonArray.length();
			double total=0d;
			for(int i=0;i<len;i++){
				JSONObject o=(JSONObject)jsonArray.get(i);
				String kind=String.valueOf((Integer)o.get("Game_Kind_id"));	
				String gameName=(String)o.get("Game_Name");
				String je=o.get("Sale_JE").toString();	
				
				String b[]=new String[2];
				b[0]=gameName;
				double tje=Double.parseDouble(je);
				total+=tje;
				
				b[1]=formater.format(tje)+"Ԫ";
				xlList.add(b);
			}
			String b[]=new String[2];
			b[0]="�ϼ�";
			b[1]=formater.format(total)+"Ԫ";
			xlList.add(b);
			return true;
		}catch(Exception e){
			Log.e("WdXlActivity", e.getMessage());
			return false;
		}
	}
	

	
	//������
	private int isCheckError(JSONArray jsonArray) throws JSONException{
		int len=jsonArray.length();
		int errorCode=0;//û�д���
		if(len==1){
			JSONObject o = (JSONObject)jsonArray.get(0);
			String error=(String)o.get("error");
			errorCode= Integer.parseInt(error);
		}
		return errorCode;
	}
	
	//��ʾ�ȴ��Ի���
	private void showProgressDialog(Context ctx,String title, String msg,int style){
		pBar = new ProgressDialog(ctx);
		pBar.setTitle(title);
		pBar.setMessage(msg);
		pBar.setProgressStyle(style);
		pBar.show();
	}
}