package com.platform.cdcs.fragment.stock;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.HouseItem;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by holytang on 2017/9/28.
 */
public class StockListFragment extends BaseFragment {

    private ItemAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(getContext());
    }

    @Override
    public void initView(View view) {
        initLoadImg(view.findViewById(R.id.load));
        hideThisToolBar(view);
        setHasOptionsMenu(true);
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("库位管理");
        ObservableXListView listView = (ObservableXListView) view.findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        requestList();
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    @Override
    protected void initMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, "新增库位").setTitle("新增库位").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentUtil.navigateToInNewActivity(getActivity(), AddStockFragment.class, null);
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    private void requestList() {
        showLoadImg();
        Map<String, String> param = new HashMap<>();
//        param.put("custName", "");
        getHttpClient().post().url(Constant.DIST_WHHOUSE_LST).params(Constant.makeParam(param)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getActivity(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                //TODO
                Type type = new TypeToken<BaseObjResponse<HouseItem.HouseList>>() {
                }.getType();
                BaseObjResponse<HouseItem.HouseList> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    adapter.addAll(response.getResult().getHouseList());
                    adapter.notifyDataSetChanged();
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }

            }
        });
    }

    private class ItemAdapter extends EnhancedAdapter<HouseItem> {

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            ViewHoder holder = (ViewHoder) paramView.getTag();
            HouseItem item = getItem(position);
            holder.titleView.setText(item.getWhName());
            holder.textView.setText(item.getWhCode() + "  " + item.getWhAddress());
            if ("1".equals(item.getIsMainHouse())) {
                holder.timeView.setVisibility(View.VISIBLE);
            } else {
                holder.timeView.setVisibility(View.GONE);
            }
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.up_down, viewgroup, false);
            rootView.setBackgroundResource(R.drawable.shape_corner_center);
            ViewHoder holder = new ViewHoder();
            holder.textView = (TextView) rootView.findViewById(R.id.text1);
            holder.timeView = (TextView) rootView.findViewById(R.id.text);
            holder.titleView = (TextView) rootView.findViewById(R.id.title1);
            holder.timeView.setBackgroundResource(R.drawable.bg_purple_rect);
            holder.timeView.setText("主库");
            rootView.setTag(holder);
            return rootView;
        }
    }

    private class ViewHoder {
        TextView titleView, textView, timeView;
    }
}
