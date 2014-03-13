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
    // ʵ��ͼƬ����ѹ��
    public static Bitmap compressImage(Bitmap image, Context c) {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
	int options = 100;
	while (baos.toByteArray().length / 1024 > 100) { // ѭ���ж����ѹ����ͼƬ�Ƿ����500kb,���ڼ���ѹ��

	    baos.reset();// ����baos�����baos
	    options -= 10;// ÿ�ζ�����10
	    image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��
	}
	ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
	Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ
	return bitmap;
    }

    // ʵ��ͼƬ����ѹ��
    public static String compressImage2(Bitmap image, Context c, String path2) {

	try {
	    boolean isCompressed = false;
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
	    int options = 100;
	    int size = (baos.toByteArray().length / 1024);

	    while ((baos.toByteArray().length / 1024) > 300) { // ѭ���ж����ѹ����ͼƬ�Ƿ����500kb,���ڼ���ѹ��
		isCompressed = true;
		baos.reset();// ����baos�����baos
		options -= 10;// ÿ�ζ�����10
		image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��
	    }
	    //
	    if (isCompressed) {
		Toast.makeText(
			c,
			"�ļ���С:" + size + "k,ѹ�����С:"
				+ (baos.toByteArray().length / 1024) + "k",
			Toast.LENGTH_SHORT).show();

	    } else {
		Toast.makeText(c, "�ļ�С��300k,����Ҫѹ��", Toast.LENGTH_SHORT).show();
	    }
	    ByteArrayInputStream isBm = new ByteArrayInputStream(
		    baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
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
	    // ��ת90��

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
	    int options = 100;
	    int size = (baos.toByteArray().length / 1024);

	    while ((baos.toByteArray().length / 1024) > 200) { // ѭ���ж����ѹ����ͼƬ�Ƿ����500kb,���ڼ���ѹ��
		isCompressed = true;
		baos.reset();// ����baos�����baos
		options -= 10;// ÿ�ζ�����10
		image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��
	    }
	    //
	    if (isCompressed) {
		Toast.makeText(c,"�ļ���С:" + size + "k,ѹ�����С:"+ 
				(baos.toByteArray().length / 1024) + "k",Toast.LENGTH_SHORT).show();

	    } else {
		Toast.makeText(c, "�ļ�С��200k,����Ҫѹ��", Toast.LENGTH_SHORT).show();
	    }
	    // ��ѹ���������baos��ŵ�ByteArrayInputStream��
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

    // ����·��ʵ��ͼƬ����ѹ��
    public static Bitmap getimage(String srcPath, Context c) {
	BitmapFactory.Options newOpts = new BitmapFactory.Options();
	// ��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
	newOpts.inJustDecodeBounds = true;
	Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// ��ʱ����bmΪ��

	newOpts.inJustDecodeBounds = false;
	int w = newOpts.outWidth;
	int h = newOpts.outHeight;
	// ���������ֻ��Ƚ϶���960*5400�ֱ��ʣ����ԸߺͿ���������Ϊ
	float hh = 800f;// �������ø߶�Ϊ960f
	float ww = 480f;// �������ÿ��Ϊ540f
	// ���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
	int be = 1;// be=1��ʾ������
	if (w > h && w > ww) {// �����ȴ�Ļ����ݿ�ȹ̶���С����
	    be = (int) (newOpts.outWidth / ww);
	} else if (w < h && h > hh) {// ����߶ȸߵĻ����ݿ�ȹ̶���С����
	    be = (int) (newOpts.outHeight / hh);
	}
	if (be <= 0)
	    be = 1;
	newOpts.inSampleSize = be;// �������ű���
	// ���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
	bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
	return compressImage(bitmap, c);// ѹ���ñ�����С���ٽ�������ѹ��
    }

    // ����ԭͼƬʵ�ֱ���ѹ��
    public static String comp(Bitmap image, Context c, String path2) {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	if (baos.toByteArray().length / 1024 > 1024) {// �ж����ͼƬ����1M,����ѹ������������ͼƬ��BitmapFactory.decodeStream��ʱ���
	    baos.reset();// ����baos�����baos
	    image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// ����ѹ��50%����ѹ��������ݴ�ŵ�baos��
	}
	ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
	BitmapFactory.Options newOpts = new BitmapFactory.Options();
	// ��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
	newOpts.inJustDecodeBounds = true;
	Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
	newOpts.inJustDecodeBounds = false;
	int w = newOpts.outWidth;
	int h = newOpts.outHeight;
	// ���������ֻ��Ƚ϶���960*540�ֱ��ʣ����ԸߺͿ���������Ϊ
	float hh = 800f;// �������ø߶�Ϊ960f
	float ww = 480f;// �������ÿ��Ϊ540f
	// ���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
	int be = 1;// be=1��ʾ������
	if (w > h && w > ww) {// �����ȴ�Ļ����ݿ�ȹ̶���С����
	    be = (int) (newOpts.outWidth / ww);
	} else if (w < h && h > hh) {// ����߶ȸߵĻ����ݿ�ȹ̶���С����
	    be = (int) (newOpts.outHeight / hh);
	}
	if (be <= 0)
	    be = 1;
	newOpts.inSampleSize = be;// �������ű���
	// ���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
	isBm = new ByteArrayInputStream(baos.toByteArray());
	bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
	return compressImage2(bitmap, c, path2);// ѹ���ñ�����С���ٽ�������ѹ��
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
     * �ж��ļ����ļ����Ƿ����
     * 
     * @param path
     * @return true �ļ�����
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