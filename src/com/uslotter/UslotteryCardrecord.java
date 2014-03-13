package com.uslotter;

import java.io.IOException;
import java.util.ArrayList;

import com.cr.uslotter.R;
import com.uslotter.nfc.Converter;
import com.uslotter.nfc.MifareBlock;
import com.uslotter.nfc.MifareClassCard;
import com.uslotter.nfc.MifareSector;
import com.uslotter.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UslotteryCardrecord extends Activity{
	String msg = "";
	private Button btnok = null;
	private Button btnback = null;
	private EditText text = null;
	private TextView tv_content = null;
	PendingIntent pendingIntent = null;
	
	NfcAdapter mAdapter;
	IntentFilter[] mFilters;
	String[][] mTechLists;
	String cardId = null;

	private final int AUTH = 1;
	private final int EMPTY_BLOCK_0 = 2;
	private final int EMPTY_BLOCK_1 = 3;
	private final int NETWORK = 4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cardrecord);
		btnok = (Button) this.findViewById(R.id.cardrecord_btnok);
		btnback = (Button) this.findViewById(R.id.cardrecord_btnback);
		text = (EditText) this.findViewById(R.id.cardrecord_et);
		btnback.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.UslotteryMainMenu");
				startActivity(intent);
			}
		});
		btnok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String WdTitle = text.getText().toString();
				if (!WdTitle.equals("") && WdTitle != null) {
					
						goJkConfirmActivity1(WdTitle, 3);
										
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UslotteryCardrecord.this);
					builder.setTitle("提示").setCancelable(false);
					builder.setMessage("请输入网点编号");
					builder.setPositiveButton("确定",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
					builder.create().show();
				}
				
			}
		});
		msg = this.getIntent().getStringExtra("msg");
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
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
		TextView data = (TextView) findViewById(R.id.cardrecord_tw2);
		if (mAdapter == null) {
			tv_content.setText("不支持刷网点IC卡方式");
		}
		
		if (msg != null && !msg.equals("")) {
			tv_content.setText(msg);
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
					// Log.e("data", s);
					c += s + "\n";
				}

				doVibrator();
				if (HttpUtil.checkNet(this)) {
					goJkConfirmActivity(cardId);
				} else {
					Toast.makeText(this, "网络不能使用，请确认网络是否打开？",
							Toast.LENGTH_SHORT).show();
				}

			} catch (IOException e) {
				doVibrator();
				Log.e("tag", e.getLocalizedMessage());
				showAlert(3);
			} finally {
				if (mifareClassCard != null) {
					mifareClassCard.debugPrint();
				}
			}
		}// End of method
	}

	private void showAlert(int alertCase) {

		TextView message = (TextView) findViewById(R.id.cardrecord_tw2);
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
	
	private void doVibrator() {
		Vibrator v = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
		v.vibrate(200);
	}
	
	@Override	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  //按下键盘上返回按钮 //
	  if(keyCode == KeyEvent.KEYCODE_BACK){ 
		  Intent intent = new Intent();
			intent.setAction("android.intent.action.UslotteryMainMenu");
			startActivity(intent);
			finish();
			return true;
	 
	  }else{  
	   return super.onKeyDown(keyCode, event);	 
	  }	 
	 }

	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null)
			mAdapter.enableForegroundDispatch(this, pendingIntent, mFilters,
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
		intent.putExtra("cardId", cardId);
		intent.putExtra("type", 1);
		intent.putExtra("value", 1);
		startActivity(intent);
		finish();
	}

	public void goJkConfirmActivity1(String wdTitle, int error) {//网点号验证
		Intent intent = new Intent();
		intent.setAction("android.intent.action.JkConfirm");
		intent.putExtra("wdTitle", wdTitle);
		intent.putExtra("state", error);
		intent.putExtra("type", 2);
		intent.putExtra("value", 1);
		startActivity(intent);
		finish();
	}
}