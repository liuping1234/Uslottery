package com.uslotter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;


import com.cr.uslotter.R;
import com.uslotter.Adapter.ItemAdapter;
import com.uslotter.mode.GridViewBean;
import com.uslotter.util.Sort;

/**
 * 图片选择器
 * 
 * @author liu
 * 
 */
public class SelectPic extends Activity implements OnClickListener,
		OnItemLongClickListener, OnItemClickListener {

	private GridView mGridView;

	private ItemAdapter mGridAdapter;

	private String imagePath = "sdcard/DCIM/Camera";

	private ArrayList<GridViewBean> listData;

	private boolean longclick = true;

	private Button checkall;

	private Button checkNone;

	private Button confirm;
	
	/**存放状态*/
	private ArrayList<String> stateList = new ArrayList<String>();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.selectpic);
		checkall = (Button) findViewById(R.id.btn_check_all);
		checkNone = (Button) findViewById(R.id.btn_check_no);
		confirm = (Button) findViewById(R.id.btn_confirm);
		mGridView = (GridView) findViewById(R.id.gridview_picselect);
		checkall.setOnClickListener(this);
		checkNone.setOnClickListener(this);
		confirm.setOnClickListener(this);
		mGridAdapter = new ItemAdapter(this);
		mGridView.setAdapter(mGridAdapter);
		listData = picDispose();
		mGridAdapter.addDatas(listData);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnItemLongClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_check_all:
			for (int i = 0; i < listData.size(); i++) {
				listData.get(i).setCheckState(true);
			}
			mGridAdapter.notifyDataSetChanged();
			break;
		case R.id.btn_check_no:
			for (int i = 0; i < listData.size(); i++) {
				listData.get(i).setCheckState(false);
			}
			mGridAdapter.notifyDataSetChanged();
			break;
		case R.id.btn_confirm:
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < stateList.size(); i++) {
					sb.append(stateList.get(i) + ",");
			}
			Intent intent = new Intent();
			intent.putExtra("sb", sb.toString() + "");
			setResult(RESULT_OK, intent);
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 *  将bitmap和地址加载进实体类 
	 *  Arrays.sort()  排序
	 */
	private ArrayList<GridViewBean> picDispose() {
		File file = new File(imagePath);
		File files[] = file.listFiles();
		Arrays.sort(files, new Sort());
		if (files.length == 0) {
			return null;
		}
		listData = new ArrayList<GridViewBean>();
		for (int i = 0; i < files.length; i++) {
			GridViewBean bean = new GridViewBean();
			bean.setPath(files[i].getAbsolutePath());
			bean.setCheckState(false);
			bean.setStates(0);
			listData.add(bean);
		}
		return listData;
	}

	/**
	 * gridview点击事件。短按切换全图。编辑模式下。短按选中
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (!longclick) {
			listData.get(position).setCheckState(!listData.get(position).isCheckState());
			mGridAdapter.notifyDataSetChanged();
			if(listData.get(position).getStates()==0){
				stateList.add(listData.get(position).getPath());
				listData.get(position).setStates(1);
			}else if(listData.get(position).getStates()==1){
				stateList.remove(listData.get(position).getPath());
				listData.get(position).setStates(0);
			}
		} else {
			GridViewBean bean = (GridViewBean) parent.getAdapter().getItem(
					position);
			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(
					Uri.fromFile(new File(bean.getPath().toString())),
					"image/*");
			SelectPic.this.startActivity(intent);
			SelectPic.this.overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
		}

	}

	/**
	 * gridview长按事件。长按进入选择编辑模式
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (longclick) {
			longclick = false;
			listData.get(position).setCheckState(!listData.get(position).isCheckState());
			mGridAdapter.notifyDataSetChanged();
			if(listData.get(position).getStates()==0){
				stateList.add(listData.get(position).getPath());
				listData.get(position).setStates(1);
			}else if(listData.get(position).getStates()==1){
				stateList.remove(listData.get(position).getPath());
				listData.get(position).setStates(0);
			}
			return true;
		}

		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!longclick) {
			longclick = true;
			for (int i = 0; i < listData.size(); i++) {
				listData.get(i).setCheckState(false);
			}
			mGridAdapter.notifyDataSetChanged();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
