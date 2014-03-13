package com.uslotter.performance;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.cr.uslotter.R;

public class PerformanceActivity extends Activity {
	private Button btn_find = null; 
	private EditText et_date = null;
	private Spinner sp_type,sp_zgy;
	private ListView lv = null;
	DatePickerDialog dp =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	   requestWindowFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	   requestWindowFeature(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	   this.setContentView(R.layout.performance);
	   
	   btn_find = (Button)this.findViewById(R.id.performance_btn_find);
	   et_date = (EditText)this.findViewById(R.id.performance_et_date);
	   Calendar c = Calendar.getInstance();
	   et_date.setText(c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH));
	   sp_type = (Spinner)this.findViewById(R.id.performance_sp_type);
	   sp_zgy = (Spinner)this.findViewById(R.id.performance_sp_zgy);
	   lv =  (ListView)this.findViewById(R.id.performance_list);
	   et_date.setOnClickListener(new View.OnClickListener() {		
		@Override
		public void onClick(View v) {
			Calendar canlendar = Calendar.getInstance();
			dp = new  DatePickerDialog(PerformanceActivity.this,
					new DatePickerDialog.OnDateSetListener() {
						
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear,
								int dayOfMonth) {
							 et_date.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);							  	
						}
					},
					canlendar.get(Calendar.YEAR), 
					canlendar.get(Calendar.MONTH), 
					canlendar.get(Calendar.DAY_OF_MONTH));
			dp.show();
		}
	});
	}
}