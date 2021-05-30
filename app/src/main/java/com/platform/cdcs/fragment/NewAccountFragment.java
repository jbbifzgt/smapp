package com.platform.cdcs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.MyApp;
import com.platform.cdcs.R;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.CustomerItem;
import com.platform.cdcs.model.InvoiceList;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.sherchen.slidetoggleheader.views.XListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.fragment.SearchListener;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/24.
 */
public class NewAccountFragment extends BaseFragment {

    private ItemAdapter adapter;
    private ObservableXListView slideListView;
    private int pageIndex = 1;
    private String searchName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(getContext());
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(View view) {
        setHasOptionsMenu(true);
        hideThisToolBar(view);
        initLoadImg(view.findViewById(R.id.load));
        setTitle("新客户申请");
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        view.findViewById(R.id.search_layout).setVisibility(View.VISIBLE);
        initSearch(view.findViewById(R.id.search), "输入客户名称搜索", new SearchListener() {
            @Override
            public void search(String s) {
                pageIndex = 1;
                adapter.clear();
                adapter.notifyDataSetChanged();
                searchName = s;
                request(true, searchName);
            }
        },view.findViewById(R.id.cancel));

        slideListView = (ObservableXListView) view.findViewById(android.R.id.list);
        slideListView.setPullRefreshEnable(true);
        slideListView.setAdapter(adapter);
        slideListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                adapter.clear();
                adapter.notifyDataSetChanged();
                request(false, searchName);
            }

            @Override
            public void onLoadMore() {
                request(false, searchName);
            }
        });
        request(true, searchName);
    }

    protected void initMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, "新增申请").setTitle("新增申请").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentUtil.navigateToInNewActivity(getActivity(), NewAccountRegFragment.class, null);
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    private void request(boolean showLoad, String name) {
        if (showLoad) {
            showLoadImg();
        }
        Map<String, String> map = new HashMap<>();
        if (TextUtils.isEmpty(name)) {
            name = "";
        }
        map.put("custName", name);
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        getHttpClient().post().url(Constant.NEW_CUSTOMER_APPLY_LST).params(Constant.makeParam(map)).build().execute(new StringCallback() {
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
                Type type = new TypeToken<BaseObjResponse<CustomerItem.CusList>>() {
                }.getType();
                BaseObjResponse<CustomerItem.CusList> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    adapter.addAll(response.getResult().getCusList());
                    adapter.notifyDataSetChanged();
                    if(response.getResult().getCusList().size()==Constant.PAGE_SIZE){
                        pageIndex++;
                    }
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(RefershEvent event) {
        if (event.mclass == this.getClass()) {
            pageIndex = 1;
            adapter.clear();
            adapter.notifyDataSetChanged();
            request(true, searchName);
        }
    }

    private class ItemAdapter extends EnhancedAdapter<CustomerItem> {

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            ViewHoder holder = (ViewHoder) paramView.getTag();
            CustomerItem item = getItem(position);
            holder.titleView.setText(item.getCusName());
            if ("0".equals(item.getStatus())) {
                holder.textView.setText("申请中");
                holder.textView.setTextColor(getResources().getColor(R.color.text_dark));
            } else if ("1".equals(item.getStatus())) {
                holder.textView.setText("申请成功");
                holder.textView.setTextColor(getResources().getColor(R.color.color_green));
            } else {
                holder.textView.setText("申请失败");
                holder.textView.setTextColor(getResources().getColor(R.color.text_red));
            }
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.account_item, viewgroup, false);
            ViewHoder holder = new ViewHoder();
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            rootView.setTag(holder);
            return rootView;
        }
    }

    private class ViewHoder {
        TextView titleView, textView;
    }
}
