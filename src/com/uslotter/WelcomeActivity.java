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
				if (isLogin == 1) {// 自动登录
					Intent intent = new Intent();
					intent.putExtra("isLogin", 1);
					intent.setAction("android.intent.action.UslotteryActivity");
					startActivity(intent);
					finish();
				} else {// 登录界面
					Intent intent = new Intent();
					intent.putExtra("isLogin", 2);
					// intent.setAction("android.intent.action.UslotteryMainMenu");//主菜单
					intent.setAction("android.intent.action.UslotteryActivity");// 登录界面
					// intent.setAction("android.intent.action.lingtuMapActivity");//地图
					startActivity(intent);
					finish();
				}
			}
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮 //
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(WelcomeActivity.this)
					.setTitle("提示！")
					.setMessage("退出程序")
					.setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									finish();
									System.exit(0);
								}
							})
					.setNegativeButton("取消",
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