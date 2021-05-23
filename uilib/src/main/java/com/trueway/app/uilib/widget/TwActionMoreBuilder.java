package com.trueway.app.uilib.widget;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.trueway.app.uilib.R;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.tool.Utils;

import java.util.Arrays;
import java.util.Collection;

public class TwActionMoreBuilder {

    private Context mContext;
    private ListView listView;
    private PopupWindow popupWindow;

    public TwActionMoreBuilder(Context mContext) {
        this(mContext, 0);
    }

    public TwActionMoreBuilder(Context mContext, int type) {
        this.mContext = mContext;
        View view = LayoutInflater.from(mContext).inflate(R.layout.icspopmenu,
                null);
        // 设置 listview
        listView = (ListView) view.findViewById(R.id.ics_listView);
        // 宽度可以在添加完listview
        // item后用listview的measure()方法测出宽度，然后设置。这里直接写死，适配屏幕时候修改不同xml变量值就好了
        popupWindow = new PopupWindow(view, mContext.getResources()
                .getDimensionPixelSize(R.dimen.ics_popwindow_width),
                LayoutParams.WRAP_CONTENT, true);
//        if (type == 0) {
//            view.findViewById(R.id.main).setBackgroundResource(R.drawable.bg_pop);
//        } else {
//            view.findViewById(R.id.main).setBackgroundResource(R.drawable.bg_pop_left);
//        }
        // 设置Background后 按系统返回键，popupWindow会自动dismiss
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        // 点击popupwindow 外部 自动dismiss
        popupWindow.setOutsideTouchable(true);
    }

    public TwActionMoreBuilder bindAction(final ActionMore moreAction) {
        listView.setAdapter(new PopAdapter(mContext, Arrays.asList(moreAction.getItems()),moreAction.getDrawable()));
        listView.setFocusable(true);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moreAction.getItemListener().onItemClick(parent, view, position, id);
                dismiss();
            }
        });

        return this;
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    public void showAsDropDown(View parent) {
        popupWindow.showAsDropDown(parent, parent.getWidth() - mContext.getResources()
                .getDimensionPixelSize(R.dimen.ics_popwindow_width), -mContext.getResources()
                .getDimensionPixelSize(R.dimen.yoff));
        popupWindow.update();
    }

    public interface ActionMore {
        public OnItemClickListener getItemListener();

        public String[] getItems();

        public String getTitle();

        public int[] getDrawable();
    }

    private class PopAdapter extends EnhancedAdapter<String> {
        int[] drawables;
        public PopAdapter(Context context, Collection<String> collection,int[] drawables) {
            super(context);
            this.dataList.addAll(collection);
            this.drawables=drawables;
        }

        @Override
        protected void bindView(View paramView, Context paramContext,
                                int position) {
            String title = getItem(position);
            TextView titleView = (TextView) ((LinearLayout) paramView)
                    .getChildAt(0);
            titleView.setCompoundDrawablePadding(Utils.convertDIP2PX(getContext(),10));
            titleView.setCompoundDrawablesWithIntrinsicBounds(drawables[position],0,0,0);
            titleView.setText(title);
        }

        @Override
        protected View newView(Context context, int i, ViewGroup viewgroup) {
            TextView textView = new TextView(context);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout ll = new LinearLayout(context);
            ll.setLayoutParams(lp);
            ll.setGravity(Gravity.CENTER);
            int size = Utils.convertDIP2PX(context, 10);
            LinearLayout.LayoutParams textLp = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, size * 4);
            textLp.setMargins(0, size / 2, 0, size / 2);
            textView.setLayoutParams(textLp);
//            try {
//                int statePressed = android.R.attr.state_pressed;
//                int[][] state = {{statePressed}, {-statePressed}};
//                int color1 = context.getResources().getColor(R.color.title_bg);
//                int color2 = context.getResources().getColor(R.color.text_dark);
//                int[] color = {color1, color2};
//                ColorStateList listColor = new ColorStateList(state, color);
//                textView.setTextColor(listColor);
//            } catch (Exception e) {
//                textView.setTextColor(Color.BLACK);
//            }

            textView.setTextSize(14);
            textView.setTextColor(context.getResources().getColor(R.color.text_dark));
            textView.setGravity(Gravity.CENTER);
            ll.addView(textView);
            return ll;
        }
    }


}