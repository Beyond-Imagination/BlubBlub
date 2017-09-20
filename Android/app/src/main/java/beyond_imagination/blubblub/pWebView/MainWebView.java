package beyond_imagination.blubblub.pWebView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import beyond_imagination.blubblub.MainActivity;

/**
 * Created by cru65 on 2017-07-26.
 */

public class MainWebView extends WebView{

    /*** Variable ***/
    MainActivity mainActivity;

    /*** Function ***/
    public MainWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mainActivity = (MainActivity)context;

        init();
    }

    public void init() {
        setWebViewClient(new MyWebViewClient());

        // getSettings()에서 폰트크기, 확대여부, 스크립트 허용 여부 등의 설정 변경 가능.
        // 이미지 타입 설정.
        getSettings().setLoadsImagesAutomatically(true);
        // 자바 스크립트 허용 여부.
        getSettings().setJavaScriptEnabled(true);

        // 임시로 naver url로 설정.
        //loadUrl("http://www.naver.com");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mainActivity.onTouchEvent(event);
    }

    @Override
    public void goBack() {
        super.goBack();
    }

    @Override
    public void goForward() {
        super.goForward();
    }
}
