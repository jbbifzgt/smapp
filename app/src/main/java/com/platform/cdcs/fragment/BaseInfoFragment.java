package com.platform.cdcs.fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.MockObj;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by holytang on 2017/9/28.
 */
public class BaseInfoFragment extends BaseFragment {

    private EditText chinaET, engET, proET, cityET, addressET, postET, phoneET, bakET, taxET;
    private ImageView rollBack, cxImg, manageImg, useAccount, showCode, showName, showNumber, proName, showChinaName, showlineName;

    @Override
    public void initView(View view) {
        initLoadImg(view.findViewById(R.id.load));
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
        chinaET = ViewTool.createEditItem(inflater, "分销商组中文名称", root1, false, false);
        chinaET.setHint("请输入分销商组中文名称");
        engET = ViewTool.createEditItem(inflater, "分销商组英文或拼音", root1, false, false);
        engET.setHint("请输入分销商组英文或拼音");
        proET = ViewTool.createEditItem(inflater, "省份", root1, false, true);
        proET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPro();
            }
        });
        proET.setHint("请选择省份");

        cityET = ViewTool.createEditItem(inflater, "城市", root1, false, true);
        cityET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//TODO
            }
        });
        proET.setHint("请选择城市");

        addressET = ViewTool.createEditItem(inflater, "地址", root1, false, false);
        addressET.setHint("请输入地址");
        postET = ViewTool.createEditItem(inflater, "邮政编码", root1, false, false);
        postET.setInputType(InputType.TYPE_CLASS_NUMBER);

        postET.setHint("请输入邮政编码");
        phoneET = ViewTool.createEditItem(inflater, "联系电话", root1, false, false);
        phoneET.setInputType(InputType.TYPE_CLASS_PHONE);
        phoneET.setHint("请输入联系电话");

        bakET = ViewTool.createEditItemNoLine(inflater, "备注", root1, false, false);
        bakET.setHint("请输入备注");

        LinearLayout root2 = (LinearLayout) view.findViewById(R.id.button3);
        taxET = ViewTool.createEditItem(inflater, "税率%", root2, false, false);
        taxET.setInputType(InputType.TYPE_CLASS_NUMBER);
        taxET.setHint("请输入税率");

        rollBack = ViewTool.createSwitchItem(inflater, root2, "整单回滚", true, true);
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
        cxImg = ViewTool.createSwitchItem(inflater, root2, "冲销", true, false);
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
        manageImg = ViewTool.createSwitchItem(inflater, root2, "管理库位", true, false);
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
        useAccount = ViewTool.createSwitchItem(inflater, root2, "代报时使用自定义客户", false, false);
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
        showCode = ViewTool.createSwitchItem(inflater, root3, "显示自定义客户代码", true, false);
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
        showName = ViewTool.createSwitchItem(inflater, root3, "显示自定义客户名称", true, false);
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
        showNumber = ViewTool.createSwitchItem(inflater, root3, "显示自定义产品代码", true, false);
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
        proName = ViewTool.createSwitchItem(inflater, root3, "显示自定义产品名称", true, false);
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
        showChinaName = ViewTool.createSwitchItem(inflater, root3, "显示销售单位的中文名称", true, false);
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
        showlineName = ViewTool.createSwitchItem(inflater, root3, "显示产品线的中文名称", false, false);
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
        requestDetail();
    }

    @Override
    public int layoutId() {
        return R.layout.baseinfo;
    }


    private void requestDetail() {
        showLoadImg();
        getHttpClient().post().url(Constant.DIST_MDMUD_INFO).params(Constant.makeParam(new HashMap<String, String>())).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Utils.showToast(getContext(), R.string.server_error);
                dismissLoadImg();
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                try {
                    JSONObject obj = new JSONObject(s).getJSONObject("result");
                    if ("1".equals(obj.getString("code"))) {
                        obj = obj.getJSONObject("distMdmudInfo");
                        addressET.setText(obj.getString("address"));
                        cityET.setText(obj.getString("city"));
                        chinaET.setText(obj.getString("distName"));
                        engET.setText(obj.getString("distENName"));
                        proET.setText(obj.getString("province"));
                        postET.setText(obj.getString("zipCode"));
                        phoneET.setText(obj.getString("tel"));
                        bakET.setText(obj.getString("remark"));
                        taxET.setText(obj.getString("taxRate"));
                        rollBack.setSelected(obj.getInt("docRollback") == 1);
                        cxImg.setSelected(obj.getInt("writeOff") == 1);
                        manageImg.setSelected(obj.getInt("isHouseManager") == 1);
                        useAccount.setSelected(obj.getInt("isCustomCus") == 1);
                        showCode.setSelected(obj.getInt("showCustCode") == 1);
                        showName.setSelected(obj.getInt("showCustName") == 1);
                        showNumber.setSelected(obj.getInt("showProductCode") == 1);
                        proName.setSelected(obj.getInt("showProductName") == 1);
                        showChinaName.setSelected(obj.getInt("showUomCNName") == 1);
                        showlineName.setSelected(obj.getInt("showSubBuCNName") == 1);
                    } else {
                        Utils.showToast(getContext(), obj.getString("msg"));
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void save() {
        showLoadImg();
        Map<String, String> param = new HashMap<>();
        param.put("distName", chinaET.getText().toString().trim());
        param.put("distENName", engET.getText().toString().trim());
        param.put("province", proET.getText().toString().trim());
        param.put("city", cityET.getText().toString().trim());
        param.put("address", addressET.getText().toString().trim());
        param.put("zipCode", postET.getText().toString().trim());
        param.put("tel", phoneET.getText().toString().trim());
        param.put("remark", bakET.getText().toString().trim());
        param.put("taxRate", taxET.getText().toString().trim());
        param.put("docRollback", rollBack.isSelected() ? "1" : "0");
        param.put("writeOff", cxImg.isSelected() ? "1" : "0");
        param.put("isHouseManager", manageImg.isSelected() ? "1" : "0");
        param.put("isCustomCus", useAccount.isSelected() ? "1" : "0");
        param.put("showCustCode", showCode.isSelected() ? "1" : "0");
        param.put("showCustName", showName.isSelected() ? "1" : "0");
        param.put("showProductCode", showNumber.isSelected() ? "1" : "0");
        param.put("showProductName", proName.isSelected() ? "1" : "0");
        param.put("showUomCNName", showChinaName.isSelected() ? "1" : "0");
        param.put("showSubBuCNName", showlineName.isSelected() ? "1" : "0");
        getHttpClient().post().url(Constant.EDIT_DIST_MDMUD_INFO).params(param).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Utils.showToast(getContext(), R.string.server_error);
                dismissLoadImg();
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                Type type = new TypeToken<BaseObjResponse<MockObj>>() {
                }.getType();
                BaseObjResponse<MockObj> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    Utils.showToast(getContext(), "设置成功！");
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }

    /**
     * TODO 省
     */
    private void requestPro() {
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
