package com.platform.cdcs.fragment.stock;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.CustomerItem;
import com.platform.cdcs.model.MockObj;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by holytang on 2017/9/28.
 */
public class AddStockFragment extends BaseFragment {

    private EditText nameET, codeET, addressET, bakET;
    private ImageView setImg;

    @Override
    public void initView(View view) {
        initLoadImg(view.findViewById(R.id.load));
        setTitle("新增库位");
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout rootView = (LinearLayout) view.findViewById(R.id.root);
        nameET = ViewTool.createEditItem(inflater, "库位名称", rootView, true, false);
        nameET.setHint("请输入库位名称");
        codeET = ViewTool.createEditItem(inflater, "库位代码", rootView, true, false);
        codeET.setHint("请输入库位代码");
        addressET = ViewTool.createEditItem(inflater, "库位地址", rootView, true, false);
        addressET.setHint("请输入库位地址");

        setImg = ViewTool.createSwitchItem(inflater, rootView, "设为主库", true, false);
        setImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setImg.isSelected()) {
                    setImg.setSelected(false);
                } else {
                    setImg.setSelected(true);
                }
            }
        });
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

    private void post() {
        String name = nameET.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Utils.showToast(getContext(), "库位名称不能为空");
            return;
        }
        Map<String, String> param = new HashMap<>();
        param.put("updateType", "0");
        param.put("whName", name);
        param.put("whCode", codeET.getText().toString().trim());
        param.put("whAddress", addressET.getText().toString().trim());
        param.put("remark", bakET.getText().toString().trim());
        param.put("isMainHouse", setImg.isSelected() ? "1" : "0");
        getHttpClient().post().url(Constant.UPDATE_DIST_WHHOUSEINFO).params(Constant.makeParam(param)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getActivity(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                Type type = new TypeToken<BaseObjResponse<MockObj>>() {
                }.getType();
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.two_layout;
    }
}
