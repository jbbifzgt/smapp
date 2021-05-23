package com.platform.cdcs.fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;

/**
 * Created by holytang on 2017/9/27.
 */
public class NewAccountRegFragment extends BaseFragment {
    @Override
    public void initView(View view) {
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("申请表");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout root1 = (LinearLayout) view.findViewById(R.id.button2);
        EditText nameET = ViewTool.createEditItem(inflater, "客户名称", root1, true, false);
        nameET.setHint("请输入客户名称");

        EditText nickET = ViewTool.createEditItemNoLine(inflater, "客户别名", root1, false, false);
        nickET.setHint("请输入客户别名");

        LinearLayout root2 = (LinearLayout) view.findViewById(R.id.button3);
        EditText typeET = ViewTool.createEditItem(inflater, "客户类型", root2, true, true);
        typeET.setInputType(InputType.TYPE_NULL);
        typeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        typeET.setHint("请选择客户类型");

        EditText classET = ViewTool.createEditItem(inflater, "客户类别", root2, true, true);
        classET.setInputType(InputType.TYPE_NULL);
        classET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        classET.setHint("请选择客户类别");

        EditText levelET = ViewTool.createEditItem(inflater, "客户分级", root2, false, true);
        levelET.setInputType(InputType.TYPE_NULL);
        levelET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        levelET.setHint("请选择客户分级");

        EditText level1ET = ViewTool.createEditItemNoLine(inflater, "客户分等", root2, false, true);
        level1ET.setInputType(InputType.TYPE_NULL);
        level1ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        level1ET.setHint("请选择客户分等");
        LinearLayout root3 = (LinearLayout) view.findViewById(R.id.button4);
        EditText proviceET = ViewTool.createEditItem(inflater, "省份", root3, false, true);
        proviceET.setInputType(InputType.TYPE_NULL);
        proviceET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        proviceET.setHint("请选择省份");
        EditText cityET = ViewTool.createEditItem(inflater, "城市", root3, false, true);
        cityET.setInputType(InputType.TYPE_NULL);
        cityET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cityET.setHint("请选择城市");
        EditText areaET = ViewTool.createEditItem(inflater, "区县", root3, false, true);
        areaET.setInputType(InputType.TYPE_NULL);
        areaET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        areaET.setHint("请选择区县");

        EditText addressET = ViewTool.createEditItem(inflater, "地址", root3, true, false);
        addressET.setHint("请输入地址");

        EditText officeET = ViewTool.createEditItem(inflater, "客户办公电话", root3, true, false);
        officeET.setHint("请输入客户办公电话");

        EditText contactET = ViewTool.createEditItem(inflater, "客户联系人", root3, true, false);
        contactET.setHint("请输入客户联系人");

        EditText contactPhone = ViewTool.createEditItem(inflater, "联系人电话", root3, true, false);
        contactPhone.setHint("请输入联系人电话");
    }

    @Override
    public int layoutId() {
        return R.layout.account_reg;
    }
}
