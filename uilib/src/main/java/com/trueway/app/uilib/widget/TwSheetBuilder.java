package com.trueway.app.uilib.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.trueway.app.uilib.R;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.tool.Utils;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Trueway on 2015/10/19.
 */
public class TwSheetBuilder {

    private Context context;
    private PopupWindow popupWindow;
    private View popupWindowView;
    private int[] imgs;

    public TwSheetBuilder(Context context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater
                .from(context);
        popupWindowView = inflater.inflate(R.layout.pick_window,
                null);
        popupWindow = new PopupWindow(popupWindowView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.popupAnimation);

        popupWindowView.findViewById(R.id.cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
    }

    public void show() {
        popupWindow.showAtLocation(popupWindowView.findViewById(R.id.cancel), Gravity.CENTER, 0, 0);
    }

    public TwSheetBuilder bindSheets(String[] titles, final DialogInterface.OnClickListener listener) {
        ItemAdapter adapter = new ItemAdapter(context, Arrays.asList(titles));
        ListView listview = (ListView) popupWindowView.findViewById(android.R.id.list);
        listview.setVisibility(View.VISIBLE);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (listener != null) {
                    listener.onClick(null, position);
                }
                dismiss();
            }
        });
        listview.setAdapter(adapter);

        return this;
    }

    public TwSheetBuilder bindSheets(int[] imgs,String[] titles, final DialogInterface.OnClickListener listener) {
        ItemAdapter adapter = new ItemAdapter(context, Arrays.asList(titles));
        this.imgs=imgs;
        ListView listview = (ListView) popupWindowView.findViewById(android.R.id.list);
        listview.setVisibility(View.VISIBLE);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (listener != null) {
                    listener.onClick(null, position);
                }
                dismiss();
            }
        });
        listview.setAdapter(adapter);

        return this;
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    private class ItemAdapter extends EnhancedAdapter<String> {

        public ItemAdapter(Context context, Collection<String> collection) {
            super(context);
            this.dataList.addAll(collection);
        }

        @Override
        protected void bindView(View paramView, Context paramContext,
                                int position) {
            String title = getItem(position);
            TextView titleView = (TextView) ((LinearLayout) paramView)
                    .getChildAt(0);
            titleView.setText(title);
            if(imgs!=null){
                titleView.setCompoundDrawablesWithIntrinsicBounds(imgs[position],0,0,0);
            }
        }

        @Override
        protected View newView(Context context, int i, ViewGroup viewgroup) {
            TextView textView = new TextView(context);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout ll = new LinearLayout(context);
            ll.setLayoutParams(lp);
            int size = Utils.convertDIP2PX(context, 10);
            LinearLayout.LayoutParams textLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(textLp);
            textView.setPadding(size,size,size,size);
            textView.setCompoundDrawablePadding(Utils.convertDIP2PX(context,20));
            try {
                int statePressed = android.R.attr.state_pressed;
                int[][] state = {{statePressed}, {-statePressed}};
                int color1 = context.getResources().getColor(R.color.title_bg);
                int color2 = context.getResources().getColor(R.color.text_black);
                int[] color = {color1, color2};
                ColorStateList listColor = new ColorStateList(state, color);
                textView.setTextColor(listColor);
            } catch (Exception e) {
                textView.setTextColor(Color.BLACK);
            }
            textView.setTextSize(14);
            textView.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            ll.addView(textView);
            ll.setBackgroundResource(R.drawable.shape_corner_center);
            return ll;
        }
    }

}
