package com.platform.cdcs.fragment;

import android.content.Context;
import android.provider.Settings;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.ViewTool;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;

/**
 * Created by holytang on 2017/9/26.
 */
public class TicketChooseFragment extends BaseFragment {

    private ItemAdapter adapter;

    @Override
    public void initView(View view) {
        adapter = new ItemAdapter(getContext());
        ChooseItem item = new ChooseItem();
        item.setTitle("0111237464883748744");
        item.setText("2017-06-03");
        item.setIsCheck(true);
        adapter.addItem(item);
        item = new ChooseItem();
        item.setTitle("0111237464883748744");
        item.setText("2017-06-03");
        adapter.addItem(item);
        hideThisToolBar(view);
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("选择出入库单");
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.ticket_choose, null);
        LinearLayout rootView = (LinearLayout) headerView.findViewById(R.id.root);
        prepareHeader(rootView);
        ObservableXListView listView = (ObservableXListView) view.findViewById(android.R.id.list);
        listView.setPullRefreshEnable(false);
        listView.addHeaderView(headerView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    ChooseItem item = (ChooseItem) adapterView.getItemAtPosition(i);
                    if (item != null) {
                        adapter.unCheck();
                        item.setIsCheck(true);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }
            }
        });
        listView.setAdapter(adapter);
    }

    private void prepareHeader(LinearLayout rootView) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        EditText codeET = ViewTool.createEditItem(inflater, "出入库单号", rootView,false,false);
        codeET.setHint("请填写出入库单号");
        EditText clientET = ViewTool.createEditItem(inflater, "客户", rootView,false,true);
        clientET.setHint("请选择客户");
        clientET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });
        EditText date1ET = ViewTool.createEditItem(inflater, "单据日期", rootView,false,true);
        date1ET.setText("6个月内");
        date1ET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });
        EditText date2ET = ViewTool.createEditItem(inflater, "上报日期", rootView,false,true);
        date2ET.setText("7天内");
        date2ET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });
        EditText typeET = ViewTool.createEditItem(inflater, "单据类型", rootView,false,true);
        typeET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });
        typeET.setInputType(InputType.TYPE_NULL);
        typeET.setHint("请选择单据类型");
        TextView[] btns = ViewTool.createTwoBtnItem(inflater, rootView,false);
        btns[0].setText("重置");
        btns[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btns[1].setText("查询");
        btns[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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
            holder.textView.setText(item.getText());
            if (item.isCheck()) {
                holder.checkView.setVisibility(View.VISIBLE);
            } else {
                holder.checkView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.ticket_choose_item, viewgroup, false);
            ViewHolder holder = new ViewHolder();
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            holder.checkView = (ImageView) rootView.findViewById(R.id.img);
            rootView.setTag(holder);
            return rootView;
        }

        public void unCheck() {
            for (ChooseItem item : dataList) {
                if (item.isCheck()) {
                    item.setIsCheck(false);
                    break;
                }
            }
        }
    }

    private class ViewHolder {
        TextView titleView, textView;
        ImageView checkView;
    }
}
