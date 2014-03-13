package com.uslotter.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;

public class CopyFileFromAssets {
	
	private String name = "Uslottery.db";
	/**
	 * 
	 * @param myContext
	 * @param ASSETS_NAME
	 *            Ҫ���Ƶ��ļ���
	 * @param savePath
	 *            Ҫ�����·��
	 * @param saveName
	 *            ���ƺ���ļ��� testCopy(Context context)��һ���������ӡ�
	 */

	public static void copy(final Context myContext, final String ASSETS_NAME,
			final String savePath, final String saveName) {
		new Thread(){
			@Override
			public void run() {
				String filename = savePath + "/" + saveName;

				File dir = new File(savePath);
				// ���Ŀ¼���д��ڣ��������Ŀ¼
				if (!dir.exists())
					dir.mkdir();
				try {
				
						InputStream is = myContext.getResources().getAssets()
								.open(ASSETS_NAME);
						FileOutputStream fos = new FileOutputStream(filename);
						byte[] buffer = new byte[7168];
						int count = 0;
						while ((count = is.read(buffer)) > 0) {
							fos.write(buffer, 0, count);
						}
						fos.close();
						is.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		
	}

	public void testCopy(Context context) {
		String path = context.getFilesDir().getAbsolutePath();
		String name = "test.txt";
		CopyFileFromAssets.copy(context, name, path, name);
	}
}
