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
	 
	// ����һ����ʾ��Ϣ�ĶԻ���
	public static void showDialog(final Context ctx
		,int title, int msg , boolean closeSelf){
		// ����һ��AlertDialog.Builder����
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setMessage(msg).setCancelable(false);
		if(closeSelf){
			builder.setPositiveButton("ȷ��", new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// ������ǰActivity
					((Activity)ctx).finish();
				}
			});		
		}else{
			builder.setPositiveButton("ȷ��", null);
		}
		builder.create().show();
	}	
	
	// ����һ����ʾָ������ĶԻ���
	public static void showDialog(Context ctx , View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
			.setView(view).setCancelable(false)
			.setPositiveButton("ȷ��", null);
		builder.create()
			.show();
	}
	
	// ����һ����ʾָ������ĶԻ���
		public static Dialog showDialog(Context ctx , String msg){
			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("ȷ��", null);
			Dialog dialog = builder.create();
			dialog.show();
			return dialog;
		}
}