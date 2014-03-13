package com.uslotter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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

import com.cr.uslotter.R;
import com.uslotter.compress.ImageCompress;
import com.uslotter.mode.App;
import com.uslotter.mode.MoveProspect;
import com.uslotter.util.Util;

/**
 * 建站勘察 k
 * */

public class UslotteryRecord_yzkcActivity extends Activity {
	private Button back, save;

	/** 勘察地点 */
	private Spinner sp_site;

	/** 业务编号 */
	private EditText et_number;

	/** 标注网点 */
	private EditText et_branch;

	/** 业务类型 */
	private Spinner sp_type;

	/** 验证申请人真实性 */
	private Spinner sp_verify1;

	/** 说明1 */
	private EditText et_explain1;

	/** 验证申请地点真实性 */
	private Spinner sp_verify2;

	/** 说明2 */
	private EditText et_explain2;

	/** 备注 */
	private EditText et_noto;

	/** 附件 */
	private ImageView iv_annx;

	private LinearLayout ll_pic;

	private String pic;

	private String imagepath;

	private App app = null;

	private MoveProspect prospect;

	private ArrayList<MoveProspect> prospects;

	private ArrayAdapter<String> adapter;

	private SharedPreferences sp;

	/** partId */
	private String partId =null;

	/** 城市id号 */
	private String cityId;

	/** 数据库名 */
	private final String DB_NAME = "Uslottery.db";

	/** terminal表名 */
	private final String TABLE_NAME = "prospect";

	/** cid列 */
	private final String C_ID = "C_id";

	/** partid列 */
	private final String PARTID = "partid";

	/** CZ_id列 */
	private final String CZ_ID = "CZ_id";

	/** CZ_name列 */
	private final String CZ_NAME = "CZ_Name";

	/** 勘察地点集合 */
	private ArrayList<String> siteList = new ArrayList<String>();

	/** 存放勘察地点和CZ_id */
	private HashMap<String, String> czid = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record2_yzkc);
		dbOperate();
		initView();
		showView();
	}

	/** 数据库操作 */
	private void dbOperate() {
		sp = getSharedPreferences("Login", MODE_WORLD_WRITEABLE);
		partId = sp.getString("partId", null);
		cityId = sp.getString("CenterID", null);
		SQLiteDatabase db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
		Cursor cursor;
		if (!partId.equals("-1")) {
			cursor = db.query(TABLE_NAME, new String[] { CZ_ID, CZ_NAME }, C_ID
					+ "=? and " + PARTID + " = ? ", new String[] { cityId,
					partId }, null, null, null);
		} else {
			cursor = db.query(TABLE_NAME, new String[] { CZ_ID, CZ_NAME }, C_ID
					+ "=? and " + PARTID + " is null ",
					new String[] { cityId }, null, null, null);
		}

		if (cursor != null) {
			String id = null;
			while (cursor.moveToNext()) {
				String site = cursor.getString(cursor.getColumnIndex(CZ_NAME));
				id = cursor.getString(cursor.getColumnIndex(CZ_ID));
				siteList.add(site);
				czid.put(site, id);
			}
			cursor.close();
		}

	}

	private void initView() {
		app = (App) getIntent().getSerializableExtra("app");
		back = (Button) findViewById(R.id.bt_move_back);
		save = (Button) findViewById(R.id.bt_move_save);
		sp_site = (Spinner) findViewById(R.id.sp_move_site);
		et_number = (EditText) findViewById(R.id.et_move_number);
		sp_type = (Spinner) findViewById(R.id.sp_move_type);
		sp_verify1 = (Spinner) findViewById(R.id.sp_move_verify_1);
		sp_verify2 = (Spinner) findViewById(R.id.sp_move_verify_2);
		et_explain1 = (EditText) findViewById(R.id.et_move_explain1);
		et_explain2 = (EditText) findViewById(R.id.et_move_explain2);
		et_noto = (EditText) findViewById(R.id.et_move_noto);
		iv_annx = (ImageView) findViewById(R.id.iv_move_annx);
		ll_pic = (LinearLayout) findViewById(R.id.ll_move_pic);
		et_branch = (EditText) findViewById(R.id.et_move_branch);
		back.setOnClickListener(exitListener);
		save.setOnClickListener(saveListener);
		iv_annx.setOnClickListener(picListener);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, siteList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_site.setAdapter(adapter);
		sp_verify1.setSelection(1);
		sp_verify2.setSelection(1);
	}

	/** 将数据保存在实体类MoveProspect中 */
	private void deposit() {
		prospect = new MoveProspect();
		prospects = new ArrayList<MoveProspect>();
		StringBuilder sb = new StringBuilder();
		if (Util.ExistSDCard()) {
			int size = ll_pic.getChildCount();
			for (int i = 0; i < size; i++) {
				ImageView iv = (ImageView) ll_pic.getChildAt(i);
				String path = iv.getTag().toString();
				if (i == size - 1) {
					sb.append(path);
				} else {
					sb.append(path).append(",");
				}
			}
		}
		prospect.setPicPath(sb + "");
		prospect.setSiteItem((int) sp_site.getSelectedItemId());
		prospect.setSite((String) sp_site.getSelectedItem());
		prospect.setSiteValue(czid.get(sp_site.getSelectedItem()));
		prospect.setBranch(et_branch.getText().toString().trim());
		prospect.setNumber(et_number.getText().toString().trim());
		prospect.setTypeItem((int) sp_type.getSelectedItemId());
		prospect.setVerify1((int) sp_verify1.getSelectedItemId());
		prospect.setVerify2((int) sp_verify2.getSelectedItemId());
		prospect.setExplain1(et_explain1.getText().toString().trim());
		prospect.setExplain2(et_explain2.getText().toString().trim());
		prospect.setNoto(et_noto.getText().toString().trim());
		prospects.add(prospect);
		app.setMoveProspects(prospects);
	}

	private void showView() {
		if (app.getMoveProspects() != null) {
			prospect = app.getMoveProspects().get(0);
			sp_site.setSelection(prospect.getSiteItem());
			et_branch.setText(prospect.getBranch());
			et_number.setText(prospect.getNumber());
			sp_type.setSelection(prospect.getTypeItem());
			sp_verify1.setSelection(prospect.getVerify1());
			sp_verify2.setSelection(prospect.getVerify2());
			et_explain1.setText(prospect.getExplain1());
			et_explain2.setText(prospect.getExplain2());
			et_noto.setText(prospect.getNoto());

			if (!prospect.getPicPath().equals("")
					&& prospect.getPicPath() != null) {
				if (prospect.getPicPath().contains(",")) {
					String[] paths = prospect.getPicPath().split(",");
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
						ll_pic.addView(iv);
					}
				} else {
					ImageView iv = new ImageView(this);
					iv.setPadding(0, 0, 15, 0);
					Bitmap bitMap = ImageCompress.decodeFile(
							prospect.getPicPath(), 800, 800);
					iv.setLayoutParams(new LayoutParams(80,
							LayoutParams.FILL_PARENT));
					iv.setImageBitmap(bitMap);
					iv.setTag(prospect.getPicPath());
					iv.setOnClickListener(clickListener);
					iv.setOnLongClickListener(longClickListener);
					ll_pic.addView(iv);
				}
			}

		}
	}

	/** 保存按钮 */
	private OnClickListener saveListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			deposit();
			Intent intent = new Intent(UslotteryRecord_yzkcActivity.this,
					UslotteryRecord.class);
			intent.putExtra("MoveProspect", app);
			intent.putExtra("flag", 12 + "");
			startActivity(intent);
			finish();
			UslotteryRecord_yzkcActivity.this.startActivity(intent);
			UslotteryRecord_yzkcActivity.this.finish();
		}
	};

	/** 返回按钮 */
	private OnClickListener exitListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/** 选择拍照 */
	OnClickListener picListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(UslotteryRecord_yzkcActivity.this)
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
											+ "/myimage/k"
											+ Util.getCurTime()
											+ ".jpg";
									intent.putExtra(MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(new File(imagepath)));
									startActivityForResult(intent, 6);
									UslotteryRecord_yzkcActivity.this
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
											UslotteryRecord_yzkcActivity.this,
											SelectPic.class);
									intent.putExtra("sb", pic);
									startActivityForResult(intent, 2);
									UslotteryRecord_yzkcActivity.this
											.overridePendingTransition(
													R.anim.push_left_in,
													R.anim.push_left_out);
								}
							}).create().show();
		}
	};

	/**
	 * 长按删除
	 */
	OnLongClickListener longClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			final ImageView iv = (ImageView) v;
			AlertDialog.Builder builder = new Builder(
					UslotteryRecord_yzkcActivity.this);
			builder.setTitle("提示");
			builder.setMessage("确定删除此照片吗？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ll_pic.removeView(iv);
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
			UslotteryRecord_yzkcActivity.this.startActivity(intent);

		}
	};

	/**
	 * 返回结果处理事件
	 */
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
							+ "/myimage/k" + Util.getCurTime() + i + ".jpg";
					String str = ImageCompress.compressImage3(bitmap,
							UslotteryRecord_yzkcActivity.this, imagepath);
					imageView.setImageBitmap(bitmap);
					imageView.setTag(str);
					ll_pic.addView(imageView);
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
						UslotteryRecord_yzkcActivity.this, imagepath);
				iv.setTag(path_wgPic);
				iv.setImageBitmap(bitmap);
				iv.setOnClickListener(clickListener);
				iv.setOnLongClickListener(longClickListener);
				ll_pic.addView(iv);

			}
		}
	}

}
