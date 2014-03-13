package com.sd.everflourish.client.bound;

import android.content.Context;
import android.telephony.TelephonyManager;

public class LotPhoneNumBound{
	

	public static String getPhoneNum_SIM(Context context){//通过SIM卡获取手机号码
		String num=null;
		TelephonyManager telephonyManager = (TelephonyManager)
				context.getSystemService(Context.TELEPHONY_SERVICE);
		num=telephonyManager.getLine1Number();
		return num;
	}
	
	public static String getPhoneID(Context context){//通过获取SIM卡ID号，与用户输入的手机号码进行绑定
		String ID=null;
		TelephonyManager telephonyManager = (TelephonyManager)
				context.getSystemService(Context.TELEPHONY_SERVICE);
		ID=telephonyManager.getSimSerialNumber();
		return ID;
	}
	
}
