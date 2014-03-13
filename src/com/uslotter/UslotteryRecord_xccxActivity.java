package com.uslotter;

import java.io.File;
import java.util.ArrayList;

import com.cr.uslotter.R;
import com.uslotter.compress.ImageCompress;
import com.uslotter.mode.App;
import com.uslotter.mode.Publicity;
import com.uslotter.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/**
 * ��������
 * 
 * @author liu
 * ����ͼƬ:f
 */
public class UslotteryRecord_xccxActivity extends Activity {
	/** ���ذ�ť */
	private Button back;

	/** ���水ť */
	private Button save;

	/** ������ */
	private Spinner spTheme;

	/** ��ע */
	private EditText etNoto;

	/** ѡ�� ͼƬ */
	private ImageView ivAffix;

	/** ���� */
	private LinearLayout llPic;
	
	private Publicity publicity ;
	
	private ArrayList<Publicity> publicities;
	
	private App app;
	
	private String imagepath;
	
	private String pic ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record2_xccx);
		app = (App) getIntent().getSerializableExtra("app");
		initView();
		if(app.getPublicities()!=null&&app.getPublicities().size()>=0){
			showView();
		}
	}
	
	
	/** ��ʼ��view */
	private void initView() {
		back = (Button) findViewById(R.id.publicity_bt_back);
		save = (Button) findViewById(R.id.publicity_bt_save);
		spTheme = (Spinner) findViewById(R.id.publicity_sp_theme);
		etNoto = (EditText) findViewById(R.id.publicity_et_noto);
		ivAffix = (ImageView) findViewById(R.id.publicity_iv_annex);
		llPic = (LinearLayout) findViewById(R.id.publicity_layout_pic);
		back.setOnClickListener(exitListener);
		save.setOnClickListener(saveListener);
		ivAffix.setOnClickListener(picListener);
	};
	
	/** �����ݱ�����ʵ����Publicity�� */
	private void deposit() {
		StringBuilder sb = new StringBuilder();
		publicity = new Publicity();
		publicities = new ArrayList<Publicity>();

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
		publicity.setThemeItem((int) spTheme.getSelectedItemId());
		publicity.setNoto(etNoto.getText().toString().trim());
		publicity.setPicPath(sb+"");
		publicities.add(publicity);
		app.setPublicities(publicities);
	}
	
	/** ���水ť */
	private OnClickListener saveListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
				deposit();
			 if(etNoto.getText().toString().equals("")) {
				Toast.makeText(UslotteryRecord_xccxActivity.this,
						R.string.publicity_theme, Toast.LENGTH_SHORT).show();
				return;
			}

			Intent intent = new Intent(UslotteryRecord_xccxActivity.this,
					UslotteryRecord.class);
			intent.putExtra("publicity", app);
			intent.putExtra("flag", 7 + "");
			startActivity(intent);
			finish();
			UslotteryRecord_xccxActivity.this.startActivity(intent);
			UslotteryRecord_xccxActivity.this.finish();
		}
	};
	
	/** ���ذ�ť */
	private OnClickListener exitListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	
	/** ��Publicity�е����ݵ���ؼ� */
	private void showView() {
		spTheme.setSelection(app.getPublicities().get(0).getThemeItem());
		etNoto.setText(app.getPublicities().get(0).getNoto());

		if (!app.getPublicities().get(0).getPicPath().equals("")
				&& app.getPublicities().get(0).getPicPath() != null) {
			if (app.getPublicities().get(0).getPicPath().contains(",")) {
				String[] paths = app.getPublicities().get(0).getPicPath()
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
				Bitmap bitMap = ImageCompress.decodeFile(app.getPublicities()
						.get(0).getPicPath(), 800, 800);
				iv.setLayoutParams(new LayoutParams(80,
						LayoutParams.FILL_PARENT));
				iv.setImageBitmap(bitMap);
				iv.setTag(app.getPublicities().get(0).getPicPath());
				iv.setOnClickListener(clickListener);
				iv.setOnLongClickListener(longClickListener);
				llPic.addView(iv);
			}
		}
	}
	
	/** ѡ������ */
	OnClickListener picListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(UslotteryRecord_xccxActivity.this)
					.setTitle("��ܰ��ʾ!")
					.setMessage("ѡ�����ջ���ͼ��")
					.setPositiveButton("����",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											"android.media.action.IMAGE_CAPTURE");
									imagepath = Environment
											.getExternalStorageDirectory()
											+ "/myimage/f"
											+ Util.getCurTime()
											+ ".jpg";
									intent.putExtra(MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(new File(imagepath)));
									startActivityForResult(intent, 6);
									UslotteryRecord_xccxActivity.this
											.overridePendingTransition(
													R.anim.push_left_in,
													R.anim.push_left_out);
								}
							})
					.setNegativeButton("ͼ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											UslotteryRecord_xccxActivity.this,
											SelectPic.class);
									intent.putExtra("sb", pic);
									startActivityForResult(intent, 2);
									UslotteryRecord_xccxActivity.this
											.overridePendingTransition(
													R.anim.push_left_in,
													R.anim.push_left_out);
								}
							}).create().show();
		}
	};
	
	/**
	 * ����ɾ��
	 */
	OnLongClickListener longClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			final ImageView iv = (ImageView) v;
			AlertDialog.Builder builder = new Builder(
					UslotteryRecord_xccxActivity.this);
			builder.setTitle("��ʾ");
			builder.setMessage("ȷ��ɾ������Ƭ��");
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							llPic.removeView(iv);
							dialog.dismiss();
						}
					});
			builder.setNegativeButton("ȡ��",
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
	 * �̰��鿴ԭͼ
	 */
	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ImageView iv = (ImageView) v;
			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(
					Uri.fromFile(new File(iv.getTag().toString())), "image/*");
			UslotteryRecord_xccxActivity.this.startActivity(intent);

		}
	};
	
	/**
	 * ���ؽ�������¼�
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
							+ "/myimage/f" + Util.getCurTime() + i + ".jpg";
					String str = ImageCompress.compressImage3(bitmap,
							UslotteryRecord_xccxActivity.this, imagepath);
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
						UslotteryRecord_xccxActivity.this, imagepath);
				iv.setTag(path_wgPic);
				iv.setImageBitmap(bitmap);
				iv.setOnClickListener(clickListener);
				iv.setOnLongClickListener(longClickListener);
				llPic.addView(iv);

			}
		}
	}
	
}