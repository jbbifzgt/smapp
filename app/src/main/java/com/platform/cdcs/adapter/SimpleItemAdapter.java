package com.platform.cdcs.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;

/**
 * Created by holytang on 2017/9/20.
 */
public class SimpleItemAdapter extends EnhancedAdapter<ChooseItem> {

    public SimpleItemAdapter(Context context) {
        super(context);
    }

    @Override
    protected void bindView(View paramView, Context paramContext, int position) {
        ChooseItem item = getItem(position);
        if (item.getType() != 0) {
            ViewHolder holder = (ViewHolder) paramView.getTag();
            if (item.getType() == 4) {
                holder.descView.setText(item.getTitle());
                holder.titleView.setText("");
                holder.textView.setText("");
                holder.rightView.setText("");
                holder.rootView.setBackgroundResource(R.drawable.shape_corner);
            } else {
                holder.titleView.setText(item.getTitle());
                if (!TextUtils.isEmpty(item.getText())) {
                    holder.textView.setText(item.getTitle());
                } else {
                    holder.textView.setText("");
                }
                if (!TextUtils.isEmpty(item.getTime())) {
                    holder.rightView.setText(item.getTime());
                } else {
                    holder.rightView.setText("");
                }
                if (item.getType() == 1) {
                    holder.rootView.setBackgroundResource(R.drawable.shape_corner_up);
                } else if (item.getType() == 2) {
                    holder.rootView.setBackgroundResource(R.drawable.shape_corner_center);
                } else if (item.getType() == 3) {
                    holder.rootView.setBackgroundResource(R.drawable.shape_corner_down);
                }
            }
            if(item.isShowRight()){
                holder.rightView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.icon_right,0);
            }else{
                holder.rightView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            }
        }
    }

    @Override
    protected View newView(Context context, int position, ViewGroup viewgroup) {
        View rootView = null;
        if (getItemViewType(position) == 0) {
            rootView = new View(context);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Utils.convertDIP2PX(context, 10));
            rootView.setLayoutParams(lp);
        } else {
            rootView = inflater.inflate(R.layout.simple_item, viewgroup, false);
            ViewHolder holder = new ViewHolder();
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            holder.descView = (TextView) rootView.findViewById(R.id.desc);
            holder.rightView = (TextView) rootView.findViewById(R.id.right_side);
            holder.rootView = rootView.findViewById(R.id.root);
            rootView.setTag(holder);
        }
        return rootView;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    private class ViewHolder {
        TextView titleView, textView, descView, rightView;
        View rootView;
    }

}
