package com.reindeermobile.hunapknotifier.network;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class AbstractClient {
	public static final String TAG = "AbstractClient";

	protected String url;
	protected DefaultHttpClient httpClient;

	public AbstractClient(String url) {
		super();
		this.url = url;
		this.httpClient = new DefaultHttpClient();
	}

	private HttpResponse send() throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(this.url);
		return this.httpClient.execute(get);
	}

	protected String getResult() throws ClientProtocolException, IOException {
		HttpEntity entity = this.send().getEntity();

		InputStream is = entity.getContent();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(is));

		StringBuilder sb = new StringBuilder();
		boolean flag = false;
		for (String line = ""; line != null; line = bufferedReader.readLine()) {
			if (flag) {
				Log.d(TAG, "getResult - line: [" + line + "]");
				sb = sb.append(line).append('\n');
			} else {
				flag = true;
			}
		}

		return sb.toString();
	}

}
