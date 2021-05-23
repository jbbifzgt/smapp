package com.trueway.app.uilib.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
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
public class TwTopGridBuilder {

    private Context context;
    private PopupWindow popupWindow;
    private View popupWindowView;
    //TODO
//    private int selectIndex = -1;
    private int selectIndex = 0;
    private int numColumn = 4;

    public TwTopGridBuilder(Context context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater
                .from(context);
        popupWindowView = inflater.inflate(R.layout.top_grid_window,
                null);
        popupWindow = new PopupWindow(popupWindowView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    public void show(View view) {
//        popupWindow.showAtLocation(popupWindowView.findViewById(android.R.id.list), Gravity.CENTER, 0,0);
        popupWindow.showAsDropDown(view);
    }

    public TwTopGridBuilder setGridNum(int num) {
        GridView gridView = (GridView) popupWindowView.findViewById(android.R.id.list);
        gridView.setNumColumns(num);
        this.numColumn = num;
        return this;
    }

    public TwTopGridBuilder setSelected(int index) {
        this.selectIndex = index;
        return this;
    }

    public TwTopGridBuilder bindSheets(String title, String[] titles, final DialogInterface.OnClickListener listener) {
        if (!TextUtils.isEmpty(title)) {
            TextView titleView = (TextView) popupWindowView.findViewById(R.id.title);
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(title);
        }
        ItemAdapter adapter = new ItemAdapter(context, Arrays.asList(titles));
        GridView gridView = (GridView) popupWindowView.findViewById(android.R.id.list);
        gridView.setVisibility(View.VISIBLE);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (listener != null) {
                    listener.onClick(null, position);
                }
                dismiss();
            }
        });
        gridView.setAdapter(adapter);

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
            ViewHolder vHolder = (ViewHolder) paramView.getTag();
            vHolder.titleView.setText(title);
//            if ((position + 1) % numColumn == 0) {
//                vHolder.lineView.setVisibility(View.GONE);
//            } else {
//                vHolder.lineView.setVisibility(View.VISIBLE);
//            }
            if (position == selectIndex) {
                vHolder.titleView.setSelected(true);
            } else {
                vHolder.titleView.setSelected(false);
            }
        }

        @Override
        protected View newView(Context context, int i, ViewGroup viewgroup) {
            View view = LayoutInflater.from(context).inflate(R.layout.top_grid_item, null);
            ViewHolder holder = new ViewHolder();
            holder.titleView = (TextView) view.findViewById(R.id.title);
            holder.titleView.setBackgroundResource(R.drawable.btn_gray_side);
            holder.lineView = view.findViewById(R.id.line);
            holder.lineView.setVisibility(View.GONE);
            int padding = Utils.convertDIP2PX(context,10);
            view.findViewById(R.id.img).setVisibility(View.GONE);
            holder.titleView.setPadding(padding,padding/2,padding,padding/2);
            view.setTag(holder);
            return view;
        }

        private class ViewHolder {
            TextView titleView;
            View lineView;
        }
    }

}
