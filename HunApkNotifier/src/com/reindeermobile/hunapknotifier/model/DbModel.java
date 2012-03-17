package com.reindeermobile.hunapknotifier.model;

import com.reindeermobile.hunapknotifier.entities.HunApkInfo;
import com.reindeermobile.reindeerutils.db.DbAdapterFactory;
import com.reindeermobile.reindeerutils.db.IDatabaseAdapter;
import com.reindeermobile.reindeerutils.mvp.IModel;
import com.reindeermobile.reindeerutils.mvp.ListWrapper;
import com.reindeermobile.reindeerutils.mvp.MessageObject;
import com.reindeermobile.reindeerutils.mvp.Presenter;
import com.reindeermobile.reindeerutils.mvp.Presenter.ModelService;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;

import java.util.List;

public class DbModel implements IModel {
	public static final String TAG = "DbModel";

	public static final String DATABASE_NAME = "hun_apk_db";
	public static final int DATABASE_VERSION = 1;

	private IDatabaseAdapter<HunApkInfo> apkInfoAdapter;

	@ModelService
	public static final int GET_HUN_APK_INFO_LIST = 100;
	public static final int SEND_HUN_APK_INFO_LIST = 101;

	@Override
	public boolean handleMessage(Message msg) {
		Callback sender = null;
		MessageObject messageObject = null;
		if (msg.obj != null && msg.obj instanceof MessageObject) {
			messageObject = (MessageObject) msg.obj;
			sender = messageObject.getSenderView();
		}
		switch (msg.what) {
		case GET_HUN_APK_INFO_LIST:
			Log.d(TAG, "handleMessage - GET_HUN_APK_INFO_LIST");
			List<HunApkInfo> hunApkInfoList = this.apkInfoAdapter.list();
			Presenter.getInst().sendViewMessage(
					SEND_HUN_APK_INFO_LIST,
					new MessageObject(sender, new ListWrapper<HunApkInfo>(
							hunApkInfoList)));
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void init(Context context) {
		DbAdapterFactory.INSTANCE.init(HunApkInfo.class);

		this.apkInfoAdapter = DbAdapterFactory.createInstance(HunApkInfo.class,
				context, DATABASE_NAME, DATABASE_VERSION);
	}

}
