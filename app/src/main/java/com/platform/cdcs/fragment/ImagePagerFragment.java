package com.platform.cdcs.fragment;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.platform.cdcs.R;
import com.squareup.picasso.Picasso;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.widget.photoview.PhotoView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by holytang on 2017/9/25.
 */
public class ImagePagerFragment extends BaseFragment {

    private static final String num_format = "%s / %s";
    private List<String> imgs;
    private int pos;
    private TextView numView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String imgsStr = getArguments().getString("imgs");
        if (!TextUtils.isEmpty(imgsStr)) {
            imgs = Arrays.asList(imgsStr.split(","));
        } else {
            imgs = getArguments().getStringArrayList("list");
        }
        pos = getArguments().getInt("pos");
    }

    @Override
    public void initView(View view) {
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("图片浏览");

        numView = (TextView) view.findViewById(R.id.title);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        SamplePagerAdapter pagerAdapter = new SamplePagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        numView.setText(String.format(num_format, 1, imgs.size()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                numView.setText(String.format(num_format, position + 1, imgs.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(pos);
    }

    @Override
    public int layoutId() {
        return R.layout.img_pager_layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Picasso.with(getContext()).cancelTag(getActivity());
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgs.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setTag(position);
            Picasso.with(getContext()).load(imgs.get(position)).tag(getActivity()).into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
