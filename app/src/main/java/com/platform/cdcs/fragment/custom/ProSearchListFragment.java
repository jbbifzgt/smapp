package com.platform.cdcs.fragment.custom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.adapter.ProductItemAdatper;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.sherchen.slidetoggleheader.views.XListView;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.fragment.SearchListener;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/28.
 */
public class ProSearchListFragment extends BaseFragment {

    private ObservableXListView slideListView;
    private ProductItemAdatper adapter;
    private String code = "", line = "";
    private TextView lineTV, lineNameTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        adapter = new ProductItemAdatper(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
        setHasOptionsMenu(true);
        setTitle("选择产品");

        view.findViewById(R.id.search_layout).setVisibility(View.VISIBLE);
        initSearch(view.findViewById(R.id.search), "输入产品代码搜索", new SearchListener() {
            @Override
            public void search(String s) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                requestList(true);
            }
        }, view.findViewById(R.id.cancel));

        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.choose_item, null);
        headerView.findViewById(R.id.img).setVisibility(View.GONE);
        lineTV = (TextView) headerView.findViewById(R.id.title);
        lineNameTV = (TextView) headerView.findViewById(R.id.text);
        lineTV.setText("全部产品线");
        lineNameTV.setText(" ▼");
        slideListView = (ObservableXListView) view.findViewById(android.R.id.list);
        slideListView.addHeaderView(headerView);
        slideListView.setPullRefreshEnable(true);
        slideListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                adapter.notifyDataSetChanged();
                requestList(false);
            }

            @Override
            public void onLoadMore() {

            }
        });
        slideListView.setAdapter(adapter);
        requestList(true);
    }

    @Override
    protected void initMenu(Menu menu, MenuInflater inflater) {
//        menu.add(0, 0, 0, "搜索").setIcon(R.mipmap.icon_search).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                FragmentUtil.navigateToInNewActivity(getActivity(), ProSearchFragment.class, null);
//                return false;
//            }
//        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    private void requestList(boolean show) {
        if (show) {
            showLoadImg();
        }
        Map<String, String> map = new HashMap<>();
        map.put("reqType", "4");
//        map.put("code", code);
//        map.put("line", line);
        getHttpClient().post().url(Constant.DIC_URL).params(Constant.makeParam(map)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                slideListView.stopRefresh();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                slideListView.stopRefresh();
                //TODO
            }
        });
    }

    @Subscribe
    public void onEventMainThread(RefershEvent event) {
        if (event.mclass == getClass()) {
            code = event.bundle.getString("code");
            line = event.bundle.getString("line");
            requestList(true);
        }
    }
}
