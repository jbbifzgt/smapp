package com.platform.cdcs.fragment.stock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.fragment.operation.OperationFragment;
import com.platform.cdcs.fragment.choose.PackageChooseFragment;
import com.platform.cdcs.tool.CacheTool;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TimeDialogBuilder;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Call;

/**
 * Created by holytang on 2017/10/6.
 */
public class EditTicketFragment extends BaseFragment {

    private EditText timeET, typeET;

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
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("填写出库单");

        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout rootView1 = (LinearLayout) view.findViewById(R.id.button1);
        EditText nameET = ViewTool.createEditItem(inflater, "出库单号", rootView1, false, false);
        nameET.setHint("请填写出库单号");
        EditText addressET = ViewTool.createEditItem(inflater, "收货方", rootView1, false, true);
        addressET.setHint("请选择客户");
        addressET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        typeET = ViewTool.createEditItem(inflater, "出库类型", rootView1, false, true);
        typeET.setHint("请选择出货类型");
        typeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestType();
            }
        });

        timeET = ViewTool.createEditItem(inflater, "出库时间", rootView1, false, true);
        timeET.setHint("请选择出库时间");
        timeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = timeET.getText().toString();
                if (TextUtils.isEmpty(time)) {
                    time = Utils.currentTime();
                }
                new TimeDialogBuilder(getContext()).showDate().setTime(time).setCallback(new TimeDialogBuilder.TimePickCallback() {
                    @Override
                    public void setTime(String time) {
                        timeET.setText(time);
                    }
                }).create().show();
            }
        });
        EditText bakET = ViewTool.createEditItemNoLine(inflater, "备注", rootView1, false, false);
        bakET.setHint("请填写");
        LinearLayout rootView2 = (LinearLayout) view.findViewById(R.id.button2);
        EditText productET = ViewTool.createEditItem(inflater, "出库产品", rootView2, false, true);
        productET.setHint("请选择出库产品");
        productET.setText(String.format("已选择%s件产品", CacheTool.getOutputCount(getContext())));
        productET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });


        EditText packageET = ViewTool.createEditItem(inflater, "套装打包", rootView2, false, true);
        packageET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.navigateToInNewActivity(getActivity(), PackageChooseFragment.class, null);
            }
        });

        EditText msgET = ViewTool.createEditItemNoLine(inflater, "手术信息", rootView2, false, true);
        msgET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.navigateToInNewActivity(getActivity(), OperationFragment.class, null);
            }
        });
        view.findViewById(R.id.button3).setVisibility(View.GONE);
        view.findViewById(R.id.button4).setVisibility(View.GONE);
        view.findViewById(R.id.button5).setVisibility(View.GONE);

        TextView btn = (TextView) view.findViewById(R.id.text);
        btn.setText("完成");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishClick();
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.five_layout;
    }

    private void finishClick() {
        FragmentUtil.navigateToInNewActivity(getActivity(), FinishFragment.class, null);
    }


    /**
     * 选择出库，出库类型
     */
    private void requestType() {
        showLoadImg();
        Map<String, String> param = new HashMap<>();
        param.put("docType", "1");
        getHttpClient().post().url(Constant.DIC_XTBM).params(Constant.makeParam(param)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                new TwDialogBuilder(getContext()).setItems(new String[]{}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO
                        typeET.setText("");
                    }
                }, "");
            }
        });
    }

    @Subscribe
    public void onEventMainThread() {
    }
}
