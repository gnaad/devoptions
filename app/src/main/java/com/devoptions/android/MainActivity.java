package com.devoptions.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button developerOptions, enableUsbDebugging, shareTheApp, rateTheApp;
    TextView statusOne, statusOneText, statusTwo, statusTwoText;
    ImageView statusOneCheck, statusTwoCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            getWindow().getDecorView().setSystemUiVisibility(0);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        statusOne = findViewById(R.id.status_one);
        statusOneText = findViewById(R.id.steps_one_textview);
        statusOneCheck = findViewById(R.id.step_one_check);

        statusTwo = findViewById(R.id.status_two);
        statusTwoText = findViewById(R.id.steps_two_textview);
        statusTwoCheck = findViewById(R.id.status_two_check);

        developerOptions = findViewById(R.id.open_developer_options);
        developerOptions.setOnClickListener(view -> {
            startActivity(new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS));
            Toast.makeText(MainActivity.this, "Click Build Number/MIUI Version options 7 times to enable Developer Options", Toast.LENGTH_LONG).show();
        });

        enableUsbDebugging = findViewById(R.id.enable_usb_debugging);
        enableUsbDebugging.setOnClickListener(view -> {
            startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
            Toast.makeText(MainActivity.this, "Toggle USB Debugging", Toast.LENGTH_LONG).show();
        });

        rateTheApp = findViewById(R.id.rate);
        rateTheApp.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.devoptions.android"));
            startActivity(intent);
        });

        shareTheApp = findViewById(R.id.share);
        shareTheApp.setOnClickListener(view -> {
            Intent intent52 = new Intent(Intent.ACTION_SEND);
            intent52.setType("text/plain");
            String shareBody2 = "About Dev Options App";
            String shareSub2 = "Hi there\n\nI found this new app called Dev Options. You can quickly enable Developer Options using this app. Give a try.\n\nDownload link : https://play.google.com/store/apps/details?id=com.devoptions.android\n\nThank You";
            intent52.putExtra(Intent.EXTRA_SUBJECT, shareBody2);
            intent52.putExtra(Intent.EXTRA_TEXT, shareSub2);
            startActivity(Intent.createChooser(intent52, "Share Dev Options using"));
        });

        update_dev_settings();
        update_usb_debugging();
    }

    @Override
    protected void onStart() {
        super.onStart();
        update_dev_settings();
        update_usb_debugging();
    }

    @Override
    protected void onStop() {
        super.onStop();
        update_dev_settings();
        update_usb_debugging();
    }

    @SuppressLint("SetTextI18n")
    private void update_dev_settings() {
        if (developer_settings()) {
            statusOne.setText("It's Enabled");
            statusOne.setTextColor(getResources().getColor(R.color.green));
            statusOneCheck.setImageResource(R.drawable.ic_enabled);
            statusOneText.setVisibility(View.GONE);
            developerOptions.setVisibility(View.GONE);
        } else {
            statusOne.setText("It's Disabled");
            statusOne.setTextColor(getResources().getColor(R.color.red));
            statusOneCheck.setImageResource(R.drawable.ic_disabled);
            statusOneText.setVisibility(View.VISIBLE);
            developerOptions.setVisibility(View.VISIBLE);
            statusOneText.setText(Html.fromHtml("<b>Steps to enable</b><br><br>On Android 4.1 and lower, the <b>Developer Options</b> screen is available by default.<br><br>On Android 4.2 and higher, user must enable this screen. To enable developer options, tap the <b>Build Number</b> option 7 times. You can find this option in one of the following locations, depending on your Android version:<br><br>• Android 9 (API level 28) and higher: <b>Settings > About Phone > Build Number</b><br><br>• Android 8.0.0 (API level 26) and Android 8.1.0 (API level 26): <b>Settings > System > About Phone > Build Number</b><br><br>• Android 7.1 (API level 25) and lower: <b>Settings > About Phone > Build Number</b>"));
        }
    }

    @SuppressLint("SetTextI18n")
    private void update_usb_debugging() {
        if (usb_debugging()) {
            statusTwo.setText("It's Enabled");
            statusTwo.setTextColor(getResources().getColor(R.color.green));
            statusTwoCheck.setImageResource(R.drawable.ic_enabled);
            enableUsbDebugging.setVisibility(View.GONE);
            statusTwoText.setVisibility(View.VISIBLE);
            statusTwoText.setText(Html.fromHtml("After this please enable these two options:<br><br>• <b>Install Via USB</b> which will allow installing apps via USB<br><br>• <b>USB Security (Security Settings)</b> which will allow granting permission and simulating input via USB Debugging"));
        } else {
            statusTwo.setText("It's Disabled");
            statusTwo.setTextColor(getResources().getColor(R.color.red));
            statusTwoCheck.setImageResource(R.drawable.ic_disabled);
            statusTwoText.setVisibility(View.VISIBLE);
            enableUsbDebugging.setVisibility(View.VISIBLE);
            statusTwoText.setText(Html.fromHtml("<b>Steps to enable</b><br><br>To enable <b>USB debugging</b>, toggle the USB debugging option in the Developer Options menu. You can find this option in one of the following locations, depending on your Android version:<br><br>• Android 9 (API level 28) and higher: Settings > <b>System > Advanced > Developer Options > USB debugging</b><br><br>• Android 8.0.0 (API level 26) and Android 8.1.0 (API level 26): <b>Settings > System > Developer Options > USB debugging</b><br><br>• Android 7.1 (API level 25) and lower: <b>Settings > Developer Options > USB debugging</b>"));
        }
    }

    private boolean usb_debugging() {
        return Settings.Secure.getInt(getContentResolver(), Settings.Secure.ADB_ENABLED, 0) == 1;
    }

    public boolean developer_settings() {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) == 16) {
            return Settings.Secure.getInt(getApplicationContext().getContentResolver(),
                    Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
        } else if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 17) {
            return Settings.Secure.getInt(getApplicationContext().getContentResolver(),
                    Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
        } else {
            return false;
        }
    }
}