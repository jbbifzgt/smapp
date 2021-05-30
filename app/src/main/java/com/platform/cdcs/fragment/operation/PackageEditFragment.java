package com.platform.cdcs.fragment.operation;

import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by holytang on 2017/10/6.
 */
public class PackageEditFragment extends BaseFragment {
    @Override
    public void initView(View view) {
        initLoadImg(view.findViewById(R.id.load));
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("套装打包");

        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout rootView1 = (LinearLayout) view.findViewById(R.id.button1);
        EditText typeET = ViewTool.createEditItem(inflater, "套包型号", rootView1, false, true);
        typeET.setHint("请选择套包型号");
        typeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestType();
            }
        });

        EditText numET = ViewTool.createEditItem(inflater, "套包数量", rootView1, false, false);
        numET.setHint("请填写套包数量");
        numET.setInputType(InputType.TYPE_CLASS_NUMBER);
        EditText unitET = ViewTool.createEditItemNoLine(inflater, "套包单位", rootView1, false, true);
        unitET.setHint("请选择套包单位");
        unitET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestUnit();
            }
        });
        view.findViewById(R.id.button2).setVisibility(View.GONE);
        view.findViewById(R.id.button3).setVisibility(View.GONE);
        view.findViewById(R.id.button4).setVisibility(View.GONE);
        view.findViewById(R.id.button5).setVisibility(View.GONE);
        TextView btn = (TextView) view.findViewById(R.id.text);
        btn.setText("完成");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void finishClick() {

    }

    @Override
    public int layoutId() {
        return R.layout.five_layout;
    }

    private void requestType() {
        showLoadImg();
        Map<String, String> map = new HashMap<>();
        map.put("reqType", "2");
        getHttpClient().post().url(Constant.DIC_URL).params(Constant.makeParam(map)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
//                String[] items=new String[]{"CNBAE0026","CNBAE0026","CNBAE0026","CNBAE0026"};
//                new TwDialogBuilder(getContext()).setItems("选择套包型号",items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                }).create().show();
            }
        });
    }

    private void requestUnit() {
        showLoadImg();
        Map<String, String> map = new HashMap<>();
        map.put("reqType", "3");
        getHttpClient().post().url(Constant.DIC_URL).params(Constant.makeParam(map)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
//                String[] items=new String[]{"箱(CA)","打(DZ)","盒(BX)","包(PK)","ZNF","CC","个(EA)"};
//                new TwDialogBuilder(getContext()).setItems("选择单位",items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                }).create().show();
            }
        });
    }
}
