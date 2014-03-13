package com.uslotter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cr.uslotter.R;
public class UslotteryRecordStandard extends Activity 
	implements View.OnClickListener,OnCheckedChangeListener{
    /** Called when the activity is first created. */
	//private CheckBox a = null;
	private TextView a = null;
	private CheckBox b = null;
	private CheckBox d = null;
	private CheckBox g = null;
	private CheckBox j = null;
	private CheckBox y = null;
	private CheckBox r = null;
	
	private CheckBox jc = null;
	private TextView tvlt = null;
	private TextView tvjk = null;
	private TextView tvwdzh = null;
	private TextView tvjc = null;
	
	private LinearLayout lla = null;
	private LinearLayout llb = null;
	private LinearLayout lld = null;
	private LinearLayout llg = null;
	private LinearLayout llj = null;
	private LinearLayout lly = null;
	private LinearLayout llr = null;
	private LinearLayout ll_wdjcbz = null;
	
	
	private CheckBox a1,a2,a3,a4,b1,b2,b3,b4,b5,b6,b7;
	private CheckBox d1,d2,d3,d4,d5,d6,d7,d8,d31,d32,d41,d42,d43;
	private CheckBox g1,g2,g3,g4,g5,g6,g11,g12,g21,g22,g31,g32,g33;
	private CheckBox j1,j2,j3,j4,j5,j11,j12,j13,j21,j22,j23,j24,j31,j32,j33,j34,j35,j36,j37,j38;
	private CheckBox j41,j42,j43,j44,j45,j46,j47,j51,j52,j53,j54,j55;
	private CheckBox y1,y2,y3,y4,y11,y12,y21,y22,y23;
	private CheckBox r1,r2,r3,r4,r5,r6,r11,r12,r13,r21,r22,r23,r51,r52,r53,r61,r62;
	   
	private Button _return , save;
	

	StringBuilder builder = null;
	int score = 0;
	int score_ab = 0;
	int score_dg = 0;
	int score_j = 0;
	int score_yr = 0;
	
	private boolean flag_a = false;
	private boolean flag_b = false;
	private boolean flag_d = false;
	private boolean flag_g = false;
	private boolean flag_j = false;
	private boolean flag_y = false;
	private boolean flag_r = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.record3);
        jc = (CheckBox) this.findViewById(R.id.ludan_smg);
        
      //  a = (CheckBox) this.findViewById(R.id.ludan_a);
        a = (TextView) this.findViewById(R.id.ludan_a);
        a.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				if(flag_a){	   		
					lla.setVisibility(View.GONE);
					flag_a = false;
				    a.setCompoundDrawablesWithIntrinsicBounds(null, null,UslotteryRecordStandard.this.getResources().getDrawable(R.drawable.expend), null);
				}else{
					lla.setVisibility(View.VISIBLE);
					flag_a = true;
				    a.setCompoundDrawablesWithIntrinsicBounds(null, null, UslotteryRecordStandard.this.getResources().getDrawable(R.drawable.substract), null);			
				}
			}
		});
        b = (CheckBox) this.findViewById(R.id.ludan_b);
        d = (CheckBox) this.findViewById(R.id.ludan_d);
        g = (CheckBox) this.findViewById(R.id.ludan_g);
        j = (CheckBox) this.findViewById(R.id.ludan_j);
        y = (CheckBox) this.findViewById(R.id.ludan_y);
        r = (CheckBox) this.findViewById(R.id.ludan_r);
        
        a1 = (CheckBox) this.findViewById(R.id.ludan_a1);
        a2 = (CheckBox) this.findViewById(R.id.ludan_a2);
        a3 = (CheckBox) this.findViewById(R.id.ludan_a3);
        a4 = (CheckBox) this.findViewById(R.id.ludan_a4);
        b1 = (CheckBox) this.findViewById(R.id.ludan_b1);
        b2 = (CheckBox) this.findViewById(R.id.ludan_b2);
        b3 = (CheckBox) this.findViewById(R.id.ludan_b3);
        b4 = (CheckBox) this.findViewById(R.id.ludan_b4);
        b5 = (CheckBox) this.findViewById(R.id.ludan_b5);
        b6 = (CheckBox) this.findViewById(R.id.ludan_b6);
        b7 = (CheckBox) this.findViewById(R.id.ludan_b7);
        d1 = (CheckBox) this.findViewById(R.id.ludan_d1);
        d2 = (CheckBox) this.findViewById(R.id.ludan_d2);
        d3 = (CheckBox) this.findViewById(R.id.ludan_d3);
        d4 = (CheckBox) this.findViewById(R.id.ludan_d4);
        d5 = (CheckBox) this.findViewById(R.id.ludan_d5);
        d6 = (CheckBox) this.findViewById(R.id.ludan_d6);
        d7 = (CheckBox) this.findViewById(R.id.ludan_d7);
        d8 = (CheckBox) this.findViewById(R.id.ludan_d8);
        d31 = (CheckBox) this.findViewById(R.id.ludan_d31);
        d32 = (CheckBox) this.findViewById(R.id.ludan_d32);
        d41 = (CheckBox) this.findViewById(R.id.ludan_d41);
        d42 = (CheckBox) this.findViewById(R.id.ludan_d42);
        d43 = (CheckBox) this.findViewById(R.id.ludan_d43);
        g1 = (CheckBox) this.findViewById(R.id.ludan_g1);
        g2 = (CheckBox) this.findViewById(R.id.ludan_g2);
        g3 = (CheckBox) this.findViewById(R.id.ludan_g3);
        g4 = (CheckBox) this.findViewById(R.id.ludan_g4);
        g5 = (CheckBox) this.findViewById(R.id.ludan_g5);
        g6 = (CheckBox) this.findViewById(R.id.ludan_g6);
        g11 = (CheckBox) this.findViewById(R.id.ludan_g11);
        g12 = (CheckBox) this.findViewById(R.id.ludan_g12);
        g21 = (CheckBox) this.findViewById(R.id.ludan_g21);
        g22 = (CheckBox) this.findViewById(R.id.ludan_g22);
        g31 = (CheckBox) this.findViewById(R.id.ludan_g31);
        g32 = (CheckBox) this.findViewById(R.id.ludan_g32);
        g33 = (CheckBox) this.findViewById(R.id.ludan_g33);
        j1 = (CheckBox) this.findViewById(R.id.ludan_j1);
        j2 = (CheckBox) this.findViewById(R.id.ludan_j2);
        j3 = (CheckBox) this.findViewById(R.id.ludan_j3);
        j4 = (CheckBox) this.findViewById(R.id.ludan_j4);
        j5 = (CheckBox) this.findViewById(R.id.ludan_j5);
        j11 = (CheckBox) this.findViewById(R.id.ludan_j11);
        j12 = (CheckBox) this.findViewById(R.id.ludan_j12);
        j13 = (CheckBox) this.findViewById(R.id.ludan_j13);
        j21 = (CheckBox) this.findViewById(R.id.ludan_j21);
        j22 = (CheckBox) this.findViewById(R.id.ludan_j22);
        j23 = (CheckBox) this.findViewById(R.id.ludan_j23);
        j24 = (CheckBox) this.findViewById(R.id.ludan_j24);
        j31 = (CheckBox) this.findViewById(R.id.ludan_j31);
        j32 = (CheckBox) this.findViewById(R.id.ludan_j32);
        j33 = (CheckBox) this.findViewById(R.id.ludan_j33);
        j34 = (CheckBox) this.findViewById(R.id.ludan_j34);
        j35 = (CheckBox) this.findViewById(R.id.ludan_j35);
        j36 = (CheckBox) this.findViewById(R.id.ludan_j36);
        j37 = (CheckBox) this.findViewById(R.id.ludan_j37);
        j38 = (CheckBox) this.findViewById(R.id.ludan_j38);
        j41 = (CheckBox) this.findViewById(R.id.ludan_j41);
        j42 = (CheckBox) this.findViewById(R.id.ludan_j42);
        j43 = (CheckBox) this.findViewById(R.id.ludan_j43);
        j44 = (CheckBox) this.findViewById(R.id.ludan_j44);
        j45 = (CheckBox) this.findViewById(R.id.ludan_j45);
        j46 = (CheckBox) this.findViewById(R.id.ludan_j46);
        j47 = (CheckBox) this.findViewById(R.id.ludan_j47);
        j51 = (CheckBox) this.findViewById(R.id.ludan_j51);
        j52 = (CheckBox) this.findViewById(R.id.ludan_j52);
        j53 = (CheckBox) this.findViewById(R.id.ludan_j53);
        j54 = (CheckBox) this.findViewById(R.id.ludan_j54);
        j55 = (CheckBox) this.findViewById(R.id.ludan_j55);
        y1 = (CheckBox) this.findViewById(R.id.ludan_y1);
        y2 = (CheckBox) this.findViewById(R.id.ludan_y2);
        y3 = (CheckBox) this.findViewById(R.id.ludan_y3);
        y4 = (CheckBox) this.findViewById(R.id.ludan_y4);
        y11 = (CheckBox) this.findViewById(R.id.ludan_y11);
        y12 = (CheckBox) this.findViewById(R.id.ludan_y12);
        y21 = (CheckBox) this.findViewById(R.id.ludan_y21);
        y22 = (CheckBox) this.findViewById(R.id.ludan_y22);
        y23 = (CheckBox) this.findViewById(R.id.ludan_y23);
        r1 = (CheckBox) this.findViewById(R.id.ludan_r1);
        r2 = (CheckBox) this.findViewById(R.id.ludan_r2);
        r3 = (CheckBox) this.findViewById(R.id.ludan_r3);
        r4 = (CheckBox) this.findViewById(R.id.ludan_r4);
        r5 = (CheckBox) this.findViewById(R.id.ludan_r5);
        r6 = (CheckBox) this.findViewById(R.id.ludan_r6);
        r11 = (CheckBox) this.findViewById(R.id.ludan_r11);
        r12 = (CheckBox) this.findViewById(R.id.ludan_r12);
        r13 = (CheckBox) this.findViewById(R.id.ludan_r13);
        r21 = (CheckBox) this.findViewById(R.id.ludan_r21);
        r22 = (CheckBox) this.findViewById(R.id.ludan_r22);
        r23 = (CheckBox) this.findViewById(R.id.ludan_r23);
        r51 = (CheckBox) this.findViewById(R.id.ludan_r51);
        r52 = (CheckBox) this.findViewById(R.id.ludan_r52);
        r53 = (CheckBox) this.findViewById(R.id.ludan_r53);
        r61 = (CheckBox) this.findViewById(R.id.ludan_r61);
        r62 = (CheckBox) this.findViewById(R.id.ludan_r62);
        
        tvwdzh = (TextView) this.findViewById(R.id.ludan_wdzhxx);
        tvlt = (TextView) this.findViewById(R.id.ludan_ltxbz);
        tvjk = (TextView) this.findViewById(R.id.ludan_wdjkpbz);
        tvjc = (TextView) this.findViewById(R.id.ludan_wdjcbz);
        lla = (LinearLayout) this.findViewById(R.id.ludan_lla);
        llb = (LinearLayout) this.findViewById(R.id.ludan_llb);
        lld = (LinearLayout) this.findViewById(R.id.ludan_lld);
        llg = (LinearLayout) this.findViewById(R.id.ludan_llg);
        llj = (LinearLayout) this.findViewById(R.id.ludan_llj);
        lly = (LinearLayout) this.findViewById(R.id.ludan_lly);
        llr = (LinearLayout) this.findViewById(R.id.ludan_llr);
        ll_wdjcbz = (LinearLayout) this.findViewById(R.id.ludan_wdjcbz_ll); 
        _return = (Button) this.findViewById(R.id.ludan_return);
        save = (Button) this.findViewById(R.id.ludan_save);
       
        //初始化
        SharedPreferences sp = getSharedPreferences("save_sp", MODE_WORLD_WRITEABLE);
        String con = sp.getString("save", "");
        Toast.makeText(this, ",,."+con, Toast.LENGTH_SHORT).show();
		
        if(!con.equals("")){
        	parse(con);
        }
        
        _return.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent();
				//Test_yjActivity.this.startActivity(intent);
				UslotteryRecordStandard.this.finish();
			}
		});
        save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//判断每一个是不是被选中
				score = 0;
				score_ab = 0;
				score_dg = 0;
				score_j = 0;
				score_yr = 0;
				builder = new  StringBuilder();
			//	if(lla.getVisibility() == 0){
//					if(a.isChecked()){
//						builder.append("a").append(",");
//					}
					if(a1.isChecked()){
						builder.append("A1").append(",");
					}
					if(a2.isChecked()){
						builder.append("A2").append(",");
					}
					if(a3.isChecked()){
						builder.append("A3").append(",");
					}
					if(a4.isChecked()){
						builder.append("A4").append(",");
					}
			//	}
			//	if(llb.getVisibility() == 0){
					if(b.isChecked()){
						builder.append("B").append(",");
						
					}
					if(b1.isChecked()){
						builder.append("B1").append(",");
						score+=1;
						score_ab+=1;
					}
					if(b2.isChecked()){
						builder.append("B2").append(",");
						score+=2;
						score_ab+=2;
					}
					if(b3.isChecked()){
						builder.append("B3").append(",");
						score+=1;
						score_ab+=1;
					}
					if(b4.isChecked()){
						builder.append("B4").append(",");
						score+=1;
						score_ab+=1;
					}
					if(b5.isChecked()){
						builder.append("B5").append(",");
						score+=1;
						score_ab+=1;
					}
					if(b6.isChecked()){
						builder.append("B6").append(",");
						score+=2;
						score_ab+=2;
					}
					if(b7.isChecked()){
						builder.append("B7").append(",");
						score+=2;
						score_ab+=2;
					}	
			//	}
			//	if(lld.getVisibility() == 0){
					if(d.isChecked()){
						builder.append("D").append(",");
					}
					if(d1.isChecked()){
						builder.append("D1").append(",");
						score+=10;
						score_dg+=10;
					}
					if(d2.isChecked()){
						builder.append("D2").append(",");
						score+=10;
						score_dg+=10;
					}
					if(d3.isChecked()){
						builder.append("D3").append(",");
					}
					if(d4.isChecked()){
						builder.append("D4").append(",");
					}
					if(d5.isChecked()){
						builder.append("D5").append(",");
						score+=10;
						score_dg+=10;
					}
					if(d6.isChecked()){
						builder.append("D6").append(",");
						score+=5;
						score_dg+=5;
					}
					if(d7.isChecked()){
						builder.append("D7").append(",");
						score+=3;
						score_dg+=3;
					}
					if(d8.isChecked()){
						builder.append("D8").append(",");
						score+=2;
						score_dg+=2;
					}
					if(d31.isChecked()){
						builder.append("D31").append(",");
						score+=8;
						score_dg+=8;
					}
					if(d32.isChecked()){
						builder.append("D32").append(",");
						score+=2;
						score_dg+=2;
					}
					if(d41.isChecked()){
						builder.append("D41").append(",");
						score+=5;
						score_dg+=5;
					}
					if(d42.isChecked()){
						builder.append("D42").append(",");
						score+=3;
						score_dg+=3;
					}
					if(d43.isChecked()){
						builder.append("D43").append(",");
						score+=2;
						score_dg+=2;
					}
			//		}
			//	if(llg.getVisibility() == 0){
					if(g.isChecked()){
						builder.append("G").append(",");
					}
					if(g1.isChecked()){
						builder.append("G1").append(",");
					}
					if(g2.isChecked()){
						builder.append("G2").append(",");
					}
					if(g3.isChecked()){
						builder.append("G3").append(",");
					}
					if(g4.isChecked()){
						builder.append("G4").append(",");
						score+=3;
						score_dg+=3;
					}
					if(g5.isChecked()){
						builder.append("G5").append(",");
						score+=2;
						score_dg+=2;
					}
					if(g6.isChecked()){
						builder.append("G6").append(",");
						score+=2;
						score_dg+=2;
					}
					if(g11.isChecked()){
						builder.append("G11").append(",");
						score+=8;
						score_dg+=8;
					}
					if(g12.isChecked()){
						builder.append("G12").append(",");
						score+=2;
						score_dg+=2;
					}
					if(g21.isChecked()){
						builder.append("G21").append(",");
						score+=3;
						score_dg+=3;
					}
					if(g22.isChecked()){
						builder.append("G22").append(",");
						score+=2;
						score_dg+=2;
					}
					if(g31.isChecked()){
						builder.append("G31").append(",");
						score+=3;
						score_dg+=3;
					}
					if(g32.isChecked()){
						builder.append("G32").append(",");
						score+=3;
						score_dg+=3;
					}
					if(g33.isChecked()){
						builder.append("G33").append(",");
						score+=2;
						score_dg+=2;
					}
			//	}
			//	if(llj.getVisibility() == 0){
					if(j.isChecked()){
						builder.append("J").append(",");
					}
					if(j1.isChecked()){
						builder.append("J1").append(",");
					
					}
					if(j2.isChecked()){
						builder.append("J2").append(",");
					}
					if(j3.isChecked()){
						builder.append("J3").append(",");
					}
					if(j4.isChecked()){
						builder.append("J4").append(",");
					}
					if(j5.isChecked()){
						builder.append("J5").append(",");
					}
					if(j11.isChecked()){
						builder.append("J11").append(",");
						score+=1;
						score_j+=1;
					}
					if(j12.isChecked()){
						builder.append("J12").append(",");
						score+=3;
						score_j+=3;
					}
					if(j13.isChecked()){
						builder.append("J13").append(",");
						score+=2;
						score_j+=2;
					}
					if(j21.isChecked()){
//						if(j22.isChecked()||j23.isChecked()||j24.isChecked()){
//							Toast.makeText(UslotteryRecordStandard.this,"j21,j22,j23,j24只能选一项！", Toast.LENGTH_SHORT).show();
//			            	return;	
//						}
						builder.append("J21").append(",");
						score+=20;
						score_j+=20;
					}
					if(j22.isChecked()){
						if(j21.isChecked()||j23.isChecked()||j24.isChecked()){
							Toast.makeText(UslotteryRecordStandard.this,"j21,j22,j23,j24只能选一项！", Toast.LENGTH_SHORT).show();
			            	return;	
						}
						builder.append("J22").append(",");
						score+=16;
						score_j+=16;
					}
					if(j23.isChecked()){
						if(j21.isChecked()||j22.isChecked()||j24.isChecked()){
							Toast.makeText(UslotteryRecordStandard.this,"j21,j22,j23,j24只能选一项！", Toast.LENGTH_SHORT).show();
			            	return;	
						}
						builder.append("J23").append(",");
						score+=8;
						score_j+=8;
					}
					if(j24.isChecked()){
						if(j21.isChecked()||j22.isChecked()||j23.isChecked()){
							Toast.makeText(UslotteryRecordStandard.this,"j21,j22,j23,j24只能选一项！", Toast.LENGTH_SHORT).show();
			            	return;	
						}
						builder.append("J24").append(",");
					}
					if((!j24.isChecked())&&(!j21.isChecked())&&(!j22.isChecked())&&(!j23.isChecked())){
						Toast.makeText(UslotteryRecordStandard.this,"j2项为必填项,j21,j22,j23,j24只能选一项！", Toast.LENGTH_SHORT).show();
		            	return;	
					}
					if(j31.isChecked()){
						builder.append("J31").append(",");
						score+=2;
						score_j+=2;
					}
					if(j32.isChecked()){
						builder.append("J32").append(",");
						score+=3;
						score_j+=3;
					}
					if(j33.isChecked()){
						builder.append("J33").append(",");
						score+=5;
						score_j+=5;
					}
					if(j34.isChecked()){
						builder.append("J34").append(",");
						score+=4;
						score_j+=4;
					}
					
					
					if(j35.isChecked()){
						builder.append("J35").append(",");
						score+=2;
						score_j+=2;
					}
					if(j36.isChecked()){
						builder.append("J36").append(",");
						score+=2;
						score_j+=2;
					}
					if(j37.isChecked()){
						builder.append("J37").append(",");
						score+=2;
						score_j+=2;
					}
					if(j38.isChecked()){
						builder.append("J38").append(",");
						score+=2;
						score_j+=2;
					}
					if(j41.isChecked()){
						builder.append("J41").append(",");
						score+=3;
						score_j+=3;
					}
					if(j42.isChecked()){
						builder.append("J42").append(",");
						score+=3;
						score_j+=3;
					}
					if(j43.isChecked()){
						builder.append("J43").append(",");
						score+=3;
						score_j+=3;
					}
					if(j44.isChecked()){
						builder.append("J44").append(",");
						score+=3;
						score_j+=3;
					}
					if(j45.isChecked()){
						builder.append("J45").append(",");
						score+=4;
						score_j+=4;
					}
					if(j46.isChecked()){
						builder.append("J46").append(",");
						score+=2;
						score_j+=2;
					}
					if(j47.isChecked()){
						builder.append("J47").append(",");
						score+=2;
						score_j+=2;
					}
					if(j51.isChecked()){
						builder.append("J51").append(",");
						score+=3;
						score_j+=3;
					}
					if(j52.isChecked()){
						builder.append("J52").append(",");
						score+=3;
						score_j+=3;
					}
					if(j53.isChecked()){
						builder.append("J53").append(",");
						score+=2;
						score_j+=2;
					}
					if(j54.isChecked()){
						builder.append("J54").append(",");
						score+=2;
						score_j+=2;
					}
					if(j55.isChecked()){
						builder.append("J55").append(",");
						score+=2;
						score_j+=2;
					}
			//	}
			//	if(lly.getVisibility() == 0){
					if(y.isChecked()){
						builder.append("Y").append(",");
					}
					if(y1.isChecked()){
						builder.append("Y1").append(",");
					}
					if(y2.isChecked()){
						builder.append("Y2").append(",");
					}
					if(y3.isChecked()){
						builder.append("Y3").append(",");
						score+=4;
						score_yr+=4;
					}
					if(y4.isChecked()){
						builder.append("Y4").append(",");
						score+=4;
						score_yr+=4;
					}
					if(y11.isChecked()){
						builder.append("Y11").append(",");
						score+=5;
						score_yr+=5;
					}
					if(y12.isChecked()){
						builder.append("Y12").append(",");
						score+=3;
						score_yr+=3;
					}
					if(y21.isChecked()){
						builder.append("Y21").append(",");
						score+=5;
						score_yr+=5;
					}
					if(y22.isChecked()){
						builder.append("Y22").append(",");
						score+=3;
						score_yr+=3;
					}
					if(y23.isChecked()){
						builder.append("Y23").append(",");
						score+=1;
						score_yr+=1;
					}
			//	}
			//	if(llr.getVisibility() == 0){
					if(r.isChecked()){
						builder.append("R").append(",");
					}
					if(r1.isChecked()){
						builder.append("R1").append(",");
					}
					if(r2.isChecked()){
						builder.append("R2").append(",");
					}
					if(r3.isChecked()){
						builder.append("R3").append(",");
						score+=3;
						score_yr+=3;
					}
					if(r4.isChecked()){
						builder.append("R4").append(",");
						score+=4;
						score_yr+=4;
					}
					if(r5.isChecked()){
						builder.append("R5").append(",");
					}
					if(r6.isChecked()){
						builder.append("R6").append(",");
					}
					if(r11.isChecked()){
						builder.append("R11").append(",");
						score+=4;
						score_yr+=4;
					}
					if(r12.isChecked()){
						builder.append("R12").append(",");
						score+=4;
						score_yr+=4;
					}
					if(r13.isChecked()){
						builder.append("R13").append(",");
						score+=4;
						score_yr+=4;
					}if(r21.isChecked()){
						builder.append("R21").append(",");
						score+=2;
						score_yr+=2;
					}
					if(r22.isChecked()){
						builder.append("R22").append(",");
						score+=2;
						score_yr+=2;
					}
					if(r23.isChecked()){
						builder.append("R23").append(",");
						score+=2;
						score_yr+=2;
					}
					
					if(r51.isChecked()){
						builder.append("R51").append(",");
						score+=2;
						score_yr+=2;
					}
					if(r52.isChecked()){
						builder.append("R52").append(",");
						score+=2;
						score_yr+=2;
					}
					if(r53.isChecked()){
						builder.append("R53").append(",");
						score+=2;
						score_yr+=2;
					}
					if(r61.isChecked()){
						builder.append("R61").append(",");
						score+=2;
						score_yr+=2;
					}
					if(r62.isChecked()){
						builder.append("R62").append(",");
						score+=2;
						score_yr+=2;
					}
			//	}
				if(builder.toString().trim().equals("")){
					Toast.makeText(UslotteryRecordStandard.this, "j2项为必填项，且只能选其中的一项！", Toast.LENGTH_SHORT).show();
					return;
				}
				tvwdzh.setText("网点综合形象:   扣"+score_ab+"分");
				tvlt.setText("网点乐透型标准:   扣"+score_dg+"分");
				tvjk.setText("网点即开票标准:   扣"+score_ab+"分");
				tvjc.setText("网点竞彩标准:  扣"+score_ab+"分");
				String options = builder.toString();
				options = options.substring(0, options.length() - 1);
				SharedPreferences save_sp = getSharedPreferences("save_sp", MODE_WORLD_WRITEABLE);
				save_sp.edit().putString("save", options).commit();
				
				Toast.makeText(UslotteryRecordStandard.this, builder.toString(), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(UslotteryRecordStandard.this,UslotteryRecord.class);
				intent.putExtra("options",options);
				intent.putExtra("score", score);
				UslotteryRecordStandard.this.setResult(3, intent);
				UslotteryRecordStandard.this.finish();
			}
		});
        
       // tvwdzh.setOnClickListener(this);
       // tvlt.setOnClickListener(this);
       // tvjk.setOnClickListener(this);
       // tvjc.setOnClickListener(this);
        //a.setOnCheckedChangeListener(this);
        
        b.setOnClickListener(this);
        d.setOnClickListener(this);
        g.setOnClickListener(this);
        j.setOnClickListener(this);
        y.setOnClickListener(this);
        r.setOnClickListener(this);
        jc.setOnClickListener(this);
        d3.setOnCheckedChangeListener(this);
        d4.setOnCheckedChangeListener(this);
        g1.setOnCheckedChangeListener(this);
        g2.setOnCheckedChangeListener(this);
        g3.setOnCheckedChangeListener(this);
        j1.setOnCheckedChangeListener(this);
        j2.setOnCheckedChangeListener(this);
        j3.setOnCheckedChangeListener(this);
        j4.setOnCheckedChangeListener(this);
        j5.setOnCheckedChangeListener(this);
        y1.setOnCheckedChangeListener(this);
        y2.setOnCheckedChangeListener(this);
        r1.setOnCheckedChangeListener(this);
        r2.setOnCheckedChangeListener(this);
        r5.setOnCheckedChangeListener(this);
        r6.setOnCheckedChangeListener(this);
         
//        b.setOnCheckedChangeListener(this);
//        d.setOnCheckedChangeListener(this);
//        g.setOnCheckedChangeListener(this);
//        j.setOnCheckedChangeListener(this);
//        y.setOnCheckedChangeListener(this);
//        r.setOnCheckedChangeListener(this);
        jc.setOnCheckedChangeListener(this);
        }
	
	public void b_allSelect(){
		b1.setChecked(true);
		b2.setChecked(true);
		b3.setChecked(true);
		b4.setChecked(true);
		b5.setChecked(true);
		b6.setChecked(true);
		b7.setChecked(true);
	}
	
	public boolean b_anySelect(){
		if(b1.isChecked()||b2.isChecked()||b3.isChecked()||b4.isChecked()
				||b5.isChecked()||b6.isChecked()||b7.isChecked()){
			return true;
		}
			return false;
	}
	
	public void d_allSelect(){
		d1.setChecked(true);
		d2.setChecked(true);
		d3.setChecked(true);
		d4.setChecked(true);
		d5.setChecked(true);
		d6.setChecked(true);
		d7.setChecked(true);
		d8.setChecked(true);
		d31.setChecked(true);
		d32.setChecked(true);
		d41.setChecked(true);
		d42.setChecked(true);
		d43.setChecked(true);	
	}
	
	public boolean d_anySelect(){
		if(d1.isChecked()||d2.isChecked()||d3.isChecked()||d4.isChecked()
				||d5.isChecked()||d6.isChecked()||d7.isChecked()||d8.isChecked()
				||d31.isChecked()||d32.isChecked()||d41.isChecked()
				||d42.isChecked()||d43.isChecked()){
			return true;
		}
			return false;
	}
	
	public void g_allSelect(){
		g1.setChecked(true);
		g2.setChecked(true);
		g3.setChecked(true);
		g4.setChecked(true);
		g5.setChecked(true);
		g6.setChecked(true);
		g11.setChecked(true);
		g12.setChecked(true);
		g21.setChecked(true);
		g22.setChecked(true);
		g31.setChecked(true);
		g32.setChecked(true);
		g33.setChecked(true);
	}
	public boolean g_anySelect(){
		if(g1.isChecked()||g2.isChecked()||g3.isChecked()||g4.isChecked()
				||g5.isChecked()||g6.isChecked()||g11.isChecked()||g12.isChecked()
				||g21.isChecked()||g22.isChecked()||g31.isChecked()
				||g32.isChecked()||g33.isChecked()){
			return true;
		}
			return false;
	}
	public void j_allSelect(){
		j1.setChecked(true);
		j2.setChecked(true);
		j3.setChecked(true);
		j4.setChecked(true);
		j5.setChecked(true);
		j11.setChecked(true);
		j12.setChecked(true);
		j13.setChecked(true);
		j21.setChecked(true);
		j22.setChecked(true);
		j23.setChecked(true);
		j24.setChecked(true);
		j31.setChecked(true);
		j32.setChecked(true);
		j33.setChecked(true);
		j34.setChecked(true);
		j35.setChecked(true);
		j36.setChecked(true);
		j37.setChecked(true);
		j38.setChecked(true);
		j41.setChecked(true);
		j42.setChecked(true);
		j43.setChecked(true);
		j44.setChecked(true);
		j45.setChecked(true);
		j46.setChecked(true);
		j47.setChecked(true);
		j51.setChecked(true);
		j52.setChecked(true);
		j53.setChecked(true);
		j54.setChecked(true);
		j55.setChecked(true);
	}
	public boolean j_anySelect(){
		if(j1.isChecked()||j2.isChecked()||j3.isChecked()||j4.isChecked()
				||j5.isChecked()||j11.isChecked()||j12.isChecked()||j13.isChecked()
				||j21.isChecked()||j22.isChecked()||j23.isChecked()
				||j24.isChecked()||j31.isChecked()||j32.isChecked()
				||j33.isChecked()||j34.isChecked()||j35.isChecked()
				||j36.isChecked()||j37.isChecked()||j38.isChecked()
				||j41.isChecked()||j42.isChecked()||j43.isChecked()
				||j44.isChecked()||j45.isChecked()||j46.isChecked()
				||j47.isChecked()||j51.isChecked()||j52.isChecked()
				||j53.isChecked()||j54.isChecked()||j55.isChecked()){
			return true;
		}
			return false;
	}
	public void y_allSelect(){
		y1.setChecked(true);
		y2.setChecked(true);
		y3.setChecked(true);
		y4.setChecked(true);
		y11.setChecked(true);
		y12.setChecked(true);
		y21.setChecked(true);
		y22.setChecked(true);
		y23.setChecked(true);
	}
	public boolean y_anySelect(){
		if(y1.isChecked()||y2.isChecked()||y3.isChecked()||y4.isChecked()
				||y11.isChecked()||y12.isChecked()
				||y21.isChecked()||y22.isChecked()||y23.isChecked()
				){
			return true;
		}
			return false;
	}
	public void r_allSelect(){
		r1.setChecked(true);
		r2.setChecked(true);
		r3.setChecked(true);
		r4.setChecked(true);
		r5.setChecked(true);
		r6.setChecked(true);
		r11.setChecked(true);
		r12.setChecked(true);
		r13.setChecked(true);
		r21.setChecked(true);
		r22.setChecked(true);
		r23.setChecked(true);
		r51.setChecked(true);
		r52.setChecked(true);
		r53.setChecked(true);
		r61.setChecked(true);
		r62.setChecked(true);
	}
	public boolean r_anySelect(){
		if(r1.isChecked()||r2.isChecked()||r3.isChecked()||r4.isChecked()
				||r5.isChecked()||r6.isChecked()
				||r11.isChecked()||r12.isChecked()||r13.isChecked()
				||r21.isChecked()||r22.isChecked()||r23.isChecked()
				||r51.isChecked()||r52.isChecked()||r53.isChecked()
				||r61.isChecked()||r62.isChecked()
				){
			return true;
		}
			return false;
	}
		@Override
		public void onClick(View v) {
			CheckBox cv = (CheckBox)v;
			 if(cv == b){
				 if(flag_b){	
					
					    if(b_anySelect()){
					    	//b.setButtonDrawable(R.drawable.check_gray);
					    	b.setChecked(true);
					    }
					    b.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(R.drawable.expend), null);
						llb.setVisibility(View.GONE);
						flag_b = false;
					}else{
						  b.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(R.drawable.substract), null);
						 if(b_anySelect()){
							// b.setButtonDrawable(R.drawable.check_gray);
							 b.setChecked(true);	
						    }else{
						    	b_allSelect();
						    }
						llb.setVisibility(View.VISIBLE);
						flag_b = true;
					}
			}else if(cv == d){
				 if(flag_d){	
					    if(d_anySelect()){
					    	d.setChecked(true);
					    }
						  d.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(R.drawable.expend), null);
							
						lld.setVisibility(View.GONE);
						flag_d = false;
					}else{
						  d.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(R.drawable.substract), null);
							
						 if(d_anySelect()){
						    	d.setChecked(true);
						    }else{
						    	d_allSelect();
						    }

						lld.setVisibility(View.VISIBLE);
						flag_d = true;
					}
			}else if(cv == g){
				 if(flag_g){	
					    if(g_anySelect()){
					    	g.setChecked(true);
					    }
						  g.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(R.drawable.expend), null);
							
						llg.setVisibility(View.GONE);
						flag_g = false;
					}else{
						  g.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(R.drawable.substract), null);
							
						 if(g_anySelect()){
						    	g.setChecked(true);
						    }else{
						    	g_allSelect();
						    }

						llg.setVisibility(View.VISIBLE);
						flag_g = true;
					}
				
			}else if(cv == j){
				 if(flag_j){	
					    if(j_anySelect()){
					    	j.setChecked(true);
					    }
					    j.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(R.drawable.expend), null);
						
						llj.setVisibility(View.GONE);
						flag_j = false;
					}else{
						  j.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(R.drawable.substract), null);
							
						 if(j_anySelect()){
						    	j.setChecked(true);
						    }else{
						    	j_allSelect();
						    }

						llj.setVisibility(View.VISIBLE);
						flag_j = true;
					}
			}else if(cv == y){
				 if(flag_y){	
					    if(y_anySelect()){
					    	y.setChecked(true);
					    }
					    y.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(R.drawable.expend), null);
						
						lly.setVisibility(View.GONE);
						flag_y = false;
					}else{
						  y.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(R.drawable.substract), null);
							
						 if(y_anySelect()){
						    	y.setChecked(true);
						    }else{
						    	y_allSelect();
						    }

						lly.setVisibility(View.VISIBLE);
						flag_y = true;
					}
			}else if(cv == r){
				 if(flag_r){	
					    if(r_anySelect()){
					    	r.setChecked(true);
					    }
					    r.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(R.drawable.expend), null);
						
						llr.setVisibility(View.GONE);
						flag_r = false;
					}else{
						  r.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(R.drawable.substract), null);
							
						 if(r_anySelect()){
						    	r.setChecked(true);
						    }else{
						    	r_allSelect();
						    }

						llr.setVisibility(View.VISIBLE);
						flag_r = true;
					}
			}
			/*TextView tv = (TextView)v;
			if(tv == tvwdzh){
				lla.setVisibility(View.VISIBLE);
				llb.setVisibility(View.VISIBLE);
			}else if(tv == tvlt){
				lld.setVisibility(View.VISIBLE);
				llg.setVisibility(View.VISIBLE);
			}else if(tv == tvjk){
				llj.setVisibility(View.VISIBLE);
			}else if(tv == tvjc){
				lly.setVisibility(View.VISIBLE);
				llr.setVisibility(View.VISIBLE);
			}*/
			
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			CheckBox cb = (CheckBox)buttonView;
			if(cb == d3){
				if(isChecked){
					d31.setChecked(true);
					d32.setChecked(true);
				}else{
					d31.setChecked(false);
					d32.setChecked(false);
				}
			}else if(cb == d4){
				if(isChecked){
					d41.setChecked(true);
					d42.setChecked(true);
					d43.setChecked(true);
					}else{
						d41.setChecked(false);
						d42.setChecked(false);
						d43.setChecked(false);
					}
			}else if(cb == g1){
				if(isChecked){
					g11.setChecked(true);
					g12.setChecked(true);
					}else{
						g11.setChecked(false);
						g12.setChecked(false);	
					}
			}else if(cb == g2){
				if(isChecked){
					g21.setChecked(true);
					g22.setChecked(true);
					}else{
						g21.setChecked(false);
						g22.setChecked(false);	
					}
			}else if(cb == g3){
				if(isChecked){
					g31.setChecked(true);
					g32.setChecked(true);
					g33.setChecked(true);
					}else{
						g31.setChecked(false);
						g32.setChecked(false);
						g33.setChecked(false);
							
					}
			}else if(cb == j1){
				if(isChecked){
					j11.setChecked(true);
					j12.setChecked(true);
					j13.setChecked(true);
					}else{
						j11.setChecked(false);
						j12.setChecked(false);
						j13.setChecked(false);	
					}
			}else if(cb == j2){
				if(isChecked){
					j21.setChecked(true);
					j22.setChecked(true);
					j23.setChecked(true);
					j24.setChecked(true);
					}else{
						j21.setChecked(false);
						j22.setChecked(false);
						j23.setChecked(false);
						j24.setChecked(false);	
					}
			}else if(cb == j3){
				if(isChecked){
					j31.setChecked(true);
					j32.setChecked(true);
					j33.setChecked(true);
					j34.setChecked(true);
					j35.setChecked(true);
					j36.setChecked(true);
					j37.setChecked(true);
					j38.setChecked(true);
					}else{
						j31.setChecked(false);
						j32.setChecked(false);
						j33.setChecked(false);
						j34.setChecked(false);
						j35.setChecked(false);
						j36.setChecked(false);
						j37.setChecked(false);
						j38.setChecked(false);	
					}
			}else if(cb == j4){
				if(isChecked){
					j41.setChecked(true);
					j42.setChecked(true);
					j43.setChecked(true);
					j44.setChecked(true);
					j45.setChecked(true);
					j46.setChecked(true);
					j47.setChecked(true);
					}else{
						j41.setChecked(false);
						j42.setChecked(false);
						j43.setChecked(false);
						j44.setChecked(false);
						j45.setChecked(false);
						j46.setChecked(false);
						j47.setChecked(false);
					}
			}else if(cb == j5){
				if(isChecked){
					j51.setChecked(true);
					j52.setChecked(true);
					j53.setChecked(true);
					j54.setChecked(true);
					j55.setChecked(true);
					}else{
						j51.setChecked(false);
						j52.setChecked(false);
						j53.setChecked(false);
						j54.setChecked(false);
						j55.setChecked(false);
					}
			}else if(cb == y1){
				if(isChecked){
					y11.setChecked(true);
					y12.setChecked(true);
					}else{
						y11.setChecked(false);
						y12.setChecked(false);
					}
			}else if(cb == y2){
				if(isChecked){
					y21.setChecked(true);
					y22.setChecked(true);
					y23.setChecked(true);
					}else{
						y21.setChecked(false);
						y22.setChecked(false);
						y23.setChecked(false);
							
					}
			}else if(cb == r1){
				if(isChecked){
					r11.setChecked(true);
					r12.setChecked(true);
					r13.setChecked(true);
					}else{
						r11.setChecked(false);
						r12.setChecked(false);
						r13.setChecked(false);	
					}
			}else if(cb == r2){
				if(isChecked){
					r21.setChecked(true);
					r22.setChecked(true);
					r23.setChecked(true);
					}else{
						r21.setChecked(false);
						r22.setChecked(false);
						r23.setChecked(false);
					}
			}else if(cb == r5){
				if(isChecked){
					r51.setChecked(true);
					r52.setChecked(true);
					r53.setChecked(true);
					}else{
						r51.setChecked(false);
						r52.setChecked(false);
						r53.setChecked(false);
					}
			}else if(cb == r6){
				if(isChecked){
					r61.setChecked(true);
					r62.setChecked(true);
					
				}else{
					r61.setChecked(false);
					r62.setChecked(false);
				}
			}else if(cb ==b){
			
				if(isChecked){
				//	llb.setVisibility(View.VISIBLE);
					b1.setChecked(true);
					b2.setChecked(true);
					b3.setChecked(true);
					b4.setChecked(true);
					b5.setChecked(true);
					b6.setChecked(true);
					b7.setChecked(true);
					}else{
				//	llb.setVisibility(View.GONE);
					b1.setChecked(false);
					b2.setChecked(false);
					b3.setChecked(false);
					b4.setChecked(false);
					b5.setChecked(false);
					b6.setChecked(false);
					b7.setChecked(false);
					}
			}else if(cb == d){
				if(isChecked){
					//lld.setVisibility(View.VISIBLE);
					d1.setChecked(true);
					d2.setChecked(true);
					d3.setChecked(true);
					d4.setChecked(true);
					d5.setChecked(true);
					d6.setChecked(true);
					d7.setChecked(true);
					d8.setChecked(true);
					d31.setChecked(true);
					d32.setChecked(true);
					d41.setChecked(true);
					d42.setChecked(true);
					d43.setChecked(true);	
					d43.setChecked(true);
					}else{
					//lld.setVisibility(View.GONE);
					d1.setChecked(false);
					d2.setChecked(false);
					d3.setChecked(false);
					d4.setChecked(false);
					d5.setChecked(false);
					d6.setChecked(false);
					d7.setChecked(false);
					d8.setChecked(false);
					d31.setChecked(false);
					d32.setChecked(false);
					d41.setChecked(false);
					d42.setChecked(false);
					d43.setChecked(false);
					}
			}else if(cb == g){
				if(isChecked){
				//	llg.setVisibility(View.VISIBLE);
					g1.setChecked(true);
					g2.setChecked(true);
					g3.setChecked(true);
					g4.setChecked(true);
					g5.setChecked(true);
					g6.setChecked(true);
					g11.setChecked(true);
					g12.setChecked(true);
					g21.setChecked(true);
					g22.setChecked(true);
					g31.setChecked(true);
					g32.setChecked(true);
					g33.setChecked(true);
					}else{
					//llg.setVisibility(View.GONE);
					g1.setChecked(false);
					g2.setChecked(false);
					g3.setChecked(false);
					g4.setChecked(false);
					g5.setChecked(false);
					g6.setChecked(false);
					g11.setChecked(false);
					g12.setChecked(false);
					g21.setChecked(false);
					g22.setChecked(false);
					g31.setChecked(false);
					g32.setChecked(false);
					g33.setChecked(false);
				
					}
			}else if(cb == j){
				if(isChecked){
					//llj.setVisibility(View.VISIBLE);
					j1.setChecked(true);
					j2.setChecked(true);
					j3.setChecked(true);
					j4.setChecked(true);
					j5.setChecked(true);
					j11.setChecked(true);
					j12.setChecked(true);
					j13.setChecked(true);
					j21.setChecked(true);
					j22.setChecked(true);
					j23.setChecked(true);
					j24.setChecked(true);
					j31.setChecked(true);
					j32.setChecked(true);
					j33.setChecked(true);
					j34.setChecked(true);
					j35.setChecked(true);
					j36.setChecked(true);
					j37.setChecked(true);
					j38.setChecked(true);
					j41.setChecked(true);
					j42.setChecked(true);
					j43.setChecked(true);
					j44.setChecked(true);
					j45.setChecked(true);
					j46.setChecked(true);
					j47.setChecked(true);
					j51.setChecked(true);
					j52.setChecked(true);
					j53.setChecked(true);
					j54.setChecked(true);
					j55.setChecked(true);
					
				}else{
					//llj.setVisibility(View.GONE);
					j1.setChecked(false);
					j2.setChecked(false);
					j3.setChecked(false);
					j4.setChecked(false);
					j5.setChecked(false);
					j11.setChecked(false);
					j12.setChecked(false);
					j13.setChecked(false);
					j21.setChecked(false);
					j22.setChecked(false);
					j23.setChecked(false);
					j24.setChecked(false);
					j31.setChecked(false);
					j32.setChecked(false);
					j33.setChecked(false);
					j34.setChecked(false);
					j35.setChecked(false);
					j36.setChecked(false);
					j37.setChecked(false);
					j38.setChecked(false);
					j41.setChecked(false);
					j42.setChecked(false);
					j43.setChecked(false);
					j44.setChecked(false);
					j45.setChecked(false);
					j46.setChecked(false);
					j47.setChecked(false);
					j51.setChecked(false);
					j52.setChecked(false);
					j53.setChecked(false);
					j54.setChecked(false);
					j55.setChecked(false);	
				}
			}else if(cb == y){
				if(isChecked){
					//lly.setVisibility(View.VISIBLE);
					y1.setChecked(true);
					y2.setChecked(true);
					y3.setChecked(true);
					y4.setChecked(true);
					y11.setChecked(true);
					y12.setChecked(true);
					y21.setChecked(true);
					y22.setChecked(true);
					y23.setChecked(true);
				}else{
					//lly.setVisibility(View.GONE);
					y1.setChecked(false);
					y2.setChecked(false);
					y3.setChecked(false);
					y11.setChecked(false);
					y12.setChecked(false);
					y21.setChecked(false);
					y22.setChecked(false);
					y23.setChecked(false);
					}
			}else if(cb == r){
				if(isChecked){
				//	llr.setVisibility(View.VISIBLE);
					r1.setChecked(true);
					r2.setChecked(true);
					r3.setChecked(true);
					r4.setChecked(true);
					r5.setChecked(true);
					r6.setChecked(true);
					r11.setChecked(true);
					r12.setChecked(true);
					r13.setChecked(true);
					r21.setChecked(true);
					r22.setChecked(true);
					r23.setChecked(true);
					r51.setChecked(true);
					r52.setChecked(true);
					r53.setChecked(true);
					r61.setChecked(true);
					r62.setChecked(true);
					}else{
					//llr.setVisibility(View.GONE);
					r1.setChecked(false);
					r2.setChecked(false);
					r3.setChecked(false);
					r4.setChecked(false);
					r5.setChecked(false);
					r6.setChecked(false);
					r11.setChecked(false);
					r12.setChecked(false);
					r13.setChecked(false);
					r21.setChecked(false);
					r22.setChecked(false);
					r23.setChecked(false);
					r51.setChecked(false);
					r52.setChecked(false);
					r53.setChecked(false);
					r61.setChecked(false);
					r62.setChecked(false);
					}
			}else if(cb ==jc){
				if(isChecked){
					ll_wdjcbz.setVisibility(View.VISIBLE);
				}else{
					ll_wdjcbz.setVisibility(View.GONE);
				}
			}
			
		}
		
		public void parse(String result){		
				String[] contents = result.split(",");
				if(contents== null||contents.length<=0){
					return;
				}
				List<String> cons = new ArrayList<String>();
				for(int i = 0;i < contents.length ;i++){
					cons.add(contents[i]);
				}
				Toast.makeText(this, cons.toString(), Toast.LENGTH_SHORT).show();
				if(cons.contains("B")){
					flag_b = true;
					llb.setVisibility(View.VISIBLE);
					b.setChecked(true);
				}
				if(cons.contains("D")){
					flag_d = true;
					
					lld.setVisibility(View.VISIBLE);
					d.setChecked(true);
					}
				if(cons.contains("G")){
					flag_g = true;
					
					llg.setVisibility(View.VISIBLE);
					g.setChecked(true);
					}
				if(cons.contains("J")){
					flag_j = true;
					llj.setVisibility(View.VISIBLE);
					j.setChecked(true);
					}
				if(cons.contains("Y")){
					flag_y = true;
					lly.setVisibility(View.VISIBLE);
					y.setChecked(true);
					}
				if(cons.contains("R")){
					flag_r = true;
					llr.setVisibility(View.VISIBLE);
					r.setChecked(true);
					}
				
				if(cons.contains("A1")||cons.contains("A2")||
						cons.contains("A3")||
						cons.contains("A4")){
					lla.setVisibility(View.VISIBLE);
				}
				if(cons.contains("A1")){
					a1.setChecked(true);
				}
				if(cons.contains("A2")){
					a2.setChecked(true);
				}
				if(cons.contains("A3")){
					a3.setChecked(true);
				}
				if(cons.contains("A4")){
					a4.setChecked(true);
				}
				if(cons.contains("B1")){
					b1.setChecked(true);
				}
				if(cons.contains("B2")){
					b2.setChecked(true);
				}
				if(cons.contains("B3")){
					b3.setChecked(true);
				}
				if(cons.contains("B4")){
					b4.setChecked(true);
				}
				if(cons.contains("B5")){
					b5.setChecked(true);
				}
				if(cons.contains("B6")){
					b6.setChecked(true);
				}
				if(cons.contains("B7")){
					b7.setChecked(true);
				}
				if(cons.contains("D1")){
					d1.setChecked(true);
				}
				if(cons.contains("D2")){
					d2.setChecked(true);
				}
				if(cons.contains("D3")){
					d3.setChecked(true);
				}
				if(cons.contains("D4")){
					d4.setChecked(true);
				}
				if(cons.contains("D5")){
					d5.setChecked(true);
				}
				if(cons.contains("D6")){
					d6.setChecked(true);
				}
				if(cons.contains("D7")){
					d7.setChecked(true);
				}
				if(cons.contains("D8")){
					d8.setChecked(true);
				}
				if(cons.contains("D31")){
					d31.setChecked(true);
				}
				if(cons.contains("D32")){
					d32.setChecked(true);
				}
				if(cons.contains("D41")){
					d41.setChecked(true);
				}
				if(cons.contains("D42")){
					d42.setChecked(true);
				}
				if(cons.contains("D43")){
					d43.setChecked(true);
				}
				if(cons.contains("G1")){
					g1.setChecked(true);
				}
				if(cons.contains("G2")){
					g2.setChecked(true);
				}
				if(cons.contains("G3")){
					g3.setChecked(true);
				}
				if(cons.contains("G4")){
					g4.setChecked(true);
				}
				if(cons.contains("G5")){
					g5.setChecked(true);
				}
				if(cons.contains("G6")){
					g6.setChecked(true);
				}
				if(cons.contains("G11")){
					g11.setChecked(true);
				}
				if(cons.contains("G12")){
					g12.setChecked(true);
				}
				if(cons.contains("G21")){
					g21.setChecked(true);
				}
				if(cons.contains("G22")){
					g22.setChecked(true);
				}
				if(cons.contains("G31")){
					g31.setChecked(true);
				}
				if(cons.contains("G32")){
					g32.setChecked(true);
				}
				if(cons.contains("G33")){
					g33.setChecked(true);
				}
				if(cons.contains("J11")){
					j11.setChecked(true);
				}
				if(cons.contains("J12")){
					j12.setChecked(true);
				}
				if(cons.contains("J13")){
					j13.setChecked(true);
				}
				if(cons.contains("J21")){
					j21.setChecked(true);
				}
				if(cons.contains("J22")){
					j22.setChecked(true);
				}
				if(cons.contains("J23")){
					j23.setChecked(true);
				}
				if(cons.contains("J24")){
					j24.setChecked(true);
				}
				if(cons.contains("J31")){
					j31.setChecked(true);
				}
				if(cons.contains("J32")){
					j32.setChecked(true);
				}
				if(cons.contains("J33")){
					j33.setChecked(true);
				}
				if(cons.contains("J34")){
					j34.setChecked(true);
				}
				if(cons.contains("J35")){
					j35.setChecked(true);
				}
				if(cons.contains("J36")){
					j36.setChecked(true);
				}
				if(cons.contains("J37")){
					j37.setChecked(true);
				}
				if(cons.contains("J38")){
					j38.setChecked(true);
				}
				if(cons.contains("J41")){
					j41.setChecked(true);
				}
				if(cons.contains("J42")){
					j42.setChecked(true);
				}
				if(cons.contains("J43")){
					j43.setChecked(true);
				}
				if(cons.contains("J44")){
					j44.setChecked(true);
				}
				if(cons.contains("J45")){
					j45.setChecked(true);
				}
				if(cons.contains("J46")){
					j46.setChecked(true);
				}
				if(cons.contains("J47")){
					j47.setChecked(true);
				}
				if(cons.contains("J51")){
					j51.setChecked(true);
				}
				if(cons.contains("J52")){
					j52.setChecked(true);
				}
				if(cons.contains("J53")){
					j53.setChecked(true);
				}
				if(cons.contains("J54")){
					j54.setChecked(true);
				}
				if(cons.contains("J55")){
					j55.setChecked(true);
				}
				if(cons.contains("Y1")){
					y1.setChecked(true);
				}
				if(cons.contains("Y2")){
					y2.setChecked(true);
				}
				if(cons.contains("Y3")){
					y3.setChecked(true);
				}
				if(cons.contains("Y4")){
					y4.setChecked(true);
				}
				if(cons.contains("Y11")){
					y11.setChecked(true);
				}
				if(cons.contains("Y12")){
					y12.setChecked(true);
				}
				if(cons.contains("Y21")){
					y21.setChecked(true);
				}
				if(cons.contains("Y22")){
					y22.setChecked(true);
				}
				if(cons.contains("Y23")){
					y23.setChecked(true);
				}
				if(cons.contains("R1")){
					r1.setChecked(true);
				}
				if(cons.contains("R2")){
					r2.setChecked(true);
				}
				if(cons.contains("R3")){
					r3.setChecked(true);
				}
				if(cons.contains("R4")){
					r4.setChecked(true);
				}
				if(cons.contains("R5")){
					r5.setChecked(true);
				}
				if(cons.contains("R11")){
					r11.setChecked(true);
				}
				if(cons.contains("R12")){
					r12.setChecked(true);
				}
				if(cons.contains("R13")){
					r13.setChecked(true);
				}
				if(cons.contains("R21")){
					r21.setChecked(true);
				}
				if(cons.contains("R22")){
					r22.setChecked(true);
				}
				if(cons.contains("R23")){
					r23.setChecked(true);
				}
				if(cons.contains("R51")){
					r51.setChecked(true);
				}
				if(cons.contains("R52")){
					r52.setChecked(true);
				}
				if(cons.contains("R53")){
					r53.setChecked(true);
				}
				if(cons.contains("R61")){
					r61.setChecked(true);
				}
				if(cons.contains("R62")){
					r62.setChecked(true);
				}
		}	
}