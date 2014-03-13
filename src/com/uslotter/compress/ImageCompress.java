package com.uslotter.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.cr.uslotter.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.uslotter.util.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.text.TextUtils;
import android.widget.Toast;

public class ImageCompress {
    // 实现图片质量压缩
    public static Bitmap compressImage(Bitmap image, Context c) {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
	int options = 100;
	while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩

	    baos.reset();// 重置baos即清空baos
	    options -= 10;// 每次都减少10
	    image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
	}
	ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
	Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
	return bitmap;
    }

    // 实现图片质量压缩
    public static String compressImage2(Bitmap image, Context c, String path2) {

	try {
	    boolean isCompressed = false;
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
	    int options = 100;
	    int size = (baos.toByteArray().length / 1024);

	    while ((baos.toByteArray().length / 1024) > 300) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
		isCompressed = true;
		baos.reset();// 重置baos即清空baos
		options -= 10;// 每次都减少10
		image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
	    }
	    //
	    if (isCompressed) {
		Toast.makeText(
			c,
			"文件大小:" + size + "k,压缩后大小:"
				+ (baos.toByteArray().length / 1024) + "k",
			Toast.LENGTH_SHORT).show();

	    } else {
		Toast.makeText(c, "文件小于300k,不需要压缩", Toast.LENGTH_SHORT).show();
	    }
	    ByteArrayInputStream isBm = new ByteArrayInputStream(
		    baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
	    File file = new File(path2);
	    OutputStream fos = new FileOutputStream(file);
	    int len = -1;
	    byte[] buf = new byte[256];
	    while ((len = isBm.read(buf)) != -1) {
		fos.write(buf, 0, len);
	    }
	    return path2;

	} catch (Exception e) {
	    e.printStackTrace();
	    return "";
	} finally {
	    image = null;
	    // if(!image.isRecycled()){
	    // image.recycle();
	    // image = null;
	    // System.gc();
	    // }
	}
    }
    
    public static String compressImage3(Bitmap image, Context c, String path2) {
	try {
	    boolean isCompressed = false;
	    // 旋转90度

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
	    int options = 100;
	    int size = (baos.toByteArray().length / 1024);

	    while ((baos.toByteArray().length / 1024) > 200) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
		isCompressed = true;
		baos.reset();// 重置baos即清空baos
		options -= 10;// 每次都减少10
		image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
	    }
	    //
	    if (isCompressed) {
		Toast.makeText(c,"文件大小:" + size + "k,压缩后大小:"+ 
				(baos.toByteArray().length / 1024) + "k",Toast.LENGTH_SHORT).show();

	    } else {
		Toast.makeText(c, "文件小于200k,不需要压缩", Toast.LENGTH_SHORT).show();
	    }
	    // 把压缩后的数据baos存放到ByteArrayInputStream中
	    ByteArrayInputStream isBm = new ByteArrayInputStream( baos.toByteArray());
	    File file = new File(path2);
	    OutputStream fos = new FileOutputStream(file);
	    int len = -1;
	    byte[] buf = new byte[256];
	    while ((len = isBm.read(buf)) != -1) {
		fos.write(buf, 0, len);
	    }

	    return path2;

	} catch (Exception e) {
	    e.printStackTrace();
	    return "";
	} finally {
	    image = null;

	}
    }

    // 根据路径实现图片比例压缩
    public static Bitmap getimage(String srcPath, Context c) {
	BitmapFactory.Options newOpts = new BitmapFactory.Options();
	// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
	newOpts.inJustDecodeBounds = true;
	Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

	newOpts.inJustDecodeBounds = false;
	int w = newOpts.outWidth;
	int h = newOpts.outHeight;
	// 现在主流手机比较多是960*5400分辨率，所以高和宽我们设置为
	float hh = 800f;// 这里设置高度为960f
	float ww = 480f;// 这里设置宽度为540f
	// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
	int be = 1;// be=1表示不缩放
	if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
	    be = (int) (newOpts.outWidth / ww);
	} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
	    be = (int) (newOpts.outHeight / hh);
	}
	if (be <= 0)
	    be = 1;
	newOpts.inSampleSize = be;// 设置缩放比例
	// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
	bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
	return compressImage(bitmap, c);// 压缩好比例大小后再进行质量压缩
    }

    // 根据原图片实现比例压缩
    public static String comp(Bitmap image, Context c, String path2) {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
	    baos.reset();// 重置baos即清空baos
	    image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
	}
	ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
	BitmapFactory.Options newOpts = new BitmapFactory.Options();
	// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
	newOpts.inJustDecodeBounds = true;
	Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
	newOpts.inJustDecodeBounds = false;
	int w = newOpts.outWidth;
	int h = newOpts.outHeight;
	// 现在主流手机比较多是960*540分辨率，所以高和宽我们设置为
	float hh = 800f;// 这里设置高度为960f
	float ww = 480f;// 这里设置宽度为540f
	// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
	int be = 1;// be=1表示不缩放
	if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
	    be = (int) (newOpts.outWidth / ww);
	} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
	    be = (int) (newOpts.outHeight / hh);
	}
	if (be <= 0)
	    be = 1;
	newOpts.inSampleSize = be;// 设置缩放比例
	// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
	isBm = new ByteArrayInputStream(baos.toByteArray());
	bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
	return compressImage2(bitmap, c, path2);// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap decodeFile(String pathName, int width, int height) {
	Bitmap bitmap = null;
	if (isExist(pathName)) {
	    BitmapFactory.Options opts = new BitmapFactory.Options();
	    opts.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(pathName, opts);
	    opts.inSampleSize = computeSampleSize(opts, -1, width * height);
	    opts.inJustDecodeBounds = false;

	    opts.inPurgeable = true;
	    opts.inInputShareable = true;
	    opts.inPreferredConfig = android.graphics.Bitmap.Config.ALPHA_8;
	    opts.inDither = true;

	    try {
		bitmap = BitmapFactory.decodeFile(pathName, opts);
	    } catch (OutOfMemoryError e) {
		System.gc();
		try {
		    bitmap = BitmapFactory.decodeFile(pathName, opts);
		} catch (OutOfMemoryError e1) {
		}
	    } catch (Exception e2) {
	    }
	}
	if (bitmap == null) {
	    bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
	}
	return bitmap;
    }

    private static int computeSampleSize(BitmapFactory.Options options,
	    int minSideLength, int maxNumOfPixels) {
	int initialSize = computeInitialSampleSize(options, minSideLength,
		maxNumOfPixels);
	int roundedSize;
	if (initialSize <= 8) {
	    roundedSize = 1;
	    while (roundedSize < initialSize) {
		roundedSize <<= 1;
	    }
	} else {
	    roundedSize = (initialSize + 7) / 8 * 8;
	}

	return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
	    int minSideLength, int maxNumOfPixels) {
	double w = options.outWidth;
	double h = options.outHeight;

	int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
		.sqrt(w * h / maxNumOfPixels));
	int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
		Math.floor(w / minSideLength), Math.floor(h / minSideLength));

	if (upperBound < lowerBound) {
	    // return the larger one when there is no overlapping zone.
	    return lowerBound;
	}

	if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
	    return 1;
	} else if (minSideLength == -1) {
	    return lowerBound;
	} else {
	    return upperBound;
	}
    }

    /**
     * 判断文件或文件夹是否存在
     * 
     * @param path
     * @return true 文件存在
     */
    public static boolean isExist(String path) {
	if (TextUtils.isEmpty(path)) {
	    return false;
	}
	boolean exist = false;
	try {
	    File file = new File(path);
	    exist = file.exists();
	} catch (Exception e) {
	    return false;
	}
	return exist;
    }

    public static DisplayImageOptions configPic() {
	DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.biz_news_specialmedia_default_bg)
		.showImageForEmptyUri(R.drawable.biz_news_specialmedia_default_bg)
		.showImageOnFail(R.drawable.biz_news_specialmedia_default_bg)
		.cacheInMemory(true).cacheOnDisc(true)
		// .displayer(new FadeInBitmapDisplayer(400))
		.bitmapConfig(Bitmap.Config.RGB_565).build();

	return options;

    }

}