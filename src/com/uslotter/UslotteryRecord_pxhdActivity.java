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
 * ��ѵ�
 * @author gxkim
 *
 */
public class UslotteryRecord_pxhdActivity extends Activity {
	/** ���ذ�ť */
	private Button btn_exit = null;

	/** ���水ť */
	private Button btn_save = null;

	/** ��ѵ���������� */
	private Spinner sp_pxdx = null;

	/** ��ѵ���������� */
	private Spinner sp_theme = null;

	/** ��ע */
	private EditText et_remarks = null;

	/** ͼƬѡ��ť */
	private ImageView iv_add = null;

	/** ���ݴ洢ʵ���� */
	private App app = null;
	
	/**���trainʵ����*/
	private ArrayList<Train> trains ;
	
	/**�����ѵ��������*/
	private Train train ;
	
	/**ͼƬ·��*/
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
	 *��ʵ�����е����ݷ���ؼ� 
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
	 * �����ݱ������ʵ������
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
	
	/**���水ť*/
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

	/** ��ʼ���ؼ� */
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
	
	
	/**���ذ�ť*/
	OnClickListener exitListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			finish();			
		}
	};
	
	
	/**ѡ������*/
	OnClickListener picListener = new OnClickListener(){
	    @Override
	    public void onClick(View v) {
		new AlertDialog.Builder(UslotteryRecord_pxhdActivity.this)
			.setTitle("��ܰ��ʾ!")
			.setMessage("ѡ�����ջ���ͼ��")
			.setPositiveButton("����",
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
			.setNegativeButton("ͼ��",
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
     * ���ؽ�������¼�
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
     * ����ɾ��
     */
    OnLongClickListener longClickListener = new OnLongClickListener() {
        
        @Override
        public boolean onLongClick(View v) {
            final ImageView iv = (ImageView) v;
            // delete photo        
            AlertDialog.Builder builder = new Builder(UslotteryRecord_pxhdActivity.this);
         	      builder.setTitle("��ʾ");
         	      builder.setMessage("ȷ��ɾ������Ƭ��");
         	      builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {         
           		   @Override
           		   public void onClick(DialogInterface dialog,int which) {
           			 px_layout.removeView(iv);
           			 dialog.dismiss();
           		   	}
           		 });
         	      builder.setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {           			  
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
     * �̰��鿴ԭͼ
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