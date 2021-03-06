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
    int errorCode=0;//错误判断
    String [] b=new String[17];
    Map<String,String> ptypeM=new HashMap<String,String>();
    Map<String,String> stypeM=new HashMap<String,String>();
    Map<String,String> regM=new HashMap<String,String>();
    Map<String,String> proM=new HashMap<String,String>();
    Map<String,String> jurM=new HashMap<String,String>();
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);    
            title=getIntent().getStringExtra("title");
        	showProgressDialog(this,"网点信息","获取数据中，请稍后...",ProgressDialog.STYLE_SPINNER);
        	init();
      		final Handler handler = new Handler(){
    			@Override
    			public void handleMessage(Message msg){
    				if(msg.what==1){
    					show();	
    			   }else{
    				   if(errorCode==-2){
    					  // showDialog("网点信息不存在!");
    					   //Toast.makeText(WdInfoActivity.this, "", Toast.LENGTH_SHORT).show();
    					   goToMainActivity("网点信息不存在!");
    				   }else if(errorCode==-1){	
    					   goToMainActivity("网络连接失败!");
    					  // showDialog("网络连接失败!");
    					  // Toast.makeText(WdInfoActivity.this, "网络连接失败!", Toast.LENGTH_SHORT).show();
    				   }else if(errorCode==-3){	
    					   goToMainActivity("现实网点信息，发生异常!");
    					  // showDialog("现实网点信息，发生异常!");
    					   //Toast.makeText(WdInfoActivity.this, "现实网点信息，发生异常!", Toast.LENGTH_SHORT).show();
    				   }
    			   }
    			};
    			
    		};

    		//启动线程来连接网络并读取开奖号码
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
    	title.setText("网点编号: "+b[0]);
    	TextView phone = (TextView)findViewById(R.id.phone);
    	phone.setText("网点电话: "+b[1]);
    	
    	TextView status = (TextView)findViewById(R.id.status);
    	status.setText("网点状态: "+b[16]);
    	
    	TextView cz = (TextView)findViewById(R.id.cz);
    	cz.setText("行政区域: "+b[2]);
    	TextView address = (TextView)findViewById(R.id.address);
    	address.setText("网点地址: "+b[3]);
    	address.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				goWdMapActivity(b[3]);
				
				
			}
			
		});
    	
    	TextView oinfo = (TextView)findViewById(R.id.oinfo);
    	oinfo.setText("姓名: "+b[12]+"\n电话: "+b[13]+"\n手机: "+b[14]+"\n地址: "+b[15]);
    	
    	TextView wdinfo = (TextView)findViewById(R.id.wdinfo);
    	wdinfo.setText("网点类型: "+b[4]+"\n经营性质: "+b[5]+"\n所属地段: "+b[6]+"\n网点性质: "+b[7]+"\n所属管辖: "+b[8]);
    	wdinfo.setText(wdinfo.getText().toString()+"\n网点面积: "+b[9]+"\n建站时间: "+b[10]+"\n高频: "+b[11]);
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
  					b[5]=stypeM.get(String.valueOf((Integer)o.get("STypeId")));//经营性质
  					b[6]=regM.get(String.valueOf((Integer)o.get("RegionId")));//所属地段
  					b[7]=proM.get(String.valueOf((Integer)o.get("ProId")));//网点性质
  					b[8]=jurM.get(String.valueOf((Integer)o.get("JurId")));//所属管辖
  					
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


  	//错误检查
	private int isCheckError(JSONArray jsonArray) throws JSONException{
		int len=jsonArray.length();
		int errorCode=0;//没有错误
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
	   	builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){		    	
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
