package com.platform.cdcs.fragment.custom;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.CustomerItem;
import com.platform.cdcs.tool.Constant;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
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
 * Created by holytang on 2017/10/15.
 */
public class PriceListFragment extends BaseFragment {

    private ItemAdapter adapter;

    @Override
    public void initView(View view) {
        hideThisToolBar(view);
        initLoadImg(view.findViewById(R.id.load));
        setHasOptionsMenu(true);
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("价格列表");
        ObservableXListView slideListView = (ObservableXListView) view.findViewById(android.R.id.list);
        slideListView.setPullRefreshEnable(false);
        adapter = new ItemAdapter(getContext());
        slideListView.setAdapter(adapter);
        request();
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    private void request() {
        showLoadImg();
        Map<String, String> param = new HashMap<>();
        param.put("distCode", getArguments().getString("id"));
        param.put("itemCode", getArguments().getString("itemCode"));
        getHttpClient().post().url(Constant.DIST_PRODUCT_PRICE_LST).params(Constant.makeParam(param)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                //TODO
                Type type = new TypeToken<BaseObjResponse<CustomerItem.CusList>>() {
                }.getType();
            }
        });
    }

    private class ItemAdapter extends EnhancedAdapter<ChooseItem> {

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            ViewHolder holder = (ViewHolder) paramView.getTag();
            ChooseItem item = getItem(position);
            holder.titleView.setText("");
            holder.textView.setText("");
            holder.descView.setText("");
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.four_item, viewgroup, false);
            ViewHolder holder = new ViewHolder();
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            rootView.findViewById(R.id.time).setVisibility(View.GONE);
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            holder.descView = (TextView) rootView.findViewById(R.id.desc);
            holder.descView.setTextSize(14);
            holder.descView.setTextColor(getResources().getColor(R.color.text_dark));
            rootView.setTag(holder);
            return rootView;
        }
    }

    private class ViewHolder {
        TextView titleView, textView, descView;
    }
}
