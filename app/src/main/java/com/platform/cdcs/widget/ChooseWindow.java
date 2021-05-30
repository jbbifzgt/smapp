package com.platform.cdcs.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.adapter.ImgAdapter;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;

import java.util.List;

/**
 * Created by holytang on 2017/10/4.
 */
public class ChooseWindow {
    private PopupWindow popupWindow;
    private Context context;
    private View rootView;
    private ItemAdapter adapter;
    private String choose;
    private String chooseTitle;

    public ChooseWindow(Context context) {
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.choose_layout,
                null);
        popupWindow = new PopupWindow(rootView, Utils.getScreenWidth(context),
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
    }

    public void setData(List<ChooseItem> data, String title, final View.OnClickListener okListener) {
        ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        rootView.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        rootView.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean flag = false;
                for (int i = 0; i < adapter.getCount(); i++) {
                    ChooseItem item = adapter.getItem(i);
                    if (item.isCheck()) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    Utils.showToast(context, "至少选择一个！");
                    return;
                }
                okListener.onClick(view);
            }
        });
        adapter = new ItemAdapter(context);
        adapter.addAll(data);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                for (int j = 0; j < adapter.getCount(); j++) {
                ChooseItem item = adapter.getItem(i);
                if (item.isCheck()) {
                    item.setIsCheck(false);
                } else {
                    item.setIsCheck(true);
                    if (i == 0) {
                        for (int j = 1; j < adapter.getCount(); j++) {
                            if (adapter.getItem(j).isCheck()) {
                                adapter.getItem(j).setIsCheck(false);
                            }
                        }
                    }else{
                        adapter.getItem(0).setIsCheck(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        listView.setAdapter(adapter);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    public String getChoose() {
        String choose = "";
        for (int i = 0; i < adapter.getCount(); i++) {
            ChooseItem item = adapter.getItem(i);
            if (item.isCheck()) {
                choose += item.getTime() + ",";
            }
        }
        if (!TextUtils.isEmpty(choose)) {
            choose.substring(0, choose.length() - 1);
        }
        return choose;
    }

    public void show(View view) {
        popupWindow.showAsDropDown(view);
    }

    private class ItemAdapter extends EnhancedAdapter<ChooseItem> {

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            ChooseItem item = getItem(position);
            ViewHolder holder = (ViewHolder) paramView.getTag();
            holder.titleView.setText(item.getTitle());
            holder.textView.setText(item.getText());
            holder.checkView.setSelected(item.isCheck());
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.choose_item, viewgroup, false);
            ViewHolder holder = new ViewHolder();
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            holder.checkView = (ImageView) rootView.findViewById(R.id.img);
            rootView.setTag(holder);
            return rootView;
        }

//        public ChooseItem getCheck() {
//            for (ChooseItem item : dataList) {
//                if (item.isCheck()) {
//                    return item;
//                }
//            }
//            return null;
//        }
    }

    private class ViewHolder {
        TextView titleView;
        TextView textView;
        ImageView checkView;
    }
}
