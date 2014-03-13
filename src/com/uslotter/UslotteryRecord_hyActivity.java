package com.uslotter;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
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

import com.cr.uslotter.R;
import com.uslotter.compress.ImageCompress;
import com.uslotter.mode.App;
import com.uslotter.mode.Meeting;
import com.uslotter.util.DialogUtil;
import com.uslotter.util.Util;

/**
 * ����
 * 
 * @author liu e
 */
public class UslotteryRecord_hyActivity extends Activity {
	/** ���� */
	private Button meeting_back;

	/** ���� */
	private Button meeting_save;

	/** �������� */
	private EditText meeting_name;

	/** �������� */
	private EditText meeting_details;

	/** ��ע */
	private EditText meeting_noto;

	/** ��������ѡ��� */
	private Spinner meeting_theme;

	/** ���� */
	private ImageView meeting_affix;

	private LinearLayout meeting_addPic;

	/** ����������ݵ�ʵ���� */
	private Meeting meeting;

	private App app;

	private ArrayList<Meeting> meetingList;

	private String imagepath;

	/** �޸����ֺ��ͼƬ·�� */
	private String pic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record2_hy);
		app = (App) getIntent().getSerializableExtra("app");
		initView();
		if (app.getMeetingList() != null && app.getMeetingList().size() >= 0) {
			showView();
		}

	}

	/** ��ʼ��view */
	private void initView() {
		meeting_back = (Button) findViewById(R.id.meeting_bt_back);
		meeting_save = (Button) findViewById(R.id.meeting_bt_save);
		meeting_name = (EditText) findViewById(R.id.meeting_et_name);
		meeting_details = (EditText) findViewById(R.id.meeting_et_details);
		meeting_noto = (EditText) findViewById(R.id.meeting_et_note);
		meeting_theme = (Spinner) findViewById(R.id.meeting_sp_theme);
		meeting_affix = (ImageView) findViewById(R.id.meeting_iv);
		meeting_addPic = (LinearLayout) findViewById(R.id.meeting_pic_layout);
		meeting_back.setOnClickListener(exitListener);
		meeting_save.setOnClickListener(saveListener);
		meeting_affix.setOnClickListener(picListener);
	};

	/** �����ݱ�����ʵ����Meeting�� */
	private void deposit() {
		StringBuilder sb = new StringBuilder();
		meeting = new Meeting();
		meetingList = new ArrayList<Meeting>();

		if (Util.ExistSDCard()) {
			int size = meeting_addPic.getChildCount();
			for (int i = 0; i < size; i++) {
				ImageView iv = (ImageView) meeting_addPic.getChildAt(i);
				String path = iv.getTag().toString();
				if (i == size - 1) {
					sb.append(path);
				} else {
					sb.append(path).append(",");
				}
			}
		}
		meeting.setPicPath(sb.toString());
		meeting.setThemeItem((int) meeting_theme.getSelectedItemId());
		meeting.setName(meeting_name.getText().toString().trim());
		meeting.setDetails(meeting_details.getText().toString().trim());
		meeting.setNoto(meeting_noto.getText().toString().trim());
		meetingList.add(meeting);
		app.setMeetingList(meetingList);

	}

	/** ��meetingList�е����ݵ���ؼ� */
	private void showView() {
		meeting_theme.setSelection(app.getMeetingList().get(0).getThemeItem());
		meeting_name.setText(app.getMeetingList().get(0).getName());
		meeting_details.setText(app.getMeetingList().get(0).getDetails());
		meeting_noto.setText(app.getMeetingList().get(0).getNoto());

		if (!app.getMeetingList().get(0).getPicPath().equals("")
				&& app.getMeetingList().get(0).getPicPath() != null) {
			if (app.getMeetingList().get(0).getPicPath().contains(",")) {
				String[] paths = app.getMeetingList().get(0).getPicPath()
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
					meeting_addPic.addView(iv);
				}
			} else {
				ImageView iv = new ImageView(this);
				iv.setPadding(0, 0, 15, 0);
				Bitmap bitMap = ImageCompress.decodeFile(app.getMeetingList()
						.get(0).getPicPath(), 800, 800);
				iv.setLayoutParams(new LayoutParams(80,
						LayoutParams.FILL_PARENT));
				iv.setImageBitmap(bitMap);
				iv.setTag(app.getMeetingList().get(0).getPicPath());
				iv.setOnClickListener(clickListener);
				iv.setOnLongClickListener(longClickListener);
				meeting_addPic.addView(iv);
			}
		}
	}

	/** ���水ť */
	private OnClickListener saveListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			deposit();
			if (meeting_name.getText().toString().equals("")
					|| meeting_details.getText().toString().equals("")) {
				Toast.makeText(UslotteryRecord_hyActivity.this,
						R.string.meeting, Toast.LENGTH_SHORT).show();
				return;
			}

			Intent intent = new Intent(UslotteryRecord_hyActivity.this,
					UslotteryRecord.class);
			intent.putExtra("meeting", app);
			intent.putExtra("flag", 6 + "");
			startActivity(intent);
			finish();
			UslotteryRecord_hyActivity.this.startActivity(intent);
			UslotteryRecord_hyActivity.this.finish();
		}
	};

	/** ���ذ�ť */
	private OnClickListener exitListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/** ѡ������ */
	OnClickListener picListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(UslotteryRecord_hyActivity.this)
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
											+ "/myimage/e"
											+ Util.getCurTime()
											+ ".jpg";
									intent.putExtra(MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(new File(imagepath)));
									startActivityForResult(intent, 6);
									UslotteryRecord_hyActivity.this
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
											UslotteryRecord_hyActivity.this,
											SelectPic.class);
									intent.putExtra("sb", pic);
									startActivityForResult(intent, 2);
									UslotteryRecord_hyActivity.this
											.overridePendingTransition(
													R.anim.push_left_in,
													R.anim.push_left_out);
								}
							}).create().show();
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
							+ "/myimage/e" + Util.getCurTime() + i + ".jpg";
					String str = ImageCompress.compressImage3(bitmap,
							UslotteryRecord_hyActivity.this, imagepath);
					imageView.setImageBitmap(bitmap);
					imageView.setTag(str);
					meeting_addPic.addView(imageView);
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
						UslotteryRecord_hyActivity.this, imagepath);
				iv.setTag(path_wgPic);
				iv.setImageBitmap(bitmap);
				iv.setOnClickListener(clickListener);
				iv.setOnLongClickListener(longClickListener);
				meeting_addPic.addView(iv);

			}
		}
	}

	/**
	 * ����ɾ��
	 */
	OnLongClickListener longClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			final ImageView iv = (ImageView) v;
			AlertDialog.Builder builder = new Builder(
					UslotteryRecord_hyActivity.this);
			builder.setTitle("��ʾ");
			builder.setMessage("ȷ��ɾ������Ƭ��");
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							meeting_addPic.removeView(iv);
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
			UslotteryRecord_hyActivity.this.startActivity(intent);

		}
	};

}