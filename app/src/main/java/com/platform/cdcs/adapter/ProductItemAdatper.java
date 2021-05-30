package com.platform.cdcs.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.fragment.stock.ProductDetailFragment;
import com.platform.cdcs.model.ProductList;
import com.platform.cdcs.tool.FragmentUtil;
import com.trueway.app.uilib.adapter.EnhancedAdapter;

/**
 * Created by holytang on 2017/10/29.
 */
public class ProductItemAdatper extends EnhancedAdapter<ProductList.ProductItem> {

    private View.OnClickListener itemListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ProductList.ProductItem item = getItem((int) view.getTag(R.mipmap.ic_launcher));
            Bundle bundle = new Bundle();
            bundle.putSerializable("model", item);
            FragmentUtil.navigateToInNewActivity(getContext(), ProductDetailFragment.class, bundle);
        }
    };

    public ProductItemAdatper(Context context) {
        super(context);
    }

    @Override
    protected void bindView(View paramView, Context paramContext, int position) {
        ViewHolder holder = (ViewHolder) paramView.getTag();
        ProductList.ProductItem item = getItem(position);
        holder.titleView.setText(item.getMaterialNumber());
        holder.textView.setText(item.getMaterialName());
        holder.dateView.setText(String.format("%s %s", item.getProductNum(), item.getDefaultUnit()));
        paramView.setTag(R.mipmap.ic_launcher, position);
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
        rootView.setTag(holder);
        return rootView;
    }

    private class ViewHolder {
        TextView titleView, dateView, textView;
    }
}
