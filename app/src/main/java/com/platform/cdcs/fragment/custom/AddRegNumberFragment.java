package com.platform.cdcs.fragment.custom;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;

/**
 * Created by holytang on 2017/9/28.
 */
public class AddRegNumberFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        setTitle("新增产品");
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout root1 = (LinearLayout) view.findViewById(R.id.button1);
        EditText nameET = ViewTool.createEditItem(inflater, "标准名称", root1, true, true);
        nameET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    FragmentUtil.navigateToInNewActivity(getActivity(), ProSearchListFragment.class, null);
                    return true;
                }
                return false;
            }
        });
        nameET.setHint("请选择产品");
        nameET.setInputType(InputType.TYPE_NULL);

        EditText codeET = ViewTool.createEditItem(inflater, "标准代码", root1, true, false);

        EditText code1ET = ViewTool.createEditItem(inflater, "自定义产品代码", root1, true, false);
        code1ET.setHint("请输入自定义产品代码");

        EditText name1ET = ViewTool.createEditItem(inflater, "自定义产品名称", root1, true, false);
        name1ET.setHint("请输入自定义产品名称");

        EditText unitET = ViewTool.createEditItemNoLine(inflater, "默认单位", root1, true, false);
        unitET.setInputType(InputType.TYPE_NULL);
        unitET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        unitET.setHint("请选择单位");
        LinearLayout root2 = (LinearLayout) view.findViewById(R.id.button2);
        EditText clientNameET = ViewTool.createEditItem(inflater, "客户名称", root2, false, true);
        clientNameET.setInputType(InputType.TYPE_NULL);
        clientNameET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        clientNameET.setHint("请选择客户名称");

        EditText clientCodeET = ViewTool.createEditItemNoLine(inflater, "客户代码", root2, false, false);

        LinearLayout root3 = (LinearLayout) view.findViewById(R.id.button3);
        EditText lineET = ViewTool.createEditItem(inflater, "产品线", root2, false, false);
        EditText lineNameET = ViewTool.createEditItemNoLine(inflater, "产品线名称", root3, false, false);

        LinearLayout root4 = (LinearLayout) view.findViewById(R.id.button4);
        EditText addressET = ViewTool.createEditItem(inflater, "产地", root4, false, false);
        addressET.setHint("请输入产地");

        EditText methodET = ViewTool.createEditItem(inflater, "灭菌方式", root4, false, false);
        methodET.setHint("请输入灭菌方式");

        EditText mCodeET = ViewTool.createEditItem(inflater, "医保编号", root4, false, false);
        mCodeET.setHint("请输入医保编号");

        EditText mNumberET = ViewTool.createEditItem(inflater, "医院物料代码", root4, false, false);
        mNumberET.setHint("请输入医院物料代码");

        EditText mUnit = ViewTool.createEditItemNoLine(inflater, "医院物料单位", root4, false, false);
        mUnit.setHint("请输入医院物料单位");

        LinearLayout root5 = (LinearLayout) view.findViewById(R.id.button5);
        EditText bakEt = ViewTool.createEditItemNoLine(inflater, "备注", root5, false, false);
        bakEt.setHint("请输入备注");

        view.findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postClick();
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.product_reg;
    }

    private void postClick() {

    }
}
