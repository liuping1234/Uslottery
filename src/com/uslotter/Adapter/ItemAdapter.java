package com.uslotter.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.cr.uslotter.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.uslotter.compress.ImageCompress;
import com.uslotter.mode.GridViewBean;

/**
 * selectPicµÄÊÊÅäÆ÷Àà
 * @author liu
 *
 */
public class ItemAdapter extends BaseAdapter {
    private Context context;
   
    private LayoutInflater mInflater;
   
    private ArrayList<GridViewBean> listData;
    /**Ç°×º*/
    private String pr = "file://";

    public ItemAdapter(Context context) {
	this.context = context;
	mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addDatas(ArrayList<GridViewBean> listData) {
	this.listData = listData;
	notifyDataSetChanged();
    }

    @Override
    public int getCount() {
	if (listData != null) {
	    return listData.size();
	}
	return 0;
    }

    @Override
    public Object getItem(int position) {

	return listData.get(position);
    }

    @Override
    public long getItemId(int position) {

	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	final Cache cache;
	if (convertView == null || (Cache) convertView.getTag() == null) {
	    cache = new Cache();
	    convertView = mInflater.inflate(R.layout.gridview_picselect_item, null);
	    cache.iv1 = (ImageView) convertView.findViewById(R.id.image_pic);
	    cache.iv2 = (ImageView) convertView.findViewById(R.id.image_select);
	    convertView.setTag(cache);
	} else {
	    cache = (Cache) convertView.getTag();
	}
//	cache.iv1.setImageBitmap(listData.get(position).getBm());

	ImageLoader.getInstance().displayImage(pr+listData.get(position).getPath(), cache.iv1, ImageCompress.configPic());
	
	
	if (listData.get(position).isCheckState()) {
	    cache.iv2.setVisibility(View.VISIBLE);
	} else {
	    cache.iv2.setVisibility(View.GONE);
	}
	return convertView;
    }

    class Cache {
	ImageView iv1;
	ImageView iv2;
	
    }
}
