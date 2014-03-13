package com.uslotter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;
import com.uslotter.util.Util;

public class RecordedFormActivity extends Activity {
	private ListView lv = null;
	RecordFormAdapter adapter = null;
	private List<Map<String, String>> datas = null;
	private Button exit, save;
	ProgressDialog pd = null;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				pd.cancel();
				adapter = new RecordFormAdapter(RecordedFormActivity.this,
						datas);
				lv.setAdapter(adapter);

				break;
			case 1:
				pd.cancel();
				Toast.makeText(RecordedFormActivity.this, "出现未知异常",
						Toast.LENGTH_SHORT).show();

				break;
			case -2:
				pd.cancel();
				Toast.makeText(RecordedFormActivity.this, "网络连接异常",
						Toast.LENGTH_SHORT).show();

				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.requestWindowFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setContentView(R.layout.recordedform);
		exit = (Button) this.findViewById(R.id.recordedform_back);
		save = (Button) this.findViewById(R.id.recordedform_save);
		exit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RecordedFormActivity.this,
						UslotteryRecord.class);
				startActivity(intent);
				RecordedFormActivity.this.overridePendingTransition(
						R.anim.push_right_in, R.anim.push_right_out);

				finish();

			}
		});
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		lv = (ListView) this.findViewById(R.id.recordedform_lv);
		final String uid = Util.getSharedPrefercencesString(this, "uid");
		pd = ProgressDialog.show(RecordedFormActivity.this, "提示",
				"正在与服务器交互，请稍等...", true, true);
		pd.show();
		new Thread() {
			public void run() {
				request(uid);
			}
		}.start();

	}

	public void request(String uid) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		int result = 0;
		if (HttpUtil.checkNet(RecordedFormActivity.this)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("uid", uid);
			try {// id wdNo serviceDate submittime
				String post = HttpUtil.postRequest(HttpUrl.URL
						+ HttpUrl.findCrServiceByUser, map);
				System.out.println(post
						+ "----------------------------------------------");
				JSONArray jsonArray = new JSONArray(post);
				// JSONArray js1 = jsonArray.getJSONArray(0);
				// int length=js1.length();
				// String stri= js1.get(2).toString();
				// JSONObject obj = js1.getJSONObject(0);
				datas = new ArrayList<Map<String, String>>();
				JSONArray jsonArray2 = jsonArray.getJSONArray(0);
				int length1 = jsonArray2.length();
				for (int i = 0; i < length1; i++) {
					Map<String, String> mesmap = new HashMap<String, String>();
					JSONObject obj1 = jsonArray2.getJSONObject(i);
					int id = obj1.getInt("id");
					String wdNo = obj1.getString("wdNo");
					String serviceDate = obj1.getString("serviceDate");
					String submittime = obj1.getString("submittime");
					mesmap.put("id", id + "");
					mesmap.put("wdNo", wdNo);
					mesmap.put("serviceDate", serviceDate);
					mesmap.put("submittime", submittime);
					datas.add(mesmap);
				}
				handler.sendEmptyMessage(0);
			} catch (ClientProtocolException e) {
				handler.sendEmptyMessage(1);
				e.printStackTrace();
			} catch (JSONException e) {
				handler.sendEmptyMessage(1);
				e.printStackTrace();
			} catch (IOException e) {
				handler.sendEmptyMessage(1);
				e.printStackTrace();
			} catch (Exception e) {
				handler.sendEmptyMessage(1);
				e.printStackTrace();
			}
		} else {
			handler.sendEmptyMessage(-2);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(RecordedFormActivity.this,
					UslotteryRecord.class);
			startActivity(intent);
			RecordedFormActivity.this.overridePendingTransition(
					R.anim.push_right_in, R.anim.push_right_out);

			finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	class RecordFormAdapter extends BaseAdapter {
		public TextView message_list;
		private LayoutInflater inflater;
		private List<Map<String, String>> datas;
		private Context context;

		@Override
		public int getCount() {
			return datas.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public RecordFormAdapter(Context context,
				List<Map<String, String>> datas) {
			this.context = context;
			this.inflater = LayoutInflater.from(context);
			this.datas = datas;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.recordedform_item, null);
			}
			final int p = position;
			// TextView id_tv =
			// (TextView)convertView.findViewById(R.id.recordedform_id);
			TextView wdbh = (TextView) convertView
					.findViewById(R.id.recordedform_wdbh);
			// wdbh.setText("jjjsll"+position);
			TextView date_service = (TextView) convertView
					.findViewById(R.id.recordedform_service_date);
			// time1.setText("fsdrgg"+position);
			TextView date_commit = (TextView) convertView
					.findViewById(R.id.recordedform_commit_time);
			// time2.setText("skfcvv"+position);
			if (position == 0) {
				// id_tv.setText("编号");
				wdbh.setText("网点编号");
				date_service.setText("服务日期");
				date_commit.setText("提交日期");
			} else {
				// id_tv.setText(datas.get(position-1).get("id"));
				wdbh.setText(datas.get(position - 1).get("wdNo"));
				date_service
						.setText(datas.get(position - 1).get("serviceDate"));
				date_commit.setText(datas.get(position - 1).get("submittime"));
			}
			// final CheckBox checkbox = mItemView.fileChcekbox;
			// cb.setOnClickListener(new OnClickListener(){
			// @Override
			// public void onClick(View v) {
			// if(isSelected.contains(new Integer(p))){//取消
			// isSelected.remove(new Integer(p));
			// cb.setChecked(false);
			// }
			// else{
			// isSelected.add(new Integer(p));//选中
			// cb.setChecked(true);
			// }
			// }
			// });

			return convertView;

		}
	}
}// 792797067