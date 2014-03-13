package com.hz;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;
import com.uslotter.util.Util;
public class ReadCardHandleActivity extends Activity{
	private Handler handler = new Handler(); 
    ProgressDialog pBar =null;
    String cardId = "";
    String xcid = "";
    int errorCode = 0;//�����ж�
    String [] b=new String[21];
    Map<String,String> ptypeM=new HashMap<String,String>();
    Map<String,String> stypeM=new HashMap<String,String>();
    Map<String,String> regM=new HashMap<String,String>();
    Map<String,String> proM=new HashMap<String,String>();
    Map<String,String> jurM=new HashMap<String,String>();
    private String title = "";
    private String time = "";
    private String wd = "";
    private String flag ;
    private Button btn_return = null;
    private boolean flags = false;//û���ְ����Զ�����
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            cardId=getIntent().getStringExtra("cardId");
            xcid=getIntent().getStringExtra("xcid");
           title = this.getIntent().getStringExtra("title");
           time = this.getIntent().getStringExtra("time");
           wd = this.getIntent().getStringExtra("wd");
           flag = this.getIntent().getStringExtra("flag");
//           if(cardId == null||cardId.equals("")){
//        	   Toast.makeText(this, "��idΪ��!", Toast.LENGTH_SHORT).show();
//        	   return;
//           }
           if(xcid == null||xcid.equals("")){
        	 //  Toast.makeText(this, "�idΪ��!", Toast.LENGTH_SHORT).show();
        	   return;
           }
           if(title == null||title.equals("")){
        	   Toast.makeText(this, "�����Ϊ��!", Toast.LENGTH_SHORT).show();
        	   return;
           }
           
           if(time == null||time.equals("")){
        	   Toast.makeText(this, "�����Ϊ��!", Toast.LENGTH_SHORT).show();
        	   return;
           }
//           if(wd == null||wd.equals("")){
//        	   Toast.makeText(this, "������Ϊ��!", Toast.LENGTH_SHORT).show();
//        	   return;
//           }
           if(flag == null||flag.equals("")){
        	   Toast.makeText(this, "����������ˢ����������!", Toast.LENGTH_SHORT).show();
        	   return;
           }
        	showProgressDialog(this,"������Ϣ","��ȡ�����У����Ժ�...",ProgressDialog.STYLE_SPINNER);
        	init();
      		final Handler handler = new Handler(){
    			@Override
    			public void handleMessage(Message msg){
    				if(msg.what==1){//��ʾerrorCode == 1
    					if(b[17].equals("")||b[17]== null){
    						return;
    					}
    					int count = Integer.parseInt(b[17]);
    					
    					if(count == -1){
    							goToReadCardActivity("������:"+wd+"\nǩ�������޶�����!");    						 
    					}else if(count == 0){
    						
    						 if(flag.trim().endsWith("1")){//��
    							 goToReadCardActivity("�򿨲��ڱ��λ��Χ��");
      					 }else if(flag.trim().endsWith("2")){//����
      						 goToReadCardActivity("�����Ų��ڱ��λ��Χ��");
      					   }
    					}else {
    						show();	
    					}
    			   }else{
    				   if(errorCode==0){
    					   if(flag.trim().endsWith("1")){//��
    						   goToReadCardActivity("��Ч��!");
    					   }else if(flag.trim().endsWith("2")){//����
    						   goToReadCardActivity("��Ч������:"+wd);
    					   }
     					  // showDialog("������Ϣ������!");
     					  //Toast.makeText(WdInfoActivity.this, "", Toast.LENGTH_SHORT).show();
     					   
     				   }else if(errorCode==-2){
     					  goToReadCardActivity("ˢ�������쳣�������ԣ�");
     					 
    					  // showDialog("������Ϣ������!");
    					   //Toast.makeText(WdInfoActivity.this, "", Toast.LENGTH_SHORT).show();
    					   
    				   }else if(errorCode==-1){	
    					   goToReadCardActivity("��������ʧ��!");
    					  // showDialog("��������ʧ��!");
    					  // Toast.makeText(WdInfoActivity.this, "��������ʧ��!", Toast.LENGTH_SHORT).show();
    				   }else if(errorCode==-3){	
    					   goToReadCardActivity("ˢ�������쳣�������ԣ�");
    					  // showDialog("��ʵ������Ϣ�������쳣!");
    					   //Toast.makeText(WdInfoActivity.this, "��ʵ������Ϣ�������쳣!", Toast.LENGTH_SHORT).show();
    				   }
    			   }
    			};
    		};

    		//�����߳����������粢��ȡ��������
    		new Thread(){
    			public void run(){
    				boolean flag=findWdInfoByTitle();
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
    
    private void init(){
    	ptypeM.put("0", "���");
    	ptypeM.put("1", "��ͳ");
    	ptypeM.put("2", "����");
    	
    	stypeM.put("0", "����");
    	stypeM.put("1", "רӪ���");
    	stypeM.put("2", "��Ӫ����");
    	stypeM.put("3", "��Ӫ����");
    	stypeM.put("4", "��������ͤ");
    	stypeM.put("5", "����");
    	stypeM.put("6", "��Ӫ���ʼ�����");
    	stypeM.put("7", "רӪ���-������");
    	
    	regM.put("0", "����");
    	regM.put("1", "סլ��");
    	regM.put("2", "��ҵ��");
    	regM.put("3", "��ҵ��");
    	regM.put("4", "����");
    	
    	proM.put("0", "����");
    	proM.put("1", "����");
    	proM.put("2", "����");
    	
    	jurM.put("1", "�м�");
    	jurM.put("2", "�ؼ�");
    	jurM.put("3", "��");
    	jurM.put("4", "�弶");
    }
    
    private void show(){
    	setContentView(R.layout.wdinfo_hz);
    	ImageView logo = (ImageView)findViewById(R.id.wdinfo_hz_logo);
    	logo.setPadding(10, 10, 0, 0);
    	logo.setImageResource(R.drawable.ticailogo);
		logo.setMaxHeight(100);
		logo.setMaxWidth(100);
		logo.setPadding(5, 0, 0, 0);
		
    	TextView title = (TextView)findViewById(R.id.wdinfo_hz_title);
    	title.setText("������: "+b[0]);
//    	TextView phone = (TextView)findViewById(R.id.phone);
//    	phone.setText("����绰: "+b[1]);
    	
//    	TextView status = (TextView)findViewById(R.id.status);
//    	status.setText("����״̬: "+b[16]);
    	
    	TextView cz = (TextView)findViewById(R.id.wdinfo_hz_cz);
    	cz.setText("��������: "+b[2]);
//    	TextView address = (TextView)findViewById(R.id.address);
//    	address.setText("�����ַ: "+b[3]);
//    	address.setOnClickListener(new View.OnClickListener(){
//
//			public void onClick(View v) {
//				goWdMapActivity(b[3]);	
//			}
//			
//		});
    	
    	TextView jc = (TextView)findViewById(R.id.wdinfo_hz_jc);
    	if(b[19].trim().equals("")||b[19].trim().equals("δ��ͨ")){
    		jc.setTextColor(Color.BLACK);
    		jc.setText("����: δ��ͨ");
    	}else{
    		jc.setText("����: "+b[19]);
    	}
    	

    	TextView gp = (TextView)findViewById(R.id.wdinfo_hz_gp);
    	if(b[20].trim().equals("")||b[20].trim().equals("δ��ͨ")){
    		gp.setTextColor(Color.BLACK);
    		gp.setText("��Ƶ: δ��ͨ");
    	}else{
    		gp.setText("��Ƶ: "+b[20]);
    	}
    	

    	TextView oinfo = (TextView)findViewById(R.id.wdinfo_hz_name);
    	//oinfo.setText("����: "+b[12]+"\n�绰: "+b[13]+"\n�ֻ�: "+b[14]+"\n��ַ: "+b[15]);
    	oinfo.setText("ҵ���� "+b[12]);
    	
    	TextView t_count = (TextView)findViewById(R.id.wdinfo_hz_tcount);
    	t_count.setText("������ "+b[17]);
    	
    	TextView t_prompt = (TextView)findViewById(R.id.wdinfo_hz_prompt);
    	t_prompt.setMovementMethod(ScrollingMovementMethod.getInstance());
    	ImageView _iv = (ImageView)findViewById(R.id.wdinfo_hz_iv_bottom);
    	if(!b[18].equals("")){
    		_iv.setVisibility(View.VISIBLE);
    		t_prompt.setVisibility(View.VISIBLE);
    		t_prompt.setText("��ʾ��"+b[18]);
    		
//    		handler.postDelayed(new Runnable(){
//
//    			@Override
//    			public void run() {
//    				if(!flags){
//				
//    				goToReadCardActivity("");	
//    				}
//    			}
//        		
//        	}, 10000);
 //   	}else{
//    		handler.postDelayed(new Runnable(){
//
//    			@Override
//    			public void run() {
//    				if(!flags){
//    				goToReadCardActivity("");
//    				}
//    			}
//        		
//        	}, 6000);
    	}
//    	TextView wdinfo = (TextView)findViewById(R.id.wdinfo);
//    	wdinfo.setText("��������: "+b[4]+"\n��Ӫ����: "+b[5]+"\n�����ض�: "+b[6]+"\n��������: "+b[7]+"\n������Ͻ: "+b[8]);
//    	wdinfo.setText(wdinfo.getText().toString()+"\n�������: "+b[9]+"\n��վʱ��: "+b[10]+"\n��Ƶ: "+b[11]);
    	btn_return = (Button)findViewById(R.id.wdinfo_hz_return); 	
    	btn_return.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flags = true;
				goToReadCardActivity("");	
				
			}
		});
    }

  		private boolean findWdInfoByTitle(){
  			boolean flag=false;
  			try{
  				SharedPreferences	preferences = getSharedPreferences("hzLotteryConfig", MODE_WORLD_READABLE);
  			//	String mobile= preferences.getString("mobile", "");
  				String url = HttpUrl.URL+HttpUrl.query;
  				Map<String ,String> rawParams=new HashMap<String,String>();
  				rawParams.put("xcId", xcid);
  				rawParams.put("cardId", cardId);
  				//rawParams.put("mobile", mobile);
  				rawParams.put("cid", Util.getSharedPrefercencesString(this, "cid"));
  				rawParams.put("oper", "2");
  				rawParams.put("wd",wd);
  				SharedPreferences _login = getSharedPreferences("Login", MODE_WORLD_WRITEABLE);
  				//rawParams.put("u_id",Util.getSharedPrefercencesString(this, "uid"));
  				rawParams.put("u_id",_login.getString("username", ""));
  				String result=HttpUtil.postRequest(url,rawParams);
  				JSONArray jsonArray=new JSONArray(result);
  				errorCode=isCheckError(jsonArray);
  				if(errorCode<=0){
  					return flag;
  				}
  				
  				int len=jsonArray.length();
  				
  				for(int i=0;i<len;i++){		
  					JSONObject o=(JSONObject)jsonArray.get(i);
  					//a.Title, b.CZ_Name,a.PAddress,a.PPhone,a.Status,a.TitlePhoto, a.PTypeId, a.STypeId ,  
  					//a.RegionId,a.ProId, a.JurId, a.Area,a.Begin_date, a.gpstatus, 
  					//a.OName,a.OPhone,a.MobilePhone, a.OAddress
  					 b[0]=(String)o.get("Title");
  					 b[1]=o.get("PPhone")==null?"":(String)o.get("PPhone");
  					
  					 b[2]=(String)o.get("CZ_Name");
  					 b[3]=(String)o.get("PAddress");
  					 
  					 b[4]=ptypeM.get(String.valueOf(o.get("PTypeId")));
  					 b[5]=stypeM.get(String.valueOf(o.get("STypeId")));//��Ӫ����
  					 b[6]=regM.get(String.valueOf(o.get("RegionId")));//�����ض�
  					 b[7]=proM.get(String.valueOf(o.get("ProId")));//��������
  					 b[8]=jurM.get(String.valueOf(o.get("JurId")));//������Ͻ
  					
//  					b[4]=ptypeM.get(String.valueOf((Integer)o.get("PTypeId")));
//  					b[5]=stypeM.get(String.valueOf((Integer)o.get("STypeId")));//��Ӫ����
//  					b[6]=regM.get(String.valueOf((Integer)o.get("RegionId")));//�����ض�
//  					b[7]=proM.get(String.valueOf((Integer)o.get("ProId")));//��������
//  					b[8]=jurM.get(String.valueOf((Integer)o.get("JurId")));//������Ͻ
  				//	b[9]=String.valueOf((Integer)o.get("Area"));
  					
  					b[9]=String.valueOf(o.get("Area"));
  					b[10]=((String)o.get("Begin_date")).substring(0,10);
  					b[11]=o.get("gpstatus")==null?"":(String)o.get("gpstatus");
  					
  					b[12]=(String)o.get("OName");
  					b[13]=(String)o.get("OPhone");
  					b[14]=(String)o.get("MobilePhone");
  					b[15]=(String)o.get("OAddress");
  					
  					b[16]=(String)o.get("Status");
  					b[17] = o.getString("Counts");
  					b[18] = o.getString("txMsg");
  					b[19] = o.getString("jcstatus");
  					b[20] = o.getString("gpstatus");
  					
  				}
  				flag= true;
  			}catch (ClientProtocolException e) {
  				errorCode=-1;
  			} catch (IOException e) {
  				errorCode=-1;
  			}catch(Exception e){
  				errorCode=-3;
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
  		
  		public  Bitmap getImage(String Url)  {
  			try {
  				URL url = new URL(Url);
  				String responseCode = url.openConnection().getHeaderField(0);
  				//if (responseCode.indexOf("200") < 0)
  					//throw new Exception("ͼƬ�ļ������ڻ�·�����󣬴�����룺" + responseCode);
  				return BitmapFactory.decodeStream(url.openStream());
  			} catch (Exception e) {
  				return BitmapFactory.decodeResource(getResources(), R.drawable.icon);
  			}
  		}

	// ������
	private int isCheckError(JSONArray jsonArray) throws JSONException {
		int len = jsonArray.length();
		int errorCode = 1;// û�д���
		if (len == 1) {
			try {
				JSONObject o = (JSONObject) jsonArray.get(0);
				String error = (String) o.getString("error");
				errorCode = Integer.parseInt(error);
			} catch (Exception e) {
				errorCode = -3;
			}
		}
		return errorCode;
	}
	
	
	public void showDialog(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);	    
	   	builder.setMessage(message).setCancelable(false);
	   	builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){		    	
				  public void onClick(DialogInterface dialog, int which){
					  goToReadCardActivity("");
				  }
	   	});		
	   	builder.create().show(); 
	}
	
	public void goToReadCardActivity(String msg){
		 Intent intent = new Intent();
		intent.putExtra("msg", msg);
		intent.putExtra("xcid", xcid);
		intent.putExtra("title", title);
		intent.putExtra("time",time);
		  intent.setAction("android.intent.action.READCARD");
		  startActivity(intent);
		  finish();
	}
	
	public void goWdMapActivity(String address) {
		 Intent intent = new Intent();
		 intent.putExtra("address", address);
		 intent.setAction("android.intent.action.VIEWMAP");
	     startActivity(intent);
	     finish();
	}
	
	 @Override	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  //���¼����Ϸ��ذ�ť //
	  if(keyCode == KeyEvent.KEYCODE_BACK){ 
		  goToReadCardActivity("");
//		  Intent intent = new Intent();
//		  Intent intent = new Intent();
//		  intent.putExtra("xcid", xcid);
//		  intent.setAction("android.intent.action.READCARD");
//		  startActivity(intent);
		  return true;
	  }else{  
	      return super.onKeyDown(keyCode, event);	 
	  }	 
	 }
}