package beyond_imagination.blubblub.pWebView;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by cru65 on 2017-07-26.
 */

public class MyWebViewClient extends WebViewClient {
    public MyWebViewClient() {
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }
}
