package beyond_imagination.blubblub.pChatting;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import beyond_imagination.blubblub.MainActivity;

/**
 * Created by cru65 on 2017-07-27.
 */

public class ChattingLayout extends LinearLayout{
    MainActivity mainActivity;

    public ChattingLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mainActivity = (MainActivity)context;

        init();
    }

    private void init() {

    }
}
