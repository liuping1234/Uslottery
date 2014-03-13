package com.uslotter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.compress.ImageCompress;
import com.uslotter.mode.App;
import com.uslotter.mode.DateInfo;
import com.uslotter.mode.MaintenanceInfo;
import com.uslotter.mode.Terminal_maintenance;
import com.uslotter.mode.WdwgInfo;
import com.uslotter.util.Util;

/**
 * 终端维护
 * 
 * @author liu i
 */
public class UslotteryRecord_zdwhActivity extends Activity {
	/** 增加一个item */
	private ImageView add_image;

	/** 删除一个item */
	private ImageView delete_image;

	/** 返回按钮 */
	private Button bt_back;

	/** 提交按钮 */
	private Button bt_save;

	/** 服务类别 */
	private Spinner sp_category;

	/** 终端型号 */
	private Spinner sp_model;

	/** 整机序号 */
	private EditText et_machineNumber;

	/** 故障描述 */
	private EditText et_fault;

	/** 处理方式 */
	private Spinner sp_processMode;

	/** 附件 */
	private ImageView iv_annex;

	/** 摆放图片的布局 */
	private LinearLayout ll_addPic;

	/** 改名后的图片路径 */
	private String imagepath;

	/** 图片路径 */
	private String pic;

	private LinearLayout ll_add;

	private App app;

	/** 终端维护实体类 */
	private Terminal_maintenance maintenance;

	private ArrayList<Terminal_maintenance> maintenanceList;

	private ArrayList<LinearLayout> lls = null;

	private ArrayList<MaintenanceInfo> infos;

	private ArrayAdapter<String> adapter;

	private SharedPreferences sp;

	/** 城市id号 */
	private String cityId;

	/** 数据库名 */
	private final String DB_NAME = "Uslottery.db";

	/** terminal表名 */
	private final String TABLE_NAME = "terminal";

	/** terminal表zdTypeName列 */
	private final String ZD_TYPE_NAME = "zdTypeName";
	
	/** terminal表id列 */
	private final String ZD_TYPE_NAME_ID = "id";

	/** cid列 */
	private final String CID = "cid";

	/**存放终端型号*/
	private ArrayList<String> modelList = new ArrayList<String>();
	
	/**存放终端型号和它的id*/
	private HashMap<String, String> modelValue = new HashMap<String, String>();

	/** unitname表 */
	private final String UNITNAME = "unitname";

	/** unitname表partsName列 */
	private final String PARTSNAME = "partsName";

	/** unitname表id列 */
	private final String UNITNAME_ID = "id";

	/** 存放部件名称 */
	private ArrayList<String> nameList = new ArrayList<String>();

	/** 存放部件名称和ID号 */
	private HashMap<String, String> nameValue = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record2_zdwh);
		dbOperate();
		initView();
		showView();
	}

	/** 数据库操作 */
	@SuppressLint("WorldWriteableFiles")
	private void dbOperate() {
		sp = getSharedPreferences("Login", MODE_WORLD_WRITEABLE);
		cityId = sp.getString("CenterID", null);
		SQLiteDatabase db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
		Cursor cursor;
		cursor = db.query(TABLE_NAME, new String[] { ZD_TYPE_NAME,ZD_TYPE_NAME_ID },
				CID + "=?", new String[] { cityId }, null, null, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				String zdTypeName = cursor.getString(cursor
						.getColumnIndex(ZD_TYPE_NAME));
				String id = cursor.getString(cursor
						.getColumnIndex(ZD_TYPE_NAME_ID));
				modelList.add(zdTypeName);
				modelValue.put(zdTypeName, id);
			}
			cursor.close();
		}
		cursor = db.query(UNITNAME, new String[] { PARTSNAME, UNITNAME_ID },
				CID + "=?", new String[] { cityId }, null, null, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				String partsName = cursor.getString(cursor
						.getColumnIndex(PARTSNAME));
				String id = cursor
						.getString(cursor.getColumnIndex(UNITNAME_ID));
				nameValue.put(partsName, id);
				nameList.add(partsName);
			}
			cursor.close();
		}
	}

	/** 将数据展示在控件中 */
	private void showView() {
		if (app.getMaintenanceList() != null) {
			maintenance = app.getMaintenanceList().get(0);
			sp_category.setSelection(maintenance.getCategory());
			sp_model.setSelection(maintenance.getModel());
			et_machineNumber.setText(maintenance.getMachineNumber());
			et_fault.setText(maintenance.getFault());
			sp_processMode.setSelection(maintenance.getProcessMode());
			if (maintenance.getInfos() != null) {
				infos = maintenance.getInfos();
				traverseArray(infos);
			} else {
				infos = new ArrayList<MaintenanceInfo>();
			}

			if (maintenance.getPicPath() != null
					&& !maintenance.getPicPath().equals("")) {
				if (maintenance.getPicPath().contains(",")) {
					String[] paths = maintenance.getPicPath().split(",");
					for (int i = 0; i < paths.length; i++) {
						ImageView iv = new ImageView(this);
						iv.setPadding(0, 0, 15, 0);
						Bitmap bitMap = ImageCompress.decodeFile(paths[i], 800,
								800);
						iv.setLayoutParams(new LayoutParams(80,
								LayoutParams.FILL_PARENT));
						iv.setImageBitmap(bitMap);
						iv.setTag(paths[i]);
						iv.setOnClickListener(clickListener);
						iv.setOnLongClickListener(longClickListener);
						ll_addPic.addView(iv);
					}
				} else {
					ImageView iv = new ImageView(this);
					iv.setPadding(0, 0, 15, 0);
					Bitmap bitMap = ImageCompress.decodeFile(
							maintenance.getPicPath(), 800, 800);
					iv.setLayoutParams(new LayoutParams(80,
							LayoutParams.FILL_PARENT));
					iv.setImageBitmap(bitMap);
					iv.setTag(maintenance.getPicPath());
					iv.setOnClickListener(clickListener);
					iv.setOnLongClickListener(longClickListener);
					ll_addPic.addView(iv);
				}
			}

		}

	}

	/** 初始化View */
	private void initView() {
		app = (App) getIntent().getSerializableExtra("app");
		lls = new ArrayList<LinearLayout>();
		sp_category = (Spinner) findViewById(R.id.maintain_category);
		sp_model = (Spinner) findViewById(R.id.maintain_model);
		et_machineNumber = (EditText) findViewById(R.id.maintain_machine_number);
		et_fault = (EditText) findViewById(R.id.maintain_fault);
		sp_processMode = (Spinner) findViewById(R.id.maintain_processmode);
		ll_add = (LinearLayout) findViewById(R.id.maintain_add_layout);
		add_image = (ImageView) findViewById(R.id.maintain_add_image);
		delete_image = (ImageView) findViewById(R.id.maintain_delete_image);
		iv_annex = (ImageView) findViewById(R.id.iv_annex);
		ll_addPic = (LinearLayout) findViewById(R.id.maintain_pic_layout);
		bt_back = (Button) findViewById(R.id.maintain_back);
		bt_save = (Button) findViewById(R.id.maintain_save);
		bt_back.setOnClickListener(backListener);
		bt_save.setOnClickListener(saveListener);
		add_image.setOnClickListener(addListener);
		delete_image.setOnClickListener(deleteListener);
		iv_annex.setOnClickListener(annexListener);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, modelList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_model.setAdapter(adapter);
	}

	/**
	 * 将数据保存到实体类中
	 */
	private void deposit() {
		StringBuilder sb = new StringBuilder();
		if (Util.ExistSDCard()) {
			int size = ll_addPic.getChildCount();
			for (int i = 0; i < size; i++) {
				ImageView iv = (ImageView) ll_addPic.getChildAt(i);
				String path = iv.getTag().toString();
				if (i == size - 1) {
					sb.append(path);
				} else {
					sb.append(path).append(",");
				}
			}
		}
		maintenance = new Terminal_maintenance();
		maintenanceList = new ArrayList<Terminal_maintenance>();
		maintenance.setCategory((int) sp_category.getSelectedItemId());
		maintenance.setModel((int) sp_model.getSelectedItemId());
		maintenance.setModelValue(modelValue.get(sp_model.getSelectedItem()));
		maintenance.setMachineNumber(et_machineNumber.getText().toString()
				.trim());
		maintenance.setFault(et_fault.getText().toString().trim());
		maintenance.setProcessMode((int) sp_processMode.getSelectedItemId());
		maintenance.setPicPath(sb + "");
		for (int i = 0; i < lls.size(); i++) {
			MaintenanceInfo info = new MaintenanceInfo();
			Spinner sp = (Spinner) lls.get(i).getChildAt(0);
			EditText et = (EditText) lls.get(i).getChildAt(1);
			info.setIndex((int) sp.getSelectedItemId());
			if(sp.getSelectedItem()!=null){
				info.setName(sp.getSelectedItem().toString());
				info.setNameValue(nameValue.get(sp.getSelectedItem().toString()));
			}else {
				Toast.makeText(this, "该城市暂不支持终端维护功能", Toast.LENGTH_SHORT);
			}
			info.setNumber(et.getText().toString().trim());
			maintenance.getInfos().add(info);
		}
		maintenanceList.add(maintenance);
		app.setMaintenanceList(maintenanceList);
	}

	/** 保存 */
	private OnClickListener saveListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			deposit();
			Intent intent = new Intent(UslotteryRecord_zdwhActivity.this,
					UslotteryRecord.class);
			intent.putExtra("flag", 5 + "");
			intent.putExtra("terminal", app);
			startActivity(intent);
			UslotteryRecord_zdwhActivity.this.startActivity(intent);
			UslotteryRecord_zdwhActivity.this.finish();

		}
	};

	/** 返回 */
	private OnClickListener backListener = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};

	/** 附件 */
	private OnClickListener annexListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			new AlertDialog.Builder(UslotteryRecord_zdwhActivity.this)
					.setTitle("温馨提示!")
					.setMessage("选择拍照还是图库")
					.setPositiveButton("拍照",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											"android.media.action.IMAGE_CAPTURE");
									imagepath = Environment
											.getExternalStorageDirectory()
											+ "/myimage/i"
											+ Util.getCurTime()
											+ ".jpg";
									intent.putExtra(MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(new File(imagepath)));
									startActivityForResult(intent, 6);
									UslotteryRecord_zdwhActivity.this
											.overridePendingTransition(
													R.anim.push_left_in,
													R.anim.push_left_out);
								}
							})
					.setNegativeButton("图库",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											UslotteryRecord_zdwhActivity.this,
											SelectPic.class);
									intent.putExtra("sb", pic);
									startActivityForResult(intent, 2);
									UslotteryRecord_zdwhActivity.this
											.overridePendingTransition(
													R.anim.push_left_in,
													R.anim.push_left_out);
								}
							}).create().show();

		}
	};

	/** 屏幕宽度（像素） */
	public int getScreenWidth() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		return width;
	}

	/**
	 * 
	 * @param i
	 *            判断创建或不创建非空item
	 * @param index
	 *            索引
	 * @param name
	 *            部件名称
	 * @param number
	 *            部件编号
	 */
	private void createNewWidget(final int i, int index, String name,
			String number) {
		LinearLayout ll = new LinearLayout(UslotteryRecord_zdwhActivity.this);
		LayoutParams lp = new LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 5, 5, 5);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(lp);
		final Spinner _sp = new Spinner(UslotteryRecord_zdwhActivity.this);
		ArrayAdapter<String>adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item,nameList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_sp.setAdapter(adapter);
		LayoutParams lp_sp = new LayoutParams(
				(this.getScreenWidth() - 100) / 2,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		_sp.setLayoutParams(lp_sp);
		EditText et = new EditText(UslotteryRecord_zdwhActivity.this);
		LayoutParams lp_et = new LayoutParams(
				(this.getScreenWidth() - 100) / 2,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		et.setLayoutParams(lp_et);
		ll.addView(_sp);
		ll.addView(et);
		ll_add.addView(ll);
		lls.add(ll);
		// 设置参数的值
		et.setText(number);
		_sp.setSelection(index);

	}

	/**
	 * 增加一个空的item
	 * 
	 * @param i
	 *            -1:表示一个空的item
	 */
	public void addInfoArray(int i) {
		createNewWidget(i, 0, "", "");
	}

	/**
	 * 遍历集合
	 **/
	public void traverseArray(ArrayList<MaintenanceInfo> infos) {
		int index = 0;
		String name = "";
		String number = "";
		if (infos instanceof ArrayList && infos.size() == 0) {
			createNewWidget(-1, index, name, number);
			return;
		}
		if (infos != null) {
			for (int i = 0; i < infos.size(); i++) {
				index = infos.get(i).getIndex();
				name = infos.get(i).getName();
				number = infos.get(i).getNumber();
				createNewWidget(i, index, name, number);
			}
		}

	}

	/**
	 * 增加一个item
	 */
	private OnClickListener addListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			addInfoArray(-1);
		}

	};

	/**
	 * 删除最下面一个item
	 */
	private OnClickListener deleteListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (lls.size() >= 1) {
				ll_add.removeViewAt(lls.size());
				lls.remove(lls.size() - 1);
			}
		}

	};

	/**
	 * 图片单击放大事件
	 */
	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ImageView iv = (ImageView) v;
			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(
					Uri.fromFile(new File(iv.getTag().toString())), "image/*");
			UslotteryRecord_zdwhActivity.this.startActivity(intent);

		}
	};

	/**
	 * 长按删除事件
	 */
	OnLongClickListener longClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			final ImageView iv = (ImageView) v;
			AlertDialog.Builder builder = new Builder(
					UslotteryRecord_zdwhActivity.this);
			builder.setTitle("提示");
			builder.setMessage("确定删除此照片吗？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ll_addPic.removeView(iv);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 2) {
				ImageView imageView = null;
				Bitmap bitmap = null;
				pic = intent.getStringExtra("sb");
				String picPath[] = pic.split(",");
				for (int i = 0; i < picPath.length; i++) {
					imageView = new ImageView(this);
					bitmap = ImageCompress.decodeFile(picPath[i], 500, 500);
					imageView.setPadding(0, 0, 15, 0);
					imageView.setLayoutParams(new LayoutParams(80,
							LayoutParams.FILL_PARENT));
					imagepath = Environment.getExternalStorageDirectory()
							+ "/myimage/i" + Util.getCurTime() + i + ".jpg";
					String str = ImageCompress.compressImage3(bitmap,
							UslotteryRecord_zdwhActivity.this, imagepath);
					imageView.setImageBitmap(bitmap);
					imageView.setTag(str);
					ll_addPic.addView(imageView);
					imageView.setOnClickListener(clickListener);
					imageView.setOnLongClickListener(longClickListener);
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
				Bitmap bitmap = ImageCompress.decodeFile(imagepath, 800, 800);
				String path_wgPic = ImageCompress.compressImage3(bitmap,
						UslotteryRecord_zdwhActivity.this, imagepath);
				iv.setTag(path_wgPic);
				iv.setImageBitmap(bitmap);
				iv.setOnClickListener(clickListener);
				iv.setOnLongClickListener(longClickListener);
				ll_addPic.addView(iv);

			}
		}
	}

}