package com.uslotter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.data.Item;
import com.uslotter.data.Option;
import com.uslotter.mode.App;
import com.uslotter.util.Contants;
public class UslotteryRecordStandardUpdateActivity extends Activity implements View.OnClickListener{
	private SAXParserFactory factory = null;
	private SAXParser parser = null;
	MyDefaultHandler handler = null;
	XMLReader reader = null;
	private InputStream is = null;
	private InputSource isource = null;
	public final int RLL_ID = 1;

	List<Option> options = null;
	public final int SPACE  = 60;
	public final int LEFT_MARGIN  = 10;
	public final int RIGHT_MARGIN  = 10;
	private List<Item> items = null;
	int lli;
	
	List<LinearLayout> lls = null;//lla llb lld llg
	List<LinearLayout> llss = null;//llab lldg llj0 llyr
	List<TextView> tvs_1 = null; //A B D G 
	List<TextView> tvs_2 = null; //J1 J2 J3 J4 
	List<CheckBox> cbs_2 = null; //A1 A2 A3 B1
	List<CheckBox> cbs_3 = null; //J11 J21 J23
	
	private Button bt_save,bt_return;
	StringBuilder builder = null;
	float score = 0;
	float score_ab = 0;
	float score_dg = 0;
	float score_j = 0;
	float score_yr = 0;
	
	private boolean flag_a = false;
	private boolean flag_b = false;
	private boolean flag_d = false;
	private boolean flag_g = false;
	private boolean flag_j = false;
	private boolean flag_y = false;
	private boolean flag_r = false;
	
	private CheckBox cb_tro,cb_con,cb_fre;
	private String status,gpstatus;
	//private boolean isClosed = false;
	private boolean isGpOpen = true;
	private boolean isAselect = false;
	private App app = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//this.setContentView(createView());
		
		app = (App)this.getIntent().getSerializableExtra("app");
		//Toast.makeText(this, app.getKfx(), Toast.LENGTH_SHORT).show();
		status = this.getIntent().getStringExtra("Status");
		gpstatus = this.getIntent().getStringExtra("gpstatus");
//		if(gpstatus.equals("未开通")){
//			isGpOpen = false;
//		}else{
//			isGpOpen = true;
//		}
		if(gpstatus.equals("未开通")){
			isGpOpen = false;
		}else if(gpstatus.equals("开通")){
			isGpOpen = true;
		}else if(gpstatus.equals("none")){
			isGpOpen = true;
		}
		this.setContentView(createView());
		//初始化
       // SharedPreferences sp = getSharedPreferences("save_sp", MODE_WORLD_WRITEABLE);
       // String con = sp.getString("save", "");
       // Toast.makeText(this, ",,."+con, Toast.LENGTH_SHORT).show();
		
		String con = app.getKfx();
        if(!con.equals("")){
        	parse(con);
        }
		for(CheckBox cb :cbs_2){
			Option op = (Option)cb.getTag();
			if(op.getId().contains("A")){
				cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						CheckBox _cb = (CheckBox)buttonView;
						if(isChecked){
							showAlertDialog(_cb);
						}
					}
				});
		      }
			}		
	}
	
	protected void showAlertDialog(final CheckBox cb) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提示");
		builder.setMessage("此项为一票否决项，勾选任意一项，该网点月标准化得分为0分，是否确认继续？");
		builder.setPositiveButton("确定", null);
       builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cb.setChecked(false);
			}
		});
       builder.create().show();
	}

	public View createView(){
		options = getOptions();
		items = handler.getItems();
		RelativeLayout rl1 = new RelativeLayout(this);
		LayoutParams lp1 = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		rl1.setBackgroundColor(Color.WHITE);
//		rl1.setId(RL_ID);
		rl1.setLayoutParams(lp1);
		
		RelativeLayout rl2 = new RelativeLayout(this);
		LayoutParams lp2 = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 80);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rl2.setBackgroundResource(R.drawable.main_navigation_highlight_bg);
		rl2.setId(RLL_ID);
		rl2.setLayoutParams(lp2);
		
		TextView tv1 = new TextView(this);
		LayoutParams lp3 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp3.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lp3.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp3.addRule(RelativeLayout.CENTER_VERTICAL);
		tv1.setGravity(Gravity.CENTER);
		tv1.setText("网点标准化");
		tv1.setTextColor(Color.WHITE);
		tv1.setTextSize(25);
		tv1.setLayoutParams(lp3);
		
		bt_save = new Button(this);
		LayoutParams lp4 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp4.addRule(RelativeLayout.CENTER_VERTICAL);
		lp4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		
		lp4.setMargins(0, 0, 20, 0);
		bt_save.setTextSize(18);
		bt_save.setTextColor(Color.WHITE);
		bt_save.setText("保存");
		bt_save.setBackgroundResource(R.drawable.zxbutton_selector);
		bt_save.setLayoutParams(lp4);
		bt_save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				score = 0;
				score_ab = 10;
				score_dg = 90;
				score_j = 90;
				score_yr = 90;
				
				builder = new  StringBuilder();
				if(cbs_2.size()<0){
					return;
				}
				if(cbs_3.size()<0){
					return;
				}
				
				for(int i = 0;i<cbs_2.size();i++){
					CheckBox cb = cbs_2.get(i);
					Option op = (Option)cb.getTag();
					if(op == null){
						continue;
					}
					
					if(cb.isChecked()){
						builder.append(op.getId()).append(",");
						if(op.getId().contains("A")){
						//	Toast.makeText(UslotteryRecordStandardActivity.this, "123987", Toast.LENGTH_SHORT).show();
							isAselect = true;
						}else if(op.getId().contains("B")){
							score_ab-=Integer.parseInt(op.getScore());	
						}else if(op.getId().contains("D")){
							//if(gpstatus.equals("已开通")){
							if(isGpOpen){	
								score_dg-=Integer.parseInt(op.getScore());
							}else{
								int s_dg = Integer.parseInt(op.getScore());
								score_dg-=s_dg*1.5;
							}	
						}else if(op.getId().contains("G")){
							//if(gpstatus.equals("已开通")){
								if(isGpOpen){
							score_dg-=Integer.parseInt(op.getScore());
							}
						}else if(op.getId().contains("J")){
							score_j-=Integer.parseInt(op.getScore());			
						}else if(op.getId().contains("Y")){
							score_yr-=Integer.parseInt(op.getScore());			
						}else if(op.getId().contains("R")){
							score_yr-=Integer.parseInt(op.getScore());
						}
					}
				}
				
				for(int j = 0;j<cbs_3.size();j++){
					CheckBox cb = cbs_3.get(j);
					Option op = (Option)cb.getTag();					
					if(cb.isChecked()){
						builder.append(op.getId()).append(",");
						if(op.getId().contains("A")){
							isAselect = true;
						}else if(op.getId().contains("B")){
							score_ab+=Integer.parseInt(op.getScore());		
						}else if(op.getId().contains("D")){
							//if(gpstatus.equals("已开通")){
								if(isGpOpen){
								score_dg-=Integer.parseInt(op.getScore());
							}else{
								int s_dg = Integer.parseInt(op.getScore());
								score_dg-=s_dg*1.5;
								//score+=Integer.parseInt(op.getScore());
							}			
						}else if(op.getId().contains("G")){
							//if(gpstatus.equals("已开通")){
							if(isGpOpen){
							score_dg-=Integer.parseInt(op.getScore());
							}
						}else if(op.getId().contains("J")){
							score_j-=Integer.parseInt(op.getScore());
						}else if(op.getId().contains("Y")){
							score_yr-=Integer.parseInt(op.getScore());			
						}else if(op.getId().contains("R")){
							score_yr-=Integer.parseInt(op.getScore());
						}
						//score+=Integer.parseInt(op.getScore());
					}	
				}
				
				//综合得分由三大玩法的总分相加除以3（无竞彩玩法除2）再加上综合形象分得出。
				if(cb_con.isChecked()){
					//竞彩玩法选中了
					score = (score_dg+score_yr+score_j)/3+score_ab;
				//	Toast.makeText(UslotteryRecordStandardActivity.this, score_dg+score_yr+score_j+"", Toast.LENGTH_SHORT).show();
					
				}else{
					//竞彩玩法没选中
					score = (score_dg+score_j)/2+score_ab;
				//	Toast.makeText(UslotteryRecordStandardActivity.this, score_dg+score_j+"", Toast.LENGTH_SHORT).show();
					}
				if(isAselect){
					score = 0;
				}
				String _score = String.valueOf(score);
				if(_score.length()>5){
					_score = _score.substring(0, 5);
					score = Float.parseFloat(_score);
				}
				
				
			//	Toast.makeText(UslotteryRecordStandardUpdateActivity.this, score+"", Toast.LENGTH_SHORT).show();
//				
				String result = builder.toString().trim();
//				if(result.equals("")
//				 ||result.contains("J11")&&result.contains("J12")
//				 ||result.contains("J13")&&result.contains("J12")
//				 ||result.contains("J11")&&result.contains("J13")
//				 ||result.contains("J11")&&result.contains("J12")&&result.contains("J13")
//				 ||!result.contains("J11")&&!result.contains("J12")&&!result.contains("J13")){
//					Toast.makeText(UslotteryRecordStandardActivity.this, "j1项为必填项，且只能选其中的一项！", Toast.LENGTH_SHORT).show();
//					return;	
//				}
				
				//tvwdzh.setText("网点综合形象:   扣"+score_ab+"分");
				//tvlt.setText("网点乐透型标准:   扣"+score_dg+"分");
				//tvjk.setText("网点即开票标准:   扣"+score_ab+"分");
				//tvjc.setText("网点竞彩标准:  扣"+score_ab+"分");
				if(result.length()> 0)
				result = result.substring(0, result.length() - 1);
//				SharedPreferences save_sp = getSharedPreferences("save_sp", MODE_WORLD_WRITEABLE);
//				save_sp.edit().putString("save", result).commit();
				
				Intent intent = new Intent(UslotteryRecordStandardUpdateActivity.this,UslotteryRecord_wdbzhUpdateActivity.class);
			//	intent.putExtra("options",result);
			//	intent.putExtra("score", score);
				app.setDf(score+"");
				app.setKfx(result);
				intent.putExtra("app", app);
				UslotteryRecordStandardUpdateActivity.this.setResult(3, intent);
				UslotteryRecordStandardUpdateActivity.this.finish();
				UslotteryRecordStandardUpdateActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);	
			}
		});
		
		bt_return = new Button(this);
		LayoutParams lp5 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp5.addRule(RelativeLayout.CENTER_VERTICAL);
		lp5.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lp5.setMargins(20, 0, 0, 0);
		bt_return.setTextSize(18);
		bt_return.setTextColor(Color.WHITE);
		bt_return.setBackgroundResource(R.drawable.zxbutton_selector);
		bt_return.setText("取消");
		bt_return.setLayoutParams(lp5);	
		bt_return.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UslotteryRecordStandardUpdateActivity.this,UnRecordFormActivity.class);	
				UslotteryRecordStandardUpdateActivity.this.startActivity(intent);
				UslotteryRecordStandardUpdateActivity.this.finish();
				UslotteryRecordStandardUpdateActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);					
			}
		});
		
		rl2.addView(tv1);
		rl2.addView(bt_save);
		rl2.addView(bt_return);
		
		ScrollView sv = new ScrollView(this);
		LayoutParams lp5_1 = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		lp5_1.addRule(RelativeLayout.BELOW,rl2.getId());
		//sv.setBackgroundResource(R.drawable.shape2);
		sv.setLayoutParams(lp5_1);
//		sv.setId(SCROLLVIEW_ID);
		
		LinearLayout ll1 = new LinearLayout(this);
		LayoutParams lp6 = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		ll1.setLayoutParams(lp6);
		ll1.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout ll1_1 = new LinearLayout(this);
		LayoutParams lp6_1 = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		lp6_1.setMargins(20, 10, 20, 0);
		ll1_1.setLayoutParams(lp6_1);
		ll1_1.setOrientation(LinearLayout.HORIZONTAL);
		ll1_1.setGravity(Gravity.CENTER);
		
		TextView tv1_1 = new TextView(this);
		android.widget.LinearLayout.LayoutParams lp6_2 = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);	
		tv1_1.setGravity(Gravity.CENTER);
		tv1_1.setTextColor(Color.BLACK);
		tv1_1.setTextSize(18);
		tv1_1.setText("彩种:");
		tv1_1.setLayoutParams(lp6_2);
		
		cb_tro = new CheckBox(this);
		android.widget.LinearLayout.LayoutParams lp6_3 = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);	
		cb_tro.setLayoutParams(lp6_3);
		cb_tro.setGravity(Gravity.CENTER);
		cb_tro.setText("传统+即开");
		cb_tro.setChecked(true);
		cb_tro.setEnabled(false);
		cb_tro.setTextColor(Color.BLACK);
//		cb_tro.setId(C_TRO_ID);
		
		cb_fre = new CheckBox(this);
		android.widget.LinearLayout.LayoutParams lp6_4 = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);	
		cb_fre.setLayoutParams(lp6_4);
		cb_fre.setGravity(Gravity.CENTER);
		cb_fre.setText("高频");
		if(isGpOpen){
			cb_fre.setChecked(true);
		}else{
			cb_fre.setChecked(false);
		}
		if(gpstatus.equals("none")){
			cb_fre.setEnabled(true);
		}else{
			cb_fre.setEnabled(false);
		}
cb_fre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					isGpOpen = true;
					
				//	Toast.makeText(UslotteryRecordStandardUpdateActivity.this, ""+isGpOpen, Toast.LENGTH_SHORT).show();
					//lls.get(3).setVisibility(View.VISIBLE);
				}else{
					isGpOpen = false; 
					if(lls.get(3).getVisibility()==View.VISIBLE){
						clearGselected();
						lls.get(3).setVisibility(View.GONE);
						for(int i = 0;i < tvs_1.size();i++){
							TextView tv = tvs_1.get(i);
							Option option = (Option)tv.getTag();
							if(option.getId().contains("G")){
								tv.setTextColor(Color.BLACK);
								 tv.setCompoundDrawablesWithIntrinsicBounds(UslotteryRecordStandardUpdateActivity.this.getResources().getDrawable(R.drawable.expend),null, null, null);
							}
						}
					}else{
						clearGselected();
						for(int i = 0;i < tvs_1.size();i++){
							TextView tv = tvs_1.get(i);
							Option option = (Option)tv.getTag();
							if(option.getId().contains("G")){
								tv.setTextColor(Color.BLACK);
							}
						}
					}
				//	Toast.makeText(UslotteryRecordStandardUpdateActivity.this, ""+isGpOpen, Toast.LENGTH_SHORT).show();
					
					//lls.get(3).setVisibility(View.VISIBLE);
				}	
			}
		});
		cb_fre.setTextColor(Color.BLACK);
//		cb_fre.setId(C_FRE_ID);
		
		cb_con = new CheckBox(this);
		android.widget.LinearLayout.LayoutParams lp6_5 = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);	
		cb_con.setLayoutParams(lp6_5);
		cb_con.setGravity(Gravity.CENTER);
		cb_con.setText("竞彩");
		cb_con.setTextColor(Color.BLACK);
//		cb_con.setId(C_CON_ID);
		cb_con.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					llss.get(3).setVisibility(View.VISIBLE);
				}else{
					llss.get(3).setVisibility(View.GONE);
				}	
			}
		});
		ll1_1.addView(tv1_1);
		ll1_1.addView(cb_tro);
		ll1_1.addView(cb_fre);
		ll1_1.addView(cb_con);
		 ll1.addView(ll1_1);
		lls= new ArrayList<LinearLayout>();
		tvs_1 = new ArrayList<TextView>();
		tvs_2 = new ArrayList<TextView>();
		cbs_2 = new ArrayList<CheckBox>();
		cbs_3 = new ArrayList<CheckBox>();
		llss = new ArrayList<LinearLayout>();
		
		 createOutBorder(items,options,ll1);
		
		sv.addView(ll1);
		
		rl1.addView(rl2,lp2);	
		rl1.addView(sv,lp5_1);
		return rl1;
		}
	
	public String getNewText(String id,List<Option> options){
		
		for(Option p :options){
			if(p.getId().equals(id)){
				if(p.getScore() == null){
					return id+":"+p.getIntro();
			}else{
				return id+":"+p.getIntro()+"("+p.getScore()+")";
			}
			}
		}
		return "";
	}
	
	public String getNewText(String id, Option op ){
	//	Log.i("tag",id+'.');
	
			if(op.getId().equals(id)){
				if(op.getScore() == null){
					return id+":"+op.getIntro();
			}else{
				return id+":"+op.getIntro()+"("+op.getScore()+")";
			}
			
		}
		return "";
	}
	
	public Option getOption(String id,List<Option> options){
		
		for(Option p :options){
			if(p.getId().equals(id)){
					return p;
			}
		}
		return null;
	}
	
	public boolean hasChild(String id,List<Option> options){	
		for(Option p :options){
			if(p.getId().equals(id)){
				if(Integer.parseInt(p.getChildnum())>0){
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
	
	public List<Option> getOptions(){
		List<Option> my_options = null;
		factory = SAXParserFactory.newInstance();
		try {
			parser = factory.newSAXParser();
			is = this.getClassLoader().getResourceAsStream("cells2.xml");
			isource = new InputSource(is); 
			handler = new MyDefaultHandler();
			try {
				parser.parse(isource, handler);
				my_options = handler.getOptions();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return my_options;
	}
	
	/**
	 * 创建一级目录
	 * @param items
	 * @param options
	 * @param ll
	 */
	public void createOutBorder(List<Item> items,List<Option> options,LinearLayout ll){
		for(int i = 0 ; i < items.size(); i++){
			Item item = items.get(i);
			LinearLayout ll1 = new LinearLayout(this);//llab lldg llj0 llyr
			android.widget.LinearLayout.LayoutParams lp7 = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			lp7.setMargins(20, 10, 20, 10);		
			ll1.setOrientation(LinearLayout.VERTICAL);
			ll1.setBackgroundResource(R.drawable.textview_boder);
			ll1.setLayoutParams(lp7);
		//	ll1.setBackgroundResource(R.drawable.shape2);
//			ll1.setId(Contants.LLAB_ID+i);
			if(i == (items.size()-1)){
				ll1.setVisibility(View.GONE);
			}
			llss.add(ll1);
		
			TextView tv_1 = new TextView(this);//网点综合形象，网点乐透型标准
			android.widget.LinearLayout.LayoutParams lp6_7_1 = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);	
			lp6_7_1.setMargins(10, 0, 30, 20);
			tv_1.setGravity(Gravity.CENTER_VERTICAL);
			tv_1.setTextColor(Color.BLACK);
			tv_1.setTextSize(22);
			tv_1.setText(item.getIntro());
			tv_1.setCompoundDrawablesWithIntrinsicBounds(null,null,this.getResources().getDrawable(R.drawable.a1),null);
			tv_1.setLayoutParams(lp6_7_1);
//			tv_1.setId(i+Contants.WDZHXX_ID);//..................................................
			//Log.i("tag", i+"");
			//cbs_1.add(tv_1);
			ll1.addView(tv_1);
			int size =item.getItems().size();
			if(size>0){
				for(int j = 0 ;j<size;j++ ){
					TextView tv = new TextView(this);//A B D G J Y R
					Option option = getOption(item.getItems().get(j), options);
					android.widget.LinearLayout.LayoutParams lp6_7_2 = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);	
					lp6_7_2.setMargins(10, 0, 30, 20);
					tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
					tv.setTextColor(Color.BLACK);
					tv.setTextSize(20);
					tv.setText(" "+getNewText(item.getItems().get(j), option));
					tv.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.expend),null,null,null);
					tv.setLayoutParams(lp6_7_2);
//					tv.setId(Contants.A_ID+100*lli);//A 10
					tv.setOnClickListener(this);
					tv.setTag(option);
					tvs_1.add(tv);
					ll1.addView(tv);
					createInnerBorder(ll1,item.getItems().get(j),options,lli++);
				}
			}
			ll.addView(ll1);
		}
	}
	
	/**
	 * 创建二级目录
	 * @param _ll
	 * @param text
	 * @param options
	 * @param _id
	 */
	public void createInnerBorder(LinearLayout _ll,String text,List<Option> options,int _id){
		for(Option op : options){
			if(op.getId().equals(text)){
				int size = Integer.parseInt(op.getChildnum());
				if(size>0){
					LinearLayout ll = new LinearLayout(this);//lla llb lld llg llj lly llr 
					android.widget.LinearLayout.LayoutParams lp6_7_3 = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);	
					ll.setVisibility(View.GONE);
					ll.setOrientation(LinearLayout.VERTICAL);
					ll.setLayoutParams(lp6_7_3);
//					ll.setId(_id+Contants.LLA_ID);//从1300开始.....................................
					
					for(int i = 0 ; i < size ; i ++){//A1,A2 B1,B2
						if(hasChild(text+(i+1), options)){
							TextView cba = new TextView(this);
							Option option = getOption(text+(i+1), options);
							android.widget.LinearLayout.LayoutParams lp7_4 = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);	
							lp7_4.setMargins(LEFT_MARGIN+SPACE,0,RIGHT_MARGIN,0);
							cba.setLayoutParams(lp7_4);
							cba.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
							cba.setText(getNewText(text+(i+1), option));
							cba.setTextColor(Color.BLACK);
							cba.setTextSize(16);
							cba.setId(Contants.A1_ID+i*10+_id*100);
							cba.setTag(option);
							//Log.i("tag",Contants.A1_ID+i*10+_id*100+"..." );
							tvs_2.add(cba);
							ll.addView(cba);
							createThreeWidget(ll,text+(i+1),options,cba.getId());
						}else{
							CheckBox cba = new CheckBox(this);
							Option option = getOption(text+(i+1), options);
							android.widget.LinearLayout.LayoutParams lp7_4 = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);	
							lp7_4.setMargins(LEFT_MARGIN+SPACE,0,RIGHT_MARGIN,0);
							cba.setLayoutParams(lp7_4);
							cba.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
							cba.setText(getNewText(text+(i+1), option));
							cba.setTextColor(Color.BLACK);
							cba.setTextSize(16);
							cba.setId(Contants.A1_ID+i*10+_id*100);
							
							cba.setTag(option);
						//	Log.i("tag",text+(i+1)+"..." );
							cbs_2.add(cba);
							ll.addView(cba);
						}
						
					}
					lls.add(ll);
					_ll.addView(ll);
					break;
				}
			}
		}
	}
	
	/**
	 * 创建三级目录
	 * @param ll
	 * @param text
	 * @param options
	 * @param id
	 */
	public void createThreeWidget(LinearLayout ll,String text,List<Option> options,int id){
		for(Option p:options){
			if(p.getId().equals(text)){
				//Log.i("tag", text);
				int size = Integer.parseInt(p.getChildnum());
				if(size > 0){
					for(int i = 0 ;i<size;i++){
					CheckBox cba = new CheckBox(this);
					Option option = getOption(text+(1+i), options);
					android.widget.LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);	
					lp.setMargins(LEFT_MARGIN+SPACE+SPACE,0,RIGHT_MARGIN,0);
					cba.setLayoutParams(lp);
					cba.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
					cba.setText(getNewText(text+(1+i), option));
					cba.setTextColor(Color.BLACK);
					cba.setId(id+1+i);
					cba.setTag(option);
					//Log.i("tag",id+1+i+"...");
					cbs_3.add(cba);
					ll.addView(cba);
					}
				}else{
					return;
				}
				break;
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		TextView tv = (TextView)v;
		Option p = (Option)tv.getTag();
		if(p == null){
			return;
		}
		if(p.getId().equals("A")){
			
			if(anySelect("A")){
				tv.setTextColor(Color.RED);
			}else{
				tv.setTextColor(Color.BLACK);
			}
			if(flag_a){	   		
				lls.get(0).setVisibility(View.GONE);
				flag_a = false;
			    tv.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.expend),null, null, null);
				
			}else{
				lls.get(0).setVisibility(View.VISIBLE);
				flag_a = true;
			    tv.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.substract),null, null, null);
			}
		}else if(p.getId().equals("B")){
			
			if(anySelect("B")){
				tv.setTextColor(Color.RED);
			}else{
				tv.setTextColor(Color.BLACK);
			}
			if(flag_b){	   		
				lls.get(1).setVisibility(View.GONE);
				flag_b = false;
			    tv.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.expend),null, null, null);
				
			}else{
				lls.get(1).setVisibility(View.VISIBLE);
				flag_b = true;
			    tv.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.substract),null, null, null);
			}
		}else if(p.getId().equals("D")){
			
			if(anySelect("D")){
				tv.setTextColor(Color.RED);
			}else{
				tv.setTextColor(Color.BLACK);
			}
			if(flag_d){	   		
				lls.get(2).setVisibility(View.GONE);
				flag_d = false;
			    tv.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.expend),null, null, null);
				
			}else{
				lls.get(2).setVisibility(View.VISIBLE);
				flag_d = true;
			    tv.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.substract),null, null, null);
			}
		}else if(p.getId().equals("G")){
			if(!isGpOpen){
				Toast.makeText(UslotteryRecordStandardUpdateActivity.this, "高频玩法未开通！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(anySelect("G")){
				tv.setTextColor(Color.RED);
			}else{
				tv.setTextColor(Color.BLACK);
			}
			if(flag_g){	   		
				lls.get(3).setVisibility(View.GONE);
				flag_g = false;
			    tv.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.expend),null, null, null);
				
			}else{
				lls.get(3).setVisibility(View.VISIBLE);
				flag_g = true;
			    tv.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.substract),null, null, null);
			}
		}else if(p.getId().equals("J")){
			
			if(anySelect("J")){
				tv.setTextColor(Color.RED);
			}else{
				tv.setTextColor(Color.BLACK);
			}
			if(flag_j){	   		
				lls.get(4).setVisibility(View.GONE);
				flag_j = false;
			    tv.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.expend),null, null, null);
			}else{
				lls.get(4).setVisibility(View.VISIBLE);
				flag_j = true;
			    tv.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.substract),null, null, null);
			}
		}else if(p.getId().equals("Y")){	
			if(anySelect("Y")){
				tv.setTextColor(Color.RED);
			}else{
				tv.setTextColor(Color.BLACK);
			}
			if(flag_y){	   		
				lls.get(5).setVisibility(View.GONE);
				flag_y = false;
			    tv.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.expend),null, null, null);
				
			}else{
				lls.get(5).setVisibility(View.VISIBLE);
				flag_y = true;
			    tv.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.substract),null, null, null);
			}
		}else if(p.getId().equals("R")){		
			if(anySelect("R")){
				tv.setTextColor(Color.RED);
			}else{
				tv.setTextColor(Color.BLACK);
			}
			if(flag_r){	   		
				lls.get(6).setVisibility(View.GONE);
				flag_r = false;
			    tv.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.expend),null, null, null);
				
			}else{
				lls.get(6).setVisibility(View.VISIBLE);
				flag_r = true;
			    tv.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.substract),null, null, null);
			}
		}
	
	}
	private boolean b_allSelect() {
		if(cbs_2.size()<0){
			return false;
		}
		if(cbs_2.get(4).isChecked()&&cbs_2.get(5).isChecked()&&cbs_2.get(6).isChecked()
				&&cbs_2.get(7).isChecked()&&cbs_2.get(8).isChecked()&&cbs_2.get(9).isChecked()&&
				cbs_2.get(10).isChecked()){
			return true;
		}
		return false;	
	}
	
	private boolean anySelect(String str) {
		if(cbs_2.size() > 0){//如果没有了
			for(CheckBox cb:cbs_2){
				Option option = (Option)cb.getTag();
				if(option == null){
					continue;
				}
				if(option.getId().contains(str)){
					if(cb.isChecked()){
						return true;
					}
				}
			}
		}
	
		
		if(cbs_3.size() > 0){
			for(CheckBox cb:cbs_3){
				Option option = (Option)cb.getTag();
				if(option == null){
					continue;
				}
				if(option.getId().contains(str)){
					if(cb.isChecked()){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void parse(String result) {
		String[] contents = result.split(",");
		
		if(contents== null||contents.length<=0){
			return;
		}
		
		//tvs_1
		setStatus(contents,"A");
		setStatus(contents,"B");
		setStatus(contents,"D");
		setStatus(contents,"G");
		setStatus(contents,"J");
		setStatus(contents,"Y");
		setStatus(contents,"R");
		for(int i = 0;i < contents.length ;i++){
			if(cbs_2.size() > 0){
				for(CheckBox cb : cbs_2){
					Option op = (Option)cb.getTag();
					if(op.getId().equals(contents[i])){
						cb.setChecked(true);
						break;
					}
				}
			}
			if(cbs_3.size() > 0){
				for(CheckBox cb : cbs_3){
					Option op = (Option)cb.getTag();
					if(op.getId().equals(contents[i])){
						cb.setChecked(true);
						break;
					}
				}
			}
		}

      }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮 //
		if (keyCode == KeyEvent.KEYCODE_BACK) {
				new AlertDialog.Builder(UslotteryRecordStandardUpdateActivity.this)
						.setTitle("提示！")
						.setMessage("是否退出修改")
						.setCancelable(false)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										Intent intent = new Intent(UslotteryRecordStandardUpdateActivity.this,UnRecordFormActivity.class);	
										UslotteryRecordStandardUpdateActivity.this.startActivity(intent);
										UslotteryRecordStandardUpdateActivity.this.finish();
										UslotteryRecordStandardUpdateActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
									}
								}).create().show();
			
			return true;

		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	public void setStatus(String[] strs, String str) {
		for (int i = 0; i < strs.length; i++) {

			if (strs[i].contains(str)) {
				for (TextView tv : tvs_1) {
					Option p = (Option) tv.getTag();
					if (p.getId().equals(str)) {
						tv.setTextColor(Color.RED);

						if (str.equals("Y") || str.equals("R")) {
							cb_con.setChecked(true);
							llss.get(3).setVisibility(View.VISIBLE);
						}
					}
				}
			}
		}
	}
	public void clearGselected(){
		if(cbs_2.size()<0){
			return ;
		}
		for(int i = 0;i < cbs_2.size();i ++){
			CheckBox cb = cbs_2.get(i);
			Option option = (Option)cb.getTag();
			if(option.getId().contains("G")){
				cb.setChecked(false);
			}
		}
		
	}
}