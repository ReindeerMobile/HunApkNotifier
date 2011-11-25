package com.reindeermobile.hunapknotifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

public class HunApkService extends Service {
	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private NotificationManager mNotificationManager;
	private Notification notification;

	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			for (int i = 0; i < 5; i++) {
				// Normally we would do some work here, like download a file.
				// For our sample, we just sleep for 5 seconds.
				long endTime = System.currentTimeMillis() + 10 * 1000;
				while (System.currentTimeMillis() < endTime) {
					synchronized (this) {
						try {
							wait(endTime - System.currentTimeMillis());
						} catch (Exception e) {
						}
					}
				}
				
				mNotificationManager.notify(i, task("New notify!", "Id: " + i));
			}
			// Stop the service using the startId, so that we don't stop
			// the service in the middle of handling another job
			stopSelf(msg.arg1);
		}
	}

	@Override
	public void onCreate() {
		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block. We also make it
		// background priority so CPU-intensive work will not disrupt our UI.
		HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		// Get the HandlerThread's Looper and use it for our Handler
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);

		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) getSystemService(ns);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		mServiceHandler.sendMessage(msg);

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
	}

	private Notification task(String title, String message) {
		int icon = R.drawable.icon;
		CharSequence tickerText = "New Hun Apk!";
		long when = System.currentTimeMillis();
		
		Notification notification = new Notification(icon, tickerText, when);
		
		Context context = getApplicationContext();
		CharSequence contentTitle = title;
		CharSequence contentText = message;
		Intent notificationIntent = new Intent(this, HunApkNotifierActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		return notification;
	}
}
