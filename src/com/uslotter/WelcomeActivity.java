package com.uslotter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import com.cr.uslotter.R;
import com.uslotter.util.CopyFileFromAssets;

public class WelcomeActivity extends Activity {
	public SharedPreferences Login;
	private String path = "data/data/com.cr.uslotter/databases";
	private String name = "Uslottery.db";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		Login = getSharedPreferences("Login", MODE_WORLD_WRITEABLE);
		Start();
		CopyFileFromAssets.copy(this, name, path, name);
	}

	public void Start() {
		new Thread() {
			int isLogin = Login.getInt("isSave", 0);

			public void run() {
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (isLogin == 1) {// �Զ���¼
					Intent intent = new Intent();
					intent.putExtra("isLogin", 1);
					intent.setAction("android.intent.action.UslotteryActivity");
					startActivity(intent);
					finish();
				} else {// ��¼����
					Intent intent = new Intent();
					intent.putExtra("isLogin", 2);
					// intent.setAction("android.intent.action.UslotteryMainMenu");//���˵�
					intent.setAction("android.intent.action.UslotteryActivity");// ��¼����
					// intent.setAction("android.intent.action.lingtuMapActivity");//��ͼ
					startActivity(intent);
					finish();
				}
			}
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ���¼����Ϸ��ذ�ť //
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(WelcomeActivity.this)
					.setTitle("��ʾ��")
					.setMessage("�˳�����")
					.setCancelable(false)
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									finish();
									System.exit(0);
								}
							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
								}
							}).create().show();
			return true;

		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}