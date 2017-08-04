package beyond_imagination.blubblub.pSetting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * Created by cru65 on 2017-08-03.
 */

public class SaveButton extends android.support.v7.widget.AppCompatButton {
    /*** Variable ***/
    private SettingActivity settingActivity;

    /*** Function ***/
    public SaveButton(Context context) {
        super(context);
        settingActivity = (SettingActivity)context;
        init();
    }

    public SaveButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        settingActivity = (SettingActivity)context;
        init();
    }

    public SaveButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        settingActivity = (SettingActivity)context;
        init();
    }

    private void init(){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("setting", settingActivity.getSetting());

                settingActivity.setResult(Activity.RESULT_OK, intent);
                settingActivity.finish();
            }
        });
    }
}
