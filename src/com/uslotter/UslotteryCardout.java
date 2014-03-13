package com.uslotter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.media.ExifInterface;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
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
import com.uslotter.nfc.Converter;
import com.uslotter.nfc.MifareBlock;
import com.uslotter.nfc.MifareClassCard;
import com.uslotter.nfc.MifareSector;
import com.uslotter.util.HttpUtil;

public class UslotteryCardout extends Activity implements OnClickListener {
	Button btBack = null;
	String cardId = null;
	EditText edWdTitle = null;
	RadioGroup mRadioGroup = null;
	RadioButton rb1 = null;
	RadioButton rb2 = null;
	RadioButton rb3 = null;
	Button btCommite = null;
	Button btCardError=null;
	TextView tv=null;
	int state = 0;// 没有刷IC卡原因
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			ExifInterface _f = new ExifInterface("");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cardout);
		btBack = (Button) findViewById(R.id.qtback);
		btBack.setOnClickListener(this);
		btCardError=(Button) findViewById(R.id.carderror_out);
		btCardError.setOnClickListener(this);
		mRadioGroup = (RadioGroup) findViewById(R.id.rp_out);
		edWdTitle = (EditText) findViewById(R.id.ettitle_out);
		rb1 = (RadioButton) findViewById(R.id.RadioButton1_out);
		rb2 = (RadioButton) findViewById(R.id.RadioButton2_out);
		rb3 = (RadioButton) findViewById(R.id.RadioButton3_out);
		btCommite = (Button) findViewById(R.id.commite_out);
		tv=(TextView) findViewById(R.id.txtitle_out);
		
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
				// TODO Auto-generated method stub
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
		TextView data = (TextView) findViewById(R.id.outmessage);
		if (mAdapter == null) {

			data.setText("不支持刷网点IC卡方式");
		}
		if (msg != null && !msg.equals("")) {
			// Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
			data.setText(msg);
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

		TextView message = (TextView) findViewById(R.id.outmessage);
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

	public void goJkConfirmActivity(String cardId) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.JkConfirm");
		intent.putExtra("cardId", cardId);
		intent.putExtra("type", 1);
		intent.putExtra("value", 2);
		startActivity(intent);
		finish();
	}
	
	public void goJkConfirmActivity1(String wdTitle, int error) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.JkConfirm");
		intent.putExtra("wdTitle", wdTitle);
		intent.putExtra("state", state);
		intent.putExtra("type", 2);
		intent.putExtra("value", 2);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.qtback:
			// 返回
			Intent intent = new Intent(UslotteryCardout.this,UslotteryMainMenu2.class);
			startActivity(intent);
			finish();
			break;
			
		case R.id.carderror_out:
			tv.setVisibility(View.VISIBLE);
			mRadioGroup.setVisibility(View.VISIBLE);
			edWdTitle.setVisibility(View.VISIBLE);
			rb1.setVisibility(View.VISIBLE);
			rb2.setVisibility(View.VISIBLE);
			rb3.setVisibility(View.VISIBLE);
			btCommite.setVisibility(View.VISIBLE);
			break;
			
		case R.id.commite_out:

			String WdTitle = edWdTitle.getText().toString();
			if (!WdTitle.equals("") && WdTitle != null) {
				if (state != 0) {
					goJkConfirmActivity1(WdTitle, state);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UslotteryCardout.this);
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
						UslotteryCardout.this);
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
			Intent intent = new Intent(UslotteryCardout.this,UslotteryMainMenu2.class);
			startActivity(intent);
			finish();
			UslotteryCardout.this.overridePendingTransition(
					R.anim.push_right_in, R.anim.push_right_out);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}