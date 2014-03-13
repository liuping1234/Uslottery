package com.sd.everflourish.lot.check;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sd.everflourish.BaseSdActivity;
import com.cr.uslotter.R;
import com.uslotter.util.HttpUtil;

public class CheckLotActivity extends BaseSdActivity {
	final String TAG = "--CheckLotActivity--";
	ProgressDialog pBar =null;
	private int[] imageIds = new int[]{R.drawable.jklog, R.drawable.j81};
	String[] b=new String[5];
	String sid="";
	int errorCode=0;//û�д���
	@Override
	public void onCreate(Bundle savedInstanceState){
		sid=getIntent().getStringExtra("sid");
		showProgressDialog(this,"��֤��Ʊ��Ϣ","��ȡ�����У����Ժ�...",ProgressDialog.STYLE_SPINNER);
		super.onCreate(savedInstanceState);
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what==1){
					showLotteryInfo(b);
			   }else{
				   if(b[0].equals("-1")){//��ȡ�Ĳ��Ǽ���Ʊ�Ķ�ά��
					   showDialog("���ȡ����Ʊ��ά�벿�֣�");
				   }else  if(b[0].equals("-2")) {//û���ҵ����ڵ�����
					   showDialog("û�ж�Ӧ�������ݣ�");
				   }else  if(b[0].equals("-3")) {//���������쳣
					   showDialog("��ȡ��Ʊ��Ϣ�����������쳣��");
				   }else  if(b[0].equals("-4")) {
					   showDialog("��������ʧ�ܣ������ԣ�");
				   }
				 
			   }
			};	
		};
		
		//�����߳����������粢��ȡ��������
		new Thread(){
			public void run(){
				boolean flag=findLotteryInfoBysid(sid);
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
	
	/**
	 * ������֤�������
	 * @param b
	 */
	private void showLotteryInfo(String [] b){
		 this.setContentView(R.layout.lotteryinfo);
		  ImageView gameImg=(ImageView)findViewById(R.id.lotteryImg);
		  gameImg.setImageResource(imageIds[0]);
		  TextView sid=(TextView)findViewById(R.id.sid);
		  sid.setText(b[0]);
		  TextView gameName=(TextView)findViewById(R.id.gameName);
		  gameName.setText(b[1]);
		  TextView faceValue=(TextView)findViewById(R.id.faceValue);
		  faceValue.setText(b[2]);
		  TextView city=(TextView)findViewById(R.id.city);
		  city.setText(b[3]);
		  TextView rkrq=(TextView)findViewById(R.id.rkrq);
		  rkrq.setText(b[4]);
		  Button btn = (Button)findViewById(R.id.btn);
		  btn.setOnClickListener(new OnClickListener(){
			  public void onClick(View v){
				  goToDigilinxActivity();
				}
		  });
	}
	 
  private boolean findLotteryInfoBysid(String sid){
	  	boolean flag=false;
		String code=sid.substring(0,2);
		if(!code.equals("35")){//���Ǽ�����Ʊ����ź�
			b[0]="-1";
			return flag;
		}
		
		String playId=String.valueOf(Integer.parseInt(sid.substring(2,6)));
		String info=String.valueOf(Integer.parseInt(sid.substring(6,13)));
		String url=HttpUtil.BASE_URL;
		Map<String ,String> rawParams=new HashMap<String,String>();
		rawParams.put("oper", "4");
		rawParams.put("playId", playId);
		rawParams.put("info", info);
		try {
			JSONArray jsonArray=new JSONArray(HttpUtil.postRequest(url,rawParams));		
			errorCode= isCheckError(jsonArray);
			if(errorCode<0){
				b[0]="-2";
				return flag;
			}
			//����ʱ����������
			for(int i=0;i<jsonArray.length();i++){
				JSONObject o=(JSONObject)jsonArray.get(i);
				
				String creTime=o.get("CreTime").toString().substring(0,10);
				Log.d("creTime", creTime);
				String game_Name=(String)o.get("game_Name");
				String face_Value=String.valueOf((Integer)o.get("face_Value"));
				String c_Name=(String)o.get("C_Name");
				
				
				b[0]=sid;
				b[1]=game_Name;
				b[2]=face_Value;
				b[3]=c_Name;
				b[4]=creTime;
			}
			flag=true;
		}catch (ClientProtocolException e) {
			b[0]="-4";
		}catch (IOException e) {
			b[0]="-4";
		}catch (Exception e) {
			Log.e("checker", e.getMessage());
			b[0]="-3";
		}
		return flag;
	 }
	
	
	//��ʾ�ȴ��Ի���
	private void showProgressDialog(Context ctx,String title, String msg,int style){
		pBar = new ProgressDialog(ctx);
		pBar.setTitle(title);
		pBar.setMessage(msg);
		pBar.setProgressStyle(style);
		pBar.show();
	}
	
	//������
	private int isCheckError(JSONArray jsonArray) {
		int len=jsonArray.length();
		int errorCode=0;//û�д���
		if(len==1){
			try{
				JSONObject o = (JSONObject)jsonArray.get(0);
				String error=(String)o.get("error");
				errorCode= Integer.parseInt(error);
			}catch(Exception e){}	
			}
		return errorCode;
	}
	
	public void showDialog(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);	    
	   	builder.setMessage(message).setCancelable(false);
	   	builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){
		    	  @Override
				  public void onClick(DialogInterface dialog, int which){
				 		goToDigilinxActivity();
				  }
	   	});		
	   	builder.create().show(); 
	}
	
	//������Ʊ��֤
	public void goToDigilinxActivity() {
		 Intent intent = new Intent();
		// intent.addCategory("android.intent.category.LAUNCHER");
		 intent.setClass(CheckLotActivity.this, LotteryCheckMainActivity.class);
	     startActivity(intent);
	     finish();
	}
	
	  @Override	 
		 public boolean onKeyDown(int keyCode, KeyEvent event) {
		  //���¼����Ϸ��ذ�ť //
		  if(keyCode == KeyEvent.KEYCODE_BACK){ 
			  goToDigilinxActivity();
			  return true;
		 
		  }else{  
		   return super.onKeyDown(keyCode, event);	 
		  }	 
		 }
}