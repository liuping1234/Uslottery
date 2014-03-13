package com.uslotter;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.cr.uslotter.R;
/*
 * ֪ͨҳ��
 */
public class NotificationDetailActivity extends Activity {
private TextView tv_title = null;
private TextView tv_content = null;
private TextView tv_content_detail = null;
private TextView tv_name = null;
private TextView tv_date = null;
private ImageButton btn_home = null;
private ImageButton btn_search = null;
private ImageButton btn_more = null;
private Button tv_return = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notification_detail);
		String id = this.getIntent().getStringExtra("id");
		String title = this.getIntent().getStringExtra("title");
		String content = this.getIntent().getStringExtra("content");
		String content_detail = this.getIntent().getStringExtra("content_detail");
		String name = this.getIntent().getStringExtra("name");
		String date = this.getIntent().getStringExtra("date");
		tv_title = (TextView) this.findViewById(R.id.notification_title);
		tv_title.setMovementMethod(ScrollingMovementMethod.getInstance());
		tv_title.setText("���⣺"+title);
		
		tv_name = (TextView) this.findViewById(R.id.notification_name);
		tv_name.setMovementMethod(ScrollingMovementMethod.getInstance());
		tv_name.setText("�����ˣ�"+name);
		tv_date = (TextView) this.findViewById(R.id.notification_date);
		tv_date.setMovementMethod(ScrollingMovementMethod.getInstance());
		tv_date.setText("����ʱ�䣺"+date);
		
		tv_content = (TextView) this.findViewById(R.id.notification_content);
		tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());
		tv_content.setText("��Ҫ���ݣ�"+content);
		btn_home = (ImageButton) this.findViewById(R.id.notification_syhome);
		btn_search = (ImageButton) this.findViewById(R.id.notification_syjianjie);
		btn_more = (ImageButton) this.findViewById(R.id.notification_symore);
		tv_return = (Button)this.findViewById(R.id.notification_detail_wdinfoback);
		tv_content_detail = (TextView) this.findViewById(R.id.notification_content_detail);
		tv_content_detail.setMovementMethod(ScrollingMovementMethod.getInstance());
		tv_content_detail.setText("��ϸ���ݣ�"+content_detail);
		
		tv_return.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NotificationDetailActivity.this.finish();
			}
		});
		
		btn_home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(NotificationDetailActivity.this, UslotteryMainMenu2.class);
				startActivity(intent);
				finish();	
			}
		});
		btn_search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentwdinfo = new Intent();
				intentwdinfo.setAction("android.intent.action.SelectByIDActivity");
				startActivity(intentwdinfo);
				
			}
		});
		btn_more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// ����
				Toast.makeText(NotificationDetailActivity.this, "�㶫���ٿƼ����޹�˾\n" +
						"ר��ԱӦ�� �汾"+getVersion(), Toast.LENGTH_SHORT).show();
				//Toast.makeText(NotificationDetailActivity.this, "�㶫���ٿƼ����޹�˾", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	 public  String getVersion() {
	      try {
	          PackageManager manager = this.getPackageManager();
	          PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
	          String version = info.versionName;
	         return version;
	     } catch (Exception e) {
	         e.printStackTrace();
	        return "";
	     }
	 }
}