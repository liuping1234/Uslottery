package com.uslotter;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.db.AppService;
import com.uslotter.mode.App;
import com.uslotter.util.DialogUtil;
import com.uslotter.util.HttpConnect;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;
import com.uslotter.util.Util;

public class UnRecordFormActivity extends Activity {
	private ListView lv = null;
	private LinearLayout tl = null;
	private UnRecordFormAdapter ca = null;
	public List<Integer> isSelected;
	private List<App> apps = null;
	private AppService appService = null;
	private Button exit, save;
	private int cur_position;
	ProgressDialog pd = null;
	int _submitStatus;
	Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
		int what = msg.what;
		if (msg.what == 1) {
		int curP = msg.arg1;
		String str = msg.obj.toString();
		JSONArray arr;
		try {
		  arr = new JSONArray(str);
		  JSONObject obj = arr.getJSONObject(0);
		  String _msg = obj.getString("Msg");
		  if (_msg.equals("1") || _msg.equals("2")|| _msg.equals("3")) {
			
			   if (curP < (isSelected.size() - 1)) {
				if (_msg.equals("1")) {	
				  
				    Toast.makeText(UnRecordFormActivity.this,"共" + 
				  isSelected.size() + "条数据，第"+ (curP + 1) + "条上传完毕!",Toast.LENGTH_LONG).show();
				} 
				else if (_msg.equals("2")) {
				 
				    DialogUtil.showDialog(UnRecordFormActivity.this, "共"+ 
				      isSelected.size() + "条数据，"+ apps.get(curP).getWdbh()+ "网点已退站，上传失败!");
				} 
				else if (_msg.equals("3")) {
				
				    DialogUtil.showDialog(UnRecordFormActivity.this, "共"+isSelected.size() 
				      + "条数据，"+ apps.get(curP).getFwdh()+ "服务单号已存在，不能重复上传!");
				}				
				appService.deleteApp(apps.get(curP).getId());
				curP++;
				commit(curP);
			} else {
				if (_msg.equals("1")) {							
				   
				    Toast.makeText(UnRecordFormActivity.this,"共" + isSelected.size() 
				        + "条数据，第"+ (curP + 1) + "条上传完毕!",Toast.LENGTH_LONG).show();
				}
				else if (_msg.equals("2")) {
				   
				    DialogUtil.showDialog(UnRecordFormActivity.this, "共"+ 
					 isSelected.size() + "条数据，"+ apps.get(curP).getWdbh()+ "网点已退站，上传失败!");
								
				} 
				else if (_msg.equals("3")) {
				    
				    DialogUtil.showDialog(UnRecordFormActivity.this, "共"+ isSelected.size() 
					   + "条数据，"+ apps.get(curP).getFwdh()+ "服务单号已存在，不能重复上传!");								
				}
				appService.deleteApp(apps.get(curP).getId());
				if (isSelected.size() <= 0) {
					
				    return;
				}
				List<App> list = new ArrayList<App>();
				for (int i = 0; i < isSelected.size(); i++) {
					int po = isSelected.get(i);
					list.add(apps.get(po));
				}
				isSelected.clear();
				apps.removeAll(list);
				pd.cancel();
				DialogUtil.showDialog(UnRecordFormActivity.this,"全部完毕!");ca.notifyDataSetChanged();
			}
		} else {
			pd.cancel();
			isSelected.clear();
			Toast.makeText(UnRecordFormActivity.this,"上传出现问题!" + str, Toast.LENGTH_LONG).show();
		}
	    } catch (JSONException e) {
			pd.cancel();
			isSelected.clear();
			Toast.makeText(UnRecordFormActivity.this, "出现异常!稍后再试!",Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
		} 
		else if (what == -2) {
			pd.cancel();
			Toast.makeText(UnRecordFormActivity.this, "网络连接出现问题!",Toast.LENGTH_LONG).show();
			} 
		else if (what == -1) {
			pd.cancel();
			Toast.makeText(UnRecordFormActivity.this, "出现异常!",Toast.LENGTH_LONG).show();
			}
		}
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.requestWindowFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setContentView(R.layout.unrecordedform);

		isSelected = new ArrayList<Integer>();
		lv = (ListView) this.findViewById(R.id.unrecordedform_lv);
		exit = (Button) this.findViewById(R.id.unrecordedform_back);
		save = (Button) this.findViewById(R.id.unrecordedform_save);
		exit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UnRecordFormActivity.this,
						UslotteryRecord.class);
				startActivity(intent);
				UnRecordFormActivity.this.overridePendingTransition(
						R.anim.push_right_in, R.anim.push_right_out);
				finish();
			}
		});
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isSelected.size() <= 0) {
					return;
				}
			new AlertDialog.Builder(UnRecordFormActivity.this)
				.setTitle("提示")
				.setMessage("保存并提交业务还是保存业务？")
				.setPositiveButton("保存并提交",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which) {
							pd = ProgressDialog.show(UnRecordFormActivity.this,
								"提示", "正在与服务器交互，请稍等...", true,true);
							pd.show();
							_submitStatus = 5;
							commit(0);
						}
				})
				.setNegativeButton("保存",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,int which) {
							pd = ProgressDialog.show(UnRecordFormActivity.this,
								"提示", "正在与服务器交互，请稍等...", true,true);
							pd.show();
							_submitStatus = 1;
							commit(0);
						}
				}).create().show();

			}
		});

		appService = new AppService(this);
		String userId = Util.getSharedPrefercencesString(this, "username");
		apps = appService.getAllAppsFromUserId(userId);
		ca = new UnRecordFormAdapter(this, apps);
		lv.setAdapter(ca);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View view, int position,
					long index) {
				if (position != 0) {
					Intent intent = new Intent(UnRecordFormActivity.this,
							UslotteryRecord_UpdateActivity.class);
					intent.putExtra("app", apps.get(position - 1));
					startActivity(intent);					
					UnRecordFormActivity.this.overridePendingTransition(
							R.anim.push_left_in, R.anim.push_left_out);
					finish();
				}
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long index) {
				final int p = position;
				new AlertDialog.Builder(UnRecordFormActivity.this)
						.setTitle("温馨提示")
						.setMessage("确定删除此条记录?")
						.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									if (p != 0) {
									appService.deleteApp(apps.get(p - 1).getId());
									// 由于全部要更新,不是删除isSelected的某一个值,删除全部
									isSelected.clear();											
									apps.remove(p - 1);
									ca.notifyDataSetChanged();
									}
								}
							}).setNegativeButton("不删除", null).create()
						.show();
				return false;
			}
		});
	}

	protected void commit(int curPosition) {
		
		if (isSelected.size() == 0) {
			return;
		}
		if (curPosition < 0 || curPosition >= isSelected.size()) {
			return;
		}
		if (apps.size() <= 0) {
			return;
		}
		App app = apps.get(isSelected.get(curPosition));
		String uid = Util.getSharedPrefercencesString(UnRecordFormActivity.this, "uid");
		String c_id = Util.getSharedPrefercencesString(UnRecordFormActivity.this, "cid");
		String[] date_time = app.getFwrq().split(",");
		String time = null, date = null;
		if (date_time.length == 2) {
			date = date_time[0];
			time = date_time[1];
		}
		String url = "";
		try {
			if (app.getState().equals("0")) {
				if (app.getNetwork_satus().trim().equals("1")) {
					url = HttpUrl.URL + HttpUrl.Cr_Service + "?u_id=" + uid
						+ "&cid=" + c_id + "&wdbh=" + app.getWdbh()
						+ "&fwdh=" + app.getFwdh() + "&date=" + date + ","
						+ time + "&state=" + app.getState() + "&options="
						+ app.getKfx() + "&completeDate="
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
						+ "&submitStatus=1";
					
				} else {
					url = HttpUrl.URL + HttpUrl.Cr_Service + "?u_id=" + uid
						+ "&cid=" + c_id + "&wdbh=" + app.getWdbh()
						+ "&fwdh=" + app.getFwdh() + "&date=" + date + ","
						+ time + "&state=" + app.getState() + "&options="
						+ app.getKfx() + "&completeDate="
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
				}
			} else {
				if (app.getNetwork_satus().trim().equals("1")) {
					url = HttpUrl.URL + HttpUrl.Cr_Service + "?u_id=" + uid
						+ "&cid=" + c_id + "&wdbh=" + app.getWdbh()
						+ "&fwdh=" + app.getFwdh() + "&date=" + date + ","
						+ time + "&state=" + app.getState()
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
						+ "&submitStatus=1";
					

				} else {
					url = HttpUrl.URL + HttpUrl.Cr_Service + "?u_id=" + uid
						+ "&cid=" + c_id + "&wdbh=" + app.getWdbh()
						+ "&fwdh=" + app.getFwdh() + "&date=" + date + ","
						+ time + "&state=" + app.getState()
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
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		File[] files = new File[0];
		int size = 0;
		if (!app.getFwyb().trim().equals("")) {
			size = size + 1;
		}

		String paths = app.getWdzp().trim();
		if (!paths.equals("")) {
			if (paths.contains(",")) {
				String[] _paths = paths.split(",");
				size = size + _paths.length;
			} else {
				// 只有一条
				size = size + 1;
			}
		}

		files = new File[size];
		if (!paths.equals("")) {
			if (paths.contains(",")) {
				String[] _paths = paths.split(",");
				for (int j = 0; j < _paths.length; j++) {
					files[j] = new File(_paths[j]);
			Toast.makeText(this,"文件大小:" + (files[j].length() / 1024) + "k",Toast.LENGTH_SHORT).show();
				}
			} 
			else {
				// 只有一条
				files[0] = new File(paths);
			Toast.makeText(this,"文件大小:" + (files[0].length() / 1024) + "k",Toast.LENGTH_SHORT).show();
			}
		}
		if (!app.getFwyb().trim().equals("")) {
			files[size - 1] = new File(app.getFwyb().trim());
			Toast.makeText(this,"文件大小:" + (files[size - 1].length() / 1024) + "k",Toast.LENGTH_SHORT).show();
		}
		Toast.makeText(this, "上传文件数量:" + files.length, Toast.LENGTH_SHORT)
				.show();
		upload(url, files, curPosition);
	}

	public void upload(final String url, final File[] files, final int curP) {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				try {
					if (HttpUtil.checkNet(UnRecordFormActivity.this)) {
						String result = HttpConnect.uploadFile1(url, files);
						Message msg = handler.obtainMessage();
						msg.obj = result;
						msg.what = 1;
						msg.arg1 = curP;
						handler.sendMessage(msg);
					} else {
						handler.sendEmptyMessage(-2);
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(-1);
					e.printStackTrace();
				}
				Looper.loop();
			}

		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(UnRecordFormActivity.this,UslotteryRecord.class);
			startActivity(intent);
			UnRecordFormActivity.this.overridePendingTransition(
					R.anim.push_right_in, R.anim.push_right_out);

			finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	class UnRecordFormAdapter extends BaseAdapter {
		public TextView message_list;
		private LayoutInflater inflater;
		private Context context;
		private List<App> apps = null;

		@Override
		public int getCount() {
			return apps.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public UnRecordFormAdapter(Context context, List<App> apps) {
			this.context = context;
			this.inflater = LayoutInflater.from(context);
			this.apps = apps;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (position == 0) {
				convertView = inflater.inflate(R.layout.unrecordedform_item0,
						null);
			} else {
				convertView = inflater.inflate(R.layout.unrecordedform_item,
						null);
			}

			final int p = position;
			if (position != 0) {
				final CheckBox cb = (CheckBox) convertView
						.findViewById(R.id.unrecored_item_cb);
				TextView wdbh = (TextView) convertView
						.findViewById(R.id.unrecored_item_wdbh);
				TextView fwdh = (TextView) convertView
						.findViewById(R.id.unrecored_item_fwdh);
				TextView time1 = (TextView) convertView
						.findViewById(R.id.unrecored_item_time1);
				TextView time2 = (TextView) convertView
						.findViewById(R.id.unrecored_item_time2);

				cb.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isSelected.contains(new Integer(p - 1))) {// 取消
							isSelected.remove(new Integer(p - 1));
							cb.setChecked(false);
						} else {
							isSelected.add(new Integer(p - 1));// 选中
							cb.setChecked(true);
						}
					}
				});

				wdbh.setText(apps.get(position - 1).getWdbh());
				fwdh.setText(apps.get(position - 1).getFwdh());
				String fwrq = apps.get(position - 1).getFwrq();
				if (fwrq.contains(",")) {
					time1.setText(fwrq.substring(0, fwrq.indexOf(",")));
				}
			}
			return convertView;
		}
	}
}