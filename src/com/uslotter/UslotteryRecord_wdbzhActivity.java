package com.uslotter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.compress.ImageCompress;
import com.uslotter.mode.App;
import com.uslotter.util.DialogUtil;
import com.uslotter.util.HttpConnect;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;
import com.uslotter.util.Util;

/**
 * 网点标准化
 * 
 * @author gxkim
 */
public class UslotteryRecord_wdbzhActivity extends Activity {
    private String pic = null ;
    private String imagepath = null;
    private Button save, exit;
    TextView tv_wdbz = null;
    String options = "无";
    private RelativeLayout kfx_rl = null;
    private TextView df = null;
    int state = 0;
    String state_s = null;
    String url = "";
    String fwdh;
    int i;
    RelativeLayout rl = null;
    private ImageView iv_kfx = null;
    private ImageView iv_wdbh_add = null;

    private Spinner sp = null;
    private LinearLayout ll = null;
    private String wdbh_str = null;
    private float score;
    private ArrayList<String> paths = null;
    App app = null;

    private Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
	    super.handleMessage(msg);
	    kfx_rl.setEnabled(true);

	    int what = msg.what;
	    if (msg.what == 1) {
		String str = msg.obj.toString();
		Toast.makeText(UslotteryRecord_wdbzhActivity.this, "上传成功!",
			Toast.LENGTH_LONG).show();
	    } else if (msg.what == 2) {
		try {
		    if (msg.obj == null) {
			Toast.makeText(UslotteryRecord_wdbzhActivity.this,
				"没有收到服务器消息！", Toast.LENGTH_SHORT).show();
			return;
		    }
		    String str = msg.obj.toString();

		    JSONArray arr = null;
		    String status = null;
		    String gpstatus = null;

		    arr = new JSONArray(str);
		    JSONObject obj = arr.getJSONObject(0);
		    String _msg = obj.getString("Msg");
		    if (_msg.equals("1")) {
			status = obj.getString("status");
			gpstatus = obj.getString("gpstatus");
		    } else {
			return;
		    }
		    Intent intent = new Intent();
		    intent.setAction("android.intent.action.UslotteryRecordStandardActivity");
		    intent.putExtra("app", app);
		    intent.putExtra("Status", status);
		    intent.putExtra("gpstatus", gpstatus);
		    UslotteryRecord_wdbzhActivity.this.startActivityForResult(
			    intent, 3);
		    UslotteryRecord_wdbzhActivity.this
			    .overridePendingTransition(R.anim.push_left_in,
				    R.anim.push_left_out);
		} catch (JSONException e) {
		    e.printStackTrace();
		}
	    } else if (msg.what == -1) {
		// 网络连接失败
		Toast.makeText(UslotteryRecord_wdbzhActivity.this, "网络连接失败!",
			Toast.LENGTH_SHORT).show();
		app.setNetwork_satus("1");// 没有网络只能保存
		Intent intent = new Intent();
		intent.setAction("android.intent.action.UslotteryRecordStandardActivity");
		intent.putExtra("app", app);
		intent.putExtra("Status", "none");
		intent.putExtra("gpstatus", "none");
		UslotteryRecord_wdbzhActivity.this.startActivityForResult(
			intent, 3);
		UslotteryRecord_wdbzhActivity.this.overridePendingTransition(
			R.anim.push_left_in, R.anim.push_left_out);

	    } else if (msg.what == -2) {

		Toast.makeText(UslotteryRecord_wdbzhActivity.this, "出现未知异常!",
			Toast.LENGTH_SHORT).show();
	    }
	}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.record2_wdbzh);
	Intent intent = this.getIntent();
	app = (App) intent.getSerializableExtra("app");
	wdbh_str = app.getWdbh();
	save = (Button) this.findViewById(R.id.record2_wdbzh_save);
	exit = (Button) this.findViewById(R.id.record2_wdbzh_back);
	paths = new ArrayList<String>();

	kfx_rl = (RelativeLayout) this.findViewById(R.id.record2_wdbzh_rl_kfx);
	kfx_rl.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// 点击进入标准化得分页面
		Toast.makeText(UslotteryRecord_wdbzhActivity.this, "正在加载中..",
			Toast.LENGTH_SHORT).show();
		if (wdbh_str.equals("") || wdbh_str == null) {
		    DialogUtil.showDialog(UslotteryRecord_wdbzhActivity.this,
			    "网点编号为空");
		    return;
		}
		kfx_rl.setEnabled(false);
		kfx_rl.setAnimation(AnimationUtils.loadAnimation(
			UslotteryRecord_wdbzhActivity.this, R.anim.changecolor));

		queryData(wdbh_str);
	    }
	});
	tv_wdbz = (TextView) this.findViewById(R.id.record2_wdbzh_bzh);
	iv_kfx = (ImageView) this.findViewById(R.id.record2_wdbzh_iv_kfx);
	df = (TextView) this.findViewById(R.id.record2_wdbzh_tv_df);
	if (app.getDf() != null && !app.getDf().trim().equals("")) {
	    df.setText(app.getDf().trim());
	}
	sp = (Spinner) this.findViewById(R.id.record2_wdbzh_sp);
	if (app.getState() == null || app.getState().trim().equals("")) {
	    sp.setSelection(0);
	    state = 0;
	    app.setState("0");
	} else {
	    sp.setSelection(Integer.parseInt(app.getState().trim()));
	    state = Integer.parseInt(app.getState().trim());
	}
	if (app.getState().equals("0") || app.getState().trim().equals("")) {
	    kfx_rl.setVisibility(View.VISIBLE);
	    iv_kfx.setVisibility(View.VISIBLE);

	} else {
	    kfx_rl.setVisibility(View.GONE);
	    iv_kfx.setVisibility(View.GONE);
	}
	sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

	    @Override
	    public void onItemSelected(AdapterView<?> arg0, View arg1,
		    int position, long index) {
		if (position == 0) {
		    // 在售
		    kfx_rl.setVisibility(View.VISIBLE);
		    iv_kfx.setVisibility(View.VISIBLE);
		    state = 0;
		} else if (position == 1) {
		    kfx_rl.setVisibility(View.GONE);
		    iv_kfx.setVisibility(View.GONE);
		    state = 1;
		} else if (position == 2) {
		    kfx_rl.setVisibility(View.GONE);
		    iv_kfx.setVisibility(View.GONE);
		    state = 2;
		} else if (position == 3) {
		    kfx_rl.setVisibility(View.GONE);
		    iv_kfx.setVisibility(View.GONE);
		    state = 3;
		}
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0) {
	    }
	});

	iv_wdbh_add = (ImageView) this.findViewById(R.id.record2_wdbzh_wdzp_iv);
	iv_wdbh_add.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		new AlertDialog.Builder(UslotteryRecord_wdbzhActivity.this)
			.setTitle("温馨提示!")
			.setMessage("选择拍照还是图库")
			.setPositiveButton("拍照",
				new DialogInterface.OnClickListener() {

				    @Override
				    public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
					imagepath = Environment.getExternalStorageDirectory()
						+ "/myimage/b"+ Util.getCurTime() + ".jpg";
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
						imagepath)));
					startActivityForResult(intent, 6);
					UslotteryRecord_wdbzhActivity.this.overridePendingTransition(
						R.anim.push_left_in,R.anim.push_left_out);
				    }

				})
			.setNegativeButton("图库",new DialogInterface.OnClickListener() {

				    @Override
				    public void onClick(DialogInterface dialog,
					    int which) {
					Intent intent = new Intent(UslotteryRecord_wdbzhActivity.this,SelectPic.class);
//					intent.putExtra("sb", pic);
					startActivityForResult(intent, 2);
					UslotteryRecord_wdbzhActivity.this.overridePendingTransition(
							R.anim.push_left_in,
							R.anim.push_left_out);
				    }
				}).create().show();
	    }
	});

	rl = (RelativeLayout) this.findViewById(R.id.record2_wdbzh_rl);
	exit.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		UslotteryRecord_wdbzhActivity.this.finish();
		UslotteryRecord_wdbzhActivity.this.overridePendingTransition(
			R.anim.push_right_in, R.anim.push_right_out);
	    }
	});
	save.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		StringBuilder sb = new StringBuilder();
		if (Util.ExistSDCard()) {
		    int size = ll.getChildCount();
		    for (int i = 0; i < size; i++) {
			ImageView iv = (ImageView) ll.getChildAt(i);
			String path = iv.getTag().toString();
			if (i == size - 1) {
			    sb.append(path);
			} else {
			    sb.append(path).append(",");
			}
			Log.d("tag", "path:::::::" + path);
			paths.add(path);
		    }
		}
		Intent intent = new Intent(UslotteryRecord_wdbzhActivity.this,
			UslotteryRecord.class);
		app.setWdzp(sb.toString());
		app.setDf(df.getText().toString().trim());
		app.setState(state + "");
		intent.putExtra("flag", 1 + "");
		intent.putExtra("app", app);
		UslotteryRecord_wdbzhActivity.this.startActivity(intent);
		UslotteryRecord_wdbzhActivity.this.finish();
	    }
	});
	ll = (LinearLayout) this.findViewById(R.id.record2_wdbzh_wdzp_ll);
	if (app.getWdzp() != null && !app.getWdzp().equals("")) {
	    if (app.getWdzp().contains(",")) {
		String[] paths = app.getWdzp().split(",");
		for (int i = 0; i < paths.length; i++) {
		    ImageView iv = new ImageView(this);
		    iv.setPadding(0, 0, 15, 0);
		    Bitmap bitMap = ImageCompress.decodeFile(paths[i],800,800);
//		    bitMap = Bitmap.createScaledBitmap(bitMap, 80, 80, false);
		    iv.setLayoutParams(new LayoutParams(80,LayoutParams.FILL_PARENT));
		    iv.setImageBitmap(bitMap);
		    iv.setTag(paths[i]);

		    iv.setOnClickListener(clickListener);
		    iv.setOnLongClickListener(longClickListener);
		    ll.addView(iv);
		}
	    } else {
		ImageView iv = new ImageView(this);
		iv.setPadding(0, 0, 15, 0);
		Bitmap bitMap = ImageCompress.decodeFile(app.getWdzp(),800,800);
		bitMap = Bitmap.createScaledBitmap(bitMap, 80, 80, false);
		iv.setLayoutParams(new LayoutParams(80,LayoutParams.FILL_PARENT));
		iv.setImageBitmap(bitMap);
		iv.setTag(app.getWdzp());

		iv.setOnClickListener(clickListener);
		iv.setOnLongClickListener(longClickListener);
		ll.addView(iv);
	    }
	}
    }
    
    
    /**
     * 图片单击放大事件
     */
     OnClickListener clickListener = new OnClickListener() {
         @Override
         public void onClick(View v) {
            ImageView iv = (ImageView) v;
 	    Intent intent = new Intent();
 	    intent.setAction(android.content.Intent.ACTION_VIEW);
 	    intent.setDataAndType(Uri.fromFile(new File(iv.getTag().toString())), "image/*");
 	    UslotteryRecord_wdbzhActivity.this.startActivity(intent);
     	
         }
     };
     
     /**
      * 长按删除事件
      */
     OnLongClickListener longClickListener = new OnLongClickListener() {
         
         @Override
         public boolean onLongClick(View v) {
           final ImageView iv = (ImageView) v;
              // delete photo        
           AlertDialog.Builder builder = new Builder(UslotteryRecord_wdbzhActivity.this);
           	      builder.setTitle("提示");
           	      builder.setMessage("确定删除此照片吗？");
           	      builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {         
             		   @Override
             		   public void onClick(DialogInterface dialog,int which) {
             			 ll.removeView(iv);
             			 dialog.dismiss();
             		   	}
             		 });
           	      builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {           			  
             		   @Override
             		   public void onClick(DialogInterface dialog,int which) {           
             		       	 dialog.dismiss();
             			}
             		});
           	      builder.create().show();
             	
           	      return false;
         }
     };
    
    
    
    

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	// 按下键盘上返回按钮
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    new AlertDialog.Builder(UslotteryRecord_wdbzhActivity.this)
		    .setTitle("提 示").setMessage("是 否 退 出 录 单?")
		    .setPositiveButton("退 出", new DialogInterface.OnClickListener() {
		        
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            Intent intent = new Intent(UslotteryRecord_wdbzhActivity.this,
				    UslotteryRecord.class);
			    UslotteryRecord_wdbzhActivity.this.startActivity(intent);
			    UslotteryRecord_wdbzhActivity.this.finish();
			    UslotteryRecord_wdbzhActivity.this .overridePendingTransition(
				 R.anim.push_right_in, R.anim.push_right_out);
		    	
		        }
		    })
		   .setNeutralButton("取消", new DialogInterface.OnClickListener() {
		    
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			
		    }
		})
		    .create().show();

	    return true;
	} else {
	    return super.onKeyDown(keyCode, event);
	}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
	    Intent intent) {
	File isExist = new File("sdcard/DCIM/camera/");
	if (!isExist.exists()) {
	    isExist.mkdirs();
	}
	if (resultCode == 3) {
	    if (requestCode == 3) {
		app = (App) intent.getSerializableExtra("app");
		options = app.getKfx();
		String score_str = app.getDf();
		score = Float.parseFloat(score_str);
		String[] contents = options.split(",");
		int size = contents.length > 14 ? 14 : contents.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
		    if (i == size - 1) {
			if (size == 14) {
			    sb.append(contents[i]).append("...");
			} else {
			    sb.append(contents[i]);
			}
		    } else {
			sb.append(contents[i]).append(",");
		    }
		}
		df.setText("" + score);
	    }
	}
	if (resultCode == RESULT_OK) {
	    if (requestCode == 2) {
		ImageView imageView = null ;
		Bitmap bitmap = null ;
		pic = intent.getStringExtra("sb");
		String picPath [] =pic.split(",");
		for (int i = 0; i < picPath.length; i++) {
		    imageView = new ImageView(this);
		    bitmap = ImageCompress.decodeFile(picPath[i], 800, 800);
		    imageView.setPadding(0, 0, 15, 0);
		    imageView.setLayoutParams(new LayoutParams(80,LayoutParams.FILL_PARENT));
		    imagepath = Environment.getExternalStorageDirectory()
				+ "/myimage/b"+ Util.getCurTime() +i+ ".jpg";
		    String str = ImageCompress.compressImage3(bitmap, UslotteryRecord_wdbzhActivity.this, imagepath);
		    imageView.setImageBitmap(bitmap);
		    imageView.setTag(str);
		    ll.addView(imageView);
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
		iv.setLayoutParams(new LayoutParams(80,LayoutParams.FILL_PARENT));
		Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
		bitmap = ImageCompress.decodeFile(imagepath, 800, 800);
		String path_wgPic =  ImageCompress.compressImage3(bitmap, UslotteryRecord_wdbzhActivity.this, imagepath);
		iv.setTag(path_wgPic);
		iv.setImageBitmap(bitmap);
		iv.setOnClickListener(clickListener);
		iv.setOnLongClickListener(longClickListener);
		ll.addView(iv);
                
	    }
	}
    }

    public void upload(final String url, final File[] files) {
	new Thread() {
	    @Override
	    public void run() {
		Looper.prepare();
		try {
		    String result = HttpConnect.uploadFile1(url, files);
		    Message msg = handler.obtainMessage();
		    msg.obj = result;
		    msg.what = 1;
		    handler.sendMessage(msg);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		Looper.loop();
	    }

	}.start();
    }

    public void queryData(final String wdbh) {
	new Thread() {
	    public void run() {
		if (HttpUtil.checkNet(UslotteryRecord_wdbzhActivity.this)) {
		    Map<String, String> map = new HashMap<String, String>();
		    map.put("wdNo", wdbh);

		    try {
			String post = HttpUtil.postRequest(HttpUrl.URL
				+ HttpUrl.Wd_Status, map);
			Message msg = new Message();
			msg.obj = post;
			msg.what = 2;
			handler.sendMessage(msg);
		    } catch (ClientProtocolException e) {
			handler.sendEmptyMessage(-2);
			e.printStackTrace();
		    } catch (IOException e) {
			handler.sendEmptyMessage(-2);
			e.printStackTrace();
		    } catch (Exception e) {
			handler.sendEmptyMessage(-2);
			e.printStackTrace();
		    }

		} else {
		    handler.sendEmptyMessage(-1);
		}
	    }
	}.start();
    }
}