package com.platform.cdcs.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.adapter.SimpleItemAdapter;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;

/**
 * Created by holytang on 2017/9/20.
 */
public class ProductFragment extends BaseFragment {

    private ObservableXListView listView;
    private SimpleItemAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SimpleItemAdapter(getContext());
        adapter.addItem(new ChooseItem());
        adapter.addItem(new ChooseItem());
        adapter.addItem(new ChooseItem());

    }

    @Override
    public void initView(View view) {
        initToolBar(view);
        setFragmentTitle("产品");
        TextView numView = new TextView(getContext());
        numView.setText("8");
        numView.setBackgroundResource(R.mipmap.ic_launcher);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, getResources().getDimensionPixelOffset(R.dimen.title_height) - Utils.convertDIP2PX(getContext(), 20));
        numView.setLayoutParams(lp);
        numView.setGravity(Gravity.CENTER);
        int padding = Utils.convertDIP2PX(getContext(), 10);
        numView.setPadding(padding, 0, padding, 0);
        addRightView(numView);
        listView = (ObservableXListView) view.findViewById(android.R.id.list);
        listView.setPullRefreshEnable(false);
        listView.setAdapter(adapter);
    }

    @Override
    public int layoutId() {
        return R.layout.tab_product;
    }
}
