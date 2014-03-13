package com.uslotter;


import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.cr.uslotter.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
	initImageLoader(this);
        super.onCreate();
        initImageLoader(getApplicationContext());
    }
    
    private void initImageLoader(Context context) {
	int width = context.getResources().getDisplayMetrics().widthPixels;
	int height = context.getResources().getDisplayMetrics().heightPixels;
	ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			.memoryCacheExtraOptions(width, height)
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.denyCacheImageMultipleSizesInMemory()
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			.memoryCacheSizePercentage(50) // default
			.build();
	ImageLoader.getInstance().init(config);
}
    
}
