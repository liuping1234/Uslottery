package com.sd.everflourish;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.sd.everflourish.client.bound.NfcCheck;
import com.sd.everflourish.db.SqliteHandle;
import com.cr.uslotter.R;
import com.uslotter.util.HttpUtil;

public class WelComeSdActivity extends BaseSdActivity {
	public static SharedPreferences setting1;
	public static SharedPreferences setting3;
	SqliteHandle sqliteHandle=null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        sqliteHandle = new SqliteHandle(this);
        Start();
    }
	
    public void Start() {
                new Thread() {
                        public void run() {
                        	setting3 = getSharedPreferences("Confirm1", Context.MODE_WORLD_WRITEABLE);
                        	setting1 = getSharedPreferences("Confirm", Context.MODE_WORLD_WRITEABLE);
                        	Boolean user_first=setting1.getBoolean("FIRST", true);
                        	String title=setting1.getString("title", null);
                        	try {
                                        Thread.sleep(2500);
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                }
                                if(user_first){
                                	 Intent intent =new Intent();
                                     intent.setClass(WelComeSdActivity.this, NfcCheck.class);
                                     startActivity(intent);
                                     finish();
                                }else{
                                	//上传数据
                                	if(HttpUtil.checkNet(WelComeSdActivity.this)){
                                		int first=setting1.getInt("head", 0);
                                    	String wdTitle=title;
                                    	String wdActivity=setting1.getString("Title", null);
                                    	List<Map<String,String>> list1=SqliteHandle.PostData1();
                                    	int last=list1.size();
                                    	setting1.edit().putInt("head", last+1).commit();
                                    	if(first<=last){
                                    		List<Map<String,String>> list2=SqliteHandle.PostData(wdTitle, wdActivity,first,last);
                                        	int size2=list2.size();
                                        	for(int i=0;i<size2;i++){
                                        		System.out.println("++++++++++++"+list2.get(i));
                                        		try {
                            						HttpUtil.postData("Http://www3.lotterygd.cn/SDcJdata.jsp", list2);
                            	        		} catch (Exception e) {
                            						// TODO Auto-generated catch block
                            						e.printStackTrace();
                            					}
                                        	}
                                    	}
                                	}
                                	//跳转
                                	Intent intent = new Intent();
                                	intent.putExtra("cTitle", title);
                                    intent.setClass(WelComeSdActivity.this, LotMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                        }
                }.start();
        }
}