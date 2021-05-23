package com.platform.cdcs.fragment.custom;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;

/**
 * Created by holytang on 2017/9/28.
 */
public class RegNumberFragment extends BaseFragment {

    private ItemAdapter adapter;
    private ObservableXListView slideListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(getContext());
    }

    @Override
    public void initView(View view) {
        hideThisToolBar(view);
        setHasOptionsMenu(true);
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("注册证号");
        slideListView = (ObservableXListView) view.findViewById(android.R.id.list);
        slideListView.setPullRefreshEnable(false);
        slideListView.setAdapter(adapter);
    }

    @Override
    protected void initMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, "新增注册证号").setTitle("新增注册证号").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentUtil.navigateToInNewActivity(getActivity(), AddRegNumberFragment.class, null);
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            ViewHolder holder = new ViewHolder();
            LinearLayout parentView = new LinearLayout(getContext());
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
            parentView.setLayoutParams(lp);

            LinearLayout rootView = new LinearLayout(getContext());
            LinearLayout.LayoutParams rootLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rootView.setBackgroundResource(R.drawable.shape_corner);
            rootView.setOrientation(LinearLayout.VERTICAL);
            int padding = Utils.convertDIP2PX(getContext(), 10);
            rootLP.setMargins(padding, padding, padding, 0);
            rootView.setLayoutParams(rootLP);
            parentView.addView(rootView);

            holder.titleView = ViewTool.createTitleItem(inflater, rootView);
            holder.titleView.setTextSize(16);
            holder.titleView.setTextColor(getResources().getColor(R.color.text_black));
            holder.titleView.getPaint().setFakeBoldText(true);
            TextView[] arrayTV = ViewTool.setFourItem(inflater, rootView, new String[]{"批号", "创建时间"});
            holder.codeView = arrayTV[0];
            holder.createTime = arrayTV[1];
            arrayTV = ViewTool.setFourItem(inflater, rootView, new String[]{"有效开始日期", "有效结束日期"});
            holder.beginTime = arrayTV[0];
            holder.endTime = arrayTV[1];
            arrayTV = ViewTool.setFourItem(inflater, rootView, new String[]{"修改时间", "修改人"});
            holder.modifyTime = arrayTV[0];
            holder.modifyTime.setTextColor(getResources().getColor(R.color.text_red));
            holder.modifyPerson = arrayTV[1];
            holder.modifyPerson.setTextColor(getResources().getColor(R.color.title_bg));

            TextView[] btns = ViewTool.createTwoBtnItem(inflater, rootView, false);
            holder.delBtn = btns[0];
            holder.modifyBtn = btns[1];
            parentView.setTag(holder);
            return parentView;
        }
    }

    private class ViewHolder {
        TextView titleView, codeView, createTime, beginTime, endTime, modifyTime, modifyPerson, delBtn, modifyBtn;
    }
}
