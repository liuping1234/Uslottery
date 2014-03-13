package com.lot.map;


import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lingtu.mapapi.GeoPoint;
import com.lingtu.mapapi.MapActivity;
import com.lingtu.mapapi.MapController;
import com.lingtu.mapapi.MapView;
import com.lingtu.mapapi.Overlay;
import com.cr.uslotter.R;

/**
 * Description:
 * <br/>site: <a href="http://www.crazyit.org">crazyit.org</a> 
 * <br/>Copyright (C), 2001-2012, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */

// 必须继承MapActivity
public class AddressLocMap extends MapActivity
{
	// 定义界面上的的可视化控件
	Button locBn;
	MapView mv;
	EditText etAddress;
	// 定义MapController对象
	MapController controller;
	Bitmap posBitmap;
	@Override
	public void onCreate(Bundle status)
	{
		super.onCreate(status);
		setContentView(R.layout.wdmap);
		posBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.pos);
		// 获得界面上MapView对象
		mv = (MapView) findViewById(R.id.mv);
		etAddress = (EditText) findViewById(R.id.address);
		// 设置显示放大、缩小控制的按钮
		mv.setBuiltInZoomControls(true);	
		// 创建MapController对象
		controller = mv.getController();
		 // 获得Button对象
		locBn = (Button) findViewById(R.id.loc);
		String address="广东省广州市天河区";//"广东省广州市"+getIntent().getStringExtra("address");
		etAddress.setText(address);
		locBn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View source)
			{
				String address = etAddress.getEditableText().toString().trim();
				if (address.equals(""))
				{ // 判断是否输入空值
					Toast.makeText(AddressLocMap.this, "请输入有效的地址!",
						Toast.LENGTH_LONG).show();
					return;
				}
				// 调用ConvertUtil执行地址解析
				double[] results = ConvertUtil.getLocationInfo(address);
				// 调用方法更新MapView
				updateMapView(results[0], results[1]); 
			}
		});
		// 触发按钮的单击事件
		locBn.performClick();
		
		//viewMap(address);
	}
	
	public void viewMap(String address){
		if (address.equals(""))
		{ // 判断是否输入空值
			Toast.makeText(AddressLocMap.this, "请输入有效的地址!",
				Toast.LENGTH_LONG).show();
			return;
		}
		// 调用ConvertUtil执行地址解析
		double[] results = ConvertUtil.getLocationInfo(address);
		// 调用方法更新MapView
		updateMapView(results[0], results[1]); 
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		return true;
	}

	// 根据经度、纬度将MapView定位到指定地点的方法
	private void updateMapView(double lng, double lat)
	{
		GeoPoint gp = new GeoPoint((int) (lat * 1E6)
			, (int) (lng * 1E6));
		// 设置显示放大缩小按钮
		mv.displayZoomControls(true);
		// 将地图移动到指定的地理位置
		controller.animateTo(gp); 
		// 获得MapView上原有的Overlay对象
		List<Overlay> ol = mv.getOverlays();
		// 清除原有的Overlay对象
		ol.clear();
		// 添加一个新的OverLay对象
		ol.add(new PosOverLay(gp, posBitmap)); 
	}
}
//stl:vector deque list Mutiset set Mutimap map statck queue string