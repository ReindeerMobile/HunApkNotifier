package com.reindeermobile.hunapknotifier;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class JumpToLinkActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String url = bundle.getString("APK_URL");
			if (url != null) {
				Intent apkUrlIntent = new Intent(Intent.ACTION_VIEW);
				apkUrlIntent.setData(Uri.parse(url));
				startActivity(apkUrlIntent);
				finish();
			}
		}
	}

}
