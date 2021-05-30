package com.platform.cdcs.fragment.choose;

import android.view.View;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.fragment.operation.PackageEditFragment;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.sherchen.slidetoggleheader.views.XListView;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by holytang on 2017/10/6.
 */
public class PackageChooseFragment extends BaseFragment {

    private ObservableXListView listView;

    @Override
    public void initView(View view) {
        hideThisToolBar(view);
        initLoadImg(view.findViewById(R.id.load));
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("选择套包产品");
        TextView btn = (TextView) view.findViewById(R.id.button1);
        btn.setVisibility(View.VISIBLE);
        btn.setText("下一步");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.navigateToInNewActivity(getActivity(), PackageEditFragment.class, null);
            }
        });
        listView = (ObservableXListView) view.findViewById(android.R.id.list);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
//                adapter.clear();
//                adapter.notifyDataSetChanged();
                requestData(false);
            }

            @Override
            public void onLoadMore() {
            }
        });
        requestData(true);
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    private void requestData(boolean show) {
//        if (show) {
//            showLoadImg();
//        }
//        Map<String, String> map = new HashMap<>();
//        map.put("reqType", "5");
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
    }
}
