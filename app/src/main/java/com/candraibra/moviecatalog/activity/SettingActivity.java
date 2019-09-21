package com.candraibra.moviecatalog.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.notif.DailyReceiver;
import com.candraibra.moviecatalog.notif.ReleaseReceiver;
import com.candraibra.moviecatalog.notif.preference;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    DailyReceiver dailyReceiver;
    ReleaseReceiver releaseReciver;
    preference notificationPreference;
    SharedPreferences spReleaseReminder, spDailyReminder;
    SharedPreferences.Editor edtReleaseReminder, edtDailyReminder;

    String TYPE_DAILY = "reminderDaily";
    String TYPE_RELEASE = "reminderRelease";
    String DAILY_REMINDER = "dailyReminder";
    String RELEASE_REMINDER = "releaseReminder";
    String KEY_RELEASE = "Release";
    String KEY_DAILY_REMINDER = "Daily";

    String timeDaily = "07:00";
    String timeRelease = "08:00";
    private SwitchCompat swDaily;
    private SwitchCompat swRealise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        dailyReceiver = new DailyReceiver();
        releaseReciver = new ReleaseReceiver();
        notificationPreference = new preference(this);
        TextView back = findViewById(R.id.backButton);
        back.setOnClickListener(this);
        TextView language = findViewById(R.id.tv_language);
        language.setOnClickListener(this);
        swDaily = findViewById(R.id.sw_daily);
        swRealise = findViewById(R.id.sw_realease);
        setPreference();
        setDaily();
        setRelease();
    }

    private void releaseOn() {
        String message = getResources().getString(R.string.realise_message);
        notificationPreference.setTimeRelease(timeRelease);
        notificationPreference.setReleaseMessage(message);
        releaseReciver.setAlarm(SettingActivity.this, TYPE_RELEASE, timeRelease, message);
    }

    private void releaseOff() {
        releaseReciver.cancelNotification(SettingActivity.this);
    }

    private void dailyOn() {
        String message = getResources().getString(R.string.daily_reminder);
        notificationPreference.setTimeDaily(timeDaily);
        notificationPreference.setDailyMessage(message);
        dailyReceiver.setAlarm(SettingActivity.this, TYPE_DAILY, timeDaily, message);
    }

    private void dailyOff() {
        dailyReceiver.cancelNotif(SettingActivity.this);
    }

    private void setPreference() {
        spDailyReminder = getSharedPreferences(DAILY_REMINDER, MODE_PRIVATE);
        boolean checkDailyReminder = spDailyReminder.getBoolean(KEY_DAILY_REMINDER, false);
        swDaily.setChecked(checkDailyReminder);
        spReleaseReminder = getSharedPreferences(RELEASE_REMINDER, MODE_PRIVATE);
        boolean checkUpcomingReminder = spReleaseReminder.getBoolean(KEY_RELEASE, false);
        swRealise.setChecked(checkUpcomingReminder);
    }

    public void setDaily() {
        edtDailyReminder = spDailyReminder.edit();
        swDaily.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                edtDailyReminder.putBoolean(KEY_DAILY_REMINDER, true);
                edtDailyReminder.apply();
                dailyOn();
            } else {
                edtDailyReminder.putBoolean(KEY_DAILY_REMINDER, false);
                edtDailyReminder.commit();
                dailyOff();
            }
        });
    }

    public void setRelease() {
        edtReleaseReminder = spReleaseReminder.edit();
        swRealise.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                edtReleaseReminder.putBoolean(KEY_RELEASE, true);
                edtReleaseReminder.apply();
                releaseOn();
            } else {
                edtReleaseReminder.putBoolean(KEY_RELEASE, false);
                edtReleaseReminder.commit();
                releaseOff();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            onBackPressed();
            {
                finish();
            }
        } else if (v.getId() == R.id.tv_language) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
    }
}
