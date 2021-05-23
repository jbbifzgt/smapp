package com.platform.cdcs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.platform.cdcs.R;
import com.trueway.app.uilib.activity.BaseActivity;

public class ArbitraryFragmentActivity extends BaseActivity {

    public static final String EXTRAS_BUNDLE = "ArbitraryFragmentActivity.EXTRAS_BUNDLE";
    public static final String EXTRAS_FRAGMENT_CLASS_NAME = "ArbitraryFragmentActivity.EXTRAS_FRAGMENT_CLASS_NAME";
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String className = getIntent().getExtras().getString(
                EXTRAS_FRAGMENT_CLASS_NAME);
        initializeFragment(className);
    }

    @Override
    public void initLayout() {
        setContentView(R.layout.base_fragment);
    }

    @Override
    protected void initView() {

    }

    public void initializeFragment(String className) {
        try {
            fragment = (Fragment) Class.forName(className)
                    .newInstance();
            fragment.setArguments(getIntent().getBundleExtra(EXTRAS_BUNDLE));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(fragment!=null){
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragment=null;
    }
}
