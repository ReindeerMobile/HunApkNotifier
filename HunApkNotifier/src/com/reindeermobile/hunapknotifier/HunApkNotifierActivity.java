package com.reindeermobile.hunapknotifier;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.reindeermobile.hunapknotifier.entities.HunApkInfo;
import com.reindeermobile.hunapknotifier.network.HunApkConnection;
import com.reindeermobile.hunapknotifier.view.adapters.HunApkListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HunApkNotifierActivity extends Activity implements OnClickListener {
	public static final String TAG = "HunApkNotifierActivity";
	// private Intent intent = null;
	// private Button startNotifyButton;
	// private Button stopNotifyButton;
	private Button exitButton;
	private Button refreshButton;
	private Button hideButton;
	private ListView hunapkListView;
	private ProgressBar progressBar;

	private AsyncHttpResponseHandler downloadApkListHandler;
	private AsyncHttpClient asyncHttpClient;

	// public ArrayList<HunApkInfo> hunApkInfoList;

	@Override
	public void onClick(View v) {
		if (v != null) {
			switch (v.getId()) {
			case R.id.buttonExit:
				this.finish();
				break;
			case R.id.buttonRefresh:
				this.showProgressBar();
				this.asyncHttpClient.get(HunApkConnection.URL,
						downloadApkListHandler);
				break;
			case R.id.buttonHide:
				break;
			// case R.id.startNotifyButton:
			// if (intent == null) {
			// intent = new Intent(HunApkNotifierActivity.this,
			// HunApkService.class);
			// }
			// startService(intent);
			// break;
			// case R.id.stopNotifyButton:
			// if (intent != null) {
			// stopService(intent);
			// }
			// break;
			default:
				break;
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate - START");
		setContentView(R.layout.activity_app_list);

		this.asyncHttpClient = new AsyncHttpClient();
		this.downloadApkListHandler = new DownloadApkListHandler();

		this.exitButton = (Button) findViewById(R.id.buttonExit);
		this.refreshButton = (Button) findViewById(R.id.buttonRefresh);
		this.hideButton = (Button) findViewById(R.id.buttonHide);
		this.hunapkListView = (ListView) findViewById(R.id.listViewAppList);
		this.progressBar = (ProgressBar) findViewById(R.id.progressBar);

		this.exitButton.setOnClickListener(this);
		this.refreshButton.setOnClickListener(this);
		this.hideButton.setOnClickListener(this);

		this.hideProgressBar();
		// startNotifyButton = (Button) findViewById(R.id.startNotifyButton);
		// stopNotifyButton = (Button) findViewById(R.id.stopNotifyButton);
		// startNotifyButton.setOnClickListener(this);
		// stopNotifyButton.setOnClickListener(this);
		Log.d(TAG, "onCreate - END");
	}

	private void showProgressBar() {
		Log.d(TAG, "showProgressBar - START");
		this.progressBar.setIndeterminate(true);
		this.progressBar.setVisibility(View.VISIBLE);
		this.refreshButton.setEnabled(false);
		Log.d(TAG, "showProgressBar - END");
	}

	private void hideProgressBar() {
		Log.d(TAG, "hideProgressBar - START");
		this.progressBar.setVisibility(View.INVISIBLE);
		this.refreshButton.setEnabled(true);
		Log.d(TAG, "hideProgressBar - END");
	}

	private void updateUI(List<HunApkInfo> list) {
		Log.d(TAG, "updateUI - START");
		if (list != null && !list.isEmpty()) {
			this.hunapkListView.setAdapter(new HunApkListAdapter(this,
					R.id.listViewAppList, list));
		}
		this.hideProgressBar();
		Log.d(TAG, "updateUI - END");
	}

	private static List<HunApkInfo> parseResponseStringToList(String content) {
		Log.d(TAG, "parseResponseStringToList - START");
		SimpleDateFormat curFormater = new SimpleDateFormat("yy-MM-dd");
		List<HunApkInfo> apkInfoList = new ArrayList<HunApkInfo>();
		int maxRow = 0;
		int columnIndex = 0;
		int rowIndex = 0;
		int count = 0;

		int contentLength = content.length();

		int position = 0;
		int endOfLineIndex = content.indexOf('\n', position);

		String line = "";
		try {
			while (endOfLineIndex > -1 && endOfLineIndex - 1 < contentLength) {
				line = content.substring(position, endOfLineIndex - 1);
				HunApkInfo hunApkInfo = new HunApkInfo();
				Log.d(TAG, "parseResponseStringToList - line[" + position + ","
						+ endOfLineIndex + "] [" + line + "]");
				Log.d(TAG, "parseResponseStringToList - column: " + columnIndex);
				Log.d(TAG, "parseResponseStringToList - row: " + rowIndex);
				if (count == 0) {
				} else if (count == 1) {
					maxRow = Integer.parseInt(line);
				} else {
					if (columnIndex == 0) {
						hunApkInfo.setId(Integer.parseInt(line));
						apkInfoList.add(hunApkInfo);
					} else if (columnIndex == 1) {
						apkInfoList.get(rowIndex).setName(line);
					} else if (columnIndex == 2) {
						try {
							apkInfoList.get(rowIndex).setDate(
									curFormater.parse(line));
						} catch (Exception exception) {
							apkInfoList.get(rowIndex).setDate(null);
						}
					} else if (columnIndex == 3) {
						apkInfoList.get(rowIndex).setLink(line);
					} else if (columnIndex == 4) {
						apkInfoList.get(rowIndex).setAuthor(line);
					}
					if (rowIndex < maxRow - 1) {
						rowIndex++;
					} else {
						columnIndex++;
						rowIndex = 0;
					}
				}
				count++;
				position = endOfLineIndex + 1;
				endOfLineIndex = content.indexOf('\n', position);
			}
		} catch (NumberFormatException exception) {
			Log.d(TAG, "parseResponseStringToList - NumberFormatException",
					exception);
		}

		Log.d(TAG, "parseResponseStringToList - END");
		return apkInfoList;
	}

	private class DownloadApkListHandler extends AsyncHttpResponseHandler {

		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);
			Log.d(TAG, "onSuccess - START");
			List<HunApkInfo> apkInfos = parseResponseStringToList(content);
			Collections.sort(apkInfos);
			updateUI(apkInfos);
			Log.d(TAG, "onSuccess - END");
		}

	}

}
