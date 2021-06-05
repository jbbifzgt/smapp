package com.platform.cdcs.fragment.stock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by holytang on 2017/10/30.
 */
public class InStockDetailFrgment extends BaseFragment {


    private ItemAdapter adapter;
    private TextView titleView;

    @Override
    public void initView(View view) {
        adapter = new ItemAdapter(getContext());
        initLoadImg(view.findViewById(R.id.load));
        hideThisToolBar(view);
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("在库");
        ObservableXListView listView = (ObservableXListView) view.findViewById(android.R.id.list);
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.left_right, null);
        titleView = (TextView) headerView.findViewById(R.id.title);
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 库位选择
            }
        });
        listView.addHeaderView(headerView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //详情页面
            }
        });
        listView.setPullRefreshEnable(false);
        listView.setAdapter(adapter);
        requestList();
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    private void requestList() {
        showLoadImg();
        Map<String, String> param = new HashMap<>();
        param.put("stockId", getArguments().getString("id"));
        getHttpClient().post().url(Constant.DIST_ITEMCODESUBBU_LST).params(param).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getActivity(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();

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
            holder.titleView.setText(item.getTitle());
            holder.textView.setText(item.getText());
            holder.dateView.setText(item.getTime());
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = null;
            rootView = inflater.inflate(R.layout.four_item, viewgroup, false);
            rootView.setBackgroundResource(R.drawable.shape_corner_center);
            ViewHolder holder = new ViewHolder();
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            holder.dateView = (TextView) rootView.findViewById(R.id.time);
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            holder.textView.setSingleLine(false);
            holder.textView.setMaxLines(2);
            rootView.setTag(holder);
            return rootView;
        }

    }

    private class ViewHolder {
        TextView titleView, dateView, textView;
    }
}
