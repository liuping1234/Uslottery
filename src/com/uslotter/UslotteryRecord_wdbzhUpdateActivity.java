package com.uslotter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.KeyEvent;
import android.view.View;
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

public class UslotteryRecord_wdbzhUpdateActivity extends Activity {
	private Button save, exit;
	TextView tv_wdbz = null;
	String options = "";
	private RelativeLayout kfx_rl = null;
	private TextView df = null;
	int state = 0;
	String state_s = null;
	String url = "";
	String wdbh, date, time;
	String fwdh;
	int i;
	RelativeLayout rl = null;
	private ImageView iv_fwyb_add = null;
	private ImageView iv_kfx = null;
	private ImageView iv_wdbh_add = null;
	private LinearLayout ll_fwyb;
	private String path_fwyb = "";
	private Spinner sp = null;
	private File mFile = null;
	private LinearLayout ll = null;
	private String wdbh_str = null;
	private float score = -1;
	private ArrayList<String> paths = null;
	private App app = null;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			kfx_rl.setEnabled(true);
			int what = msg.what;
			if (msg.what == 1) {
				String str = msg.obj.toString();
				Toast.makeText(UslotteryRecord_wdbzhUpdateActivity.this,
						"上传成功!", Toast.LENGTH_LONG).show();
			} else if (msg.what == 2) {
				try {
					if (msg.obj == null) {
						Toast.makeText(
								UslotteryRecord_wdbzhUpdateActivity.this,
								"没有收到服务器消息!", Toast.LENGTH_SHORT).show();
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
					Intent intent = new Intent(
							UslotteryRecord_wdbzhUpdateActivity.this,
							UslotteryRecordStandardUpdateActivity.class);
					intent.putExtra("app", app);
					intent.putExtra("Status", status);
					intent.putExtra("gpstatus", gpstatus);
					// UslotteryRecord.this.startActivity(intent);
					UslotteryRecord_wdbzhUpdateActivity.this
							.startActivityForResult(intent, 3);
					UslotteryRecord_wdbzhUpdateActivity.this
							.overridePendingTransition(R.anim.push_left_in,
									R.anim.push_left_out);
					// finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (msg.what == -1) {
				// 网络连接失败
				app.setNetwork_satus("1");
				Intent intent = new Intent();
				intent.setAction("android.intent.action.UslotteryRecordStandardActivity");
				intent.putExtra("app", app);
				intent.putExtra("Status", "none");
				intent.putExtra("gpstatus", "none");
				UslotteryRecord_wdbzhUpdateActivity.this
						.startActivityForResult(intent, 3);
				UslotteryRecord_wdbzhUpdateActivity.this
						.overridePendingTransition(R.anim.push_left_in,
								R.anim.push_left_out);

				Toast.makeText(UslotteryRecord_wdbzhUpdateActivity.this,
						"网络连接失败!", Toast.LENGTH_SHORT).show();
			} else if (msg.what == -2) {
				// 网络连接失败
				Toast.makeText(UslotteryRecord_wdbzhUpdateActivity.this,
						"出现未知异常!", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record2_wdbzh_update);
		app = (App) this.getIntent().getSerializableExtra("app");

		wdbh_str = this.getIntent().getStringExtra("wdbh");
		final Calendar calendar = Calendar.getInstance();
		save = (Button) this.findViewById(R.id.record2_wdbzh_up_save);
		exit = (Button) this.findViewById(R.id.record2_wdbzh_up_back);
		paths = new ArrayList<String>();

		kfx_rl = (RelativeLayout) this
				.findViewById(R.id.record2_wdbzh_up_rl_kfx);
		kfx_rl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击进入标准得分页面
				if (wdbh_str.equals("") || wdbh_str == null) {
					DialogUtil.showDialog(
							UslotteryRecord_wdbzhUpdateActivity.this, "网点编号为空");
					return;
				}
				kfx_rl.setEnabled(false);
				kfx_rl.setAnimation(AnimationUtils.loadAnimation(
						UslotteryRecord_wdbzhUpdateActivity.this,
						R.anim.changecolor));

				queryData(wdbh_str);
			}
		});
		tv_wdbz = (TextView) this.findViewById(R.id.record2_wdbzh_up_bzh);
		iv_kfx = (ImageView) this.findViewById(R.id.record2_wdbzh_up_iv_kfx);
		df = (TextView) this.findViewById(R.id.record2_wdbzh_up_tv_df);
		if (!app.getDf().trim().equals("")) {
			df.setText(app.getDf().trim());
		}

		sp = (Spinner) this.findViewById(R.id.record2_wdbzh_up_sp);

		if (app.getState().trim().equals("")) {
			sp.setSelection(0);
			state = 0;
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

		iv_wdbh_add = (ImageView) this
				.findViewById(R.id.record2_wdbzh_up_wdzp_iv);
		// iv_fwyb_add =
		// (ImageView)this.findViewById(R.id.record2_wdbzh_wdzp_iv);
		// ll_fwyb =
		// (LinearLayout)this.findViewById(R.id.record2_wdbzh_fwyb_ll);
		// iv_fwyb = (ImageView)this.findViewById(R.id.record2_fwyb_iv2);
		iv_wdbh_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(
						UslotteryRecord_wdbzhUpdateActivity.this)
						.setTitle("温馨提示!")
						.setMessage("选择拍照还是图库!")
						.setPositiveButton("拍照",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent(
												"android.media.action.IMAGE_CAPTURE");
										startActivityForResult(intent, 6);
										UslotteryRecord_wdbzhUpdateActivity.this
												.overridePendingTransition(
														R.anim.push_left_in,
														R.anim.push_left_out);
									}
								})
						.setNegativeButton("图库",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent(
												Intent.ACTION_GET_CONTENT);
										intent.addCategory(Intent.CATEGORY_OPENABLE);
										intent.setType("image/*");
										intent.putExtra("return-data", true);
										startActivityForResult(intent, 2);
										UslotteryRecord_wdbzhUpdateActivity.this
												.overridePendingTransition(
														R.anim.push_left_in,
														R.anim.push_left_out);
									}
								}).create().show();

			}
		});
		// iv_fwyb_add.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
		// intent.addCategory(Intent.CATEGORY_OPENABLE);
		// intent.setType("image/*");
		// intent.putExtra("return-data", true);
		// startActivityForResult(intent, 4);
		// }
		// });

		// rlkfx.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// //...............
		// wdbh= et_wdbh.getText().toString().trim();
		// if(wdbh.equals("")||(wdbh.length()!=5)){
		// DialogUtil.showDialog(UslotteryRecord.this, "请填写正确网点编号!");
		// return;
		// }
		// queryData(wdbh);
		// }
		// });
		rl = (RelativeLayout) this.findViewById(R.id.record2_wdbzh_up_rl);
		// rl.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent intent = new
		// Intent(UslotteryRecord_wdbzhActivity.this,UslotteryRecordStandardActivity.class);
		// UslotteryRecord_wdbzhActivity.this.startActivity(intent);
		// }
		// });

		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						UslotteryRecord_wdbzhUpdateActivity.this,
						UnRecordFormActivity.class);
				UslotteryRecord_wdbzhUpdateActivity.this.startActivity(intent);
				UslotteryRecord_wdbzhUpdateActivity.this.finish();
				UslotteryRecord_wdbzhUpdateActivity.this
						.overridePendingTransition(R.anim.push_right_in,
								R.anim.push_right_out);

			}
		});
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// if(options.equals("")){
				// DialogUtil.showDialog(UslotteryRecord_wdbzhUpdateActivity.this,
				// "请选好标准评分!");
				// return;
				// }
				// url = HttpUrl.URL+HttpUrl.Cr_Service
				// +"?wdbh="+wdbh+"&fwdh="+fwdh+"&date="+date+"&time="+time+"&state="+state+"&options="+options;
				// }else{
				// url = HttpUrl.URL+HttpUrl.Cr_Service
				// +"?wdbh="+wdbh+"&fwdh="+fwdh+"&date="+date+"&time="+time+"&state="+state+"&ssss=";
				// }
				if (Util.ExistSDCard()) {
					int size = ll.getChildCount();
					StringBuilder builder = new StringBuilder();
					for (int i = 0; i < size; i++) {
						ImageView iv = (ImageView) ll.getChildAt(i);
						String path = iv.getTag().toString();
						if (i == (size - 1)) {
							builder.append(path);
						} else {
							builder.append(path).append(",");
						}
						paths.add(path);
					}
					app.setWdzp(builder.toString());
				}

				Intent intent = new Intent(
						UslotteryRecord_wdbzhUpdateActivity.this,
						UslotteryRecord_UpdateActivity.class);
				intent.putExtra("sp", state + "");
				if (sp.getSelectedItemPosition() == 0) {
					if (score == -1) {
						intent.putExtra("score", app.getDf());
						intent.putExtra("options", app.getKfx());
					} else {
						intent.putExtra("score", score + "");
						intent.putExtra("options", options);
					}
				} else {
					intent.putExtra("score", "无");
					intent.putExtra("options", "无");
				}
				intent.putExtra("paths", paths);
				intent.putExtra("flag", 1 + "");
				intent.putExtra("app", app);
				// UslotteryRecord_wdbzhActivity.this.setResult(3, intent);
				UslotteryRecord_wdbzhUpdateActivity.this.startActivity(intent);
				UslotteryRecord_wdbzhUpdateActivity.this.finish();
				UslotteryRecord_wdbzhUpdateActivity.this
						.overridePendingTransition(R.anim.push_right_in,
								R.anim.push_right_out);
			}
		});
		// btn_time = (Button) this.findViewById(R.id.record2_btn_time);
		// kfxm.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// DialogUtil.showDialog(UslotteryRecord.this,options);
		// }
		// });
		// hs = (HorizontalScrollView) this.findViewById(R.id.record2_wdzp_hs);
		ll = (LinearLayout) this.findViewById(R.id.record2_wdbzh_up_wdzp_ll);
		if (!app.getWdzp().equals("")) {
			if (app.getWdzp().contains(",")) {
				String[] paths = app.getWdzp().split(",");
				for (int i = 0; i < paths.length; i++) {
					ImageView iv = new ImageView(this);
					iv.setPadding(0, 0, 15, 0);
					Bitmap bitMap = BitmapFactory.decodeFile(paths[i]);
					bitMap = Bitmap.createScaledBitmap(bitMap, 80, 80, false);
					iv.setLayoutParams(new LayoutParams(80,
							LayoutParams.FILL_PARENT));
					iv.setImageBitmap(bitMap);
					iv.setTag(paths[i]);

					iv.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							ImageView iv = (ImageView) v;
							Intent intent = new Intent();
							intent.setAction(android.content.Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile(new File(iv
									.getTag().toString())), "image/*");
							UslotteryRecord_wdbzhUpdateActivity.this
									.startActivity(intent);
						}
					});
					iv.setOnLongClickListener(new View.OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							final ImageView iv = (ImageView) v;

							// delete photo
							AlertDialog.Builder builder = new Builder(
									UslotteryRecord_wdbzhUpdateActivity.this);
							builder.setTitle("提示");
							builder.setMessage("确定删除此照片吗？");
							builder.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											ll.removeView(iv);
											dialog.dismiss();
										}

									});
							builder.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

											dialog.dismiss();
										}
									});
							builder.create().show();
							return false;
						}
					});
					ll.addView(iv);
				}

			} else {
				ImageView iv = new ImageView(this);
				iv.setPadding(0, 0, 15, 0);
				Bitmap bitMap = BitmapFactory.decodeFile(app.getWdzp());
				bitMap = Bitmap.createScaledBitmap(bitMap, 80, 80, false);
				iv.setLayoutParams(new LayoutParams(80,
						LayoutParams.FILL_PARENT));
				iv.setImageBitmap(bitMap);
				iv.setTag(app.getWdzp());

				iv.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ImageView iv = (ImageView) v;
						Intent intent = new Intent();
						intent.setAction(android.content.Intent.ACTION_VIEW);
						intent.setDataAndType(
								Uri.fromFile(new File(iv.getTag().toString())),
								"image/*");
						UslotteryRecord_wdbzhUpdateActivity.this
								.startActivity(intent);
						UslotteryRecord_wdbzhUpdateActivity.this
								.overridePendingTransition(R.anim.push_left_in,
										R.anim.push_left_out);

					}
				});
				iv.setOnLongClickListener(new View.OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						final ImageView iv = (ImageView) v;
						// delete photo
						AlertDialog.Builder builder = new Builder(
								UslotteryRecord_wdbzhUpdateActivity.this);
						builder.setTitle("提示");
						builder.setMessage("确定删除此照片吗？");
						builder.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										ll.removeView(iv);
										dialog.dismiss();
									}

								});
						builder.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										dialog.dismiss();
									}
								});
						builder.create().show();
						return false;
					}
				});
				ll.addView(iv);
			}
		}
		// paths = new ArrayList<String>();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮 //
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(UslotteryRecord_wdbzhUpdateActivity.this)
					.setTitle("提示！")
					.setMessage("是否退出修改")
					.setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									Intent intent = new Intent(
											UslotteryRecord_wdbzhUpdateActivity.this,
											UnRecordFormActivity.class);
									UslotteryRecord_wdbzhUpdateActivity.this
											.startActivity(intent);
									UslotteryRecord_wdbzhUpdateActivity.this
											.finish();
									UslotteryRecord_wdbzhUpdateActivity.this
											.overridePendingTransition(
													R.anim.push_right_in,
													R.anim.push_right_out);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// Toast.makeText(UslotteryRecord.this, requestCode+","+resultCode,
		// Toast.LENGTH_SHORT).show();
		if (resultCode == 3) {
			if (requestCode == 3) {
				app = (App) intent.getSerializableExtra("app");
				// options = intent.getStringExtra("options");
				// score = intent.getFloatExtra("score", -1);
				options = app.getKfx();
				score = Float.parseFloat(app.getDf());
				// Toast.makeText(UslotteryRecord.this, options,
				// Toast.LENGTH_SHORT).show();
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
				// kfxm.setText(sb.toString());
				df.setText("" + score);
			}
		}
		if (resultCode == RESULT_OK && intent != null) {
			if (requestCode == 2) {
				Bundle bundle = intent.getExtras();
				Uri originalUri = intent.getData();
				if (originalUri != null) {
					Bitmap bitMap = null;
					try {
						if (bitMap != null)
							bitMap.recycle();
						bitMap = null;
						ContentResolver resolver = getContentResolver();
						bitMap = MediaStore.Images.Media.getBitmap(resolver,
								originalUri);
						String[] proj = { MediaColumns.DATA };
						Cursor cursor = managedQuery(originalUri, proj, null,
								null, null); // 按我个人理解 这个是获得用户选择的图片的索引值
						int column_index = cursor
								.getColumnIndexOrThrow(MediaColumns.DATA);
						System.out.println("706-------------camera------"
								+ column_index);
						// 将光标移至开头 ，这个很重要，不小心很容易引起越界
						cursor.moveToFirst(); // 最后根据索引值获取图片路径
						// ByteArrayOutputStream out = new
						// ByteArrayOutputStream();

						// String path = cursor.getString(column_index);
						String _path = "/sdcard/b" + Util.getCurTime()
								+ ".jpeg";
						Toast.makeText(
								UslotteryRecord_wdbzhUpdateActivity.this,
								"正在检测压缩，请稍候...", Toast.LENGTH_SHORT).show();

						String path_last = ImageCompress
								.compressImage2(
										bitMap,
										UslotteryRecord_wdbzhUpdateActivity.this,
										_path);

						// paths.add(path);
						// Toast.makeText(this, "..."+path,
						// Toast.LENGTH_SHORT).show();
						// Bitmap map = scaleImg(bitMap, 250, 420);
						// map.compress(Bitmap.CompressFormat.JPEG, 25, out);
						// byte[] photo = out.toByteArray();
						// intTobyte(photo.length); // 图片的长度

						ImageView iv = new ImageView(this);
						iv.setPadding(0, 0, 15, 0);
						bitMap = Bitmap.createScaledBitmap(bitMap, 80, 80,
								false);
						iv.setLayoutParams(new LayoutParams(80,
								LayoutParams.FILL_PARENT));
						iv.setImageBitmap(bitMap);
						iv.setTag(path_last);

						iv.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								ImageView iv = (ImageView) v;
								// Toast.makeText(UslotteryRecord.this, +"...",
								// Toast.LENGTH_SHORT).show();
								Intent intent = new Intent();
								intent.setAction(android.content.Intent.ACTION_VIEW);
								intent.setDataAndType(Uri.fromFile(new File(iv
										.getTag().toString())), "image/*");
								UslotteryRecord_wdbzhUpdateActivity.this
										.startActivity(intent);
								UslotteryRecord_wdbzhUpdateActivity.this
										.overridePendingTransition(
												R.anim.push_left_in,
												R.anim.push_left_out);

							}
						});
						iv.setOnLongClickListener(new View.OnLongClickListener() {
							@Override
							public boolean onLongClick(View v) {
								final ImageView iv = (ImageView) v;

								// delete photo
								AlertDialog.Builder builder = new Builder(
										UslotteryRecord_wdbzhUpdateActivity.this);
								builder.setTitle("提示");
								builder.setMessage("确定删除此照片吗？");
								builder.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												ll.removeView(iv);
												dialog.dismiss();
											}
										});
								builder.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {

												dialog.dismiss();
											}
										});
								builder.create().show();
								return false;
							}
						});
						ll.addView(iv);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (requestCode == 6) {
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					return;
				}
				Bundle bundle = intent.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");
				File file = new File("/sdcard/");
				file.mkdirs();

				String fileName = "/sdcard/b" + Util.getCurTime() + ".jpg";
				String path_last = ImageCompress.compressImage2(bitmap,
						UslotteryRecord_wdbzhUpdateActivity.this, fileName);
				ImageView iv = new ImageView(this);
				iv.setPadding(0, 0, 15, 0);
				bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
				iv.setLayoutParams(new LayoutParams(80,
						LayoutParams.FILL_PARENT));
				iv.setImageBitmap(bitmap);
				// iv.setId(i++);
				iv.setTag(path_last);
				iv.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ImageView iv = (ImageView) v;
						Intent intent = new Intent();
						intent.setAction(android.content.Intent.ACTION_VIEW);
						intent.setDataAndType(
								Uri.fromFile(new File(iv.getTag().toString())),
								"image/*");
						UslotteryRecord_wdbzhUpdateActivity.this
								.startActivity(intent);
						UslotteryRecord_wdbzhUpdateActivity.this
								.overridePendingTransition(
										R.anim.push_right_in,
										R.anim.push_right_out);

					}
				});
				iv.setOnLongClickListener(new View.OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						final ImageView iv = (ImageView) v;

						// delete photo
						AlertDialog.Builder builder = new Builder(
								UslotteryRecord_wdbzhUpdateActivity.this);
						builder.setTitle("提示");
						builder.setMessage("确定删除此照片吗？");
						builder.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// Toast.makeText(UslotteryRecord.this,
										// iv.getId()+"...",
										// Toast.LENGTH_SHORT).show();
										// paths.remove(paths.get(iv.getId()));
										ll.removeView(iv);
										dialog.dismiss();
									}
								});
						builder.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						builder.create().show();
						return false;
					}
				});
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
				if (HttpUtil.checkNet(UslotteryRecord_wdbzhUpdateActivity.this)) {
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
	/*
	 * tableLayout tab = new TableLayout(this); 然后设置属性. mSpinnerAge = new
	 * Spinner(context); mSpinnerAge.setLayoutParams(paramsw); ageList =
	 * super.defaultAgeList; ArrayAdapter<String> adapter = new
	 * ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
	 * ageList);//ageList是数组
	 * adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item
	 * ); mSpinnerAge.setAdapter(adapter); 多选框 checkBox cb = new CheckBox(this);
	 * cb.setChecked(true);
	 * 
	 * cb.setOnCheckedChangeListener( new
	 * CompoundButton.OnCheckedChangeListener() {
	 * 
	 * @Override public void onCheckedChanged( CompoundButton buttonView,
	 * boolean isChecked) { CheckBox checkBox = (CheckBox) buttonView;
	 * Log.v("Test", String.valueOf(checkBox.getText()) + " is " +
	 * String.valueOf(isChecked)); }
	 * 
	 * tab.addview(sp); tab.addView(checkbox);
	 */
}