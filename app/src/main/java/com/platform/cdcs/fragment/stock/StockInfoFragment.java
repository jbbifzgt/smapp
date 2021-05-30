package com.platform.cdcs.fragment.stock;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.DistSysStock;
import com.platform.cdcs.tool.Constant;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.sherchen.slidetoggleheader.views.XListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by holytang on 2017/9/28.
 */
public class StockInfoFragment extends BaseFragment {
    private ItemAdapter adapter;
    private int pageIndex = 1;
    private ObservableXListView slideListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(getContext());
    }

    @Override
    public void initView(View view) {
        initLoadImg(view.findViewById(R.id.load));
        hideThisToolBar(view);
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("库存快照");
        slideListView = (ObservableXListView) view.findViewById(android.R.id.list);
        slideListView.setPullRefreshEnable(true);
        slideListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex=1;
                adapter.clear();
                adapter.notifyDataSetChanged();
                request(false);
            }

            @Override
            public void onLoadMore() {
                request(false);
            }
        });
        slideListView.setAdapter(adapter);
        request(true);
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    private void request(boolean showload) {
        if (showload) {
            showLoadImg();
        }
        Map<String, String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        getHttpClient().post().url(Constant.DIST_SYSSTOCK_TAKE_LST).params(Constant.makeParam(map)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                slideListView.stopRefresh();
                slideListView.stopLoadMore();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                slideListView.stopRefresh();
                slideListView.stopLoadMore();
                Type type = new TypeToken<BaseObjResponse<DistSysStock.DistSysStockTakeList>>() {
                }.getType();
                BaseObjResponse<DistSysStock.DistSysStockTakeList> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    adapter.addAll(response.getResult().getDistSysStockTakeList());
                    adapter.notifyDataSetChanged();
                    if(response.getResult().getDistSysStockTakeList().size()==Constant.PAGE_SIZE){
                        pageIndex++;
                    }
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }

    private class ItemAdapter extends EnhancedAdapter<DistSysStock> {

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            ViewHoder holder = (ViewHoder) paramView.getTag();
            DistSysStock item = getItem(position);
            holder.titleView.setText(item.getStockTakeDate());
            holder.textView.setText("产品线范围："+item.getSubBu());
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.up_down, viewgroup, false);
            rootView.setBackgroundResource(R.drawable.shape_corner_center);
            ViewHoder holder = new ViewHoder();
            holder.textView = (TextView) rootView.findViewById(R.id.text1);
            holder.titleView = (TextView) rootView.findViewById(R.id.title1);
            rootView.setTag(holder);
            return rootView;
        }
    }

    private class ViewHoder {
        TextView titleView, textView;
    }

}
