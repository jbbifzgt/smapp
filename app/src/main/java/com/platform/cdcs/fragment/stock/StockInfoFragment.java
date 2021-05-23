package com.platform.cdcs.fragment.stock;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;

/**
 * Created by holytang on 2017/9/28.
 */
public class StockInfoFragment extends BaseFragment {
    private ItemAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(getContext());
        ChooseItem item = new ChooseItem();
        item.setTitle("2017-03-14");
        item.setText("产品范围");
        adapter.addItem(item);
    }

    @Override
    public void initView(View view) {
        hideThisToolBar(view);
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("库存快照");
        ObservableXListView listView = (ObservableXListView) view.findViewById(android.R.id.list);
        listView.setAdapter(adapter);
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    private class ItemAdapter extends EnhancedAdapter<ChooseItem> {

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            ViewHoder holder = (ViewHoder) paramView.getTag();
            ChooseItem item = getItem(position);
            holder.titleView.setText(item.getTitle());
            holder.textView.setText(item.getText());
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.up_down, viewgroup, false);
            ViewHoder holder = new ViewHoder();
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            rootView.setTag(holder);
            return rootView;
        }
    }

    private class ViewHoder {
        TextView titleView, textView;
    }

}
