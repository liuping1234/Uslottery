package com.uslotter;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.compress.ImageCompress;
import com.uslotter.mode.App;
import com.uslotter.mode.Remould;
import com.uslotter.util.Util;

/**
 * 网点改造
 * 
 * @author liu j
 */
public class UslotteryRecord_wdgzActivity extends Activity {

	/** 返回按钮 */
	private Button back;

	/** 保存按钮 */
	private Button save;

	/** 业务编号 */
	private EditText etNumber;

	/** 价格 */
	private EditText etPrice;

	/** 日期 */
	private EditText etDate;

	/** 备注 */
	private EditText etNoto;

	/** 选择 图片 */
	private ImageView ivAffix;

	/** 容器 */
	private LinearLayout llPic;

	private App app;

	private String imagepath;

	private String pic;

	private Remould remould;

	private ArrayList<Remould> remoulds;

	private DatePicker dpDate;

	private AlertDialog.Builder builder;

	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record2_wdgz);
		app = (App) getIntent().getSerializableExtra("app");
		initView();
		if (app.getRemoulds() != null && app.getRemoulds().size() >= 0) {
			showView();
		}

	}

	/** 初始化view */
	private void initView() {
		back = (Button) findViewById(R.id.remould_bt_back);
		save = (Button) findViewById(R.id.remould_bt_save);
		etPrice = (EditText) findViewById(R.id.remould_et_price);
		etNoto = (EditText) findViewById(R.id.remould_et_noto);
		etNumber = (EditText) findViewById(R.id.remould_et_number);
		etDate = (EditText) findViewById(R.id.remould_et_date);
		ivAffix = (ImageView) findViewById(R.id.remould_iv_annex);
		llPic = (LinearLayout) findViewById(R.id.remould_layout_pic);
		etDate.setInputType(InputType.TYPE_NULL);
		back.setOnClickListener(exitListener);
		save.setOnClickListener(saveListener);
		ivAffix.setOnClickListener(picListener);
		etDate.setOnFocusChangeListener(dateListener);
	};

	/** 将数据保存在实体类Remould中 */
	private void deposit() {
		StringBuilder sb = new StringBuilder();
		remould = new Remould();
		remoulds = new ArrayList<Remould>();

		if (Util.ExistSDCard()) {
			int size = llPic.getChildCount();
			for (int i = 0; i < size; i++) {
				ImageView iv = (ImageView) llPic.getChildAt(i);
				String path = iv.getTag().toString();
				if (i == size - 1) {
					sb.append(path);
				} else {
					sb.append(path).append(",");
				}
			}
		}
		remould.setNumber(etNumber.getText().toString().trim());
		remould.setPrice(etPrice.getText().toString().trim());
		remould.setDateDicker(etDate.getText().toString().trim());
		remould.setNoto(etNoto.getText().toString().trim());
		remould.setPicPath(sb + "");
		remoulds.add(remould);
		app.setRemoulds(remoulds);

	}

	/** 保存按钮 */
	private OnClickListener saveListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!etNumber.getText().toString().equals("")
					&& !etPrice.getText().toString().equals("")
					&& !etDate.getText().toString().equals("")) {
				deposit();
			} else {
				Toast.makeText(UslotteryRecord_wdgzActivity.this,
						R.string.save_warning, Toast.LENGTH_SHORT).show();
				return;
			}

			Intent intent = new Intent(UslotteryRecord_wdgzActivity.this,
					UslotteryRecord.class);
			intent.putExtra("remould", app);
			intent.putExtra("flag", 10 + "");
			startActivity(intent);
			finish();
			UslotteryRecord_wdgzActivity.this.startActivity(intent);
			UslotteryRecord_wdgzActivity.this.finish();
		}
	};

	/** 返回按钮 */
	private OnClickListener exitListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/** 编辑框 */
	private OnFocusChangeListener dateListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				builder = new Builder(UslotteryRecord_wdgzActivity.this);
				LayoutInflater inflater = LayoutInflater
						.from(UslotteryRecord_wdgzActivity.this);
				View dialogView = inflater.inflate(R.layout.datedialog, null);
				dpDate = (DatePicker) dialogView.findViewById(R.id.datepickers);
				// dpDate.setCalendarViewShown(false);
				builder.setTitle("选择日期:");
				builder.setView(dialogView);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								int year = dpDate.getYear();
								int month = dpDate.getMonth() + 1;
								int day = dpDate.getDayOfMonth();
								etDate.setText(year + "-" + month + "-" + day);
							}

						});
				dialog = builder.create();
				dialog.show();
				dialog.setCancelable(false);
			} else {
				return;
			}

		}
	};

	/** 选择拍照 */
	OnClickListener picListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(UslotteryRecord_wdgzActivity.this)
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
											+ "/myimage/j"
											+ Util.getCurTime()
											+ ".jpg";
									intent.putExtra(MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(new File(imagepath)));
									startActivityForResult(intent, 6);
									UslotteryRecord_wdgzActivity.this
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
											UslotteryRecord_wdgzActivity.this,
											SelectPic.class);
									intent.putExtra("sb", pic);
									startActivityForResult(intent, 2);
									UslotteryRecord_wdgzActivity.this
											.overridePendingTransition(
													R.anim.push_left_in,
													R.anim.push_left_out);
								}
							}).create().show();
		}
	};

	/** 将Remoulds中的数据导入控件 */
	private void showView() {
		etNumber.setText(app.getRemoulds().get(0).getNumber());
		etPrice.setText(app.getRemoulds().get(0).getPrice());
		etDate.setText(app.getRemoulds().get(0).getDateDicker());
		etNoto.setText(app.getRemoulds().get(0).getNoto());

		if (!app.getRemoulds().get(0).getPicPath().equals("")
				&& app.getRemoulds().get(0).getPicPath() != null) {
			if (app.getRemoulds().get(0).getPicPath().contains(",")) {
				String[] paths = app.getRemoulds().get(0).getPicPath()
						.split(",");
				for (int i = 0; i < paths.length; i++) {
					ImageView iv = new ImageView(this);
					iv.setPadding(0, 0, 15, 0);
					Bitmap bitMap = ImageCompress
							.decodeFile(paths[i], 800, 800);
					iv.setLayoutParams(new LayoutParams(80,
							LayoutParams.FILL_PARENT));
					iv.setImageBitmap(bitMap);
					iv.setTag(paths[i]);
					iv.setOnClickListener(clickListener);
					iv.setOnLongClickListener(longClickListener);
					llPic.addView(iv);
				}
			} else {
				ImageView iv = new ImageView(this);
				iv.setPadding(0, 0, 15, 0);
				Bitmap bitMap = ImageCompress.decodeFile(
						app.getRemoulds().get(0).getPicPath(), 800, 800);
				iv.setLayoutParams(new LayoutParams(80,
						LayoutParams.FILL_PARENT));
				iv.setImageBitmap(bitMap);
				iv.setTag(app.getRemoulds().get(0).getPicPath());
				iv.setOnClickListener(clickListener);
				iv.setOnLongClickListener(longClickListener);
				llPic.addView(iv);
			}
		}
	}

	/**
	 * 长按删除
	 */
	OnLongClickListener longClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			final ImageView iv = (ImageView) v;
			AlertDialog.Builder builder = new Builder(
					UslotteryRecord_wdgzActivity.this);
			builder.setTitle("提示");
			builder.setMessage("确定删除此照片吗？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							llPic.removeView(iv);
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
			UslotteryRecord_wdgzActivity.this.startActivity(intent);

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
							+ "/myimage/j" + Util.getCurTime() + i + ".jpg";
					String str = ImageCompress.compressImage3(bitmap,
							UslotteryRecord_wdgzActivity.this, imagepath);
					imageView.setImageBitmap(bitmap);
					imageView.setTag(str);
					llPic.addView(imageView);
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
						UslotteryRecord_wdgzActivity.this, imagepath);
				iv.setTag(path_wgPic);
				iv.setImageBitmap(bitmap);
				iv.setOnClickListener(clickListener);
				iv.setOnLongClickListener(longClickListener);
				llPic.addView(iv);

			}
		}
	}

}