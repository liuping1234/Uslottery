package com.nfclottery;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.cr.uslotter.R;
import com.uslotter.util.HttpUtil;

public class JkConfirmActivity extends BaseActivity {

    ProgressDialog pBar =null;
    String cardId="";
    String title="";
    int errorCode=0;//�����ж�
    String [] b=new String[17];
    Map<String,String> ptypeM=new HashMap<String,String>();
    Map<String,String> stypeM=new HashMap<String,String>();
    Map<String,String> regM=new HashMap<String,String>();
    Map<String,String> proM=new HashMap<String,String>();
    Map<String,String> jurM=new HashMap<String,String>();
    List<String[]> jkLotList=new ArrayList<String[]>();
    List<String[]> mateList=new ArrayList<String[]>();
    NumberFormat   formater   =   new   DecimalFormat( "###,###.## "); 
    SimpleDateFormat datefmt=new SimpleDateFormat("yyyy-mm-dd");
    int appid=0;//������Ʊ�Ķ���ID
    int wdType=0;//�������ͣ�1�������㡢2��ἴ�����㣩
    int qty=0;//����Ʊ�İ���
    int confirmType=2;//ȷ�Ϸ�ʽ 2��ic����3�ֹ����������ŷ�ʽ
    List<String> selMateNoList=new ArrayList<String>();
    private Button btn_return = null;
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    		this.requestWindowFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    		
            cardId=getIntent().getStringExtra("cardId");
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
    					   goToMainActivity("������Ϣ������!");
    				   }else if(errorCode==-1){	
    					   goToMainActivity("��������ʧ��!");
    				   }else if(errorCode==-3){	
    					   goToMainActivity("��ѯ������Ϣ�������쳣!");
    				   }else if(errorCode==-4){	
    					   goToMainActivity("��ЧIC��!");
    				   }else if(errorCode==-5){	
    					   goToMainActivity("���㲻���ڡ����Ѿ���վ!");
    				   }else if(errorCode==-6){	
    					   goToMainActivity("IC���ѱ�ͣ�û�ע��!");
    				   }
    			   }
    			};
    			
    		};

    		//�����߳����������粢��ȡ��������
    		new Thread(){
    			public void run(){
    				boolean flag=findWdInfoBy(cardId,title);
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
    	
    	 setContentView(R.layout.jklotinfo);
    	 btn_return = (Button)this.findViewById(R.id.jklotinfo_back);
         btn_return.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
				}
			});
    	ImageView logo = (ImageView)findViewById(R.id.logo);
    	logo.setPadding(10, 10, 0, 0);
    	logo.setImageResource(R.drawable.ticailogo);
		logo.setMaxHeight(100);
		logo.setMaxWidth(100);
		logo.setPadding(5, 0, 0, 0);
		logo.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(wdType==1){
					goWdInfoActivity(title);
				}
				
			}
			
		});
		
		Button btn=(Button)findViewById(R.id.cfmbtm);
		btn.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				if(qty==0&&findMateInfo().size()==0){//û��Ʊ��Ϣ��������Ϣ
					Toast.makeText(JkConfirmActivity.this,"û���κβ�Ʊ��������Ҫȷ��", Toast.LENGTH_SHORT).show();
					return ;		
				}
				// TODO Auto-generated method stub
				showProgressDialog(JkConfirmActivity.this,"��ʾ��Ϣ","�ύȷ����Ϣ�����Ժ�...",ProgressDialog.STYLE_SPINNER);
				final Handler handler = new Handler(){
	    			@Override
	    			public void handleMessage(Message msg){
	    				if(msg.what==1){
	    					 goToMainActivity("�ύȷ����Ϣ�ɹ�!");
	    			   }else{
	    					 if(errorCode==-1){ 
	    						 goToMainActivity("�ύȷ����Ϣ�������쳣!");
	    					}
	    			   }
	    			};
	    		};

				new Thread(){
	    			public void run(){
	    				boolean flag=doIcCardCfm(b[0]);
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
			
		});
    	TextView title = (TextView)findViewById(R.id.title);
    	title.setText("������: "+b[0]);
    	title.setTextColor(Color.BLACK);
    	TextView phone = (TextView)findViewById(R.id.phone);
    	phone.setTextColor(Color.BLACK);
    	phone.setText("����绰: "+b[1]);
    	
    	TextView status = (TextView)findViewById(R.id.status);
    	status.setText("����״̬: "+b[4]);
    	status.setTextColor(Color.BLACK);
    	
    	TextView cz = (TextView)findViewById(R.id.cz);
    	cz.setText("��������: "+b[2]);
    	cz.setTextColor(Color.BLACK);
    	TextView address = (TextView)findViewById(R.id.address);
    	address.setText("�����ַ: "+b[3]);
    	address.setTextColor(Color.BLACK);
    		
    	TextView ydtime=(TextView)findViewById(R.id.ydTime);
    	ydtime.setText("");
    	ydtime.setTextColor(Color.BLACK);
    	
    	double total=0;
		int textSize=15;
		int rowHeight=45;
    	TableLayout table = (TableLayout)findViewById(R.id.lotinfo);
    	table.setStretchAllColumns(true);
    	
    	TableRow row1=new TableRow(this);
		TextView textVew1 = new TextView(this);
		textVew1.setTextSize(15);
		textVew1.setText("��������");
		textVew1.setGravity(Gravity.LEFT);
		textVew1.setHeight(rowHeight);
		textVew1.setPadding(35, 0, 10, 0);
		textVew1.setTextColor(Color.BLACK);
    	row1.addView(textVew1);
		
		textVew1=new TextView(this);
		textVew1.setTextSize(15);
		textVew1.setText("���� ");
		textVew1.setGravity(Gravity.CENTER);
		textVew1.setHeight(rowHeight);
		textVew1.setPadding(35, 0, 10, 0);
		textVew1.setTextColor(Color.BLACK);
    	row1.addView(textVew1);
			
		textVew1=new TextView(this);
		textVew1.setTextSize(15);
		textVew1.setText("���(Ԫ)");
		textVew1.setGravity(Gravity.RIGHT);
		textVew1.setHeight(rowHeight);
		textVew1.setPadding(35, 0, 10, 0);
		textVew1.setTextColor(Color.BLACK);
    	row1.addView(textVew1);
		table.addView(row1);
		
    	for(String [] b:jkLotList){
    		qty+=Integer.parseInt(b[1]);
    		TableRow row=new TableRow(this);
    		TextView textVew=new TextView(this);
    		textVew.setTextSize(textSize);
    		textVew.setText(b[0]);
    		textVew.setGravity(Gravity.LEFT);
    		textVew.setHeight(rowHeight);
    		textVew.setPadding(35, 5, 10, 0);
    		textVew.setTextColor(Color.BLACK);
        	row.addView(textVew);
    		
    		textVew=new TextView(this);
    		textVew.setTextSize(textSize);
    		textVew.setText(b[1]);
    		textVew.setGravity(Gravity.CENTER);
    		textVew.setHeight(rowHeight);
    		textVew.setPadding(35, 5, 10, 0);
    		textVew.setTextColor(Color.BLACK);
        	row.addView(textVew);
    		double je=Double.parseDouble(b[2]);
    		total+=je;
			b[2]=formater.format(je)+"Ԫ";
    		textVew=new TextView(this);
    		textVew.setTextSize(textSize);
    		textVew.setText(b[2]);
    		textVew.setGravity(Gravity.RIGHT);
    		textVew.setHeight(rowHeight);
    		textVew.setPadding(35, 5, 10, 0);
    		textVew.setTextColor(Color.BLACK);
        	row.addView(textVew);
    		table.addView(row);
    	}
    	
    	
    	
    	TableRow row=new TableRow(this);
		TextView textVew=new TextView(this);
		textVew.setTextSize(textSize);
		textVew.setText("�ϼ�");
		textVew.setGravity(Gravity.LEFT);
		textVew.setHeight(rowHeight);
		textVew.setPadding(35, 5, 10, 0);
		textVew.setTextColor(Color.BLACK);
    	row.addView(textVew);
		
		textVew=new TextView(this);
		textVew.setTextSize(textSize);
		textVew.setText(""+qty);
		textVew.setGravity(Gravity.CENTER);
		textVew.setHeight(rowHeight);
		textVew.setPadding(35, 5, 10, 0);
		textVew.setTextColor(Color.BLACK);
    	row.addView(textVew);
		textVew=new TextView(this);
		textVew.setTextSize(textSize);
		textVew.setText(formater.format(total)+"Ԫ");
		textVew.setGravity(Gravity.RIGHT);
		textVew.setHeight(rowHeight);
		textVew.setPadding(35, 5, 10, 0);
		textVew.setTextColor(Color.BLACK);
    	row.addView(textVew);
		table.addView(row);
		
		TableLayout matetable = (TableLayout)findViewById(R.id.matetable);
		matetable.setStretchAllColumns(true);
		
		row1=new TableRow(this);
		textVew1=new TextView(this);
		textVew1.setTextSize(15);
		textVew1.setText("���");
		textVew1.setGravity(Gravity.LEFT);
		textVew1.setHeight(rowHeight);
		textVew1.setPadding(35, 0, 10, 0);
		textVew1.setTextColor(Color.BLACK);
    	row1.addView(textVew1);
		
		textVew1=new TextView(this);
		textVew1.setTextSize(15);
		textVew1.setText("��������");
		textVew1.setGravity(Gravity.CENTER);
		textVew1.setHeight(rowHeight);
		textVew1.setPadding(35, 0, 10, 0);
		textVew1.setTextColor(Color.BLACK);
    	row1.addView(textVew1);
			
		textVew1=new TextView(this);
		textVew1.setTextSize(15);
		textVew1.setText("����");
		textVew1.setGravity(Gravity.RIGHT);
		textVew1.setHeight(rowHeight);
		textVew1.setPadding(35, 0, 10, 0);
		textVew1.setTextColor(Color.BLACK);
    	row1.addView(textVew1);
		matetable.addView(row1);
		
		int size=mateList.size();
		for(int i=0;i<size;i++){
			String b[]=mateList.get(i);
			row=new TableRow(this);
			LinearLayout ll=new LinearLayout(this);
			//right|center_vertical
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
			CheckBox checkbox=new CheckBox(this);
			checkbox.setChecked(false);
			checkbox.setId(Integer.parseInt(b[0]));
			checkbox.setPadding(35, 5, 10, 0);
			checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				//@Override
				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						selMateNoList.add(String.valueOf(buttonView.getId()));
						
					}else{
						selMateNoList.remove(String.valueOf(buttonView.getId()));
					}
				}	
			});
			ll.addView(checkbox);
			
			row.addView(ll);
			
    		textVew=new TextView(this);
    		textVew.setTextSize(textSize);
    		textVew.setText(b[1]);
    		textVew.setGravity(Gravity.CENTER);
    		textVew.setHeight(rowHeight);
    		textVew.setPadding(35, 5, 10, 0);
    		textVew.setTextColor(Color.BLACK);
        	row.addView(textVew);
    	
    		EditText edit=new EditText(this);
    		edit.setText("1");
    		edit.setId(Integer.parseInt(b[0])+1000);
    		edit.setKeyListener(new DigitsKeyListener());
    		edit.setGravity(Gravity.RIGHT);
    		edit.setHeight(rowHeight);
    		edit.setPadding(35, 5, 10, 0);
    		edit.setOnKeyListener(new View.OnKeyListener() { 
    		  //  @Override
    		    public boolean onKey(View v, int keyCode, KeyEvent event) { 
    		    	
    		        return false; 
    		    } 
    		});
    		row.addView(edit);
    		matetable.addView(row);
		}
    }
    
    private List<String[]> findMateInfo(){
    	List<String[]> list=new ArrayList<String[]>();
    	for(int i=0;i<selMateNoList.size();i++){
    		int id=Integer.parseInt(selMateNoList.get(i))+1000;
    		EditText edit=(EditText)findViewById(id);
    		String b[]=new String[2];
    		b[0]=selMateNoList.get(i);
    		b[1]=edit.getText().toString();
    		list.add(b);
    	}
    	
    	
    	return list;
    }

    private boolean doIcCardCfm(String title){
    	boolean flag=false;
    	try{
    		List<String []> mlist=findMateInfo();
			String mate="";
			for(String b[]:mlist){
				
				mate+=b[0]+"_"+b[1]+",";
			}
			if(!mate.equals("")){
				mate=mate.substring(0,mate.length()-1);
			}
				
    		String url=HttpUtil.BASE_URL;
			Map<String ,String> rawParams=new HashMap<String,String>();
			rawParams.put("appid", String.valueOf(appid));
			rawParams.put("oper", "12");
			rawParams.put("mate", mate);
			rawParams.put("title", title);
			rawParams.put("wdType",String.valueOf( wdType));
			rawParams.put("confirmType",String.valueOf( confirmType));
			
			String result=HttpUtil.postRequest(url,rawParams);
			JSONArray jsonArray=new JSONArray(result);
			JSONObject obj=(JSONObject)jsonArray.get(0);
			errorCode=(Integer)obj.get("errorCode");
			if(errorCode==0)
			flag=true;
    	}catch(Exception e){
    		errorCode=-1;
    	}
    	return flag;
    }
    
    
 
    
	private boolean findWdInfoBy(String cardId,String title) {
		boolean flag=false;
		try{
			String url=HttpUtil.BASE_URL;
			Map<String ,String> rawParams=new HashMap<String,String>();
			if(title==null||title.equals("")){//IC��ȷ�Ϸ�ʽ
				rawParams.put("cardId", cardId);
				rawParams.put("oper", "10");
			}else{
				rawParams.put("title", title);
				rawParams.put("oper", "13");
				confirmType=3;
			}
			//rawParams.put("date", datefmt.format(new Date()));
			String result=HttpUtil.postRequest(url,rawParams);//192.168.0.108:8080/cr
			if(result==null){
				errorCode=-4;
			}else{
			Log.d("result", result);
			JSONArray jsonArray=new JSONArray(result);
			
			int len=jsonArray.length();
			for(int j=0;j<len;j++){
				JSONObject obj=(JSONObject)jsonArray.get(j);
				
				//wdType=(Integer)obj.get("wdType");
				errorCode=(Integer)obj.get("errorCode");
  				if(errorCode<0){
  					return flag;
  				}	
				JSONArray  json=(JSONArray)obj.get("wdinfo");
				int size=json.length();
				for(int i=0;i<size;i++){
  					JSONObject o=(JSONObject)json.get(i);
  					 b[0]=(String)o.get("Title");
  					 this.title=b[0];
  					 b[1]=o.get("PPhone")==null?"":(String)o.get("PPhone");
  					 b[2]=(String)o.get("CZ_Name");
  					 b[3]=(String)o.get("PAddress");
  					 b[4]=(String)o.get("Status");
  				}
				
				JSONArray jkinfo=(JSONArray)obj.get("jkinfo");
				Log.d("jsonarry", jkinfo+"");
				int size1=jkinfo.length();
				for(int i=0;i<size1;i++){
					JSONObject o=(JSONObject)jkinfo.get(i);
					appid=o.getInt("appid");
					String b[]=new String[3];
  					 b[0]=(String)o.get("game_Name");
  					 b[1]=String.valueOf((Integer)o.get("Qty"));
  					 b[2]=o.get("Price").toString();
  					jkLotList.add(b);
				}
				
				JSONArray  mateinfo=(JSONArray)obj.get("mateinfo");
				int size2=mateinfo.length();
				for(int i=0;i<size2;i++){
					JSONObject o=(JSONObject)mateinfo.get(i);
					String b[]=new String[3];
					 b[0]=String.valueOf((Integer)o.get("id"));
  					 b[1]=(String)o.get("name");
  					 b[2]=(String)o.get("unit");
  					mateList.add(b);
				}
				
			}
			flag= true;
			}
		}catch (ClientProtocolException e) {
			errorCode=-1;
		} catch (IOException e) {
			errorCode=-1;
		}catch(Exception e){
			System.out.println("error"+e.getMessage());
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
	     finish();
	}
	
	 @Override	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  //���¼����Ϸ��ذ�ť //
	  if(keyCode == KeyEvent.KEYCODE_BACK){ 
		  //goToMainActivity("");
		  finish();
		  return true;
	 
	  }else{  
	   return super.onKeyDown(keyCode, event);	 
	  }	 
	 }
	 
	//��ѯ����
	public void goWdInfoActivity(String title) {
		 Intent intent = new Intent();
		 intent.putExtra("title", title);
		 intent.setAction("android.intent.action.VIEWWDINFO");
	     startActivity(intent);
	}
}