package com.uslotter;

import java.io.File;
import java.util.ArrayList;

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
import com.uslotter.mode.WdwgInfo;
import com.uslotter.util.Util;

public class UslotteryRecord_wdwgActivity extends Activity {
    private Button btn_save = null;

    private Button btn_exit = null;

    private ImageView delete_image;

    private ImageView add_image;

    private LinearLayout ll_add = null;

    ArrayList<LinearLayout> lls = null;

    private ImageView iv_add = null;

    private LinearLayout wdwg_Layout;

    private ArrayList<String> wdzp_path;

    private String imagepath;

    private App app;

    private ArrayList<WdwgInfo> info;
    
    private String pic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.record2_wdwg);
	Intent intent = getIntent();
	app = (App) intent.getSerializableExtra("app");
	btn_exit = (Button) this.findViewById(R.id.record2_wdwg_back);
	btn_save = (Button) this.findViewById(R.id.record2_wdwg_save);
	ll_add = (LinearLayout) this.findViewById(R.id.record2_wdwg_ll);
	iv_add = (ImageView) this.findViewById(R.id.record2_wdwg_iv_annex);
	wdwg_Layout = (LinearLayout) findViewById(R.id.record2_wdwg_wdzp_ll);
	delete_image = (ImageView) findViewById(R.id.delete_image);
	add_image = (ImageView) findViewById(R.id.add_image);
	delete_image.setOnClickListener(deleteListener);
	add_image.setOnClickListener(addListener);
	wdzp_path = new ArrayList<String>();
	lls = new ArrayList<LinearLayout>();
	if(app.getInfo()!=null){
		info = app.getInfo();
	}else {
		info = new ArrayList<WdwgInfo>();
	}
	
	// 遍历集合
	traverseArray(info);
	iv_add.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		new AlertDialog.Builder(UslotteryRecord_wdwgActivity.this)
			.setTitle("温馨提示!")
			.setMessage("选择拍照还是图库")
			.setPositiveButton("拍照",
				new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog,int which) {
					Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
					imagepath = Environment.getExternalStorageDirectory()
						+ "/myimage/c"+ Util.getCurTime() + ".jpg";
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
						imagepath)));
					startActivityForResult(intent, 6);
					UslotteryRecord_wdwgActivity.this.overridePendingTransition(
						R.anim.push_left_in,R.anim.push_left_out);
				    }
				})
			.setNegativeButton("图库",
				new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog,
					    int which) {
					Intent intent = new Intent(UslotteryRecord_wdwgActivity.this,SelectPic.class);
					intent.putExtra("sb", pic);
					startActivityForResult(intent, 2);
					UslotteryRecord_wdwgActivity.this.overridePendingTransition(
							R.anim.push_left_in,
							R.anim.push_left_out);
				    }
				}).create().show();
	    }
	});
	btn_exit.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		finish();
	    }
	});

	btn_save.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		StringBuilder sb = new StringBuilder();
		if (Util.ExistSDCard()) {
		    int size = wdwg_Layout.getChildCount();
		    for (int i = 0; i < size; i++) {
			ImageView iv = (ImageView) wdwg_Layout.getChildAt(i);
			String path = iv.getTag().toString();
			if (i == size - 1) {
			    sb.append(path);
			} else {
			    sb.append(path).append(",");
			}
			wdzp_path.add(path);
		    }
		}
		app.setWgzp(sb.toString());
		Log.d("tag", "app.getWgzp:" + app.getWgzp());
	
		if(app.getInfo()!=null){
			app.getInfo().clear();
		}
	
		for (int i = 0; i < lls.size(); i++) {
		    WdwgInfo info = new WdwgInfo();
		    Spinner sp = (Spinner) lls.get(i).getChildAt(0);
		    EditText et = (EditText) lls.get(i).getChildAt(1);
		    info.setIndex((int) sp.getSelectedItemId());
		    info.setWgnr(sp.getSelectedItem().toString());
		    info.setWgxx(et.getText().toString().trim());
		    app.getInfo().add(info);
		}

		if (!(app.getInfo() == null) || app.getInfo().size() >= 0) {
		    for (int i = 0; i < app.getInfo().size(); i++) {
			if (app.getInfo().get(i).getWgxx() == null
				|| app.getInfo().get(i).getWgxx().equals("")) {
			    Toast.makeText(UslotteryRecord_wdwgActivity.this,
				    "请将内容填写完整...", 2).show();
			 return;
			}

		    }
		}

		Intent intent = new Intent(UslotteryRecord_wdwgActivity.this,
			UslotteryRecord.class);
		intent.putExtra("flag", 2 + "");
		intent.putExtra("_app", app);
		UslotteryRecord_wdwgActivity.this.startActivity(intent);
		UslotteryRecord_wdwgActivity.this.finish();
	    }
	});

	if (app.getWgzp() != null && !app.getWgzp().equals("")) {
	    if (app.getWgzp().contains(",")) {
		String[] paths = app.getWgzp().split(",");
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
		    wdwg_Layout.addView(iv);
		}
	    } else {
		ImageView iv = new ImageView(this);
		iv.setPadding(0, 0, 15, 0);
		Bitmap bitMap = ImageCompress.decodeFile(app.getWgzp(),800,800);
		iv.setLayoutParams(new LayoutParams(80,LayoutParams.FILL_PARENT));
		iv.setImageBitmap(bitMap);
		iv.setTag(app.getWgzp());
		iv.setOnClickListener(clickListener);
		iv.setOnLongClickListener(longClickListener);
		wdwg_Layout.addView(iv);
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
	    intent.setDataAndType(Uri.fromFile(new File(iv
		    .getTag().toString())), "image/*");
	    UslotteryRecord_wdwgActivity.this
		    .startActivity(intent);
    	
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
          AlertDialog.Builder builder = new Builder(UslotteryRecord_wdwgActivity.this);
          	      builder.setTitle("提示");
          	      builder.setMessage("确定删除此照片吗？");
          	      builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {         
            		   @Override
            		   public void onClick(DialogInterface dialog,int which) {
            			 wdwg_Layout.removeView(iv);
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
     * 遍历info集合
     * 
     * @param info
     */
    public void traverseArray(ArrayList<WdwgInfo> info) {
	int index = 0;
	String wgxx = "";
	String wgnr = "";
	if (info instanceof ArrayList && info.size() == 0) {
	    createNewWidget(-1, index, wgxx, wgnr);
	    return;
	}
	if(info!=null){
		for (int i = 0; i < info.size(); i++) {
		    index = info.get(i).getIndex();
		    wgxx = info.get(i).getWgxx();
		    wgnr = info.get(i).getWgnr();
		    Log.d("test", " info.size() = " + info.size());
		    createNewWidget(i, index, wgxx, wgnr);
		}
	}


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
     * 创建一个Linlayout
     * 
     * @param index
     * @param wgxx
     * @param wgnr
     */
    private void createNewWidget(final int i, int index, String wgxx,
	    String wgnr) {
	// 创建新的控件
	LinearLayout ll = new LinearLayout(UslotteryRecord_wdwgActivity.this);
	LayoutParams lp = new LayoutParams(
		LinearLayout.LayoutParams.FILL_PARENT,
		LinearLayout.LayoutParams.WRAP_CONTENT);
	lp.setMargins(5, 5, 5, 5);
	ll.setOrientation(LinearLayout.HORIZONTAL);
	ll.setLayoutParams(lp);
	final Spinner _sp = new Spinner(UslotteryRecord_wdwgActivity.this);
	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		this, R.array.wdwg, android.R.layout.simple_spinner_item);
	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	_sp.setAdapter(adapter);
	LayoutParams lp_sp = new LayoutParams(
		(this.getScreenWidth() - 100) / 2,
		LinearLayout.LayoutParams.WRAP_CONTENT);
	_sp.setLayoutParams(lp_sp);
	EditText et = new EditText(UslotteryRecord_wdwgActivity.this);
	LayoutParams lp_et = new LayoutParams(
		(this.getScreenWidth() - 100) / 2,
		LinearLayout.LayoutParams.WRAP_CONTENT);
	et.setLayoutParams(lp_et);
	ll.addView(_sp);
	ll.addView(et);
	ll_add.addView(ll);
	lls.add(ll);
	// 设置参数的值
	et.setText(wgxx);
	_sp.setSelection(index);

    }

    public int getScreenWidth() {
	DisplayMetrics metric = new DisplayMetrics();
	getWindowManager().getDefaultDisplay().getMetrics(metric);
	int width = metric.widthPixels; // 屏幕宽度（像素）
	return width;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
	    Intent intent) {
	if (resultCode == RESULT_OK) {
	    if (requestCode == 2) {
		ImageView imageView = null ;
		Bitmap bitmap = null ;
		pic = intent.getStringExtra("sb");
		String picPath [] =pic.split(",");
		for (int i = 0; i < picPath.length; i++) {
		    imageView = new ImageView(this);
		    bitmap = ImageCompress.decodeFile(picPath[i], 500, 500);
		    imageView.setPadding(0, 0, 15, 0);
		    imageView.setLayoutParams(new LayoutParams(80,LayoutParams.FILL_PARENT));
		    imagepath = Environment.getExternalStorageDirectory()
				+ "/myimage/c"+ Util.getCurTime() +i+ ".jpg";
		    String str = ImageCompress.compressImage3(bitmap, UslotteryRecord_wdwgActivity.this, imagepath);
		    imageView.setImageBitmap(bitmap);
		    imageView.setTag(str);
		    wdwg_Layout.addView(imageView);
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
		Bitmap	bitmap = ImageCompress.decodeFile(imagepath, 800, 800);
		String path_wgPic =  ImageCompress.compressImage3(bitmap, UslotteryRecord_wdwgActivity.this, imagepath);
		iv.setTag(path_wgPic);
		iv.setImageBitmap(bitmap);
		iv.setOnClickListener(clickListener);
		iv.setOnLongClickListener(longClickListener);
		wdwg_Layout.addView(iv);
                
	    }
	}
    }

}
