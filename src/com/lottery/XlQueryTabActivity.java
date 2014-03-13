package com.lottery;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.UslotteryMainMenu2;
import com.uslotter.util.HttpUtil;

public class XlQueryTabActivity extends TabActivity {
	private String queryTmpDate = "";
	private String cityName = "全省";
	private String cityNameArray[] = { "全省", "广州", "深圳", "珠海", "汕头", "佛山",
			"韶关", "湛江", "肇庆", "江门", "茂名", "惠州", "梅州", "汕尾", "河源", "阳江", "清远",
			"东莞", "中山", "潮州", "揭阳", "云浮", "顺德" };
	private String cityCidArray[] = { "", "01,23,27", "02,26", "03", "04",
			"05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
			"16", "17,28", "18", "19", "20", "21", "22" };
	private int position = 0;
	private String cid = "";
	private SharedPreferences preferences;
	TabHost tabHost = null;
	LinearLayout ll = null;
	private Button btn_exit = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.requestWindowFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setContentView(R.layout.query_xl_dialog);
		preferences = getSharedPreferences("lotteryConfig", MODE_WORLD_READABLE);
		// cid=getIntent().getStringExtra("cid");
		cid = preferences.getString("cid", "23");
		cityName = preferences.getString("cityName", "全省");
		// tabHost = this.getTabHost();
		tabHost = (TabHost) this.findViewById(android.R.id.tabhost);
		btn_exit = (Button) this.findViewById(R.id.queryxl_dialog_back);
		btn_exit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView tabView1 = new TextView(this);
		tabView1.setText("网点销量");
		tabView1.setHeight(30);
		tabView1.setPadding(50, 10, 5, 5);
		tabView1.setTextSize(20);

		// 添加第一个标签页
		TabSpec tab1 = tabHost.newTabSpec("tab1");
		tab1.setIndicator("网点销量");
		tab1.setContent(R.id.tab01);

		tabView1 = new TextView(this);
		tabView1.setText("中心销量");
		tabView1.setHeight(30);
		tabView1.setTextSize(20);
		// 添加第二个标签页
		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator("中心销量").setContent(R.id.tab02));
		tabHost.addTab(tab1);
		// queryWdxlDialog();
		queryCityxlDialog();
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				// new
				// AlertDialog.Builder(XlQueryTabActivity.this).setTitle("Hint").setMessage(tabId).setPositiveButton("OK",
				// null).show();
				if (tabId.equals("tab1")) {
					queryWdxlDialog();
				} else if (tabId.equals("tab2")) {
					queryCityxlDialog();
				}
			}
		});
	}

	/**
	 * 查询网点销量表单
	 */
	private void queryWdxlDialog() {
		// TabWidget tab=getTabWidget ();
		// tab.findViewById(id)
		final EditText titleText = (EditText) findViewById(R.id.title);
		final TextView queryDate = (TextView) findViewById(R.id.queryDate);
		DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -1);
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH);
		int d = c.get(Calendar.DAY_OF_MONTH);

		setQueryDate(queryDate, y, m, d);
		datePicker.init(y, m, d, new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker arg0, int year, int month,
					int day) {
				setQueryDate(queryDate, year, month, day);
			}
		});

		Button btn2 = (Button) findViewById(R.id.xlbtn2);
		btn2.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String title = titleText.getText().toString();
				if (title == null || title.equals("")) {
					Toast.makeText(XlQueryTabActivity.this, "请输入查询网点编号!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (title.length() != 5) {
					Toast.makeText(XlQueryTabActivity.this, "请输入正确的网点编号!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (HttpUtil.checkNet(XlQueryTabActivity.this)) {
					// goDaywdxlActivity();
					goWdxlActivity(title, queryDate.getText().toString());
				} else {
					Toast.makeText(XlQueryTabActivity.this,
							"网络不能使用，请确认网络是否打开？", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * 查询中心销量表单
	 */
	private void queryCityxlDialog() {
		// TabWidget tab=getTabWidget ();
		tabHost = (TabHost) this.findViewById(android.R.id.tabhost);

		// tabHost = getTabHost();
		FrameLayout tab = tabHost.getTabContentView();
		position = Integer.parseInt(cid);
		position = position == 23 ? 0 : position;
		// cid=cityCidArray[position];
		final TextView cityText = (TextView) tab.findViewById(R.id.cityName);
		cityName = cityNameArray[position];
		cityText.setText(cityName);
		final TextView queryDate = (TextView) tab.findViewById(R.id.queryDate2);
		DatePicker datePicker = (DatePicker) tab.findViewById(R.id.datePicker2);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -1);
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH);
		int d = c.get(Calendar.DAY_OF_MONTH);

		setQueryDate(queryDate, y, m, d);
		datePicker.init(y, m, d, new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker arg0, int year, int month,
					int day) {
				setQueryDate(queryDate, year, month, day);
			}
		});

		Button btn1 = (Button) tab.findViewById(R.id.xlbtn12);
		btn1.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				findCitys();
			}
		});
		Button btn2 = (Button) tab.findViewById(R.id.xlbtn22);
		btn2.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (HttpUtil.checkNet(XlQueryTabActivity.this)) {
					goCityxlActivity(cityCidArray[position], queryDate
							.getText().toString());
				} else {
					Toast.makeText(XlQueryTabActivity.this,
							"网络不能使用，请确认网络是否打开？", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void setQueryDate(TextView queryDate, int y, int m, int d) {
		String tm = String.valueOf(m + 1);
		String td = String.valueOf(d);
		if (Integer.parseInt(tm) < 10) {
			tm = "0" + tm;
		}
		if (Integer.parseInt(td) < 10) {
			td = "0" + td;
		}
		queryDate.setText(y + "-" + tm + "-" + td);
		queryTmpDate = y + "-" + tm + "-" + td;
	}

	// 通过二维码获取网点编号，查询网点天销量
	// public void goDaywdxlActivity() {
	// Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	// intent.addCategory(LotteryMainActivity.LOTTERY_CATEGORY);
	// startActivityForResult(intent, 1);
	// }

	// 查询网点天销量
	public void goWdxlActivity(String title, String date) {
		Intent intent = new Intent();
		intent.putExtra("title", title);
		intent.putExtra("date", date);
		intent.setAction("android.intent.action.VIEWWDXL");
		startActivity(intent);
	}

	/**
	 * 查询中心天销量
	 * 
	 * @param cid
	 * @param date
	 */
	public void goCityxlActivity(String cid, String date) {
		Intent intent = new Intent();
		intent.putExtra("cid", cid);
		intent.putExtra("cityName", cityName);
		intent.putExtra("date", date);
		intent.setAction("android.intent.action.VIEWCITYXL");
		startActivity(intent);
	}

	/**
	 * 中心列表
	 */
	public void findCitys() {
		Builder b = new AlertDialog.Builder(this);
		// 设置对话框的图标
		b.setIcon(R.drawable.logo);
		// 设置对话框的标题
		b.setTitle("请选择中心");
		// 为对话框设置多个列表
		b.setItems(cityNameArray, new OnClickListener() {
			// 该方法的which参数代表用户单击了那个列表项
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView cityNameText = (TextView) findViewById(R.id.cityName);
				// which代表哪个列表项被单击了
				cityNameText.setText(cityNameArray[which]);
				position = which;
				cityName = cityNameArray[which];
				cid = String.valueOf(position);
				if (position == 0) {
					cid = "23";
				} else if (position < 10) {
					cid = "0" + position;
				}
			}
		});
		// 创建、并显示对话框
		b.create().show();
	}

	public void goLogout() {
		new AlertDialog.Builder(this)
				// .setIcon(R.drawable.logo)
				.setTitle(R.string.dialog_top_title)
				.setMessage(R.string.quit_description)
				.setNegativeButton(R.string.button_cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						})
				.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								finish();
							}
						}).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮 //
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// goLogout();
			Intent intent = new Intent(XlQueryTabActivity.this,
					UslotteryMainMenu2.class);
			XlQueryTabActivity.this.startActivity(intent);
			finish();
			XlQueryTabActivity.this.overridePendingTransition(
					R.anim.push_right_in, R.anim.push_right_out);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// 系统配置
	public void goConfigActivity() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.CONFIG");
		startActivity(intent);
	}
}