package com.platform.cdcs.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TimeDialogBuilder;

/**
 * Created by holytang on 2017/9/27.
 */
public class TicketSearchFragment extends BaseFragment {

    private EditText codeET, numberET, startET, endET;
    private ImageView switchView;

    @Override
    public void initView(View view) {
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("发票查询");
        LinearLayout rootView = (LinearLayout) view.findViewById(R.id.root);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        codeET = ViewTool.createEditItem(inflater, "客户名称", rootView, false, false);
        codeET.setHint("请输入客户名称");

        numberET = ViewTool.createEditItem(inflater, "发票号码", rootView, false, false);
        numberET.setHint("请输入发票号码");


        startET = ViewTool.createEditItem(inflater, "发票时间 从", rootView, false, true);
        startET.setHint("请选择日期");
        startET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String time = startET.getText().toString();
                if (TextUtils.isEmpty(time)) {
                    time = Utils.currentTime();
                }
                new TimeDialogBuilder(getContext()).showDate().setTime(time).setCallback(new TimeDialogBuilder.TimePickCallback() {
                    @Override
                    public void setTime(String time) {
                        if (!TextUtils.isEmpty(endET.getText().toString()) && startET.getText().toString().compareTo(endET.getText().toString()) > 0) {
                            Utils.showToast(getContext(), "开始时间不能大于结束时间!");
                            return;
                        }
                        startET.setText(time);
                    }
                }).create().show();
            }
        });
        endET = ViewTool.createEditItem(inflater, "到", rootView, false, true);
        endET.setHint("请选择日期");
        endET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String time = endET.getText().toString();
                if (TextUtils.isEmpty(time)) {
                    time = Utils.currentTime();
                }
                new TimeDialogBuilder(getContext()).showDate().setTime(time).setCallback(new TimeDialogBuilder.TimePickCallback() {
                    @Override
                    public void setTime(String time) {
                        if (!TextUtils.isEmpty(startET.getText().toString()) && endET.getText().toString().compareTo(startET.getText().toString()) < 0) {
                            Utils.showToast(getContext(), "结束时间不能小于开始时间!");
                            return;
                        }
                        endET.setText(time);
                    }
                }).create().show();
            }
        });
        switchView = ViewTool.createSwitchItem(inflater, rootView, "是否上传发票照片", false, true);
        switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchView.isSelected()) {
                    switchView.setSelected(false);
                } else {
                    switchView.setSelected(true);
                }
            }
        });

        LinearLayout bottomView = (LinearLayout) view.findViewById(R.id.bottom);
        bottomView.addView(inflater.inflate(R.layout.item_two_btn1, null));
        TextView btn1 = (TextView) bottomView.findViewById(R.id.button1);
        btn1.setText("重置");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //重置
                codeET.setText("");
                numberET.setText("");
                startET.setText("");
                endET.setText("");
            }
        });
        TextView btn2 = (TextView) bottomView.findViewById(R.id.button2);
        btn2.setText("查询");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("cusName", codeET.getText().toString().trim());
                bundle.putString("inNO", numberET.getText().toString().trim());
                bundle.putString("startTime", startET.getText().toString().trim());
                bundle.putString("endTime", endET.getText().toString().trim());
                bundle.putString("havePic", switchView.isSelected() ? "1" : "0");
                FragmentUtil.navigateToInNewActivity(getActivity(), TicketFragment.class, bundle);
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.two_layout;
    }
}
