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

// ����̳�MapActivity
public class AddressLocMap extends MapActivity
{
	// ��������ϵĵĿ��ӻ��ؼ�
	Button locBn;
	MapView mv;
	EditText etAddress;
	// ����MapController����
	MapController controller;
	Bitmap posBitmap;
	@Override
	public void onCreate(Bundle status)
	{
		super.onCreate(status);
		setContentView(R.layout.wdmap);
		posBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.pos);
		// ��ý�����MapView����
		mv = (MapView) findViewById(R.id.mv);
		etAddress = (EditText) findViewById(R.id.address);
		// ������ʾ�Ŵ���С���Ƶİ�ť
		mv.setBuiltInZoomControls(true);	
		// ����MapController����
		controller = mv.getController();
		 // ���Button����
		locBn = (Button) findViewById(R.id.loc);
		String address="�㶫ʡ�����������";//"�㶫ʡ������"+getIntent().getStringExtra("address");
		etAddress.setText(address);
		locBn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View source)
			{
				String address = etAddress.getEditableText().toString().trim();
				if (address.equals(""))
				{ // �ж��Ƿ������ֵ
					Toast.makeText(AddressLocMap.this, "��������Ч�ĵ�ַ!",
						Toast.LENGTH_LONG).show();
					return;
				}
				// ����ConvertUtilִ�е�ַ����
				double[] results = ConvertUtil.getLocationInfo(address);
				// ���÷�������MapView
				updateMapView(results[0], results[1]); 
			}
		});
		// ������ť�ĵ����¼�
		locBn.performClick();
		
		//viewMap(address);
	}
	
	public void viewMap(String address){
		if (address.equals(""))
		{ // �ж��Ƿ������ֵ
			Toast.makeText(AddressLocMap.this, "��������Ч�ĵ�ַ!",
				Toast.LENGTH_LONG).show();
			return;
		}
		// ����ConvertUtilִ�е�ַ����
		double[] results = ConvertUtil.getLocationInfo(address);
		// ���÷�������MapView
		updateMapView(results[0], results[1]); 
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		return true;
	}

	// ���ݾ��ȡ�γ�Ƚ�MapView��λ��ָ���ص�ķ���
	private void updateMapView(double lng, double lat)
	{
		GeoPoint gp = new GeoPoint((int) (lat * 1E6)
			, (int) (lng * 1E6));
		// ������ʾ�Ŵ���С��ť
		mv.displayZoomControls(true);
		// ����ͼ�ƶ���ָ���ĵ���λ��
		controller.animateTo(gp); 
		// ���MapView��ԭ�е�Overlay����
		List<Overlay> ol = mv.getOverlays();
		// ���ԭ�е�Overlay����
		ol.clear();
		// ���һ���µ�OverLay����
		ol.add(new PosOverLay(gp, posBitmap)); 
	}
}
//stl:vector deque list Mutiset set Mutimap map statck queue string