package com.reindeermobile.hunapknotifier.network;

import com.reindeermobile.hunapknotifier.entities.HunApkInfo;

import org.apache.http.client.ClientProtocolException;

import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HunApkClient extends AbstractClient {
	public static final String TAG = "HunApkClient";

	public HunApkClient(String url) {
		super(url);
	}

	public ArrayList<HunApkInfo> fetchHunApkList()
			throws ClientProtocolException, IOException {
		return this.parseResponseStringToList(super.getResult());
	}

	private ArrayList<HunApkInfo> parseResponseStringToList(String content) {
		Log.d(TAG, "parseResponseStringToList - START");
		Log.d(TAG, "parseResponseStringToList - content: " + content.substring(0, 30));
		SimpleDateFormat curFormater = new SimpleDateFormat("yy-MM-dd");
		ArrayList<HunApkInfo> apkInfoList = new ArrayList<HunApkInfo>();
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
				 Log.d(TAG, "parseResponseStringToList - line[" + position +
				 ","
				 + endOfLineIndex + "] [" + line + "]");
				 Log.d(TAG, "parseResponseStringToList - column: " +
				 columnIndex);
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
			Log.w(TAG, "parseResponseStringToList - NumberFormatException",
					exception);
		}

		Log.d(TAG, "parseResponseStringToList - END");
		return apkInfoList;
	}

}
