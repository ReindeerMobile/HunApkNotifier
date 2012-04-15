package com.reindeermobile.hunapknotifier.model;

import com.reindeermobile.hunapknotifier.entities.HunApkInfo;
import com.reindeermobile.hunapknotifier.services.HunApkService;
import com.reindeermobile.reindeerutils.mvp.AbstractController;
import com.reindeermobile.reindeerutils.mvp.ListWrapper;
import com.reindeermobile.reindeerutils.mvp.MessageObject;
import com.reindeermobile.reindeerutils.mvp.Presenter;
import com.reindeermobile.reindeerutils.mvp.Presenter.ControllerServices;
import com.reindeermobile.reindeerutils.service.SimpleReceiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.util.Log;

import java.util.ArrayList;

public class RemoteController extends AbstractController implements
		SimpleReceiver.Receiver {
	public static final String TAG = "RemoteController";

	public static final String URL = "http://rootrulez.uw.hu/down.hunapk";

	@ControllerServices
	public static final String[] SERVICES = new String[] { "GET_HUN_APK_LIST",
			"SEND_HUN_APK_LIST" };

	public SimpleReceiver receiver;
	private Context context;

	@Override
	public void init(Context context) {
		this.context = context;
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch (resultCode) {
		case HunApkService.STATUS_RUNNING:
			Log.d(TAG, "onReceiveResult - RUNNING");
			break;
		case HunApkService.STATUS_UPDATE:
			Log.d(TAG, "onReceiveResult - STATUS_UPDATE");
			final ArrayList<HunApkInfo> hunApkList = resultData
					.getParcelableArrayList(HunApkService.FETCH_HUN_APK_LIST);
			if (hunApkList != null) {
				Log.d(TAG,
						"onReceiveResult - hunApkList.size: "
								+ hunApkList.size());
				Presenter.getInst().sendViewMessage(
						"SEND_HUN_APK_LIST",
						new MessageObject(this, new ListWrapper<HunApkInfo>(
								hunApkList)));
			}
			break;
		case HunApkService.STATUS_ERROR:
			Log.d(TAG, "onReceiveResult - ERROR");
			break;
		case HunApkService.STATUS_FINISHED:
			Log.d(TAG, "onReceiveResult - STATUS_FINISHED");
			break;
		default:
			break;
		}
	}

	private void startRestService() {
		// Init the receiver.
		Log.d(TAG, "startRestService - START");

		this.receiver = new SimpleReceiver(new Handler());
		this.receiver.setReceiver(this);

		// Start RestService.
		final Intent restServiceIntent = new Intent(Intent.ACTION_SYNC, null,
				context, HunApkService.class);
		restServiceIntent.putExtra("receiver", this.receiver);
		restServiceIntent.putExtra("command",
				HunApkService.SERVICE_COMMAND_UPDATE);
		context.startService(restServiceIntent);
	}

	@Override
	protected void initTasks() {
		super.registerTask("GET_HUN_APK_LIST", new ContollerTask() {
			@Override
			public void execute(Callback sender, MessageObject messageObject) {
				RemoteController.this.startRestService();
			}
		});
	}

}