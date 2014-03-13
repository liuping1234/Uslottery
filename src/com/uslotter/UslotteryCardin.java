package com.uslotter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.db.Obj;
import com.uslotter.db.ObjService;
import com.uslotter.nfc.Converter;
import com.uslotter.nfc.MifareBlock;
import com.uslotter.nfc.MifareClassCard;
import com.uslotter.nfc.MifareSector;
import com.uslotter.util.HttpUrl;
import com.uslotter.util.HttpUtil;
import com.uslotter.util.Util;
/**两种方式：
 * 刷卡获取卡id-->验证卡id
 * 输入网店编号-->验证网点编号
 */
public class UslotteryCardin extends Activity implements OnClickListener {
	Button btBack = null;
	EditText edWdTitle = null;
	RadioGroup mRadioGroup = null;
	RadioButton rb1 = null;
	RadioButton rb2 = null;
	RadioButton rb3 = null;
	Button btCommite = null;
	Button btCardError=null;
	TextView tv=null;
	int state = 0;// 没有刷IC卡原因
	String cardId = null;
	NfcAdapter mAdapter;
	PendingIntent mPendingIntent;
	IntentFilter[] mFilters;
	String[][] mTechLists;

	private final int AUTH = 1;
	private final int EMPTY_BLOCK_0 = 2;
	private final int EMPTY_BLOCK_1 = 3;
	private final int NETWORK = 4;
	private final String TAG = "mifare";
	public List<Activity> activityList = new ArrayList<Activity>();
	String msg = "";
	boolean isUpdateCheck = false;
	ObjService objService = null;
	List<Obj> objs =null;
	int index =0;
	int accumulate =0;
	Handler handler =new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			switch(what){
			case 5:
				//sendNotification(accumulate);
				if(msg.arg1 >0){
					accumulate++;
				}
				index++;	
				if(index <= objs.size()){
					if(index == objs.size()){
						for(int i = 0;i < objs.size();i++){
							int id = objs.get(i).get_id();
							objService.deleteApp(id);
						}
						
						sendNotification(accumulate,objs.size(),msg.obj.toString());
					}else{
						uploadSign(index);
					}
				}
				break;
			}		
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cardin);
		btBack = (Button) findViewById(R.id.qdback);
		btCardError=(Button) findViewById(R.id.carderror_in);
		btCardError.setOnClickListener(this);
		btBack.setOnClickListener(this);
		mRadioGroup = (RadioGroup) findViewById(R.id.rp_in);
		edWdTitle = (EditText) findViewById(R.id.ettitle_in);
		rb1 = (RadioButton) findViewById(R.id.RadioButton1_in);
		rb2 = (RadioButton) findViewById(R.id.RadioButton2_in);
		rb3 = (RadioButton) findViewById(R.id.RadioButton3_in);
		btCommite = (Button) findViewById(R.id.commite_in);
		tv=(TextView) findViewById(R.id.txtitle_in);
		
		tv.setVisibility(View.GONE);
		mRadioGroup.setVisibility(View.GONE);
		edWdTitle.setVisibility(View.GONE);
		rb1.setVisibility(View.GONE);
		rb2.setVisibility(View.GONE);
		rb3.setVisibility(View.GONE);
		btCommite.setVisibility(View.GONE);
		btCommite.setOnClickListener(this);

		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == rb1.getId()) {
					state = 1;
				} else if (checkedId == rb2.getId()) {
					state = 2;
				} else if (checkedId == rb3.getId()) {
					state = 3;
				}
			}
		});

		msg = this.getIntent().getStringExtra("msg");

		mAdapter = NfcAdapter.getDefaultAdapter(this);

		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		mFilters = new IntentFilter[] { ndef, };
		mTechLists = new String[][] { new String[] { MifareClassic.class
				.getName() } };

		Intent intent = getIntent();
		resolveIntent(intent);
		TextView data = (TextView) findViewById(R.id.inmessage);
		if (mAdapter == null) {
			data.setText("不支持刷网点IC卡方式");
		}
		
		if (msg != null && !msg.equals("")) {
			// Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
			data.setText(msg);
		}

		if(objService == null){
			objService =  new ObjService(UslotteryCardin.this);
		}
		String userId = Util.getSharedPrefercencesString(UslotteryCardin.this, "username");
		objs = objService.getAllAppsFromUserId(userId);
		//Toast.makeText(UslotteryCardin.this, "objs:"+objs.size(), Toast.LENGTH_SHORT).show();
		index = 0;
		if(objs.size() > 0){
			uploadSign(index);
		}
	}

	void resolveIntent(Intent intent) {
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			MifareClassic mfc = MifareClassic.get(tagFromIntent);
			MifareClassCard mifareClassCard = null;
			try {
				mfc.connect();
				byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
				cardId = getCardId(tagId);

				boolean auth = false;
				int secCount = mfc.getSectorCount();
				mifareClassCard = new MifareClassCard(secCount);
				int bCount = 0;
				int bIndex = 0;
				for (int j = 0; j < secCount; j++) {
					MifareSector mifareSector = new MifareSector();
					mifareSector.sectorIndex = j;
					// 6.1) authenticate the sector
					auth = mfc.authenticateSectorWithKeyA(j,
							MifareClassic.KEY_DEFAULT);
					if (auth) {
						// 6.2) In each sector - get the block count
						bCount = mfc.getBlockCountInSector(j);
						bCount = Math.min(bCount, MifareSector.BLOCKCOUNT);
						bIndex = mfc.sectorToBlock(j);
						// Log.e("test", j+" ---> "+bIndex);
						for (int i = 0; i < bCount; i++) {

							// 6.3) Read the block
							byte[] data = mfc.readBlock(bIndex);
							MifareBlock mifareBlock = new MifareBlock(data);
							mifareBlock.blockIndex = bIndex;
							// 7) Convert the data into a string from Hex
							// format.

							bIndex++;
							mifareSector.blocks[i] = mifareBlock;

						}
						mifareClassCard.setSector(mifareSector.sectorIndex,
								mifareSector);
					} else { // Authentication failed - Handle it

					}
				}
				ArrayList<String> blockData = new ArrayList<String>();
				int blockIndex = 0;
				for (int i = 0; i < secCount; i++) {
					MifareSector mifareSector = mifareClassCard.getSector(i);
					for (int j = 0; j < MifareSector.BLOCKCOUNT; j++) {
						MifareBlock mifareBlock = mifareSector.blocks[j];
						byte[] data = mifareBlock.getData();
						blockData
								.add(Converter.getHexString(data, data.length));

					}
				}
				String[] contents = new String[blockData.size()];
				blockData.toArray(contents);
				String c = "";
				for (String s : contents) {
					c += s + "\n";
				}

				doVibrator();
				goJkConfirmActivity(cardId);
//				if (HttpUtil.checkNet(this)) {
//					goJkConfirmActivity(cardId);
//				} else {
//					Toast.makeText(this, "网络不能使用，请确认网络是否打开？",
//							Toast.LENGTH_SHORT).show();
//				}

			} catch (IOException e) {
				doVibrator();
				Log.e(TAG, e.getLocalizedMessage());
				showAlert(3);
			} finally {
				if (mifareClassCard != null) {
					mifareClassCard.debugPrint();
				}
			}
		}// End of method
	}

	private void showAlert(int alertCase) {

		TextView message = (TextView) findViewById(R.id.inmessage);
		message.setText(msg);
		switch (alertCase) {

		case AUTH:
			Toast.makeText(this, "没有读取权限!!!", Toast.LENGTH_LONG).show();
			break;
		case EMPTY_BLOCK_0: // Block 0 Empty
			Toast.makeText(this, "第一分区数据为空!!!", Toast.LENGTH_LONG).show();
			break;
		case EMPTY_BLOCK_1:// Block 1 Empty
			message.setText("刷卡失败，请重试!");
			break;
		case NETWORK: // Communication Error
			Toast.makeText(this, "第一分区数据为空!!!", Toast.LENGTH_LONG).show();
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null)
			mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
					mTechLists);
	}

	@Override
	public void onNewIntent(Intent intent) {
		Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
		resolveIntent(intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mAdapter != null)
			mAdapter.disableForegroundDispatch(this);
	}

	private void doVibrator() {
		Vibrator v = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
		v.vibrate(200);
	}

	private String getCardId(byte[] tagId) {
		String cardId = "";
		String tmp = Converter.getHexString(tagId, tagId.length);
		int len = tmp.length();
		int size = len / 2;
		for (int i = 0; i < size; i++) {
			cardId += tmp.substring(len - 2, len);
			len = len - 2;
		}
		Log.d("卡ID", cardId);
		// Toast.makeText(this, "ID卡编号"+cardId, Toast.LENGTH_SHORT).show();
		return cardId;
	}

	public void goJkConfirmActivity(String cardId) {//卡id验证
		Intent intent = new Intent();
		intent.setAction("android.intent.action.JkConfirm");
		intent.putExtra("cardId", cardId);//卡id
		intent.putExtra("type", 1);// 签到，签退方法，1.刷卡，2.手输
		intent.putExtra("value", 1);// 1.签到，2.签退，3.录单
		startActivity(intent);
		finish();
	}

	public void goJkConfirmActivity1(String wdTitle, int error) {//网点号验证
		Intent intent = new Intent();
		intent.setAction("android.intent.action.JkConfirm");
		intent.putExtra("wdTitle", wdTitle);//网点编号
		intent.putExtra("state", state);// 不能刷卡的原因
		intent.putExtra("type", 2);// 签到，签退方法，1.刷卡，2.手输
		intent.putExtra("value", 1);// 1.签到，2.签退，3.录单
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.qdback:
			// 返回
			Intent intent = new Intent(UslotteryCardin.this,UslotteryMainMenu2.class);
			startActivity(intent);
			finish();
			break;
			
		case R.id.carderror_in:
			tv.setVisibility(View.VISIBLE);
			mRadioGroup.setVisibility(View.VISIBLE);
			edWdTitle.setVisibility(View.VISIBLE);
			rb1.setVisibility(View.VISIBLE);
			rb2.setVisibility(View.VISIBLE);
			rb3.setVisibility(View.VISIBLE);
			btCommite.setVisibility(View.VISIBLE);
			break;
		case R.id.commite_in://确定按钮

			String WdTitle = edWdTitle.getText().toString();
			if (!WdTitle.equals("") && WdTitle != null) {
				if (state != 0) {
					goJkConfirmActivity1(WdTitle, state);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UslotteryCardin.this);
					builder.setTitle("提示").setCancelable(false);
					builder.setMessage("请选择无法刷卡原因！");
					builder.setPositiveButton(
							"确定",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
					builder.create().show();
				}
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UslotteryCardin.this);
				builder.setTitle("提示").setCancelable(false);
				builder.setMessage("请输入网点编号！");
				builder.setPositiveButton("确定",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				builder.create().show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮 //
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(UslotteryCardin.this,UslotteryMainMenu2.class);
			startActivity(intent);
			finish();
			UslotteryCardin.this.overridePendingTransition(
					R.anim.push_right_in, R.anim.push_right_out);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	public void sendNotification(int count,int totalCount,String type){
		  //1.得到NotificationManager  
		        NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
		        //2.实例化一个通知，指定图标、概要、时间  
		        Notification n=new Notification(R.drawable.iconmarka,"通知",System.currentTimeMillis());  
		        //3.指定通知的标题、内容和intent  
		        Intent intent = new Intent(this, UslotteryMainMenu2.class);  
		        PendingIntent pi= PendingIntent.getActivity(this, 0, intent, 0);
//		        if(type.trim().equals("1")){
//		        	 n.setLatestEventInfo(this, "签到通知", "成功刷卡签到"+count+"个,共"+totalCount+"次签到", pi);         	
//		        }else if(type.trim().equals("2")){
//		        	 n.setLatestEventInfo(this, "签到通知", "成功手输签到"+count+"个,共"+totalCount+"次签到", pi);  		       	
//		        }else{
		         	 n.setLatestEventInfo(this, "签到通知", "成功签到"+count+"个,共"+totalCount+"次签到", pi);  		       		
		    //    }
		       //指定声音  
		        n.defaults = Notification.DEFAULT_SOUND;   
		        n.vibrate= new long[]{100,1000,500};
		        //4.发送通知  
		        nm.notify(1, n);  
	}
	
	protected void uploadSign(final int index) {
		new Thread(){//检查更新
			public void run(){
		if(objs == null){
			return;
		}
		Obj obj = null;
		
		if(index <= (objs.size()-1)){
		   obj = objs.get(index);
		}else{
			return;
		}
		
		String U_id = Util.getSharedPrefercencesString(UslotteryCardin.this,"uid");
		String c_id = Util.getSharedPrefercencesString(UslotteryCardin.this,"cid");
			try {
			if(HttpUtil.checkNet(UslotteryCardin.this)){
			Map<String, String> rawParams = new HashMap<String, String>();
			// IC卡确认方式
			if(obj.getType().trim().equals("1")){//刷卡
				rawParams.put("cardNo", obj.getMsg());// IC卡ID
				rawParams.put("WdTitle", "");// 网点编号
		    }else if(obj.getType().trim().equals("2")){//手输
					rawParams.put("cardNo", "");// IC卡ID
					rawParams.put("WdTitle", obj.getMsg());// 网点编号	
			}
			
			rawParams.put("U_id", U_id);// 专管员ID
			rawParams.put("type", obj.getType());// 签到类型
			
			rawParams.put("state", obj.getState());// 不能刷卡的原因
			rawParams.put("CenterID", c_id);
			// rawParams.put("date", datefmt.format(new Date()));
			String result = HttpUtil.postRequest(HttpUrl.URL + HttpUrl.CardURL,
					rawParams);
			JSONArray jsonArray = new JSONArray(result);
			Log.d("jsonarry", jsonArray.toString());
			if (jsonArray.length() > 0) {
				JSONObject obj1 = jsonArray.getJSONObject(0);
				int returnCode = (Integer) obj1.get("Msg");
				Message msg = handler.obtainMessage();
				msg.arg1 = returnCode;
				msg.obj = obj.getType();
				msg.what = 5;
				handler.sendMessage(msg);
			}
			}else{
				
			}
		} catch (ClientProtocolException e) {
			handler.sendEmptyMessage(-3);
		} catch (IOException e) {
			handler.sendEmptyMessage(-3);
		} catch (Exception e) {
			handler.sendEmptyMessage(-3);
		}
		}
		}.start();	
	}
}