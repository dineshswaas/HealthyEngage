package com.vidyo.vidyoconnector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int PERMISSIONS_REQUEST_ALL = 0x7c9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        enable(false);
        requestPermissions();
    }

    public void onCustomChoice(View view) {
        startActivity(new Intent(this, VideoConferenceActivity.class));
    }

    public void onSettings(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT > 22) {
            List<String> permissionsNeeded = new ArrayList<>();
            for (String permission : PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                    permissionsNeeded.add(permission);
            }

            if (permissionsNeeded.size() > 0) {
                ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[0]), PERMISSIONS_REQUEST_ALL);
            } else {
                enable(true);
            }
        } else {
            enable(true);
        }
    }

    private void enable(boolean enable) {
        findViewById(R.id.custom_layout).setEnabled(enable);
        findViewById(R.id.custom_layout).setAlpha(enable ? 1f : 0.2f);

        findViewById(R.id.settings).setEnabled(enable);
        findViewById(R.id.settings).setAlpha(enable ? 1f : 0.2f);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ALL) {
            requestPermissions();
        }
    }
}