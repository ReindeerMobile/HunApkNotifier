package com.reindeermobile.hunapknotifier;

import com.reindeermobile.hunapknotifier.entities.HunApkInfo;
import com.reindeermobile.hunapknotifier.view.adapters.HunApkListAdapter;
import com.reindeermobile.reindeerutils.mvp.ListWrapper;
import com.reindeermobile.reindeerutils.mvp.MessageObject;
import com.reindeermobile.reindeerutils.mvp.Presenter;
import com.reindeermobile.reindeerutils.mvp.ViewHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

public class HunApkNotifierActivity extends Activity implements
		OnClickListener, OnItemClickListener {
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

	private ViewHandler viewHandler;

	@Override
	public void onClick(View v) {
		if (v != null) {
			switch (v.getId()) {
			case R.id.buttonExit:
				this.finish();
				break;
			case R.id.buttonRefresh:
				this.showProgressBar();
				Presenter.getInst().sendModelMessage("GET_HUN_APK_LIST",
						new MessageObject(this.viewHandler));
				break;
			case R.id.buttonHide:
				Intent intent = new Intent(HunApkNotifierActivity.this,
						HunApkService.class);
				startService(intent);
				break;
			// case R.id.startNotifyButton:
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HunApkInfo apkInfo = this.hunApkInfoList.get(position);
		apkInfo.setReaded(true);
		Intent jumpToLink = new Intent(this, JumpToLinkActivity.class);
		Presenter.getInst().sendModelMessage("UPDATE_APK_INFO",
				new MessageObject(this.viewHandler, apkInfo));
		jumpToLink.putExtra("APK_URL", apkInfo.getLink());
		this.startActivity(jumpToLink);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate - START");
		this.setContentView(R.layout.activity_app_list);

		this.exitButton = (Button) findViewById(R.id.buttonExit);
		this.refreshButton = (Button) findViewById(R.id.buttonRefresh);
		this.hideButton = (Button) findViewById(R.id.buttonHide);
		this.hunapkListView = (ListView) findViewById(R.id.listViewAppList);
		this.progressBar = (ProgressBar) findViewById(R.id.progressBar);

		this.exitButton.setOnClickListener(this);
		this.refreshButton.setOnClickListener(this);
		this.hideButton.setOnClickListener(this);

		// startNotifyButton = (Button) findViewById(R.id.startNotifyButton);
		// stopNotifyButton = (Button) findViewById(R.id.stopNotifyButton);
		// startNotifyButton.setOnClickListener(this);
		// stopNotifyButton.setOnClickListener(this);
		Log.d(TAG, "onCreate - END");
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.hideProgressBar();
		this.viewHandler = new ViewHandler(TAG);

		Presenter.getInst().subscribe(this.viewHandler);
		this.initTasks();
		Presenter.getInst().sendModelMessage("GET_HUN_APK_INFO_LIST",
				new MessageObject(this.viewHandler));
	}

	private void initTasks() {
		this.viewHandler.registerTask("SEND_HUN_APK_LIST",
				this.viewHandler.new ViewTask() {
					@Override
					public void execute(MessageObject messageObject) {
						if (messageObject != null
								&& messageObject.hasData(ListWrapper.class)) {
							@SuppressWarnings("unchecked")
							List<HunApkInfo> hunApkList = ((ListWrapper<HunApkInfo>) messageObject
									.getData()).getList();
							Log.d(TAG, "execute - hunApkList.size:"
									+ hunApkList);
							Presenter
									.getInst()
									.sendModelMessage(
											"SAVE_HUN_APK_INFO_LIST",
											new MessageObject(
													HunApkNotifierActivity.this.viewHandler,
													new ListWrapper<HunApkInfo>(
															hunApkList)));
						}
					}
				});
		this.viewHandler.registerTask("SAVE_OK_HUN_APK_INFO_LIST",
				this.viewHandler.new ViewTask() {
					@Override
					public void execute(MessageObject messageObject) {
						Presenter.getInst().sendModelMessage(
								"GET_HUN_APK_INFO_LIST",
								new MessageObject(HunApkNotifierActivity.this.viewHandler));
					}
				});
		this.viewHandler.registerTask("SEND_HUN_APK_INFO_LIST",
				this.viewHandler.new ViewTask() {
					@Override
					public void execute(MessageObject messageObject) {
						if (messageObject != null
								&& messageObject.hasData(ListWrapper.class)) {
							@SuppressWarnings("unchecked")
							List<HunApkInfo> loadedHunApkInfoList = ((ListWrapper<HunApkInfo>) messageObject
									.getData()).getList();
							hunApkInfoList = loadedHunApkInfoList;
							updateUI(hunApkInfoList);
						}
					}
				});
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
			this.hunapkListView.setOnItemClickListener(this);
		}
		this.hideProgressBar();
		Log.d(TAG, "updateUI - END");
	}

}
