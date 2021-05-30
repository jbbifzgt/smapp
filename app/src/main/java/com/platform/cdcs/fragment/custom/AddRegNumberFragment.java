package com.platform.cdcs.fragment.custom;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.fragment.choose.AccountChooseFragment;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.MockObj;
import com.platform.cdcs.model.SubBUItem;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by holytang on 2017/9/28.
 */
public class AddRegNumberFragment extends BaseFragment {

    private EditText lineET, lineNameET;

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
        nameET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.navigateToInNewActivity(getActivity(), ProSearchListFragment.class, null);
            }
        });
        nameET.setHint("请选择产品");

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
                requestUnit();
            }
        });
        unitET.setHint("请选择单位");
        LinearLayout root2 = (LinearLayout) view.findViewById(R.id.button2);
        EditText clientNameET = ViewTool.createEditItem(inflater, "客户名称", root2, false, true);
        clientNameET.setInputType(InputType.TYPE_NULL);
        clientNameET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.navigateToInNewActivity(getActivity(), AccountChooseFragment.class, null);
            }
        });
        clientNameET.setHint("请选择客户名称");

        EditText clientCodeET = ViewTool.createEditItemNoLine(inflater, "客户代码", root2, false, false);

        LinearLayout root3 = (LinearLayout) view.findViewById(R.id.button3);
        lineET = ViewTool.createEditItem(inflater, "产品线", root2, false, true);
        lineET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestSubBU();
            }
        });
        lineNameET = ViewTool.createEditItemNoLine(inflater, "产品线名称", root3, false, false);

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

    private void requestSubBU() {
        getHttpClient().post().url(Constant.SUBBU_LST).params(Constant.makeParam(new HashMap<String, String>())).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Utils.showToast(getActivity(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    Type type = new TypeToken<BaseObjResponse<SubBUItem.SubBUList>>() {
                    }.getType();
                    BaseObjResponse<SubBUItem.SubBUList> response = new Gson().fromJson(s, type);
                    if ("1".equals(response.getResult().getCode())) {
                        final List<ChooseItem> subBuList = new ArrayList<ChooseItem>();
                        ChooseItem item = new ChooseItem();
                        item.setTitle("全部产品线");
                        item.setTime("");
                        item.setIsCheck(true);
                        subBuList.add(item);
                        for (SubBUItem sub : response.getResult().getObjList()) {
                            item = new ChooseItem();
                            item.setTitle(sub.getLocalSubBUCode());
                            item.setTime(sub.getSubBU());
                            subBuList.add(item);
                        }
                        String[] array = new String[subBuList.size()];
                        for (int j = 0; j < subBuList.size(); j++) {
                            array[j] = subBuList.get(j).getTitle();
                        }
                        new TwDialogBuilder(getContext()).setItems(array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //TODO
                                lineET.setText(subBuList.get(i).getTitle());
                                lineNameET.setText(subBuList.get(i).getText());
                            }
                        }, "").create().show();
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.five_layout;
    }

    private void postClick() {
        //TODO
        showLoadImg();
        
        Map<String,String> param=new HashMap<>();
        getHttpClient().post().url(Constant.UPDATE_DIST_PRODUCT).params(Constant.makeParam(param)).build().execute(new StringCallback() {
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
