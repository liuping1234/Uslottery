package com.sd.everflourish.lot.scan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sd.everflourish.BaseSdActivity;
import com.sd.everflourish.db.SqliteHandle;
//抽奖方案
public class DrawScanHandle extends BaseSdActivity{
	String sid="";
	int errorCode=0;
	String draw[]=new String[4];
	SqliteHandle sqliteHandle=null;
	ProgressDialog pBar =null;
	String Code=null;
	String Grade=null;
	String NCode=null;
	String NGrade=null;
	String Id1=null;
	int NgradeNo=0;
	int Count=0;
	int GNum=0;
	SimpleDateFormat sdf;
	public static SharedPreferences setting0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setting0 = getSharedPreferences("save", Context.MODE_WORLD_WRITEABLE);
		sid=getIntent().getStringExtra("sid");
		sqliteHandle = new SqliteHandle(this);
		//showProgressDialog(this,"提示信息","正在进行抽奖，请稍后...",ProgressDialog.STYLE_SPINNER);
		super.onCreate(savedInstanceState);
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what==1){
						showDialog("恭喜您，中奖啦！\n"+Code+"\n"+Grade+"\n"+"是否马上兑奖？");
			   }else{
				   if(errorCode==1){
					   showTipMsg("提示：未中奖，继续努力！");
				   }else if(errorCode==-1){//
					   showTipMsg("提示：彩票不在本次抽奖范围！");
				   }else  if(errorCode==-2) {
					   showTipMsg("提示：彩票已经抽奖！");
				   }else  if(errorCode==-3) {
					   showTipMsg("错误：扫描异常，请重新扫描！");
				   }else  if(errorCode==-5) {
					   showTipMsg("错误：请扫描彩票的有效二维码区域！");
				   }else  if(errorCode==-6) {
					   showTipMsg("提示：本次抽奖活动已经结束！");
				   }else if(errorCode==-7){
					   showDialog("您还未兑奖！\n"+NCode+"\n"+NGrade+"\n"+"是否马上兑奖？");
				   }
				 
			   }
			};	
		};
		
		new Thread(){
			public void run(){
				int flag=handle(title,sid);
				Message m=new Message();
				
				if(flag==0){
					m.what=1;
				}else{
					m.what=0;
				}
				/*try {
					sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				//pBar.cancel();
				//SqliteHandle.Close();
				handler.sendMessage(m);
			}

		}.start();
	
	}
	
	private void showProgressDialog(Context ctx,String title, String msg,int style){
		pBar = new ProgressDialog(ctx);
		pBar.setTitle(title);
		pBar.setMessage(msg);
		pBar.setProgressStyle(style); 
		pBar.show();
	}
	
	public void showTipMsg(final String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);	    
	   	builder.setMessage(message).setCancelable(false);
	   	builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){
		    	  @Override
				  public void onClick(DialogInterface dialog, int which){
				 		goToDigilinxActivity(message);
				  }
	   	});		
	   	builder.create().show(); 
		//goToDigilinxActivity(message);
	}
	
	//即开彩票验证
		public void goToDigilinxActivity(String message) {
			 Intent intent = new Intent();
			 intent.putExtra("message", message);
			 intent.setClass(this, DrawScanActivity.class);
		     startActivity(intent);
		     finish();
		}
		//从数据库中获取数据来判断是否可以参加抽奖
		private int handle(String title,String sid){
			 String code=sid.substring(0,2);//国家编码
			  	String playId=String.valueOf(sid.substring(2,6));//玩法
				String info=String.valueOf(sid.substring(6,13));//包号信息
				String bagNo=code+"-"+playId+"-"+info;
				String serialNo=String.valueOf(sid.substring(13,16));//张序列号
				String sidStr=code+"-"+playId+"-"+info+"-"+serialNo;
				String sql1="select * from CardNum where Sid=?;";
				String sql="select * from BagNum where BagNumber= ?;";
				String sql2="select * from cr_winNum where state = 0 and Sid = '"+sid+"';";
				int i=SqliteHandle.findBynum(sidStr, sql1);
				ContentValues values1 = new ContentValues();
				sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String datenow=sdf.format(new Date());
				values1.put("Date", datenow);
				values1.put("Sid", sidStr);
				try{
					List<Map<String,String>> list=SqliteHandle.queryForList(sql,bagNo);
					if(list.isEmpty()||list.size()==0){
						this.errorCode=-1;//不在抽奖的包号范围
					}else{
						if(i==0){//是否抽过奖
							SqliteHandle.Insert("CardNum", values1);
							synchronized(this){
								System.out.println("获取抽奖数据 : "+title);
									// try {
										//Thread.sleep(10*1000);
									//} catch (InterruptedException e) {
										//e.printStackTrace();
									//}
								
								List<String[]> drawGradeList=new ArrayList<String[]>();
								Log.d("aaaaaaaa", "123");
								sql="select * from cr_drawgrade;";
								list=SqliteHandle.findLotteryNum(sql);
								for(Map<String,String> map:list){
									String dg[]=new String[7];
									dg[0]=(String)map.get("gradeNo");
									dg[1]=(String)map.get("gradeName");
									dg[2]=(String)map.get("drawCount");
									dg[3] =(String)map.get("drawName");
									dg[4] =(String)map.get("drawNo");
									dg[5]=(String)map.get("outCount");
									dg[6]=(String)map.get("Count");
									drawGradeList.add(dg);
								}	
								
								System.out.println("开始抽奖 : "+title);	
								String  draw[]=startDrawBy(drawGradeList);//开始抽奖
								if(draw[0].equals("-1")){//本次抽奖活动已经结束抽奖（已经抽完了）
									this.errorCode=-6;
								}else{
									String status="0";
									String gradeNo="";
									String gradeName="";
									String drawName="";
									String MCount="";
									//String rand=draw[3];//本次抽出号码
									if(draw[0].equals("1")){//中奖了
										gradeNo=draw[1];
										gradeName=draw[2];
										drawName=draw[3];
										MCount=draw[4];
										sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										String datenow1=sdf.format(new Date());
										ContentValues values = new ContentValues();
										values.put("state", 0);
										values.put("gradeNo", gradeNo);
										values.put("gradeName", gradeName);
										values.put("drawName", drawName);
										values.put("Sid", sid);
										values.put("day", datenow1);
										//values.put("isPost",0);
										String pack="cr_winNum";
										SqliteHandle.Count(MCount, gradeName);
										SqliteHandle.Insert(pack, values);
										this.errorCode=0;
									}else{
										this.errorCode=1;
									}
									/*drawMap.put("gradeName",gradeName);
									drawMap.put("drawName",drawName);
									drawMap.put("sid", sid);*/
								}
							}
						}else{
							List<Map<String,String>> list1=SqliteHandle.FindIsWin(sql2);
							System.out.println("`hhhhh---"+list1.size());
							if(list1.size()!=0){
								for (Map<String, String> map : list1) {
									Id1=map.get("Sid");
									NCode=map.get("drawName");
									NGrade=map.get("gradeName");
									String grade=map.get("gradeNo");
									NgradeNo=Integer.parseInt(grade);
								}
								this.errorCode=-7;
							}
							else{
								this.errorCode=-2;//已经抽过奖了
							}
						}	
					}
				}catch(Exception e){
					this.errorCode=-3;
					System.out.println("saveDrawInfo: "+e.getMessage()+" "+sql);
				}
				
				return this.errorCode;
		 }
		
		 public  String[] startDrawBy(List<String[]> drawGradeList){
		  		String draw[]={"","","","",""};
		  		try{
		  			String SMaxNum=ShowSetting.Showsetting.getString("maxNum", "");
			  		setting0.edit().putString("num", SMaxNum).commit();
		  		}catch(Exception e){
		  			System.out.println("startDrawBy: "+e.getMessage());
		  		}
		  		
				try
				{
					//Context setting=createPackageContext("com.everflourish.lot.scan.ShowSetting", Context.CONTEXT_IGNORE_SECURITY);
					//SharedPreferences prefs=setting.getSharedPreferences("ShowSetting", Context.MODE_WORLD_WRITEABLE);
					String Max=setting0.getString("num", null);
					int MaxNum=Integer.parseInt(Max);
					int rand=(int)(Math.random()*MaxNum);
					Log.d("1111111111111", rand+"");
					for(String dg[]:drawGradeList){
						int gradeNo=Integer.parseInt(dg[0]);
						String gradeName=dg[1];
						int drawCount=Integer.parseInt(dg[2]);
						String drawName =dg[3];
						String drawNo =dg[4];
						int outCount=Integer.parseInt(dg[5]);
						int SumCount=Integer.parseInt(dg[6]);
						String[] b=drawNo.split(",");
						int s=Integer.parseInt(b[0]);
						int e=Integer.parseInt(b[1]);
						if(SumCount<drawCount){//还有奖品
							if(rand>=s&&rand<e){//中奖了
								//draw=new String[4];
								draw[0]="1";
								draw[1]=gradeNo+"";
								draw[2]=gradeName;
								draw[3]=drawName;
								draw[4]=SumCount+"";
								Count=outCount;
								GNum=gradeNo;
								Code=gradeName;
								Grade=drawName;
								break;
							}
						}
					}
					Log.d("rand", rand+"");
					//draw[3]=rand+"";
				}catch(Exception e){
					System.out.println("startDrawBy1: "+e.getMessage());
				}
				return draw;	
		  }
		 
		 
		 public void showDialog1(String message){
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				final AlertDialog.Builder builder1 = new AlertDialog.Builder(DrawScanHandle.this);
			   	builder.setMessage(message).setCancelable(false);
			   	builder.setPositiveButton("确定", new OnClickListener(){
				    	  @Override
						  public void onClick(DialogInterface dialog, int which){
				    		  	int isCast=SqliteHandle.upDateState(sid);
				    		  	if(isCast==0){
				    		  		builder1.setMessage("兑奖成功");
				    		  		builder1.setPositiveButton("确定", new OnClickListener(){

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											String sqll="select outCount from cr_drawgrade where id = "+NgradeNo;
											int count=SqliteHandle.RetCount(sqll);
											SqliteHandle.updateState1(Id1);
											SqliteHandle.upDataOutCount(count, NgradeNo);
											goToDigilinxActivity(null);
										}
				    		  		});
				    		  		builder1.create().show();
				    		  	}
						 		
						  }
			   	});
			   	builder.setNegativeButton("取消", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						goToDigilinxActivity(null);
					}
				});
			   	builder.create().show(); 
			}
		 
		 
		 
		 public void showDialog(String message){
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				final AlertDialog.Builder builder1 = new AlertDialog.Builder(DrawScanHandle.this);
			   	builder.setMessage(message).setCancelable(false);
			   	builder.setPositiveButton("确定", new OnClickListener(){
				    	  @Override
						  public void onClick(DialogInterface dialog, int which){
				    		  	int isCast=SqliteHandle.upDateState(sid);
				    		  	if(isCast==0){
				    		  		builder1.setMessage("兑奖成功");
				    		  		builder1.setPositiveButton("确定", new OnClickListener(){

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											//SqliteHandle.updateState2(sid);
											SqliteHandle.upDataOutCount(Count, GNum);
											
											goToDigilinxActivity(null);
										}
				    		  		});
				    		  		builder1.create().show();
				    		  	}
						 		
						  }
			   	});
			   	builder.setNegativeButton("取消", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						goToDigilinxActivity(null);
					}
			   	});
			   	builder.create().show(); 
		 }

			 public int Lottery(String num){
				 return 0;
			 }
		 
}
