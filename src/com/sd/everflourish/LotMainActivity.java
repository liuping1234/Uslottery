package com.sd.everflourish;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sd.everflourish.client.bound.LotPhoneNumBound;
import com.sd.everflourish.client.bound.NfcCheck;
import com.cr.uslotter.R;
import com.uslotter.util.Config;
import com.uslotter.util.HttpUtil;

public class LotMainActivity extends BaseSdActivity {
	WelcomeView wv;//进入欢迎界面
	String num="";
	String ID;//SIM卡号
	String title;
	String value;
	public static SharedPreferences setting;
	final static String LOTTERY_CATEGORY="com.everflourish.lot.intent.category.LOTTERY_CATEGORY";
	private String[] names = new String[]{"活动抽奖","查看包号","抽奖历史记录","中奖记录","设置"};
	private int[] imageIds = new int[]{ R.drawable.act,R.drawable.spag,R.drawable.history,R.drawable.qq,R.drawable.setting1};
	
	ProgressDialog pBar =null;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	SimpleDateFormat sdf;
	
	public static List<Activity> activityList = new ArrayList<Activity>();
	@Override
	public void onCreate(final Bundle savedInstanceState){	
		  super.onCreate(savedInstanceState);
		  activityList.add(this);
		  
	        //设置为全屏
	        requestWindowFeature(Window.FEATURE_NO_TITLE); 
			//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,   WindowManager.LayoutParams.FLAG_FULLSCREEN);
			//设置横屏模式
			//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);	
	        //暂时屏蔽    
	    String view=getIntent().getStringExtra("view");
	    if(view==null){
	    	  this.hd.sendEmptyMessage(1);//发送消息进入欢迎界面
	    }else{
	    	 this.hd.sendEmptyMessage(1);//发送消息进入欢迎界面
	    }
	    
	    //判断是否首次运行
	    setting = getSharedPreferences("CitiGame", MODE_WORLD_WRITEABLE);
	    final String url="http://www3.lotterygd.cn/androidService.jsp";
	    
	    title=getIntent().getStringExtra("cTitle");
        Boolean user_first = setting.getBoolean("FIRST",true);
        if(user_first){//第一次		
        	setting.edit().putBoolean("FIRST", false).commit();
        	setting.edit().putString("title", title).commit();
        	//1.通过SIM卡获取手机号码,记录当前操作人员的电话号码
	        num= LotPhoneNumBound.getPhoneNum_SIM(LotMainActivity.this);
	        	if(num!=""){
	        		Map<String,String> maps = new HashMap<String, String>();
	        		maps.put("phonenum", num);//号码
	        		maps.put("title", title);//网店编号
	        		maps.put("oper", "21");
	        		try {
						HttpUtil.postRequest(url, maps);
	        		} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					setting.edit().putString("NUM", num).commit();
	        }else{
	        		//2.通过读取SIM卡的ID号来和手机号码进行绑定
	        		ID=LotPhoneNumBound.getPhoneID(LotMainActivity.this);
	        		//onCreateDialog(ID).show();
	        		final EditText input = new EditText(this);
	        		Builder dialog=new AlertDialog.Builder(this);   
	                dialog.setTitle("请输入本人手机号");   
	                dialog.setView(input);  
	                dialog.setPositiveButton("确定",    
	                        new DialogInterface.OnClickListener() {   
	   
	                            public void onClick(DialogInterface dialog,   
	                                    int which) { 
	                            	value = input.getText().toString();
	                            	if(value.equals("")){
	                            		Toast.makeText(LotMainActivity.this, "输入有误", Toast.LENGTH_SHORT).show();
	                            		Editor editor = setting.edit();  
	            	        	        editor.remove("FIRST");
	            	        	        editor.remove("title");
	            	        	        editor.commit();
	            	        	        Editor editor1=WelComeSdActivity.setting1.edit();
	            	        	        editor1.remove("FIRST");
	            	        	        editor1.remove("title");
	            	        	        editor1.commit();
	                            		Intent intent =new Intent();
	                                    intent.setClass(LotMainActivity.this, NfcCheck.class);
	                                    startActivity(intent);
	                            		return;
	                            	}else{
		                            	Map<String,String> maps = new HashMap<String, String>();
		            	        		maps.put("phonenum", value);
		            	        		maps.put("simid",ID);
		            	        		maps.put("oper", "20");
		                            	try {
											String result=HttpUtil.postRequest(url, maps);
		                            	
		                            } catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
		                            	setting.edit().putString("ID",ID).commit();
										setting.edit().putString("title", title).commit();
										setting.edit().putString("NUM", value).commit();
										goToMainMenu();
		                            }
	                            }
	                        });
	                dialog.setNegativeButton("取消", new OnClickListener() {
	        			@Override
	        			public void onClick(DialogInterface dialog, int which) {
	        				Editor editor = setting.edit();  
    	        	        editor.remove("FIRST");
    	        	        editor.remove("title");
    	        	        editor.commit();  
    	        	        Editor editor1=WelComeSdActivity.setting1.edit();
    	        	        editor1.remove("FIRST");
    	        	        editor1.remove("title");
    	        	        editor1.commit();
    	        	        Intent intent=new Intent();
    	        	        intent.setClass(LotMainActivity.this, NfcCheck.class);
    	        	        startActivity(intent);
	        			}
	                });
	                dialog.create().show();
	        	}
        }else{
        	Boolean is_post=WelComeSdActivity.setting3.getBoolean("FIRST",true);
        	if(is_post){
        		WelComeSdActivity.setting3.edit().putBoolean("FIRST", false).commit();
	        	sdf=new SimpleDateFormat("yyyy年MM月dd日"+"HH:mm:ss");
	        	Map<String,String> maps = new HashMap<String, String>();
	        	String wdtitle=setting.getString("title", "");
	        	String phone=setting.getString("NUM", "");
	        	String datenow=sdf.format(new Date());
	        	//Toast.makeText(this, datenow, Toast.LENGTH_LONG).show();
	        	maps.put("date", datenow);
	        	maps.put("title", wdtitle);
	        	maps.put("phoneNo", phone);
	        	maps.put("oper", "22");
	        	/*try {
					HttpUtil.postRequest(url, maps);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
	        }
        }
	    
//	    new Thread(){//检查更新
//			public void run(){
//				  checkUpdate();
//			}
//		}.start();
	    
	    //初始化系统配置信息
		//initConfig();
		//启动消息推送服务
		//String t=lotApp.getTitle();
		//Log.e("main", t);
	}
	
	//声明消息处理器{
	Handler hd=new Handler(){
			@Override
			public void handleMessage(Message msg){
        		switch(msg.what){
	        		case 0://进入欢迎界面
	        			goToWelcomeView();
	        		break;
	        		case 1://进入菜单界面
	        			goToMainMenu();           			
	        		break;
        		}
        	}
	};
	
	/**
	 * 初始化系统配置信息
	 */
	private void initConfig(){
		// 获取只能被本应用程序读、写的SharedPreferences对象
		//preferences = getSharedPreferences("lotteryConfig", MODE_WORLD_READABLE);
		//editor = preferences.edit();
		//boolean kj=preferences.getBoolean("kjnotify", false);
		//editor.putBoolean("kjnotify", kj);
	
		//title=preferences.getString("title", "");
		//editor.putString("title", title);
		//editor.commit();
	}
		
	@Override
	protected void onResume() {
		super.onResume();
	}
	public void goToWelcomeView(){
		//如果该对象没创建则创建
    	if(wv==null){
    		wv=new WelcomeView(this);
    	}
    	setContentView(wv);
//    	setContentView(R.layout.welcome);
//    	
//    	   try {
//               Thread.sleep(3500);
//               hd.sendEmptyMessage(1);
//              
//       } catch (Exception e) {
//               e.printStackTrace();
//       }
	}
	
	//去主菜单
	public void goToMainMenu(){
		setContentView(R.layout.mainsd);
		String text=setting.getString("TITLE", "");
		TextView tx=(TextView) findViewById(R.id.header);
		TextView tx1=(TextView) findViewById(R.id.title);
		TextView tx2=(TextView) findViewById(R.id.phoneNum);
		tx.setText(text);
		WelComeSdActivity.setting1.edit().putString("Title", tx.getText().toString()).commit();
		String phonenum="本机号码："+setting.getString("NUM", "");
		String wdtitle=setting.getString("title", "");
		tx1.setText("顺德体彩网点："+wdtitle);
		tx2.setText(phonenum);
	   
		//创建一个List集合，List集合的元素是Map
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < names.length; i++){
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("logo", imageIds[i]);
			listItem.put("name", names[i]);
			
			listItems.add(listItem);
		}
		//创建一个SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this
			, listItems 
			//使用/layout/cell.xml文件作为界面布局
			, R.layout.cell
			, new String[]{ "name", "logo" }
			, new int[]{R.id.name , R.id.logo});
		GridView grid = (GridView)findViewById(R.id.grid01);
		//为ListView设置Adapter
		grid.setAdapter(simpleAdapter);
		grid.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent
				, View view, int position, long id){
				switch(position){
				case 0:  	
					goToDrawScanActivity("android.intent.action.DRAWSCAN");//抽奖
					break;
				case 1:
					//goToLotScanActivity("android.intent.action.LOTSCAN") ;//扫描包号
					goToDrawScanActivity("android.intent.action.LOOKBAG") ;
        			break;
				case 2:
					goToLookActivity("android.intent.action.LOOKHISTORYNUM") ;//历史抽奖记录
					break;
				case 3:
					goToLookActivity("android.intent.action.FINDLOTWIN");//中奖记录
					break;
				case 4:
					goToLookActivity("android.intent.action.SETTING");//设置
        			break;
				}
			}
		});
	}
	
	
	/**
	 * 启动线程检查是否有新版本
	 * 
	 */
	private void checkUpdate(){
		if(HttpUtil.checkNet(this)){
			if(Config.hasNewVersion(this)){
				  Intent intent = new Intent();
				  intent.putExtra("newVerName", Config.newVerName);
				  intent.putExtra("updateDesc", Config.updateDesc);
				  intent.setAction("android.intent.action.NFCUPDATEAPK");
				  //intent.addCategory(LotteryCheckMainActivity.UDATE_CATEGORY);
			      startActivity(intent);
			}
		}     
	}
	
	private void goToLotCheckActivity(String action){
		 Intent intent = new Intent();
		 intent.setAction(action);
	     startActivity(intent);
	}
	private void goToLookActivity(String action){
			 Intent intent = new Intent();
			 intent.setAction(action);
		     startActivity(intent);
	}
	private void goToDrawScanActivity(String action){
			 Intent intent = new Intent();
			 intent.setAction(action);
		     startActivity(intent);
	}
	/**
	 * 兑奖
	 * @param action
	 */
	private void goToLotPrizeActivity(String action){
		if(isLogin()){
			 Intent intent = new Intent();
			 intent.setAction(action);
		     startActivity(intent);
		}else{
			goToLoginActivity(action);
		}
		
	}
	
	private boolean isLogin(){
		if("".equals(title)){
			return false;
		}
		return true;
	}
	
	private void goToLoginActivity(final String action){
		final  View loginDialog = getLayoutInflater().inflate(
				R.layout.login, null);
		new AlertDialog.Builder(this)
		.setTitle("登录系统")
		.setView(loginDialog)
		.setPositiveButton("登录",
			new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog,
					int which){
					final String title=	((EditText)loginDialog.findViewById(R.id.title)).getText().toString();;
					final String pwd = ((EditText) loginDialog.findViewById(R.id.pass)).getText().toString();
					if(title.equals("")){
						Toast.makeText(LotMainActivity.this, "请输入网点编号！", Toast.LENGTH_LONG).show();
						return;
					}
					if(pwd.equals("")){
						Toast.makeText(LotMainActivity.this, "密码不能为空！", Toast.LENGTH_LONG).show();
						return;
					}
					try{
						final Handler handler = new Handler(){
							@Override
							public void handleMessage(Message msg){
								if(msg.what==1){
									Intent intent = new Intent();
									intent.setAction(action);
									startActivity(intent);
							   }else{
								   if(errorCode==-1){//
									   Toast.makeText(LotMainActivity.this, "网点编号不存在！", Toast.LENGTH_LONG).show();
								   }else  if(errorCode==-2) {
									   Toast.makeText(LotMainActivity.this, "登录时，发生其它异常！", Toast.LENGTH_LONG).show();
								   }else  if(errorCode==-3) {
									   Toast.makeText(LotMainActivity.this, "网点已经禁用！", Toast.LENGTH_LONG).show();  
								   }else  if(errorCode==-4) {
									   Toast.makeText(LotMainActivity.this, "网络连接有误！", Toast.LENGTH_LONG).show();
								   }
								 
							   }
							};	
						};
						showProgressDialog(LotMainActivity.this,"提示信息","正在登录中，请稍后...",ProgressDialog.STYLE_SPINNER);
						new Thread(){
							public void run(){
								errorCode=login( title, pwd);
								Message m=new Message();
								if(errorCode==0){
									lotApp.setTitle(title);
									m.what=1;
								}else{
									m.what=0;
								}
								pBar.cancel();
								handler.sendMessage(m);
							}
						}.start();
						
					}catch (Exception e){
						e.printStackTrace();
					}
				}
			}).setNegativeButton("取消", null).show();
	}
	
	private int login(String title,String pwd){
		int errorCode=0;
		String url=HttpUtil.BASE_URL;
		Map<String ,String> rawParams=new HashMap<String,String>();
		rawParams.put("oper", "16");
		rawParams.put("title", title);
		rawParams.put("pwd", pwd);
		try {
			JSONArray jsonArray=new JSONArray(HttpUtil.postRequest(url,rawParams));		
			for(int i=0;i<jsonArray.length();i++){
				JSONObject o=(JSONObject)jsonArray.get(i);
				errorCode=(Integer)o.get("errorCode");
				String cid=(String)o.get("cid");
				lotApp.setCid(cid);
				
			}
		}catch (Exception e) {
			Log.e("login", e.getMessage());
			errorCode=-4;
		}
		return errorCode;
	}
	//显示等待对话框
	private void showProgressDialog(Context ctx,String title, String msg,int style){
		pBar = new ProgressDialog(ctx);
		pBar.setTitle(title);
		pBar.setMessage(msg);
		pBar.setProgressStyle(style);
		pBar.show();
	}
	int errorCode=0;
}
