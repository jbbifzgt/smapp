package com.platform.cdcs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.CircleTransform;
import com.squareup.picasso.Picasso;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;

import java.util.ArrayList;

/**
 * Created by holytang on 2017/9/25.
 */
public class ImgAdapter extends EnhancedAdapter<ChooseItem> {

    private int imgWidth;
    private CircleTransform transform;

    public ImgAdapter(Context context) {
        super(context);
        transform = new CircleTransform(Utils.convertDIP2PX(context, 5));
        imgWidth = (Utils.getScreenWidth(context) - Utils.convertDIP2PX(context, 10) * 4) / 3;
    }

    @Override
    protected void bindView(View paramView, Context paramContext, int position) {
        ImageView imgView = (ImageView) paramView;
        ChooseItem item = getItem(position);
        if (item.getType() == 1) {
//            .resize(imgWidth, imgWidth).centerCrop()
            Picasso.with(getContext()).load(R.mipmap.icon_img_add).into(imgView);
        } else {
            Picasso.with(getContext()).load(item.getImageurl()).resize(imgWidth, imgWidth).centerCrop().placeholder(R.mipmap.icon_img_add).transform(transform).tag("imgloader").into(imgView);
        }
    }

    @Override
    protected View newView(Context context, int position, ViewGroup viewgroup) {
        ImageView imgView = new ImageView(context);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(imgWidth, imgWidth);
        imgView.setLayoutParams(lp);
        return imgView;
    }

    public void cancelLoader() {
        Picasso.with(mContext).cancelTag("imgloader");
    }

    public ArrayList<String> getImgList() {
        ArrayList<String> imgs = new ArrayList<>();
        for (ChooseItem item : dataList) {
            if(item.getType()!=1){
                imgs.add(item.getImageurl());
            }
        }
        return imgs;
    }
}
