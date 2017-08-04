package beyond_imagination.blubblub.pSetting;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import beyond_imagination.blubblub.R;

/**
 * Created by cru65 on 2017-08-04.
 */

public class AutoRadioGroup extends RadioGroup {
    /*** Variable ***/
    SettingActivity settingActivity;

    /*** Function ***/
    public AutoRadioGroup(Context context) {
        super(context);
        settingActivity = (SettingActivity)context;
        init();
    }

    public AutoRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        settingActivity = (SettingActivity)context;
        init();
    }

    private void init() {
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.Auto) {
                    settingActivity.getSetting().setAuto(true);
                }
                else
                {
                    settingActivity.getSetting().setAuto(false);
                }
            }
        });
    }

    void setAuto(boolean isAuto) {
        if(isAuto)
            check(R.id.Auto);
        else
            check(R.id.Passive);
    }
}
