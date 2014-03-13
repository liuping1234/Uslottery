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
    // ����HttpClient����
    public static HttpClient httpClient = new DefaultHttpClient();
    
    
    /**
	 * ���ӳ�ʱ
	 */
	private static final int TIMEOUT_CONNECTION = 16000;  
	/**
	 * �ײ�Socket��ʱ
	 */
	private static final int TIMEOUT_SOCKET = 16000;
	
	/**
	 * �����ı���
	 */
	public static final String ENCODE = "UTF-8";
	
	/**
	 * ��ȷ��Http��Ӧ��
	 */
	public static final int STATUS_OK = 200;

    /**
     * 192.168.0.108:8080
     * 
     * @param url
     *            ���������URL
     * @return ��������Ӧ�ַ���
     * @throws Exception
     */
    public static String getRequest(String url) throws Exception {
	try {
	    // ����HttpGet����
	    HttpGet get = new HttpGet(url);

	    // ����ʱ
	    httpClient.getParams().setParameter(
		    CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
	    // ��ȡ��ʱ
	    httpClient.getParams().setParameter(
		    CoreConnectionPNames.SO_TIMEOUT, 20000);
	    
	    Log.d("tag", "��������");
	    // ����GET����
	    HttpResponse httpResponse = httpClient.execute(get);
	    Log.d("tag", "���������");
	    // ����������ɹ��ط�����Ӧ
	    if (httpResponse.getStatusLine().getStatusCode() == 200) {
		// ��ȡ��������Ӧ�ַ���
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
     *            ���������URL
     * @param params
     *            �������
     * @return ��������Ӧ�ַ���
     * @throws Exception
     */
    public static String postData(String url, List<Map<String, String>> list)
	    throws ClientProtocolException, IOException {
	// ����HttpPost����
	HttpPost post = new HttpPost(url);
	// ������ݲ��������Ƚ϶�Ļ����ԶԴ��ݵĲ������з�װ
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	int size = list.size();
	for (int i = 0; i < size; i++) {
	    for (String key : list.get(i).keySet()) {
		// ��װ�������
		params.add(new BasicNameValuePair(key, list.get(i).get(key)));
	    }
	}
	String result = null;
	try {
	    // �����������
	    post.setEntity(new UrlEncodedFormEntity(params, "gbk"));
	    // ����ʱ
	    httpClient.getParams().setParameter(
		    CoreConnectionPNames.CONNECTION_TIMEOUT, 50000);
	    // ��ȡ��ʱ
	    httpClient.getParams().setParameter(
		    CoreConnectionPNames.SO_TIMEOUT, 20000);
	    // ����POST����
	    HttpResponse httpResponse = httpClient.execute(post);
	    // ����������ɹ��ط�����Ӧ
	    int statusCode = httpResponse.getStatusLine().getStatusCode();
	    if (statusCode == 200) {
		// ��ȡ��������Ӧ�ַ���
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
	// ����HttpPost����
	HttpPost post = new HttpPost(url);
	// ������ݲ��������Ƚ϶�Ļ����ԶԴ��ݵĲ������з�װ
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	for (String key : rawParams.keySet()) {
	    // ��װ�������
	    params.add(new BasicNameValuePair(key, rawParams.get(key)));
	}
	String result = null;
	try {
	    // �����������
	    post.setEntity(new UrlEncodedFormEntity(params, "gbk"));
	    // ����ʱ
	    mhttpClient.getParams().setParameter(
		    CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
	    // ��ȡ��ʱ
	    mhttpClient.getParams().setParameter(
		    CoreConnectionPNames.SO_TIMEOUT, 20000);
	    // ����POST����
	    HttpResponse httpResponse;
	    
	    Log.d("tag", "��ʼ����:::"+new Date(System.currentTimeMillis())+"");
	    httpResponse = mhttpClient.execute(post);
	 	Log.d("tag", "������:::"+new Date(System.currentTimeMillis())+"");
		
	   
	    // ����������ɹ��ط�����Ӧ
	    int statusCode = httpResponse.getStatusLine().getStatusCode();
	    if (statusCode == 200) {
		// ��ȡ��������Ӧ�ַ���
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
    

    public static boolean checkNet(Context context) {// ��ȡ�ֻ��������ӹ�����󣨰�����wi-fi,
						     // net�����ӵĹ���
	try {
	    ConnectivityManager connectivity = (ConnectivityManager) context
		    .getSystemService(Context.CONNECTIVITY_SERVICE);
	    if (connectivity != null) {
		// ��ȡ�������ӹ���Ķ���
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
	 * ����Post����
	 * @param url ��������url
	 * @param params ����
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
				Log.d("tag", "û��");
				 response = httpClient.execute(post);
				 Log.d("tag", "����");
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
	 * ��������װ��List����
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
