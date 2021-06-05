package com.platform.cdcs.fragment.account;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.fragment.choose.AccountChooseFragment;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.MockObj;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/28.
 */
public class AccountRegFragment extends BaseFragment {

    private String standardCode = "", standardName = "";
    private EditText nameET, codeET, accountNameET, numberET, bakET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(View view) {
        setTitle("新增客户");
        initLoadImg(view.findViewById(R.id.load));
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout rootView = (LinearLayout) view.findViewById(R.id.root);
        nameET = ViewTool.createEditItem(inflater, "标准名称", rootView, true, true);
        nameET.setInputType(InputType.TYPE_NULL);
        nameET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("class", String.valueOf(AccountRegFragment.this.getClass()));
                FragmentUtil.navigateToInNewActivity(getActivity(), AccountChooseFragment.class, bundle);
            }
        });
        nameET.setHint("请选择");
        codeET = ViewTool.createEditItem(inflater, "标准代码", rootView, true, false);
        codeET.setHint("请输入标准代码");
        codeET.setEnabled(false);
        numberET = ViewTool.createEditItem(inflater, "自定义客户代码", rootView, false, false);
        numberET.setHint("请输入自定义客户代码");

        accountNameET = ViewTool.createEditItem(inflater, "自定义客户名称", rootView, false, false);
        accountNameET.setHint("请输入自定义客户名称");

        bakET = ViewTool.createEditItemNoLine(inflater, "备注", rootView, false, false);
        bakET.setHint("请输入备注");

        LinearLayout bottomView = (LinearLayout) view.findViewById(R.id.bottom);
        bottomView.addView(inflater.inflate(R.layout.item_two_btn1, null));
        bottomView.findViewById(R.id.button1).setVisibility(View.GONE);
        TextView btn2 = (TextView) bottomView.findViewById(R.id.button2);
        btn2.setText("提交");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });

    }

    @Override
    public int layoutId() {
        return R.layout.two_layout;
    }


    private void post() {
        showLoadImg();
        //TODO
        Map<String, String> param = new HashMap<>();
        param.put("custCode", standardCode);
        param.put("custName", standardName);
        param.put("remark", bakET.getText().toString().trim());
        param.put("custCode", numberET.getText().toString().toString());
        param.put("custName", accountNameET.getText().toString().trim());

        getHttpClient().post().url(Constant.UPDATE_DIST_CUSTOMER).params(Constant.makeParam(param)).build().execute(new StringCallback() {
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
                    Utils.showToast(getContext(), "添加成功！");
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(RefershEvent event) {
        if (event.mclass == getClass()) {
            String code = event.bundle.getString("code");
            String name = event.bundle.getString("name");
            nameET.setText(name);
            codeET.setText(code);
        }
    }
}
