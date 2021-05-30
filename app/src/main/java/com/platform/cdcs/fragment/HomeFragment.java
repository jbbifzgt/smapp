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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.activity.SearchScanActivity;
import com.platform.cdcs.activity.StockScanActivity;
import com.platform.cdcs.activity.TicketScanActivity;
import com.platform.cdcs.adapter.HomeAdapter;
import com.platform.cdcs.fragment.account.AccountListFragment;
import com.platform.cdcs.fragment.custom.CustomProFragment;
import com.platform.cdcs.fragment.stock.StockInfoFragment;
import com.platform.cdcs.fragment.stock.StockListFragment;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.CustomerItem;
import com.platform.cdcs.model.MockObj;
import com.platform.cdcs.model.NoticeList;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableScrollViewCallbacks;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.sherchen.slidetoggleheader.views.ScrollState;
import com.sherchen.slidetoggleheader.views.ScrollUtils;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.PermissionsUtil;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwSheetBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

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
    private HomeAdapter adapter;

    @Override
    public void initView(View view) {
        initSelfLoadImg(view.findViewById(R.id.load));
        slideListView = (ObservableXListView) view.findViewById(android.R.id.list);
        slideListView.setDividerHeight(0);
        pcflContent = (PtrClassicFrameLayout) view.findViewById(R.id.pcfl_main_content);
        mVHeader = view.findViewById(R.id.rl_header);
        mHeaderToggle = view.findViewById(R.id.header);
        mHeaderToggle.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO1
                Intent intent = new Intent(getContext(), StockScanActivity.class);
                intent.putExtra("model", 0);
                startActivity(intent);
            }
        });
        mHeaderToggle.findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO2
                Intent intent = new Intent(getContext(), StockScanActivity.class);
                intent.putExtra("model", 1);
                startActivity(intent);
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
                startActivity(new Intent(getContext(), SearchScanActivity.class));
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
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, slideListView, header);
//                return true;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                if (pcflContent != null) {
//                    pcflContent.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (pcflContent != null) {
//                                pcflContent.refreshComplete();
//                            }
//                        }
//                    }, 300);
//                }
                adapter.removeList();
                adapter.notifyDataSetChanged();
                requestNotify();
            }
        });
        pcflContent.setPullToRefresh(true);
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
        adapter = new HomeAdapter(getContext());
        adapter.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NoticeList.NoticeItem item = (NoticeList.NoticeItem) view.getTag();

                new TwSheetBuilder(getContext()).bindSheets(new int[]{R.mipmap.visibility_off, R.mipmap.remove_circle}, new String[]{"忽略本条", "不在接受本通知"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {

                        } else {
                            notReceive(item.getMsgType());
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
                        FragmentUtil.navigateToInNewActivity(getContext(), ApplyFragment.class, null);
                        break;
                }
            }
        });
        adapter.addItem(new NoticeList.NoticeItem());
        slideListView.setAdapter(adapter);
        showSelfLoadImg();
        requestNotify();
    }

    @Override
    public int layoutId() {
        return R.layout.tab_home;
    }

    private void requestNotify() {
        getHttpClient().post().url(Constant.NOTICE_LST).params(Constant.makeParam(new HashMap<String, String>())).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Utils.showToast(getContext(), R.string.server_error);
                dismissSelfLoadImg();
            }

            @Override
            public void onResponse(String s, int i) {
                dismissSelfLoadImg();
                pcflContent.refreshComplete();
                Type type = new TypeToken<BaseObjResponse<NoticeList>>() {
                }.getType();
                BaseObjResponse<NoticeList> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    adapter.addAll(response.getResult().getNoticeList());
                    adapter.notifyDataSetChanged();
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }

    private void notReceive(int type) {
        showSelfLoadImg();

        Map<String, String> map = new HashMap<>();
        if (type == 2) {
            map.put("inoutMsg", "0");
        } else if (type == 3) {
            map.put("invoiceMsg", "0");
        } else if (type == 4) {
            map.put("commonMsg", "0");
        } else {
            map.put("arriveMsg", "0");
        }
        getHttpClient().post().url(Constant.EDIT_DIST_MSGINFO).params(Constant.makeParam(map)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Utils.showToast(getContext(), R.string.server_error);
                dismissSelfLoadImg();
            }

            @Override
            public void onResponse(String s, int i) {
                dismissSelfLoadImg();
                Type type = new TypeToken<BaseObjResponse<MockObj>>() {
                }.getType();
                BaseObjResponse<MockObj> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    Utils.showToast(getContext(), "设置成功！");
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }
}
