package com.platform.cdcs.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.fragment.stock.ProductDetailFragment;
import com.platform.cdcs.model.ProductList;
import com.platform.cdcs.tool.FragmentUtil;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.imgpick.ImageSet;

/**
 * Created by holytang on 2017/10/29.
 */
public class ProductItemAdatper extends EnhancedAdapter<ProductList.ProductItem> {
    private int model;

    private View.OnClickListener itemListener;

    public ProductItemAdatper(Context context) {
        super(context);
    }

    public void setItemListener(View.OnClickListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setModel(int model) {
        this.model = model;
    }

    @Override
    protected void bindView(View paramView, Context paramContext, int position) {
        ViewHolder holder = (ViewHolder) paramView.getTag();
        ProductList.ProductItem item = getItem(position);
        holder.titleView.setText(item.getMaterialNumber());
        holder.textView.setText(item.getMaterialName());
        holder.dateView.setText(String.format("%s %s", item.getProductNum(), item.getDefaultUnit()));
        paramView.setTag(R.mipmap.ic_launcher, item);
        if (model == 1) {
            holder.checkView.setSelected(item.isChoose());
        }
    }

    @Override
    protected View newView(Context context, int position, ViewGroup viewgroup) {
        View rootView = inflater.inflate(R.layout.four_item, viewgroup, false);
        rootView.setBackgroundResource(R.drawable.shape_corner_center);
        rootView.setOnClickListener(itemListener);
        ViewHolder holder = new ViewHolder();
        holder.titleView = (TextView) rootView.findViewById(R.id.title);
        holder.dateView = (TextView) rootView.findViewById(R.id.time);
        holder.textView = (TextView) rootView.findViewById(R.id.text);
        holder.textView.setSingleLine(false);
        holder.textView.setMaxLines(2);
        if (model == 1) {
            holder.checkView = (ImageView) rootView.findViewById(R.id.img);
        }
        rootView.setTag(holder);
        return rootView;
    }

    public String getChooseCode() {
        String code = "";
        for (ProductList.ProductItem item : dataList) {
            if (item.isChoose()) {
                code += item.getItemCode() + ",";
            }
        }
        if (!TextUtils.isEmpty(code)) {
            code = code.substring(0, code.length() - 1);
        }
        return code;
    }

    public boolean isExistUnCheck() {
        boolean existUnCheck = false;
        for (ProductList.ProductItem item : dataList) {
            if (!item.isChoose()) {
                existUnCheck = true;
            }
        }
        return existUnCheck;
    }

    public void chooseAll() {
        for (ProductList.ProductItem item : dataList) {
            item.setChoose(true);
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView titleView, dateView, textView;
        ImageView checkView;
    }
}
