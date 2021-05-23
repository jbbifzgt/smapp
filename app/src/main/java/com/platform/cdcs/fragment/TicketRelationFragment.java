package com.platform.cdcs.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;

import org.w3c.dom.Text;

/**
 * Created by holytang on 2017/9/25.
 */
public class TicketRelationFragment extends BaseFragment {

    private ItemAdapter itemAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemAdapter = new ItemAdapter(getContext());
        ChooseItem item = new ChooseItem();
        item.setTitle("W9236T");
        item.setTime("0.50 EA");
        item.setText("ECHELON(FLEX)60腔镜（关节头）直线型切割吻 合器钉仓，白色");
        itemAdapter.addItem(item);

        item = new ChooseItem();
        item.setTitle("W9236T");
        item.setTime("0.50 EA");
        item.setText("ECHELON(FLEX)60腔镜（关节头）直线型切割吻 合器钉仓，白色");
        itemAdapter.addItem(item);


        item = new ChooseItem();
        item.setTitle("W9236T");
        item.setTime("0.50 EA");
        item.setText("ECHELON(FLEX)60腔镜（关节头）直线型切割吻 合器钉仓，白色");
        itemAdapter.addItem(item);
    }

    @Override
    public void initView(View view) {
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
                FragmentUtil.navigateToInNewActivity(getActivity(),TicketChooseFragment.class,null);
            }
        });
        TextView codeView = (TextView) headerView.findViewById(R.id.code);
        codeView.setText("0111387457485849");
        TextView timeView = (TextView) headerView.findViewById(R.id.code);
        timeView.setText("2017-01-09");
        ObservableXListView listView = (ObservableXListView) view.findViewById(android.R.id.list);
        listView.setPullRefreshEnable(false);
        listView.addHeaderView(headerView);
        listView.setAdapter(itemAdapter);
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
            ViewHolder holder = (ViewHolder) paramView.getTag();
            ChooseItem item = getItem(position);
            holder.titleView.setText(item.getTitle());
            holder.timeView.setText(item.getTime());
            holder.textView.setText(item.getText());
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.ticket_item, viewgroup, false);
            rootView.setBackgroundResource(R.drawable.shape_corner_center);
            ViewHolder holder = new ViewHolder();
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            holder.titleView.getPaint().setFakeBoldText(true);
            holder.timeView = (TextView) rootView.findViewById(R.id.time);
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            TextView desc = (TextView) rootView.findViewById(R.id.desc);
            desc.setText("0.50 EA");
            desc.setVisibility(View.INVISIBLE);
            holder.textView.setSingleLine(false);
            rootView.setTag(holder);
            return rootView;
        }
    }

    private class ViewHolder {
        TextView titleView, timeView, textView;
    }
}
