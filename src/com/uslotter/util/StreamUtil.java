package com.uslotter.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamUtil {
	/**
	 * @param is
	 * @param encode
	 * @return
	 */
	public static String stream2Str(InputStream is , String encode){
		try {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,encode));
			String line = null;
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param outfile
	 * @param is
	 * @throws Exception
	 */
	public static void copyStream(File outfile, InputStream is) throws Exception{
		byte[] buf = new byte[1024];
		int len = -1;
		FileOutputStream out = new FileOutputStream(outfile);
		while((len = is.read(buf)) != -1){
			out.write(buf, 0, len);
		}
		out.flush();
		out.close();
		is.close();
	}
}
