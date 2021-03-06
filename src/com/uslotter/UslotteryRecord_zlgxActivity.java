package com.uslotter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.cr.uslotter.R;

public class UslotteryRecord_zlgxActivity extends Activity{
	private Button btn_exit = null;
	private Button btn_save = null;
	private LinearLayout ll_add = null;
	private Spinner sp = null;
	private List<LinearLayout> lists = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record2_zlgx);
		btn_exit = (Button)this.findViewById(R.id.record2_zlgx_back);
		btn_save = (Button)this.findViewById(R.id.record2_zlgx_save);
		btn_exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				test();		
			}
		});
		lists = new ArrayList<LinearLayout>();
		ll_add = (LinearLayout)this.findViewById(R.id.record2_zlgx_ll);
		createWidgets();
		//		sp = (Spinner) this.findViewById(R.id.record2_zlgx_sp_theme);
//		sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				View view = LayoutInflater.from(UslotteryRecord_zlgxActivity.this).inflate(R.layout.record2_zlgx_item, null);
//				ll_add.addView(view);
//			
//				Toast.makeText(UslotteryRecord_zlgxActivity.this, "one select", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				View view = LayoutInflater.from(UslotteryRecord_zlgxActivity.this).inflate(R.layout.record2_zlgx_item, null);
//				ll_add.addView(view);
//				Toast.makeText(UslotteryRecord_zlgxActivity.this, "noting select", Toast.LENGTH_SHORT).show();
//	
//			}
//		});
	}
	
	public void createWidgets(){
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundResource(R.drawable.textview_boder);
		ll.setOrientation(LinearLayout.VERTICAL);
		LayoutParams lp_ll = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		lp_ll.setMargins(10, 10, 10, 10);
		ll.setLayoutParams(lp_ll);
		
		LinearLayout lla = new LinearLayout(this);
		lla.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams lp_lla = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		lp_lla.setMargins(5, 5, 5, 5);
		lla.setLayoutParams(lp_lla);
		
		
		TextView tv =new  TextView(this);
		tv.setText("更新项目:");
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(20);
		LayoutParams lp_tv = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
	    tv.setLayoutParams(lp_tv);
	    
	   final Spinner sp =new Spinner(this);
	  ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.zlgx,android.R.layout.simple_spinner_item);
	  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   sp.setAdapter(adapter);
	    sp.setTag(false);
	    LayoutParams lp_et = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); 
	    sp.setLayoutParams(lp_et);
	    sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				boolean flag = (Boolean)sp.getTag();
				if(flag){
					
				}else{
					if(!sp.getSelectedItem().toString().equals("")){
						sp.setTag(true);
						createWidgets();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
						
			}
	    });
	    
	    lla.addView(tv);
	    lla.addView(sp);
	    
	    LinearLayout llb = new LinearLayout(this);
	    llb.setOrientation(LinearLayout.HORIZONTAL);
	    LayoutParams lp_llb = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		lp_llb.setMargins(5, 5, 5, 5);
		llb.setLayoutParams(lp_llb);
	    
	    TextView tv_b = new TextView(this);
	    LayoutParams lp_tv_b = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        tv_b.setText("更  新 为:");
        tv_b.setTextColor(Color.BLACK);
        tv_b.setTextSize(20);
        tv_b.setLayoutParams(lp_tv_b);
        
        EditText et_b = new EditText(this);
        LayoutParams lp_et_b = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        et_b.setLayoutParams(lp_et_b);
        llb.addView(tv_b);
        llb.addView(et_b);
        
        LinearLayout llc = new  LinearLayout(this);
        llc.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams lp_llc = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    	lp_llc.setMargins(5, 5, 5, 5);
    	llc.setLayoutParams(lp_llc);
		
        TextView  tv_c = new TextView(this);
        tv_c.setTextColor(Color.BLACK);
        tv_c.setTextSize(20);
        tv_c.setText("备       注:");
        LayoutParams lp_tv_c = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv_c.setLayoutParams(lp_tv_c);
        
        EditText et_c = new EditText(this);
        LayoutParams lp_et_c = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        et_c.setLayoutParams(lp_et_c);
        llc.addView(tv_c);
        llc.addView(et_c);
        
        ll.addView(lla);
        ll.addView(llb);
        ll.addView(llc);
        lists.add(ll);
        
        ll_add.addView(ll);
	}
	public void test(){
		if(lists.size() <= 0){
			return;
		}
        for(LinearLayout _ll:lists){
        	for(int i = 0;i<3;i++){
        	LinearLayout _ll_1 = (LinearLayout)_ll.getChildAt(i);
        	
        	TextView _tv = (TextView)_ll_1.getChildAt(0);
        	if(_ll_1.getChildAt(1) instanceof Spinner){
        		
        		Toast.makeText(this, ((Spinner)_ll_1.getChildAt(1)).getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        	}else if(_ll_1.getChildAt(1)instanceof EditText){
        		
        		Toast.makeText(this, ((EditText)_ll_1.getChildAt(1)).getText().toString(), Toast.LENGTH_SHORT).show();
        	}
//        	if(i == 0){
//        		Spinner _sp = (Spinner)_ll_1.getChildAt(1);
//        		
//        	}else{
//        		EditText _et = (EditText)_ll_1.getChildAt(1);
//        		
//        	}
         }
        }
	}
}