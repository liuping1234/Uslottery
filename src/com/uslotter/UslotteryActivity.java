package com.uslotter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;

public class UslotteryActivity extends Activity {
    ProgressDialog pBar = null;
    EditText etUserID = null;
    EditText etUserPWD = null;
    Button btSubmit = null;
    CheckBox cbsavePWD = null;
    String userName = "";// 账户
    String userPassword = "";// 密码
    int isSave = 0;// 自动登陆
    // static List<Map<String,String>> meslist=null;//通知
    String U_Name = null;
    public String U_Id = null;
    public String CenterID = "";
    SharedPreferences _sp = null;
    private String partId = "" ;
    private String update = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.main);
	// meslist=new ArrayList<Map<String,String>>();
	_sp = getSharedPreferences("Login", MODE_WORLD_WRITEABLE);
	etUserID = (EditText) findViewById(R.id.userId);
	etUserPWD = (EditText) findViewById(R.id.userPassword);
	btSubmit = (Button) findViewById(R.id.submit);
	cbsavePWD = (CheckBox) findViewById(R.id.savePWD);

	cbsavePWD.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	    @Override
	    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		isSave = arg1 ? 1 : 0;// 1.保存，0.不保存

		_sp.edit().putInt("isSave", isSave).commit();// 是否保存密码，1.保存，0.不保存
		Toast.makeText(UslotteryActivity.this,
			(arg1 ? "自动登录" : "取消自动登录"), Toast.LENGTH_LONG).show();
	    }
	});
	btSubmit.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		userName = etUserID.getText().toString();
		userPassword = etUserPWD.getText().toString();
		if (!userName.equals("") && !userPassword.equals("")) {
		    showProgressDialog(UslotteryActivity.this, "提示信息",
			    "正在登录，请稍后...", ProgressDialog.STYLE_SPINNER);
		    new Thread() {
			public void run() {
			    int success = Login(userName, userPassword);
			    Message m = new Message();
			    m.what = success;
			    pBar.cancel();
			    handler.sendMessage(m);
			}
		    }.start();
		} else {
		    new AlertDialog.Builder(UslotteryActivity.this)
			    .setTitle("提示！")
			    .setMessage("用户名，密码不能为空！")
			    .setCancelable(false)
			    .setPositiveButton("确定",
				    new DialogInterface.OnClickListener() {
					@Override
					public void onClick(
						DialogInterface arg0, int arg1) {
					    etUserID.setText("");
					    etUserPWD.setText("");
					}
				    }).create().show();
		}
	    }
	});
	int isLogin = getIntent().getIntExtra("isLogin", 0);// 1.自动登录，2.登录框登录
	if (isLogin == 1) {
	    cbsavePWD.setChecked(true);
	    showProgressDialog(UslotteryActivity.this, "提示信息", "自动登录，请稍后...",
		    ProgressDialog.STYLE_SPINNER);
	    new Thread() {
		public void run() {
		    userName = _sp.getString("username", null);
		    userPassword = _sp.getString("userPWD", null);
		    etUserID.setText(userName);
		    etUserPWD.setText(userPassword);
		    int success = Login(userName, userPassword);
		    Message m = new Message();
		    m.what = success;
		    try {
			sleep(1000);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		    pBar.cancel();
		    handler.sendMessage(m);
		}
	    }.start();
	}
    }

    Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
	    if (msg.what != 0 && msg.what != -1 && msg.what != -2
		    && msg.what != -4) {
		Intent intent = new Intent(UslotteryActivity.this,
			UslotteryMainMenu2.class);
		startActivity(intent);
		UslotteryActivity.this.overridePendingTransition(
			R.anim.push_left_in, R.anim.push_left_out);
		finish();
	    } else if (msg.what == -2) {
		Toast.makeText(UslotteryActivity.this,
			"网络连接异常,帐号和密码与上次成功登录相同!", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(UslotteryActivity.this,
			UslotteryMainMenu2.class);
		startActivity(intent);
		UslotteryActivity.this.overridePendingTransition(
			R.anim.push_left_in, R.anim.push_left_out);
		finish();
	    } else if (msg.what == -1) {
		showDialog("登录超时!");
	    } else if (msg.what == 0) {
		showDialog("用户帐号或密码错误!");
	    } else if (msg.what == -4) {
		showDialog("网络连接异常,帐号或密码与上次成功登录的不一样!");
	    }
	};
    };

    public int Login(String userName, String userPass) {
	int result = 0;
	if (HttpUtil.checkNet(UslotteryActivity.this)) {
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("UserID", userName);
	    map.put("pwd", userPass);
	    try {
	    	String post = HttpUtil.postRequest(HttpUrl.URL
						+ HttpUrl.loginURL, map);
					Log.d("tag", "--post:::::" + post);
					
		
		JSONArray jsonArray = new JSONArray(post);
		JSONObject obj = jsonArray.getJSONObject(0);
		result = Integer.parseInt((String) obj.get("u_Id"));// 0.失败
		if (result != 0 && result != -1 && result != -2) {
		    _sp.edit().putString("username", userName).commit();
		    _sp.edit().putString("userPWD", userPass).commit();
		    U_Name = obj.getString("UserName");
		    U_Id = obj.getString("u_Id");
		    CenterID = obj.getString("CenterID");
		    partId = obj.getString("partid");
		    update = obj.getString("updateMsg");
		    _sp.edit().putString("CenterID", CenterID).commit();	  
			_sp.edit().putString("partId", partId).commit();
			_sp.edit().putString("update", update).commit();
		    String userRoles = null;
		    if (obj.has("userRoles")) {
			userRoles = obj.getString("userRoles");
		    }
		    SharedPreferences u_info = getSharedPreferences("u_info",
			    MODE_WORLD_WRITEABLE);
		    u_info.edit().putString("cid", CenterID).commit();
		    u_info.edit().putString("uid", U_Id).commit();
		    u_info.edit().putString("u_name", U_Name).commit();
		    if (userRoles != null) {
			u_info.edit().putString("userRoles", userRoles)
				.commit();
		    }
		    u_info.edit().putString("username", userName).commit();
		    u_info.edit().putString("pass", userPass).commit();
		}
	    } catch (ClientProtocolException e) {
		e.printStackTrace();
		result = -1;
	    } catch (JSONException e) {
		e.printStackTrace();
		result = -1;
	    } catch (IOException e) {
		e.printStackTrace();
		result = -1;
	    } catch (Exception e) {
		e.printStackTrace();
		result = -1;
	    }
	} else {
	    SharedPreferences sp = getSharedPreferences("u_info",
		    MODE_WORLD_WRITEABLE);
	    String user = sp.getString("username", "");
	    String pass = sp.getString("pass", "");
	    if (user.equals(etUserID.getText().toString().trim())
		    && pass.equals(etUserPWD.getText().toString().trim())) {
		result = -2;
	    } else {
		result = -4;
	    }
	}
	return result;
    }

    public void showDialog(String msg) {
	// WelcomeActivity.Login.edit().putInt("isSave",
	// 0).commit();//是否保存密码，1.保存，0.不保存
	_sp.edit().putInt("isSave", 0).commit();// 是否保存密码，1.保存，0.不保存
	new AlertDialog.Builder(UslotteryActivity.this).setTitle("提示！")
		.setMessage(msg).setCancelable(false)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface arg0, int arg1) {
		    }
		}).create().show();
    }

    private void showProgressDialog(Context ctx, String title, String msg,
	    int style) {
	pBar = new ProgressDialog(ctx);
	pBar.setTitle(title);
	pBar.setMessage(msg);
	pBar.setProgressStyle(style);
	pBar.show();
	pBar.setCancelable(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	// 按下键盘上返回按钮 //
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    new AlertDialog.Builder(UslotteryActivity.this)
		    .setTitle("提示！")
		    .setMessage("退出程序")
		    .setCancelable(false)
		    .setPositiveButton("确定",
			    new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0,
					int arg1) {
				    // WelcomeActivity.Login.edit().clear().commit();
				    _sp.edit().clear().commit();
				    Intent intent = new Intent(
					    Intent.ACTION_MAIN);
				    intent.addCategory(Intent.CATEGORY_HOME);
				    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				    startActivity(intent);
				    UslotteryActivity.this.finish();
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