package com.platform.cdcs.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.platform.cdcs.R;
import com.platform.cdcs.fragment.HomeFragment;
import com.platform.cdcs.fragment.MineFragment;
import com.platform.cdcs.fragment.ProductFragment;
import com.platform.cdcs.fragment.TicketFragment;
import com.trueway.app.uilib.activity.BaseActivity;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwSegmentWidget;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by holytang on 2017/9/20.
 */
public class MainTabActivity extends BaseActivity {

    private ViewPager viewPager;
    private PagerAdapter adapter;
    private TwSegmentWidget segWidth;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        removeHomeReceiver();
        getToolBar().setVisibility(View.GONE);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                segWidth.choose(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new ProductFragment());
        fragments.add(new TicketFragment());
        fragments.add(new MineFragment());
        adapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        GridView segView = (GridView) findViewById(R.id.seg);
        segView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewPager.setCurrentItem(i, false);
            }
        });
        String[] pages = new String[]{"首页", "产品", "发票", "我"};
        int[] imgs = new int[]{R.drawable.tab_1, R.drawable.tab_2, R.drawable.tab_3, R.drawable.tab_4};
        segWidth = new TwSegmentWidget(segView, pages, imgs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public PagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }
}
