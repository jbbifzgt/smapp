package com.trueway.app.uilib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用设配器
 *
 * @param <T>
 * @author 汤海泉
 * @date 2013-3-14
 */
public abstract class EnhancedAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> dataList;
    protected LayoutInflater inflater;
    private boolean checked;

    public EnhancedAdapter(Context context) {
        mContext = context;
        dataList = new ArrayList<T>();
        inflater=LayoutInflater.from(context);
    }

    public void addAll(List<T> items) {
//        this.dataList.clear();
        this.dataList.addAll(items);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void clear() {
        dataList.clear();
    }

    public void remove(T t) {
        dataList.remove(t);
    }

    public void addItem(T t) {
        dataList.add(t);
    }

    public void addItem(T t, int position) {
        dataList.add(position, t);
    }

    public void addItems(List<T> items) {
        dataList.addAll(items);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public T getItem(int position) {
        T t = null;
        if (position >= 0 && position < getCount()) {
            t = dataList.get(position);
        }
        return t;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getPosition(T t) {
        return dataList.indexOf(t);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = newView(mContext, position, parent);
        }
        bindView(convertView, mContext, position);
        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    /**
     * 给view附上相关属性，包括文字，图片，动画等等
     */
    protected abstract void bindView(View paramView, Context paramContext, int position);

    public Context getContext() {
        return mContext;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public boolean isEmpty() {
        return dataList.isEmpty();
    }

    /**
     * 容器view
     *
     * @param context
     * @param viewgroup
     * @return
     */
    protected abstract View newView(Context context, int position, ViewGroup viewgroup);


}
