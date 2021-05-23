package com.platform.cdcs.fragment;

import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.activity.TicketScanActivity;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;

/**
 * Created by holytang on 2017/9/26.
 */
public class TicketAddFragment extends BaseFragment {

    @Override
    public void initView(View view) {
        setHasOptionsMenu(true);
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("扫码添加");
        LinearLayout rootView = (LinearLayout) view.findViewById(R.id.root);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        EditText codeET = ViewTool.createEditItem(inflater, "发票代码", rootView, false, false);
        codeET.setHint("请填写发票代码");

        EditText numberET = ViewTool.createEditItem(inflater, "发票号码", rootView, true, false);
        numberET.setHint("请填写发票号码");

        EditText dateET = ViewTool.createEditItem(inflater, "发票日期", rootView, true, true);
        dateET.setHint("请选择发票日期");
        dateET.setInputType(InputType.TYPE_NULL);

        EditText moneyET = ViewTool.createEditItem(inflater, "不含税金额（¥）", rootView, true, false);
        moneyET.setHint("请输入不含税金额");

        EditText taxET = ViewTool.createEditItem(inflater, "税率（%)", rootView, true, false);
        taxET.setHint("请输入税率");

        EditText nameET = ViewTool.createEditItem(inflater, "客户名称", rootView, true, true);
        nameET.setHint("请选择客户");
        nameET.setInputType(InputType.TYPE_NULL);

        EditText bakET = ViewTool.createEditItemNoLine(inflater, "备注", rootView, true, false);
        bakET.setHint("请输入发票备注信息");

        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        TextView titleView = (TextView)view.findViewById(R.id.bottom).findViewById(R.id.title);
        titleView.setText(String.format("已添加%s张", 3));
        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAdd();
            }
        });

    }

    protected void initMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, "扫码添加").setTitle("扫码添加").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(getContext(), TicketScanActivity.class));
                getActivity().finish();
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public int layoutId() {
        return R.layout.ticket_add;
    }

    private void finishAdd() {

    }
}
