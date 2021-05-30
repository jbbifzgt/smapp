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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.activity.TicketScanActivity;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.MockObj;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/26.
 */
public class TicketAddFragment extends BaseFragment {

    private EditText codeET, numberET, dateET, moneyET, taxET, nameET, bakET;
    private TextView titleView;
    private int count;

    @Override
    public void initView(View view) {
        setHasOptionsMenu(true);
        initLoadImg(view.findViewById(R.id.load));
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("手动添加");
        LinearLayout rootView = (LinearLayout) view.findViewById(R.id.root);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        codeET = ViewTool.createEditItem(inflater, "发票代码", rootView, false, false);
        codeET.setHint("请填写发票代码");

        numberET = ViewTool.createEditItem(inflater, "发票号码", rootView, true, false);
        numberET.setHint("请填写发票号码");

        dateET = ViewTool.createEditItem(inflater, "发票日期", rootView, true, true);
        dateET.setHint("请选择发票日期");
        dateET.setInputType(InputType.TYPE_NULL);

        moneyET = ViewTool.createEditItem(inflater, "不含税金额（¥）", rootView, true, false);
        moneyET.setHint("请输入不含税金额");

        taxET = ViewTool.createEditItem(inflater, "税率（%)", rootView, true, false);
        taxET.setHint("请输入税率");

        nameET = ViewTool.createEditItem(inflater, "客户名称", rootView, false, true);
        nameET.setHint("请选择客户");
        nameET.setInputType(InputType.TYPE_NULL);

        bakET = ViewTool.createEditItemNoLine(inflater, "备注", rootView, false, false);
        bakET.setHint("请输入发票备注信息");

        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });
        titleView = (TextView) view.findViewById(R.id.bottom).findViewById(R.id.title);
        titleView.setText(String.format("已添加%s张", count));
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
        getActivity().finish();
    }

    private void post() {
        String inCode = codeET.getText().toString().toString();
        String inNO = numberET.getText().toString().toString();
        String cusType = "";
        String cusName = "";
        String cusCode = "";
        String nonetaxTotal = moneyET.getText().toString().trim();
        String taxTotal = "";
        String inDate = dateET.getText().toString().trim();
        String inRemark = bakET.getText().toString().trim();
        String tax = taxET.getText().toString().toString();
        showLoadImg();
        Map<String, String> map = new HashMap<>();
        map.put("inCode", inCode);
        map.put("inNO", inNO);
        map.put("cusType", cusType);
        map.put("cusName", cusName);
        map.put("cusCode", cusCode);
        map.put("nonetaxTotal", nonetaxTotal);
        map.put("taxTotal", taxTotal);
        map.put("inDate", inDate);
        map.put("inRemark", inRemark);
        map.put("tax", tax);
        getHttpClient().post().url(Constant.ADD_INVOICE).params(Constant.makeParam(map)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                Type type = new TypeToken<BaseObjResponse<MockObj>>() {
                }.getType();
                BaseObjResponse<MockObj> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    Utils.showToast(getContext(), "发票添加成功");
                    codeET.setText("");
                    numberET.setText("");
                    dateET.setText("");
                    moneyET.setText("");
                    taxET.setText("");
                    nameET.setText("");
                    bakET.setText("");
                    count++;
                    titleView.setText(String.format("已添加%s张", count));
                    //刷新下主页
                    RefershEvent event = new RefershEvent();
                    event.mclass = TicketFragment.class;
                    EventBus.getDefault().post(event);
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }
}
