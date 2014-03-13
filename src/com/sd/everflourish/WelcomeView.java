package com.sd.everflourish;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.cr.uslotter.R;

public class WelcomeView extends SurfaceView implements SurfaceHolder.Callback   //实现生命周期回调接口
{
	LotMainActivity activity;//activity的引用
	Paint paint;      //画笔
	int currentAlpha=0;  //当前的不透明值
	int screenWidth=480;   //屏幕宽度
	int screenHeight=320;  //屏幕高度
	int sleepSpan=50;      //动画的时延ms
	Bitmap[] logos=new Bitmap[1];//logo图片数组
	Bitmap currentLogo;  //当前logo图片引用
	int currentX;      //图片位置
	int currentY;
	public WelcomeView(LotMainActivity activity){
		super(activity);
		this.activity = activity;
		this.getHolder().addCallback(this);  //设置生命周期回调接口的实现者
		paint = new Paint();  //创建画笔
		paint.setAntiAlias(true);  //打开抗锯齿
		//加载图片
		logos[0]=BitmapFactory.decodeResource(activity.getResources(),  R.drawable.welcom);
		//logos[1]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.bnkjs);		
	}
	
	public void onDraw(Canvas canvas){	
		super.onDraw(canvas);
		//canvas.drawColor(Color.WHITE);
		//绘制黑填充矩形清背景
		//paint.setColor(Color.BLACK);//设置画笔颜色
		paint.setAlpha(255);//设置不透明度为255
		canvas.drawRect(0, 0, screenWidth, screenHeight, paint);
		//进行平面贴图
		if(currentLogo==null)return;
		paint.setAlpha(currentAlpha);		
		canvas.drawBitmap(currentLogo, 60, currentY, paint);	
		canvas.drawText("广东长荣科技有限公司  Android:v1.01", 60,screenHeight-30, paint);
	}
	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3){}
	
	//创建时被调用	
	public void surfaceCreated(SurfaceHolder holder){	
		new Thread(){
			public void run(){
				for(Bitmap bm:logos){
					currentLogo=bm;//当前图片的引用
					//currentX=screenWidth/2-bm.getWidth()/2;//图片位置
					currentX=bm.getWidth()/2;//图片位置
					currentY=screenHeight/2;
					//动态更改图片的透明度值并不断重绘	
					for(int i=255;i>-10;i=i-10){
						currentAlpha=i;
						//如果当前不透明度小于零
						if(currentAlpha<0){
							currentAlpha=0;//将不透明度置为零
						}
						SurfaceHolder myholder=WelcomeView.this.getHolder();//获取回调接口	
						Canvas canvas = myholder.lockCanvas();//获取画布
						try{
							//同步
							synchronized(myholder){
								onDraw(canvas);//进行绘制绘制
							}
						}catch(Exception e){
							e.printStackTrace();
						}finally{
							//如果当前画布不为空
							if(canvas!= null){
								myholder.unlockCanvasAndPost(canvas);//解锁画布
							}
						}
						try{
							//若是新图片，多等待一会
							if(i==255){
								Thread.sleep(1000);
							}
							Thread.sleep(sleepSpan);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
				activity.hd.sendEmptyMessage(1);//发送消息，开始加载棋子模型
			}
		}.start();
	}
	public void surfaceDestroyed(SurfaceHolder arg0){//销毁时被调用
	}
}