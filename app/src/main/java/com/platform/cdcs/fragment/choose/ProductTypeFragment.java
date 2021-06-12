package com.platform.cdcs.fragment.choose;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.MyApp;
import com.platform.cdcs.R;
import com.platform.cdcs.adapter.ProductItemAdatper;
import com.platform.cdcs.fragment.account.AccountRegFragment;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.ProductList;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.sherchen.slidetoggleheader.views.XListView;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by holytang on 2017/10/28.
 */
public class ProductTypeFragment extends BaseFragment {

    private ObservableXListView listView;
    private ProductItemAdatper adapter;
    private int model;
    private TextView allBtn;
    private String mClass;
    private int pageIndex = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = getArguments().getInt("model", 0);
            mClass = getArguments().getString("class");
        }
    }

    @Override
    public void initView(View view) {
        setHasOptionsMenu(true);
        adapter = new ProductItemAdatper(getContext());
        if (model == 0) {
            setTitle("选择产品型号");
        } else {
            setTitle("选择产品");
        }
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        initLoadImg(view.findViewById(R.id.load));
        hideThisToolBar(view);
        listView = (ObservableXListView) view.findViewById(android.R.id.list);
        if (model == 1) {
            View headerView = LayoutInflater.from(getContext()).inflate(R.layout.left_right, null);
            ((TextView) headerView.findViewById(R.id.title)).setText("选择产品（可多选）");
            allBtn = (TextView) headerView.findViewById(R.id.text);
            allBtn.setCompoundDrawablePadding(Utils.convertDIP2PX(getContext(), 10));
            allBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_check_rect_normal, 0);
            allBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (allBtn.isSelected()) {
                        allBtn.setSelected(false);
                        allBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_check_rect_normal, 0);
                    } else {
                        allBtn.setSelected(true);
                        allBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_check_rect_press, 0);
                        adapter.chooseAll();
                    }
                }
            });
            listView.addHeaderView(headerView);
        }

        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                adapter.notifyDataSetChanged();
                pageIndex = 1;
                //  if (model == 0) {
                requestDictData(false);
                // } else {
                //     requestData(false);
                // }
            }

            @Override
            public void onLoadMore() {
                // if (model == 0) {
                requestDictData(false);
                // } else {
                //     requestData(false);
                //  }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (model == 1) {
                    ProductList.ProductItem item = adapter.getItem(i);
                    if (item != null) {
                        item.setChoose(item.isChoose() ? false : true);
                        adapter.notifyDataSetChanged();
                    }
                    if (adapter.isExistUnCheck()) {
                        allBtn.setSelected(false);
                        allBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_check_rect_normal, 0);
                    } else {
                        allBtn.setSelected(true);
                        allBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_check_rect_press, 0);
                    }
                } else {
                    RefershEvent event = new RefershEvent();
                    try {
                        event.mclass = Class.forName(mClass);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    event.oclass = ProductTypeFragment.class;
                    event.bundle = new Bundle();
                    event.bundle.putString("code", adapter.getItem(i).getItemCode());
                    EventBus.getDefault().post(event);
                }
            }
        });
        listView.setAdapter(adapter);
//        if (model == 0) {
        requestDictData(true);
//        } else {
//            requestData(true);
//        }
    }

    @Override
    protected void initMenu(Menu menu, MenuInflater inflater) {
        if (model == 1) {
            menu.add(0, 0, 0, "确定").setTitle("确定").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    RefershEvent event = new RefershEvent();
                    try {
                        event.mclass = Class.forName(mClass);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    event.oclass = ProductTypeFragment.class;
                    event.bundle = new Bundle();
                    event.bundle.putString("code", adapter.getChooseCode());
                    EventBus.getDefault().post(event);
                    return false;
                }
            }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

//    private void requestData(boolean show) {
//        if (show) {
//            showLoadImg();
//        }
//        Map<String, String> map = new HashMap<>();
//        map.put("reqType", "4");
//        getHttpClient().post().url(Constant.DIC_URL).params(Constant.makeParam(map)).build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int i) {
//                dismissLoadImg();
//                listView.stopRefresh();
//                Utils.showToast(getContext(), R.string.server_error);
//            }
//
//            @Override
//            public void onResponse(String s, int i) {
//                dismissLoadImg();
//                listView.stopRefresh();
//            }
//        });
//    }

    private void requestDictData(boolean show) {
        if (show) {
            showLoadImg();
        }
        Map<String, String> map = new HashMap<>();
        map.put("distCode", MyApp.getInstance().getAccount().getOrgId());
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(Constant.PAGE_SIZE));

        getHttpClient().post().url(Constant.DIST_PRODUCT_LST).params(Constant.makeParam(map)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                listView.stopRefresh();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                listView.stopRefresh();
                try {
                    Type type = new TypeToken<BaseObjResponse<ProductList>>() {
                    }.getType();
                    BaseObjResponse<ProductList> response = new Gson().fromJson(s, type);
                    if ("1".equals(response.getResult().getCode())) {
                        adapter.addAll(response.getResult().getProductList());
                        adapter.notifyDataSetChanged();
                        if (response.getResult().getProductList().size() == Constant.PAGE_SIZE) {
                            pageIndex++;
                        }
                    } else {
                        Utils.showToast(getContext(), response.getResult().getMsg());
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }


}
