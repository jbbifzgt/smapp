package com.platform.cdcs.fragment.stock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.Constant;
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
 * Created by holytang on 2017/9/28.
 */
public class ProductChooseFragment extends BaseFragment {

    private ItemAdapter adapter;
    private boolean allFlag;
    private int pageIndex = 1;

    @Override
    public void initView(View view) {
        adapter = new ItemAdapter(getContext());
        initLoadImg(view.findViewById(R.id.load));
        setTitle("选择产品型号");
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        ObservableXListView listView = (ObservableXListView) view.findViewById(android.R.id.list);
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.up_down_choose, null);
        headerView.findViewById(R.id.title).setVisibility(View.GONE);
        TextView textView = (TextView) headerView.findViewById(R.id.text);
        textView.setText("选择产品（可多选）");
        final TextView checkView = (TextView) headerView.findViewById(R.id.img);
        checkView.setText("全选");
        checkView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_check_rect_press, 0);
        checkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allFlag) {
                    checkView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_check_rect_normal, 0);
                } else {
                    checkView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_check_rect_press, 0);
                }
                allFlag = !allFlag;
            }
        });
        listView.addHeaderView(headerView);
        listView.setPullRefreshEnable(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        listView.setAdapter(adapter);
        request();
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    private void request() {
        showLoadImg();
        Map<String, String> map = new HashMap<>();
        map.put("stockId", "stockId");
        map.put("itemCodes", "itemCodes");
        map.put("subBUs", "subBUs");
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        getHttpClient().post().url(Constant.DIST_SYSSTOCK_TAKE_LST).params(Constant.makeParam(map)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                System.out.println(s);
//                Type type = new TypeToken<BaseObjResponse<CustomerItem.CusList>>() {
//                }.getType();
//                BaseObjResponse<CustomerItem.CusList> response = new Gson().fromJson(s, type);
//                if ("1".equals(response.getResult().getCode())) {
//                    adapter.addAll(response.getResult().getCusList());
//                    adapter.notifyDataSetChanged();
//                } else {
//                    Utils.showToast(getContext(), response.getResult().getMsg());
//                }
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
            if (true) {
                holder.checkView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_check_rect_press, 0);
            } else {
                holder.checkView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_check_rect_normal, 0);
            }
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.up_down_choose, viewgroup, false);
            ViewHolder holder = new ViewHolder();
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            holder.textView.setSingleLine(false);
            holder.textView.setMaxLines(2);
            holder.checkView = (TextView) rootView.findViewById(R.id.img);
            rootView.setTag(holder);
            return rootView;
        }
    }

    private class ViewHolder {
        TextView titleView, checkView, textView;
    }
}
