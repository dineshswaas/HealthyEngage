package com.vidyo.vidyoconnector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.TextView;

import com.vidyo.vidyoconnector.connect.ConnectParams;
import com.vidyo.vidyoconnector.utils.AppUtils;
import com.vidyo.vidyoconnector.utils.Preferences;

public class SettingsActivity extends AppCompatActivity {

    private TextView ioCred;
    private TextView guestCred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ioCred = findViewById(R.id.io_api_credentials);
        ioCred.setText(patchIOCred());

        guestCred = findViewById(R.id.guest_api_credentials);
        guestCred.setText(patchGuestCred());

        SwitchCompat switchCompat = findViewById(R.id.enable_guest_join_api);

        boolean state = Preferences.get(Preferences.GUEST_API_ENABLED_KEY, false);
        switchCompat.setChecked(state);
        updateUI(state);

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Preferences.store(Preferences.GUEST_API_ENABLED_KEY, isChecked);

            updateUI(isChecked);
        });
    }

    private String patchGuestCred() {
        return String.format("Portal: %1s\nRoom key: %2s\nGuest Name: %3s\nPin: %4s",
                ConnectParams.PORTAL_HOST, ConnectParams.ROOM_KEY, ConnectParams.ROOM_DISPLAY_NAME, ConnectParams.ROOM_PIN);
    }

    private String patchIOCred() {
        return String.format("Host: %1s\nResource: %2s\nToken: %3s\nGuest Name: %4s",
                ConnectParams.HOST, ConnectParams.RESOURCE, AppUtils.formatToken(), ConnectParams.DISPLAY_NAME);
    }

    private void updateUI(boolean isChecked) {
        guestCred.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        ioCred.setVisibility(isChecked ? View.GONE : View.VISIBLE);
    }
}