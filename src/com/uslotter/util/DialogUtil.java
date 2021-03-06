/**
 * 
 */
package com.uslotter.util;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.app.Activity;
/**
 * Description:
 * @author  lixianm
 * @version  1.0
 */
public class DialogUtil{
	 
	// 定义一个显示消息的对话框
	public static void showDialog(final Context ctx
		,int title, int msg , boolean closeSelf){
		// 创建一个AlertDialog.Builder对象
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setMessage(msg).setCancelable(false);
		if(closeSelf){
			builder.setPositiveButton("确定", new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// 结束当前Activity
					((Activity)ctx).finish();
				}
			});		
		}else{
			builder.setPositiveButton("确定", null);
		}
		builder.create().show();
	}	
	
	// 定义一个显示指定组件的对话框
	public static void showDialog(Context ctx , View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
			.setView(view).setCancelable(false)
			.setPositiveButton("确定", null);
		builder.create()
			.show();
	}
	
	// 定义一个显示指定组件的对话框
		public static Dialog showDialog(Context ctx , String msg){
			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("确定", null);
			Dialog dialog = builder.create();
			dialog.show();
			return dialog;
		}
}
