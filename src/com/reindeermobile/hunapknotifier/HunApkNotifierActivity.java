
package com.reindeermobile.hunapknotifier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HunApkNotifierActivity extends Activity {

    private Intent intent = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button startNotifyButton = (Button) findViewById(R.id.startNotifyButton);
        startNotifyButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (intent == null) {
                    intent = new Intent(HunApkNotifierActivity.this, HunApkService.class);
                }
                startService(intent);
            }
        });

        Button stopNotifyButton = (Button) findViewById(R.id.stopNotifyButton);
        stopNotifyButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (intent != null) {
                    stopService(intent);
                }
            }
        });
    }

}
