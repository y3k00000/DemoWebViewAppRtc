package y3k.testwebviewapprtc;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TestWebViewAppRTCActivity extends AppCompatActivity {

    WebView webView;
    boolean videoElementDisplayingSetTo = true;
    
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = findViewById(R.id.web_view);
        final String tag = "WEB_DEBUG";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(tag,"onPageFinished "+url);
                if(url.startsWith("https://appr.tc/join/")) {
                    videoElementDisplayingSetTo = false;
                    view.loadUrl("javascript:(document.getElementById(\"videos\").style.display='none')();");
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return this.shouldInterceptRequest(view,request.getUrl().toString());
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @SuppressWarnings("deprecation")
            @Override
            public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {
                Log.d(tag,"shouldInterceptRequest url="+url);
                if(!videoElementDisplayingSetTo&&url.startsWith("https://appr.tc/join/")){
                    // Strange, this should be called on main thread but I found it not.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.loadUrl("javascript:(document.getElementById(\"videos\").style.display='block')();");
                        }
                    });
                    videoElementDisplayingSetTo = true;
                }
                return super.shouldInterceptRequest(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.loadUrl(getIntent().getStringExtra("url"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.loadData("<html></html>","text/html","utf8");
    }

}
