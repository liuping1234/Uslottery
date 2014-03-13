package com.uslotter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.hz.HzLotteryActivity;
import com.nfclottery.MainNfcActivity;
import com.sd.everflourish.WelComeSdActivity;
import com.uslotter.db.AppService;
import com.uslotter.db.ObjService;
import com.uslotter.mode.App;
import com.uslotter.util.Config;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;
import com.uslotter.util.UpdateManager;
import com.uslotter.util.Util;

public class UslotteryMainMenu2 extends Activity implements OnClickListener,
		OnGestureListener, OnTouchListener {
	private ProgressDialog pBar = null;
	private List<Map<String, String>> meslist = null;
	private Map<String, String> maps = null;
	private List<String> list_name = null;
	private List<Integer> list_icon = null;
	private String[] names = {
	// "工作查询", "绩效查询",
	"网点信息", };
	private int[] images = new int[] {
	// R.drawable.worklook,
	R.drawable.pointinfo,
	// R.drawable.xiaoji
	};

	private String[] names2 = new String[] { "抽奖" };
	private int[] images2 = new int[] { R.drawable.in };
	private TextView tvName = null;
	private TextView tvWork = null;
	private Button btLogout = null;
	private GridView grid = null;
	private ImageButton ibHome = null;
	private ImageButton ibRecomm = null;
	private ImageButton ibMore = null;
	private TextView tv_unRecord = null;
	private AppService appService = null;
	private Button btMore = null;
	private String UserName = null;// 专管员姓名
	private String UserId = null;// 专管员ID
	private String wdid = null;// 网点编号
	private ListView listView_Message = null;
	private CustomListAdapter adapter = null;
	private ImageView iv1, iv2 = null;
	int errorCode = 0;// 错误判断
	private Map<String, String> messages = new HashMap<String, String>();// 通知列表
	// public static SharedPreferences CardInOut;// 签到签退信息存贮
	public SharedPreferences CardInOut;// 签到签退信息存贮
	private TextView tv = null;
	private LinearLayout ll = null;
	private SimpleAdapter _adapter = null;
	private String userRoles = "";
	private LinearLayout ll_main = null;
	private ObjService objService = null;
	private GestureDetector myGestureDetector = null;// new

	private static boolean isUpdate;
	// GestureDetector(this);
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case -1:
				// 有异常
				Toast.makeText(UslotteryMainMenu2.this, "有异常",
						Toast.LENGTH_SHORT).show();
				break;
			case 0:
				// 没有结果
				tv.setText("暂无通知");
				break;
			case 1:
				// 有结果
				ll.removeView(tv);
				listView_Message.setVisibility(View.VISIBLE);
				adapter = new CustomListAdapter(UslotteryMainMenu2.this);
				listView_Message.setAdapter(adapter);
				// listView_Message.setOnItemClickListener(new
				// AdapterView.OnItemClickListener() {
				// @Override
				// public void onItemClick(AdapterView<?> arg0, View arg1,
				// int position, long index) {
				// maps = meslist.get(position);
				// String id = maps.get("id");
				// queryData(id, 3);
				// }
				// });
				break;
			case 3:
				try {
					String post = msg.obj.toString();
					Log.d("tag", "post:::" + post);
					JSONArray arr = new JSONArray(post);
					JSONObject obj = arr.getJSONObject(0);
					String _msg = obj.getString("Msg");
					if (_msg.equals("1")) {
						String content = obj.getString("content");
						String content_detail = obj.getString("MsgInfo");
						String name = obj.getString("CreateUserName");
						String date = obj.getString("CreateTime");
						Intent intent = new Intent();
						intent.putExtra("id", maps.get("id"));
						intent.putExtra("title", maps.get("title"));
						intent.putExtra("content_detail", content);
						intent.putExtra("content", content_detail);
						intent.putExtra("name", name);
						intent.putExtra("date", date);
						intent.setAction("android.intent.action.NotificationDetailActivity");
						UslotteryMainMenu2.this.startActivity(intent);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			case 4:
				UpdateManager manager = new UpdateManager(
						UslotteryMainMenu2.this);
				manager.checkUpdateInfo();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mainmenu2);

		list_name = new ArrayList<String>();
		list_icon = new ArrayList<Integer>();
		userRoles = Util.getSharedPrefercencesString(this, "userRoles");
		if (userRoles.equals("") || userRoles == null) {
			userRoles = "3";
			// Toast.makeText(this, "权限分配出现问题!", Toast.LENGTH_SHORT).show();
		}
		if (userRoles.contains("3")) {
			list_name.add("签到");
			list_icon.add(R.drawable.in);
			list_name.add("签退");
			list_icon.add(R.drawable.out);
			list_name.add("录单");
			list_icon.add(R.drawable.write_button);
			list_name.add("活动");
			list_icon.add(R.drawable.hd5);
			list_name.add("销量");
			list_icon.add(R.drawable.sales);
		}
		if (userRoles.contains("2")) {
			list_name.add("即开派票");
			list_icon.add(R.drawable.wdinfo);
		}
		if (userRoles.contains("1")) {
			list_name.add("抽奖");
			list_icon.add(R.drawable.in);
		}

		for (int i = 0; i < names.length; i++) {
			list_name.add(names[i]);
			list_icon.add(images[i]);
		}

		tv = (TextView) this.findViewById(R.id.listtv2);
		ll = (LinearLayout) this.findViewById(R.id.lil12);
		meslist = new ArrayList<Map<String, String>>();
		iv1 = (ImageView) this.findViewById(R.id.mainmenu2_iv1);
		// iv1.setBackgroundResource(R.drawable.guide_dot_white);
		iv2 = (ImageView) this.findViewById(R.id.mainmenu2_iv2);
		CardInOut = getSharedPreferences("CardInOut", MODE_WORLD_WRITEABLE);
		wdid = CardInOut.getString("wdID", "无");
		tvName = (TextView) findViewById(R.id.syzgyname2);
		tvWork = (TextView) findViewById(R.id.workid2);
		tvWork.setText(wdid);
		grid = (GridView) findViewById(R.id.mainmenu2_gridView);

		listView_Message = (ListView) findViewById(R.id.listWdtz2);
		btMore = (Button) findViewById(R.id.btmore2);
		btLogout = (Button) findViewById(R.id.syzhuxiao2);
		ibHome = (ImageButton) findViewById(R.id.syhome2);
		ibRecomm = (ImageButton) findViewById(R.id.syjianjie2);
		ibMore = (ImageButton) findViewById(R.id.symore2);
		tv_unRecord = (TextView) this.findViewById(R.id.mainmenu_tv_count2);
		appService = new AppService(this);

		ibHome.setOnClickListener(this);
		ibRecomm.setOnClickListener(this);
		ibMore.setOnClickListener(this);
		btLogout.setOnClickListener(this);
		btMore.setOnClickListener(this);
		tvName.setText(Util.getSharedPrefercencesString(
				UslotteryMainMenu2.this, "u_name"));

		final List<Map<String, Object>> listItems1 = new ArrayList<Map<String, Object>>();
		if (list_name.size() > 9) {
			iv1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					iv2.setImageResource(R.drawable.guide_dot_white);
					iv1.setImageResource(R.drawable.guide_dot_black);
				}
			});
			iv2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					iv1.setImageResource(R.drawable.guide_dot_white);
					iv2.setImageResource(R.drawable.guide_dot_black);
					Intent intent = new Intent(UslotteryMainMenu2.this,
							UslotteryMainMenu3.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
					finish();
				}
			});
		} else {
			iv1.setVisibility(View.GONE);
			iv2.setVisibility(View.GONE);
		}
		int size = (list_name.size() > 9) ? 9 : list_name.size();
		for (int i = 0; i < size; i++) {
			Map<String, Object> listItem1 = new HashMap<String, Object>();
			listItem1.put("name1", list_name.get(i));
			listItem1.put("logo1", list_icon.get(i));
			listItems1.add(listItem1);
		}
		_adapter = new SimpleAdapter(this, listItems1, R.layout.cell1,
				new String[] { "name1", "logo1" }, new int[] { R.id.name1,
						R.id.logo1 });

		grid.setAdapter(_adapter);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onItemClick1(position);
			}
		});
		myGestureDetector = new GestureDetector(this);
		grid.setOnTouchListener(this);
		grid.setLongClickable(true);
		queryData();
		if (!isUpdate) {
			new Thread() {// 检查更新
				public void run() {
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					checkUpdate();
				}
			}.start();

		}
		isUpdate = true;

	}

	public void onItemClick1(int position) {
		String key = list_name.get(position);
		if (key.equals("工作查询")) {
			Toast.makeText(UslotteryMainMenu2.this, "暂未开通，敬请期待",
					Toast.LENGTH_SHORT).show();
		} else if (key.equals("网点信息")) {
			Intent intentwdinfo = new Intent();
			intentwdinfo.setAction("android.intent.action.SelectByIDActivity");
			startActivity(intentwdinfo);
			UslotteryMainMenu2.this.overridePendingTransition(
					R.anim.push_left_in, R.anim.push_left_out);

		} else if (key.equals("绩效查询")) {
			Toast.makeText(UslotteryMainMenu2.this, "暂未开通，敬请期待",
					Toast.LENGTH_SHORT).show();
		} else if (key.equals("销量")) {
			if (userRoles.contains("3")) {
				Intent intent7 = new Intent();
				String cid = Util.getSharedPrefercencesString(
						UslotteryMainMenu2.this, "cid");
				intent7.putExtra("cid", cid);
				intent7.setAction("android.intent.action.VIEWQUERY");
				startActivity(intent7);
				UslotteryMainMenu2.this.overridePendingTransition(
						R.anim.push_left_in, R.anim.push_left_out);
			} else {
				Toast.makeText(UslotteryMainMenu2.this, "没有权限查看!",
						Toast.LENGTH_SHORT).show();
			}
		} else if (key.equals("抽奖")) {
			if (userRoles.contains("1")) {
				Intent intent0 = new Intent(UslotteryMainMenu2.this,
						WelComeSdActivity.class);
				startActivity(intent0);
				UslotteryMainMenu2.this.overridePendingTransition(
						R.anim.push_left_in, R.anim.push_left_out);
			} else {
				Toast.makeText(UslotteryMainMenu2.this, "没有权限查看!",
						Toast.LENGTH_SHORT).show();
			}

		} else if (key.equals("即开派票")) {
			if (userRoles.contains("2")) {
				Intent intent6 = new Intent(UslotteryMainMenu2.this,
						MainNfcActivity.class);
				startActivity(intent6);
				UslotteryMainMenu2.this.overridePendingTransition(
						R.anim.push_left_in, R.anim.push_left_out);
			} else {
				Toast.makeText(UslotteryMainMenu2.this, "没有权限查看!",
						Toast.LENGTH_SHORT).show();
			}
		} else if (key.equals("签到")) {
			if (userRoles.contains("3")) {
				Intent intentin = new Intent();
				intentin.setAction("android.intent.action.UslotteryCardin");
				startActivity(intentin);
				UslotteryMainMenu2.this.overridePendingTransition(
						R.anim.push_left_in, R.anim.push_left_out);
				finish();
			} else {
				Toast.makeText(UslotteryMainMenu2.this, "没有权限查看!",
						Toast.LENGTH_SHORT).show();
			}

		} else if (key.equals("签退")) {
			if (userRoles.contains("3")) {
				Intent intentin = new Intent();
				intentin.setAction("android.intent.action.UslotteryCardout");
				startActivity(intentin);
				UslotteryMainMenu2.this.overridePendingTransition(
						R.anim.push_left_in, R.anim.push_left_out);
				finish();
			} else {
				Toast.makeText(UslotteryMainMenu2.this, "没有权限查看!",
						Toast.LENGTH_SHORT).show();
			}
		} else if (key.equals("录单")) {
			if (userRoles.contains("3")) {
				Intent intentrecord2 = new Intent();
				intentrecord2
						.setAction("android.intent.action.UslotteryRecord");
				startActivity(intentrecord2);
				UslotteryMainMenu2.this.overridePendingTransition(
						R.anim.push_left_in, R.anim.push_left_out);
			} else {
				Toast.makeText(UslotteryMainMenu2.this, "没有权限查看!",
						Toast.LENGTH_SHORT).show();
			}

		} else if (key.equals("活动")) {
			if (userRoles.contains("3")) {
				Intent intent = new Intent(UslotteryMainMenu2.this,
						HzLotteryActivity.class);
				startActivity(intent);
				UslotteryMainMenu2.this.overridePendingTransition(
						R.anim.push_left_in, R.anim.push_left_out);

			} else {
				Toast.makeText(UslotteryMainMenu2.this, "没有权限查看!",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onStart() {
		if (userRoles.contains("3")) {
			String userId = Util.getSharedPrefercencesString(this, "username");
			List<App> lists = appService.getAllAppsFromUserId(userId);
			if (lists.size() > 0) {
				tv_unRecord.setText(lists.size() + "");
			}
		}
		super.onStart();
	}

	/**
	 * 启动线程检查是否有新版本
	 * 
	 */
	private void checkUpdate() {
		if (HttpUtil.checkNet(this)) {
			if (Config.hasNewVersion(this)) {

				handler.sendEmptyMessage(4);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮 //
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (wdid.equals("无")) {
				new AlertDialog.Builder(UslotteryMainMenu2.this)
						.setTitle("提示！")
						.setMessage("退出程序")
						.setCancelable(false)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										Intent intent = new Intent(
												Intent.ACTION_MAIN);
										intent.addCategory(Intent.CATEGORY_HOME);
										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(intent);
										UslotteryMainMenu2.this.finish();
										System.exit(0);
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
									}
								}).create().show();
			} else {
				new AlertDialog.Builder(UslotteryMainMenu2.this)
						.setTitle("提示！")
						.setMessage("未签退，请签退后再退出！")
						.setCancelable(false)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
									}
								}).create().show();
			}
			return true;

		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	// 主菜单通知列表适配器
	class CustomListAdapter extends BaseAdapter {
		public TextView message_list;
		private LayoutInflater inflater;
		private List<Map<String, String>> data;
		private Context context;

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public CustomListAdapter(Context context) {
			this.context = context;
			this.inflater = LayoutInflater.from(context);
			data = getData();
			// Log.i("tag", "...."+data);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.message_item, null);
			}
			message_list = (TextView) findViewById(R.id.message_list);
			String mess = data.get(position).get("title");
			String type = data.get(position).get("type");
			((TextView) convertView.findViewById(R.id.message_list))
					.setText(mess);
			((TextView) convertView.findViewById(R.id.button)).setText(type);
			return convertView;
		}

		public List<Map<String, String>> getData() {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			/*
			 * Map<String, String> map = new HashMap<String, String>();
			 * map.put("title", "暂未开通···"); map.put("type", ""); list.add(map);
			 */
			if (HttpUtil.checkNet(UslotteryMainMenu2.this)) {
				if (meslist == null || meslist.size() < 1) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("title", "当月份没有通知···");
					map.put("type", "");
					list.add(map);
				} else {
					if (list.size() < 4) {
						list = meslist;
					} else {
						list = meslist.subList(0, 4);
					}
				}
			} else {
				Map<String, String> map = new HashMap<String, String>();
				map.put("title", "网络连接失败···");
				map.put("type", "");
				list.add(map);
			}
			return list;
		}
	}

	public void queryData() {
		new Thread() {
			public void run() {
				if (HttpUtil.checkNet(UslotteryMainMenu2.this)) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("u_id", Util.getSharedPrefercencesString(
							UslotteryMainMenu2.this, "uid"));
					map.put("page", "");
					map.put("limit", "3");
					try {
						String post = HttpUtil.post(HttpUrl.URL
								+ HttpUrl.NoticeQuery, map);
						System.out.println("=====================" + post);
						JSONArray arr = new JSONArray(post);
						JSONArray obj2 = arr.getJSONArray(0);
						JSONObject obj = obj2.getJSONObject(0);
						String msg = obj.getString("Msg");
						Log.i("tag", msg);
						if (msg.equals("1")) {
							int len = obj2.length();
							for (int i = 0; i < len; i++) {
								Map<String, String> mesmap = new HashMap<String, String>();
								JSONObject obj1 = obj2.getJSONObject(i);
								int id = obj1.getInt("id");// 通知编号
								String title = obj1.getString("title");// 通知标题
								String type = obj1.getString("T_type");// 通知类型

								mesmap.put("id", id + "");
								mesmap.put("title", title);
								mesmap.put("type", type);
								meslist.add(mesmap);
							}
							handler.sendEmptyMessage(1);
						} else if (msg.equals("0")) {
							handler.sendEmptyMessage(0);
						} else if (msg.equals("-1")) {
							handler.sendEmptyMessage(-1);
						}
//					} catch (ClientProtocolException e) {
//						e.printStackTrace();
//					} catch (IOException e) {
//						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					handler.sendEmptyMessage(-1);
				}
			}
		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.syzhuxiao2:
			new AlertDialog.Builder(UslotteryMainMenu2.this)
					.setTitle("提示！")
					.setMessage("注销登录")
					.setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// WelcomeActivity.Login.edit().clear().commit();
									SharedPreferences sp = getSharedPreferences(
											"Login", MODE_WORLD_WRITEABLE);
									sp.edit().clear().commit();
									Intent intent = new Intent();
									intent.putExtra("isLogin", 2);
									intent.setAction("android.intent.action.UslotteryActivity");
									startActivity(intent);
									UslotteryMainMenu2.this
											.overridePendingTransition(
													R.anim.push_right_in,
													R.anim.push_right_out);

									finish();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
								}
							}).create().show();
			break;
		case R.id.btmore2:
			// Toast.makeText(this, "尚未开通，敬请期待", Toast.LENGTH_SHORT).show();
			if (meslist.size() <= 0) {
				Toast.makeText(this, "暂无通知", Toast.LENGTH_SHORT).show();
			} else {
				if (HttpUtil.checkNet(UslotteryMainMenu2.this)) {
					Intent intent1 = new Intent();
					intent1.setAction("android.intent.action.MessageInfo");
					startActivity(intent1);
					UslotteryMainMenu2.this.overridePendingTransition(
							R.anim.push_left_in, R.anim.push_left_out);
				} else {
					Toast.makeText(this, "没有可用网络", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.syhome2:
			// 主页
			Intent intent = new Intent();
			intent.setClass(this, UslotteryMainMenu2.class);
			startActivity(intent);
			UslotteryMainMenu2.this.overridePendingTransition(
					R.anim.push_left_in, R.anim.push_left_out);

			finish();
			break;
		case R.id.syjianjie2:
			// 查询
			Intent intentwdinfo = new Intent();
			intentwdinfo.setAction("android.intent.action.SelectByIDActivity");
			startActivity(intentwdinfo);
			UslotteryMainMenu2.this.overridePendingTransition(
					R.anim.push_left_in, R.anim.push_left_out);

			break;
		case R.id.symore2:
			// 更多
			if (!tv_unRecord.getText().equals("")) {
				if (userRoles.contains("3")) {
					Intent intent_more = new Intent(UslotteryMainMenu2.this,
							UnRecordFormActivity.class);
					startActivity(intent_more);
					UslotteryMainMenu2.this.overridePendingTransition(
							R.anim.push_left_in, R.anim.push_left_out);
				} else {
					Toast.makeText(UslotteryMainMenu2.this, "没有权限查看!",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				if (Config.newVerCode > Config.getVerCode(this)) {
					Log.d("tag", "Config.newVerCode:     " + Config.newVerCode);
					Log.d("tag",
							"Config.getVerCode(this):     "
									+ Config.getVerCode(this));
					new Thread() {
						public void run() {
							checkUpdate();
						};
					}.start();

				} else {
					Toast.makeText(this,
							"广东长荣科技有限公司\n" + "专管员应用 版本" + getVersion(),
							Toast.LENGTH_SHORT).show();
				}

			}
			break;
		default:
			break;
		}
	}

	public void queryData(final String id, final int what) {
		new Thread() {
			public void run() {

				if (HttpUtil.checkNet(UslotteryMainMenu2.this)) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", id);

					try {
						String post = HttpUtil.postRequest(HttpUrl.URL
								+ HttpUrl.NoticeMsg, map);
						Message msg = new Message();
						msg.obj = post;
						msg.what = what;
						handler.sendMessage(msg);
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					handler.sendEmptyMessage(-1);
				}
			}
		}.start();
	}

	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		myGestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return myGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		final int FLING_MIN_DISTANCE = 100;// X或者y轴上移动的距离(像素)

		final int FLING_MIN_VELOCITY = 200;// x或者y轴上的移动速度(像素/秒)
		if ((e1.getX() - e2.getX()) > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			if (list_name.size() > 9) {
				iv1.setImageResource(R.drawable.guide_dot_white);
				iv2.setImageResource(R.drawable.guide_dot_black);
				Intent intent = new Intent(UslotteryMainMenu2.this,
						UslotteryMainMenu3.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
				finish();
			}
		} else if ((e2.getX() - e1.getX()) > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}