package com.uslotter.util;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import com.cr.uslotter.R;
public class Config {
	private static final String TAG = "Config";	
	//  public static final String UPDATE_SERVER = "http://app.lotterygd.cn/";
		public static final String UPDATE_APKNAME = "Uslottery.apk";
		public static final String UPDATE_VERJSON = "Uslottery.json";
		public static final String UPDATE_SAVENAME = "Uslottery.apk";
	
	//服务器最新的版本号、版本名称
	public static int newVerCode = 0;
	public static String newVerName = "";
	public static String updateDesc = "";
	
	public static int getNewVerCode() {
		return newVerCode;
	}

	public void setNewVerCode(int newVerCode) {
		this.newVerCode = newVerCode;
	}

	public String getNewVerName() {
		return newVerName;
	}

	public void setNewVerName(String newVerName) {
		this.newVerName = newVerName;
	}

	public String getUpdateDesc() {
		return updateDesc;
	}

	public void setUpdateDesc(String updateDesc) {
		this.updateDesc = updateDesc;
	}


	
	/**
	 * 目前的客户端的版本号
	 * @param context
	 * @return
	 */
	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			String p=context.getPackageName();
			verCode = context.getPackageManager().getPackageInfo(p, 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verCode;
	}
	
	/**
	 * 目前的客户端的版本名称
	 * @param context
	 * @return
	 */
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verName;	

	}
	/**
	 * 应用名称
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context) {
		String verName = context.getResources()
		.getText(R.string.app_name).toString();
		return verName;
	}
	
	/**
	 * 获取服务器应用的最新的版本号、版本名称
	 * @return
	 */
	public static  boolean getServerVerCode() {
		boolean flag=false;
		try {
			String verjson = HttpUtil.getRequest(HttpUrl.URL + Config.UPDATE_VERJSON);
			Log.d("tag", "verjson::"+verjson);
		JSONArray array = new JSONArray(verjson);
		Log.d("tag", "array::"+array);
			if (array.length() > 0) {
				JSONObject obj = array.getJSONObject(0);
				newVerCode = Integer.parseInt(obj.getString("verCode"));
				Log.d("tag", "newVerCode:"+newVerCode);
				newVerName = obj.getString("verName");
				updateDesc = obj.getString("updateDesc");
				flag=true;
			}
		} catch (Exception e) {
			//Log.e(TAG, e.getMessage());
		}
		return flag;
	}
	
	public static boolean hasNewVersion(Context context){
		if(getServerVerCode()){
			if (newVerCode > getVerCode(context)) {
				return true;
			}
		}
		return false;
	}
	
}
