package com.platform.cdcs.tool;

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
            ((TextView) rootView.findViewById(R.id.title)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        ((TextView) rootView.findViewById(R.id.title)).setText(title);
        EditText et = (EditText) rootView.findViewById(R.id.text);
        if (!arrow) {
            et.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        return et;
    }

    public static EditText createEditItemNoLine(LayoutInflater inflater, String title, ViewGroup parent, boolean mustFlag, boolean arrow) {
        View rootView = inflater.inflate(R.layout.item_edit, null);
        if (parent != null) {
            parent.addView(rootView);
        }
        if (mustFlag) {
            ((TextView) rootView.findViewById(R.id.title)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        ((TextView) rootView.findViewById(R.id.title)).setText(title);
        EditText et = (EditText) rootView.findViewById(R.id.text);
        if (!arrow) {
            et.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
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

    public static void setFourItem(LayoutInflater inflater, ViewGroup parent, String[] labels, String[] values) {
        View rootView = inflater.inflate(R.layout.item_four_text, null);
        if (parent != null) {
            parent.addView(rootView);
        }
        ((TextView) rootView.findViewById(R.id.title)).setText(labels[0]);
        ((TextView) rootView.findViewById(R.id.text)).setText(values[0]);
        ((TextView) rootView.findViewById(R.id.title1)).setText(labels[1]);
        ((TextView) rootView.findViewById(R.id.text1)).setText(values[1]);
    }

    public static TextView[] setFourItem(LayoutInflater inflater, ViewGroup parent, String[] labels) {
        View rootView = inflater.inflate(R.layout.item_four_text, null);
        if (parent != null) {
            parent.addView(rootView);
        }
        ((TextView) rootView.findViewById(R.id.title)).setText(labels[0]);
        ((TextView) rootView.findViewById(R.id.title1)).setText(labels[1]);
        return new TextView[]{(TextView) rootView.findViewById(R.id.text), (TextView) rootView.findViewById(R.id.text1)};
    }

    public static TextView createTitleItem(LayoutInflater inflater, LinearLayout parent) {
        View rootView = inflater.inflate(R.layout.item_edit, null);
        if (parent != null) {
            parent.addView(rootView);
        }
        rootView.findViewById(R.id.text).setVisibility(View.GONE);
        return ((TextView) rootView.findViewById(R.id.title));
    }
}
