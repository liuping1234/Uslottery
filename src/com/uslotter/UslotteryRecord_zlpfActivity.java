package com.uslotter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
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
import com.uslotter.db.MyDataBase;
import com.uslotter.db.MySqliteHelp;
import com.uslotter.mode.App;
import com.uslotter.mode.DataAllot;
import com.uslotter.mode.DateInfo;
import com.uslotter.util.Util;

/***
 * �����ɷ�
 * @author gxkim
 *g
 */
public class UslotteryRecord_zlpfActivity extends Activity {
	private Button btn_save = null;

	private Button btn_exit = null;

	private ImageView delete_image;

	private ImageView add_image;

	private LinearLayout ll_add = null;

	ArrayList<LinearLayout> lls = null;

	private ImageView iv_add = null;
	
	private EditText et_noto;

	/**ͼƬ�ڷ�λ��*/
	private LinearLayout zlpf_layout;

	private String imagepath;

	private App app;

	/**��������ɷ�����*/
	private DataAllot allot;

	/**���spinner��editText����*/
	private ArrayList<DateInfo> infos;

	private String pic = null;
	
	private ArrayList<DataAllot> dataAllots;
	
	private SharedPreferences sp ;
	
	/**����id��*/
	private String cityId ;
	
	/**���ݿ���*/
	public  final String DB_NAME = "Uslottery.db";
	
	/**����*/
	public  final String TABLE_NAME = "datasend";
	
	/**��mcode��*/
	public  final String CODE = "mcode";
	
	/**mname��*/
	public  final String NAME = "mname";
	
	/**cid��*/
	public  final String CID = "cid";
	
	/**���mcode�к�mname����*/
	private HashMap<String, String>sqlMap = new HashMap<String, String>();
	
	/**���mname����*/
	private ArrayList<String>nameArray = new ArrayList<String>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record2_zlpf2);
		initView();		
		SQLiteDatabase db =openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
		Cursor cursor;
		cursor = db.query(TABLE_NAME, new String[]{CODE,NAME}, CID+"=?", new String[]{cityId}, null, null,
				null);
		if(cursor==null){	
			
		}else {
			while (cursor.moveToNext()) {
				String mcode = cursor.getString(cursor.getColumnIndex(CODE));
				String mname = cursor.getString(cursor.getColumnIndex(NAME));
				sqlMap.put(mname, mcode);
				nameArray.add(mname);
			}
			cursor.close();
		}
		if(app.getDataAllots()==null){
			dataAllots = new ArrayList<DataAllot>();
			allot = new DataAllot();
			infos = new ArrayList<DateInfo>();
			
		}else {
			dataAllots = app.getDataAllots();
			allot = dataAllots.get(0);
			infos = allot.getDateInfos();
		}
		// ��������
		traverseArray(infos);
	

		if (allot.getPicPath() != null && !allot.getPicPath().equals("")) {
			if (allot.getPicPath().contains(",")) {
				String[] paths = allot.getPicPath().split(",");
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
					zlpf_layout.addView(iv);
				}
			} else {
				ImageView iv = new ImageView(this);
				iv.setPadding(0, 0, 15, 0);
				Bitmap bitMap = ImageCompress.decodeFile(allot.getPicPath(),
						800, 800);
				iv.setLayoutParams(new LayoutParams(80,
						LayoutParams.FILL_PARENT));
				iv.setImageBitmap(bitMap);
				iv.setTag(allot.getPicPath());
				iv.setOnClickListener(clickListener);
				iv.setOnLongClickListener(longClickListener);
				zlpf_layout.addView(iv);
			}
		}
		et_noto.setText(allot.getNoto());
	}
	
	/**��ʼ��*/
	@SuppressLint("WorldWriteableFiles")
	private void initView(){
		sp = getSharedPreferences("Login", MODE_WORLD_WRITEABLE);
		cityId = sp.getString("CenterID", null);
		Intent intent = getIntent();
		app = (App) intent.getSerializableExtra("app");
		lls = new ArrayList<LinearLayout>();
		btn_exit = (Button) this.findViewById(R.id.data_bt_back);
		btn_save = (Button) this.findViewById(R.id.data_bt_save);
		ll_add = (LinearLayout) this.findViewById(R.id.date_add_layout);
		iv_add = (ImageView) this.findViewById(R.id.date_iv_annex);
		zlpf_layout = (LinearLayout) findViewById(R.id.date_layout_pic);
		delete_image = (ImageView) findViewById(R.id.date_iv_del);
		add_image = (ImageView) findViewById(R.id.date_iv_add);
		et_noto = (EditText) findViewById(R.id.date_et_noto);
		delete_image.setOnClickListener(deleteListener);
		add_image.setOnClickListener(addListener);
		iv_add.setOnClickListener(annexListener);
		btn_exit.setOnClickListener(backListener);
		btn_save.setOnClickListener(saveListener);
	}
	
	/**�����¼�*/
	private OnClickListener saveListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			StringBuilder sb = new StringBuilder();
			if (Util.ExistSDCard()) {
				int size = zlpf_layout.getChildCount();
				for (int i = 0; i < size; i++) {
					ImageView iv = (ImageView) zlpf_layout.getChildAt(i);
					String path = iv.getTag().toString();
					if (i == size - 1) {
						sb.append(path);
					} else {
						sb.append(path).append(",");
					}
				}
			}
			allot.setPicPath(sb + "");
			allot.getDateInfos().clear();
			
			allot.setNoto(et_noto.getText().toString().trim());
			for (int i = 0; i < lls.size(); i++) {
				DateInfo dateInfo = new DateInfo();
				Spinner sp = (Spinner) lls.get(i).getChildAt(0);
				EditText et = (EditText) lls.get(i).getChildAt(1);
				dateInfo.setIndex((int) sp.getSelectedItemId());
				if(sp.getSelectedItem()!=null){
					dateInfo.setName(sp.getSelectedItem().toString());
					dateInfo.setMapValue(sqlMap.get(sp.getSelectedItem().toString()));
				}
				dateInfo.setNumber(et.getText().toString().trim());
				
				allot.getDateInfos().add(dateInfo);
			}

			if (!(allot.getDateInfos() == null)
					|| allot.getDateInfos().size() >= 0) {
				for (int i = 0; i < allot.getDateInfos().size(); i++) {
					if (allot.getDateInfos().get(i).getNumber() == null
							|| allot.getDateInfos().get(i).getNumber()
									.equals("")) {
						Toast.makeText(UslotteryRecord_zlpfActivity.this,
								"�������ɷ�����...", Toast.LENGTH_SHORT).show();
						return;
					}
					if(allot.getDateInfos().get(i).getMapValue()==null){
						Toast.makeText(UslotteryRecord_zlpfActivity.this,
								"�ó����ݲ�֧�������ɷ�����...",Toast.LENGTH_SHORT).show();
						return;
					}

				}
			}
			
			dataAllots.add(allot);
			app.setDataAllots(dataAllots);
			Intent intent = new Intent(UslotteryRecord_zlpfActivity.this,
					UslotteryRecord.class);
			intent.putExtra("flag", 8 + "");
			intent.putExtra("dataAllot", app);
			UslotteryRecord_zlpfActivity.this.startActivity(intent);
			UslotteryRecord_zlpfActivity.this.finish();
		
			
		}
	};
	
	/**�����¼�*/
	private OnClickListener backListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
			
		}
	};
	
	/**����*/
	private OnClickListener annexListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			new AlertDialog.Builder(UslotteryRecord_zlpfActivity.this)
					.setTitle("��ܰ��ʾ!")
					.setMessage("ѡ�����ջ���ͼ��")
					.setPositiveButton("����",new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
									imagepath = Environment.getExternalStorageDirectory()+ "/myimage/g"
											+ Util.getCurTime() + ".jpg";
									intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
															imagepath)));
									startActivityForResult(intent, 6);
									UslotteryRecord_zlpfActivity.this.overridePendingTransition(
											R.anim.push_left_in,R.anim.push_left_out);
								}
							})
					.setNegativeButton("ͼ��",new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									Intent intent = new Intent(UslotteryRecord_zlpfActivity.this,
											SelectPic.class);
									intent.putExtra("sb", pic);
									startActivityForResult(intent, 2);
									UslotteryRecord_zlpfActivity.this.overridePendingTransition(
											R.anim.push_left_in,R.anim.push_left_out);
								}
							}).create().show();
		
			
		}
	};
	

	/**
	 * ͼƬ�����Ŵ��¼�
	 */
	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ImageView iv = (ImageView) v;
			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(
					Uri.fromFile(new File(iv.getTag().toString())), "image/*");
			UslotteryRecord_zlpfActivity.this.startActivity(intent);

		}
	};

	/**
	 * ����ɾ���¼�
	 */
	OnLongClickListener longClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			final ImageView iv = (ImageView) v;
			// delete photo
			AlertDialog.Builder builder = new Builder(
					UslotteryRecord_zlpfActivity.this);
			builder.setTitle("��ʾ");
			builder.setMessage("ȷ��ɾ������Ƭ��");
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							zlpf_layout.removeView(iv);
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
	 * ����һ��item
	 */
	private OnClickListener addListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			addInfoArray(-1);
		}

	};

	/**
	 * ɾ��������һ��item
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
	 * ����info����
	 * 
	 * @param infos
	 */
	public void traverseArray(ArrayList<DateInfo> infos) {
		int index = 0;
		String name = "";
		String number = "";
		if (infos instanceof ArrayList && infos.size() == 0) {
			createNewWidget(-1, index, name, number);
			return;
		}
		if (infos != null) {
			for (int i = 0; i < infos.size(); i++) {
				index = infos.get(i).getIndex();
				name = infos.get(i).getName();
				number = infos.get(i).getNumber();
				Log.d("test", " info.size() = " + infos.size());
				createNewWidget(i, index, name, number);
			}
		}

	}

	/**
	 * ����һ���յ�item
	 * 
	 * @param i
	 *            -1:��ʾһ���յ�item
	 */
	public void addInfoArray(int i) {
		createNewWidget(i, 0, "", "");
	}

	/**
	 * ����һ��Linlayout
	 * 
	 * @param index
	 * @param wgxx
	 * @param wgnr
	 */
	private void createNewWidget(final int i, int index, String name,
			String number) {
		// �����µĿؼ�
		LinearLayout ll = new LinearLayout(UslotteryRecord_zlpfActivity.this);
		LayoutParams lp = new LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 5, 5, 5);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(lp);
		final Spinner _sp = new Spinner(UslotteryRecord_zlpfActivity.this);
		ArrayAdapter<String>adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item,nameArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_sp.setAdapter(adapter);
		LayoutParams lp_sp = new LayoutParams((this.getScreenWidth() - 100) / 2,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		_sp.setLayoutParams(lp_sp);
		EditText et = new EditText(UslotteryRecord_zlpfActivity.this);
		LayoutParams lp_et = new LayoutParams(
				(this.getScreenWidth() - 100) / 2,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		et.setLayoutParams(lp_et);
		et.setInputType(InputType.TYPE_CLASS_NUMBER);
		ll.addView(_sp);
		ll.addView(et);
		ll_add.addView(ll);
		lls.add(ll);
		// ���ò�����ֵ
		et.setText(number);
		_sp.setSelection(index);

	}

	public int getScreenWidth() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // ��Ļ��ȣ����أ�
		return width;
	}

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
							+ "/myimage/g" + Util.getCurTime() + i + ".jpg";
					String str = ImageCompress.compressImage3(bitmap,
							UslotteryRecord_zlpfActivity.this, imagepath);
					imageView.setImageBitmap(bitmap);
					imageView.setTag(str);
					zlpf_layout.addView(imageView);
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
						UslotteryRecord_zlpfActivity.this, imagepath);
				iv.setTag(path_wgPic);
				iv.setImageBitmap(bitmap);
				iv.setOnClickListener(clickListener);
				iv.setOnLongClickListener(longClickListener);
				zlpf_layout.addView(iv);

			}
		}
	}

}
