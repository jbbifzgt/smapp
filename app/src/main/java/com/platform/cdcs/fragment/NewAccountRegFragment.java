package com.platform.cdcs.fragment;

import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.MockObj;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/27.
 */
public class NewAccountRegFragment extends BaseFragment {

    private EditText nameET, nickET, addressET, officeET, contactET, contactPhone, typeET, classET, levelET, level1ET;

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
        setTitle("申请表");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout root1 = (LinearLayout) view.findViewById(R.id.button2);
        nameET = ViewTool.createEditItem(inflater, "客户名称", root1, true, false);
        nameET.setHint("请输入客户名称");

        nickET = ViewTool.createEditItemNoLine(inflater, "客户别名", root1, false, false);
        nickET.setHint("请输入客户别名");

        LinearLayout root2 = (LinearLayout) view.findViewById(R.id.button3);
        typeET = ViewTool.createEditItem(inflater, "客户类型", root2, true, true);
        typeET.setInputType(InputType.TYPE_NULL);
        typeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestChoose(0);
            }
        });
        typeET.setHint("请选择客户类型");

        classET = ViewTool.createEditItem(inflater, "客户类别", root2, true, true);
        classET.setInputType(InputType.TYPE_NULL);
        classET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestChoose(1);
            }
        });
        classET.setHint("请选择客户类别");

        levelET = ViewTool.createEditItem(inflater, "客户分级", root2, false, true);
        levelET.setInputType(InputType.TYPE_NULL);
        levelET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestChoose(2);
            }
        });
        levelET.setHint("请选择客户分级");

        level1ET = ViewTool.createEditItemNoLine(inflater, "客户分等", root2, false, true);
        level1ET.setInputType(InputType.TYPE_NULL);
        level1ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestChoose(3);
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
                requestPro();
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

        addressET = ViewTool.createEditItem(inflater, "地址", root3, true, false);
        addressET.setHint("请输入地址");

        officeET = ViewTool.createEditItem(inflater, "客户办公电话", root3, true, false);
        officeET.setHint("请输入客户办公电话");

        contactET = ViewTool.createEditItem(inflater, "客户联系人", root3, true, false);
        contactET.setHint("请输入客户联系人");

        contactPhone = ViewTool.createEditItem(inflater, "联系人电话", root3, true, false);
        contactPhone.setHint("请输入联系人电话");
    }

    @Override
    public int layoutId() {
        return R.layout.account_reg;
    }

    private void post() {
        String msg = "";
        String name = nameET.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            msg = "客户名称不能为空";
        }
        String address = addressET.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            msg = "地址不能为空";
        }
        if (!TextUtils.isEmpty(msg)) {
            Utils.showToast(getContext(), msg);
            return;
        }
        showLoadImg();
        Map<String, String> map = new HashMap<>();

        getHttpClient().post().url(Constant.ADD_NEW_CUSTOMER_APPLY).params(map).build().execute(new StringCallback() {
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
                    getActivity().finish();
                    //通知列表刷新下
                    RefershEvent event = new RefershEvent();
                    event.mclass = NewAccountFragment.class;
                    EventBus.getDefault().post(event);
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }


    /**
     * 选择
     */
    private void requestChoose(final int type) {
        showLoadImg();
        Map<String, String> param = new HashMap<>();
        if (type == 0) {
            param.put("distType", "1");
        } else if (type == 1) {
            param.put("C2", "1");
        } else if (type == 2) {
            param.put("HPLevel", "1");
        } else {
            param.put("HPGrade", "1");
        }
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
                        if (type == 0) {
                            typeET.setText("");
                        } else if (type == 1) {
                            classET.setText("");
                        } else if (type == 2) {
                            levelET.setText("");
                        } else {
                            level1ET.setText("");
                        }
                    }
                }, "");
            }
        });
    }


    private void requestPro(){
        showLoadImg();
        getHttpClient().post().url(Constant.PROVINCE_CITY_LIST).params(new HashMap<String, String>()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
            }
        });
    }
}
