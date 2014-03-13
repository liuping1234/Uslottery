package com.uslotter.lingtu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lingtu.mapapi.GeoPoint;
import com.lingtu.mapapi.ItemizedOverlay;
import com.lingtu.mapapi.MapActivity;
import com.lingtu.mapapi.MapController;
import com.lingtu.mapapi.MapView;
import com.lingtu.mapapi.MyLocationOverlay;
import com.lingtu.mapapi.Overlay;
import com.lingtu.mapapi.OverlayItem;
import com.lingtu.mapapi.Projection;
import com.cr.uslotter.R;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;
import com.uslotter.util.Util;

public class lingtuMapActivity extends MapActivity implements OnClickListener{
	ProgressDialog pBar =null;
	static MapView mMapView=null;
	MapController mController;
	private MyLocationOverlayProxy mLocationOverlay;
	Double jd;
	Double wd;
	Button back=null;
	Button setting=null;
	private static final int SUCCESS=100;//判断定位成功时的状态值
	private String poiType;
	private GeoPoint startPoint = null;
	private GeoPoint endPoint = null;
	private MapPointOverlay overlay;
	int wdId=0;//网点id
	String wdTitle=null;
	// 加载库文件
		static {
			System.loadLibrary("gpsencryption");
		}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		jd=getIntent().getDoubleExtra("jd",0.0);
		wd=getIntent().getDoubleExtra("wd",0.0);
		wdId=getIntent().getIntExtra("wdId", 0);
		wdTitle=getIntent().getStringExtra("wdTitle");
		System.out.println(jd+","+wd);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
		
		back=(Button) findViewById(R.id.wdmapback);
		setting=(Button) findViewById(R.id.wdmapsetting);
		back.setOnClickListener(this);
		setting.setOnClickListener(this);
		
		
		mMapView=(MapView)findViewById(R.id.mapView);
		//当前位置
     	mLocationOverlay = new MyLocationOverlayProxy(this, mMapView);
		mLocationOverlay.enableMyLocation();
		mLocationOverlay.enableCompass();
		
		
		mController = mMapView.getController();
		if(wd !=0.0&&jd!=0.0){
			GeoPoint point2=new GeoPoint((int) (wd * 1E5), (int) (jd * 1E5));
			mController.setCenter(point2);
		}else{//定位到当前位置
			LocationManager alm =
					(LocationManager)lingtuMapActivity.this.getSystemService( Context.LOCATION_SERVICE );
					if(alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER )){
						Toast.makeText(getApplicationContext(), "正在定位中...", 0).show();
						// 实现初次定位使定位结果居中显示
						mLocationOverlay.runOnFirstFix(new Runnable() {

							@Override
							public void run() {
								handler.sendMessage(Message.obtain(handler, SUCCESS));
							}
						});
					}else{
						Toast.makeText(lingtuMapActivity.this, "GPS未打开", Toast.LENGTH_SHORT ).show();
						AlertDialog.Builder builder = new AlertDialog.Builder(lingtuMapActivity.this);
						builder.setTitle("GPS设置");
					   	builder.setMessage("是否去打开GPS");
					   	builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){
						    	  @Override
								  public void onClick(DialogInterface dialog, int which){
						    		  Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						    		  startActivity(myIntent);
						    	  }
					   	});	
					   	builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){
					    	  @Override
							  public void onClick(DialogInterface dialog, int which){
					    	  }
				   	});			
					   	builder.create().show(); 
					}
		}
		mController.setZoom(12);
		overlay = new MapPointOverlay(this);
		mMapView.setBuiltInZoomControls(true);
		Drawable marker=getResources().getDrawable(R.drawable.lt_search_balloon);
		marker.setBounds(0,0,marker.getIntrinsicWidth(),marker.getIntrinsicHeight());//为marker定义位置和边界
		
	
		mMapView.getOverlays().add(new OverItemT(marker, this));
		mMapView.getOverlays().add(mLocationOverlay);
	}
	
	@Override
	protected void onPause() {
		this.mLocationOverlay.disableMyLocation();
		super.onPause();
	}

	@Override
	protected void onResume() {
		this.mLocationOverlay.enableMyLocation();
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == SUCCESS) {
				System.out.println("getApplicationContext()是：：：："+getApplicationContext());
				mController.animateTo(mLocationOverlay.getMyLocation());
			}
		}
	};
	//多个覆盖物，类型相同，显示方式相同，请使用此类
	class OverItemT extends ItemizedOverlay{
		private List<OverlayItem> mGeoList=new ArrayList<OverlayItem>();
		private Drawable marker;
		private Context mContext;
		
		/*private double mLat1=23.1201;//point1纬度
		private double mLon1=113.2752;//point1经度
		
		private double mLat2=23.0971;
		private double mLon2=113.2772;*/
		
		//private double mLat3=39.917723;
		//private double mLon3=116.6552;

		public OverItemT(Drawable marker,Context context) {
			super(boundCenterBottom(marker));
			// TODO Auto-generated constructor stub
			this.marker=marker;
			this.mContext=context;
			 // 用给定的经纬度构造GeoPoint
			GeoPoint p1 = new GeoPoint((int) (wd * 1E5), (int) (jd * 1E5));
			// 构造OverlayItem的三个参数依次为：item的位置，标题文本，文字片段
			mGeoList.add(new OverlayItem(p1, "", wdTitle));
			populate();  //createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
		}
		
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			  // Projection接口用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换
			Projection projection=mapView.getProjection();
			for(int index=size()-1;index>=0;index--){//遍历GeoList
				OverlayItem overLayItem=getItem(index);// 得到给定索引的item
				String title=overLayItem.getTitle();
				//把经纬度变换到相对于MapView左上角的屏幕像素坐标
				Point point=projection.toPixels(overLayItem.getPoint(),new Point());
				//可在此处添加您的绘制代码
				Paint paintText=new Paint();
				paintText.setColor(Color.RED);
				paintText.setFakeBoldText(true);
				paintText.setTextSize(25);
				canvas.drawText(title, point.x-30, point.y, paintText);// 绘制文本
			}
			// TODO Auto-generated method stub
			super.draw(canvas, mapView, shadow);
			//调整一个drawable边界，使得（0，0）是这个drawable底部最后一行中心的一个像素
			boundCenterBottom(marker);
		}



		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return mGeoList.get(i);
		}
		
		@Override
		public int size() {
			// TODO Auto-generated method stub
			return mGeoList.size();
		}
		
		@Override
		// 处理当点击事件
		protected boolean onTap(int i) {
			// TODO Auto-generated method stub
			setFocus(mGeoList.get(i));
			Toast.makeText(this.mContext, mGeoList.get(i).getSnippet(),
					0).show();
					return true;
		}
		
		@Override
		public boolean onTap(GeoPoint p, MapView mapView) {
			// TODO Auto-generated method stub
			return super.onTap(p, mapView);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	class MyLocationOverlayProxy extends MyLocationOverlay {
		private Location mLocation;
		private final LinkedList<Runnable> mRunOnFirstFix = new LinkedList<Runnable>();

		public MyLocationOverlayProxy(Context arg0, MapView arg1) {
			super(arg0, arg1);
			// TODO Auto-generated constructor stub
		}

		public boolean runOnFirstFix(final Runnable runnable) {
			if (mLocation != null) {
				new Thread(runnable).start();
				// runnable.run();
				return true;
			} else {
				mRunOnFirstFix.addLast(runnable);
				return false;
			}
		}

		@Override
		public void onLocationChanged(Location location) {
			mLocation = location;
			for (final Runnable runnable : mRunOnFirstFix) {
				new Thread(runnable).start();
			}
			mRunOnFirstFix.clear();
			// TODO Auto-generated method stub
			super.onLocationChanged(location);
		}
	}
	
	@Override	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  //按下键盘上返回按钮 //
	  if(keyCode == KeyEvent.KEYCODE_BACK){ 
		  finish();
		  return true;
	 
	  }else{  
	   return super.onKeyDown(keyCode, event);	 
	  }	 
	 }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.wdmapback:
			//返回
			finish();
			break;
		case R.id.wdmapsetting:
			//选项
			//重新标注地址，规划路线
			showDialog(0*113);
			break;
		default:
			break;
		}
	}
	
	public Dialog onCreateDialog(int id,Bundle state){
		switch (id) {
		case 0*113:
			final String [] names=new String[]{"定位","重新标注网点"};
			final int [] image=new int[]{R.drawable.marker_gpstracker,R.drawable.iconmarka};
			Builder b=new AlertDialog.Builder(this);
			b.setIcon(R.drawable.qqmail_attach_icon_pressed);
			b.setTitle("选项");
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			for(int i=0;i<names.length;i++){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("image",image[i]);
				map.put("name",names[i]);
				list.add(map);
			}
			SimpleAdapter adapter=new SimpleAdapter(this,
					list, 
					R.layout.row, 
					new String[]{"image","name"}, 
					new int[]{R.id.mapdialog_image,R.id.mapdialog_name});
			b.setAdapter(adapter, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					switch (which) {
					case 0:
						LocationManager alm =
						(LocationManager)lingtuMapActivity.this.getSystemService( Context.LOCATION_SERVICE );
						if(alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER )){
							Toast.makeText(getApplicationContext(), "正在定位中...", 0).show();
							// 实现初次定位使定位结果居中显示
							mLocationOverlay.runOnFirstFix(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									handler.sendMessage(Message.obtain(handler, SUCCESS));
								}
							});
						}else{
							Toast.makeText(lingtuMapActivity.this, "GPS未打开", Toast.LENGTH_SHORT ).show();
							AlertDialog.Builder builder = new AlertDialog.Builder(lingtuMapActivity.this);
							builder.setTitle("GPS设置");
						   	builder.setMessage("是否去打开GPS");
						   	builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){
							    	  @Override
									  public void onClick(DialogInterface dialog, int which){
							    		  Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							    		  startActivity(myIntent);
							    	  }
						   	});	
						   	builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){
						    	  @Override
								  public void onClick(DialogInterface dialog, int which){
						    	  }
					   	});			
						   	builder.create().show(); 
						}
						break;
					case 1:
						//重新标注
						if(HttpUtil.checkNet(lingtuMapActivity.this)){
							setting.setEnabled(false);
							Toast.makeText(lingtuMapActivity.this, "在地图上点击您的网点地址", Toast.LENGTH_SHORT).show();
							poiType = "startPoint";
							mMapView.getOverlays().add(overlay);
						}else{
							showDialog( "网络连接失败，无法使用此功能");
						}
						
						break;
					case 2:
						//导航
						Toast.makeText(lingtuMapActivity.this, names[which], Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
					}
				}
			});
			return b.create();
		}
		return null;
	}
	//点击标注
	public class MapPointOverlay extends Overlay {
		private Context context;
		private LayoutInflater inflater;
		private View popUpView;

		public MapPointOverlay(Context context) {
			this.context = context;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			// TODO Auto-generated method stub
			super.draw(canvas, mapView, shadow);
		}

		@Override
		public boolean onTap(final GeoPoint point, final MapView view) {
			// TODO Auto-generated method stub
			if (popUpView != null) {
				view.removeView(popUpView);
			}
			// Projection接口用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换
			popUpView = inflater.inflate(R.layout.point, null);
			TextView tv_jd=(TextView) popUpView.findViewById(R.id.point_jd);
			TextView tv_wd=(TextView) popUpView.findViewById(R.id.point_wd);
			Button bt_commint=(Button) popUpView.findViewById(R.id.point_commint);
			
			int wd_1=point.getLatitudeE5();//纬度
			int jd_1=point.getLongitudeE5();//经度
			jd=jd_1/1E5;
			wd=wd_1/1E5;
			tv_jd.setText("经度："+jd);
			tv_wd.setText("纬度："+wd);
			
			Toast.makeText(lingtuMapActivity.this, "纬度："+wd_1/1E5+","+"经度"+jd_1/1E5, Toast.LENGTH_SHORT).show();;
			MapView.LayoutParams lp;
			lp = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
					MapView.LayoutParams.WRAP_CONTENT, point, 0, 0,
					MapView.LayoutParams.BOTTOM_CENTER);
			view.addView(popUpView, lp);
			bt_commint.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					/*
					 * 上传经纬度到服务器
					 */
					showProgressDialog(lingtuMapActivity.this,"提示信息","提交数据，请稍后...",ProgressDialog.STYLE_SPINNER);
					final Handler handler = new Handler(){
						@Override
						public void handleMessage(Message msg){
							if(msg.what==1){
								Intent intent=new Intent();
								intent.putExtra("jd", jd);
								intent.putExtra("wd", wd);
								intent.putExtra("wdId", wdId);
								intent.putExtra("wdTitle", wdTitle);
								intent.setAction("android.intent.action.lingtuMapActivity");
								startActivity(intent);
								finish();
							}else{
								
							}
						}
					};
					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int flag=post(jd,wd);
							Message m=new Message();
							if(flag==1){
								m.what=1;
							}else{
								m.what=0;
							}
							pBar.cancel();
							handler.sendMessage(m);
						}
					}.start();
				}
			});
			return super.onTap(point, view);
		}
	}
	//上传经纬度
	private int post(double jd,double wd){
		int Code=0;//
		Map<String ,String> rawParams=new HashMap<String,String>();
		String [] ltbm=convertJW(jd, wd).split("-");
		rawParams.put("jd", jd+"");
		rawParams.put("wd", wd+"");
		rawParams.put("UserId",Util.getSharedPrefercencesString(lingtuMapActivity.this,"uid"));
		rawParams.put("lt1", ltbm[0]);
		rawParams.put("lt2", ltbm[1]);
		rawParams.put("lt3", ltbm[2]);
		rawParams.put("wdId",wdId+"");
		if(HttpUtil.checkNet(lingtuMapActivity.this)){
			try {
				JSONArray jsonArray=new JSONArray(HttpUtil.postRequest(HttpUrl.URL+HttpUrl.MapUpdate,rawParams));
				System.out.println(jsonArray);
					JSONObject obj=(JSONObject)jsonArray.get(0);
					Code=(Integer) obj.get("Msg");
			}catch (Exception e) {
				Log.e("handle", e.getMessage());
			}
		}else{
			
		}
		return Code;
	}
	
	//显示等待对话框
	private void showProgressDialog(Context ctx,String title, String msg,int style){
			pBar = new ProgressDialog(ctx);
			pBar.setTitle(title);
			pBar.setMessage(msg);
			pBar.setProgressStyle(style);
			pBar.show();
		}
	//弹出对话框
	public void showDialog(String message){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);	    
		   	builder.setMessage(message).setCancelable(false);
		   	builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){
			    	  @Override
					  public void onClick(DialogInterface dialog, int which){
			    		  
			    	  }
		   	});		
		   	builder.create().show(); 
		}
	public String convertJW(double longitude,double latitude){
		if(longitude>=100) {longitude-=100;}
	    longitude *= 10000;
	    latitude *= 10000;
	    String lt="";
	   String str1 = String.valueOf(longitude);
	   String str2 = String.valueOf(latitude);
	    if(str2.length()<6) str2 = "0" + str2;
		    lt =    String.valueOf(str2.charAt(4));
		    lt += str2.charAt(1);
		    lt += String.valueOf(str1.charAt(3));
		    lt += String.valueOf(str2.charAt(5));
		    lt += "-";
		    lt += String.valueOf(str2.charAt(3));
		    lt += String.valueOf(str1.charAt(4));
		    lt += String.valueOf(str1.charAt(1));
		    lt += String.valueOf(str1.charAt(2));
		    lt += "-";
		    lt += String.valueOf(str1.charAt(5));
		    lt += String.valueOf(str2.charAt(0));
		    lt += String.valueOf(str2.charAt(2));
		    lt += String.valueOf(str1.charAt(0));
		    System.out.println(lt);
		    return lt;
  }
}