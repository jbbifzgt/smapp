package com.platform.cdcs.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;

/**
 * Created by holytang on 2017/10/4.
 */
public class ScanItemAdapter extends EnhancedAdapter<ChooseItem> {

    private int choose = 0;

    public ScanItemAdapter(Context context) {
        super(context);
    }

    public void setChoose(int choose) {
        this.choose = choose;
    }

    @Override
    protected void bindView(View paramView, Context paramContext, int position) {
        ChooseItem item = getItem(position);
        TextView textView = (TextView) paramView;
        textView.setText(item.getTitle());
        if (position == choose) {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, item.getType(), 0, 0);
            textView.setTextColor(paramContext.getResources().getColor(R.color.title_bg));
        } else {
            textView.setTextColor(paramContext.getResources().getColor(R.color.white));
            textView.setCompoundDrawablesWithIntrinsicBounds(0, item.getDrawable(), 0, 0);
        }

    }

    @Override
    protected View newView(Context context, int position, ViewGroup viewgroup) {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(context);
        textView.setLayoutParams(lp);
        textView.setTextSize(14);
        textView.setGravity(Gravity.CENTER);
        textView.setCompoundDrawablePadding(Utils.convertDIP2PX(context, 10));
        return textView;
    }
}
