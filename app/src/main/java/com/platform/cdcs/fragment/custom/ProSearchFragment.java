package com.platform.cdcs.fragment.custom;

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
 * Created by holytang on 2017/9/28.
 */
public class ProSearchFragment extends BaseFragment {

    @Override
    public void initView(View view) {
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("产品查询");
        LinearLayout rootView = (LinearLayout) view.findViewById(R.id.root);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        EditText codeET = ViewTool.createEditItem(inflater, "产品代码", rootView, false, false);
        codeET.setHint("请输入产品代码");

        EditText lineET = ViewTool.createEditItem(inflater, "产品线", rootView, false, true);
        lineET.setInputType(InputType.TYPE_NULL);
        lineET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        lineET.setHint("请选择产品线");


        EditText l3ET = ViewTool.createEditItem(inflater, "Level3", rootView, false, false);
        l3ET.setHint("请输入Level3");

        EditText l4ET = ViewTool.createEditItem(inflater, "Level4", rootView, false, false);
        l4ET.setHint("请输入Level4");

        EditText l5ET = ViewTool.createEditItemNoLine(inflater, "Level5", rootView, false, false);
        l5ET.setHint("请输入Level5");

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
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.two_layout;
    }
}
