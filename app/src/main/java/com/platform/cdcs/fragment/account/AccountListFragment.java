package com.platform.cdcs.fragment.account;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.fragment.custom.AddRegNumberFragment;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.CustomerItem;
import com.platform.cdcs.model.DistCustomerList;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
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
import java.util.Map;

import okhttp3.Call;

/**
 * Created by holytang on 2017/9/28.
 */
public class AccountListFragment extends BaseFragment {

    private ObservableXListView slideListView;
    private LetterBar letterBar;
    private ItemAdapter adapter;
    private String custName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(getContext());
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
        initSearch(view.findViewById(R.id.search), "输入产品型号查询", new SearchListener() {
            @Override
            public void search(String s) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                custName = s;
                requestList();
            }
        }, view.findViewById(R.id.cancel));

        letterBar = (LetterBar) view.findViewById(R.id.letter_bar);
        letterBar.setOnLetterSelectListener(new LetterBar.OnLetterSelectListener() {
            @Override
            public void onLetterSelect(int position, String letter, boolean confirmed) {
                Integer sectionPosition = adapter.getSectionPosition(position);
                if (sectionPosition != null)
                    slideListView.setSelection(sectionPosition);
            }
        });
        slideListView = (ObservableXListView) view.findViewById(android.R.id.list);
        slideListView.setPullRefreshEnable(false);
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
                //TODO
                Type type = new TypeToken<BaseObjResponse<DistCustomerList>>() {
                }.getType();
                BaseObjResponse<DistCustomerList> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    adapter.addAll(response.getResult().getDistCustomerList());
                    adapter.notifyDataSetChanged();
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }

    private class ItemAdapter extends EnhancedAdapter<DistCustomerList.Customer> {

        private int mLineNumber = 0;
        private SparseArray<Integer> mKeyPositionMap = new SparseArray<>();

        public ItemAdapter(Context context) {
            super(context);
        }

        private void init() {
            calculateSectionPosition();
        }

        public Integer getSectionPosition(int asciiPosition) {
            return mKeyPositionMap.get(asciiPosition);
        }

        private void calculateSectionPosition() {
            int pos = 0;
            int i = 0;
//            for (PatientModel.PatientGroup group : dataModel.getGroupList()) {
//                mKeyPositionMap.put(group.getTitle().charAt(0) - 'A' + 1, i);
//                i += group.getPatientList().size();
//                i++;
//            }
            mLineNumber = pos;
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            TextView textView = (TextView) paramView;
            textView.setBackgroundResource(R.drawable.shape_corner_center);
            textView.setText(getItem(position).getCustName());
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            TextView textView = new TextView(context);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
            textView.setTextSize(14);
            int padding = Utils.convertDIP2PX(getContext(), 12);
            textView.setPadding(padding, padding, padding, padding);
            textView.setTextColor(getResources().getColor(R.color.text_black));
            textView.setLayoutParams(lp);
            return textView;
        }
    }
}
