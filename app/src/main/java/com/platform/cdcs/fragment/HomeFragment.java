package com.platform.cdcs.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.platform.cdcs.R;
import com.platform.cdcs.activity.TicketScanActivity;
import com.platform.cdcs.adapter.HomeAdapter;
import com.platform.cdcs.fragment.account.AccountListFragment;
import com.platform.cdcs.fragment.custom.CustomProFragment;
import com.platform.cdcs.fragment.stock.StockInfoFragment;
import com.platform.cdcs.fragment.stock.StockListFragment;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableScrollViewCallbacks;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.sherchen.slidetoggleheader.views.ScrollState;
import com.sherchen.slidetoggleheader.views.ScrollUtils;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.PermissionsUtil;
import com.trueway.app.uilib.widget.TwSheetBuilder;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by holytang on 2017/9/20.
 */
public class HomeFragment extends BaseFragment {

    ObservableXListView slideListView;
    PtrClassicFrameLayout pcflContent;
    View mHeaderToggle;
    /**
     * 能够滑动的header
     */
    private View mVHeader;
    /**
     * 背景图片高度
     */
    private int mBgHeight;
    /**
     * 固定栏高度
     */
    private int mStickHeight;
    /**
     * 能够滑动的最大距离
     */
    private int maxScrollY;
    /**
     * layout已经结束
     */
    private boolean mLayoutFinished;
    private View topView;

    @Override
    public void initView(View view) {
        slideListView = (ObservableXListView) view.findViewById(android.R.id.list);
        slideListView.setDividerHeight(0);
        pcflContent = (PtrClassicFrameLayout) view.findViewById(R.id.pcfl_main_content);
        mVHeader = view.findViewById(R.id.rl_header);
        mHeaderToggle = view.findViewById(R.id.header);
        mHeaderToggle.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO1
            }
        });
        mHeaderToggle.findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO2
            }
        });
        mHeaderToggle.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TicketScanActivity.class));
            }
        });
        mHeaderToggle.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mHeaderToggle.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO help
            }
        });
        topView = view.findViewById(R.id.topPanel);
        topView.findViewById(R.id.scan1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHeaderToggle.findViewById(R.id.button1).performClick();
            }
        });
        topView.findViewById(R.id.scan2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHeaderToggle.findViewById(R.id.button5).performClick();
            }
        });
        topView.findViewById(R.id.scan3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHeaderToggle.findViewById(R.id.button2).performClick();
            }
        });
        topView.findViewById(R.id.scan4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHeaderToggle.findViewById(R.id.button3).performClick();
            }
        });
        topView.findViewById(R.id.help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHeaderToggle.findViewById(R.id.button4).performClick();
            }
        });
//        mHeaderToggle.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, Color.parseColor("#FF61A0FF")));
        mHeaderToggle.setAlpha(0);
        mBgHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mStickHeight = getResources().getDimensionPixelSize(R.dimen.header_sticky_height);
        initPcfl();
        initSlide();
        showContents();
    }

    private void initPcfl() {
        pcflContent.disableWhenHorizontalMove(true);
        pcflContent.setLastUpdateTimeRelateObject(this);
        pcflContent.setDurationToCloseHeader(1500);
        pcflContent.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, slideListView, header);
                return false;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (pcflContent != null) {
                    pcflContent.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (pcflContent != null) {
                                pcflContent.refreshComplete();
                            }
                        }
                    }, 300);
                }
            }
        });
        pcflContent.setPullToRefresh(false);
    }


    private void initSlide() {
        slideListView.setPullRefreshEnable(false);
        ScrollUtils.addOnGlobalLayoutListener(slideListView, new Runnable() {
            @Override
            public void run() {
                mLayoutFinished = true;
                updateScroll(slideListView.getCurrentScrollY());
            }
        });
        slideListView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                if (!mLayoutFinished) return;
                updateScroll(scrollY);
            }

            @Override
            public void onDownMotionEvent() {

            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
            }
        });

        //为ListView添加看不见的header，这个多出的地方是用来给滑动使用的。
        View headerView = new View(getContext());
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, mBgHeight));
        headerView.setMinimumHeight(mBgHeight);
        // This is required to disable header's list selector effect
        headerView.setClickable(true);
        slideListView.addHeaderView(headerView);
    }

    private void updateScroll(int scrollY) {
        if (maxScrollY == 0) {
            maxScrollY = mBgHeight - mStickHeight;
        }
        if (scrollY < 0) {
            scrollY = 0;
        }
        int newScroll = scrollY;
        if (newScroll > maxScrollY) {
            newScroll = maxScrollY;
        }
//        float alpha = ScrollUtils.getFloat((float) newScroll / maxScrollY, 0, 1);
//        mHeaderToggle.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, Color.parseColor("#FF61A0FF")));
//        mTvHeaderToggle.setTextColor(ScrollUtils.getColorWithAlpha(alpha, Color.parseColor("#000000")));
        if (scrollY >= maxScrollY) {
            topView.setAlpha(0);
            mHeaderToggle.setAlpha(1);
        } else {
            mHeaderToggle.setAlpha(0);
            topView.setAlpha(1);
        }
//
//        mVHeader.setAlpha(255-alpha);
//        System.out.println(alpha+"----"+scrollY+"---"+maxScrollY);
        ViewCompat.setTranslationY(mVHeader, -newScroll);
    }


    private void showContents() {
        HomeAdapter adapter = new HomeAdapter(getContext());
        adapter.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TwSheetBuilder(getContext()).bindSheets(new int[]{R.mipmap.visibility_off, R.mipmap.remove_circle}, new String[]{"忽略本条", "不在接受本通知"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {

                        } else {

                        }
                    }
                }).show();
            }
        });
        adapter.setOnGridItemListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        FragmentUtil.navigateToInNewActivity(getContext(), NewAccountFragment.class, null);
                        break;
                    case 1:
                        FragmentUtil.navigateToInNewActivity(getContext(), CustomProFragment.class, null);
                        break;
                    case 2:
                        FragmentUtil.navigateToInNewActivity(getContext(), AccountListFragment.class, null);
                        break;
                    case 3:
                        FragmentUtil.navigateToInNewActivity(getContext(), StockListFragment.class, null);
                        break;
                    case 4:
                        FragmentUtil.navigateToInNewActivity(getContext(), StockInfoFragment.class, null);
                        break;
                    case 5:
                        FragmentUtil.navigateToInNewActivity(getContext(),ApplyFragment.class, null);
                        break;
                }
            }
        });
        for (int i = 0; i < 10; i++) {
            adapter.addItem(new ChooseItem());
        }
        slideListView.setAdapter(adapter);
    }

    @Override
    public int layoutId() {
        return R.layout.tab_home;
    }
}
