/**
 * 
 */
package com.uslotter.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Description:
 * 
 * @author lixianm
 * @version 1.0
 */
public class HttpUtil {
    final String TAG = "--HttpUtil--";
    public static final String BASE_URL = "http://www3.lotterygd.cn/androidService.jsp";
    // 创建HttpClient对象
    public static HttpClient httpClient = new DefaultHttpClient();
    
    
    /**
	 * 链接超时
	 */
	private static final int TIMEOUT_CONNECTION = 16000;  
	/**
	 * 底层Socket超时
	 */
	private static final int TIMEOUT_SOCKET = 16000;
	
	/**
	 * 参数的编码
	 */
	public static final String ENCODE = "UTF-8";
	
	/**
	 * 正确的Http响应码
	 */
	public static final int STATUS_OK = 200;

    /**
     * 192.168.0.108:8080
     * 
     * @param url
     *            发送请求的URL
     * @return 服务器响应字符串
     * @throws Exception
     */
    public static String getRequest(String url) throws Exception {
	try {
	    // 创建HttpGet对象。
	    HttpGet get = new HttpGet(url);

	    // 请求超时
	    httpClient.getParams().setParameter(
		    CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
	    // 读取超时
	    httpClient.getParams().setParameter(
		    CoreConnectionPNames.SO_TIMEOUT, 20000);
	    
	    Log.d("tag", "发送请求");
	    // 发送GET请求
	    HttpResponse httpResponse = httpClient.execute(get);
	    Log.d("tag", "请求发送完成");
	    // 如果服务器成功地返回响应
	    if (httpResponse.getStatusLine().getStatusCode() == 200) {
		// 获取服务器响应字符串
		String result = EntityUtils.toString(httpResponse.getEntity());
		return result;
	    }
	    return null;
	} catch (Exception e) {
	    throw e;
	}
    }

    /**
     * 
     * @param url
     *            发送请求的URL
     * @param params
     *            请求参数
     * @return 服务器响应字符串
     * @throws Exception
     */
    public static String postData(String url, List<Map<String, String>> list)
	    throws ClientProtocolException, IOException {
	// 创建HttpPost对象。
	HttpPost post = new HttpPost(url);
	// 如果传递参数个数比较多的话可以对传递的参数进行封装
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	int size = list.size();
	for (int i = 0; i < size; i++) {
	    for (String key : list.get(i).keySet()) {
		// 封装请求参数
		params.add(new BasicNameValuePair(key, list.get(i).get(key)));
	    }
	}
	String result = null;
	try {
	    // 设置请求参数
	    post.setEntity(new UrlEncodedFormEntity(params, "gbk"));
	    // 请求超时
	    httpClient.getParams().setParameter(
		    CoreConnectionPNames.CONNECTION_TIMEOUT, 50000);
	    // 读取超时
	    httpClient.getParams().setParameter(
		    CoreConnectionPNames.SO_TIMEOUT, 20000);
	    // 发送POST请求
	    HttpResponse httpResponse = httpClient.execute(post);
	    // 如果服务器成功地返回响应
	    int statusCode = httpResponse.getStatusLine().getStatusCode();
	    if (statusCode == 200) {
		// 获取服务器响应字符串
		result = EntityUtils.toString(httpResponse.getEntity());
	    }
	} catch (ClientProtocolException e) {
	    Log.e("HttpUtil", "ClientProtocolException:   " + e.getMessage());
	    throw e;
	} catch (IOException e) {
	    Log.e("HttpUtil", "IOException:   " + e.getMessage());
	    throw e;
	}
	return result;
    }
    
    public static  String postRequest(String url, Map<String, String> rawParams)
	    throws ClientProtocolException, IOException {
    	
    	HttpClient mhttpClient = new DefaultHttpClient();
	// 创建HttpPost对象。
	HttpPost post = new HttpPost(url);
	// 如果传递参数个数比较多的话可以对传递的参数进行封装
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	for (String key : rawParams.keySet()) {
	    // 封装请求参数
	    params.add(new BasicNameValuePair(key, rawParams.get(key)));
	}
	String result = null;
	try {
	    // 设置请求参数
	    post.setEntity(new UrlEncodedFormEntity(params, "gbk"));
	    // 请求超时
	    mhttpClient.getParams().setParameter(
		    CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
	    // 读取超时
	    mhttpClient.getParams().setParameter(
		    CoreConnectionPNames.SO_TIMEOUT, 20000);
	    // 发送POST请求
	    HttpResponse httpResponse;
	    
	    Log.d("tag", "开始请求:::"+new Date(System.currentTimeMillis())+"");
	    httpResponse = mhttpClient.execute(post);
	 	Log.d("tag", "请求完:::"+new Date(System.currentTimeMillis())+"");
		
	   
	    // 如果服务器成功地返回响应
	    int statusCode = httpResponse.getStatusLine().getStatusCode();
	    if (statusCode == 200) {
		// 获取服务器响应字符串
		result = EntityUtils.toString(httpResponse.getEntity());

	    }
	} catch (ClientProtocolException e) {
	    Log.e("HttpUtil", "ClientProtocolException:   " + e.getMessage());
	    throw e;
	} catch (IOException e) {
	    Log.e("HttpUtil", "IOException:   " + e.getMessage());
	    throw e;
	}
	return result;
    }
    

    public static boolean checkNet(Context context) {// 获取手机所有连接管理对象（包括对wi-fi,
						     // net等连接的管理）
	try {
	    ConnectivityManager connectivity = (ConnectivityManager) context
		    .getSystemService(Context.CONNECTIVITY_SERVICE);
	    if (connectivity != null) {
		// 获取网络连接管理的对象
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		// System.out.println(">>>>>>>>>>>>Net:"+info);
		if (info == null || !info.isAvailable()) {
		    return false;
		} else {
		    return true;
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return false;
    }

  

	/**
	 * 发送Post请求
	 * @param url 服务器的url
	 * @param params 参数
	 * @return
	 */
	public static String post(String url, Map<String, String> params){
		try {
				HttpPost post = new HttpPost(url);
					List<BasicNameValuePair> paramList = getParams(params);
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "gbk");
					post.setEntity(entity);
				
					  httpClient.getParams().setParameter(
							    CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			HttpResponse response;
			synchronized (httpClient) {
				Log.d("tag", "没过");
				 response = httpClient.execute(post);
				 Log.d("tag", "过了");
			}
			if(response.getStatusLine().getStatusCode() == STATUS_OK){
				return StreamUtil.stream2Str(response.getEntity().getContent(), ENCODE);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将参数组装成List集合
	 * @param params
	 * @return
	 */
	private static List<BasicNameValuePair> getParams(Map<String, String> params){
		List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
		StringBuffer sb=new StringBuffer();
		if(params != null && params.size() > 0){
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(entry.getKey()).append("=").append(entry.getValue());
				param.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		return param;
	}
	
    
}
