package beyond_imagination.blubblub.pSetting;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import beyond_imagination.blubblub.MainActivity;

/**
 * Created by cru65 on 2017-08-03.
 */

public class SettingButton extends android.support.v7.widget.AppCompatButton{
    /*** Variable ***/
    MainActivity mainActivity;
    AttributeSet attrs;

    // Code vaule
    private final static int SETTING_CALL = 1000;

    /*** Function ***/
    public SettingButton(Context context) {
        super(context);
        mainActivity = (MainActivity)context;
        init();
    }

    public SettingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mainActivity = (MainActivity)context;
        this.attrs = attrs;
        init();
    }

    public SettingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mainActivity = (MainActivity)context;
        this.attrs = attrs;
        init();
    }

    private void init() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, SettingActivity.class);
                intent.putExtra("setting", mainActivity.getSetting());

                mainActivity.startActivityForResult(intent, SETTING_CALL);
            }
        });
    }
}
