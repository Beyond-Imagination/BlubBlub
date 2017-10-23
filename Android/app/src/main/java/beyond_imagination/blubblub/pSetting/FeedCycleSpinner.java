package beyond_imagination.blubblub.pSetting;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by cru65 on 2017-08-04.
 */
/**
 * @file FeedCycleSpinner.java
 * @breif
 * Class custom view about Spinner
 * @author Yehun Park
 */
public class FeedCycleSpinner extends android.support.v7.widget.AppCompatSpinner {
    /****************/
    /*** Variable ***/
    /****************/
    private SettingActivity settingActivity;
    private ArrayAdapter<Integer> adapter;
    private Integer[] items;

    /****************/
    /*** Function ***/
    /****************/
    public FeedCycleSpinner(Context context) {
        super(context);
        settingActivity = (SettingActivity) context;
        init();
    }

    public FeedCycleSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        settingActivity = (SettingActivity) context;
        init();
    }

    private void init() {
        items = new Integer[7];

        for (int i = 0; i < 7; i++) {
            items[i] = i+6;
        }

        adapter = new ArrayAdapter<Integer>(settingActivity, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setAdapter(adapter);
        setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settingActivity.getSetting().setFeed_cycle(items[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Log.d("FeedCycleSpinner", "init()-success");
    }

    void setFeedCycle(int feedCycle) {
        setSelection(feedCycle - 6);
    }
}
