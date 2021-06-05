package com.platform.cdcs.tool;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.trueway.app.uilib.tool.Utils;

/**
 * Created by holytang on 2017/9/26.
 */
public class ViewTool {

    public static EditText createEditItem(LayoutInflater inflater, String title, ViewGroup parent, boolean mustFlag, boolean arrow) {
        View rootView = inflater.inflate(R.layout.item_edit, null);
        if (parent != null) {
            parent.addView(rootView);
        }
        if (mustFlag) {
            ((TextView) rootView.findViewById(R.id.title1)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.input_star, 0);
        }
        ((TextView) rootView.findViewById(R.id.title1)).setText(title);
        EditText et = (EditText) rootView.findViewById(R.id.text1);
        if (!arrow) {
            et.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            et.setInputType(InputType.TYPE_NULL);
            et.setClickable(true);
            et.setFocusableInTouchMode(false);
        }
        return et;
    }

    public static EditText createEditItemNoLine(LayoutInflater inflater, String title, ViewGroup parent, boolean mustFlag, boolean arrow) {
        View rootView = inflater.inflate(R.layout.item_edit, null);
        if (parent != null) {
            parent.addView(rootView);
        }
        if (mustFlag) {
            ((TextView) rootView.findViewById(R.id.title1)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.input_star, 0);
        }
        ((TextView) rootView.findViewById(R.id.title1)).setText(title);
        EditText et = (EditText) rootView.findViewById(R.id.text1);
        if (!arrow) {
            et.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            et.setInputType(InputType.TYPE_NULL);
            et.setClickable(true);
            et.setFocusableInTouchMode(false);
        }
        return et;
    }

    public static TextView[] createTwoBtnItem(LayoutInflater inflater, ViewGroup parent, boolean showLine) {
        View rootView = inflater.inflate(R.layout.item_two_btn, null);
        if (parent != null) {
            parent.addView(rootView);
        }
        if (showLine) {
            rootView.findViewById(R.id.line).setVisibility(View.VISIBLE);
        }
        return new TextView[]{(TextView) rootView.findViewById(R.id.button1), (TextView) rootView.findViewById(R.id.button2)};
    }

    public static ImageView createSwitchItem(LayoutInflater inflater, ViewGroup parent, String title, boolean lineFlag, boolean openFlag) {
        View rootView = inflater.inflate(R.layout.item_switch, null);
        if (parent != null) {
            parent.addView(rootView);
        }
        ((TextView) rootView.findViewById(R.id.title)).setText(title);
        ImageView imgView = (ImageView) rootView.findViewById(R.id.img);
        if (!lineFlag) {
            rootView.findViewById(R.id.line).setVisibility(View.GONE);
        }
        if (openFlag) {
            imgView.setSelected(true);
        }
        return imgView;
    }

    public static TextView[] setFourItem(LayoutInflater inflater, ViewGroup parent, String[] labels, String[] values, boolean star) {
//        View rootView = inflater.inflate(R.layout.item_four_text, null);
//        if (parent != null) {
//            parent.addView(rootView);
//        }
//        ((TextView) rootView.findViewById(R.id.title)).setText(labels[0]);
//        ((TextView) rootView.findViewById(R.id.text)).setText(values[0]);
//        ((TextView) rootView.findViewById(R.id.title1)).setText(labels[1]);
//        ((TextView) rootView.findViewById(R.id.text1)).setText(values[1]);
        View rootView = setFourItem(inflater, parent, labels, values);
        ((TextView) rootView.findViewById(R.id.title1)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.input_star, 0);
        ((TextView) rootView.findViewById(R.id.title2)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.input_star, 0);
        TextView[] tvs = new TextView[]{(TextView) rootView.findViewById(R.id.text1), (TextView) rootView.findViewById(R.id.text2)};
        return tvs;
    }

    public static View setFourItem(LayoutInflater inflater, ViewGroup parent, String[] labels, String[] values) {
        View rootView = inflater.inflate(R.layout.item_four_text, null);
        if (parent != null) {
            parent.addView(rootView);
        }
        ((TextView) rootView.findViewById(R.id.title1)).setText(labels[0]);
        ((TextView) rootView.findViewById(R.id.text1)).setText(values[0]);
        ((TextView) rootView.findViewById(R.id.title2)).setText(labels[1]);
        ((TextView) rootView.findViewById(R.id.text2)).setText(values[1]);
        return rootView;
    }


    public static TextView[] setFourItem(LayoutInflater inflater, ViewGroup parent, String[] labels) {
        View rootView = inflater.inflate(R.layout.item_four_text, null);
        if (parent != null) {
            parent.addView(rootView);
        }
        ((TextView) rootView.findViewById(R.id.title1)).setText(labels[0]);
        ((TextView) rootView.findViewById(R.id.text1)).setText(labels[1]);
        return new TextView[]{(TextView) rootView.findViewById(R.id.title1), (TextView) rootView.findViewById(R.id.text1)};
    }

    public static TextView createTitleItem(LayoutInflater inflater, LinearLayout parent) {
        View rootView = inflater.inflate(R.layout.item_edit, null);
        if (parent != null) {
            parent.addView(rootView);
        }
        rootView.findViewById(R.id.text1).setVisibility(View.GONE);
        return ((TextView) rootView.findViewById(R.id.title1));
    }

    public static View setUpDownItem(LayoutInflater inflater, LinearLayout parent, String title, String text) {
        View rootView = inflater.inflate(R.layout.up_down, null);
        if (parent != null) {
            parent.addView(rootView);
        }
        TextView titleView = (TextView) rootView.findViewById(R.id.title1);
        titleView.setTextSize(12);
        titleView.setTextColor(inflater.getContext().getResources().getColor(R.color.text_hint));
        titleView.setText(title);
        TextView textView = (TextView) rootView.findViewById(R.id.text1);
        textView.setTextSize(14);
        textView.setTextColor(inflater.getContext().getResources().getColor(R.color.text_dark));
        textView.setText(text);
        return rootView;
    }

    public static void setSixItem(LayoutInflater inflater, LinearLayout parent, String[] titles, String[] texts) {
        View rootView = inflater.inflate(R.layout.item_six_text, null);
        if (parent != null) {
            parent.addView(rootView);
        }
        ((TextView) rootView.findViewById(R.id.title1)).setText(titles[0]);
        ((TextView) rootView.findViewById(R.id.text1)).setText(texts[0]);
        ((TextView) rootView.findViewById(R.id.title2)).setText(titles[1]);
        ((TextView) rootView.findViewById(R.id.text2)).setText(texts[1]);
        ((TextView) rootView.findViewById(R.id.title3)).setText(titles[2]);
        ((TextView) rootView.findViewById(R.id.text3)).setText(texts[2]);
    }

    public static SpannableString makeQtyAndUnit(Context context, String nowqty, String qty, String unit) {
        SpannableString ss = new SpannableString(String.format("%s /%s %s", nowqty, qty, unit));
        ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.title_bg)), 0, nowqty.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static SpannableString makeArrorDown(String text, String arror) {
        SpannableString ss = new SpannableString(String.format("%s %s", text, arror));
        ss.setSpan(new RelativeSizeSpan(0.8f), text.length(), ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
