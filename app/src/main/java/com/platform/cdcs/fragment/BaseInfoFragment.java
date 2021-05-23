package com.platform.cdcs.fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;

/**
 * Created by holytang on 2017/9/28.
 */
public class BaseInfoFragment extends BaseFragment {

    @Override
    public void initView(View view) {
        setTitle("基础信息设置");
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout root1 = (LinearLayout) view.findViewById(R.id.button2);
        EditText chinaET = ViewTool.createEditItem(inflater, "分销商组中文名称", root1, false, false);
        chinaET.setHint("请输入分销商组中文名称");
        EditText engET = ViewTool.createEditItem(inflater, "分销商组英文或拼音", root1, false, false);
        chinaET.setHint("请输入分销商组英文或拼音");
        EditText proET = ViewTool.createEditItem(inflater, "省份", root1, false, true);
        proET.setEnabled(false);
        proET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    return true;
                }
                return false;
            }
        });
        proET.setHint("请选择省份");

        EditText cityET = ViewTool.createEditItem(inflater, "城市", root1, false, true);
        cityET.setEnabled(false);
        cityET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    return true;
                }
                return false;
            }
        });
        proET.setHint("请选择城市");

        EditText addressET = ViewTool.createEditItem(inflater, "地址", root1, false, false);
        addressET.setHint("请输入地址");
        EditText postET = ViewTool.createEditItem(inflater, "邮政编码", root1, false, false);
        postET.setHint("请输入邮政编码");
        EditText phoneET = ViewTool.createEditItem(inflater, "联系电话", root1, false, false);
        phoneET.setHint("请输入联系电话");

        EditText bakET = ViewTool.createEditItemNoLine(inflater, "备注", root1, false, false);
        bakET.setHint("请输入备注");

        LinearLayout root2 = (LinearLayout) view.findViewById(R.id.button3);
        EditText taxET = ViewTool.createEditItem(inflater, "税率%", root2, false, false);
        taxET.setHint("请输入税率");

        final ImageView rollBack = ViewTool.createSwitchItem(inflater, root2, "整单回滚", true, true);
        rollBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rollBack.isSelected()) {
                    rollBack.setSelected(false);
                } else {
                    rollBack.setSelected(true);
                }
            }
        });
        final ImageView cxImg = ViewTool.createSwitchItem(inflater, root2, "冲销", true, false);
        cxImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cxImg.isSelected()) {
                    cxImg.setSelected(false);
                } else {
                    cxImg.setSelected(true);
                }
            }
        });
        final ImageView manageImg = ViewTool.createSwitchItem(inflater, root2, "管理库位", true, false);
        manageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (manageImg.isSelected()) {
                    manageImg.setSelected(false);
                } else {
                    manageImg.setSelected(true);
                }
            }
        });
        final ImageView useAccount = ViewTool.createSwitchItem(inflater, root2, "代报时使用自定义客户", false, false);
        useAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (useAccount.isSelected()) {
                    useAccount.setSelected(false);
                } else {
                    useAccount.setSelected(true);
                }
            }
        });

        LinearLayout root3 = (LinearLayout) view.findViewById(R.id.button4);
        final ImageView showCode = ViewTool.createSwitchItem(inflater, root3, "显示自定义客户代码", true, false);
        showCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showCode.isSelected()) {
                    showCode.setSelected(false);
                } else {
                    showCode.setSelected(true);
                }
            }
        });
        final ImageView showName = ViewTool.createSwitchItem(inflater, root3, "显示自定义客户名称", true, false);
        showName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showName.isSelected()) {
                    showName.setSelected(false);
                } else {
                    showName.setSelected(true);
                }
            }
        });
        final ImageView showNumber = ViewTool.createSwitchItem(inflater, root3, "显示自定义产品代码", true, false);
        showNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showNumber.isSelected()) {
                    showNumber.setSelected(false);
                } else {
                    showNumber.setSelected(true);
                }
            }
        });
        final ImageView proName = ViewTool.createSwitchItem(inflater, root3, "显示自定义产品名称", true, false);
        proName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (proName.isSelected()) {
                    proName.setSelected(false);
                } else {
                    proName.setSelected(true);
                }
            }
        });
        final ImageView showChinaName = ViewTool.createSwitchItem(inflater, root3, "显示销售单位的中文名称", true, false);
        showChinaName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showChinaName.isSelected()) {
                    showChinaName.setSelected(false);
                } else {
                    showChinaName.setSelected(true);
                }
            }
        });
        final ImageView showlineName = ViewTool.createSwitchItem(inflater, root3, "显示产品线的中文名称", false, false);
        showlineName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showlineName.isSelected()) {
                    showlineName.setSelected(false);
                } else {
                    showlineName.setSelected(true);
                }
            }
        });
        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.baseinfo;
    }

    private void save() {

    }
}
