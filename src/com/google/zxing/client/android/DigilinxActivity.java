package com.google.zxing.client.android;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.cr.uslotter.R;
public class DigilinxActivity extends Activity {
	TextView productText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.test);
		  findViewById(R.id.scan_product).setOnClickListener(mScanProduct);
		  productText= (TextView) findViewById(R.id.TextView01);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		 productText.setText($contents);
	}
	
	 public final Button.OnClickListener mScanProduct = new Button.OnClickListener() {
		    public void onClick(View v) {
		      Intent intent = new Intent("com.google.zxing.client.android.TestSCAN");
		      startActivityForResult(intent, 0);
		    }
	};


	public static String $contents;
	 @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 0) {
	      if (resultCode == RESULT_OK) {
	        String contents = intent.getStringExtra("SCAN_RESULT");
	        String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	        showDialog(R.string.result_succeeded,"产品信息: " + contents);
	        $contents = contents;
	        productText.setText($contents);
	        
	      } else if (resultCode == RESULT_CANCELED) {
	        showDialog(R.string.result_failed, getString(R.string.result_failed_why));
	      }
	    }
	  }
	 
	 private void showDialog(int title, String message) {
		    AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setTitle(title);
		    builder.setMessage(message);
		    builder.setPositiveButton("OK", null);
		    builder.show();
	 }

}