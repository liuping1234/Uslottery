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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.MessageInfo.CustomListAdapter;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;
import com.uslotter.util.Util;
/*
 * 通知页面
 */
public class MessageInfo extends Activity implements OnScrollListener {
	int items = 1;
	ListView mesInfo = null;
	CustomListAdapter adapter = null;
	Button btBack = null;
	private int visibleLastIndex = 0; // 最后的可视项索引
	private int visibleItemCount; // 当前窗口可见项总数
	private List<Map<String, String>> data;
	Map<String,String> maps = null;
	private int page = 1;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
				if (msg.obj == null) {
					return;
				}
				
			if(msg.what == 1){
				try{
					String post = msg.obj.toString();
					JSONArray arr = new JSONArray(post);
					JSONArray obj2 = arr.getJSONArray(0);

					JSONObject obj = obj2.getJSONObject(0);
					String _msg = obj.getString("Msg");
					Log.i("tag", _msg);
					if (_msg.equals("1")) {
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

							data.add(mesmap);
						}
					}
					}catch(Exception e){
						e.printStackTrace();
					}
				adapter = new CustomListAdapter(MessageInfo.this);
				mesInfo.setAdapter(adapter);

				AdapterView.OnItemClickListener messInfoClickListener = new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// 跳转到详细页面
						maps = data.get(position);
						String _id = maps.get("id");
						queryData(_id, 3);
					}
				};
				mesInfo.setOnItemClickListener(messInfoClickListener);
			}else if(msg.what == 2){
				try{
					String post = msg.obj.toString();
					JSONArray arr = new JSONArray(post);
					JSONArray obj2 = arr.getJSONArray(0);

					JSONObject obj = obj2.getJSONObject(0);
					String _msg = obj.getString("Msg");
					Log.i("tag", _msg);
					if (_msg.equals("1")) {
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
						
							data.add(mesmap);
						}
					}
					}catch(Exception e){
						e.printStackTrace();
					}
				System.out.println("正在加载···");
				adapter.notifyDataSetChanged(); // 数据集变化后,通知adapter
				mesInfo.setSelection(visibleLastIndex - visibleItemCount + 1); // 设置选中项
//				loadMoreButton.setText("加载更多"); // 恢复按钮文字
			}else if(msg.what == 3){
				try{
				String post = msg.obj.toString();
				JSONArray arr = new JSONArray(post);

				JSONObject obj = arr.getJSONObject(0);
				String _msg = obj.getString("Msg");
				if(_msg.equals("1")){
					String content = obj.getString("MsgInfo");
					String content_detail = obj.getString("content");
					String name = obj.getString("CreateUserName");
					String date = obj.getString("CreateTime");
					Intent intent = new Intent();
					intent.putExtra("id", maps.get("id"));
					intent.putExtra("title", maps.get("title"));
					intent.putExtra("content", content);
					intent.putExtra("content_detail",content_detail);
					intent.putExtra("name",name );
					intent.putExtra("date", date);
					intent.setAction("android.intent.action.NotoficationDetailActivity");
					MessageInfo.this.startActivity(intent);			
				//	Toast.makeText(MessageInfo.this, content+"", Toast.LENGTH_SHORT).show();
				}
			
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	};
	
	String U_id=null;//专管员ID
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.messagelist);
		
	//	U_id=UslotteryActivity.U_Id;
		U_id = Util.getSharedPrefercencesString(this, "uid");//去服务器获取列表
		
//		loadMoreView = getLayoutInflater()
//				.inflate(R.layout.load_moremess, null);
//		loadMoreButton = (TextView) loadMoreView.findViewById(R.id.loadMore);
		mesInfo = (ListView) findViewById(R.id.listtzinfo);
//		mesInfo.addFooterView(loadMoreView); // 设置列表底部视图
//		mesInfo.setOnScrollListener(this); // 添加滑动监听
		mesInfo.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
			}
		});

		btBack = (Button) findViewById(R.id.messageback);
		btBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		data = new ArrayList<Map<String,String>>();
		queryData(page,1);
	}

	class CustomListAdapter extends BaseAdapter {
		public TextView message_list;
		private LayoutInflater inflater;
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
			// TODO Auto-generated method stub
			return position;
		}

		public CustomListAdapter(Context context) {
			this.context = context;
			this.inflater = LayoutInflater.from(context);
			//data = getData();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.message_item, null);
			}
			
			
			
			String title = (position+1)+":"+data.get(position).get("title");//+" "+ data.get(position).get("id");
			String type = data.get(position).get("type");
			title=title.length()>20?title.substring(0, 20)+"...":title;
			((TextView) convertView.findViewById(R.id.message_list))
					.setText(title);
			((TextView) convertView.findViewById(R.id.button))
					.setText(type);
			return convertView;
		}
	}
	public void queryData(final String id,final int what){
		new Thread(){
			
			public void run(){
				
				if(HttpUtil.checkNet(MessageInfo.this)){
					 Map<String,String> map=new HashMap<String,String>();
					 map.put("id", id);					 
					try {
						String post = HttpUtil.postRequest(HttpUrl.URL+HttpUrl.NoticeMsg,map);
						Message msg = new Message();
						msg.obj = post;
						msg.what = what;
						handler.sendMessage(msg);	
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}catch (Exception e) {
						e.printStackTrace();
					}
					
				}else{
					handler.sendEmptyMessage(-1);
				}
			}
			
		}.start();
	}
	public void queryData(final int page,final int what){
		new Thread(){
			
			public void run(){
				
				if(HttpUtil.checkNet(MessageInfo.this)){
					 Map<String,String> map=new HashMap<String,String>();
					 map.put("u_id", U_id);
					 map.put("page", page+"");
					 map.put("limit", "20");
					try {
						String post = HttpUtil.postRequest(HttpUrl.URL+HttpUrl.NoticeQuery,map);						
						Message msg = new Message();
						msg.obj = post;
						msg.what = what;
						handler.sendMessage(msg);	
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}catch (Exception e) {
						e.printStackTrace();
					}
					
				}else{
					handler.sendEmptyMessage(-1);
				}
			}
			
		}.start();
	}

	/**
	 * 滑动时被调用
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}

	/**
	 * 滑动状态改变时被调用
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		int itemsLastIndex = adapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1; // 加上底部的loadMoreView项
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			// 自动加载
//			loadMoreButton.setText("正在加载..."); // 设置按钮文字loading
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					page++;
					queryData(page, 2);
				}
			}, 1000);
		}
	}
}