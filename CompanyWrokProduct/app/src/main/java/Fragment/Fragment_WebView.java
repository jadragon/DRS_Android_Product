package Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.test.tw.wrokproduct.R;

public class Fragment_WebView extends Fragment {
    private WebView luntanListview;
    String html;
    private View v;


    public Fragment_WebView() {
    }

    @SuppressLint("ValidFragment")
    public Fragment_WebView(String html) {
        this.html = html;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.webview_layout, container, false);
        showWebView();
        return v;
    }
    private void showWebView() {
        Log.e("html", html);
        luntanListview=v.findViewById(R.id.pccontent_webview);
        // 设置WevView要显示的网页
        luntanListview.loadDataWithBaseURL(null, html, "text/html", "utf-8",
                null);
        luntanListview.getSettings().setJavaScriptEnabled(true); //设置支持Javascript
        luntanListview.requestFocus(); //触摸焦点起作用.如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
               luntanListview.getSettings().setBuiltInZoomControls(true); //页面添加缩放按钮
        luntanListview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        luntanListview.setVerticalScrollBarEnabled(false);
        luntanListview.setVerticalScrollbarOverlay(false);
        luntanListview.setHorizontalScrollBarEnabled(false);
        luntanListview.setHorizontalScrollbarOverlay(false);
        //                luntanListview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);   //取消滚动条
        //                点击链接由自己处理，而不是新开Android的系统browser响应该链接。
        luntanListview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //设置点击网页里面的链接还是在当前的webview里跳转
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                luntanListview.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
            }
        });
        luntanListview.addJavascriptInterface(this, "App");
        //        luntanListview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        //            @Override
        //            public void onFocusChange(View v, boolean hasFocus) {
        //                if (hasFocus) {
        //                    try {
        //                        // 禁止网页上的缩放
        //                        Field defaultScale = WebView.class
        //                                .getDeclaredField("mDefaultScale");
        //                        defaultScale.setAccessible(true);
        //                        defaultScale.setFloat(luntanListview, 1.0f);
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
    }
    @JavascriptInterface
    public void resize(final float height) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), height + "", Toast.LENGTH_LONG).show();
                //此处的 layoutParmas 需要根据父控件类型进行区分，这里为了简单就不这么做了
                luntanListview.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
            }
        });
    }
}
