package com.platform.cdcs.fragment.account;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.adapter.LetterAcountAdapter;
import com.platform.cdcs.fragment.custom.AddRegNumberFragment;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.CustomerItem;
import com.platform.cdcs.model.DistCustomerList;
import com.platform.cdcs.model.LetterGroup;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.widget.PullToRefreshRecycleView;
import com.platform.cdcs.widget.SectionedRecyclerViewAdapter;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.fragment.SearchListener;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.LetterBar;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/28.
 */
public class AccountListFragment extends BaseFragment {

    private PullToRefreshRecycleView slideListView;
    private LetterBar letterBar;
    private LetterAcountAdapter adapter;
    private String custName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new LetterAcountAdapter(getContext());
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(View view) {
        initLoadImg(view.findViewById(R.id.load));
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setHasOptionsMenu(true);
        setTitle("我的客户");
        view.findViewById(R.id.search_layout).setVisibility(View.VISIBLE);
        initSearch(view.findViewById(R.id.search), "输入名称或代码搜索", new SearchListener() {
            @Override
            public void search(String s) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                custName = s;
                requestList();
            }
        }, view.findViewById(R.id.cancel));
        final GridLayoutManager manager = new GridLayoutManager(getActivity(),1);
        adapter.setLayoutManager(manager);
        letterBar = (LetterBar) view.findViewById(R.id.letter_bar);
        letterBar.setOnLetterSelectListener(new LetterBar.OnLetterSelectListener() {
            @Override
            public void onLetterSelect(int position, String letter, boolean confirmed) {
                Integer sectionPosition = adapter.getSectionPosition(position);
                if (sectionPosition != null)
                    manager.scrollToPositionWithOffset(sectionPosition, 0);
            }
        });
        adapter.setOnItemClickLitener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DistCustomerList.Customer item = (DistCustomerList.Customer) adapter.get(i).getSubList().get((int) l);
                //TODO
            }
        });
        slideListView = (PullToRefreshRecycleView) view.findViewById(android.R.id.list);
        slideListView.setLayoutManager(manager);
        slideListView.setAdapter(adapter);
        requestList();
    }

    @Override
    protected void initMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, "新增客户").setTitle("新增客户").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentUtil.navigateToInNewActivity(getActivity(), AccountRegFragment.class, null);
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public int layoutId() {
        return R.layout.letter_list_view;
    }

    private void requestList() {
        showLoadImg();
        Map<String, String> param = new HashMap<>();
        param.put("custName", custName);
//        param.put("orderType", "0");
//        param.put("pageSize", "5000");
        getHttpClient().post().url(Constant.DIST_CUSTOMER_LST).params(Constant.makeParam(param)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getActivity(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                Type type = new TypeToken<BaseObjResponse<DistCustomerList>>() {
                }.getType();
                BaseObjResponse<DistCustomerList> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    adapter.setDataModel(response.getResult().getLetterModel());
                    adapter.notifyDataSetChanged();
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(RefershEvent event) {
        if (event.mclass == getClass()) {
            requestList();
        }
    }

}
