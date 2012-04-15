package com.reindeermobile.hunapknotifier.model;

import com.reindeermobile.hunapknotifier.entities.HunApkInfo;
import com.reindeermobile.reindeerutils.db.DbAdapterFactory;
import com.reindeermobile.reindeerutils.db.IDatabaseAdapter;
import com.reindeermobile.reindeerutils.mvp.AbstractController;
import com.reindeermobile.reindeerutils.mvp.ListWrapper;
import com.reindeermobile.reindeerutils.mvp.MessageObject;
import com.reindeermobile.reindeerutils.mvp.Presenter;
import com.reindeermobile.reindeerutils.mvp.Presenter.ControllerServices;

import android.content.Context;
import android.os.Handler.Callback;
import android.util.Log;

import java.util.List;

public class DbController extends AbstractController {
	public static final String TAG = "DbController";

	public static final String DATABASE_NAME = "hun_apk_db";
	public static final int DATABASE_VERSION = 1;

	private IDatabaseAdapter<HunApkInfo> apkInfoAdapter;

	@ControllerServices
	public static final String[] SERVICES = new String[] {
			"GET_HUN_APK_INFO_LIST", "SEND_HUN_APK_INFO_LIST",
			"SAVE_HUN_APK_INFO_LIST", "SAVE_OK_HUN_APK_INFO_LIST",
			"UPDATE_APK_INFO" };

	@Override
	public void init(Context context) {
		this.initTasks();
		
		DbAdapterFactory.INSTANCE.init(HunApkInfo.class);
		this.apkInfoAdapter = DbAdapterFactory.createInstance(HunApkInfo.class,
				context, DATABASE_NAME, DATABASE_VERSION);
	}

	@Override
	protected void initTasks() {
		super.registerTask("GET_HUN_APK_INFO_LIST", new ContollerTask() {
			@Override
			public void execute(Callback sender, MessageObject messageObject) {
				List<HunApkInfo> hunApkInfoList = apkInfoAdapter.list();
				Presenter.getInst().sendViewMessage(
						"SEND_HUN_APK_INFO_LIST",
						new MessageObject(sender, new ListWrapper<HunApkInfo>(
								hunApkInfoList)));
			}
		});

		super.registerTask("SAVE_HUN_APK_INFO_LIST", new ContollerTask() {
			@Override
			public void execute(Callback sender, MessageObject messageObject) {
				if (messageObject != null
						&& messageObject.hasData(ListWrapper.class)) {
					@SuppressWarnings("unchecked")
					List<HunApkInfo> hunApkInfos = ((ListWrapper<HunApkInfo>) messageObject
							.getData()).getList();
					for (HunApkInfo apkInfo : hunApkInfos) {
						Log.d(TAG, "handleMessage - save: " + apkInfo);
						apkInfoAdapter.insert(apkInfo);
					}
					Presenter.getInst().sendViewMessage(
							"SAVE_OK_HUN_APK_INFO_LIST",
							new MessageObject(sender));
				}
			}
		});

		super.registerTask("UPDATE_APK_INFO", new ContollerTask() {
			@Override
			public void execute(Callback sender, MessageObject messageObject) {
				if (messageObject != null
						&& messageObject.hasData(HunApkInfo.class)) {
					HunApkInfo apkInfo = (HunApkInfo) messageObject.getData();
					apkInfoAdapter.update(apkInfo);
				}
			}
		});
	}

}
