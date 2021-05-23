package com.platform.cdcs.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.activity.TicketScanActivity;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

/**
 * Created by holytang on 2017/9/20.
 */
public class TicketFragment extends BaseFragment {

    private ObservableXListView slideListView;
    private ItemAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(getContext());
        adapter.addItem(new ChooseItem());
    }

    @Override
    public void initView(View view) {
        initToolBar(view);
        toolbar.setNavigationIcon(R.mipmap.icon_search);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.navigateToInNewActivity(getActivity(), TicketSearchFragment.class, null);
            }
        });
        toolbar.getMenu().add(0, 0, 0, "新增发票").setTitle("新增发票").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(getContext(), TicketScanActivity.class));
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        setFragmentTitle("发票");
        RelativeLayout headerView = new RelativeLayout(getContext());
        headerView.setBackgroundColor(getResources().getColor(R.color.header_bg));
        AbsListView.LayoutParams llp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        headerView.setLayoutParams(llp);
        int padding = Utils.convertDIP2PX(getContext(), 7);
        headerView.setPadding(padding * 2, padding, padding * 2, padding);
        TextView textView = new TextView(getContext());
        textView.setText("发票号码/客户");
        textView.setTextSize(12);
        textView.setTextColor(getResources().getColor(R.color.text_gray));
        headerView.addView(textView);

        textView = new TextView(getContext());
        RelativeLayout.LayoutParams tLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        textView.setLayoutParams(tLP);
        textView.setText("发票日期");
        textView.setTextSize(12);
        textView.setTextColor(getResources().getColor(R.color.text_gray));
        headerView.addView(textView);
        slideListView = (ObservableXListView) view.findViewById(android.R.id.list);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentUtil.navigateToInNewActivity(getActivity(), TicketDetailFragment.class, null);
            }
        });
        slideListView.addHeaderView(headerView);
        slideListView.setAdapter(adapter);
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    private void removeTicket(){

    }

    private class ItemAdapter extends EnhancedAdapter<ChooseItem> implements View.OnClickListener {

        private AdapterView.OnItemClickListener onItemClickListener;

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            ViewHoder holder = (ViewHoder) paramView.getTag();
            ChooseItem item = getItem(position);
//            holder.titleView.setText(item.getTitle());
//            if(item.getType()){

//            }
            holder.titleView.setText("33679412");
            holder.textView.setText("海安县人民医院");
            holder.timeView.setText("2017-09-12");
//            holder.descView.setTextColor(getResources().getColor(R.color.text_hint));
            holder.descView.setTextColor(getResources().getColor(R.color.color_green));
            holder.descView.setText("已拍照");
            holder.removeView.setTag(position);
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            SwipeHorizontalMenuLayout rootView = (SwipeHorizontalMenuLayout) inflater.inflate(R.layout.ticket_item_group, viewgroup, false);
            ViewHoder holder = new ViewHoder();
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            holder.timeView = (TextView) rootView.findViewById(R.id.time);
            holder.descView = (TextView) rootView.findViewById(R.id.desc);
            holder.removeView = (TextView) rootView.findViewById(R.id.smMenuViewRight);
            holder.removeView.setOnClickListener(this);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   onItemClickListener.onItemClick(null,view,0,0);
                }
            });
            rootView.setTag(holder);
            return rootView;
        }

        @Override
        public void onClick(View view) {
            new TwDialogBuilder(getContext()).setTitle(R.string.attention).setMessage("确定要作废吗？").setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    removeTicket();
                }
            }).setPositiveButton(R.string.cancel_text,null).create().show();
        }

        public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    private class ViewHoder {
        TextView titleView, textView, timeView, descView, removeView;
    }
}
