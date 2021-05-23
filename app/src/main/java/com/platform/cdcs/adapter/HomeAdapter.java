package com.platform.cdcs.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.NoScrollGridView;

/**
 * Created by holytang on 2017/9/20.
 */
public class HomeAdapter extends EnhancedAdapter<ChooseItem> {

    private AdapterView.OnItemClickListener onGridItemListener;
    private View.OnClickListener moreListener;
    private ItemAdapter itemAdapter;
    private int padding;

    public HomeAdapter(Context context) {
        super(context);
        padding = Utils.convertDIP2PX(context, 10);
        itemAdapter = new ItemAdapter(context);
        String[] titles = new String[]{"新客户申请", "自定义产品", "我的客户", "库位管理", "库存快照", "代报申请"};
        int[] imgs = new int[]{R.mipmap.home_1, R.mipmap.home_2, R.mipmap.home_3, R.mipmap.home_4, R.mipmap.home_5, R.mipmap.home_6};
        for (int i = 0; i < titles.length; i++) {
            ChooseItem item = new ChooseItem();
            item.setTitle(titles[i]);
            item.setDrawable(imgs[i]);
            itemAdapter.addItem(item);
        }

    }

    public void setMoreListener(View.OnClickListener moreListener) {
        this.moreListener = moreListener;
    }

    public void setOnGridItemListener(AdapterView.OnItemClickListener onGridItemListener) {
        this.onGridItemListener = onGridItemListener;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    protected void bindView(View paramView, Context paramContext, int position) {
        int type = getItemViewType(position);
        if (type == 1) {
            ChooseItem item=getItem(position);
            ViewHolder holder=(ViewHolder)paramView.getTag();
            holder.moreView.setTag(item);
            if (position == getCount() - 1) {
                paramView.setPadding(padding, padding, padding, padding);
            } else {
                paramView.setPadding(padding, padding, padding, 0);
            }
        }

    }

    @Override
    protected View newView(Context context, int position, ViewGroup viewgroup) {
        int type = getItemViewType(position);
        View rootView = null;
        if (type == 0) {
            NoScrollGridView gridView = new NoScrollGridView(context);
            gridView.setOnItemClickListener(onGridItemListener);
            gridView.setNumColumns(4);
            gridView.setVerticalSpacing(padding);
            gridView.setPadding(0, padding, 0, padding);
            gridView.setAdapter(itemAdapter);
            rootView = gridView;
        } else {
            rootView = inflater.inflate(R.layout.tab_home_item, viewgroup, false);
            ViewHolder holder = new ViewHolder();
            holder.moreView = (TextView) rootView.findViewById(R.id.more);
            holder.moreView.setOnClickListener(moreListener);
            rootView.setTag(holder);
        }
        return rootView;
    }

    private class ItemAdapter extends EnhancedAdapter<ChooseItem> {

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            TextView textView = (TextView) paramView;
            ChooseItem item = getItem(position);
            textView.setText(item.getTitle());
            textView.setCompoundDrawablesWithIntrinsicBounds(0, item.getDrawable(), 0, 0);
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            TextView textView = new TextView(context);
            textView.setCompoundDrawablePadding(Utils.convertDIP2PX(context, 10));
            textView.setTextSize(14);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(context.getResources().getColor(R.color.text_dark));
            return textView;
        }
    }

    private class ViewHolder {
        TextView moreView;
    }
}
