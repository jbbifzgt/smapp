package com.platform.cdcs.fragment.choose;

import android.view.View;

import com.platform.cdcs.R;
import com.platform.cdcs.adapter.ProductItemAdatper;
import com.platform.cdcs.tool.Constant;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.sherchen.slidetoggleheader.views.XListView;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by holytang on 2017/10/28.
 */
public class ProductTypeFragment extends BaseFragment {

    private ObservableXListView listView;
    private ProductItemAdatper adapter;

    @Override
    public void initView(View view) {
        adapter = new ProductItemAdatper(getContext());
        setTitle("选择产品型号");
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
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                adapter.notifyDataSetChanged();
                requestData(false);
            }

            @Override
            public void onLoadMore() {
            }
        });
        listView.setAdapter(adapter);
        requestData(true);
    }

    private void requestData(boolean show) {
        if (show) {
            showLoadImg();
        }
        Map<String, String> map = new HashMap<>();
        map.put("reqType", "4");
        getHttpClient().post().url(Constant.DIC_URL).params(Constant.makeParam(map)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                listView.stopRefresh();
                Utils.showToast(getContext(),R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                listView.stopRefresh();
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }


}
