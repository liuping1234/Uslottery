package com.lottery;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cr.uslotter.R;
import com.uslotter.util.DialogUtil;
import com.uslotter.util.HttpUtil;

public class CityXlActivity extends BaseActivity {
	final String TAG = "--CityXlActivity--";
	ProgressDialog pBar = null;
	// String
	// gameNameArr[]={"大乐透","排列3","排列5","七星彩","22选5","36选7","胜负彩","半全场","进球彩"};
	final List<String> kindKeyList = new ArrayList<String>();
	final List<String> kindNameList = new ArrayList<String>();
	List<String[]> xlList = null;
	String cid = null;
	String cityName = null;
	String date = null;
	String day = null;
	int errorCode = 0;// 错误判断
	Map<String, String> typeMap = new HashMap<String, String>();
	Map<String, List<String[]>> dataMap = new HashMap<String, List<String[]>>();
	Map<String, Double> hjMap = new HashMap<String, Double>();
	String trw, txl, tbl, tpm;
	String hzArray[] = null;
	double total = 0d;// 合计
	Map<String, Integer> kindGameMap = new HashMap<String, Integer>();;
	NumberFormat formater = new DecimalFormat("###,###.## ");
	private Button btn_exit = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		init();
		cid = getIntent().getStringExtra("cid");
		cityName = getIntent().getStringExtra("cityName");
		date = getIntent().getStringExtra("date");
		day = String.valueOf(this.getDayOfYear(date));
		showProgressDialog(this, "中心销量", "获取数据中，请稍后...",
				ProgressDialog.STYLE_SPINNER);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.requestWindowFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daycityxllist);
		TextView city = (TextView) findViewById(R.id.cityName);
		city.setText(cityName);
		TextView querydate = (TextView) findViewById(R.id.querydate);
		btn_exit = (Button) this.findViewById(R.id.daycityxlist_back);
		btn_exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		querydate.setText(date);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					// showList();
					showList();
				} else {
					if (errorCode == -2) {
						DialogUtil.showDialog(CityXlActivity.this,
								R.string.dialog_top_title,
								R.string.result_xl_error, true);
					} else {
						DialogUtil.showDialog(CityXlActivity.this,
								R.string.dialog_top_title,
								R.string.result_net_error, true);
					}

				}
			};

		};

		// 启动线程来连接网络并读取开奖号码
		new Thread() {
			public void run() {
				boolean flag = findDayCityxlArray(cid, date, day);
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
	}

	private void init() {
		// String kindList[]={"1","3","4","6","7","10"};//{ "概率型", "传统足彩",
		// "单场足彩","即开","竞猜","高频"};
		typeMap.put("1", "概率型");
		typeMap.put("3", "传统足彩");
		typeMap.put("4", "单场足彩");
		typeMap.put("6", "即开");
		typeMap.put("7", "竞猜");
		typeMap.put("10", "高频");
	}

	// 显示销量数据
	private void showList() {

		Button btnpre = (Button) findViewById(R.id.btnpre);
		Button btnnext = (Button) findViewById(R.id.btnnext);
		btnpre.setOnClickListener(new BtnClickLister(-1));
		btnnext.setOnClickListener(new BtnClickLister(1));

		TextView zj = (TextView) findViewById(R.id.zj);
		zj.setText(formater.format(total) + "元");

		TextView rw = (TextView) findViewById(R.id.rw);
		TextView zxl = (TextView) findViewById(R.id.zxl);
		TextView per = (TextView) findViewById(R.id.per);
		if (hzArray != null) {
			NumberFormat fmt = new DecimalFormat("##.## ");
			rw.setText(formater.format(Double.parseDouble(hzArray[0])) + "元");
			zxl.setText(formater.format(Double.parseDouble(hzArray[1])) + "元");
			per.setText(fmt.format(Double.parseDouble(hzArray[2]) * 100) + "%");
		}
		// 创建一个BaseAdapter对象，该对象负责提供Gallery所显示的图片
		// final String[] armTypes = new String[]{ "概率型", "传统足彩",
		// "单场足彩","即开","竞猜","高频"};
		int len = kindKeyList.size();
		final List[] arms = new ArrayList[len];
		for (int i = 0; i < len; i++) {
			String key = kindKeyList.get(i);
			arms[i] = dataMap.get(key);
		}
		ExpandableListAdapter adapter = new BaseExpandableListAdapter() {
			// 获取指定组位置、指定子列表项处的子列表项数据
			@Override
			public Object getChild(int groupPosition, int childPosition) {
				return arms[groupPosition].get(childPosition);
			}

			@Override
			public long getChildId(int groupPosition, int childPosition) {
				return childPosition;
			}

			@Override
			public int getChildrenCount(int groupPosition) {
				return arms[groupPosition].size();
			}

			// 该方法决定每个子选项的外观
			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				// TextView textView = getTextView();
				// textView.setText(getChild(groupPosition,
				// childPosition).toString());

				String[] b = (String[]) getChild(groupPosition, childPosition);
				LinearLayout ll = new LinearLayout(CityXlActivity.this);
				ll.setOrientation(0);
				TableLayout table = new TableLayout(CityXlActivity.this);
				table.setStretchAllColumns(true);
				TableRow row = new TableRow(CityXlActivity.this);
				TextView textVew = new TextView(CityXlActivity.this);
				textVew.setTextSize(15);
				textVew.setText(b[0]);
				textVew.setPadding(55, 5, 10, 0);
				textVew.setTextColor(Color.BLACK);
				row.addView(textVew);
				textVew = new TextView(CityXlActivity.this);
				textVew.setTextSize(15);
				textVew.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
				textVew.setPadding(0, 0, 10, 0);
				textVew.setText(b[1]);
				textVew.setTextColor(Color.BLACK);
				row.addView(textVew);
				table.addView(row);
				ll.addView(table);
				return ll;
			}

			// 获取指定组位置处的组数据
			@Override
			public Object getGroup(int groupPosition) {
				return kindNameList.get(groupPosition);
			}

			@Override
			public int getGroupCount() {
				return kindNameList.size();
			}

			@Override
			public long getGroupId(int groupPosition) {
				return groupPosition;
			}

			// 该方法决定每个组选项的外观
			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				String groupName = getGroup(groupPosition).toString();
				String kind = kindKeyList.get(groupPosition);
				double hj = hjMap.get(kind);
				LinearLayout ll = new LinearLayout(CityXlActivity.this);
				ll.setPadding(0, 5, 0, 5);
				ListView.LayoutParams lp = new ListView.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				// lp.setMargins(0, 20, 0, 20);
				ll.setLayoutParams(lp);
				ll.setOrientation(0);
				TableLayout table = new TableLayout(CityXlActivity.this);
				table.setStretchAllColumns(true);
				TableRow row = new TableRow(CityXlActivity.this);
				TextView textVew = new TextView(CityXlActivity.this);
				textVew.setTextSize(15);
				textVew.setText("   " + groupName);
				textVew.setGravity(Gravity.BOTTOM);
				textVew.setHeight(50);
				textVew.setPadding(55, 5, 10, 0);
				textVew.setTextColor(Color.BLACK);
				row.addView(textVew);
				textVew = new TextView(CityXlActivity.this);
				textVew.setTextSize(15);
				textVew.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
				textVew.setPadding(0, 0, 10, 0);
				textVew.setText(formater.format(hj) + "元");
				textVew.setTextColor(Color.BLACK);
				row.addView(textVew);
				table.addView(row);
				ll.addView(table);
				return ll;
			}

			@Override
			public boolean isChildSelectable(int groupPosition,
					int childPosition) {
				return true;
			}

			@Override
			public boolean hasStableIds() {
				return true;
			}
		};
		ExpandableListView expandListView = (ExpandableListView) findViewById(R.id.daycityxllist);
		expandListView.setAdapter(adapter);
	}

	class BtnClickLister implements View.OnClickListener {
		int num = 0;

		public BtnClickLister(int num) {
			this.num = num;
		}

		public void onClick(View v) {
			Calendar c = Calendar.getInstance();
			TextView querydate = (TextView) findViewById(R.id.querydate);
			String qdate = querydate.getText().toString();
			SimpleDateFormat dateFomat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				c.setTime(dateFomat.parse(qdate));
				c.add(Calendar.DAY_OF_MONTH, num);
				Intent intent = new Intent();
				intent.putExtra("cid", cid);
				intent.putExtra("cityName", cityName);
				intent.putExtra("date", dateFomat.format(c.getTime()));
				intent.setAction("android.intent.action.VIEWCITYXL");
				startActivity(intent);
				finish();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// 连接网络并查询网点日销量
	/**
	 * 
	 * @param cid
	 *            中心销量
	 * @param date
	 *            查找日期
	 * @return
	 */
	private boolean findDayCityxlArray(String cid, String date, String day) {
		boolean flag = false;
		try {
			String url = HttpUtil.BASE_URL;
			Map<String, String> rawParams = new HashMap<String, String>();
			rawParams.put("cid", cid);
			rawParams.put("date", date);
			rawParams.put("day", day);
			rawParams.put("oper", "6");
			JSONArray jsonArray = new JSONArray(HttpUtil.postRequest(url,
					rawParams));
			Log.d("jsonarray", jsonArray + "");
			errorCode = isCheckError(jsonArray);
			Log.d("error", errorCode + "");
			if (errorCode < 0) {
				return flag;
			}

			xlList = new ArrayList<String[]>();
			int len = jsonArray.length();
			for (int i = 0; i < len; i++) {
				JSONObject o = (JSONObject) jsonArray.get(i);
				String gameName = (String) o.get("Game_Name");
				String gameId = o.get("Game_id").toString();
				String je = o.get("Z_Sale_Je").toString();
				String kind = String.valueOf((Integer) o.get("Game_Kind_id")); // 1,3,4,6,7,10
				if (gameId.equals("024") || gameId.equals("022")) {// 如果是11选5、快中才，另当作高频处理
					kind = "10";
				}

				if (gameId.equals("777")) {// 汇总
					hzArray = je.split("_");
					continue;
				}

				String b[] = new String[2];
				b[0] = gameName;
				double tje = Double.parseDouble(je);
				b[1] = formater.format(tje) + "元";
				if (dataMap.containsKey(kind)) {
					List<String[]> arrayList = dataMap.get(kind);
					arrayList.add(b);
					double hj = hjMap.get(kind) + tje;
					hjMap.put(kind, hj);
				} else {
					List<String[]> arrayList = new ArrayList<String[]>();
					arrayList.add(b);
					dataMap.put(kind, arrayList);
					hjMap.put(kind, tje);
				}
				if (!kindKeyList.contains(kind)) {
					kindKeyList.add(kind);
					kindNameList.add(typeMap.get(kind));
				}
				// 获取合计数据
				total += tje;
			}
			flag = true;
		} catch (Exception e) {
			Log.e("findDayCityxlArray", e.getMessage());
		}
		return flag;
	}

	// 错误检查
	private int isCheckError(JSONArray jsonArray) throws JSONException {
		int len = jsonArray.length();
		int errorCode = 0;// 没有错误
		if (len == 1) {
			JSONObject o = (JSONObject) jsonArray.get(0);
			String error = (String) o.get("error");
			errorCode = Integer.parseInt(error);
		}
		return errorCode;
	}

	// 显示等待对话框
	private void showProgressDialog(Context ctx, String title, String msg,
			int style) {
		pBar = new ProgressDialog(ctx);
		pBar.setTitle(title);
		pBar.setMessage(msg);
		pBar.setProgressStyle(style);
		pBar.show();
	}

	private int getDayOfYear(String date) {
		SimpleDateFormat dateFomat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(dateFomat.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c.get(Calendar.DAY_OF_YEAR);
	}

	private void createPic() {
		// //new出来一个图饼的对象， 参数简介1.所占的比例 2.颜色 3.是否分组（是否种其他的块是分开的） 4.文字介绍
		// PieDataSerie pds = new PieDataSerie(chart_scale, chart_color,
		// chart_group, chart_lable);
		// //设置label的样式
		// pds.valueFont = GraphicsProvider.getFont("Arial", ChartFont.PLAIN,
		// 14);
		// //设置label到中心的距离
		// pds.textDistanceToCenter = 1.6;
		//
		// //设置图饼的标题
		// Title title = new Title("理财账户");
		//
		// //开始绘图
		// PiePlotter pp = new PiePlotter();
		// //设置3D效果为true
		// pp.effect3D = true;
		// //设置边框
		// pp.border = new
		// LineStyle(1,GraphicsProvider.getColor(ChartColor.BLACK),LineStyle.LINE_NORMAL);
		// //设置label的格式（#PERCENTAGE#，#VALUE#）
		// pp.labelFormat = "#PERCENTAGE#";
		// //设置半径
		// pp.radiusModifier = 3.2;
		// //设置块与块之间的间隔
		// pp.space = 10;
		// //设置label到块之间的线的样式
		// pp.labelLine = new
		// LineStyle(1,GraphicsProvider.getColor(ChartColor.BLACK),LineStyle.LINE_NORMAL);
		//
		// //生成一个对象
		// Legend legend = new Legend();
		// //因为我这里不需要解说，所以设置它为" ",中间有空格，没有空格的话，会出现多余的文字
		// legend.legendLabel=" ";
		//
		// //new一个图表对象，用来存放生成的图饼
		// com.java4less.rchart.Chart chart = new
		// com.java4less.rchart.Chart(title, pp, null, null);
		// chart.layout = com.java4less.rchart.Chart.LAYOUT_LEGEND_TOP;
		// chart.back = new
		// FillStyle(GraphicsProvider.getColor(ChartColor.WHITE));
		// chart.topMargin = 0.1;
		// chart.bottomMargin = 0.4;
		// chart.leftMargin =0.1;
		// chart.legend = legend;
		// chart.setHeight(200);
		// chart.setWidth(width);
		// chart.addSerie(pds);
		//
		// LinearLayout l = (LinearLayout) this.findViewById(R.id.chart);
		// ChartPanel chart = new ChartPanel(this);
		// chart.setChart(getChart());
		// l.addView(chart);
	}
}