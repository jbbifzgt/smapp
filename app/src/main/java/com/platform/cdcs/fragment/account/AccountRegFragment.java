package com.platform.cdcs.fragment.account;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;

/**
 * Created by holytang on 2017/9/28.
 */
public class AccountRegFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        setTitle("新增客户");
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout rootView = (LinearLayout) view.findViewById(R.id.root);
        EditText nameET = ViewTool.createEditItem(inflater, "标准名称", rootView, true, true);
        nameET.setInputType(InputType.TYPE_NULL);
        nameET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        nameET.setHint("请选择");
        EditText codeET = ViewTool.createEditItem(inflater, "标准代码", rootView, true, false);
        codeET.setHint("请输入标准代码");

        EditText numberET = ViewTool.createEditItem(inflater, "自定义客户代码", rootView, false, false);
        numberET.setHint("请输入自定义客户代码");

        EditText accountNameET = ViewTool.createEditItem(inflater, "自定义客户名称", rootView, false, false);
        accountNameET.setHint("请输入自定义客户名称");

        EditText bakET = ViewTool.createEditItemNoLine(inflater, "备注", rootView, false, false);
        bakET.setHint("请输入备注");

        LinearLayout bottomView = (LinearLayout) view.findViewById(R.id.bottom);
        bottomView.addView(inflater.inflate(R.layout.item_two_btn1, null));
        bottomView.findViewById(R.id.button1).setVisibility(View.GONE);
        TextView btn2 = (TextView) bottomView.findViewById(R.id.button2);
        btn2.setText("提交");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int layoutId() {
        return R.layout.two_layout;
    }
}
