package com.platform.cdcs.fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;

/**
 * Created by holytang on 2017/9/27.
 */
public class TicketSearchFragment extends BaseFragment {

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
        EditText codeET = ViewTool.createEditItem(inflater, "客户名称", rootView, false, false);
        codeET.setHint("请输入客户名称");

        EditText numberET = ViewTool.createEditItem(inflater, "发票号码", rootView, false, false);
        numberET.setHint("请输入发票号码");


        EditText startET = ViewTool.createEditItem(inflater, "发票时间 从", rootView, false, false);
        startET.setHint("请选择日期");
        startET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });
        startET.setInputType(InputType.TYPE_NULL);
        EditText endET = ViewTool.createEditItem(inflater, "到", rootView, false, false);
        endET.setHint("请选择日期");
        endET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });
        endET.setInputType(InputType.TYPE_NULL);
        final ImageView switchView = ViewTool.createSwitchItem(inflater, rootView, "是否上传发票照片", false, true);
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
        TextView btn1=(TextView)bottomView.findViewById(R.id.button1);
        btn1.setText("重置");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //重置
            }
        });
        TextView btn2=(TextView)bottomView.findViewById(R.id.button2);
        btn2.setText("查询");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查询
                getActivity().finish();
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.two_layout;
    }
}
