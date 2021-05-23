package com.platform.cdcs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.adapter.SimpleItemAdapter;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;

/**
 * Created by holytang on 2017/9/20.
 */
public class MineFragment extends BaseFragment {

    private ItemAdpter itemAdapter;
    private SimpleItemAdapter simpleItemAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemAdapter = new ItemAdpter(getContext());
        ChooseItem item = new ChooseItem();
        item.setTitle("手机");
        item.setDrawable(R.mipmap.self_phone);
        item.setText("13913866132");
        itemAdapter.addItem(item);

        item = new ChooseItem();
        item.setTitle("固话");
        item.setDrawable(R.mipmap.self_tel);
        item.setText("025-88687912");
        itemAdapter.addItem(item);

        item = new ChooseItem();
        item.setTitle("E-mail");
        item.setDrawable(R.mipmap.self_email);
        item.setText("xiaolichen@gmail.com");
        itemAdapter.addItem(item);

        item = new ChooseItem();
        item.setTitle("QQ");
        item.setDrawable(R.mipmap.self_qq);
        item.setText("--");
        itemAdapter.addItem(item);

        simpleItemAdapter = new SimpleItemAdapter(getContext());
        ChooseItem chooseItem = new ChooseItem();
        chooseItem.setType(0);
        simpleItemAdapter.addItem(chooseItem);
        chooseItem = new ChooseItem();
        chooseItem.setType(1);
        chooseItem.showRight(true);
        chooseItem.setTitle("基础信息及税率设置");
        simpleItemAdapter.addItem(chooseItem);
        chooseItem = new ChooseItem();
        chooseItem.setType(2);
        chooseItem.showRight(true);
        chooseItem.setTitle("消息设置");
        simpleItemAdapter.addItem(chooseItem);
        chooseItem = new ChooseItem();
        chooseItem.setType(3);
        chooseItem.setTitle("检测系统更新");
        chooseItem.setTime("当前版本：v5.0");
        chooseItem.showRight(true);
        simpleItemAdapter.addItem(chooseItem);
        chooseItem = new ChooseItem();
        chooseItem.setType(0);
        simpleItemAdapter.addItem(chooseItem);
        chooseItem = new ChooseItem();
        chooseItem.setType(4);
        chooseItem.setTitle("退出当前账号");
        simpleItemAdapter.addItem(chooseItem);
    }

    @Override
    public void initView(View view) {
        initToolBar(view);
        setFragmentTitle("陈晓丽");
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.tab_mine_header, null);
        GridView gridView = (GridView) headerView.findViewById(R.id.grid);
        gridView.setNumColumns(2);
        gridView.setAdapter(itemAdapter);
        ObservableXListView listView = (ObservableXListView) view.findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==7){
                    getActivity().finish();
                }else if(i==3){
                    FragmentUtil.navigateToInNewActivity(getActivity(),BaseInfoFragment.class,null);
                }else if(i==4){
                    FragmentUtil.navigateToInNewActivity(getActivity(),MsgSettingFragment.class,null);
                }else if(i==5){
                    //检测版本
                }
            }
        });
        listView.setPullRefreshEnable(false);
        listView.addHeaderView(headerView);
        listView.setAdapter(simpleItemAdapter);
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    private class ItemAdpter extends EnhancedAdapter<ChooseItem> {

        public ItemAdpter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            ViewHolder holder = (ViewHolder) paramView.getTag();
            ChooseItem item = getItem(position);
            holder.titleView.setText(item.getTitle());
            holder.titleView.setCompoundDrawablesWithIntrinsicBounds(item.getDrawable(), 0, 0, 0);
            holder.textView.setText(item.getText());
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.two_textview, viewgroup, false);
            ViewHolder holder = new ViewHolder();
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            rootView.setTag(holder);
            return rootView;
        }
    }

    private class ViewHolder {
        TextView titleView, textView;
    }
}
