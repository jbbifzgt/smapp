package com.platform.cdcs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;

import org.w3c.dom.Text;

/**
 * Created by holytang on 2017/9/24.
 */
public class NewAccountFragment extends BaseFragment {

    private ItemAdapter adapter;
    private ObservableXListView slideListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(getContext());
        ChooseItem item = new ChooseItem();
        item.setTitle("海安县人民医院");
        item.setType(1);
        adapter.addItem(item);
    }

    @Override
    public void initView(View view) {
        setHasOptionsMenu(true);
        hideThisToolBar(view);
        setTitle("新客户申请");
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        view.findViewById(R.id.search_layout).setVisibility(View.VISIBLE);
        slideListView = (ObservableXListView) view.findViewById(android.R.id.list);
        slideListView.setAdapter(adapter);
    }

    protected void initMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, "添加").setIcon(R.mipmap.icon_add).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentUtil.navigateToInNewActivity(getActivity(), NewAccountRegFragment.class, null);
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
            ViewHoder holder = (ViewHoder) paramView.getTag();
            ChooseItem item = getItem(position);
            holder.titleView.setText(item.getTitle());
//            if(item.getType()){

//            }
            holder.textView.setText("已申请");
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.account_item, viewgroup, false);
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
