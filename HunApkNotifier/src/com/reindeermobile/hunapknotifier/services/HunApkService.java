package com.reindeermobile.hunapknotifier.services;

import com.reindeermobile.hunapknotifier.entities.HunApkInfo;
import com.reindeermobile.hunapknotifier.model.RemoteController;
import com.reindeermobile.hunapknotifier.network.HunApkClient;

import org.apache.http.client.ClientProtocolException;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HunApkService extends IntentService {
	public static final String TAG = "HunApkService";

	public static final String SERVICE_COMMAND_UPDATE = "update";
	
	public static final String FETCH_HUN_APK_LIST = "FETCH_HUN_APK_LIST";

	public static final int STATUS_RUNNING = 1;
	public static final int STATUS_FINISHED = 2;
	public static final int STATUS_ERROR = -1;
	public static final int STATUS_UPDATE = 3;

	private static Timer timer = new Timer();

	private HunApkClient hunApkClient;

	public HunApkService() {
		super("HunApkService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate - START");
		this.hunApkClient = new HunApkClient(RemoteController.URL);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy - DONE");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ResultReceiver receiver = intent.getParcelableExtra("receiver");
		String command = intent.getStringExtra("command");
		if (command.equals(SERVICE_COMMAND_UPDATE)) {
			receiver.send(STATUS_RUNNING, Bundle.EMPTY);
			HunApkService.timer.scheduleAtFixedRate(new UpdateTask(receiver,
					this.hunApkClient), 0, 20000);
		}
	}

	private class UpdateTask extends TimerTask {
		private HunApkClient hunApkClient;
		private ResultReceiver receiver;

		public UpdateTask(ResultReceiver receiver, HunApkClient hunApkClient) {
			super();
			this.hunApkClient = hunApkClient;
			this.receiver = receiver;
		}

		@Override
		public void run() {
			Bundle bundle = new Bundle();
			ArrayList<HunApkInfo> list = new ArrayList<HunApkInfo>();
			try {
				list = this.hunApkClient.fetchHunApkList();
				Log.d(TAG, "run - list.size: " + list.size());
				bundle.putParcelableArrayList(FETCH_HUN_APK_LIST, list);
				receiver.send(STATUS_UPDATE, bundle);
			} catch (ClientProtocolException exception) {
				exception.printStackTrace();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}

}
