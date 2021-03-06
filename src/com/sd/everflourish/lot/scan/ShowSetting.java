package com.sd.everflourish.lot.scan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sd.everflourish.LotMainActivity;
import com.sd.everflourish.WelComeSdActivity;
import com.sd.everflourish.db.SqliteHandle;
import com.cr.uslotter.R;

public class ShowSetting extends Activity {
	SqliteHandle sqliteHandle=null;
	int item=0;
	String selectedName;
	String [] num=new String[]{"1","2","3","4","5","6"};
	String gradenum;
	int sum;
	int numMax;
	int num1_int;
	int num2_int;
	int num3_int;
	int num4_int;
	int num5_int;
	int num6_int;
	int num7_int;
	int num8_int;
	int num9_int;
	int num10_int;
	EditText grade1;
	EditText grade2;
	EditText grade3;
	EditText grade4;
	EditText grade5;
	EditText grade6;
	EditText grade7;
	EditText grade8;
	EditText grade9;
	EditText grade10;
	
	EditText drawName1;
	EditText drawName2;
	EditText drawName3;
	EditText drawName4;
	EditText drawName5;
	EditText drawName6;
	EditText drawName7;
	EditText drawName8;
	EditText drawName9;
	EditText drawName10;
	EditText actText;
	EditText ticketNum;
	List<Map<String,String>> list=null;
	Button Clear=null;
	public static SharedPreferences Showsetting;
	public static SharedPreferences.Editor edit;
	//private ArrayAdapter<String> adapter; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sqliteHandle = new SqliteHandle(this);
		Showsetting = getSharedPreferences("ShowSetting", Context.MODE_WORLD_WRITEABLE);
		edit=Showsetting.edit();
		Boolean isFirst=Showsetting.getBoolean("FIRST",true);
		setContentView(R.layout.setting);
		Button subm=(Button) findViewById(R.id.sub);
		actText=(EditText) findViewById(R.id.ActText);
		 ticketNum=(EditText) findViewById(R.id.TicketNum);
		 grade1=(EditText) findViewById(R.id.Grade1);
		 grade2=(EditText) findViewById(R.id.Grade2);
		 grade3=(EditText) findViewById(R.id.Grade3);
		 grade4=(EditText) findViewById(R.id.Grade4);
		 grade5=(EditText) findViewById(R.id.Grade5);
		 grade6=(EditText) findViewById(R.id.Grade6);
		 grade7=(EditText) findViewById(R.id.Grade7);
		 grade8=(EditText) findViewById(R.id.Grade8);
		 grade9=(EditText) findViewById(R.id.Grade9);
		 grade10=(EditText) findViewById(R.id.Grade10);
		
		 drawName1=(EditText) findViewById(R.id.DrawName1);
		 drawName2=(EditText) findViewById(R.id.DrawName2);
		 drawName3=(EditText) findViewById(R.id.DrawName3);
		 drawName4=(EditText) findViewById(R.id.DrawName4);
		 drawName5=(EditText) findViewById(R.id.DrawName5);
		 drawName6=(EditText) findViewById(R.id.DrawName6);
		 drawName7=(EditText) findViewById(R.id.DrawName7);
		 drawName8=(EditText) findViewById(R.id.DrawName8);
		 drawName9=(EditText) findViewById(R.id.DrawName9);
		 drawName10=(EditText) findViewById(R.id.DrawName10);
		 Clear=(Button) findViewById(R.id.clear);
		 Clear.setVisibility(View.GONE);
		 Clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder=new Builder(ShowSetting.this);
				builder.setTitle("警告！");
				builder.setMessage("清空后删除所有数据");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {   
	             	   
                    public void onClick(DialogInterface dialog,   
                            int which) { 
                    	actText.setText("");
               		 	ticketNum.setText("");
                    	grade1.setText("");grade2.setText("");
                		grade3.setText("");grade4.setText("");
                		grade5.setText("");grade6.setText("");
                		grade7.setText("");grade8.setText("");
                		grade9.setText("");grade10.setText("");
                		drawName1.setText("");drawName2.setText("");
                		drawName3.setText("");drawName4.setText("");
                		drawName5.setText("");drawName6.setText("");
                		drawName7.setText("");drawName8.setText("");
                		drawName9.setText("");drawName10.setText("");
                		LotMainActivity.setting.edit().putString("TITLE", "").commit();
                		WelComeSdActivity.setting1.edit().putInt("head", 0).commit();
                		edit.clear();
                		edit.commit();
                		SqliteHandle.deleteTable();
                    }
			});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});
		 ticketNum.clearFocus(); //清除焦点 
		 Spinner grade=(Spinner) findViewById(R.id.Grade);
		 
		 if(!isFirst){
			 Clear.setVisibility(View.VISIBLE);
			 String num=Showsetting.getString("maxNum", null);
			 String num1=Showsetting.getString("SpinnerNum", null);
			 int position=Integer.parseInt(num1);
			 actText.setText(Showsetting.getString("title", null));
			 ticketNum.setText(num);
			 grade.setSelection(position);
			 
			 showItem(num1);
			 	grade1.setText(Showsetting.getString("grade1", null));
				grade2.setText(Showsetting.getString("grade2", null));
				grade3.setText(Showsetting.getString("grade3", null));
				grade4.setText(Showsetting.getString("grade4", null));
				grade5.setText(Showsetting.getString("grade5", null));
				grade6.setText(Showsetting.getString("grade6", null));
				grade7.setText(Showsetting.getString("grade7", null));
				grade8.setText(Showsetting.getString("grade8", null));
				grade9.setText(Showsetting.getString("grade9", null));
				grade10.setText(Showsetting.getString("grade10", null));
				
				drawName1.setText(Showsetting.getString("drawname1", null));
				drawName2.setText(Showsetting.getString("drawname2", null));
				drawName3.setText(Showsetting.getString("drawname3", null));
				drawName4.setText(Showsetting.getString("drawname4", null));
				drawName5.setText(Showsetting.getString("drawname5", null));
				drawName6.setText(Showsetting.getString("drawname6", null));
				drawName7.setText(Showsetting.getString("drawname7", null));
				drawName8.setText(Showsetting.getString("drawname8", null));
				drawName9.setText(Showsetting.getString("drawname9", null));
				drawName10.setText(Showsetting.getString("drawname10", null));
		 }
		 	
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,num);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
		grade.setAdapter(adapter); 
		//grade.setSelection(item);
		grade.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){           
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{                  
				item=position;
				selectedName = parent.getItemAtPosition(position).toString();
				gradenum=parent.getItemAtPosition(position).toString();                       
			    showItem(gradenum);
			}        
			public void onNothingSelected(AdapterView<?> parent){    
				
			}     
			});   
		subm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String title=actText.getText().toString();//活动主题
				final String MaxNum=ticketNum.getText().toString();//彩票基数
				final String grade01=grade1.getText().toString();
				final String grade02=grade2.getText().toString();
				final String grade03=grade3.getText().toString();
				final String grade04=grade4.getText().toString();
				final String grade05=grade5.getText().toString();
				final String grade06=grade6.getText().toString();
				final String grade07=grade7.getText().toString();
				final String grade08=grade8.getText().toString();
				final String grade09=grade9.getText().toString();
				final String grade010=grade10.getText().toString();
				final String drawName01=drawName1.getText().toString();
				final String drawName02=drawName2.getText().toString();
				final String drawName03=drawName3.getText().toString();
				final String drawName04=drawName4.getText().toString();
				final String drawName05=drawName5.getText().toString();
				final String drawName06=drawName6.getText().toString();
				final String drawName07=drawName7.getText().toString();
				final String drawName08=drawName8.getText().toString();
				final String drawName09=drawName9.getText().toString();
				final String drawName010=drawName10.getText().toString();
					
				if(title.equals("")||MaxNum.equals("")){
					Toast.makeText(ShowSetting.this, "错误：活动名称和奖组票数不能为空！", Toast.LENGTH_LONG).show();
				}
				else{
					
					numMax=Integer.parseInt(ticketNum.getText().toString());//彩票基数
					switch (item) {
					case 0:
						num1_int=Integer.parseInt(grade1.getText().toString());
						sum=num1_int;
						break;
					
					case 1:
						num1_int=Integer.parseInt(grade1.getText().toString());
						sum=num1_int;
						break;
						
					case 2:
						num1_int=Integer.parseInt(grade1.getText().toString());
						num2_int=Integer.parseInt(grade2.getText().toString());
						sum=num1_int+num2_int;
						break;
						
					case 3:
						num1_int=Integer.parseInt(grade1.getText().toString());
						num2_int=Integer.parseInt(grade2.getText().toString());
						num3_int=Integer.parseInt(grade3.getText().toString());
						sum=num1_int+num2_int+num3_int;
						break;
						
					case 4:
						num1_int=Integer.parseInt(grade1.getText().toString());
						num2_int=Integer.parseInt(grade2.getText().toString());
						num3_int=Integer.parseInt(grade3.getText().toString());
						num4_int=Integer.parseInt(grade4.getText().toString());
						sum=num1_int+num2_int+num3_int+num4_int;
						break;
						
					case 5:
						num1_int=Integer.parseInt(grade1.getText().toString());
						num2_int=Integer.parseInt(grade2.getText().toString());
						num3_int=Integer.parseInt(grade3.getText().toString());
						num4_int=Integer.parseInt(grade4.getText().toString());
						num5_int=Integer.parseInt(grade5.getText().toString());
						sum=num1_int+num2_int+num3_int+num4_int+num5_int;
						break;
						
					case 6:
						num1_int=Integer.parseInt(grade1.getText().toString());
						num2_int=Integer.parseInt(grade2.getText().toString());
						num3_int=Integer.parseInt(grade3.getText().toString());
						num4_int=Integer.parseInt(grade4.getText().toString());
						num5_int=Integer.parseInt(grade5.getText().toString());
						num6_int=Integer.parseInt(grade6.getText().toString());
						sum=num1_int+num2_int+num3_int+num4_int+num5_int+num6_int;
						break;
						
					default:
						break;
					}
					if(sum>=numMax){
						Toast.makeText(ShowSetting.this, "错误：设奖个数超过奖组票数！", Toast.LENGTH_LONG).show();
					}else{
						Map<String,String> map=new HashMap<String,String>();
						map.put("gradeNum", gradenum);
						map.put("grade1", grade01);map.put("drawname1", drawName01);
						map.put("grade2", grade02);map.put("drawname2", drawName02);
						map.put("grade3", grade03);map.put("drawname3", drawName03);
						map.put("grade4", grade04);map.put("drawname4", drawName04);
						map.put("grade5", grade05);map.put("drawname5", drawName05);
						map.put("grade6", grade06);map.put("drawname6", drawName06);
						map.put("grade7", grade07);map.put("drawname7", drawName07);
						map.put("grade8", grade08);map.put("drawname8", drawName08);
						map.put("grade9", grade09);map.put("drawname9", drawName09);
						map.put("grade10", grade010);map.put("drawname10", drawName010);
						int error=SqliteHandle.InsertDrawGrade(map);
						if(error==0){
							Builder dialog=new AlertDialog.Builder(ShowSetting.this);   
			                dialog.setTitle("数据更新成功！"); 
			                dialog.setNegativeButton("确定",new DialogInterface.OnClickListener() {   
			             	   
	                            public void onClick(DialogInterface dialog,   
	                                    int which) { 
	                            	
	                            	
	                            	edit.putString("SpinnerNum", gradenum).commit();
	                            	edit.putString("title", title).commit();
	                            	LotMainActivity.setting.edit().putString("TITLE", title).commit();
	                            	
	                            	edit.putString("maxNum", MaxNum).commit();
	            					
	                            	edit.putString("grade1", grade01).commit();
	                            	edit.putString("drawname1", drawName01).commit();
	                            	edit.putString("grade2", grade02).commit();
	                            	edit.putString("drawname2", drawName02).commit();
	                            	edit.putString("grade3", grade03).commit();
	                            	edit.putString("drawname3", drawName03).commit();
	                            	edit.putString("grade4", grade04).commit();
	                            	edit.putString("drawname4", drawName04).commit();
	                            	edit.putString("grade5", grade05).commit();
	                            	edit.putString("drawname5", drawName05).commit();
	                            	edit.putString("grade6", grade06).commit();
	                            	edit.putString("drawname6", drawName06).commit();
	                            	edit.putString("grade7", grade07).commit();
	                            	edit.putString("drawname7", drawName07).commit();
	                            	edit.putString("grade8", grade08).commit();
	                            	edit.putString("drawname8", drawName08).commit();
	                            	edit.putString("grade9", grade09).commit();
	                            	edit.putString("drawname9", drawName09).commit();
	                            	edit.putString("grade10", grade010).commit();
	                            	edit.putString("drawname10", drawName010).commit();
	                            	edit.putBoolean("FIRST", false).commit();
	                            	Intent intent =new Intent();
	                                intent.setClass(ShowSetting.this, LotMainActivity.class);
	                                startActivity(intent);
	                                finish();
	                            }
			                });
			                dialog.create().show();
						}
					}
				}
			}
		});
	}
	public void showItem(String num){
		/*grade1.setText("");
		grade2.setText("");
		grade3.setText("");
		grade4.setText("");
		grade5.setText("");
		grade6.setText("");
		grade7.setText("");
		grade8.setText("");
		grade9.setText("");
		grade10.setText("");
		
		drawName1.setText("");
		drawName2.setText("");
		drawName3.setText("");
		drawName4.setText("");
		drawName5.setText("");
		drawName6.setText("");
		drawName7.setText("");
		drawName8.setText("");
		drawName9.setText("");
		drawName10.setText("");*/
		
		String m="a"+num;
		if(m.equals("a1")){
			findViewById(R.id.a1).setVisibility(View.VISIBLE);
			findViewById(R.id.a2).setVisibility(View.GONE);
			findViewById(R.id.a3).setVisibility(View.GONE);
			findViewById(R.id.a4).setVisibility(View.GONE);
			findViewById(R.id.a5).setVisibility(View.GONE);
			findViewById(R.id.a6).setVisibility(View.GONE);
			findViewById(R.id.a7).setVisibility(View.GONE);
			findViewById(R.id.a8).setVisibility(View.GONE);
			findViewById(R.id.a9).setVisibility(View.GONE);
			findViewById(R.id.a10).setVisibility(View.GONE);
		}else if(m.equals("a2")){
			findViewById(R.id.a1).setVisibility(View.VISIBLE);
			findViewById(R.id.a2).setVisibility(View.VISIBLE);
			findViewById(R.id.a3).setVisibility(View.GONE);
			findViewById(R.id.a4).setVisibility(View.GONE);
			findViewById(R.id.a5).setVisibility(View.GONE);
			findViewById(R.id.a6).setVisibility(View.GONE);
			findViewById(R.id.a7).setVisibility(View.GONE);
			findViewById(R.id.a8).setVisibility(View.GONE);
			findViewById(R.id.a9).setVisibility(View.GONE);
			findViewById(R.id.a10).setVisibility(View.GONE);
		}else if(m.equals("a3")){
			findViewById(R.id.a1).setVisibility(View.VISIBLE);
			findViewById(R.id.a2).setVisibility(View.VISIBLE);
			findViewById(R.id.a3).setVisibility(View.VISIBLE);
			findViewById(R.id.a4).setVisibility(View.GONE);
			findViewById(R.id.a5).setVisibility(View.GONE);
			findViewById(R.id.a6).setVisibility(View.GONE);
			findViewById(R.id.a7).setVisibility(View.GONE);
			findViewById(R.id.a8).setVisibility(View.GONE);
			findViewById(R.id.a9).setVisibility(View.GONE);
			findViewById(R.id.a10).setVisibility(View.GONE);
		}else if(m.equals("a4")){
			findViewById(R.id.a1).setVisibility(View.VISIBLE);
			findViewById(R.id.a2).setVisibility(View.VISIBLE);
			findViewById(R.id.a3).setVisibility(View.VISIBLE);
			findViewById(R.id.a4).setVisibility(View.VISIBLE);
			findViewById(R.id.a5).setVisibility(View.GONE);
			findViewById(R.id.a6).setVisibility(View.GONE);
			findViewById(R.id.a7).setVisibility(View.GONE);
			findViewById(R.id.a8).setVisibility(View.GONE);
			findViewById(R.id.a9).setVisibility(View.GONE);
			findViewById(R.id.a10).setVisibility(View.GONE);
		}else if(m.equals("a5")){
			findViewById(R.id.a1).setVisibility(View.VISIBLE);
			findViewById(R.id.a2).setVisibility(View.VISIBLE);
			findViewById(R.id.a3).setVisibility(View.VISIBLE);
			findViewById(R.id.a4).setVisibility(View.VISIBLE);
			findViewById(R.id.a5).setVisibility(View.VISIBLE);
			findViewById(R.id.a6).setVisibility(View.GONE);
			findViewById(R.id.a7).setVisibility(View.GONE);
			findViewById(R.id.a8).setVisibility(View.GONE);
			findViewById(R.id.a9).setVisibility(View.GONE);
			findViewById(R.id.a10).setVisibility(View.GONE);
		}else if(m.equals("a6")){
			findViewById(R.id.a1).setVisibility(View.VISIBLE);
			findViewById(R.id.a2).setVisibility(View.VISIBLE);
			findViewById(R.id.a3).setVisibility(View.VISIBLE);
			findViewById(R.id.a4).setVisibility(View.VISIBLE);
			findViewById(R.id.a5).setVisibility(View.VISIBLE);
			findViewById(R.id.a6).setVisibility(View.VISIBLE);
			findViewById(R.id.a7).setVisibility(View.GONE);
			findViewById(R.id.a8).setVisibility(View.GONE);
			findViewById(R.id.a9).setVisibility(View.GONE);
			findViewById(R.id.a10).setVisibility(View.GONE);
		}else if(m.equals("a7")){
			findViewById(R.id.a1).setVisibility(View.VISIBLE);
			findViewById(R.id.a2).setVisibility(View.VISIBLE);
			findViewById(R.id.a3).setVisibility(View.VISIBLE);
			findViewById(R.id.a4).setVisibility(View.VISIBLE);
			findViewById(R.id.a5).setVisibility(View.VISIBLE);
			findViewById(R.id.a6).setVisibility(View.VISIBLE);
			findViewById(R.id.a7).setVisibility(View.VISIBLE);
			findViewById(R.id.a8).setVisibility(View.GONE);
			findViewById(R.id.a9).setVisibility(View.GONE);
			findViewById(R.id.a10).setVisibility(View.GONE);
		}else if(m.equals("a8")){
			findViewById(R.id.a1).setVisibility(View.VISIBLE);
			findViewById(R.id.a2).setVisibility(View.VISIBLE);
			findViewById(R.id.a3).setVisibility(View.VISIBLE);
			findViewById(R.id.a4).setVisibility(View.VISIBLE);
			findViewById(R.id.a5).setVisibility(View.VISIBLE);
			findViewById(R.id.a6).setVisibility(View.VISIBLE);
			findViewById(R.id.a7).setVisibility(View.VISIBLE);
			findViewById(R.id.a8).setVisibility(View.VISIBLE);
			findViewById(R.id.a9).setVisibility(View.GONE);
			findViewById(R.id.a10).setVisibility(View.GONE);
		}else if(m.equals("a9")){
			findViewById(R.id.a1).setVisibility(View.VISIBLE);
			findViewById(R.id.a2).setVisibility(View.VISIBLE);
			findViewById(R.id.a3).setVisibility(View.VISIBLE);
			findViewById(R.id.a4).setVisibility(View.VISIBLE);
			findViewById(R.id.a5).setVisibility(View.VISIBLE);
			findViewById(R.id.a6).setVisibility(View.VISIBLE);
			findViewById(R.id.a7).setVisibility(View.VISIBLE);
			findViewById(R.id.a8).setVisibility(View.VISIBLE);
			findViewById(R.id.a9).setVisibility(View.VISIBLE);
			findViewById(R.id.a10).setVisibility(View.GONE);
		}else if(m.equals("a10")){
			findViewById(R.id.a1).setVisibility(View.VISIBLE);
			findViewById(R.id.a2).setVisibility(View.VISIBLE);
			findViewById(R.id.a3).setVisibility(View.VISIBLE);
			findViewById(R.id.a4).setVisibility(View.VISIBLE);
			findViewById(R.id.a5).setVisibility(View.VISIBLE);
			findViewById(R.id.a6).setVisibility(View.VISIBLE);
			findViewById(R.id.a7).setVisibility(View.VISIBLE);
			findViewById(R.id.a8).setVisibility(View.VISIBLE);
			findViewById(R.id.a9).setVisibility(View.VISIBLE);
			findViewById(R.id.a10).setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
}