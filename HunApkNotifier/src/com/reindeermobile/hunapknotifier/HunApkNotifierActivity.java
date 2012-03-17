package com.reindeermobile.hunapknotifier;

import com.reindeermobile.hunapknotifier.entities.HunApkInfo;
import com.reindeermobile.hunapknotifier.model.RemoteModel;
import com.reindeermobile.hunapknotifier.view.adapters.HunApkListAdapter;
import com.reindeermobile.reindeerutils.mvp.ListWrapper;
import com.reindeermobile.reindeerutils.mvp.MessageObject;
import com.reindeermobile.reindeerutils.mvp.Presenter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

public class HunApkNotifierActivity extends Activity implements
		OnClickListener, Callback {
	public static final String TAG = "HunApkNotifierActivity";
	// private Intent intent = null;
	// private Button startNotifyButton;
	// private Button stopNotifyButton;
	private Button exitButton;
	private Button refreshButton;
	private Button hideButton;
	private ListView hunapkListView;
	private ProgressBar progressBar;

	public List<HunApkInfo> hunApkInfoList;

	@Override
	public boolean handleMessage(Message msg) {
		MessageObject messageObject = null;
		if (msg.obj != null && msg.obj instanceof MessageObject) {
			messageObject = (MessageObject) msg.obj;
		}
		switch (msg.what) {
		case RemoteModel.SEND_HUN_APK_LIST:
			Log.d(TAG, "handleMessage - SEND_HUN_APK_LIST");
			if (messageObject != null
					&& messageObject.hasData(ListWrapper.class)) {
				@SuppressWarnings("unchecked")
				List<HunApkInfo> productList = ((ListWrapper<HunApkInfo>) messageObject
						.getData()).getList();
				this.hunApkInfoList = productList;
				this.updateUI(this.hunApkInfoList);
			}
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (v != null) {
			switch (v.getId()) {
			case R.id.buttonExit:
				this.finish();
				break;
			case R.id.buttonRefresh:
				this.showProgressBar();
				Presenter.getInst().sendModelMessage(
						RemoteModel.GET_HUN_APK_LIST, new MessageObject(this));
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

		this.exitButton = (Button) findViewById(R.id.buttonExit);
		this.refreshButton = (Button) findViewById(R.id.buttonRefresh);
		this.hideButton = (Button) findViewById(R.id.buttonHide);
		this.hunapkListView = (ListView) findViewById(R.id.listViewAppList);
		this.progressBar = (ProgressBar) findViewById(R.id.progressBar);

		this.exitButton.setOnClickListener(this);
		this.refreshButton.setOnClickListener(this);
		this.hideButton.setOnClickListener(this);

		this.hideProgressBar();
		
		Presenter.getInst().subscribe(this);
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

}
