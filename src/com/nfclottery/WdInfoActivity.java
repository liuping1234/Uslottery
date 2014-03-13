package com.nfclottery;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.cr.uslotter.R;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;
public class WdInfoActivity extends BaseActivity {

    ProgressDialog pBar =null;
    String title="";
    int errorCode=0;//�����ж�
    String [] b=new String[17];
    Map<String,String> ptypeM=new HashMap<String,String>();
    Map<String,String> stypeM=new HashMap<String,String>();
    Map<String,String> regM=new HashMap<String,String>();
    Map<String,String> proM=new HashMap<String,String>();
    Map<String,String> jurM=new HashMap<String,String>();
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);    
            title=getIntent().getStringExtra("title");
        	showProgressDialog(this,"������Ϣ","��ȡ�����У����Ժ�...",ProgressDialog.STYLE_SPINNER);
        	init();
      		final Handler handler = new Handler(){
    			@Override
    			public void handleMessage(Message msg){
    				if(msg.what==1){
    					show();	
    			   }else{
    				   if(errorCode==-2){
    					  // showDialog("������Ϣ������!");
    					   //Toast.makeText(WdInfoActivity.this, "", Toast.LENGTH_SHORT).show();
    					   goToMainActivity("������Ϣ������!");
    				   }else if(errorCode==-1){	
    					   goToMainActivity("��������ʧ��!");
    					  // showDialog("��������ʧ��!");
    					  // Toast.makeText(WdInfoActivity.this, "��������ʧ��!", Toast.LENGTH_SHORT).show();
    				   }else if(errorCode==-3){	
    					   goToMainActivity("��ʵ������Ϣ�������쳣!");
    					  // showDialog("��ʵ������Ϣ�������쳣!");
    					   //Toast.makeText(WdInfoActivity.this, "��ʵ������Ϣ�������쳣!", Toast.LENGTH_SHORT).show();
    				   }
    			   }
    			};
    			
    		};

    		//�����߳����������粢��ȡ��������
    		new Thread(){
    			public void run(){
    				boolean flag=findWdInfoByTitle(title);
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
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.requestWindowFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
    	setContentView(R.layout.wdinfonfc);
    	ImageView logo = (ImageView)findViewById(R.id.logo);
    	logo.setPadding(10, 10, 0, 0);
    	logo.setImageResource(R.drawable.ticailogo);
		logo.setMaxHeight(100);
		logo.setMaxWidth(100);
		logo.setPadding(5, 0, 0, 0);
		
    	TextView title = (TextView)findViewById(R.id.title);
    	title.setText("������: "+b[0]);
    	TextView phone = (TextView)findViewById(R.id.phone);
    	phone.setText("����绰: "+b[1]);
    	
    	TextView status = (TextView)findViewById(R.id.status);
    	status.setText("����״̬: "+b[16]);
    	
    	TextView cz = (TextView)findViewById(R.id.cz);
    	cz.setText("��������: "+b[2]);
    	TextView address = (TextView)findViewById(R.id.address);
    	address.setText("�����ַ: "+b[3]);
    	address.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				goWdMapActivity(b[3]);
				
				
			}
			
		});
    	
    	TextView oinfo = (TextView)findViewById(R.id.oinfo);
    	oinfo.setText("����: "+b[12]+"\n�绰: "+b[13]+"\n�ֻ�: "+b[14]+"\n��ַ: "+b[15]);
    	
    	TextView wdinfo = (TextView)findViewById(R.id.wdinfo);
    	wdinfo.setText("��������: "+b[4]+"\n��Ӫ����: "+b[5]+"\n�����ض�: "+b[6]+"\n��������: "+b[7]+"\n������Ͻ: "+b[8]);
    	wdinfo.setText(wdinfo.getText().toString()+"\n�������: "+b[9]+"\n��վʱ��: "+b[10]+"\n��Ƶ: "+b[11]);
    }

  		private boolean findWdInfoByTitle(String title){
  			boolean flag=false;
  			try{
  				String url=HttpUtil.BASE_URL;
  				Map<String ,String> rawParams=new HashMap<String,String>();
  				rawParams.put("title", title);
  				rawParams.put("oper", "11");
  				String result=HttpUtil.postRequest(url,rawParams);		
  				JSONArray jsonArray=new JSONArray(result);
  				errorCode=isCheckError(jsonArray);
  				if(errorCode<0){
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
  					 
  					b[4]=ptypeM.get(String.valueOf((Integer)o.get("PTypeId")));
  					b[5]=stypeM.get(String.valueOf((Integer)o.get("STypeId")));//��Ӫ����
  					b[6]=regM.get(String.valueOf((Integer)o.get("RegionId")));//�����ض�
  					b[7]=proM.get(String.valueOf((Integer)o.get("ProId")));//��������
  					b[8]=jurM.get(String.valueOf((Integer)o.get("JurId")));//������Ͻ
  					
  					b[9]=String.valueOf((Integer)o.get("Area"));
  					b[10]=((String)o.get("Begin_date")).substring(0,10);
  					b[11]=o.get("gpstatus")==null?"":(String)o.get("gpstatus");
  					
  					b[12]=(String)o.get("OName");
  					b[13]=(String)o.get("OPhone");
  					b[14]=(String)o.get("MobilePhone");
  					b[15]=(String)o.get("OAddress");
  					
  					b[16]=(String)o.get("Status");
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


  	//������
	private int isCheckError(JSONArray jsonArray) throws JSONException{
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
				  public void onClick(DialogInterface dialog, int which){
				 		goToMainActivity("");
				  }
	   	});		
	   	builder.create().show(); 
	}
	
	public void goToMainActivity(String msg){
		 Intent intent = new Intent(this,MainNfcActivity.class);
		 intent.putExtra("msg", msg);
	     startActivity(intent);
		
	}
	
	public void goWdMapActivity(String address) {
		 Intent intent = new Intent();
		 intent.putExtra("address", address);
		 intent.setAction("android.intent.action.VIEWMAP");
	     startActivity(intent);
	}
	
}