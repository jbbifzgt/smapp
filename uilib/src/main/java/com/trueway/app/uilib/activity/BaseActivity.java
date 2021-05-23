package com.trueway.app.uilib.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.trueway.app.uilib.R;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * Created by holy on 16-11-17.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    private ImageView loadView;
    private BackListener backListener;

    protected abstract void initLayout();

    protected abstract void initView();

    public void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initActionBar();
        initView();
    }

    public Toolbar getToolBar() {
        return toolbar;
    }

    public void initLoadImg(View loadView) {
        try {
            this.loadView = (ImageView) loadView;
            this.loadView.setImageResource(R.mipmap.loading_icon);
            dismissLoadImg();
        } catch (Exception e) {
        }
    }

    public void showLoadImg() {
        if (loadView != null) {
            loadView.setVisibility(View.VISIBLE);
            Animation localAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_spinner);
            localAnimation.reset();
            loadView.startAnimation(localAnimation);
        }
    }

    public void dismissLoadImg() {
        if (loadView != null) {
            loadView.setVisibility(View.GONE);
            Animation localAnimation = new AlphaAnimation(1, 0);
            localAnimation.reset();
            loadView.startAnimation(localAnimation);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        initMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected void initMenu(Menu menu) {

    }

    protected Activity getActivity() {
        return this;
    }

    @Override
    public void setTitle(CharSequence title) {
        ((TextView) toolbar.findViewById(R.id.title)).setText(title);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    public void addBackListener(BackListener listener) {
        backListener = listener;
    }

    public void removeBackListener() {
        backListener = null;
    }

    @Override
    public void onBackPressed() {
        if (backListener != null) {
            backListener.onBackListener();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }


    public void onBackListener() {

    }

    public OkHttpUtils getHttpClient() {
        return OkHttpUtils.getInstance();
    }

}
