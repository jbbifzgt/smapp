package com.platform.cdcs.fragment;

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
import com.trueway.app.uilib.tool.Utils;

/**
 * Created by holytang on 2017/9/28.
 */
public class MsgSettingFragment extends BaseFragment {

    private ItemAdapter adapter;
    private ObservableXListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(getContext());
        ChooseItem item = new ChooseItem();
        item.setTitle("出入库上报");
        item.setType(1);
        adapter.addItem(item);
        item = new ChooseItem();
        item.setTitle("发票上报");
        item.setType(1);
        adapter.addItem(item);
        item = new ChooseItem();
        item.setTitle("日常提醒");
        item.setType(1);
        adapter.addItem(item);
    }

    @Override
    public void initView(View view) {
        hideThisToolBar(view);
        setTitle("消息设置");
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        TextView textView = new TextView(getContext());
        textView.setText("已“不再接收”消息");
        textView.setTextSize(12);
        textView.setTextColor(getResources().getColor(R.color.text_hint));
        int paddingx = Utils.convertDIP2PX(getContext(), 15);
        textView.setPadding(paddingx, paddingx / 2, paddingx, paddingx / 2);
        listView = (ObservableXListView) view.findViewById(android.R.id.list);
        listView.setPullRefreshEnable(false);
        listView.addHeaderView(textView);
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
            if (item.getType() == 1) {
                holder.textView.setText("推送给我");
            } else {
                holder.textView.setText("已推送");
            }
            View rootView = (View) holder.titleView.getParent();
            if (position == 0) {
                rootView.setBackgroundResource(R.drawable.shape_corner_up);
            } else if (position == getCount() - 1) {
                rootView.setBackgroundResource(R.drawable.shape_corner_down);
            } else {
                rootView.setBackgroundResource(R.drawable.shape_corner_center);
            }
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.margin_item, viewgroup, false);
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
