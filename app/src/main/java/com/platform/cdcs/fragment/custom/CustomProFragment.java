package com.platform.cdcs.fragment.custom;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.MyApp;
import com.platform.cdcs.R;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.CustomerItem;
import com.platform.cdcs.model.DisProductList;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.sherchen.slidetoggleheader.views.XListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.fragment.SearchListener;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by holytang on 2017/9/27.
 */
public class CustomProFragment extends BaseFragment {

    private ItemAdapter adapter;
    private ObservableXListView slideListView;
    private int pageIndex = 1;
    private String searchName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(getContext());
    }

    @Override
    public void initView(View view) {
        hideThisToolBar(view);
        setHasOptionsMenu(true);
        setTitle("自定义产品");
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        view.findViewById(R.id.search_layout).setVisibility(View.VISIBLE);
        initSearch(view.findViewById(R.id.search), "输入产品型号搜索", new SearchListener() {
            @Override
            public void search(String s) {
                pageIndex = 1;
                adapter.clear();
                adapter.notifyDataSetChanged();
                searchName = s;
                request(true, searchName);
            }
        }, view.findViewById(R.id.cancel));
        slideListView = (ObservableXListView) view.findViewById(android.R.id.list);
        slideListView.setPullRefreshEnable(true);
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
        slideListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               final  DisProductList.DisProduct item = (DisProductList.DisProduct) adapterView.getItemAtPosition(i);
                if(item!=null){
                    new TwDialogBuilder(getContext()).setItems(new String[]{"注册证号","价格列表"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Bundle bundle=new Bundle();
                            bundle.putString("id",item.getId());
                            if(i==0){
                                FragmentUtil.navigateToInNewActivity(getActivity(),RegNumberFragment.class,bundle);
                            }else{
                                bundle.putString("itemCode",item.getMaterialStCode());
                                FragmentUtil.navigateToInNewActivity(getActivity(),PriceListFragment.class,bundle);
                            }
                        }
                    },"").create().show();
                }
            }
        });
        slideListView.setAdapter(adapter);
        request(true, searchName);
    }

    @Override
    protected void initMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, "新增产品").setTitle("新增产品").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentUtil.navigateToInNewActivity(getActivity(), AddRegNumberFragment.class, null);
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
        map.put("itemCode", name);
        map.put("distCode", MyApp.getInstance().getAccount().getOrgId());
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        getHttpClient().post().url(Constant.DIST_PRODUCT_LST).params(Constant.makeParam(map)).build().execute(new StringCallback() {
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
                Type type = new TypeToken<BaseObjResponse<DisProductList>>() {
                }.getType();
                BaseObjResponse<DisProductList> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    adapter.addAll(response.getResult().getDisProductList());
                    adapter.notifyDataSetChanged();
                    if (response.getResult().getDisProductList().size() == Constant.PAGE_SIZE) {
                        pageIndex++;
                    }
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }

    private class ItemAdapter extends EnhancedAdapter<DisProductList.DisProduct> {

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            ViewHoder holder = (ViewHoder) paramView.getTag();
            DisProductList.DisProduct item = getItem(position);
            holder.titleView.setText(item.getMaterialStCode());
            holder.textView.setText(item.getMaterialCNDesc());
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
