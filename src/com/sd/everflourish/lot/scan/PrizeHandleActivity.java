package com.sd.everflourish.lot.scan;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sd.everflourish.BaseSdActivity;
import com.cr.uslotter.R;
import com.uslotter.util.HttpUtil;

public class PrizeHandleActivity extends BaseSdActivity {
	final String TAG = "--PrizeHandleActivity--";
	ProgressDialog pBar =null;
	String sid="";
	int errorCode=0;//û�д���
	//String title="17888";
	String draw[]=new String[5];
	@Override
	public void onCreate(Bundle savedInstanceState){
		sid=getIntent().getStringExtra("sid");
		showProgressDialog(this,"��ʾ��Ϣ","�ύ�����У����Ժ�...",ProgressDialog.STYLE_SPINNER);
		super.onCreate(savedInstanceState);
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what==1){
					showInfo(draw);
			   }else{
				   if(errorCode==-1){//   
					   showTipMsg("�ҽ�ʱ��ϵͳ�����쳣");
				   }else  if(errorCode==-2) {
					   showTipMsg("��������ʧ�ܣ������ԣ�");
				   }
				 
			   }
			};	
		};
		
		new Thread(){
			public void run(){
				int flag=handle(sid);
				Message m=new Message();
				if(flag==0){
					m.what=1;
				}else{
					m.what=0;
				}
				pBar.cancel();
				handler.sendMessage(m);
			}
		}.start();
	
	}
	
	 
  private int handle(String sid){
	  	//int errorCode=0;
		String code=sid.substring(0,2);
		if(!code.equals("35")){//���Ǽ�����Ʊ����ź�
			this.errorCode=-5;
			return this.errorCode;
		}
		String playId=String.valueOf(sid.substring(2,6));
		String info=String.valueOf(sid.substring(6,13));
		String serialNo=String.valueOf(sid.substring(13,16));
		String bagNo=code+"-"+playId+"-"+info;
		String url=HttpUtil.BASE_URL;
		Map<String ,String> rawParams=new HashMap<String,String>();
		rawParams.put("oper", "17");
		rawParams.put("sid", sid);
		rawParams.put("cid", lotApp.getCid());
		try {
			JSONArray jsonArray=new JSONArray(HttpUtil.postRequest(url,rawParams));		
			for(int i=0;i<jsonArray.length();i++){
				JSONObject obj=(JSONObject)jsonArray.get(i);
				JSONArray  json=(JSONArray)obj.get("draw");
				int size=json.length();
				for(int j=0;j<size;j++){
  					JSONObject o=(JSONObject)json.get(i);
  					 draw[0]=(String)o.get("status");
  					 draw[1]=(String)o.get("isprize");
  					 draw[2]=(String)o.get("prizetime");
  					 draw[3]=(String)o.get("gradeName");
  					 draw[4]=(String)o.get("drawName");	
  				}
				
				this.errorCode=(Integer)obj.get("errorCode");
			}
		}catch (Exception e) {
			//Log.e("handle", e.getMessage());
			this.errorCode=-2;
		}
		return this.errorCode;
	 }
	
  /**
	 * �ҽ��������
	 * @param b
	 */
	private void showInfo(String [] b){
		 this.setContentView(R.layout.prizeinfo);
		 TextView prizeinfo=(TextView)findViewById(R.id.prizeinfo);
		 String msg="";
		 if(b[0].equals("-1")){
			 msg="�ܱ�Ǹ������Ʊû�вμӳ齱��";
		 }else if(b[0].equals("0")){
			 msg="�ܱ�Ǹ��û���н���";
		 }else if(b[0].equals("1")){
			 msg=" ��ϲ�����н�����";
		 }
		 prizeinfo.setText(msg);
		 
		  TextView sidtext=(TextView)findViewById(R.id.sid);
		  sidtext.setText(sid);
		 // TextView gameName=(TextView)findViewById(R.id.gameName);
		 // gameName.setText("");
		 // TextView faceValue=(TextView)findViewById(R.id.faceValue);
		 // faceValue.setText(b[2]);
		  TextView gradeNo=(TextView)findViewById(R.id.gradeNo);
		  gradeNo.setText(b[3]);
		  TextView drawName=(TextView)findViewById(R.id.drawName);
		  drawName.setText(b[4]);
		  TextView status=(TextView)findViewById(R.id.status);
		  final String isPrize= b[1];
		  if(b[0].equals("1")&&isPrize.equals("0")){
			  b[1]="δ�ҽ�"; 
		  }else if(b[0].equals("1")&&isPrize.equals("1")){
			  b[1]="�Ѷҽ�"; 
		  }else{
			  b[1]=""; 
		  }
		  status.setText(b[1]);
		  TextView prizeTime=(TextView)findViewById(R.id.prizeTime);
		  prizeTime.setText(b[2]);
		  
		  Button btn = (Button)findViewById(R.id.btn);
		  if(!isPrize.equals("0")){
			  btn.setText("���ؼ����ҽ�") ;
		  }
			  
		  btn.setOnClickListener(new OnClickListener(){
			  public void onClick(View v){
				  if(isPrize.equals("0")){
					  showProgressDialog(PrizeHandleActivity.this,"��ʾ��Ϣ","�ύ�����У����Ժ�...",ProgressDialog.STYLE_SPINNER);
					  submitPrize();
				  }else{
					  goToDigilinxActivity(null);
				  }
				 
				}
		  });
		  Button clsbtn = (Button)findViewById(R.id.clsbtn); 
		  clsbtn.setOnClickListener(new OnClickListener(){
			  public void onClick(View v){
					 goToDigilinxActivity(null); 
				 
				}
		  });
	}
	
	private void submitPrize(){
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what==1){
					prizeTipMsg("�ҽ���ɣ�\nлл����������Ʊ��֧�֣�");
			   }else{
				   if(errorCode==-1){//   
					   prizeTipMsg("�ҽ�ʱ��ϵͳ�����쳣");
				   }else  if(errorCode==-2) {
					   prizeTipMsg("��������ʧ�ܣ������ԣ�");
				   }
				 
			   }
			};	
		};
		
		new Thread(){
			public void run(){
				int flag=doSubmitPrize(sid);
				Message m=new Message();
				if(flag==0){
					m.what=1;
				}else{
					m.what=0;
				}
				pBar.cancel();
				handler.sendMessage(m);
			}
		}.start();
	}
	
  private int doSubmitPrize(String sid){
	  	//int errorCode=0;
		String url=HttpUtil.BASE_URL;
		Map<String ,String> rawParams=new HashMap<String,String>();
		rawParams.put("oper", "18");
		rawParams.put("sid", sid);
		rawParams.put("cid", lotApp.getCid());
		try {
			JSONArray jsonArray=new JSONArray(HttpUtil.postRequest(url,rawParams));		
			for(int i=0;i<jsonArray.length();i++){
				JSONObject obj=(JSONObject)jsonArray.get(i);
				this.errorCode=(Integer)obj.get("errorCode");
			}
		}catch (Exception e) {
			this.errorCode=-2;
		}
		return this.errorCode;
	 }
  
	public void prizeTipMsg(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);	    
	   	builder.setMessage(message).setCancelable(false);
	   	builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){
		    	  @Override
				  public void onClick(DialogInterface dialog, int which){
		    		  goToDigilinxActivity(null);
				  }
	   	});		
	   	builder.create().show(); 
	}
	
	//��ʾ�ȴ��Ի���
	private void showProgressDialog(Context ctx,String title, String msg,int style){
		pBar = new ProgressDialog(ctx);
		pBar.setTitle(title);
		pBar.setMessage(msg);
		pBar.setProgressStyle(style);
		pBar.show();
	}
	
	
	public void showTipMsg(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);	    
	   	builder.setMessage(message).setCancelable(false);
	   	builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){
		    	  @Override
				  public void onClick(DialogInterface dialog, int which){
				 		goToDigilinxActivity(null);
				  }
	   	});		
	   	builder.create().show(); 
		//goToDigilinxActivity(message);
	   	
	}
	
	//������Ʊ��֤
	public void goToDigilinxActivity(String message) {
		 Intent intent = new Intent();
		 intent.putExtra("message", message);
		 intent.setClass(this, PrizeActivity.class);
	     startActivity(intent);
	     finish();
	}
	
	  @Override	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  //���¼����Ϸ��ذ�ť //
	  if(keyCode == KeyEvent.KEYCODE_BACK){ 
		  goToDigilinxActivity(null);
		  return true;
	 
	  }else{  
	   return super.onKeyDown(keyCode, event);	 
	  }	 
	 }
	  
	  public void showDialog(String message){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);	    
		   	builder.setMessage(message).setCancelable(false);
		   	builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){
			    	  @Override
					  public void onClick(DialogInterface dialog, int which){
					 		goToDigilinxActivity(null);
					  }
		   	});		
		   	builder.create().show(); 
		}
}
