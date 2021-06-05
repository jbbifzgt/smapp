package com.platform.cdcs.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.MyApp;
import com.platform.cdcs.R;
import com.platform.cdcs.activity.StockScanActivity;
import com.platform.cdcs.adapter.ProductItemAdatper;
import com.platform.cdcs.adapter.SimpleItemAdapter;
import com.platform.cdcs.fragment.stock.EditProductFragment;
import com.platform.cdcs.fragment.stock.ProductDetailFragment;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.HouseItem;
import com.platform.cdcs.model.ProductList;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.model.SubBUItem;
import com.platform.cdcs.tool.CacheTool;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.platform.cdcs.widget.ChooseWindow;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.sherchen.slidetoggleheader.views.XListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/20.
 */
public class ProductFragment extends BaseFragment {

    private ProductItemAdatper adapter;

    private ObservableXListView listView;
    private int pageIndex = 1;
    private TextView titleView, typeView;
    private HouseItem currentItem;
    private String subBU = "";
    private List<ChooseItem> subBuList;
    private TextView numView;

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
        initSelfLoadImg(view.findViewById(R.id.load));
        titleView = (TextView) view.findViewById(R.id.title);
        view.findViewById(R.id.right_side).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.navigateToInNewActivity(getActivity(), EditProductFragment.class, null);
            }
        });
        numView = (TextView) view.findViewById(R.id.num);
        refershNum();
        typeView = (TextView) view.findViewById(R.id.text);
        typeView.setText(ViewTool.makeArrorDown("全部产品线", "▼"));
        TextView pieView = (TextView) view.findViewById(R.id.button1);
        pieView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_pie, 0, 0, 0);
        pieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO pie
            }
        });
        listView = (ObservableXListView) view.findViewById(android.R.id.list);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                adapter.clear();
                adapter.notifyDataSetChanged();
                requestList(false);
            }

            @Override
            public void onLoadMore() {
                requestList(false);
            }
        });
        listView.setAdapter(adapter);
        requestStockList();
        requestSubBu();
    }

    private void showChooseWindow(View view) {
        if (subBuList == null || subBuList.size() == 0) {
            return;
        }
        final ChooseWindow window = new ChooseWindow(getContext());
        window.setData(subBuList, "产品线", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                subBU = window.getChoose();
                if (!TextUtils.isEmpty(subBU)) {
                    if (subBU.contains(",")) {
                        typeView.setText(ViewTool.makeArrorDown(subBU.substring(0, subBU.indexOf(",")) + "等", "▼"));
                    } else {
                        typeView.setText(ViewTool.makeArrorDown(subBU, "▼"));
                    }
                } else {
                    typeView.setText(ViewTool.makeArrorDown("全部产品线", "▼"));
                }
                pageIndex = 1;
                adapter.clear();
                adapter.notifyDataSetChanged();
                requestList(true);
            }
        });
        window.show((View) view.getParent());
    }

    @Override
    public int layoutId() {
        return R.layout.tab_product;
    }

    private void requestStockList() {
        showSelfLoadImg();
        getHttpClient().post().url(Constant.DIST_WHHOUSE_LST).params(Constant.makeParam(new HashMap<String, String>())).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissSelfLoadImg();
                Utils.showToast(getActivity(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissSelfLoadImg();
                Type type = new TypeToken<BaseObjResponse<HouseItem.HouseList>>() {
                }.getType();
                BaseObjResponse<HouseItem.HouseList> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    final List<HouseItem> items = new ArrayList<HouseItem>();
                    for (HouseItem item : response.getResult().getHouseList()) {
                        if ("1".equals(item.getIsMainHouse())) {
                            currentItem = item;
                            items.add(0, item);
                        } else {
                            items.add(item);
                        }
                    }
                    final String[] itemStrs = new String[items.size()];
                    int index = 0;
                    for (HouseItem item : items) {
                        itemStrs[index] = item.getWhName();
                        index++;
                    }
                    if (items.size() > 0) {
                        titleView.setText(ViewTool.makeArrorDown(items.get(0).getWhName(), " ▼"));
                        titleView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new TwDialogBuilder(getContext()).setItems("选择库位", itemStrs, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        currentItem = items.get(i);
                                        titleView.setText(ViewTool.makeArrorDown(items.get(i).getWhName(), " ▼"));
                                        pageIndex = 1;
                                        adapter.clear();
                                        adapter.notifyDataSetChanged();
                                        requestList(true);
                                    }
                                }).create().show();
                            }
                        });
                        requestList(true);
                    } else {
                        Utils.showToast(getContext(), "暂无产品");
                    }
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }

            }
        });
    }

    private void requestList(boolean showFlag) {
        if (showFlag) {
            showSelfLoadImg();
        }
        Map<String, String> param = new HashMap<>();
        param.put("distCode", MyApp.getInstance().getAccount().getOrgId());
        param.put("whCode", currentItem.getWhCode());
        param.put("whName", currentItem.getWhName());
        param.put("subBU", subBU);
        param.put("pageIndex", String.valueOf(pageIndex));
        param.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        getHttpClient().post().url(Constant.DIST_PRODUCT_LST).params(Constant.makeParam(param)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getActivity(), R.string.server_error);
                listView.stopRefresh();
                listView.stopLoadMore();
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    dismissSelfLoadImg();
                    listView.stopRefresh();
                    listView.stopLoadMore();
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
                    e.printStackTrace();
                }
            }
        });
    }

    private void requestSubBu() {
        getHttpClient().post().url(Constant.SUBBU_LST).params(Constant.makeParam(new HashMap<String, String>())).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Utils.showToast(getActivity(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    Type type = new TypeToken<BaseObjResponse<SubBUItem.SubBUList>>() {
                    }.getType();
                    BaseObjResponse<SubBUItem.SubBUList> response = new Gson().fromJson(s, type);
                    if ("1".equals(response.getResult().getCode())) {
                        subBuList = new ArrayList<ChooseItem>();
                        ChooseItem item = new ChooseItem();
                        item.setTitle("全部产品线");
                        item.setTime("");
                        item.setIsCheck(true);
                        subBuList.add(item);
                        for (SubBUItem sub : response.getResult().getObjList()) {
                            item = new ChooseItem();
                            item.setTitle(sub.getLocalSubBUCode());
                            item.setTime(sub.getSubBU());
                            subBuList.add(item);
                        }
                        typeView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showChooseWindow(titleView);
                            }
                        });
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void refershNum() {
        int count = CacheTool.getInputCount(getContext()) + CacheTool.getOutputCount(getContext());
        if (count == 0) {
            numView.setVisibility(View.GONE);
            return;
        }
        numView.setVisibility(View.VISIBLE);
        numView.setText(String.valueOf(count));
    }


    @Subscribe
    public void onEventMainThread(RefershEvent event) {
        if (event.mclass == this.getClass()) {
            refershNum();
        }
    }
}
