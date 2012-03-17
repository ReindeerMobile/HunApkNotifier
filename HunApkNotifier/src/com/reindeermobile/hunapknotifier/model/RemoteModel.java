package com.reindeermobile.hunapknotifier.model;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.reindeermobile.hunapknotifier.entities.HunApkInfo;
import com.reindeermobile.reindeerutils.mvp.IModel;
import com.reindeermobile.reindeerutils.mvp.ListWrapper;
import com.reindeermobile.reindeerutils.mvp.MessageObject;
import com.reindeermobile.reindeerutils.mvp.Presenter;
import com.reindeermobile.reindeerutils.mvp.Presenter.ModelService;

import android.content.Context;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RemoteModel implements IModel {
	public static final String TAG = "RemoteModel";

	public static final String URL = "http://rootrulez.uw.hu/down.hunapk";

	@ModelService
	public static final int GET_HUN_APK_LIST = 200;
	public static final int SEND_HUN_APK_LIST = 201;

	private AsyncHttpClient httpClient;

	@Override
	public boolean handleMessage(Message msg) {
		Callback sender = null;
		MessageObject messageObject = null;
		if (msg.obj != null && msg.obj instanceof MessageObject) {
			messageObject = (MessageObject) msg.obj;
			sender = messageObject.getSenderView();
		}
		switch (msg.what) {
		case GET_HUN_APK_LIST:
			Log.d(TAG, "handleMessage - GET_HUN_APK_LIST");
			httpClient.get(URL, new DownloadApkListHandler(sender));
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void init(Context context) {
		this.httpClient = new AsyncHttpClient();
	}

	private class DownloadApkListHandler extends AsyncHttpResponseHandler {
		private Callback sender;

		public DownloadApkListHandler(Callback sender) {
			super();
			this.sender = sender;
		}

		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);
			Log.d(TAG, "onSuccess - START");
			List<HunApkInfo> apkInfos = parseResponseStringToList(content);
			Collections.sort(apkInfos);

			Presenter.getInst().sendViewMessage(
					SEND_HUN_APK_LIST,
					new MessageObject(this.sender, new ListWrapper<HunApkInfo>(
							apkInfos)));
			Log.d(TAG, "onSuccess - END");
		}
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

}
