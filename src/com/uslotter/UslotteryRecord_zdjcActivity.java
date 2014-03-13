package com.uslotter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.mode.App;
import com.uslotter.mode.ListResearch;
import com.uslotter.mode.ListResult;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;

public class UslotteryRecord_zdjcActivity extends Activity {

	private Button check_bt_back, check_bt_save;

	private LinearLayout check_ll_layout;

	/** listview展示数据 */
	private ListView check_lv_showData;

	/** 备注 */
	private EditText check_et_notes;

	/** 附件 */
	private ImageView check_iv_annx;

	/** show图片 */
	private LinearLayout check_ll_pic;

	private App app;
	
	private ListResearch research ;
	
	private ArrayList<ListResearch> researchList = new ArrayList<ListResearch>();
	
	private ListResult result ;
	
	private ArrayList<ListResult> resultList = new ArrayList<ListResult>();
	private ArrayList<ArrayList<ListResult>> list=new ArrayList<ArrayList<ListResult>>();
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record2_zdjc);
		initView();
		new Thread(){
			public void run() {
				requestData(getCurTime());
			};
		}.start();

	}
	
	Handler handler =new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Toast.makeText(UslotteryRecord_zdjcActivity.this, "111111111", 5000).show();
			Log.d("haihai1229", "打印toast");
		}
		
	};

	private void initView() {
		check_lv_showData = (ListView) findViewById(R.id.check_lv_showData);
		check_et_notes = (EditText) findViewById(R.id.check_et_remarks);
		check_iv_annx = (ImageView) findViewById(R.id.check_iv_annex);
		check_bt_back = (Button) findViewById(R.id.check_bt_back);
		check_bt_save = (Button) findViewById(R.id.check_bt_save);
		check_ll_pic = (LinearLayout) findViewById(R.id.check_pic_layout);
		// check_iv_annx.setOnClickListener();
		// check_lv_showData.setOnItemClickListener();
	}

	public int requestData(String date) {
//		int result = 0;
		if (HttpUtil.checkNet(UslotteryRecord_zdjcActivity.this)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("month", date);
			try {
				String post = HttpUtil.postRequest(HttpUrl.URL+HttpUrl.ZDJC_URL, map);
				JSONArray jsonArray = new JSONArray(post);
				JSONObject object = (JSONObject) jsonArray.get(0);				
				//JSONObject jsonObject = (JSONObject) object.get("0");
				JSONArray jsonResearch = object.getJSONArray("listResearch");
				for (int i = 0; i < jsonResearch.length(); i++) {
					research = new ListResearch();
					JSONObject object1 = (JSONObject) jsonResearch.get(i);
					research.setId(object1.getString("id"));
					research.setTopic(object1.getString("topic"));
					research.setSubid(object1.getString("subid"));
					researchList.add(research);
				}
				
				JSONArray jsonResult = object.getJSONArray("listResult");
				for (int i = 0; i < jsonResult.length(); i++) {
					result = new ListResult();
					JSONObject object1 = (JSONObject) jsonResult.get(i);
					result.setId(object1.getString("id"));
					result.setReDesc(object1.getString("rsDesc"));
					result.setResid(object1.getString("resid"));
					resultList.add(result);
				}
				
					for (int i = 0; i < researchList.size(); i++) {
						ArrayList<ListResult> arrayList  =new ArrayList<ListResult>();
						for (int j = 0; j < resultList.size(); j++) {
							if(researchList.get(i).getId().equals(resultList.get(j).getResid())){
								arrayList.add(resultList.get(j));
							}
							
							}
						list.add(arrayList);
						
						
				}
					
					for (int j = 0; j < researchList.size(); j++) {
						Log.d("haihai1229", "  :question="+researchList.get(j).toString());
						ArrayList<ListResult> arrayList2 = list.get(j);
						for (int k = 0; k < arrayList2.size(); k++) {
							Log.d("haihai1229", "  result="+arrayList2.get(k).toString());

						}
					}
					handler.sendEmptyMessage(0);
					
			
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return 0;

	}

	@SuppressLint("SimpleDateFormat")
	private String getCurTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

}