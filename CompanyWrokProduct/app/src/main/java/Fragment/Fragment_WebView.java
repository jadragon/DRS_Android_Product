package Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.test.tw.wrokproduct.R;

import library.Component.CustomerWebView;

public class Fragment_WebView extends Fragment {
    private CustomerWebView webView;
    String html;
    private View v;
    ViewGroup container;

    Fragment_WebView.OnHeighChangerListener onHeighChangerListener;

    public Fragment_WebView() {
    }

    public interface OnHeighChangerListener {
        void valueChanged(int Height);
    }

    public void setOnHeighChangerListener(Fragment_WebView.OnHeighChangerListener onHeighChangerListener) {
        this.onHeighChangerListener = onHeighChangerListener;
    }


    @SuppressLint("ValidFragment")
    public Fragment_WebView(String html) {
        this.html = html;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        v = inflater.inflate(R.layout.webview_layout, container, false);
        showWebView();
        return v;
    }

    private void showWebView() {
        webView = v.findViewById(R.id.pccontent_webview);
        webView.setOnScrollChangeListener(new CustomerWebView.OnScrollChangeListener() {
            @Override
            public void onPageEnd(int l, int t, int oldl, int oldt) {
            }

            @Override
            public void onPageTop(int l, int t, int oldl, int oldt) {
                webView.getParent().getParent().getParent().getParent().getParent().requestDisallowInterceptTouchEvent(false);
                //    webView.getParent().getParent().requestDisallowInterceptTouchEvent(false);
            }

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                //     webView.getParent().getParent().requestDisallowInterceptTouchEvent(true);
            }
        });
        // 设置WevView要显示的网页
        webView.loadDataWithBaseURL(null, html, "text/html", "utf-8",
                null);
        webView.getSettings().setJavaScriptEnabled(true); //设置支持Javascript
        // 设置可以支持缩放
        //    webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        //     webView.getSettings().setBuiltInZoomControls(true);
        //设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        webView.getSettings().setUseWideViewPort(true);
        //设置默认加载的可视范围是大视野范围
        webView.getSettings().setLoadWithOverviewMode(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webView.setVerticalScrollBarEnabled(false);
        webView.setVerticalScrollbarOverlay(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setHorizontalScrollbarOverlay(false);
        //                webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);   //取消滚动条
        //                点击链接由自己处理，而不是新开Android的系统browser响应该链接。
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //设置点击网页里面的链接还是在当前的webview里跳转
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //    webView.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
            }
        });
        //        webView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        //            @Override
        //            public void onFocusChange(View v, boolean hasFocus) {
        //                if (hasFocus) {
        //                    try {
        //                        // 禁止网页上的缩放
        //                        Field defaultScale = WebView.class
        //                                .getDeclaredField("mDefaultScale");
        //                        defaultScale.setAccessible(true);
        //                        defaultScale.setFloat(webView, 1.0f);
        //                    } catch (SecurityException e) {
        //                        e.printStackTrace();
        //                    } catch (IllegalArgumentException e) {
        //                        e.printStackTrace();
        //                    } catch (IllegalAccessException e) {
        //                        e.printStackTrace();
        //                    } catch (NoSuchFieldException e) {
        //                        e.printStackTrace();
        //                    }
        //                }
        //            }
        //        });

        //    webView.addJavascriptInterface(this, "App");
    }

    public void clearWebViewResource(ViewGroup container, WebView webView) {
        if (webView != null) {
            webView.removeAllViews();
            // in android 5.1(sdk:21) we should invoke this to avoid memory leak
            // see (https://coolpers.github.io/webview/memory/leak/2015/07/16/
            // android-5.1-webview-memory-leak.html)
            container.removeView(webView);
            webView.setTag(null);
            webView.clearHistory();
            webView.destroy();
        }
    }

    /*

            @JavascriptInterface
            public void resize(final float height) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //此处的 layoutParmas 需要根据父控件类型进行区分，这里为了简单就不这么做了
                        try {
                            webView.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
                            int webviewHeigh = (int) (height);
                            if (onHeighChangerListener != null)
                                onHeighChangerListener.valueChanged(webviewHeigh);
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
    */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearWebViewResource(container, webView);
    }
}
