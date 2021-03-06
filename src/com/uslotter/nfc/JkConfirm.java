package com.uslotter.nfc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.UslotteryCardin;
import com.uslotter.UslotteryCardout;
import com.uslotter.UslotteryCardrecord;
import com.uslotter.db.Obj;
import com.uslotter.db.ObjService;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;
import com.uslotter.util.Util;

public class JkConfirm extends Activity {
	ProgressDialog pBar = null;
	int errorCode = 0;// 错误判断
	int wdType = 0;// 网点类型（1电脑网点、2社会即开网点）
	String U_id = null;// 专管员编号
	String titleNum = "";// 从服务器获取到的网点编号
	String Cname = "";// 业主姓名
	String Caddr = "";// 网点地址
	String Cen = "";// 中心
	String CenterID = null;// 中心编号
	int type = 0;// 刷卡类型，签到签退
	int state = 0;// 不能刷卡的原因
	Thread thread = null;
	SharedPreferences _cardInOut;
	public static int type_card = 0;// 签到，签退方法，1.刷卡，2.手输
	//public static Map<String, String> info = new HashMap<String, String>();
	public static Map<String, String> info = null;
	public static String wdTitle = "";// 输入的网点编号进行签到签退
	public static String cardId = "";// IC卡编号
	//TextView tv = null;
	ObjService objService =null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.waithttp);
		//tv = (TextView) this.findViewById(R.id.waithttp_tv);
	//	CenterID = UslotteryActivity.CenterID;
	
		_cardInOut = getSharedPreferences("CardInOut", MODE_WORLD_WRITEABLE);
		CenterID = Util.getSharedPrefercencesString(JkConfirm.this,"cid");
	//	CenterID = LotApplication.getInstance().getCid();
		type_card = getIntent().getIntExtra("type", 0);// 签到，签退方法，1.刷卡，2.手输
		type = getIntent().getIntExtra("value", 0);// 1.签到，2.签退，3.录单
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					// show();
					if (type == 1) {
						showInfo(JkConfirm.this, info);
					}
					if (type == 2) {			
						//UslotteryMainMenu.CardInOut.edit().clear().commit();
						_cardInOut.edit().clear().commit();
						goToMainActivity("签退成功");
					}
				} else {
					String value = null;
					if (type == 1) {
						value = "签到失败!";
					} else if (type == 2) {
						value = "签退失败!";
					} else if (type == 3) {
						value = "刷卡失败!";
					}
					if (errorCode == -2) {
						goToMainActivity(value);
					} else if (errorCode == -1) {
						goToMainActivity("无效的IC卡!");
					} else if (errorCode == -3) {
						goToMainActivity("网络连接失败!");
					} else if (errorCode == -4) {
						goToMainActivity(value + "发生异常!");
					} else if (errorCode == 0) {
						goToMainActivity(value + "网点不在管辖范围！");
					}else if(errorCode == -8){
						Toast.makeText(JkConfirm.this, "网络连接失败", Toast.LENGTH_SHORT).show();
						Obj obj = new Obj();
						if(type_card == 1){
							//刷卡  
							String userId = Util.getSharedPrefercencesString(JkConfirm.this, "username");
							obj.setMsg(cardId);
							obj.setState("");//不能输的原因
							obj.setType(type_card+"");//刷卡 手输
					        obj.setUserId(userId);
							obj.setValue(type+"");//签到，签退
							
						}else if(type_card == 2){
							//手输
							String userId = Util.getSharedPrefercencesString(JkConfirm.this, "username");
							obj.setMsg(wdTitle);
							obj.setState(state+"");//不能输的原因
							obj.setType(type_card+"");//刷卡 手输
					        obj.setUserId(userId);
							obj.setValue(type+"");//签到，签退
						}
						if(objService == null){
						objService =  new ObjService(JkConfirm.this);
						}
						long index = objService.insert(obj);
						if(index < 0){
							goToMainActivity("网络连接异常，数据未被保存!");	
						}else{
						goToMainActivity("网络连接异常，数据保存!");
						}
					}else{
						goToMainActivity(value);
					}
				}
			}
		};
		thread = new Thread() {
			public void run() {
				boolean flag;
				if(type_card == 1){
					 flag = findWdInfoBy(cardId, "");
				}else{
					 flag = findWdInfoBy("", wdTitle);
				}
				
				Message m = new Message();
				if (flag) {
					m.what = 1;
				} else {
					m.what = 0;
				}
				pBar.cancel();
				handler.sendMessage(m);
			}
		};
		if (type_card == 1) {//刷卡
			cardId = getIntent().getStringExtra("cardId");
			//String scardId = UslotteryMainMenu.CardInOut.getString("CardID",
			String scardId = _cardInOut.getString("CardID","");

			if (type == 1) {//签到
				//判断是否其它卡签过到，其他卡签到后这卡就不能签到
				if(!scardId.equals("")&&(!scardId.equals(cardId))){
					goToMainActivity("卡号"+scardId+"已经签到，请先签退!");
					return;
				}
				if (cardId.equals(scardId)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("提示").setCancelable(false);
					builder.setMessage("该网点已签到！");
					builder.setPositiveButton(
							"确定",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									goToMainActivity("已签到！");
								}
							});
					builder.create().show();
				} else {

					showProgressDialog(this, "网点签到", "提交数据中，请稍后...",
							ProgressDialog.STYLE_SPINNER);
					thread.start();

				}
			} else if (type == 2) {//签退
				if (cardId.equals(scardId)) {
					showProgressDialog(this, "网点签退", "提交数据中，请稍后...",
							ProgressDialog.STYLE_SPINNER);
					thread.start();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("提示").setCancelable(false);
					builder.setMessage("请刷当前工作网点的IC卡签退！");
					builder.setPositiveButton(
							"确定",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									goToMainActivity("签退请刷卡！");
								}
							});
					builder.create().show();
				}
			}
		} else if (type_card == 2) {//手输
			wdTitle = getIntent().getStringExtra("wdTitle");
			//String swdTitle = UslotteryMainMenu.CardInOut.getString("wdID",null);
			String swdTitle = _cardInOut.getString("wdID","");
			
			if (type == 1) {//签到
				if(!swdTitle.equals("")&&(!swdTitle.equals(cardId))){
					goToMainActivity("网点编号"+swdTitle+"已经签到，请先签退!");
					return;
				}
				if (wdTitle.equals(swdTitle)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("提示").setCancelable(false);
					builder.setMessage("该网点已签到！");
					builder.setPositiveButton(
							"确定",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									goToMainActivity("签到请刷卡！");
								}
							});
					builder.create().show();
				} else {
					showProgressDialog(this, "网点签到", "提交数据中，请稍后...",
							ProgressDialog.STYLE_SPINNER);
					thread.start();
				}
			} else if (type == 2) {//签退
				if (wdTitle.equals(swdTitle)) {
			//	if (wdTitle.equals("17005")) {	
					showProgressDialog(this, "网点签退", "提交数据中，请稍后...",
							ProgressDialog.STYLE_SPINNER);
					thread.start();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("提示").setCancelable(false);
					builder.setMessage("请输入当前工作的网点的IC卡签退！");
					builder.setPositiveButton(
							"确定",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									goToMainActivity("签退请刷卡！");
								}
							});
					builder.create().show();
				}

			}
			state = getIntent().getIntExtra("state", 0);
		}
	}

	private boolean findWdInfoBy(String cardId, String wdTitle) {
		info =new HashMap<String, String>(); 
		boolean flag = true;
	//	U_id = UslotteryActivity.U_Id;
		U_id = Util.getSharedPrefercencesString(JkConfirm.this,"uid");
		try {
			if(HttpUtil.checkNet(JkConfirm.this)){
			// String url=HttpUrl.URL;
			Map<String, String> rawParams = new HashMap<String, String>();
			// IC卡确认方式
			rawParams.put("cardNo", cardId);// IC卡ID
			rawParams.put("U_id", U_id);// 专管员ID
			rawParams.put("type", type + "");// 签到类型
			rawParams.put("WdTitle", wdTitle);// 网点编号
			rawParams.put("state", state + "");// 不能刷卡的原因
			rawParams.put("CenterID", CenterID);
			// rawParams.put("date", datefmt.format(new Date()));
			String result = HttpUtil.postRequest(HttpUrl.URL + HttpUrl.CardURL,
					rawParams);
			JSONArray jsonArray = new JSONArray(result);
			Log.d("jsonarry", jsonArray.toString());
			if (jsonArray.length() > 0) {
				JSONObject obj = jsonArray.getJSONObject(0);
				errorCode = (Integer) obj.get("Msg");
				if (errorCode <= 0) {
					flag = false;
				} else {
					// [{"PAddress":"番禺区市桥环城西路172号","C_Name":"广州","OName":"杜炳钊","Msg":1,"Title":"27104"}]
					titleNum = obj.getString("Title");// 网点编号
					Cname = obj.getString("OName");// 业主姓名
					Caddr = obj.getString("PAddress");// 网店地址
					Cen = obj.getString("C_Name");// 中心
					info.put("wdNum", titleNum);
					info.put("oname", Cname);
					info.put("paddress", Caddr);
					info.put("cname", Cen);
				}
			}
			}else{
				errorCode = -8;
				flag = false;
			}
		} catch (ClientProtocolException e) {
			errorCode = -3;
		} catch (IOException e) {
			errorCode = -3;
			System.out.println("error:" + e.getMessage());
		} catch (Exception e) {
			errorCode = -4;
		}
		return flag;
	}

	private void showProgressDialog(Context ctx, String title, String msg,int style) {
		pBar = new ProgressDialog(ctx);
		pBar.setTitle(title);
		pBar.setMessage(msg);
		pBar.setProgressStyle(style);
		pBar.show();
	}

	public void goToMainActivity(String msg) {
		Intent intent = null;
		if (type == 1) {
			intent = new Intent(this, UslotteryCardin.class);
		} else if (type == 2) {
			intent = new Intent(this, UslotteryCardout.class);
		} else if (type == 3) {
			intent = new Intent(this, UslotteryCardrecord.class);
		}
		intent.putExtra("msg", msg);
		startActivity(intent);
		finish();

	}

	/*
	 * public void show(){ Intent intent = new Intent(this,Converter.class);
	 * intent.putExtra("cTitle", titleNum); startActivity(intent); }
	 */

	public void showInfo(Context ctx, Map<String, String> maps) {
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("请确认信息").setCancelable(false);
		dialog.setItems(new String[] { "网店编号:" + maps.get("wdNum"),
				"业主姓名:" + maps.get("oname"), "网店地址:" + maps.get("paddress"),
				"中心:" + maps.get("cname") }, null);

		dialog.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			//	if(type_card == 3){//活动签到
					
			//	}else{//专管员到网点进行签到
//				UslotteryMainMenu.CardInOut.edit().putString("wdID", titleNum)
//						.commit();
//				UslotteryMainMenu.CardInOut.edit().putString("CardID", cardId)
//						.commit();
					_cardInOut.edit().putString("CardID", cardId).commit();
				
					_cardInOut.edit().putString("wdID", titleNum).commit();	
					
	//	}
				goToMainActivity("签到成功");
			}
		});
		dialog.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setClass(JkConfirm.this, UslotteryCardin.class);
				startActivity(intent);
				finish();
			}
		});
		dialog.create().show();
	}
}