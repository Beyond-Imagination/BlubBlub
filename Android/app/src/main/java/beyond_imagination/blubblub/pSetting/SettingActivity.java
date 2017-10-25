package beyond_imagination.blubblub.pSetting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import beyond_imagination.blubblub.R;
import beyond_imagination.blubblub.Setting;

/**
 * Created by cru65 on 2017-08-03.
 */

/**
 * @author Yehun Park
 * @file SettingActivity.java
 * @breif Class include all of things about setting
 * You can modify your setting values.
 * If you change your setting values, send your setting data to chatbot server.
 */
public class SettingActivity extends AppCompatActivity {
    /****************/
    /*** Variable ***/
    /****************/
    private Setting setting;
    private AutoRadioGroup autoRadioGroup;
    private FeedCycleSpinner feedCycleSpinner;
    private MaxTempSpinner maxTempSpinner;
    private MinTempSpinner minTempSpinner;
    private MaxIlluminationSpinner maxilluminationSpinner;
    private MinIlluminationSpinner minilluminationSpinner;
    private SaveButton saveButton;
    private CancelButton cancelButton;

    /****************/
    /*** Function ***/
    /****************/
    public SettingActivity() {

    }

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        autoRadioGroup = (AutoRadioGroup) findViewById(R.id.autoradiogroup);
        feedCycleSpinner = (FeedCycleSpinner) findViewById(R.id.feedcyclespinner);
        maxTempSpinner = (MaxTempSpinner) findViewById(R.id.maxtempspinner);
        minTempSpinner = (MinTempSpinner) findViewById(R.id.mintempspinner);
        maxilluminationSpinner = (MaxIlluminationSpinner) findViewById(R.id.maxillumspinner);
        minilluminationSpinner = (MinIlluminationSpinner) findViewById(R.id.minillumspinner);
        saveButton = (SaveButton) findViewById(R.id.savebtn);
        cancelButton = (CancelButton) findViewById(R.id.cancelBtn);

        Intent intent = getIntent();
        setting = intent.getParcelableExtra("setting");

        init();
    }

    private void init() {
        autoRadioGroup.setAuto(setting.getAuto());
        feedCycleSpinner.setFeedCycle(setting.getFeed_cycle());
        maxTempSpinner.setMaxTemp(setting.getTmp_max());
        minTempSpinner.setMinTemp(setting.getTmp_min());
        maxilluminationSpinner.setLux(setting.getIllum_max());
        minilluminationSpinner.setLux(setting.getIllum_min());

        Log.d("SettingActivity", "init()-success");
    }

    ////
    // Getter and Setter
    ////
    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }
}
