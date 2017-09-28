package beyond_imagination.blubblub.pSetting;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * Created by cru65 on 2017-08-04.
 */
/**
 * @file CancelButton.java
 * @breif
 * Class custom view about button
 * Close Setting Activity and back to previous setting data
 * @author Yehun Park
 */
public class CancelButton extends android.support.v7.widget.AppCompatButton {
    /*** Variable ***/
    private SettingActivity settingActivity;

    /*** Function ***/
    public CancelButton(Context context) {
        super(context);
        settingActivity = (SettingActivity)context;
        init();
    }

    public CancelButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        settingActivity = (SettingActivity)context;
        init();
    }

    private void init() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                settingActivity.setResult(Activity.RESULT_CANCELED);
                settingActivity.finish();
            }
        });
    }
}
