package com.platform.cdcs.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.cdcs.MyApp;
import com.platform.cdcs.R;
import com.platform.cdcs.model.NoticeList;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.NoScrollGridView;

/**
 * Created by holytang on 2017/9/20.
 */
public class HomeAdapter extends EnhancedAdapter<NoticeList.NoticeItem> {

    private AdapterView.OnItemClickListener onGridItemListener;
    private View.OnClickListener moreListener, lookListener;
    private ItemAdapter itemAdapter;
    private int padding;
    private int[] imgs = new int[]{R.mipmap.home_type_1, R.mipmap.home_type_4, R.mipmap.home_type_2, R.mipmap.home_type_3, R.mipmap.home_type_1};
    private String[] titles = new String[]{"日常提醒", "到货通知", "出入库上报", "发票上报", "日常提醒"};

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

    public View.OnClickListener getLookListener() {
        return lookListener;
    }

    public void setLookListener(View.OnClickListener lookListener) {
        this.lookListener = lookListener;
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
            NoticeList.NoticeItem item = getItem(position);
            ViewHolder holder = (ViewHolder) paramView.getTag();
            holder.moreView.setTag(item);
            if (position == getCount() - 1) {
                paramView.setPadding(padding, padding, padding, padding);
            } else {
                paramView.setPadding(padding, padding, padding, 0);
            }
            holder.imgView.setImageResource(imgs[item.getMsgType()]);
            holder.titleView.setText(titles[item.getMsgType()]);
            if(!TextUtils.isEmpty(item.getSend_date())&&item.getSend_date().length()>18){
                holder.timeView.setText(item.getSend_date().substring(5,17));
            }else{
                holder.timeView.setText("");
            }
            holder.desc.setText(item.getMsgInfo());
            if(item.getMsgType()==1){
                holder.button3.setVisibility(View.VISIBLE);
                holder.button1.setVisibility(View.VISIBLE);
                holder.button2.setVisibility(View.GONE);
                holder.button1.setText("单号："+item.getTopicId());
            }else if(item.getMsgType()==2||item.getMsgType()==3){
                holder.button3.setVisibility(View.VISIBLE);
                holder.button1.setVisibility(View.VISIBLE);
                holder.button2.setVisibility(View.VISIBLE);
                holder.button1.setText("文件编号："+item.getId());
                holder.button2.setText("上报人："+ MyApp.getInstance().getAccount().getUserName());
            }else{
                holder.button3.setVisibility(View.GONE);
                holder.button1.setVisibility(View.GONE);
                holder.button2.setVisibility(View.GONE);
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
            holder.button1 = (TextView) rootView.findViewById(R.id.button1);
            holder.button2 = (TextView) rootView.findViewById(R.id.button2);
            holder.button3 = (TextView) rootView.findViewById(R.id.button3);
            holder.button3.setOnClickListener(lookListener);
            holder.button3.setText("立即查看");
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            holder.timeView = (TextView) rootView.findViewById(R.id.time);
            holder.desc = (TextView) rootView.findViewById(R.id.desc);
            holder.imgView = (ImageView) rootView.findViewById(R.id.img);
            rootView.setTag(holder);
        }
        return rootView;
    }

    public void removeList() {
        for (int i = dataList.size() - 1; i > 0; i--) {
            dataList.remove(i);
        }
        notifyDataSetChanged();
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
        TextView moreView, titleView, timeView, desc, button1, button2, button3;
        ImageView imgView;
    }
}
