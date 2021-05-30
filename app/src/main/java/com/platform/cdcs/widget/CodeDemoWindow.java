package com.platform.cdcs.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.platform.cdcs.R;
import com.trueway.app.uilib.tool.Utils;

/**
 * Created by holytang on 2017/10/4.
 */
public class CodeDemoWindow {
    private PopupWindow popupWindow;
    private Context context;
    private ImageView imgView;

    public CodeDemoWindow(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.demo_layout,
                null);
        imgView = (ImageView) view.findViewById(R.id.img);
        popupWindow = new PopupWindow(view, Utils.getScreenWidth(context),
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
    }

    public void setImg(int drawable, View view) {
        imgView.setImageResource(drawable);
        popupWindow.showAsDropDown(view);

    }
}
