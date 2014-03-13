package com.uslotter.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpConnect {
	/**
	 * 上传文件和参数并返回获取到的结果
	 * 
	 * @param srcFile
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String upLoadFile(String url, File srcFile) {
		String resultContent = "noinfo";
		OutputStream out = null;
		InputStream in = null;
		URL myurl = null;
		HttpURLConnection conn = null;
		InputStream getNetInfoStream = null;
		ByteArrayOutputStream writeNetResult = new ByteArrayOutputStream();
		try {
			myurl = new URL(url);
			conn = (HttpURLConnection) myurl.openConnection();
			conn.setReadTimeout(5 * 1000);

			// 缓存的最长时间

			conn.setDoInput(true);// 允许输入

			conn.setDoOutput(true);// 允许输出

			conn.setUseCaches(false); // 不允许使用缓存

			conn.setRequestMethod("POST");

			conn.setRequestProperty("connection", "keep-alive");

			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// 上传文件
			if (srcFile != null && srcFile.exists()) {

				// int len = (int) srcFile.length();	
				// in = new FileInputStream(srcFile);
				// byte[] buf = new byte[len];
				// in.read(buf);
				// out = conn.getOutputStream();
				// out.write(buf);

				in = new FileInputStream(srcFile);
				out = conn.getOutputStream();
				byte[] buffer = new byte[512];
				int len = -1;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
					Log.i("tag", ".....开始写入总长度:" + len);
				}
			}

			int resultCode = conn.getResponseCode();
			Log.i("tag", "返回码:" + resultCode);
			// 获取网络返回的结果
			if (resultCode == 200) {
				getNetInfoStream = conn.getInputStream();

				int lenght = 0;
				byte[] buffer = new byte[1024];

				while ((lenght = getNetInfoStream.read(buffer)) > 0) {
					writeNetResult.write(buffer, 0, lenght);
				}

				byte[] data = writeNetResult.toByteArray();
				resultContent = new String(data, "UTF-8");

				Log.i("tag", "从网络获取到的数据是:" + resultContent);
			} else {
				resultContent = "exception";
			}
		} catch (Exception e) {
			Log.i("tag", "上传出现异常");
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
				if (getNetInfoStream != null) {

					getNetInfoStream.close();
				}
				if (writeNetResult != null) {

					writeNetResult.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		return resultContent;
	}

	/**
	 * 从网络获取指定名字的资源文件,图片或视频,封装成file返回
	 * 
	 * @param filePath
	 *            存放返回文件的路径
	 * @param filename
	 *            返回文件的名字
	 * @param url
	 *            网络资源的位置
	 * @return
	 * @throws Exception
	 */
	public static File getFile(String filePath, String filename, URL url) {

		File dir = new File(filePath);
		if (!dir.exists() || dir.isDirectory()) {
			dir.mkdirs();
		}
		File file = new File(dir, filename);
		Log.i("tag", "f:" + file.exists());

		HttpURLConnection conn;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			Log.i("tag", "f:" + conn.getResponseCode());
			System.out.println("mypost-返回码:" + conn.getResponseCode());
			if (conn.getResponseCode() == 200) {

				is = conn.getInputStream();
				fos = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = is.read(buf)) > 0) {

					fos.write(buf, 0, len);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return file;
	}

	public static String sengGet(String url) {
		String result = "";
		// 第一步，创建HttpGet对象
		HttpGet httpGet = new HttpGet(url);
		// 第二步，使用execute方法发送HTTP GET请求，并返回HttpResponse对象
		HttpResponse httpResponse;
		try {
			HttpParams hp = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(hp, 5000);
			HttpConnectionParams.setSoTimeout(hp, 5 * 1000);
			httpResponse = new DefaultHttpClient(hp).execute(httpGet);
			// HttpConnectionParams.setConnectionTimeout(httpResponse.getParams(),5000);
			// HttpConnectionParams.setSoTimeout(httpResponse.getParams(),
			// 5000);
			// httpResponse.getParams().setIntParameter(
			// HttpConnectionParams.SO_TIMEOUT, 5000); // 超时设置
			// httpResponse.getParams().setIntParameter(
			// HttpConnectionParams.CONNECTION_TIMEOUT, 5000);// 连接超时
			// httpResponse.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
			// 5000);
			// httpResponse.getParams().setSoTimeout(CoreConnectionPNames.CONNECTION_TIMEOUT,
			// 5000);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 第三步，使用getEntity方法活得返回结果
				result = EntityUtils.toString(httpResponse.getEntity());
				Log.i("tag", "结果:" + result);
				return result;
			}
		} catch (IOException e) {
			Log.i("tag", "io异常");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 * 
	 * @param actionUrl
	 * @param params
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public static String post(String actionUrl, Map<String, String> params,
			Map<String, File> files) throws IOException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(5 * 1000); // 缓存的最长时间
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		// 发送文件数据
		if (files != null) {
			int i = 0;
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\"file"
						+ (i++) + "\"; filename=\"" + file.getKey() + "\""
						+ LINEND);
				sb1.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());

				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}

				is.close();
				outStream.write(LINEND.getBytes());
			}
		}

		// 请求结束标志
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();

		// 得到响应码
		int res = conn.getResponseCode();
		InputStream in = null;
		if (res == 200) {
			in = conn.getInputStream();
			int ch;
			StringBuilder sb2 = new StringBuilder();
			while ((ch = in.read()) != -1) {
				sb2.append((char) ch);
			}
		}
		return in == null ? null : in.toString();
	}

	/* 上传文件到Server的方法 */

	/**
	 * 
	 * 
	 * 
	 * @param actionUrl
	 * 
	 * @param params
	 * 
	 * @param files
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */

	public static String upload(String actionUrl, String FileName)

	throws IOException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();

		String PREFIX = "--", LINEND = "rn";

		String MULTIPART_FROM_DATA = "multipart/form-data";

		String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);

		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

		conn.setReadTimeout(5 * 1000);

		// 缓存的最长时间

		conn.setDoInput(true);// 允许输入

		conn.setDoOutput(true);// 允许输出

		conn.setUseCaches(false); // 不允许使用缓存

		conn.setRequestMethod("POST");

		conn.setRequestProperty("connection", "keep-alive");

		conn.setRequestProperty("charset", "UTF-8");

		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA

		+ ";boundary=" + BOUNDARY);

		DataOutputStream outStream = new DataOutputStream(

		conn.getOutputStream());

		// 发送文件数据

		if (FileName != "") {

			StringBuilder sb1 = new StringBuilder();

			sb1.append(PREFIX);

			sb1.append(BOUNDARY);

			sb1.append(LINEND);

			sb1.append("Content-Disposition: form-data; name='file1'; filename=''+ FileName + ''"
					+ LINEND);

			sb1.append("Content-Type: application/octet-stream; charset="

			+ CHARSET + LINEND);

			sb1.append(LINEND);

			outStream.write(sb1.toString().getBytes());

			InputStream is = new FileInputStream(FileName);

			byte[] buffer = new byte[1024];

			int len = 0;

			while ((len = is.read(buffer)) != -1) {

				outStream.write(buffer, 0, len);

			}

			is.close();

			outStream.write(LINEND.getBytes());

		}

		// 请求结束标志

		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();

		outStream.write(end_data);

		outStream.flush();

		// 得到响应码

		int res = conn.getResponseCode();

		InputStream in = null;

		if (res == 200) {

			in = conn.getInputStream();

			int ch;

			StringBuilder sb2 = new StringBuilder();

			while ((ch = in.read()) != -1) {

				sb2.append((char) ch);

			}

		}

		return in == null ? null : in.toString();

	}

	public static String uploadFile1(String actionUrl, File[] uploadFiles) {
		String result = "";
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		HttpURLConnection con = null;
		StringBuffer b = null;
		DataOutputStream ds = null;
		try {
			URL url = new URL(actionUrl);

			con = (HttpURLConnection) url.openConnection();

			/* 允许Input、Output，不使用Cache */

			con.setDoInput(true);

			con.setDoOutput(true);

			con.setUseCaches(false);

			/* 设置传送的method=POST */

			con.setRequestMethod("POST");

			/* setRequestProperty */

			con.setRequestProperty("Connection", "Keep-Alive");

			con.setRequestProperty("Charset", "UTF-8");

			con.setRequestProperty("Content-Type",

			"multipart/form-data;boundary=" + boundary);

			/* 设置DataOutputStream */

			ds = new DataOutputStream(con.getOutputStream());
			int size = uploadFiles.length;

			for (int i = 0; i < size; i++) {

				File file = uploadFiles[i];

				ds.writeBytes(twoHyphens + boundary + end);

				ds.writeBytes("Content-Disposition: form-data; " +

				"name=\"file\";filename=\"" +

				file.getName() + "\"" + end);

				ds.writeBytes(end);

				/* 取得文件的FileInputStream */

				FileInputStream fStream = new FileInputStream(file);
				/* 设置每次写入1024bytes */

				int bufferSize = 1024;

				byte[] buffer = new byte[bufferSize];

				int length = -1;

				/* 从文件读取数据至缓冲区 */

				while ((length = fStream.read(buffer)) != -1)

				{
					/* 将资料写入DataOutputStream中 */
					ds.write(buffer, 0, length);
				}

				ds.writeBytes(end);

				/* close streams */

				fStream.close();
			}
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			ds.flush();
			/* 取得Response内容 */
			// 得到响应码
			int res = con.getResponseCode();
			if (res == 200) {

				InputStream is = con.getInputStream();
				int ch;

				b = new StringBuffer();

				while ((ch = is.read()) != -1) {

					b.append((char) ch);

				}
			}

			/* 将Response显示于Dialog */

			// showDialog("上传成功"+b.toString().trim());

			/* 关闭DataOutputStream */

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				ds.close();
				con.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return b.toString();
	}

	public static String uploadFile2(String actionUrl, File uploadFile) {
		String result = "";
		String end = "\r\n";

		String twoHyphens = "--";

		String boundary = "*****";

		try {
			URL url = new URL(actionUrl);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			/* 允许Input、Output，不使用Cache */

			con.setDoInput(true);

			con.setDoOutput(true);

			con.setUseCaches(false);

			/* 设置传送的method=POST */

			con.setRequestMethod("POST");

			/* setRequestProperty */

			con.setRequestProperty("Connection", "Keep-Alive");

			con.setRequestProperty("Charset", "UTF-8");

			con.setRequestProperty("Content-Type",

			"multipart/form-data;boundary=" + boundary);

			/* 设置DataOutputStream */

			DataOutputStream ds =

			new DataOutputStream(con.getOutputStream());

			ds.writeBytes(twoHyphens + boundary + end);

			ds.writeBytes("Content-Disposition: form-data; " +

			"name=\"file1\";filename=\"" +

			"a.jpg" + "\"" + end);

			ds.writeBytes(end);

			/* 取得文件的FileInputStream */

			FileInputStream fStream = new FileInputStream(uploadFile);
			/* 设置每次写入1024bytes */

			int bufferSize = 1024;

			byte[] buffer = new byte[bufferSize];

			int length = -1;

			/* 从文件读取数据至缓冲区 */

			while ((length = fStream.read(buffer)) != -1)

			{
				/* 将资料写入DataOutputStream中 */

				ds.write(buffer, 0, length);
			}

			ds.writeBytes(end);

			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

			/* close streams */

			fStream.close();

			ds.flush();

			/* 取得Response内容 */

			InputStream is = con.getInputStream();
			int ch;

			StringBuffer b = new StringBuffer();

			while ((ch = is.read()) != -1)

			{
				b.append((char) ch);
			}

			/* 将Response显示于Dialog */

			// showDialog("上传成功"+b.toString().trim());

			/* 关闭DataOutputStream */

			ds.close();
			return b.toString();
		} catch (Exception e) {
			return "";
		}

	}
}