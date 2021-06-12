package com.platform.cdcs.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.adapter.ProductItemAdatper;
import com.platform.cdcs.model.ProductList;
import com.platform.cdcs.tool.Constant;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by holytang on 2017/11/1.
 */
public class GoodsNotifyFragment extends BaseFragment {

    private ProductItemAdatper adatper;

    @Override
    public void initView(View view) {
        initLoadImg(view.findViewById(R.id.load));
        adatper = new ProductItemAdatper(getContext());
        hideThisToolBar(view);
        setTitle("到货通知详情");
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        TextView btnView = (TextView) view.findViewById(R.id.button1);
        btnView.setText("入库");
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inStock();
            }
        });
        TextView layout = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = Utils.convertDIP2PX(getContext(), 10);
        int padding = Utils.convertDIP2PX(getContext(), 12);
        layoutParams.setMargins(margin, margin, margin, margin);
        layout.setPadding(padding, padding, padding, padding);
        layout.setLayoutParams(layoutParams);
        layout.setTextSize(14);
        layout.setTextColor(getResources().getColor(R.color.text_dark));
        layout.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_right, 0);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO  发货方出库单

            }
        });
        ObservableXListView listView = (ObservableXListView) view.findViewById(android.R.id.list);
        listView.addHeaderView(layout);
        listView.setPullRefreshEnable(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProductList.ProductItem item = (ProductList.ProductItem) adapterView.getItemAtPosition(i);
                //TODO 产品详情
            }
        });
        listView.setAdapter(adatper);
        requestList();
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }


    private void requestList() {
        showLoadImg();
        Map<String, String> map = new HashMap<>();
        getHttpClient().post().url(Constant.DOCUMENT_INFO_LST).params(Constant.makeParam(map)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
            }
        });
    }

    private void inStock() {

    }
}
