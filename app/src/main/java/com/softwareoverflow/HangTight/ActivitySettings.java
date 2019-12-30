package com.softwareoverflow.HangTight;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.softwareoverflow.HangTight.helper.SharedPreferenceHelper;

public class ActivitySettings extends AppCompatActivity {

    private SwitchCompat toggleVibrate, toggleWarmUp;
    private Spinner soundSpinner, backgroundSpinner;
    private SeekBar countdownSeekBar;
    private TextView countdownTextView;

    private int soundIndex; // This is required to prevent onItemSelected firing on initialization

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        getSupportActionBar().hide();

        soundSpinner = findViewById(R.id.settings_soundSpinner);
        toggleVibrate = findViewById(R.id.settings_vibrateToggle);
        toggleWarmUp = findViewById(R.id.settings_warmUpWarningToggle);
        backgroundSpinner = findViewById(R.id.settings_backgroundSpinner);
        countdownSeekBar = findViewById(R.id.settings_countdown);
        countdownTextView = findViewById(R.id.settings_countdown_tv);

        loadCurrentSettings();
    }

    private void loadCurrentSettings() {
        /* TODO - change all usages of hard coded string values for SP to string resource to prevent
            spelling errors etc */
        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);

        boolean isVibrateOn = settings.getBoolean("vibrate", false);
        toggleVibrate.setChecked(isVibrateOn);

        boolean showWarmUpWarning = settings.getBoolean("showWarmUp", true);
        toggleWarmUp.setChecked(showWarmUpWarning);

        int backgroundImage = settings.getInt("backgroundImage", 1);
        backgroundSpinner.setSelection(backgroundImage, true);

        soundIndex = SharedPreferenceHelper.getSavedSoundIndex(this, settings);
        soundSpinner.setSelection(soundIndex);

        int countdown = settings.getInt("timer", 5);
        countdownSeekBar.setProgress(countdown);
        countdownTextView.setText(String.valueOf(countdown));
    }

    @Override
    protected void onStart() {
        super.onStart();

        soundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == soundIndex)
                    return;

                String rawFile = parent.getItemAtPosition(position).toString().toLowerCase();
                if(rawFile.contains("none"))
                    return;

                int soundId = SharedPreferenceHelper.getSoundId(rawFile);
                MediaPlayer.create(ActivitySettings.this, soundId).start();

                soundIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        countdownSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                countdownTextView.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void saveSettings(View v){
        SharedPreferences settings = getApplication().getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("backgroundImage", backgroundSpinner.getSelectedItem().equals("Female") ? 1 : 0);
        editor.putBoolean("vibrate", toggleVibrate.isChecked());
        editor.putInt("timer", countdownSeekBar.getProgress());
        editor.putString("soundName", soundSpinner.getSelectedItem().toString());
        editor.putBoolean("showWarmUp", toggleWarmUp.isChecked());
        editor.apply();

        // TODO - startActivityForResult from MainActivity and show Snackbar on return if changes made
        finish();
    }

    public void cancel(View v){
        finish();
    }
}