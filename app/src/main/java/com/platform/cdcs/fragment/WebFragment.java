package com.platform.cdcs.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.platform.cdcs.R;
import com.platform.cdcs.fragment.stock.InStockFragment;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.tool.FragmentUtil;
import com.trueway.app.uilib.activity.BackListener;
import com.trueway.app.uilib.fragment.BaseFragment;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by holytang on 2017/11/1.
 */
public class WebFragment extends BaseFragment {

    private String title;
    private WebView webView;
    private String linkUrl;

    private String mClass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title", "");
            linkUrl = getArguments().getString("url", "");
            mClass = getArguments().getString("class");

        }
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void initView(View view) {
        initLoadImg(view.findViewById(R.id.load));
        setTitle(title);
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        webView = new WebView(getContext());
        webView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayout rootView = (RelativeLayout) view.findViewById(R.id.root);
        rootView.addView(webView);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoadImg();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoadImg();
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.addJavascriptInterface(this, "OS");
        webView.loadUrl(linkUrl);
        addBackListener(new BackListener() {
            @Override
            public void onBackListener() {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    getActivity().finish();
                }
            }
        });
    }

    private RefershEvent getEvent() {
        RefershEvent event = new RefershEvent();
        try {
            event.mclass = Class.forName(mClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return event;
    }

    @JavascriptInterface
    public void command(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            String cmd = obj.getString("cmd");
            if (mClass.contains("CustomProFragment")) {
                if ("delete".equals(cmd)) {
                    EventBus.getDefault().post(getEvent());
                } else if ("regNumber".equals(cmd)) {

                } else if ("priceList".equals(cmd)) {

                }
            } else if (mClass.contains("AccountListFragment")) {
                if ("delete".equals(cmd)) {
                    EventBus.getDefault().post(getEvent());
                }
            } else if (mClass.contains("StockListFragment")) {
                if ("delete".equals(cmd)) {
                    EventBus.getDefault().post(getEvent());
                }
            } else if (mClass.contains("StockInfoFragment")) {
                if ("chooseType".equals(cmd)) {

                } else if ("chooseLine".equals(cmd)) {

                } else if ("click".equals(cmd)) {
                    JSONObject param = obj.getJSONObject("param");
                    int index = param.getInt("index");
                    if (1 == index) {

                    } else if (2 == index) {
                        FragmentUtil.navigateToInNewActivity(getActivity(), InStockFragment.class, getArguments());
                    } else if (3 == index) {

                    } else {

                    }
                }
            } else if ("产品详情".equals(title)) {
                if ("click".equals(cmd)) {
                    JSONObject param = obj.getJSONObject("param");
                    int index = param.getInt("index");
                    if (1 == index) {
                        //产品归属
                    } else if (2 == index) {
                        //发票
                    } else if (3 == index) {
                        //出入库单
                    } else {
                        //产品轨迹
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.removeAllViews();
            webView.clearHistory();
            webView.destroy();
            webView = null;
        }
    }

    @Override
    public int layoutId() {
        return R.layout.webview;
    }
}
