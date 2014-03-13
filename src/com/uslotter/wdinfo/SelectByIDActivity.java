package com.uslotter.wdinfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cr.uslotter.R;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;
import com.uslotter.util.Util;

public class SelectByIDActivity extends Activity implements OnClickListener {
	private ProgressDialog pBar = null;
	private Button BtMap = null;
	private Button BtBack = null;
	private Button BtSelectbyid = null;
	private EditText etWdtitle = null;
	private TextView tvWdtel = null;
	private TextView tvWdtype = null;
	private TextView tvWdquality = null;
	private TextView tvWddate = null;
	private TextView tvWdaddr = null;
	private TextView tvName = null;

	private String u_id = null;
	private String wdTitle = null;
	private int wdId = 0;
	private double jd, wd;
	private int errorCode = 0;// 错误判断
	private Map<String, String> info = new HashMap<String, String>();
	private String wdPhone = null;// 网点电话
	private String wdType = null;// 网点类型
	private String wdstate = null;// 网点性质
	private String wddate = null;// 建站日期
	private String wdaddr = null;// 网点地址
	private String wdname = null;// 业主姓名
	private String lingtuid = null;// 领土编码
	private String CenterID = null;// 中心

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wd_selectbyid);
		SharedPreferences _cardInOut = getSharedPreferences("CardInOut",
				MODE_WORLD_WRITEABLE);

		wdTitle = _cardInOut.getString("wdID", "");
		u_id = Util.getSharedPrefercencesString(SelectByIDActivity.this, "uid");
		CenterID = Util.getSharedPrefercencesString(SelectByIDActivity.this,
				"cid");
		BtMap = (Button) findViewById(R.id.selectbymap);
		BtBack = (Button) findViewById(R.id.wdinfobackby_id);
		BtSelectbyid = (Button) findViewById(R.id.selectbyid);

		etWdtitle = (EditText) findViewById(R.id.wdNumber);
		etWdtitle.setText(wdTitle);
		tvWdtel = (TextView) findViewById(R.id.wdtel);
		tvWdtype = (TextView) findViewById(R.id.wdtype);
		tvWdquality = (TextView) findViewById(R.id.wdquality);
		tvWddate = (TextView) findViewById(R.id.wddate);
		tvWdaddr = (TextView) findViewById(R.id.wdaddr);
		tvName = (TextView) findViewById(R.id.wdName);

		BtSelectbyid.setOnClickListener(this);
		BtMap.setOnClickListener(this);
		BtBack.setOnClickListener(this);
	}

	public void clear() {
		tvName.setText("");
		tvWdtel.setText("");
		tvWdtype.setText("");
		tvWdquality.setText("");
		tvWddate.setText("");
		tvWdaddr.setText("");
	}

	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 1) {
				tvName.setText(info.get("OName"));
				tvWdtel.setText(info.get("PPhone"));
				tvWdtype.setText(info.get("PType"));
				tvWdquality.setText(info.get("PPro"));
				tvWddate.setText(info.get("Begin_date"));

				String addr = info.get("PAddress");
				if (addr == null||addr.equals("")) {
					return;
				}

				if (addr.length() > 10) {
					tvWdaddr.setTextSize(10);
				}
				if (addr.length() > 20) {
					tvWdaddr.setTextSize(8);
				}
				tvWdaddr.setText(addr);

			} else {
				if (errorCode == 0) {
					ShowDialog("查询的网点不在管辖范围！");
				} else if (errorCode == -1) {
					ShowDialog("查询失败！");
				} else if (errorCode == -2) {
					ShowDialog("数据解析错误！");
				} else if (errorCode == -3) {
					ShowDialog("网络连接失败！");
				} else if (errorCode == -4) {
					ShowDialog("查询失败！");
				} else if (errorCode == -10) {
					ShowDialog("网络连接失败！");
				}
				clear();
			}

		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮 //
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;

		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.selectbymap:
			lingtuid = info.get("LTCode");
			if (lingtuid == null || lingtuid.equals("--")) {
				// ShowDialog("没搜索到当前网点地图！");
				Intent intent = new Intent();
				intent.setAction("android.intent.action.lingtuMapActivity");// 地图
				intent.putExtra("jd", -1);
				intent.putExtra("wd", -1);
				intent.putExtra("wdId", wdId);
				intent.putExtra("wdTitle", wdTitle);
				startActivity(intent);
			} else {
				Convert(lingtuid);
				Intent intent = new Intent();
				intent.setAction("android.intent.action.lingtuMapActivity");// 地图
				intent.putExtra("jd", jd);
				intent.putExtra("wd", wd);
				intent.putExtra("wdId", wdId);
				intent.putExtra("wdTitle", wdTitle);
				startActivity(intent);
			}
			break;

		case R.id.selectbyid:
			wdTitle = etWdtitle.getText().toString();
			if (wdTitle != null && !wdTitle.equals("")) {
				showProgressDialog(SelectByIDActivity.this, "网点信息",
						"获取数据中，请稍后...", ProgressDialog.STYLE_SPINNER);
				new Thread() {
					public void run() {
						boolean flag = findWdInfoByid(u_id, wdTitle);
						Message m = new Message();
						if (flag) {
							m.what = 1;
						} else {
							m.what = 0;
						}
						pBar.cancel();
						handler.sendMessage(m);
					}
				}.start();
			} else {
				ShowDialog("查询网点编号不能为空！");
			}
			break;

		case R.id.wdinfobackby_id:
			finish();
			break;
		default:
			break;
		}
	}

	public void Convert(String map) {// 灵图编码转经纬度
		int a = Integer.parseInt(map.substring(0, 1));
		int b = Integer.parseInt(map.substring(1, 2));
		int c = Integer.parseInt(map.substring(2, 3));
		int d = Integer.parseInt(map.substring(3, 4));
		int e = Integer.parseInt(map.substring(5, 6));
		int f = Integer.parseInt(map.substring(6, 7));
		int g = Integer.parseInt(map.substring(7, 8));
		int h = Integer.parseInt(map.substring(8, 9));
		int i = Integer.parseInt(map.substring(10, 11));
		int j = Integer.parseInt(map.substring(11, 12));
		int k = Integer.parseInt(map.substring(12, 13));
		int l = Integer.parseInt(map.substring(13, 14));

		wd = j * 10 + b + k * 0.1 + e * 0.01 + a * 0.001 + d * 0.0001;
		if (l < 5) {
			jd = 100 + l * 10 + g + h * 0.1 + c * 0.01 + f * 0.001 + i * 0.0001;
		} else {
			jd = l * 10 + g + h * 0.1 + c * 0.01 + f * 0.001 + i * 0.0001;
		}
		System.out.println("纬度" + wd + "," + "经度" + jd);
	}

	private boolean findWdInfoByid(String cardId, String wdTitle) {
		boolean flag = true;
		try {
			if (HttpUtil.checkNet(this)) {
				Map<String, String> rawParams = new HashMap<String, String>();
				// IC卡确认方式
				rawParams.put("U_id", u_id);// 专管员ID
				rawParams.put("WdTitle", wdTitle);// 网点编号
				rawParams.put("CenterID", CenterID);// 中心
				String result = HttpUtil.postRequest(HttpUrl.URL
						+ HttpUrl.InfoURL, rawParams);
				JSONArray jsonArray = new JSONArray(result);
				System.out.println(result);
				if (jsonArray.length() > 0) {
					JSONObject obj = jsonArray.getJSONObject(0);
					errorCode = (Integer) obj.get("Msg");
					if (errorCode <= 0) {
						flag = false;
					} else {
						// [{"PAddress":"番禺区市桥环城西路172号","C_Name":"广州","OName":"杜炳钊","Msg":1,"Title":"27104"}]
						wdPhone = (String) obj.get("PPhone");// 网点电话
						wdType = (String) obj.get("PType");// 网点类型
						wdstate = (String) (obj.get("PPro") == null ? "" : obj
								.get("PPro"));// 网点性质
						wddate = (String) (obj.get("Begin_date") == null ? ""
								: obj.get("Begin_date"));// 建站日期
						if (!"".equals(wddate)) {
							wddate = wddate.substring(0, 10);
						}
						wdaddr = (String) (obj.get("PAddress") == null ? ""
								: obj.get("PAddress"));// 地址
						lingtuid = (String) (obj.get("LTCode") == null ? ""
								: obj.get("LTCode"));// 灵图编码
						wdId = obj.getInt("wdId");// 网点id
						wdname = (String) (obj.get("Oname") == null ? "" : obj
								.get("Oname"));// 业主姓名

						info.put("PPhone", wdPhone);
						info.put("PType", wdType);
						info.put("PPro", wdstate);
						info.put("Begin_date", wddate);
						info.put("PAddress", wdaddr);
						info.put("LTCode", lingtuid);
						info.put("OName", wdname);
					}
				}
			} else {
				flag = false;
				errorCode = -10;
			}
		} catch (ClientProtocolException e) {
			errorCode = -2;
		} catch (IOException e) {
			errorCode = -3;
			System.out.println("error:" + e.getMessage());
		} catch (Exception e) {
			errorCode = -4;
		}
		return flag;
	}

	public void ShowDialog(String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示").setCancelable(false);
		builder.setMessage(title);
		builder.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		builder.create().show();
	}

	private void showProgressDialog(Context ctx, String title, String msg,
			int style) {
		pBar = new ProgressDialog(ctx);
		pBar.setTitle(title);
		pBar.setMessage(msg);
		pBar.setProgressStyle(style);
		pBar.show();
	}
}