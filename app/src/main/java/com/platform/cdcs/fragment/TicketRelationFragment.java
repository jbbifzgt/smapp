package com.platform.cdcs.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.DocNo;
import com.platform.cdcs.model.InvoiceList;
import com.platform.cdcs.model.MockObj;
import com.platform.cdcs.model.ProductList;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.widget.FormDialog;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.sherchen.slidetoggleheader.views.XListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/25.
 */
public class TicketRelationFragment extends BaseFragment {

    private ItemAdapter itemAdapter;
    private TextView codeView, timeView;
    private String id, cusCode;
    private DocNo docNo;
    private int pageIndex=1;
    private int position;
    private ObservableXListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        itemAdapter = new ItemAdapter(getContext());
        id = getArguments().getString("id");
        cusCode = getArguments().getString("cusCode");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(View view) {
        initLoadImg(view.findViewById(R.id.load));
        view.findViewById(R.id.activity_main_toolbar).setVisibility(View.GONE);
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("关联产品");
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.ticket_header, null);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("pos", position);
                bundle.putSerializable("model", docNo);
                FragmentUtil.navigateToInNewActivity(getActivity(), TicketChooseFragment.class, bundle);
            }
        });
        codeView = (TextView) headerView.findViewById(R.id.code);
        timeView = (TextView) headerView.findViewById(R.id.time);
        listView = (ObservableXListView) view.findViewById(android.R.id.list);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                itemAdapter.clear();
                itemAdapter.notifyDataSetChanged();
                requestProduct(false);
            }

            @Override
            public void onLoadMore() {
                requestProduct(false);
            }
        });
        listView.addHeaderView(headerView);
        listView.setAdapter(itemAdapter);
        requestDetail();
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    private void requestDetail() {
        showLoadImg();
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        param.put("cusCode", cusCode);
        getHttpClient().post().url(Constant.LIST_FOR_INVOICE).params(Constant.makeParam(param)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                Type type = new TypeToken<BaseObjResponse<DocNo>>() {
                }.getType();
                BaseObjResponse<DocNo> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    docNo = response.getResult();
                    if (docNo.getDocNoList().size() > 0) {
                        position = 0;
                        showDoc(docNo.getDocNoList().get(position));
                    }
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }

            }
        });
    }

    private void showDoc(DocNo.DocItem item) {
        codeView.setText(item.getDocNo());
        timeView.setText(item.getDocDate());
        requestProduct(true);
    }

    private void requestProduct(boolean load) {
        if (load) {
            showLoadImg();
        }
        Map<String, String> map = new HashMap<>();
        map.put("cusCode", cusCode);
        map.put("docNo", codeView.getText().toString());
        map.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        map.put("pageIndex", String.valueOf(pageIndex));
        getHttpClient().post().url(Constant.NO_INVOICE_PRODUCT).params(Constant.makeParam(map)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                listView.stopLoadMore();
                listView.stopRefresh();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                listView.stopLoadMore();
                listView.stopRefresh();
                Type type = new TypeToken<BaseObjResponse<ProductList>>() {
                }.getType();
                BaseObjResponse<ProductList> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    itemAdapter.addAll(response.getResult().getProductList());
                    itemAdapter.notifyDataSetChanged();
                    if (response.getResult().getProductList().size() == Constant.PAGE_SIZE) {
                        pageIndex++;
                    }
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(DocNo.DocItem item) {
        position = item.getPosition();
        showDoc(docNo.getDocNoList().get(position));
    }

    private void removeProduct(ProductList.ProductItem item) {
        showLoadImg();
        Map<String, String> map = new HashMap<>();
        map.put("id", item.getId());
        getHttpClient().post().url(Constant.CANCEL_INVOICE_PRODUCT).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                Type type = new TypeToken<BaseObjResponse<MockObj>>() {
                }.getType();
                BaseObjResponse<MockObj> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    pageIndex = 1;
                    itemAdapter.clear();
                    itemAdapter.notifyDataSetChanged();
                    requestProduct(true);
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }

    private class ItemAdapter extends EnhancedAdapter<ProductList.ProductItem> {

        private View.OnClickListener leftListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductList.ProductItem item = (ProductList.ProductItem) view.getTag();
                new FormDialog(getContext(), new FormDialog.FormListener() {
                    @Override
                    public void okClick(String[] value) {

                    }
                }, "编辑产品数量").addView(new String[]{"产品数量"}, 0,"").show();
            }
        };

        private View.OnClickListener rightListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProductList.ProductItem item = (ProductList.ProductItem) view.getTag();
                new TwDialogBuilder(getContext()).setTitle(R.string.attention).setMessage("确定是否移除吗？").setPositiveButton(R.string.cancel_text, null).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeProduct(item);
                    }
                }).create().show();
            }
        };

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            ViewHolder holder = (ViewHolder) paramView.getTag();
            ProductList.ProductItem item = getItem(position);
            holder.titleView.setText(item.getItemCode());
            holder.timeView.setText(item.getUom());
            holder.textView.setText(item.getItemName());
            holder.leftBtn.setTag(item);
            holder.rightBtn.setTag(item);
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.stock_item_group, viewgroup, false);
            rootView.setBackgroundResource(R.drawable.shape_corner_center);
            ViewHolder holder = new ViewHolder();
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            holder.titleView.getPaint().setFakeBoldText(true);
            holder.timeView = (TextView) rootView.findViewById(R.id.time);
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            holder.textView.setSingleLine(false);
            holder.leftBtn = (TextView) rootView.findViewById(R.id.smMenuViewLeft);
            holder.rightBtn = (TextView) rootView.findViewById(R.id.smMenuViewRight);
            holder.leftBtn.setOnClickListener(leftListener);
            holder.rightBtn.setOnClickListener(rightListener);
            rootView.setTag(holder);
            return rootView;
        }
    }

    private class ViewHolder {
        TextView titleView, timeView, textView, leftBtn, rightBtn;
    }
}