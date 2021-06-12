package com.platform.cdcs.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by holytang on 2017/10/31.
 */
public class PullToRefreshRecycleView extends RecyclerView{
    public PullToRefreshRecycleView(Context context) {
        super(context);
    }

    public PullToRefreshRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
