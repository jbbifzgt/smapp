package com.platform.cdcs.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.wheelview.OnWheelChangedListener;
import com.cncoderx.wheelview.WheelView;
import com.platform.cdcs.R;
import com.trueway.app.uilib.tool.Utils;

import java.util.List;
import java.util.Map;

/**
 * Created by holytang on 2017/10/31.
 */
public class TwoWheelDialog {

    private Context context;
    private Dialog mD;

    public TwoWheelDialog(Context context, String title, List<String> array, final Map<String, List<String>> map, final View.OnClickListener listener) {
        this.context = context;
        mD = new Dialog(context, R.style.IgDialog);
        this.mD.setContentView(R.layout.two_wheel_dialog);
        mD.findViewById(R.id.parentPanel);
        mD.setCancelable(false);
        ((TextView) mD.findViewById(R.id.title)).setText(title);
        final WheelView proView = (WheelView) mD.findViewById(R.id.text1);
        proView.setEntries(array.toArray(new String[array.size()]));
        final WheelView cityView = (WheelView) mD.findViewById(R.id.text2);
        proView.setOnWheelChangedListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldIndex, int newIndex) {
                String text = (String) proView.getItem(newIndex);
                List<String> sub = map.get(text);
                cityView.setEntries(sub.toArray(new String[sub.size()]));
            }
        });
        List<String> sub = map.get(array.get(0));
        cityView.setEntries(sub.toArray(new String[sub.size()]));
        mD.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mD.dismiss();
            }
        });
        mD.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setTag(proView.getCurrentItem() + "," + cityView.getCurrentItem());
                listener.onClick(view);
                mD.dismiss();
            }
        });
    }

    public void show() {
        mD.show();
    }
}
