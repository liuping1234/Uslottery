package com.hz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.media.AudioManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cr.uslotter.R;
import com.uslotter.nfc.Converter;
import com.uslotter.nfc.MifareBlock;
import com.uslotter.nfc.MifareClassCard;
import com.uslotter.nfc.MifareSector;
import com.uslotter.util.HttpUtil;

public class ReadCardActivity extends Activity {

	// NFC parts
	private static NfcAdapter mAdapter;
	private static PendingIntent mPendingIntent;
	private static IntentFilter[] mFilters;
	private static String[][] mTechLists;

	private static final int AUTH = 1;
	private static final int EMPTY_BLOCK_0 = 2;
	private static final int EMPTY_BLOCK_1 = 3;
	private static final int NETWORK = 4;
	private static final String TAG = "mifare";
	public static List<Activity> activityList = new ArrayList<Activity>();
	String msg="";
	String xcid="";
	boolean isUpdateCheck=false;
	
	private TextView tv_title = null;
	private TextView tv_time = null;
	private Button btn_exit = null;
	private Button btn_card_bug = null;
	private RelativeLayout rll = null;
	private Button btn_ok = null;
	EditText edWdTitle = null;
	
	private String title = "";
	String time = "";
	//String wd = "";
	private String flag = "1";//区分是打卡还是手输 ,1打卡，2手输
	
//	private String title_r = "";
//	private String time_r ="";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.readcard);
		xcid=this.getIntent().getStringExtra("xcid");
		msg=this.getIntent().getStringExtra("msg");
		title = this.getIntent().getStringExtra("title");
		time = this.getIntent().getStringExtra("time");
		//wd = this.getIntent().getStringExtra("wd");
		
		rll = (RelativeLayout) this.findViewById(R.id.readcard_linear2);
		btn_ok = (Button)this.findViewById(R.id.readcard_commit);
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String WdTitle = edWdTitle.getText().toString();
				if (WdTitle.equals("") ||WdTitle == null) {		
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ReadCardActivity.this);
					builder.setTitle("提示").setCancelable(false);
					builder.setMessage("请输入网点编号！");
					builder.setPositiveButton("确定",null);
					builder.create().show();
					return;		
					//	goJkConfirmActivity1(WdTitle, state);
					
				}else if(WdTitle.length()!=5) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ReadCardActivity.this);
					builder.setTitle("提示").setCancelable(false);
					builder.setMessage("请输入正确网点编号！");
					builder.setPositiveButton("确定",null);
					builder.create().show();
					return;
				}else{				
					flag = "2";//手输
					goReadCardHandleActivity("",xcid,title,time,WdTitle,flag);
				}
			}
		});
		btn_exit = (Button)this.findViewById(R.id.readcard_qdback);
		btn_exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReadCardActivity.this,HzLotteryActivity.class);
				//intent.setAction("android.intent.action.WelcomeHzActivity");
				startActivity(intent);
				ReadCardActivity.this.finish();	
			}
		});
		btn_card_bug = (Button)this.findViewById(R.id.readcard_carderror);
		btn_card_bug.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//cardbug
				rll.setVisibility(View.VISIBLE);
			}
		});
		edWdTitle = (EditText) findViewById(R.id.readcard_ettitle_in);
		
		tv_title = (TextView) this.findViewById(R.id.readcard_title);
		tv_title.setText("主题："+title);
		tv_time = (TextView) this.findViewById(R.id.readcard_time);
		tv_time.setText("时间："+time);
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		// Create a generic PendingIntent that will be deliver to this activity.
		// The NFC stack
		// will fill in the intent with the details of the discovered tag before
		// delivering to
		// this activity.
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		// Setup an intent filter for all MIME based dispatches
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		mFilters = new IntentFilter[] { ndef, };

		// Setup a tech list for all NfcF tags
		mTechLists = new String[][] { new String[] { MifareClassic.class
				.getName() } };

		Intent intent = getIntent();
		resolveIntent(intent);
		TextView message=(TextView)findViewById(R.id.readcard_data);
		if(msg!=null&&!msg.equals("")){
			//Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
			message.setText(msg);
		}
		
		//Button btn=(Button)findViewById(R.id.btn);
		if(mAdapter==null){
			TextView data=(TextView)findViewById(R.id.readcard_data);
			data.setText("不支持刷网点IC卡方式");
		}
	
//		Button btn = (Button)findViewById(R.id.backbtn);
//		btn.setOnClickListener(new  android.view.View.OnClickListener(){
//			@Override
//			public void onClick(View v){
//				  Intent intent = new Intent();
//		          intent.setClass(ReadCardActivity.this, HzLotteryActivity.class);
//		          startActivity(intent);
//			}
//		});
	}
	

	void resolveIntent(Intent intent) {
	
		// 1) Parse the intent and get the action that triggered this intent
		String action = intent.getAction();
		// 2) Check if it was triggered by a tag discovered interruption.
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			
			// 3) Get an instance of the TAG from the NfcAdapter
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			
			//String id= new String(strvalue.getBytes("ISO8859_1"), "GBK");
					
			// 4) Get an instance of the Mifare classic card from this TAG
			// intent
			MifareClassic mfc = MifareClassic.get(tagFromIntent);
			MifareClassCard mifareClassCard=null;

			try { // 5.1) Connect to card
				mfc.connect();
				
				byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
				//Log.e("-------tab id: ", new String(tagId,"ISO14443"));
				String cardId=getCardId(tagId);
			
				boolean auth = false;
				// 5.2) and get the number of sectors this card has..and loop
				// thru these sectors
				int secCount = mfc.getSectorCount();
				mifareClassCard= new MifareClassCard(secCount);
				int bCount = 0;
				int bIndex = 0;
				for (int j = 0; j < secCount; j++) {
					MifareSector mifareSector = new MifareSector();
					mifareSector.sectorIndex = j;
					// 6.1) authenticate the sector
					auth = mfc.authenticateSectorWithKeyA(j,MifareClassic.KEY_DEFAULT);
					//String key="everflourish";
					//MifareKey mkey=new MifareKey();
					//mkey.setKey(key.getBytes());
					//auth = mfc.authenticateSectorWithKeyB(j,mkey.getKey());

					if (auth) {
						// 6.2) In each sector - get the block count
						bCount = mfc.getBlockCountInSector(j);
						bCount =Math.min(bCount, MifareSector.BLOCKCOUNT);
						bIndex = mfc.sectorToBlock(j);
						//Log.e("test", j+" ---> "+bIndex);
						for (int i = 0; i < bCount; i++) {

							// 6.3) Read the block
							byte []data = mfc.readBlock(bIndex);
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
				ArrayList<String> blockData=new ArrayList<String>();
				int blockIndex=0;
				for(int i=0;i<secCount;i++){					
					MifareSector mifareSector=mifareClassCard.getSector(i);
					for(int j=0;j<MifareSector.BLOCKCOUNT;j++){
						MifareBlock mifareBlock=mifareSector.blocks[j];
						byte []data=mifareBlock.getData();
						blockData.add(Converter.getHexString(data, data.length));
					
					}
				}
				String []contents=new String[blockData.size()];
				blockData.toArray(contents);
			    String c="";
				for(String s:contents){
					//Log.e("data", s);
					c+=s+"\n";
				}
				
				doVibrator();
				//String cardId=contents[0].substring(0,8);//"5F89231C";//
				if(HttpUtil.checkNet(this)){
					//cardId="04A99A42";
					flag = "1";//打卡
					goReadCardHandleActivity(cardId,xcid,title,time,"",flag);
				}else{
					Toast.makeText(this, "网络不能使用，请确认网络是否打开？", Toast.LENGTH_SHORT).show();
				}
					
			} catch (IOException e) {
				doVibrator();
				Log.e(TAG, e.getLocalizedMessage());
				showAlert(3);
			}finally{

				if(mifareClassCard!=null){
					mifareClassCard.debugPrint();
				}
			}
		}// End of method
	}
	
	
	private void doAudio(){
		AudioManager m=(AudioManager)this.getSystemService(Service.AUDIO_SERVICE);
		m.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
		m.playSoundEffect(AudioManager.ADJUST_LOWER);
	}
	
	private void doVibrator(){
		Vibrator v=(Vibrator)this.getSystemService(Service.VIBRATOR_SERVICE);
		v.vibrate(200);
	}
	private void showAlert(int alertCase) {
		
		TextView message=(TextView)findViewById(R.id.readcard_message);
		//if(msg!=null&&!msg.equals("")){
			//Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
			message.setText(msg);
		//}
		// prepare the alert box
		//AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		switch (alertCase) {

		case AUTH:// Card Authentication Error
			//alertbox.setMessage("Authentication Failed ");
			Toast.makeText(this, "没有读取权限!!!", Toast.LENGTH_LONG).show();
			break;
		case EMPTY_BLOCK_0: // Block 0 Empty
			//alertbox.setMessage("Failed reading ");
			Toast.makeText(this, "第一分区数据为空!!!", Toast.LENGTH_LONG).show();
			break;
		case EMPTY_BLOCK_1:// Block 1 Empty
			//alertbox.setMessage("读卡失败，请重试!!!");
			//data.setText("读卡失败，请重试!!!");
			//Toast.makeText(this, "读卡失败，请重试!", Toast.LENGTH_LONG).show();
			message.setText("刷卡失败，请重试!");
			break;
		case NETWORK: // Communication Error
			//alertbox.setMessage("Tag reading error");
			Toast.makeText(this, "第一分区数据为空!!!", Toast.LENGTH_LONG).show();
			break;
		}
		// set a positive/yes button and create a listener
//		alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//			// Save the data from the UI to the database - already done
//			public void onClick(DialogInterface arg0, int arg1) {
//				clearFields();
//			}
//		});
		// display box
		//alertbox.show();

	}


	private void clearFields() {
		//TextView data=(TextView)this.findViewById(R.id.data);
  		//data.setText("");
	}

	

	@Override
	public void onResume() {
		super.onResume();
		if(mAdapter!=null)
		mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,mTechLists);
	}

	@Override
	public void onNewIntent(Intent intent) {
		Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
		resolveIntent(intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		if(mAdapter!=null)
		mAdapter.disableForegroundDispatch(this);
	}
	
	//查询网点
	public void goReadCardHandleActivity(String cardId,String xcid,String title,String time,String wd,String flag) {
		 Intent intent = new Intent();
		 intent.putExtra("cardId", cardId);
		 intent.putExtra("xcid", xcid);
		 intent.putExtra("title", title);
		 intent.putExtra("wd", wd);
		 intent.putExtra("time", time);
		 intent.putExtra("flag", flag);
		 intent.setAction("android.intent.action.READCARDHND");
	     startActivity(intent);
	     finish();
	}

    @Override	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  //按下键盘上返回按钮 //
	  if(keyCode == KeyEvent.KEYCODE_BACK){ 
		//  Intent intent = new Intent();
			// intent.setAction("android.intent.action.HZLOTTERY");
		    // startActivity(intent);
		  Intent intent = new Intent();
          intent.setClass(ReadCardActivity.this, HzLotteryActivity.class);
          startActivity(intent);
          ReadCardActivity.this.finish();
		  return true;
	 
	  }else{  
	   return super.onKeyDown(keyCode, event);	 
	  }	 
	 }
//    
    NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs =intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs = (NdefMessage[]) rawMsgs;
                }
            }
            else {
            // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty,empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
            }
        } else {
            Log.e(TAG, "Unknown intent " + intent);
            finish();
        }
        return msgs;
    }

    private String getCardId(byte[] tagId){
    	String cardId="";
    	String tmp=Converter.getHexString(tagId, tagId.length);
    	int len=tmp.length();
    	int size=len/2;
    	for(int i=0;i<size;i++){
    		cardId+=tmp.substring(len-2, len);
    		len=len-2;
    	}
		return cardId;
    }
    
   

}
