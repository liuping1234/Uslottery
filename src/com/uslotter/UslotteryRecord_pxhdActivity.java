package com.uslotter;



import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
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
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
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
import com.uslotter.mode.Train;
import com.uslotter.util.Util;

/**
 * 培训活动
 * @author gxkim
 *
 */
public class UslotteryRecord_pxhdActivity extends Activity {
	/** 返回按钮 */
	private Button btn_exit = null;

	/** 保存按钮 */
	private Button btn_save = null;

	/** 培训对象下拉框 */
	private Spinner sp_pxdx = null;

	/** 培训主题下拉框 */
	private Spinner sp_theme = null;

	/** 备注 */
	private EditText et_remarks = null;

	/** 图片选择按钮 */
	private ImageView iv_add = null;

	/** 数据存储实体类 */
	private App app = null;
	
	/**存放train实体类*/
	private ArrayList<Train> trains ;
	
	/**存放培训活动里的数据*/
	private Train train ;
	
	/**图片路径*/
	private String imagepath;
	
	private String pic ;
	
	private LinearLayout px_layout ;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record2_pxhd);
		initView();
		if(app.getTrains()!=null&&app.getTrains().size()>=0){
			showView();
		}
		
	}
	
	/**
	 *将实体类中的数据放入控件 
	 */
	private void showView(){
		sp_theme.setSelection(app.getTrains().get(0).getTrainThemeItem());
		sp_pxdx.setSelection(app.getTrains().get(0).getTrainObjectItem());
		et_remarks.setText(app.getTrains().get(0).getRemarks());		
		if (app.getTrains().get(0).getPicPath() != null && !app.getTrains().get(0).getPicPath().equals("")) {
			if(app.getTrains().get(0).getPicPath().contains(",")){
				String[] paths = app.getTrains().get(0).getPicPath().split(",");
				for (int i = 0; i < paths.length; i++) {
				    ImageView iv = new ImageView(this);
				    iv.setPadding(0, 0, 15, 0);
				    Bitmap bitMap = ImageCompress.decodeFile(paths[i],800,800);
//				    bitMap = Bitmap.createScaledBitmap(bitMap, 80, 80, false);
				    iv.setLayoutParams(new LayoutParams(80,LayoutParams.FILL_PARENT));
				    iv.setImageBitmap(bitMap);
				    iv.setTag(paths[i]);
				    iv.setOnClickListener(clickListener);
				    iv.setOnLongClickListener(longClickListener);
				    px_layout.addView(iv);
				}
			}
		else {
			
			ImageView iv = new ImageView(this);
			iv.setPadding(0, 0, 15, 0);
			Bitmap bitMap = ImageCompress.decodeFile(app.getTrains().get(0).getPicPath(),800,800);
			iv.setLayoutParams(new LayoutParams(80,LayoutParams.FILL_PARENT));
			iv.setImageBitmap(bitMap);
			iv.setTag(app.getTrains().get(0).getPicPath());
			iv.setOnClickListener(clickListener);
			iv.setOnLongClickListener(longClickListener);
			px_layout.addView(iv);
		    }		
		}
	}

	/**
	 * 将数据保存放在实体类中
	 */
	private void deposit(){
		StringBuilder sb = new StringBuilder();
		if (Util.ExistSDCard()) {
		    int size = px_layout.getChildCount();
		    for (int i = 0; i < size; i++) {
			ImageView iv = (ImageView) px_layout.getChildAt(i);
			String path = iv.getTag().toString();
			if (i == size - 1) {
			    sb.append(path);
			} else {
			    sb.append(path).append(",");
			}
		 }
	  }		
		train = new Train() ;
		trains = new ArrayList<Train>();
		train.setTrainThemeItem((int) sp_theme.getSelectedItemId());
		train.setTrainObjectItem((int) sp_pxdx.getSelectedItemId());		
		train.setTrainObject(sp_pxdx.getSelectedItem().toString());	
		train.setRemarks(et_remarks.getText().toString().trim());			
		train.setPicPath(sb.toString());		
		trains.add(train);
		app.setTrains(trains);	
	}
	
	/**保存按钮*/
	OnClickListener saveListener = new OnClickListener(){

		@SuppressLint("ShowToast")
		@Override
		public void onClick(View v) {
			
				deposit();		
			if(et_remarks.getText().toString().equals("")) {
				Toast.makeText(UslotteryRecord_pxhdActivity.this, R.string.save_warning, Toast.LENGTH_SHORT).show();
				return ;
			}		
			
			Intent intent = new Intent(UslotteryRecord_pxhdActivity.this, 
					UslotteryRecord.class);
			intent.putExtra("train", app);
			intent.putExtra("flag", 4+"");
			UslotteryRecord_pxhdActivity.this.startActivity(intent);
			UslotteryRecord_pxhdActivity.this.finish();
		}
		
	};

	/** 初始化控件 */
	private void initView() {
		Intent intent = getIntent();
		app = (App) intent.getSerializableExtra("app");
		btn_exit = (Button) this.findViewById(R.id.record2_pxhd_back);
		btn_save = (Button) this.findViewById(R.id.record2_pxhd_save);
		sp_pxdx = (Spinner) this.findViewById(R.id.record2_pxhd_sp_pxdx);
		sp_theme = (Spinner) this.findViewById(R.id.record2_pxhd_sp_theme);
		et_remarks = (EditText) this.findViewById(R.id.record2_pxhd_et_remarks);
		iv_add = (ImageView) this.findViewById(R.id.record2_pxhd_iv_annex);
		px_layout = (LinearLayout) findViewById(R.id.record2_wdpx_wdzp_ll);
		btn_exit.setOnClickListener(exitListener);
		btn_save.setOnClickListener(saveListener);
		iv_add.setOnClickListener(picListener);
		
	}
	
	
	/**返回按钮*/
	OnClickListener exitListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			finish();			
		}
	};
	
	
	/**选择拍照*/
	OnClickListener picListener = new OnClickListener(){
	    @Override
	    public void onClick(View v) {
		new AlertDialog.Builder(UslotteryRecord_pxhdActivity.this)
			.setTitle("温馨提示!")
			.setMessage("选择拍照还是图库")
			.setPositiveButton("拍照",
				new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog,int which) {
					Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
					imagepath = Environment.getExternalStorageDirectory()
						+ "/myimage/d"+ Util.getCurTime() + ".jpg";
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imagepath)));
					startActivityForResult(intent, 6);
					UslotteryRecord_pxhdActivity.this.overridePendingTransition(
						R.anim.push_left_in,R.anim.push_left_out);
				    }
				})
			.setNegativeButton("图库",
				new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog,int which) {
					Intent intent = new Intent(UslotteryRecord_pxhdActivity.this,SelectPic.class);
					intent.putExtra("sb", pic);
					startActivityForResult(intent, 2);
					UslotteryRecord_pxhdActivity.this.overridePendingTransition(
							R.anim.push_left_in,R.anim.push_left_out);
				    }
				}).create().show();
	    }
	};


	
	 /**
     * 返回结果处理事件
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
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
    		    imagepath = Environment.getExternalStorageDirectory()+ "/myimage/d"+ Util.getCurTime() +i+ ".jpg";
    		    String str = ImageCompress.compressImage3(bitmap, UslotteryRecord_pxhdActivity.this, imagepath);
    		    imageView.setImageBitmap(bitmap);
    		    imageView.setTag(str);
    		    px_layout.addView(imageView);
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
    		String path_wgPic =  ImageCompress.compressImage3(bitmap, UslotteryRecord_pxhdActivity.this, imagepath);
    		iv.setTag(path_wgPic);
    		iv.setImageBitmap(bitmap);
    		iv.setOnClickListener(clickListener);
    		iv.setOnLongClickListener(longClickListener);
    		px_layout.addView(iv);
                    
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
            // delete photo        
            AlertDialog.Builder builder = new Builder(UslotteryRecord_pxhdActivity.this);
         	      builder.setTitle("提示");
         	      builder.setMessage("确定删除此照片吗？");
         	      builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {         
           		   @Override
           		   public void onClick(DialogInterface dialog,int which) {
           			 px_layout.removeView(iv);
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
     * 短按查看原图
     */
    OnClickListener clickListener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            ImageView iv = (ImageView) v;
	    Intent intent = new Intent();
	    intent.setAction(android.content.Intent.ACTION_VIEW);
	    intent.setDataAndType(Uri.fromFile(new File(iv.getTag().toString())), "image/*");
	    UslotteryRecord_pxhdActivity.this.startActivity(intent);
    	
        }
    };
    
	
	
	
	
}