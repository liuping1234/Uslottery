package com.sd.everflourish.lot.scan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfclottery.BaseActivity;
import com.cr.uslotter.R;

public class DhActivity extends BaseActivity implements SensorEventListener{ 
    private SensorManager sensorManager; 
    private Vibrator vibrator;
    String sid="";
    ImageView img1,img2;
    private static final String TAG = "TestSensorActivity"; 
    private static final int SENSOR_SHAKE = 10; 
    private SoundPool sp;
    private int num1,num2;
    Animation am1,am2;
    DisplayMetrics dm ;
    TextView titNum;
    /** Called when the activity is first created. */ 
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.dhitem);
        
        TextView text=new TextView(this);
        TextPaint paint = text.getPaint();
        paint.setFakeBoldText(true); 
        
        img1=(ImageView) findViewById(R.id.img1);
        img2=(ImageView) findViewById(R.id.img2);
        sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        num1 = sp.load(this, R.raw.shake_sound_male, 1); 
        num2 = sp.load(this, R.raw.shake_match, 1); 
        dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);
      //(x1, x2, y1, y2)
        
        am1 = new TranslateAnimation ( 0, 0, 0,-dm.heightPixels/4);
        am2 = new TranslateAnimation ( 0, 0, 0,dm.heightPixels/4);
        am1.setDuration ( 1000 );
        am2.setDuration ( 1000 );
        am1.setRepeatCount( 0 );
        am2.setRepeatCount ( 0 );
        
        
        sid=getIntent().getStringExtra("sid");
        String code=sid.substring(0,2);//���ұ���
        
        if(!code.equals("35")){//���Ǽ�����Ʊ����ź�
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		   	builder.setMessage("���󣺲��Ǽ���Ʊ��ţ�").setCancelable(false);
		   	builder.setPositiveButton("����ɨ��", new android.content.DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					goToDigilinxActivity(null);
				}
		   		
		   	}).create().show();
		}
        else{
        	String playId=String.valueOf(sid.substring(2,6));//�淨
    		String info=String.valueOf(sid.substring(6,13));//������Ϣ
    		String bagNo=code+"-"+playId+"-"+info;
    		String serialNo=String.valueOf(sid.substring(13,16));//�����к�
    		String sidStr=code+"-"+playId+"-"+info+"-"+serialNo;
            
            titNum=(TextView) findViewById(R.id.titnum);
            titNum.setText("���кţ�"+sidStr);
            
            Button Lott=(Button) findViewById(R.id.but_shake);
            
            Lott.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				sp.play(num2, 1, 1, 0, 0, 1);
    				Intent intent=new Intent();
                    intent.putExtra("sid", sid);
    	    		intent.setAction("android.intent.action.DRAWHANDLE1");
    	    		startActivity(intent);
    	    		finish();
    			}
    		});
        }
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); 
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE); 
    } 
 
    @Override 
    protected void onResume() { 
        super.onResume(); 
        if (sensorManager != null) {// ע������� 
            sensorManager.registerListener(this, 
            		sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL); 
            // ��һ��������Listener���ڶ������������ô��������ͣ�����������ֵ��ȡ��������Ϣ��Ƶ�� 
        } 
    } 
 
    @Override 
    protected void onStop() { 
        super.onStop(); 
        if (sensorManager != null) {// ȡ�������� 
            sensorManager.unregisterListener(/*sensorEventListener*/this); 
        } 
    } 
 
    @Override 
    public void onAccuracyChanged(Sensor sensor, int accuracy) { 
    	
    } 
    
    @Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
    	 float[] values = event.values; 
         float x = values[0]; // x�᷽����������ٶȣ�����Ϊ�� 
         float y = values[1]; // y�᷽����������ٶȣ���ǰΪ�� 
         float z = values[2]; // z�᷽����������ٶȣ�����Ϊ�� 
         Log.i(TAG, "x�᷽����������ٶ�" + x +  "��y�᷽����������ٶ�" + y +  "��z�᷽����������ٶ�" + z); 
         
         //int medumValue = 15;
         if (Math.abs(x) > 13 || Math.abs(y) > 13 || Math.abs(z) > 13 || Math.abs(x) < -13 || Math.abs(y) < -13 || Math.abs(z) <-13) { 
             vibrator.vibrate(200);
             
             handler.postDelayed(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						 Log.i(TAG, "��⵽ҡ�Σ�ִ�в�����");
			                sp.play(num2, 1, 1, 0, 0, 1);
			                Intent intent=new Intent();
			                intent.putExtra("sid", sid);
				    		intent.setAction("android.intent.action.DRAWHANDLE1");
				    		startActivity(intent);
				    		finish();
					}
             }, 2000);
             
             final Message msg = new Message();
             onStop();
             msg.what = SENSOR_SHAKE;
             handler.sendMessage(msg); 
         } 
	}

    /**
     * ����ִ��
     */ 
    Handler handler = new Handler() { 
    	
        @Override 
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
            switch (msg.what) { 
            case SENSOR_SHAKE:
            	
            	img1.setAnimation (am1);
                img2.setAnimation(am2);
                img1. startAnimation(am1);
                img2. startAnimation(am2);
                img1.setVisibility(0);
                img2.setVisibility(0);
                sp.play(num1, 1, 1, 0, 0, 1);
            	
                break;
            } 
        } 
    };
    
    public void goToDigilinxActivity(String message) {
		 Intent intent = new Intent();
		 intent.setClass(this, DrawScanActivity.class);
	     startActivity(intent);
	    // SqliteHandle.Close();
	     finish();
	}
    
    @Override	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  //���¼����Ϸ��ذ�ť //
	  if(keyCode == KeyEvent.KEYCODE_BACK){ 
		  goToDigilinxActivity(null);
		  return true;
	 
	  }else{  
	   return super.onKeyDown(keyCode, event);	 
	  }	 
	 }
}
