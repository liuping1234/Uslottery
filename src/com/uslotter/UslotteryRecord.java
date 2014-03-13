package com.uslotter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.compress.ImageCompress;
import com.uslotter.db.AppService;
import com.uslotter.mode.App;
import com.uslotter.mode.Multiple;
import com.uslotter.util.DialogUtil;
import com.uslotter.util.HttpConnect;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;
import com.uslotter.util.Util;

public class UslotteryRecord extends Activity implements OnClickListener {
	private SharedPreferences sp;
	private DatePickerDialog dialog_date = null;
	private TimePickerDialog dialog_time = null;
	private EditText et_date = null;
	private EditText et_time = null;
	private EditText et_wdbh, et_fwdh;
	private Button save, exit;
	private String url = "";
	private String wdbh, date, time;
	private String fwdh;
	int i;
	private LinearLayout r0, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12,
			r13;
	private ImageView iv_fwyb_add = null;
	private ImageView iv_fwyb = null;
	private String path_fwyb = "";
	public List<LinearLayout> rels;// 保存网点标准化 重点检查 ...
	private String _flag = "";
	private String _options = "";
	private String _df = "100";
	private String _sp = "0";
	private ArrayList<String> _paths;
	private Button un_commit, commit, un_standardlize;
	private AppService appService = null;
	private TextView tv_count;
	private ProgressDialog pd = null;
	private App app = null;
	private EditText et_date1 = null;
	private EditText et_time1 = null;
	private Spinner sp_fwpj;
	private EditText et_wdyj;
	private EditText et_zjfx;
	private EditText et_zgyjy;
	private EditText et_jbrqm;
	private String imagepath = null;

	// 多选选项
	private String array[] = { "标准化检查(a)", "网点违规(M)", "培训活动(c)", 
			"会议(j)", "宣传促销(b)", "资料派发(d)" ,"其它服务(K)","终端维护(e)","建站勘察(f)","移站勘察(g)","验收勘察(h)","重点检查(h)"};
	// 默认一项为true
	private boolean[] arrayState = { true, false, false, false, false, false,
			false,false,false,false,false,false};
	// 多选容器
	private ListView arrayCheck;
	private Button btn_muti;
	private ArrayList<Multiple> multiplesList;
	private Multiple multiple;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			if (msg.what == 1) {
				pd.dismiss();
				String str = msg.obj.toString();
				JSONArray arr;
				try {
					arr = new JSONArray(str);
					JSONObject obj = arr.getJSONObject(0);
					String _msg = obj.getString("Msg");
					if (_msg.equals("1")) {
						if (UslotteryRecord.this == null) {
							return;
						}
						final Dialog dialog = DialogUtil.showDialog(
								UslotteryRecord.this, "上传成功");
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {

							@Override
							public void run() {
								dialog.dismiss();
								Intent intent = new Intent(UslotteryRecord.this,
										UslotteryMainMenu2.class);
								startActivity(intent);
								UslotteryRecord.this.overridePendingTransition(
									R.anim.push_right_in,R.anim.push_right_out);
								finish();
							}
						}, 1000);

					} else if (_msg.equals("2")) {
						if (UslotteryRecord.this == null) {
							return;
						}
						DialogUtil.showDialog(UslotteryRecord.this,
								"网点已退站,上传失败!");
					} else if (_msg.equals("3")) {
						if (UslotteryRecord.this == null) {
							return;
						}
						DialogUtil.showDialog(UslotteryRecord.this,
								"服务单号已存在，不能重复上传!");
					} else {
						if (UslotteryRecord.this == null) {
							return;
						}
						DialogUtil.showDialog(UslotteryRecord.this, "上传失败!");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else if (msg.what == 2) {
				try {
					if (msg.obj == null) {
						Toast.makeText(UslotteryRecord.this, "没有收到服务器消息！",
								Toast.LENGTH_SHORT).show();
						return;
					}
					String str = msg.obj.toString();

					JSONArray arr = null;
					String status = null;
					String gpstatus = null;
					arr = new JSONArray(str);
					// JSONArray arr1 = arr.getJSONArray(0);
					JSONObject obj = arr.getJSONObject(0);
					String _msg = obj.getString("Msg");
					if (_msg.equals("1")) {
						status = obj.getString("status");// 通知标题
						gpstatus = obj.getString("gpstatus");// 通知类型
					} else {
						return;
					}
					Intent intent = new Intent();
					intent.setAction("android.intent.action.UslotteryRecordStandardActivity");
					intent.putExtra("Status", status);
					intent.putExtra("gpstatus", gpstatus);
					// UslotteryRecord.this.startActivity(intent);
					UslotteryRecord.this.startActivityForResult(intent, 3);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (msg.what == -1) {
				// 网络连接失败
				if (pd != null && pd.isShowing()) {
					pd.cancel();
				}
				Toast.makeText(UslotteryRecord.this, "网络连接失败!数据被保存",
						Toast.LENGTH_SHORT).show();

				String userId = Util.getSharedPrefercencesString(
						UslotteryRecord.this, "username");
				List<App> apps1 = appService.getAllAppsFromUserId(userId);
				if (apps1.size() >= 10) {
					Intent intent = new Intent(UslotteryRecord.this,
							UslotteryMainMenu2.class);
					startActivity(intent);
					UslotteryRecord.this.overridePendingTransition(
							R.anim.push_right_in, R.anim.push_right_out);
					finish();
					return;
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UslotteryRecord.this);
				builder.setTitle("提示");
				builder.setMessage("网络连接失败，未提交的记录是否保存到本机?");
				builder.setPositiveButton("保存",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								saveRecord();
							}
						});
				builder.setNegativeButton("不保存",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										UslotteryRecord.this,
										UslotteryMainMenu2.class);
								startActivity(intent);
								UslotteryRecord.this.overridePendingTransition(
										R.anim.push_right_in,
										R.anim.push_right_out);
								finish();
							}
						}).create().show();
			} else if (msg.what == -2) {
				// 网络连接失败
				if (pd != null && pd.isShowing()) {
					pd.cancel();
				}
				Toast.makeText(UslotteryRecord.this, "出现未知异常!",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	/**
	 * 
	 * 将app中保存的状态显示在界面中
	 */
	private void saved() {
		_paths = new ArrayList<String>();
		String _wdbh = app.getWdbh();
		String fwdh_str = app.getFwdh();
		String fwrq_str = app.getFwrq();
		if (_wdbh != null && !_wdbh.equals(""))
			et_wdbh.setText(_wdbh);
		if (fwdh_str != null && !fwdh_str.equals("")) {// 服务单号
			et_fwdh.setText(fwdh_str);
		}

		if (fwrq_str != null && !fwrq_str.equals("")) {// 服务日期
			String[] dates = fwrq_str.split(",");
			String date_str = dates[0];
			String time_str = dates[1];
			et_date.setText(date_str);
			et_time.setText(time_str);
		}
		// 标准化
		if (app.getWdzp() != null && !app.getWdzp().toString().equals("")) {
			String[] paths = app.getWdzp().toString().split(",");
			for (int i = 0; i < paths.length; i++) {
				_paths.add(paths[i]);
			}
		}
		// 违规照片
		if (app.getWgzp() != null && !app.getWgzp().toString().equals("")) {
			String[] paths = app.getWgzp().toString().split(",");
			for (int i = 0; i < paths.length; i++) {
				_paths.add(paths[i]);
			}
		}

		// 培训
		if (app.getTrains() != null) {
			if (app.getTrains().get(0).getPicPath() != null
					&& !app.getTrains().get(0).getPicPath().equals("")) {
				String[] paths = app.getTrains().get(0).getPicPath().toString()
						.split(",");
				for (int i = 0; i < paths.length; i++) {
					_paths.add(paths[i]);
				}
			}
		}

		// 终端维护
		if (app.getMaintenanceList() != null) {
			if (app.getMaintenanceList().get(0).getPicPath() != null
					&& !app.getMaintenanceList().get(0).getPicPath().equals("")) {
				String[] paths = app.getMaintenanceList().get(0).getPicPath()
						.toString().split(",");
				for (int i = 0; i < paths.length; i++) {
					_paths.add(paths[i]);
				}
			}
		}

		// 会议
		if (app.getMeetingList() != null) {
			if (app.getMeetingList().get(0).getPicPath() != null
					&& !app.getMeetingList().get(0).getPicPath().equals("")) {
				String[] paths = app.getMeetingList().get(0).getPicPath()
						.toString().split(",");
				for (int i = 0; i < paths.length; i++) {
					_paths.add(paths[i]);
				}
			}
		}

		// 宣传促销
		if (app.getPublicities() != null) {
			if (app.getPublicities().get(0).getPicPath() != null
					&& !app.getPublicities().get(0).getPicPath().equals("")) {
				String[] paths = app.getPublicities().get(0).getPicPath()
						.toString().split(",");
				for (int i = 0; i < paths.length; i++) {
					_paths.add(paths[i]);
				}
			}
		}
		
		// 资料派发
		if (app.getDataAllots() != null) {
			if (app.getDataAllots().get(0).getPicPath() != null
					&& !app.getDataAllots().get(0).getPicPath().equals("")) {
				String[] paths = app.getDataAllots().get(0).getPicPath()
						.toString().split(",");
				for (int i = 0; i < paths.length; i++) {
					_paths.add(paths[i]);
				}
			}
		}
		
		// 其它服务
		if (app.getOtherServes() != null) {
			if (app.getOtherServes().get(0).getPicPath() != null
					&& !app.getOtherServes().get(0).getPicPath().equals("")) {
				String[] paths = app.getOtherServes().get(0).getPicPath()
						.toString().split(",");
				for (int i = 0; i < paths.length; i++) {
					_paths.add(paths[i]);
				}
			}
		}
		
		// 建站勘察
				if (app.getProspects() != null) {
					if (app.getProspects().get(0).getPicPath() != null
							&& !app.getProspects().get(0).getPicPath().equals("")) {
						String[] paths = app.getProspects().get(0).getPicPath()
								.toString().split(",");
						for (int i = 0; i < paths.length; i++) {
							_paths.add(paths[i]);
						}
					}
				}
			
			// 移站勘察
			if (app.getMoveProspects() != null) {
				if (app.getMoveProspects().get(0).getPicPath() != null
						&& !app.getMoveProspects().get(0).getPicPath().equals("")) {
					String[] paths = app.getMoveProspects().get(0).getPicPath()
							.toString().split(",");
					for (int i = 0; i < paths.length; i++) {
						_paths.add(paths[i]);
					}
				}
			}
			
			// 验收勘察
						if (app.getTestProspects() != null) {
							if (app.getTestProspects().get(0).getPicPath() != null
									&& !app.getTestProspects().get(0).getPicPath().equals("")) {
								String[] paths = app.getTestProspects().get(0).getPicPath()
										.toString().split(",");
								for (int i = 0; i < paths.length; i++) {
									_paths.add(paths[i]);
								}
							}
						}

		String wcsj_str = app.getWcrq();
		String fwpj_str = app.getFwpj();
		String wdyj_str = app.getWdyj();
		String zjfx_str = app.getZjfx();
		String zgyjy_str = app.getZgyyj();
		String jbr_str = app.getJbr();
		if (wcsj_str != null && !wcsj_str.equals("")) {// 完成时间
			String[] dates1 = wcsj_str.split(",");
			String date_str1 = dates1[0];
			String time_str1 = dates1[1];
			et_date1.setText(date_str1);
			et_time1.setText(time_str1);
		}

		if (fwpj_str != null && !fwpj_str.equals("")) {// 服务评价
			if (fwpj_str.equals("满意")) {
				sp_fwpj.setSelection(0);
			} else if (fwpj_str.equals("一般")) {
				sp_fwpj.setSelection(1);
			} else if (fwpj_str.equals("不满意")) {
				sp_fwpj.setSelection(2);
			}
		}
		if (wdyj_str != null && !wdyj_str.equals("")) {// 网点意见
			et_wdyj.setText(wdyj_str);
		}
		if (zjfx_str != null && !zjfx_str.equals("")) {// 总结分析
			et_zjfx.setText(zjfx_str);
		}
		if (zgyjy_str != null && !zgyjy_str.equals("")) {// 专管员建议
			et_zgyjy.setText(zgyjy_str);
		}
		if (jbr_str != null && !jbr_str.equals("")) {
			et_jbrqm.setText(jbr_str);
		}

		if (app.getMultiples() != null) {
			String _index = app.getMultiples().get(0).getMulIndex();
			String _state = app.getMultiples().get(0).getMulState();
			String[] indexArray = _index.split(";");
			String[] stateArray = _state.split(";");
			for (int i = 0; i < array.length; i++) {
				rels.get(Integer.parseInt(indexArray[i])).setVisibility(
						Integer.parseInt(stateArray[i]));
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record2);
		File file = new File("mnt/sdcard/myimage");
		if (!file.exists()) {
			file.mkdirs();
		}
		relsAdd();
		sp = getSharedPreferences("Login", MODE_WORLD_WRITEABLE);
		app = new App();
		app.setState("0");
		et_wdbh = (EditText) this.findViewById(R.id.record2_et_wdbh);
		_flag = this.getIntent().getStringExtra("flag");
		final Calendar calendar = Calendar.getInstance();
		save = (Button) this.findViewById(R.id.record2_save);
		exit = (Button) this.findViewById(R.id.record2_back);
		et_date = (EditText) this.findViewById(R.id.record2_et_date);

		int mon = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		et_date.setText(calendar.get(Calendar.YEAR) + "-"
				+ ((mon + 1) > 9 ? (mon + 1) : "0" + (mon + 1)) + "-"
				+ (day > 9 ? day : "0" + day));
		et_time = (EditText) this.findViewById(R.id.record2_et_time);
		et_date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_date = new DatePickerDialog(UslotteryRecord.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								et_date.setText(year
										+ "-"
										+ ((monthOfYear + 1) > 9 ? (monthOfYear + 1)
												: "0" + (monthOfYear + 1))
										+ "-"
										+ (dayOfMonth > 9 ? dayOfMonth : "0"
												+ dayOfMonth));
							}
						}, calendar.get(Calendar.YEAR), calendar
								.get(Calendar.MONTH), calendar
								.get(Calendar.DAY_OF_MONTH));
				dialog_date.show();
			}
		});
		et_time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_time = new TimePickerDialog(UslotteryRecord.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								et_time.setText((hourOfDay > 9 ? hourOfDay
										: "0" + hourOfDay)
										+ ":"
										+ (minute > 9 ? minute : "0" + minute));

							}
						}, calendar.get(Calendar.HOUR_OF_DAY), calendar
								.get(Calendar.MINUTE), true);
				dialog_time.show();
			}
		});
		int minu = calendar.get(Calendar.MINUTE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);

		et_time.setText((hour > 9 ? hour : "0" + hour) + ":"
				+ (minu > 9 ? minu : "0" + minu));
		et_fwdh = (EditText) this.findViewById(R.id.record2_et_fwdh);
		// ........................................
		et_date1 = (EditText) this.findViewById(R.id.record2_et_date1);
		et_date1.setText(calendar.get(Calendar.YEAR) + "-"
				+ ((mon + 1) > 9 ? (mon + 1) : "0" + (mon + 1)) + "-"
				+ (day > 9 ? day : "0" + day));

		et_time1 = (EditText) this.findViewById(R.id.record2_et_time1);
		et_date1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_date = new DatePickerDialog(UslotteryRecord.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								et_date1.setText(year
										+ "-"
										+ ((monthOfYear + 1) > 9 ? (monthOfYear + 1)
												: "0" + (monthOfYear + 1))
										+ "-"
										+ (dayOfMonth > 9 ? dayOfMonth : "0"
												+ dayOfMonth));
							}
						}, calendar.get(Calendar.YEAR), calendar
								.get(Calendar.MONTH), calendar
								.get(Calendar.DAY_OF_MONTH));
				dialog_date.show();
			}
		});
		et_time1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_time = new TimePickerDialog(UslotteryRecord.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {

								et_time1.setText((hourOfDay > 9 ? hourOfDay
										: "0" + hourOfDay)
										+ ":"
										+ (minute > 9 ? minute : "0" + minute));

							}
						}, calendar.get(Calendar.HOUR_OF_DAY), calendar
								.get(Calendar.MINUTE), true);
				dialog_time.show();
			}
		});

		et_time1.setText((hour > 9 ? hour : "0" + hour) + ":"
				+ (minu > 9 ? minu : "0" + minu));
		// 服务评价
		sp_fwpj = (Spinner) this.findViewById(R.id.record2_sp_fwpj);
		// 网点意见
		et_wdyj = (EditText) this.findViewById(R.id.record2_et_wdyj);
		et_wdyj.setMovementMethod(ScrollingMovementMethod.getInstance());
		// 总结分析
		et_zjfx = (EditText) this.findViewById(R.id.record2_et_zjfx);
		et_zjfx.setMovementMethod(ScrollingMovementMethod.getInstance());
		// 专管员建议
		et_zgyjy = (EditText) this.findViewById(R.id.record2_et_zgyjy);
		et_zgyjy.setMovementMethod(ScrollingMovementMethod.getInstance());
		// 经办人签名
		et_jbrqm = (EditText) this.findViewById(R.id.record2_et_jbrqm);
		et_jbrqm.setMovementMethod(ScrollingMovementMethod.getInstance());
		// ...........................................

		if (_flag != null && _flag.equals("1")) {
			app = (App) this.getIntent().getSerializableExtra("app");
			_options = app.getKfx();
			Log.d("tag", "_options" + _options);
			_df = app.getDf();
			_sp = app.getState();
			saved();

		} else if (_flag != null && _flag.equals("2")) {
			app = (App) this.getIntent().getSerializableExtra("_app");
			saved();

		} else if (_flag != null && _flag.equals("3")) {
			app = (App) this.getIntent().getSerializableExtra("updateApp");
			saved();
		} else if (_flag != null && _flag.equals("4")) {
			app = (App) this.getIntent().getSerializableExtra("train");
			saved();
		} else if (_flag != null && _flag.equals("5")) {
			app = (App) this.getIntent().getSerializableExtra("terminal");
			saved();
		} else if (_flag != null && _flag.equals("6")) {
			app = (App) this.getIntent().getSerializableExtra("meeting");
			saved();
		} else if (_flag != null && _flag.equals("7")) {
			app = (App) this.getIntent().getSerializableExtra("publicity");
			saved();
		} else if (_flag != null && _flag.equals("8")) {
			app = (App) this.getIntent().getSerializableExtra("dataAllot");
			saved();
		}else if (_flag != null && _flag.equals("9")) {
			app = (App) this.getIntent().getSerializableExtra("otherServe");
			saved();
		}else if (_flag != null && _flag.equals("10")) {
			app = (App) this.getIntent().getSerializableExtra("remould");
			saved();
		}
		else if (_flag != null && _flag.equals("11")) {
			app = (App) this.getIntent().getSerializableExtra("NewProspect");
			saved();
		}
		else if (_flag != null && _flag.equals("12")) {
			app = (App) this.getIntent().getSerializableExtra("MoveProspect");
			saved();
		}
		else if (_flag != null && _flag.equals("13")) {
			app = (App) this.getIntent().getSerializableExtra("TestProspect");
			saved();
		}

		btn_muti = (Button) this.findViewById(R.id.record2_mutibtn);
		btn_muti.setOnClickListener(checkListener);
		// 显示网点标准化

		iv_fwyb_add = (ImageView) this.findViewById(R.id.record2_fwyb_iv_add);
		iv_fwyb = (ImageView) this.findViewById(R.id.record2_fwyb_iv);
		if (app.getFwyb() != null && !app.getFwyb().trim().equals("")) {
			path_fwyb = app.getFwyb().trim();
			Bitmap bmp = BitmapFactory.decodeFile(path_fwyb);
			bmp = Bitmap.createScaledBitmap(bmp, 80, 80, false);
			iv_fwyb.setVisibility(View.VISIBLE);
			iv_fwyb.setImageBitmap(bmp);
			iv_fwyb.setTag(path_fwyb);

			iv_fwyb.setOnClickListener(clickListener);
			iv_fwyb.setOnLongClickListener(longClickListener);
		} else {
			iv_fwyb.setTag("");
		}
		iv_fwyb_add.setOnClickListener(pictureListener);

		commit = (Button) this.findViewById(R.id.commit);
		un_commit = (Button) this.findViewById(R.id.uncommit);
		un_standardlize = (Button) this.findViewById(R.id.unstandardize);
		commit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UslotteryRecord.this,
						RecordedFormActivity.class);
				UslotteryRecord.this.startActivity(intent);
				UslotteryRecord.this.overridePendingTransition(
						R.anim.push_left_in, R.anim.push_left_out);
				finish();
			}
		});

		un_commit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UslotteryRecord.this,
						UnRecordFormActivity.class);
				UslotteryRecord.this.startActivity(intent);
				UslotteryRecord.this.overridePendingTransition(
						R.anim.push_left_in, R.anim.push_left_out);
				finish();
			}
		});

		un_standardlize.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UslotteryRecord.this,
						UnStandardizeActivity.class);
				UslotteryRecord.this.startActivity(intent);
				UslotteryRecord.this.overridePendingTransition(
						R.anim.push_left_in, R.anim.push_left_out);
				finish();
			}
		});
		exit.setOnClickListener(exitListener);
		save.setOnClickListener(submitListener);
		r0.setOnClickListener(this);
		r2.setOnClickListener(this);
		r3.setOnClickListener(this);
		r4.setOnClickListener(this);
		r5.setOnClickListener(this);
		r6.setOnClickListener(this);
		r7.setOnClickListener(this);
		r8.setOnClickListener(this);
		r10.setOnClickListener(this);
		r11.setOnClickListener(this);
		r13.setOnClickListener(this);
		r1.setOnClickListener(this);		
		appService = new AppService(this);
		tv_count = (TextView) this.findViewById(R.id.record2_tv_count);
		String userId = Util.getSharedPrefercencesString(this, "username");
		List<App> apps = appService.getAllAppsFromUserId(userId);
		if (apps.size() > 0) {
			tv_count.setText(apps.size() + "");
		}
	}
	
	@Override
	public void onClick(View v) {
		String con = et_wdbh.getText().toString();
		if (con.equals("") || con.length() != 5) {
			DialogUtil.showDialog(UslotteryRecord.this, "请填写正确网点编号!");
			return;
		}
		String fwdh = et_fwdh.getText().toString();
		String fwrq_date = et_date.getText().toString();
		String fwrq_time = et_time.getText().toString();
		app.setWdbh(con);
		app.setFwdh(fwdh);
		app.setFwrq(fwrq_date + "," + fwrq_time);
		// ..........................................
		String et_date1_str = et_date1.getText().toString().trim();
		String et_time1_str = et_time1.getText().toString().trim();
		String jbr_str = et_jbrqm.getText().toString().trim();
		String fwpj_str = sp_fwpj.getSelectedItem().toString();
		String et_wdyj_str = et_wdyj.getText().toString().trim();
		String et_zjfx_str = et_zjfx.getText().toString().trim();
		String et_zgyjy_str = et_zgyjy.getText().toString().trim();
		app.setWcrq(et_date1_str + "," + et_time1_str);
		app.setJbr(jbr_str);
		app.setFwpj(fwpj_str);
		app.setWdyj(et_wdyj_str);
		app.setZjfx(et_zjfx_str);
		app.setZgyyj(et_zgyjy_str);
		String path = iv_fwyb.getTag().toString();
		app.setFwyb(path);
		Intent intent;
		switch (v.getId()) {			
	case R.id.record2_llwdbzh:		
			intent = new Intent(this, UslotteryRecord_wdbzhActivity.class);
			intent.putExtra("app", app);
			UslotteryRecord.this.startActivity(intent);
			UslotteryRecord.this.overridePendingTransition(
					R.anim.push_left_in, R.anim.push_left_out);
		break;
	case R.id.record2_wdwg_rl:
		intent = new Intent(this, UslotteryRecord_wdwgActivity.class);
		intent.putExtra("app", app);
		UslotteryRecord.this.startActivity(intent);
		UslotteryRecord.this.overridePendingTransition(
				R.anim.push_left_in, R.anim.push_left_out);
			break;
	case R.id.record2_pxhd_rl:
		intent = new Intent(this, UslotteryRecord_pxhdActivity.class);
		intent.putExtra("app", app);
		UslotteryRecord.this.startActivity(intent);
		UslotteryRecord.this.overridePendingTransition(
				R.anim.push_left_in, R.anim.push_left_out);
		break;
	case R.id.record2_hy_rl:
		intent = new Intent(this, UslotteryRecord_hyActivity.class);
		intent.putExtra("app", app);
		UslotteryRecord.this.startActivity(intent);
		UslotteryRecord.this.overridePendingTransition(
				R.anim.push_left_in, R.anim.push_left_out);
		break;
	case R.id.record2_zccx_rl:
		intent = new Intent(this, UslotteryRecord_xccxActivity.class);
		intent.putExtra("app", app);
		UslotteryRecord.this.startActivity(intent);
		UslotteryRecord.this.overridePendingTransition(
				R.anim.push_left_in, R.anim.push_left_out);
		break;
	case R.id.record2_zlpf_rl:
		intent = new Intent(this, UslotteryRecord_zlpfActivity.class);
		intent.putExtra("app", app);
		UslotteryRecord.this.startActivity(intent);
		UslotteryRecord.this.overridePendingTransition(
				R.anim.push_left_in, R.anim.push_left_out);
		break;
	case R.id.record2_qtfw_rl:
		intent = new Intent(this, UslotteryRecord_qtfwActivity.class);
		intent.putExtra("app", app);
		UslotteryRecord.this.startActivity(intent);
		UslotteryRecord.this.overridePendingTransition(
				R.anim.push_left_in, R.anim.push_left_out);
		break;
	case R.id.record2_zdwh_rl:
		intent = new Intent(this, UslotteryRecord_zdwhActivity.class);
		intent.putExtra("app", app);
		UslotteryRecord.this.startActivity(intent);
		UslotteryRecord.this.overridePendingTransition(
				R.anim.push_left_in, R.anim.push_left_out);
		break;
	case R.id.record2_jzkc_rl:
		intent = new Intent(this, UslotteryRecord_jzkcActivity.class);
		intent.putExtra("app", app);
		UslotteryRecord.this.startActivity(intent);
		UslotteryRecord.this.overridePendingTransition(
				R.anim.push_left_in, R.anim.push_left_out);
		break;

	case R.id.record2_yzkc_rl:
		intent = new Intent(this, UslotteryRecord_yzkcActivity.class);
		intent.putExtra("app", app);
		UslotteryRecord.this.startActivity(intent);
		UslotteryRecord.this.overridePendingTransition(
				R.anim.push_left_in, R.anim.push_left_out);
		break;
	case R.id.record2_yskc_rl:
		intent = new Intent(this, UslotteryRecord_yskcActivity.class);
		intent.putExtra("app", app);
		UslotteryRecord.this.startActivity(intent);
		UslotteryRecord.this.overridePendingTransition(
				R.anim.push_left_in, R.anim.push_left_out);
	break;
	case R.id.record2_zdjc_rl:
		intent = new Intent(this, UslotteryRecord_zdjcActivity.class);
		intent.putExtra("app", app);
		UslotteryRecord.this.startActivity(intent);
		UslotteryRecord.this.overridePendingTransition(
				R.anim.push_left_in, R.anim.push_left_out);
	break;
		default:
			break;
		}

	}
	
	
  
	public void relsAdd() {
		rels = new ArrayList<LinearLayout>();
		Log.d("log", rels.size() + " ");
		r0 = (LinearLayout) this.findViewById(R.id.record2_llwdbzh);
		r1 = (LinearLayout) this.findViewById(R.id.record2_zdjc_rl);
		r2 = (LinearLayout) this.findViewById(R.id.record2_zccx_rl);
		r3 = (LinearLayout) this.findViewById(R.id.record2_pxhd_rl);
		r4 = (LinearLayout) this.findViewById(R.id.record2_zlpf_rl);
		r5 = (LinearLayout) this.findViewById(R.id.record2_zdwh_rl);
		r6 = (LinearLayout) this.findViewById(R.id.record2_jzkc_rl);
		r7 = (LinearLayout) this.findViewById(R.id.record2_yzkc_rl);
		r8 = (LinearLayout) this.findViewById(R.id.record2_yskc_rl);
		r9 = (LinearLayout) this.findViewById(R.id.record2_zlgx_rl);
		r10 = (LinearLayout) this.findViewById(R.id.record2_hy_rl);
		r11 = (LinearLayout) this.findViewById(R.id.record2_qtfw_rl);
		r12 = (LinearLayout) this.findViewById(R.id.record2_wdgz_rl);
		r13 = (LinearLayout) this.findViewById(R.id.record2_wdwg_rl);
		r0.setVisibility(View.VISIBLE);
		r1.setVisibility(View.GONE);
		r2.setVisibility(View.GONE);
		r3.setVisibility(View.GONE);
		r4.setVisibility(View.GONE);
		r5.setVisibility(View.GONE);
		r6.setVisibility(View.GONE);
		r7.setVisibility(View.GONE);
		r8.setVisibility(View.GONE);
		r9.setVisibility(View.GONE);
		r10.setVisibility(View.GONE);
		r11.setVisibility(View.GONE);
		r12.setVisibility(View.GONE);
		r13.setVisibility(View.GONE);
		rels.add(r0);
		rels.add(r13);
		rels.add(r3);
		rels.add(r10);
		rels.add(r2);
		rels.add(r4);
		rels.add(r11);
		rels.add(r5);
		rels.add(r6);		
		rels.add(r7);
		rels.add(r8);
		rels.add(r1);
	}

	/**
	 * 多选框选择要操作的内容
	 */
	public OnClickListener checkListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			AlertDialog ad = new AlertDialog.Builder(UslotteryRecord.this)
					.setTitle("选择区域")
					.setMultiChoiceItems(array, arrayState,
							new DialogInterface.OnMultiChoiceClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton, boolean isChecked) {
									// 点击某个区域
								}
							})
					.setPositiveButton("确定",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int whichButton) {
									multiple = new Multiple();
									multiplesList = new ArrayList<Multiple>();														
									StringBuilder index = new StringBuilder();
									StringBuilder state = new StringBuilder();
									for (int i = 0; i < array.length; i++) {

										if (arrayCheck.getCheckedItemPositions().get(i)) {
											index.append(i + ";");
											state.append(View.VISIBLE + ";");
											rels.get(i).setVisibility(View.VISIBLE);

										} else {
											index.append(i + ";");
											state.append(View.GONE + ";");
											rels.get(i).setVisibility(View.GONE);
											// 标准化
											if (rels != null&& rels.get(i) == r0) {
												app.setDf("");
												app.setWdzp("");
												app.setState("");
											}
											// 违规
											if (rels != null&& rels.get(i) == r13) {
												app.setWgzp("");
												for (int j = 0; j < app.getInfo().size(); j++) {
													app.getInfo().get(j).setIndex(0);
													app.getInfo().get(j).setWgnr("");
													app.getInfo().get(j).setWgxx("");
													app.getInfo().clear();
												}
											}
											// 培训
											if (rels != null&& rels.get(i) == r3) {
												if (app.getTrains() != null) {
													app.setTrains(null);
												}
											}

											// 会议
											if (rels != null&& rels.get(i) == r10) {
												if (app.getMeetingList() != null) {												
													app.setMeetingList(null);
												}
											}
											// 宣传促销
											if (rels != null&& rels.get(i) == r2) {
												if (app.getPublicities() != null) {
													app.setPublicities(null);
												}
											}
											// 资料派发
											if (rels != null&& rels.get(i) == r4) {
												if (app.getDataAllots() != null) {										
													app.setDataAllots(null);
												}
											}
											// 其它服务
											if (rels != null&& rels.get(i) == r11) {
												if (app.getOtherServes() != null) {
													app.setOtherServes(null);
												}
											}
											// 终端
											if (rels != null&& rels.get(i) == r5) {
												if (app.getMaintenanceList() != null) {
													app.setMaintenanceList(null);
												}
											}
											
											// 建站
											if (rels != null&& rels.get(i) == r6) {
												if (app.getProspects() != null) {
													app.setProspects(null);
												}
											}
											
											// 移站
											if (rels != null&& rels.get(i) == r7) {
												if (app.getMoveProspects() != null) {
													app.setMoveProspects(null);
												}
											}
											// 验收
											if (rels != null&& rels.get(i) == r8) {
												if (app.getTestProspects() != null) {
													app.setTestProspects(null);
												}
											}
										}
									}
									multiple.setMulIndex(index.toString());
									multiple.setMulState(state.toString());
									multiplesList.add(multiple);
									app.setMultiples(multiplesList);
									if (arrayCheck.getCheckedItemPositions().size() > 0) {

									} else {
										// 没有选择
									}
									dialog.dismiss();
								}
							}).setNegativeButton("取消", null).create();
			arrayCheck = ad.getListView();
			ad.show();
		}

	};

	/**
	 * 添加图片按钮,可以拍照或者从图库选择
	 */
	public OnClickListener pictureListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			new AlertDialog.Builder(UslotteryRecord.this)
					.setTitle("温馨提示!")
					.setMessage("请选择手机拍照还是图库")
					.setPositiveButton("拍照",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									imagepath = Environment
											.getExternalStorageDirectory()
											+ "/myimage/a"
											+ Util.getCurTime()
											+ ".jpg";
									Intent intent = new Intent(
											"android.media.action.IMAGE_CAPTURE");
									intent.putExtra(MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(new File(imagepath)));
									startActivityForResult(intent, 6);
								}
							})
					.setNegativeButton("图库",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
									intent.addCategory(Intent.CATEGORY_OPENABLE);
									intent.setType("image/*");
									intent.putExtra("return-data", true);
									startActivityForResult(intent, 4);
								}
							}).create().show();

		}
	};

	/**
	 * 提交按钮监听事件
	 */
	public OnClickListener submitListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			new AlertDialog.Builder(UslotteryRecord.this)
					.setTitle("提示")
					.setMessage("保存并提交业务还是保存业务？")
					.setPositiveButton("保存并提交",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									// 截取网点编号前俩位
									if (!et_wdbh.getText().toString().trim()
											.equals("")
											&& et_wdbh.getText().toString()
													.trim().length() > 2) {
										String branchNumber = et_wdbh.getText()
												.toString().trim()
												.substring(0, 2);
										Log.d("tag", " branchNumber:  "
												+ branchNumber);
										// 城市编号
										String cityId = sp.getString(
												"CenterID", null);
										Log.d("tag", " cityId:  " + cityId);
										if (branchNumber.equals(cityId)) {
											save(5);
										} else if ((branchNumber.equals("17") || branchNumber
												.equals("28"))
												&& (cityId.equals("17") || cityId
														.equals("28"))) {
											save(5);
										} else if ((branchNumber.equals("02") || branchNumber
												.equals("26"))
												&& (cityId.equals("02") || cityId
														.equals("26"))) {
											save(5);
										} else {
											Toast.makeText(
													UslotteryRecord.this,
													"请输入正确的网点编号",
													Toast.LENGTH_SHORT).show();
										}
									} else {
										Toast.makeText(UslotteryRecord.this,
												"网点编号不能为空", Toast.LENGTH_SHORT)
												.show();
									}

								}
							})
					.setNegativeButton("保存",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (!et_wdbh.getText().toString().trim()
											.equals("")
											&& et_wdbh.getText().toString()
													.trim().length() > 2) {
										// 截取网点编号前俩位
										String branchNumber = et_wdbh.getText()
												.toString().trim()
												.substring(0, 2);
										Log.d("tag", " branchNumber:  "
												+ branchNumber);
										// 城市编号
										String cityId = sp.getString(
												"CenterID", null);
										Log.d("tag", " cityId:  " + cityId);
										if (branchNumber.equals(cityId)) {
											save(1);
										} else if ((branchNumber.equals("17") || branchNumber
												.equals("28"))
												&& (cityId.equals("17") || cityId
														.equals("28"))) {
											save(1);
										} else if ((branchNumber.equals("02") || branchNumber
												.equals("26"))
												&& (cityId.equals("02") || cityId
														.equals("26"))) {
											save(1);
										} else {
											Toast.makeText(
													UslotteryRecord.this,
													"请输入正确的网点编号",
													Toast.LENGTH_SHORT).show();
										}
									} else {
										Toast.makeText(UslotteryRecord.this,
												"网点编号不能为空", Toast.LENGTH_SHORT)
												.show();
									}
								}
							}).create().show();

		}
	};

	/**
	 * 返回按钮监听事件
	 */
	public OnClickListener exitListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			wdbh = et_wdbh.getText().toString().trim();
			if (wdbh.equals("")) {
				Intent intent = new Intent(UslotteryRecord.this,
						UslotteryMainMenu2.class);
				startActivity(intent);
				UslotteryRecord.this.overridePendingTransition(
						R.anim.push_right_in, R.anim.push_right_out);
				finish();
				return;
			}
			String userId = Util.getSharedPrefercencesString(
					UslotteryRecord.this, "username");
			List<App> apps1 = appService.getAllAppsFromUserId(userId);
			if (apps1.size() >= 10) {
				Intent intent = new Intent(UslotteryRecord.this,
						UslotteryMainMenu2.class);
				startActivity(intent);
				UslotteryRecord.this.overridePendingTransition(
						R.anim.push_right_in, R.anim.push_right_out);
				finish();
				return;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(
					UslotteryRecord.this);
			builder.setTitle("提示");
			builder.setMessage("未提交的记录是否保存?");
			builder.setPositiveButton("保存",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							saveRecord();
						}
					});
			builder.setNegativeButton("不保存",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(UslotteryRecord.this,
									UslotteryMainMenu2.class);
							startActivity(intent);
							UslotteryRecord.this
									.overridePendingTransition(
											R.anim.push_right_in,
											R.anim.push_right_out);
							finish();
						}
					}).create().show();

		}
	};

	/**
	 * 将单保存进数据库
	 */
	public void saveRecord() {
		String userId = Util.getSharedPrefercencesString(this, "username");

		wdbh = et_wdbh.getText().toString().trim();

		if (wdbh.equals("") || (wdbh.length() != 5)) {
			DialogUtil.showDialog(UslotteryRecord.this, "请填写正确网点编号!");
			return;
		}

		fwdh = et_fwdh.getText().toString().trim();
		if (fwdh.equals("")) {
			DialogUtil.showDialog(UslotteryRecord.this, "请填写好服务单号!");
			return;
		}

		date = et_date.getText().toString().trim();
		if (date.equals("")) {
			DialogUtil.showDialog(UslotteryRecord.this, "请填写好日期!");
			return;
		}

		time = et_time.getText().toString().trim();
		if (time.equals("")) {
			DialogUtil.showDialog(UslotteryRecord.this, "请填写好时间!");
			return;
		}

		String date1 = et_date1.getText().toString().trim();
		String time1 = et_time1.getText().toString().trim();
		String fwpj_str = sp_fwpj.getSelectedItem().toString();
		String et_wdyj_str = et_wdyj.getText().toString().trim();
		String et_zjfx_str = et_zjfx.getText().toString().trim();
		String et_zgyjy_str = et_zgyjy.getText().toString().trim();
		String et_jbrqm_str = et_jbrqm.getText().toString().trim();
		if (date1.equals("")) {
			DialogUtil.showDialog(UslotteryRecord.this, "请填写好完成日期!");
			return;
		}

		if (time1.equals("")) {
			DialogUtil.showDialog(UslotteryRecord.this, "请填写好完成时间!");
			return;
		}

		app.setWdbh(wdbh);
		app.setFwdh(fwdh);
		app.setFwrq(date + "," + time);
		app.setFwyb(path_fwyb);

		if (_sp != null) {
			app.setState(_sp);
		}

		if (_df != null) {
			app.setDf(_df);
		}

		if (_options != null) {
			app.setKfx(_options);
		}

		if (!userId.equals("")) {
			app.setUserId(userId);
		}
		app.setWcrq(date1 + "," + time1);
		app.setJbr(et_jbrqm_str);
		app.setFwpj(fwpj_str);
		app.setWdyj(et_wdyj_str);
		app.setZjfx(et_zjfx_str);
		app.setZgyyj(et_zgyjy_str);
		if (_paths != null) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < _paths.size(); i++) {
				if (i == _paths.size() - 1) {
					builder.append(_paths.get(i));
				} else {
					builder.append(_paths.get(i) + ",");
				}
			}
			app.setWdzp(builder.toString());
		}

		List<App> apps = appService.getAllAppsFromUserId(userId);
		if (apps.size() != 0) {
			for (int i = 0; i < apps.size(); i++) {
				if (apps.get(i).getWdbh().equals(wdbh)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UslotteryRecord.this);
					builder.setTitle("提示");
					builder.setMessage("该网点未提交记录已存在，是否覆盖?");
					builder.setPositiveButton("覆盖",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									appService.updateFromWDBH(app);
									Intent intent = new Intent(
											UslotteryRecord.this,
											UslotteryMainMenu2.class);
									startActivity(intent);
									UslotteryRecord.this
											.overridePendingTransition(
													R.anim.push_right_in,
													R.anim.push_right_out);

									finish();
									dialog.dismiss();
								}
							});
					builder.setNegativeButton("不覆盖",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent intent = new Intent(
											UslotteryRecord.this,
											UslotteryMainMenu2.class);
									startActivity(intent);
									UslotteryRecord.this
											.overridePendingTransition(
													R.anim.push_right_in,
													R.anim.push_right_out);

									finish();
								}
							}).create().show();

					return;
				}
			}
		}

		appService = new AppService(UslotteryRecord.this);
		appService.insert(app);
		List<App> _apps = appService.getAllAppsFromUserId(userId);
		if (_apps.size() > 0) {
			tv_count.setText(_apps.size() + "");
		}

		Intent intent = new Intent(UslotteryRecord.this,
				UslotteryMainMenu2.class);
		startActivity(intent);
		UslotteryRecord.this.overridePendingTransition(R.anim.push_right_in,
				R.anim.push_right_out);

		finish();

	}

	/**
	 * 返回事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮 //
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			wdbh = et_wdbh.getText().toString().trim();
			if (wdbh.equals("")) {
				Intent intent = new Intent(UslotteryRecord.this,
						UslotteryMainMenu2.class);
				startActivity(intent);
				UslotteryRecord.this.overridePendingTransition(
						R.anim.push_right_in, R.anim.push_right_out);
				finish();
				return true;
			}
			String userId = Util.getSharedPrefercencesString(
					UslotteryRecord.this, "username");
			List<App> apps1 = appService.getAllAppsFromUserId(userId);
			if (apps1.size() >= 10) {
				Intent intent = new Intent(UslotteryRecord.this,
						UslotteryMainMenu2.class);
				startActivity(intent);
				UslotteryRecord.this.overridePendingTransition(
						R.anim.push_right_in, R.anim.push_right_out);
				finish();
				return true;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(
					UslotteryRecord.this);
			builder.setTitle("提示");
			builder.setMessage("未提交的记录是否保存?");
			builder.setPositiveButton("保存",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							saveRecord();
						}
					});
			builder.setNegativeButton("不保存",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(UslotteryRecord.this,
									UslotteryMainMenu2.class);
							startActivity(intent);
							UslotteryRecord.this
									.overridePendingTransition(
											R.anim.push_right_in,
											R.anim.push_right_out);
							finish();
						}
					}).create().show();

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * 长按删除
	 */
	OnLongClickListener longClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			AlertDialog.Builder builder = new Builder(UslotteryRecord.this);
			builder.setTitle("提示");
			builder.setMessage("确定删除此照片吗？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							iv_fwyb.setTag("");
							iv_fwyb.setVisibility(View.GONE);
							dialog.dismiss();
						}

					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
			return false;

		}
	};

	/**
	 * 短按查看原图
	 */
	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ImageView iv = (ImageView) v;
			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(
					Uri.fromFile(new File(iv.getTag().toString())), "image/*");
			UslotteryRecord.this.startActivity(intent);

		}
	};

	/**
	 * 返回结果处理事件
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 4) {
				Bundle bundle = intent.getExtras();
				Uri originalUri = intent.getData();
				if (originalUri != null) {
					Bitmap bitMap = null;
					try {
						if (bitMap != null)
							bitMap.recycle();
						bitMap = null;
						ContentResolver resolver = getContentResolver();
						bitMap = MediaStore.Images.Media.getBitmap(resolver,
								originalUri);
						String[] proj = { MediaColumns.DATA };
						// 按我个人理解 这个是获得用户选择的图片的索引值
						Cursor cursor = managedQuery(originalUri, proj, null,
								null, null);
						// 将光标移至开头 ，这个很重要，不小心很容易引起越界
						cursor.moveToFirst(); // 最后根据索引值获取图片路径
						imagepath = Environment.getExternalStorageDirectory()
								+ "/myimage/a" + Util.getCurTime() + ".jpg";
						path_fwyb = ImageCompress.compressImage3(bitMap,
								UslotteryRecord.this, imagepath);
						bitMap = Bitmap.createScaledBitmap(bitMap, 80, 80,
								false);
						iv_fwyb.setVisibility(View.VISIBLE);
						iv_fwyb.setImageBitmap(bitMap);
						iv_fwyb.setTag(path_fwyb);

						iv_fwyb.setOnClickListener(clickListener);
						iv_fwyb.setOnLongClickListener(longClickListener);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (requestCode == 6) {

				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
					return;
				}
				ImageView iv = new ImageView(this);
				iv.setPadding(0, 0, 15, 0);
				iv.setLayoutParams(new LayoutParams(80,
						LayoutParams.FILL_PARENT));
				Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
				bitmap = ImageCompress.decodeFile(imagepath, 800, 800);
				String path_fwyb = ImageCompress.compressImage3(bitmap,
						UslotteryRecord.this, imagepath);
				iv_fwyb.setVisibility(View.VISIBLE);
				iv_fwyb.setTag(path_fwyb);
				iv_fwyb.setImageBitmap(bitmap);

				iv_fwyb.setOnClickListener(clickListener);
				iv_fwyb.setOnLongClickListener(longClickListener);

			}
		}
	}

	/**
	 * 上传数据
	 * 
	 * @param url
	 * @param files
	 */
	public void upload(final String url, final File[] files) {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				if (HttpUtil.checkNet(UslotteryRecord.this)) {
					try {
						String result = HttpConnect.uploadFile1(url, files);
						Message msg = handler.obtainMessage();
						msg.obj = result;
						msg.what = 1;
						handler.sendMessage(msg);

					} catch (Exception e) {
						handler.sendEmptyMessage(-2);
						e.printStackTrace();
					}
				} else {
					handler.sendEmptyMessage(-1);
				}
				Looper.loop();
			}

		}.start();
	}

	/**
	 * 保存
	 * 
	 * @param _submitStatus
	 */
	public void save(int _submitStatus) {
		wdbh = et_wdbh.getText().toString().trim();
		if (wdbh.equals("")) {
			DialogUtil.showDialog(UslotteryRecord.this, "请填写好网点编号!");
			return;
		}
		//

		fwdh = et_fwdh.getText().toString().trim();
		if (fwdh.equals("")) {
			DialogUtil.showDialog(UslotteryRecord.this, "请填写好服务单号!");
			return;
		}

		date = et_date.getText().toString().trim();
		if (date.equals("")) {
			DialogUtil.showDialog(UslotteryRecord.this, "请填写好日期!");
			return;
		}

		time = et_time.getText().toString().trim();
		if (time.equals("")) {
			DialogUtil.showDialog(UslotteryRecord.this, "请填写好时间!");
			return;
		}
		app.setWdbh(wdbh);
		app.setFwdh(fwdh);
		app.setFwrq(date + "," + time);
		if (iv_fwyb != null && iv_fwyb.getTag() != null
				&& (!iv_fwyb.getTag().toString().equals(""))) {
			app.setFwyb(iv_fwyb.getTag().toString());
		}

		// ................................................................
		String et_date1_str = et_date1.getText().toString().trim();
		String et_time1_str = et_time1.getText().toString().trim();
		if (et_date1_str.equals("")) {
			DialogUtil.showDialog(UslotteryRecord.this, "请填写好完成日期!");
			return;
		}
		if (et_time1_str.equals("")) {
			DialogUtil.showDialog(UslotteryRecord.this, "请填写好完成时间!");
			return;
		}
		String jbr_str = et_jbrqm.getText().toString().trim();
		String fwpj_str = sp_fwpj.getSelectedItem().toString();
		String et_wdyj_str = et_wdyj.getText().toString().trim();
		String et_zjfx_str = et_zjfx.getText().toString().trim();
		String et_zgyjy_str = et_zgyjy.getText().toString().trim();
		// String et_jbrqm_str = et_jbrqm.getText().toString().trim();
		app.setWcrq(et_date1_str + "," + et_time1_str);
		app.setJbr(jbr_str);
		app.setFwpj(fwpj_str);
		app.setWdyj(et_wdyj_str);
		app.setZjfx(et_zjfx_str);
		app.setZgyyj(et_zgyjy_str);
		// .................................................................
		pd = ProgressDialog.show(UslotteryRecord.this, "提示", "正在与服务器交互，请稍等...",
				true, true);

		String u_id = Util.getSharedPrefercencesString(UslotteryRecord.this,
				"uid");
		String c_id = Util.getSharedPrefercencesString(UslotteryRecord.this,
				"cid");

		// ***************网点违规数据*****************//
		StringBuilder violations = new StringBuilder();
		StringBuilder violations_details = new StringBuilder();
		StringBuilder violations_index = new StringBuilder();
		if (app.getInfo() != null) {
			for (int i = 0; i < app.getInfo().size(); i++) {
				violations.append(app.getInfo().get(i).getWgnr() + ";");
				violations_details.append(app.getInfo().get(i).getWgxx() + ";");
				violations_index.append(app.getInfo().get(i).getIndex() + ";");
			}

		}
		Log.d("tag", violations_index+"");
		Log.d("tag", violations_details+"");
		Log.d("tag", violations+"");
		

		// ****************培训活动数据***************//
		String _trainThemeId = "";
		int trainThemeId = 0;
		String remarks = "";
		String trainObject = "";
		if (app.getTrains() != null) {
			trainThemeId = app.getTrains().get(0).getTrainThemeItem();
			remarks = app.getTrains().get(0).getRemarks();
			trainObject = app.getTrains().get(0).getTrainObject();
			_trainThemeId = trainThemeId + "";
		}


		// ****************会议数据***********************//
		String meetingTheme = "";
		int _meetingTheme = 0;
		String meetingName = "";
		String meetingDetails = "";
		String meetingNoto = "";
		if (app.getMeetingList() != null) {
			_meetingTheme = app.getMeetingList().get(0).getThemeItem();
			meetingName = app.getMeetingList().get(0).getName();
			meetingDetails = app.getMeetingList().get(0).getDetails();
			meetingNoto = app.getMeetingList().get(0).getNoto();
			meetingTheme = _meetingTheme + "";
		}

		// ***************宣传促销数据********************//
		String publicityItem = "";
		String publicityNoto = "";
		if (app.getPublicities() != null) {
			publicityItem = app.getPublicities().get(0).getThemeItem() + "";
			publicityNoto = app.getPublicities().get(0).getNoto();
		}
		
		// ***************其它服务********************//
		String otherNumber ="";
		String otherName ="";
		String otherNoto="";
		String otherDetails="";
		if(app.getOtherServes()!=null){
			otherNumber = app.getOtherServes().get(0).getNumber();
			otherDetails = app.getOtherServes().get(0).getDetails();
			otherNoto = app.getOtherServes().get(0).getNoto();
			otherName = app.getOtherServes().get(0).getName();			
		}
			
		// ***************资料派发数据********************//
		StringBuilder dataValue = new StringBuilder();
		StringBuilder dataNumber =new StringBuilder();
		String dataNoto = "";
		if(app.getDataAllots()!=null){
			dataNoto = app.getDataAllots().get(0).getNoto();
			if(app.getDataAllots().get(0).getDateInfos()!=null){
				for (int i = 0; i < app.getDataAllots().get(0).getDateInfos().size(); i++) {
					dataValue.append(app.getDataAllots().get(0).getDateInfos().get(i).getMapValue()+";");
					dataNumber.append(app.getDataAllots().get(0).getDateInfos().get(i).getNumber()+";");
				}
			}
		}
		
		
		// ***************终端维护********************//
		String zd_category = "";
		String zd_model ="";
		String zd_number ="";
		String zd_fault ="";
		String zd_processMode="";
		StringBuilder zd_name = new StringBuilder();
		StringBuilder zd_partNumber = new StringBuilder();
		if(app.getMaintenanceList()!=null){
			zd_category = app.getMaintenanceList().get(0).getCategory()+"";
			zd_model =app.getMaintenanceList().get(0).getModelValue();
			zd_number = app.getMaintenanceList().get(0).getMachineNumber();
			zd_fault = app.getMaintenanceList().get(0).getFault();
			zd_processMode = app.getMaintenanceList().get(0).getProcessMode()+"";
			if(app.getMaintenanceList().get(0).getInfos()!=null){
				for (int i = 0; i < app.getMaintenanceList().get(0).getInfos().size(); i++) {
					zd_name.append(app.getMaintenanceList().get(0).getInfos().get(i).getNameValue()+";");
					zd_partNumber.append(app.getMaintenanceList().get(0).getInfos().get(i).getNumber()+";");
				}
			}
		}
		
		// ***************建站勘察********************j//
		String jz_site = "";
		String jz_branch="";
		String jz_number = "";
		String jz_type = "";
		String verify_person ="";
		String verify_branch="";
		String explain_person = "";
		String explain_branch ="";
		String jz_noto="";
		if(app.getProspects()!=null){
			jz_site = app.getProspects().get(0).getSiteValue();
			jz_branch = app.getProspects().get(0).getBranch();
			jz_number = app.getProspects().get(0).getNumber();
			jz_type = app.getProspects().get(0).getTypeItem()+"";
			verify_person = app.getProspects().get(0).getVerify1()+"";
			verify_branch = app.getProspects().get(0).getVerify2()+"";
			explain_person = app.getProspects().get(0).getExplain1();
			explain_branch = app.getProspects().get(0).getExplain2();
			jz_noto = app.getProspects().get(0).getNoto();
		}
		
		// ***************移站勘察********************k//
		String yz_site = "";
		String yz_branch="";
		String yz_number = "";
		String yz_type = "";
		String yz_verify_person ="";
		String yz_verify_branch="";
		String yz_explain_person = "";
		String yz_explain_branch ="";
		String yz_noto="";
		if(app.getMoveProspects()!=null){
			yz_site = app.getMoveProspects().get(0).getSiteValue();
			yz_branch = app.getMoveProspects().get(0).getBranch();
			yz_number = app.getMoveProspects().get(0).getNumber();
			yz_type = app.getMoveProspects().get(0).getTypeItem()+"";
			yz_verify_person = app.getMoveProspects().get(0).getVerify1()+"";
			yz_verify_branch = app.getMoveProspects().get(0).getVerify2()+"";
			yz_explain_person = app.getMoveProspects().get(0).getExplain1();
			yz_explain_branch = app.getMoveProspects().get(0).getExplain2();
			yz_noto = app.getMoveProspects().get(0).getNoto();
		}
		
		// ***************验收勘察********************k//
		String ys_site = "";
		String ys_branch="";
		String ys_number = "";
		String ys_type = "";
		String ys_verify_person ="";
		String ys_verify_branch="";
		String ys_explain_person = "";
		String ys_explain_branch ="";
		String ys_noto="";
		if(app.getTestProspects()!=null){
			ys_site = app.getTestProspects().get(0).getSiteValue();
			ys_branch = app.getTestProspects().get(0).getBranch();
			ys_number = app.getTestProspects().get(0).getNumber();
			ys_type = app.getTestProspects().get(0).getTypeItem()+"";
			ys_verify_person = app.getTestProspects().get(0).getVerify1()+"";
			ys_verify_branch = app.getTestProspects().get(0).getVerify2()+"";
			ys_explain_person = app.getTestProspects().get(0).getExplain1();
			ys_explain_branch = app.getTestProspects().get(0).getExplain2();
			ys_noto = app.getTestProspects().get(0).getNoto();
		}

		try {

			if (app.getState().equals("0")) {
				url = HttpUrl.URL
						+ HttpUrl.Cr_Service
						+ "?u_id="
						+ u_id
						+ "&cid="
						+ c_id
						+ "&wdbh="
						+ app.getWdbh()
						+ "&fwdh="
						+ app.getFwdh()
						+ "&date="
						+ app.getFwrq()
						+ "&state="
						+ app.getState()
						+ "&options="
						+ app.getKfx()
						// 网点违规
						+ "&violations_index="
						+ violations_index
						+ "&violations="
						+ URLEncoder.encode(violations.toString(), "gb2312")
						+ "&violations_details="
						+ URLEncoder.encode(violations_details.toString(),
								"gb2312")
						// 培训
						+ "&trainThemeId="
						+ _trainThemeId
						+ "&trainObject="
						+ URLEncoder.encode(trainObject, "gb2312")
						+ "&remarks="
						+ URLEncoder.encode(remarks, "gb2312")
						// 会议
						+ "&meetingTheme="
						+ URLEncoder.encode(meetingTheme, "gb2312")
						+ "&meetingName="
						+ URLEncoder.encode(meetingName, "gb2312")
						+ "&meetingDetails="
						+ URLEncoder.encode(meetingDetails, "gb2312")
						+ "&meetingNoto="
						+ URLEncoder.encode(meetingNoto, "gb2312")
						// 宣传促销
						+ "&publicityItem="
						+ URLEncoder.encode(publicityItem, "gb2312")
						+ "&publicityNoto="
						+ URLEncoder.encode(publicityNoto, "gb2312")
						//资料派发
						+"&dataValue="
						+ URLEncoder.encode(dataValue.toString(), "gb2312")
						+"&dataNumber="
						+ URLEncoder.encode(dataNumber.toString(), "gb2312")
						+"&dataNoto="
						+ URLEncoder.encode(dataNoto, "gb2312")
						//其它服务
						+"&otherNumber="
						+ URLEncoder.encode(otherNumber, "gb2312")
						+"&otherName="
						+ URLEncoder.encode(otherName, "gb2312")
						+"&otherNoto="
						+ URLEncoder.encode(otherNoto, "gb2312")
						+"&otherDetails="
						+ URLEncoder.encode(otherDetails, "gb2312")
						//终端维护
						+"&zd_category="
						+ URLEncoder.encode(zd_category, "gb2312")
						+"&zd_model="
						+ URLEncoder.encode(zd_model, "gb2312")
						+"&zd_number="
						+ URLEncoder.encode(zd_number, "gb2312")
						+"&zd_fault="
						+ URLEncoder.encode(zd_fault, "gb2312")
						+"&zd_processMode="
						+ URLEncoder.encode(zd_processMode, "gb2312")
						+"&zd_name="
						+ URLEncoder.encode(zd_name.toString(), "gb2312")
						+"&zd_partNumber="
						+ URLEncoder.encode(zd_partNumber.toString(), "gb2312")
						//建站勘察
						+"&jz_site="
						+ URLEncoder.encode(jz_site, "gb2312")
						+"&jz_branch="
						+ URLEncoder.encode(jz_branch, "gb2312")
						+"&jz_number="
						+ URLEncoder.encode(jz_number, "gb2312")
						+"&jz_type="
						+ URLEncoder.encode(jz_type, "gb2312")
						+"&verify_person="
						+ URLEncoder.encode(verify_person, "gb2312")
						+"&verify_branch="
						+ URLEncoder.encode(verify_branch, "gb2312")
						+"&explain_person="
						+ URLEncoder.encode(explain_person, "gb2312")
						+"&explain_branch="
						+ URLEncoder.encode(explain_branch, "gb2312")
						+"&jz_noto="
						+ URLEncoder.encode(jz_noto, "gb2312")
						//移站勘察
						+"&yz_site="
						+ URLEncoder.encode(yz_site, "gb2312")
						+"&yz_branch="
						+ URLEncoder.encode(yz_branch, "gb2312")
						+"&yz_number="
						+ URLEncoder.encode(yz_number, "gb2312")
						+"&yz_type="
						+ URLEncoder.encode(yz_type, "gb2312")
						+"&yz_verify_person="
						+ URLEncoder.encode(yz_verify_person, "gb2312")
						+"&yz_verify_branch="
						+ URLEncoder.encode(yz_verify_branch, "gb2312")
						+"&yz_explain_person="
						+ URLEncoder.encode(yz_explain_person, "gb2312")
						+"&yz_explain_branch="
						+ URLEncoder.encode(yz_explain_branch, "gb2312")
						+"&yz_noto="
						+ URLEncoder.encode(yz_noto, "gb2312")
						//验收勘察
						+"&ys_site="
						+ URLEncoder.encode(ys_site, "gb2312")
						+"&ys_branch="
						+ URLEncoder.encode(ys_branch, "gb2312")
						+"&ys_number="
						+ URLEncoder.encode(ys_number, "gb2312")
						+"&ys_type="
						+ URLEncoder.encode(ys_type, "gb2312")
						+"&ys_verify_person="
						+ URLEncoder.encode(ys_verify_person, "gb2312")
						+"&ys_verify_branch="
						+ URLEncoder.encode(ys_verify_branch, "gb2312")
						+"&ys_explain_person="
						+ URLEncoder.encode(ys_explain_person, "gb2312")
						+"&ys_explain_branch="
						+ URLEncoder.encode(ys_explain_branch, "gb2312")
						+"&ys_noto="
						+ URLEncoder.encode(ys_noto, "gb2312")
											
						
						+ "&completeDate="
						+ URLEncoder.encode(app.getWcrq(), "gb2312")
						+ "&serviceResult="
						+ URLEncoder.encode(app.getFwpj(), "gb2312")
						+ "&serviceAdvice="
						+ URLEncoder.encode(app.getWdyj(), "gb2312")
						+ "&analysisRS="
						+ URLEncoder.encode(app.getZjfx(), "gb2312")
						+ "&towdAdvice="
						+ URLEncoder.encode(app.getZgyyj(), "gb2312")
						+ "&wdOwner="
						+ URLEncoder.encode(app.getJbr(), "gb2312")
						+ "&submitStatus=" + _submitStatus;
				Log.d("tag", "if_url::::" + url);
			} else {
				url = HttpUrl.URL
						+ HttpUrl.Cr_Service
						+ "?u_id="
						+ u_id
						+ "&cid="
						+ c_id
						+ "&wdbh="
						+ app.getWdbh()
						+ "&fwdh="
						+ app.getFwdh()
						+ "&date="
						+ app.getFwrq()
						+ "&state="
						+ app.getState()
						+ "&violations_index="
						+ violations_index
						+ "&violations="
						+ URLEncoder.encode(violations.toString(), "gb2312")
						+ "&violations_details="
						+ URLEncoder.encode(violations_details.toString(),
								"gb2312")
						+ "&trainThemeId="
						+ _trainThemeId
						+ "&trainObject="
						+ URLEncoder.encode(trainObject, "gb2312")
						+ "&remarks="
						+ URLEncoder.encode(remarks, "gb2312")
						// 会议
						+ "&meetingTheme="
						+ URLEncoder.encode(meetingTheme, "gb2312")
						+ "&meetingName="
						+ URLEncoder.encode(meetingName, "gb2312")
						+ "&meetingDetails="
						+ URLEncoder.encode(meetingDetails, "gb2312")
						+ "&meetingNoto="
						+ URLEncoder.encode(meetingNoto, "gb2312")
						// 宣传促销
						+ "&publicityItem="
						+ URLEncoder.encode(publicityItem, "gb2312")
						+ "&publicityNoto="
						+ URLEncoder.encode(publicityNoto, "gb2312")
						//资料派发
						+"&dataValue="
						+ URLEncoder.encode(dataValue.toString(), "gb2312")
						+"&dataNumber="
						+ URLEncoder.encode(dataNumber.toString(), "gb2312")
						+"&dataNoto="
						+ URLEncoder.encode(dataNoto, "gb2312")	
						//其它服务
						+"&otherNumber="
						+ URLEncoder.encode(otherNumber, "gb2312")
						+"&otherName="
						+ URLEncoder.encode(otherName, "gb2312")
						+"&otherNoto="
						+ URLEncoder.encode(otherNoto, "gb2312")
						+"&otherDetails="
						+ URLEncoder.encode(otherDetails, "gb2312")
						//终端维护
						+"&zd_category="
						+ URLEncoder.encode(zd_category, "gb2312")
						+"&zd_model="
						+ URLEncoder.encode(zd_model, "gb2312")
						+"&zd_number="
						+ URLEncoder.encode(zd_number, "gb2312")
						+"&zd_fault="
						+ URLEncoder.encode(zd_fault, "gb2312")
						+"&zd_processMode="
						+ URLEncoder.encode(zd_processMode, "gb2312")
						+"&zd_name="
						+ URLEncoder.encode(zd_name.toString(), "gb2312")
						+"&zd_partNumber="
						+ URLEncoder.encode(zd_partNumber.toString(), "gb2312")
						//建站勘察
						+"&jz_site="
						+ URLEncoder.encode(jz_site, "gb2312")
						+"&jz_branch="
						+ URLEncoder.encode(jz_branch, "gb2312")
						+"&jz_number="
						+ URLEncoder.encode(jz_number, "gb2312")
						+"&jz_type="
						+ URLEncoder.encode(jz_type, "gb2312")
						+"&verify_person="
						+ URLEncoder.encode(verify_person, "gb2312")
						+"&verify_branch="
						+ URLEncoder.encode(verify_branch, "gb2312")
						+"&explain_person="
						+ URLEncoder.encode(explain_person, "gb2312")
						+"&explain_branch="
						+ URLEncoder.encode(explain_branch, "gb2312")
						+"&jz_noto="
						+ URLEncoder.encode(jz_noto, "gb2312")
							//移站勘察
						+"&yz_site="
						+ URLEncoder.encode(yz_site, "gb2312")
						+"&yz_branch="
						+ URLEncoder.encode(yz_branch, "gb2312")
						+"&yz_number="
						+ URLEncoder.encode(yz_number, "gb2312")
						+"&yz_type="
						+ URLEncoder.encode(yz_type, "gb2312")
						+"&yz_verify_person="
						+ URLEncoder.encode(yz_verify_person, "gb2312")
						+"&yz_verify_branch="
						+ URLEncoder.encode(yz_verify_branch, "gb2312")
						+"&yz_explain_person="
						+ URLEncoder.encode(yz_explain_person, "gb2312")
						+"&yz_explain_branch="
						+ URLEncoder.encode(yz_explain_branch, "gb2312")
						+"&yz_noto="
						+ URLEncoder.encode(yz_noto, "gb2312")
						//验收勘察
						+"&ys_site="
						+ URLEncoder.encode(ys_site, "gb2312")
						+"&ys_branch="
						+ URLEncoder.encode(ys_branch, "gb2312")
						+"&ys_number="
						+ URLEncoder.encode(ys_number, "gb2312")
						+"&ys_type="
						+ URLEncoder.encode(ys_type, "gb2312")
						+"&ys_verify_person="
						+ URLEncoder.encode(ys_verify_person, "gb2312")
						+"&ys_verify_branch="
						+ URLEncoder.encode(ys_verify_branch, "gb2312")
						+"&ys_explain_person="
						+ URLEncoder.encode(ys_explain_person, "gb2312")
						+"&ys_explain_branch="
						+ URLEncoder.encode(ys_explain_branch, "gb2312")
						+"&ys_noto="
						+ URLEncoder.encode(ys_noto, "gb2312")

						+ "&completeDate="
						+ URLEncoder.encode(app.getWcrq(), "gb2312")
						+ "&serviceResult="
						+ URLEncoder.encode(app.getFwpj(), "gb2312")
						+ "&serviceAdvice="
						+ URLEncoder.encode(app.getWdyj(), "gb2312")
						+ "&analysisRS="
						+ URLEncoder.encode(app.getZjfx(), "gb2312")
						+ "&towdAdvice="
						+ URLEncoder.encode(app.getZgyyj(), "gb2312")
						+ "&wdOwner="
						+ URLEncoder.encode(app.getJbr(), "gb2312")
						+ "&submitStatus=" + _submitStatus;
				Log.d("tag", "else_url::::" + url);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File[] files = new File[0];
		if (Util.ExistSDCard()) {
			int size;
			if (_paths == null) {
				size = 0;
			} else {
				size = _paths.size();
			}

			if (iv_fwyb != null && iv_fwyb.getTag() != null
					&& (!iv_fwyb.getTag().toString().equals(""))) {
				files = new File[size + 1];
				files[size] = new File(app.getFwyb());
				Toast.makeText(UslotteryRecord.this,
						"文件大小:" + (files[size].length() / 1024) + "k",
						Toast.LENGTH_SHORT).show();
			} else {
				files = new File[size];
			}
			for (int i = 0; i < size; i++) {
				String path = _paths.get(i);
				files[i] = new File(path);
				Toast.makeText(UslotteryRecord.this,
						"文件大小:" + (files[i].length() / 1024) + "k",
						Toast.LENGTH_SHORT).show();
				Log.d("tag", "path:" + path);
				if (!files[i].exists()) {
					Toast.makeText(UslotteryRecord.this, "文件不存在!",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
		}
		if (files.length <= 0) {
			Toast.makeText(UslotteryRecord.this, "没有上传文件！", Toast.LENGTH_LONG)
					.show();
		}
		upload(url, files);
	}

	/**
	 * 将网点编号传入服务器进行比对。是否是有效的编号
	 * 
	 * @param wdbh
	 */
	public void queryData(final String wdbh) {
		new Thread() {

			public void run() {

				if (HttpUtil.checkNet(UslotteryRecord.this)) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("wdNo", wdbh);

					try {
						String post = HttpUtil.postRequest(HttpUrl.URL
								+ HttpUrl.Wd_Status, map);
						Message msg = new Message();
						msg.obj = post;
						msg.what = 2;
						handler.sendMessage(msg);
					} catch (ClientProtocolException e) {
						handler.sendEmptyMessage(-2);
						e.printStackTrace();
					} catch (IOException e) {
						handler.sendEmptyMessage(-2);
						e.printStackTrace();
					} catch (Exception e) {
						handler.sendEmptyMessage(-2);
						e.printStackTrace();
					}
				} else {
					handler.sendEmptyMessage(-1);
				}
			}
		}.start();
	}


}