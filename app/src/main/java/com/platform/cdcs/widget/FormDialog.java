package com.platform.cdcs.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.fragment.stock.ProductChooseFragment;
import com.platform.cdcs.tool.FragmentUtil;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TimeDialogBuilder;

/**
 * Created by holytang on 2017/10/5.
 */
public class FormDialog {

    private Context context;
    private Dialog mD;
    private View parentPanel;
    private EditText[] array;
    private FormListener listener;
    private String[] textArray;
    private int type;

    public FormDialog(final Context context, final FormListener listener, String title) {
        this.context = context;
        mD = new Dialog(context, R.style.IgDialog);
        this.mD.setContentView(R.layout.form_dialog);
        parentPanel = mD.findViewById(R.id.parentPanel);
        this.listener = listener;
        parentPanel.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mD.dismiss();
            }
        });
        mD.setCancelable(false);
        parentPanel.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = check();
                if (!TextUtils.isEmpty(msg)) {
                    Utils.showToast(context, msg);
                    return;
                }
                listener.okClick(getValue());
                mD.dismiss();
            }
        });
        ((TextView) mD.findViewById(R.id.title)).setText(title);
    }

    public FormDialog addView(String[] textArray, int type,String value) {
        this.type = type;
        this.textArray = textArray;
        array = new EditText[textArray.length];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Utils.getScreenWidth(context) - Utils.convertDIP2PX(context, 30), LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = Utils.convertDIP2PX(context, 16);
        lp.setMargins(0, margin, 0, 0);
        LinearLayout rootView = (LinearLayout) parentPanel.findViewById(R.id.root);
        for (int i = 0; i < textArray.length; i++) {
            String hint = textArray[i];
            final EditText editText = new EditText(context);
            if (type == 0) {
                editText.setHint("请输入" + hint);
            } else {
                editText.setHint(hint);
            }
            editText.setLayoutParams(lp);
            editText.setTextSize(14);
            editText.setTextColor(context.getResources().getColor(R.color.text_dark));
            editText.setHintTextColor(context.getResources().getColor(R.color.edit_hint));
            editText.setBackgroundResource(R.drawable.bg_rect_gray);
            editText.setPadding(margin / 2, margin / 2, margin / 2, margin / 2);
            if (hint.contains("日期")) {
                editText.setClickable(true);
                editText.setFocusableInTouchMode(false);
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String time = editText.getText().toString();
                        if (TextUtils.isEmpty(time)) {
                            time = Utils.currentTime();
                        }
                        new TimeDialogBuilder(context).showDate().setTime(time).setCallback(new TimeDialogBuilder.TimePickCallback() {
                            @Override
                            public void setTime(String time) {
                                editText.setText(time);
                            }
                        }).create().show();
                    }
                });
            } else {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }
            rootView.addView(editText);
            if(i==0){
                editText.setText(value);
                editText.setSelection(value.length());
            }
            array[i] = editText;
        }
        return this;
    }

    public void show() {
        this.mD.show();
    }

    private String[] getValue() {
        String[] data = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            data[i] = array[i].getText().toString().trim();
        }
        return data;
    }

    private String check() {
        int len = array.length;
        if (type == 1) {
            len = 2;
        }
        for (int i = 0; i < len; i++) {
            if (TextUtils.isEmpty(array[i].getText().toString().trim())) {
                return textArray[i] + "不能为空";
            }
        }
        return "";
    }

    public interface FormListener {
        public void okClick(String[] value);
    }
}
