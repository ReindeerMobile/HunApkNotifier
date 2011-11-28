package com.reindeermobile.hunapknotifier.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

import com.reindeermobile.hunapknotifier.entities.HunApkInfo;

public class HunApkConnection {
	private static final int HTTP_TIMEOUT = 3600;
	private static final String url = "http://rootrulez.uw.hu/down.hunapk";

	private DefaultHttpClient httpClient;

	public HunApkConnection() {
		super();
		this.httpClient = new DefaultHttpClient();
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);

		if (this.httpClient != null) {
			Log.d(getClass().getName(), "init http client: success");
		}
	}

	private HttpResponse send() throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url);
		HttpResponse response = null;
		try {
			response = this.httpClient.execute(httpPost);
		} catch (IOException exception) {
			Log.w("network", exception.getMessage(), exception);
		}
		return response;
	}

	public List<HunApkInfo> getList() throws IllegalStateException, IOException, ParseException {
		HttpEntity entity = send().getEntity();
		InputStream is = entity.getContent();

		BufferedReader bufferedReader = null;
		String line = null;

		SimpleDateFormat curFormater = new SimpleDateFormat("yy-MM-dd");
		int maxRow = 0;
		int columnIndex = 0;
		int rowIndex = 0;
		int count = 0;
		List<HunApkInfo> apkInfoList = new ArrayList<HunApkInfo>();
		bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8")); // ISO-8859-1
		try {
			while ((line = bufferedReader.readLine()) != null) {
				HunApkInfo hunApkInfo = new HunApkInfo();
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
						apkInfoList.get(rowIndex).setDate(curFormater.parse(line));
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
			}
		} catch (NumberFormatException exception) {
			Log.e("network", exception.getMessage(), exception);
		}
		bufferedReader.close();
		return apkInfoList;
	}
}
