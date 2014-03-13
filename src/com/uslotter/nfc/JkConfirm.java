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
	int errorCode = 0;// �����ж�
	int wdType = 0;// �������ͣ�1�������㡢2��ἴ�����㣩
	String U_id = null;// ר��Ա���
	String titleNum = "";// �ӷ�������ȡ����������
	String Cname = "";// ҵ������
	String Caddr = "";// �����ַ
	String Cen = "";// ����
	String CenterID = null;// ���ı��
	int type = 0;// ˢ�����ͣ�ǩ��ǩ��
	int state = 0;// ����ˢ����ԭ��
	Thread thread = null;
	SharedPreferences _cardInOut;
	public static int type_card = 0;// ǩ����ǩ�˷�����1.ˢ����2.����
	//public static Map<String, String> info = new HashMap<String, String>();
	public static Map<String, String> info = null;
	public static String wdTitle = "";// ����������Ž���ǩ��ǩ��
	public static String cardId = "";// IC�����
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
		type_card = getIntent().getIntExtra("type", 0);// ǩ����ǩ�˷�����1.ˢ����2.����
		type = getIntent().getIntExtra("value", 0);// 1.ǩ����2.ǩ�ˣ�3.¼��
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
						goToMainActivity("ǩ�˳ɹ�");
					}
				} else {
					String value = null;
					if (type == 1) {
						value = "ǩ��ʧ��!";
					} else if (type == 2) {
						value = "ǩ��ʧ��!";
					} else if (type == 3) {
						value = "ˢ��ʧ��!";
					}
					if (errorCode == -2) {
						goToMainActivity(value);
					} else if (errorCode == -1) {
						goToMainActivity("��Ч��IC��!");
					} else if (errorCode == -3) {
						goToMainActivity("��������ʧ��!");
					} else if (errorCode == -4) {
						goToMainActivity(value + "�����쳣!");
					} else if (errorCode == 0) {
						goToMainActivity(value + "���㲻�ڹ�Ͻ��Χ��");
					}else if(errorCode == -8){
						Toast.makeText(JkConfirm.this, "��������ʧ��", Toast.LENGTH_SHORT).show();
						Obj obj = new Obj();
						if(type_card == 1){
							//ˢ��  
							String userId = Util.getSharedPrefercencesString(JkConfirm.this, "username");
							obj.setMsg(cardId);
							obj.setState("");//�������ԭ��
							obj.setType(type_card+"");//ˢ�� ����
					        obj.setUserId(userId);
							obj.setValue(type+"");//ǩ����ǩ��
							
						}else if(type_card == 2){
							//����
							String userId = Util.getSharedPrefercencesString(JkConfirm.this, "username");
							obj.setMsg(wdTitle);
							obj.setState(state+"");//�������ԭ��
							obj.setType(type_card+"");//ˢ�� ����
					        obj.setUserId(userId);
							obj.setValue(type+"");//ǩ����ǩ��
						}
						if(objService == null){
						objService =  new ObjService(JkConfirm.this);
						}
						long index = objService.insert(obj);
						if(index < 0){
							goToMainActivity("���������쳣������δ������!");	
						}else{
						goToMainActivity("���������쳣�����ݱ���!");
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
		if (type_card == 1) {//ˢ��
			cardId = getIntent().getStringExtra("cardId");
			//String scardId = UslotteryMainMenu.CardInOut.getString("CardID",
			String scardId = _cardInOut.getString("CardID","");

			if (type == 1) {//ǩ��
				//�ж��Ƿ�������ǩ������������ǩ�����⿨�Ͳ���ǩ��
				if(!scardId.equals("")&&(!scardId.equals(cardId))){
					goToMainActivity("����"+scardId+"�Ѿ�ǩ��������ǩ��!");
					return;
				}
				if (cardId.equals(scardId)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("��ʾ").setCancelable(false);
					builder.setMessage("��������ǩ����");
					builder.setPositiveButton(
							"ȷ��",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									goToMainActivity("��ǩ����");
								}
							});
					builder.create().show();
				} else {

					showProgressDialog(this, "����ǩ��", "�ύ�����У����Ժ�...",
							ProgressDialog.STYLE_SPINNER);
					thread.start();

				}
			} else if (type == 2) {//ǩ��
				if (cardId.equals(scardId)) {
					showProgressDialog(this, "����ǩ��", "�ύ�����У����Ժ�...",
							ProgressDialog.STYLE_SPINNER);
					thread.start();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("��ʾ").setCancelable(false);
					builder.setMessage("��ˢ��ǰ���������IC��ǩ�ˣ�");
					builder.setPositiveButton(
							"ȷ��",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									goToMainActivity("ǩ����ˢ����");
								}
							});
					builder.create().show();
				}
			}
		} else if (type_card == 2) {//����
			wdTitle = getIntent().getStringExtra("wdTitle");
			//String swdTitle = UslotteryMainMenu.CardInOut.getString("wdID",null);
			String swdTitle = _cardInOut.getString("wdID","");
			
			if (type == 1) {//ǩ��
				if(!swdTitle.equals("")&&(!swdTitle.equals(cardId))){
					goToMainActivity("������"+swdTitle+"�Ѿ�ǩ��������ǩ��!");
					return;
				}
				if (wdTitle.equals(swdTitle)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("��ʾ").setCancelable(false);
					builder.setMessage("��������ǩ����");
					builder.setPositiveButton(
							"ȷ��",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									goToMainActivity("ǩ����ˢ����");
								}
							});
					builder.create().show();
				} else {
					showProgressDialog(this, "����ǩ��", "�ύ�����У����Ժ�...",
							ProgressDialog.STYLE_SPINNER);
					thread.start();
				}
			} else if (type == 2) {//ǩ��
				if (wdTitle.equals(swdTitle)) {
			//	if (wdTitle.equals("17005")) {	
					showProgressDialog(this, "����ǩ��", "�ύ�����У����Ժ�...",
							ProgressDialog.STYLE_SPINNER);
					thread.start();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("��ʾ").setCancelable(false);
					builder.setMessage("�����뵱ǰ�����������IC��ǩ�ˣ�");
					builder.setPositiveButton(
							"ȷ��",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									goToMainActivity("ǩ����ˢ����");
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
			// IC��ȷ�Ϸ�ʽ
			rawParams.put("cardNo", cardId);// IC��ID
			rawParams.put("U_id", U_id);// ר��ԱID
			rawParams.put("type", type + "");// ǩ������
			rawParams.put("WdTitle", wdTitle);// ������
			rawParams.put("state", state + "");// ����ˢ����ԭ��
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
					// [{"PAddress":"��خ�����Ż�����·172��","C_Name":"����","OName":"�ű���","Msg":1,"Title":"27104"}]
					titleNum = obj.getString("Title");// ������
					Cname = obj.getString("OName");// ҵ������
					Caddr = obj.getString("PAddress");// �����ַ
					Cen = obj.getString("C_Name");// ����
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
		dialog.setTitle("��ȷ����Ϣ").setCancelable(false);
		dialog.setItems(new String[] { "������:" + maps.get("wdNum"),
				"ҵ������:" + maps.get("oname"), "�����ַ:" + maps.get("paddress"),
				"����:" + maps.get("cname") }, null);

		dialog.setPositiveButton("ȷ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			//	if(type_card == 3){//�ǩ��
					
			//	}else{//ר��Ա���������ǩ��
//				UslotteryMainMenu.CardInOut.edit().putString("wdID", titleNum)
//						.commit();
//				UslotteryMainMenu.CardInOut.edit().putString("CardID", cardId)
//						.commit();
					_cardInOut.edit().putString("CardID", cardId).commit();
				
					_cardInOut.edit().putString("wdID", titleNum).commit();	
					
	//	}
				goToMainActivity("ǩ���ɹ�");
			}
		});
		dialog.setNegativeButton("ȡ��", new OnClickListener() {
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