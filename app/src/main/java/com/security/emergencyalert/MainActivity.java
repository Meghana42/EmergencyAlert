package com.security.emergencyalert;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static final String PREFERENCE = "Phone_pref";
    static final String NUMBER = "number";

    Button mCall;
    TextView mPermissionText;
    EditText mEdtText;
    SharedPreferences mSharedpref;
    String mNumber;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCall = findViewById(R.id.call);
        mEdtText = findViewById(R.id.numberId);

        mSharedpref = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        mContext = getApplicationContext();

        checkPermissions();

        mCall.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                mNumber = mEdtText.getText().toString();
                if (!mNumber.isEmpty()) {
                    mSharedpref.edit().putString(NUMBER, mNumber).apply();
                } else {
                    return;
                }

                Uri alarmType = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                MediaPlayer mp = MediaPlayer.create(mContext, alarmType);
                mp.start();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mp.stop();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel: " + mNumber));
                startActivity(callIntent);
            }
        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, 1);
        }
    }
}
