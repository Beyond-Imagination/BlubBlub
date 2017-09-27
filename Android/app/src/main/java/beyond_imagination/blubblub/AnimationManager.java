package beyond_imagination.blubblub;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by cru65 on 2017-07-27.
 */

public class AnimationManager {
    /*** Variable ***/
    MainActivity mainActivity;

    Animation chattinglayout_open;
    Animation chattinglayout_close;
    Animation.AnimationListener animationListener;


    /*** Function ***/
    public AnimationManager(Context context) {
        mainActivity = (MainActivity)context;

        init();
    }

    private void init() {
        // 리스너 등록
        animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        // 애니메이션 등록
        registerAnimation();
    }

    private void registerAnimation()
    {
        chattinglayout_open = AnimationUtils.loadAnimation(mainActivity,R.anim.anim_chattinglayout_open);
        chattinglayout_open.setAnimationListener(animationListener);

        chattinglayout_close = AnimationUtils.loadAnimation(mainActivity,R.anim.anim_chattinglayout_close);
        chattinglayout_close.setAnimationListener(animationListener);
    }

    ////
    // Getter, Setter
    ////
    public Animation getChattinglayout_open() {
        return chattinglayout_open;
    }

    public void setChattinglayout_open(Animation chattinglayout_open) {
        this.chattinglayout_open = chattinglayout_open;
    }

    public Animation getChattinglayout_close() {
        return chattinglayout_close;
    }

    public void setChattinglayout_close(Animation chattinglayout_close) {
        this.chattinglayout_close = chattinglayout_close;
    }
}
