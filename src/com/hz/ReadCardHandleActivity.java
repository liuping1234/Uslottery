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
    int errorCode = 0;//错误判断
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
    private boolean flags = false;//没有手按，自动返回
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            cardId=getIntent().getStringExtra("cardId");
            xcid=getIntent().getStringExtra("xcid");
           title = this.getIntent().getStringExtra("title");
           time = this.getIntent().getStringExtra("time");
           wd = this.getIntent().getStringExtra("wd");
           flag = this.getIntent().getStringExtra("flag");
//           if(cardId == null||cardId.equals("")){
//        	   Toast.makeText(this, "卡id为空!", Toast.LENGTH_SHORT).show();
//        	   return;
//           }
           if(xcid == null||xcid.equals("")){
        	 //  Toast.makeText(this, "活动id为空!", Toast.LENGTH_SHORT).show();
        	   return;
           }
           if(title == null||title.equals("")){
        	   Toast.makeText(this, "活动标题为空!", Toast.LENGTH_SHORT).show();
        	   return;
           }
           
           if(time == null||time.equals("")){
        	   Toast.makeText(this, "活动日期为空!", Toast.LENGTH_SHORT).show();
        	   return;
           }
//           if(wd == null||wd.equals("")){
//        	   Toast.makeText(this, "网点编号为空!", Toast.LENGTH_SHORT).show();
//        	   return;
//           }
           if(flag == null||flag.equals("")){
        	   Toast.makeText(this, "不能区分是刷卡还是手输!", Toast.LENGTH_SHORT).show();
        	   return;
           }
        	showProgressDialog(this,"网点信息","获取数据中，请稍后...",ProgressDialog.STYLE_SPINNER);
        	init();
      		final Handler handler = new Handler(){
    			@Override
    			public void handleMessage(Message msg){
    				if(msg.what==1){//表示errorCode == 1
    					if(b[17].equals("")||b[17]== null){
    						return;
    					}
    					int count = Integer.parseInt(b[17]);
    					
    					if(count == -1){
    							goToReadCardActivity("网点编号:"+wd+"\n签到超过限定次数!");    						 
    					}else if(count == 0){
    						
    						 if(flag.trim().endsWith("1")){//打卡
    							 goToReadCardActivity("打卡不在本次活动范围内");
      					 }else if(flag.trim().endsWith("2")){//手输
      						 goToReadCardActivity("网点编号不在本次活动范围内");
      					   }
    					}else {
    						show();	
    					}
    			   }else{
    				   if(errorCode==0){
    					   if(flag.trim().endsWith("1")){//打卡
    						   goToReadCardActivity("无效卡!");
    					   }else if(flag.trim().endsWith("2")){//手输
    						   goToReadCardActivity("无效网点编号:"+wd);
    					   }
     					  // showDialog("网点信息不存在!");
     					  //Toast.makeText(WdInfoActivity.this, "", Toast.LENGTH_SHORT).show();
     					   
     				   }else if(errorCode==-2){
     					  goToReadCardActivity("刷卡发生异常，请重试！");
     					 
    					  // showDialog("网点信息不存在!");
    					   //Toast.makeText(WdInfoActivity.this, "", Toast.LENGTH_SHORT).show();
    					   
    				   }else if(errorCode==-1){	
    					   goToReadCardActivity("网络连接失败!");
    					  // showDialog("网络连接失败!");
    					  // Toast.makeText(WdInfoActivity.this, "网络连接失败!", Toast.LENGTH_SHORT).show();
    				   }else if(errorCode==-3){	
    					   goToReadCardActivity("刷卡发生异常，请重试！");
    					  // showDialog("现实网点信息，发生异常!");
    					   //Toast.makeText(WdInfoActivity.this, "现实网点信息，发生异常!", Toast.LENGTH_SHORT).show();
    				   }
    			   }
    			};
    		};

    		//启动线程来连接网络并读取开奖号码
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
    	ptypeM.put("0", "混合");
    	ptypeM.put("1", "传统");
    	ptypeM.put("2", "单场");
    	
    	stypeM.put("0", "其它");
    	stypeM.put("1", "专营体彩");
    	stypeM.put("2", "兼营福彩");
    	stypeM.put("3", "兼营其他");
    	stypeM.put("4", "邮政报刊亭");
    	stypeM.put("5", "超市");
    	stypeM.put("6", "兼营福彩及其他");
    	stypeM.put("7", "专营体彩-附福彩");
    	
    	regM.put("0", "其它");
    	regM.put("1", "住宅区");
    	regM.put("2", "商业区");
    	regM.put("3", "工业区");
    	regM.put("4", "郊区");
    	
    	proM.put("0", "其它");
    	proM.put("1", "自有");
    	proM.put("2", "租赁");
    	
    	jurM.put("1", "市级");
    	jurM.put("2", "县级");
    	jurM.put("3", "镇级");
    	jurM.put("4", "村级");
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
    	title.setText("网点编号: "+b[0]);
//    	TextView phone = (TextView)findViewById(R.id.phone);
//    	phone.setText("网点电话: "+b[1]);
    	
//    	TextView status = (TextView)findViewById(R.id.status);
//    	status.setText("网点状态: "+b[16]);
    	
    	TextView cz = (TextView)findViewById(R.id.wdinfo_hz_cz);
    	cz.setText("行政区域: "+b[2]);
//    	TextView address = (TextView)findViewById(R.id.address);
//    	address.setText("网点地址: "+b[3]);
//    	address.setOnClickListener(new View.OnClickListener(){
//
//			public void onClick(View v) {
//				goWdMapActivity(b[3]);	
//			}
//			
//		});
    	
    	TextView jc = (TextView)findViewById(R.id.wdinfo_hz_jc);
    	if(b[19].trim().equals("")||b[19].trim().equals("未开通")){
    		jc.setTextColor(Color.BLACK);
    		jc.setText("竞彩: 未开通");
    	}else{
    		jc.setText("竞彩: "+b[19]);
    	}
    	

    	TextView gp = (TextView)findViewById(R.id.wdinfo_hz_gp);
    	if(b[20].trim().equals("")||b[20].trim().equals("未开通")){
    		gp.setTextColor(Color.BLACK);
    		gp.setText("高频: 未开通");
    	}else{
    		gp.setText("高频: "+b[20]);
    	}
    	

    	TextView oinfo = (TextView)findViewById(R.id.wdinfo_hz_name);
    	//oinfo.setText("姓名: "+b[12]+"\n电话: "+b[13]+"\n手机: "+b[14]+"\n地址: "+b[15]);
    	oinfo.setText("业主： "+b[12]);
    	
    	TextView t_count = (TextView)findViewById(R.id.wdinfo_hz_tcount);
    	t_count.setText("次数： "+b[17]);
    	
    	TextView t_prompt = (TextView)findViewById(R.id.wdinfo_hz_prompt);
    	t_prompt.setMovementMethod(ScrollingMovementMethod.getInstance());
    	ImageView _iv = (ImageView)findViewById(R.id.wdinfo_hz_iv_bottom);
    	if(!b[18].equals("")){
    		_iv.setVisibility(View.VISIBLE);
    		t_prompt.setVisibility(View.VISIBLE);
    		t_prompt.setText("提示："+b[18]);
    		
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
//    	wdinfo.setText("网点类型: "+b[4]+"\n经营性质: "+b[5]+"\n所属地段: "+b[6]+"\n网点性质: "+b[7]+"\n所属管辖: "+b[8]);
//    	wdinfo.setText(wdinfo.getText().toString()+"\n网点面积: "+b[9]+"\n建站时间: "+b[10]+"\n高频: "+b[11]);
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
  					 b[5]=stypeM.get(String.valueOf(o.get("STypeId")));//经营性质
  					 b[6]=regM.get(String.valueOf(o.get("RegionId")));//所属地段
  					 b[7]=proM.get(String.valueOf(o.get("ProId")));//网点性质
  					 b[8]=jurM.get(String.valueOf(o.get("JurId")));//所属管辖
  					
//  					b[4]=ptypeM.get(String.valueOf((Integer)o.get("PTypeId")));
//  					b[5]=stypeM.get(String.valueOf((Integer)o.get("STypeId")));//经营性质
//  					b[6]=regM.get(String.valueOf((Integer)o.get("RegionId")));//所属地段
//  					b[7]=proM.get(String.valueOf((Integer)o.get("ProId")));//网点性质
//  					b[8]=jurM.get(String.valueOf((Integer)o.get("JurId")));//所属管辖
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
  		
  		//显示等待对话框
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
  					//throw new Exception("图片文件不存在或路径错误，错误代码：" + responseCode);
  				return BitmapFactory.decodeStream(url.openStream());
  			} catch (Exception e) {
  				return BitmapFactory.decodeResource(getResources(), R.drawable.icon);
  			}
  		}

	// 错误检查
	private int isCheckError(JSONArray jsonArray) throws JSONException {
		int len = jsonArray.length();
		int errorCode = 1;// 没有错误
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
	   	builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){		    	
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
	  //按下键盘上返回按钮 //
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