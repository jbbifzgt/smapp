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
import com.platform.cdcs.model.ProvinceCity;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.model.XtbmItem;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/27.
 */
public class NewAccountRegFragment extends BaseFragment {

    private EditText nameET, nickET, addressET, officeET, contactET, contactPhone, typeET, classET, levelET, level1ET, proET, cityET;
    private String[] typeArray, classArray, levelArray, level1Array;
    private Map<String, List<String>> proMap;
    private List<String> proList;

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
        typeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showType();
            }
        });
        typeET.setHint("请选择客户类型");

        classET = ViewTool.createEditItem(inflater, "客户类别", root2, true, true);
        classET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClass();
            }
        });
        classET.setHint("请选择客户类别");

        levelET = ViewTool.createEditItem(inflater, "客户分级", root2, false, true);
        levelET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLevel();
            }
        });
        levelET.setHint("请选择客户分级");

        level1ET = ViewTool.createEditItemNoLine(inflater, "客户分等", root2, false, true);
        level1ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLevel1();
            }
        });
        level1ET.setHint("请选择客户分等");
        LinearLayout root3 = (LinearLayout) view.findViewById(R.id.button4);
        proET = ViewTool.createEditItem(inflater, "省份", root3, false, true);
        proET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPro();
            }
        });
        proET.setHint("请选择省份");
        cityET = ViewTool.createEditItem(inflater, "城市", root3, false, true);
        cityET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCityList();
            }
        });
        cityET.setHint("请选择城市");

//        EditText areaET = ViewTool.createEditItem(inflater, "区县", root3, false, true);
//        areaET.setInputType(InputType.TYPE_NULL);
//        areaET.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        areaET.setHint("请选择区县");

        addressET = ViewTool.createEditItem(inflater, "地址", root3, true, false);
        addressET.setHint("请输入地址");

        officeET = ViewTool.createEditItem(inflater, "客户办公电话", root3, false, false);
        officeET.setInputType(InputType.TYPE_CLASS_PHONE);
        officeET.setHint("请输入客户办公电话");

        contactET = ViewTool.createEditItem(inflater, "客户联系人", root3, false, false);
        contactET.setHint("请输入客户联系人");

        contactPhone = ViewTool.createEditItem(inflater, "联系人电话", root3, false, false);
        contactPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        contactPhone.setHint("请输入联系人电话");

        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });
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
        String type = typeET.getText().toString().trim();
        if (TextUtils.isEmpty(type)) {
            msg = "客户类型不能为空";
        }
        String mClass = classET.getText().toString().trim();
        if (TextUtils.isEmpty(mClass)) {
            msg = "客户类别不能为空";
        }
        String pro = proET.getText().toString().trim();
        if (TextUtils.isEmpty(pro)) {
            msg = "省份不能为空";
        }
        String city = cityET.getText().toString().trim();
        if (TextUtils.isEmpty(city)) {
            msg = "城市不能为空";
        }
        String address = addressET.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            msg = "地址不能为空";
        }
        if (!TextUtils.isEmpty(msg)) {
            Utils.showToast(getContext(), msg);
            return;
        }
        showLoadImg();
        Map<String, String> map = new HashMap<>();
        map.put("cusName", name);
        map.put("cusSubName", nickET.getText().toString().trim());
        map.put("cusType", type);
        map.put("cusCate", classET.getText().toString());
        map.put("level", levelET.getText().toString());
        map.put("grade", level1ET.getText().toString());
        map.put("province", pro);
        map.put("city", city);
        map.put("country", "");
        map.put("address", address);
        map.put("telephone", officeET.getText().toString().trim());
        map.put("contacts", contactET.getText().toString().trim());
        map.put("contactsNo", contactPhone.getText().toString().trim());
        map.put("flag", "");
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
            param.put("type", "distType");
        } else if (type == 1) {
            param.put("type", "C2");
        } else if (type == 2) {
            param.put("type", "HPLevel");
        } else {
            param.put("type", "HPGrade");
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
                try {
                    Type mtype = new TypeToken<BaseObjResponse<XtbmItem.XtbmList>>() {
                    }.getType();
                    BaseObjResponse<XtbmItem.XtbmList> response = new Gson().fromJson(s, mtype);
                    if ("1".equals(response.getResult().getCode())) {
                        if (type == 0) {
                            typeArray = response.getResult().toArray();
                            showType();
                        } else if (type == 1) {
                            classArray = response.getResult().toArray();
                            showClass();
                        } else if (type == 2) {
                            levelArray = response.getResult().toArray();
                            showLevel();
                        } else {
                            level1Array = response.getResult().toArray();
                            showLevel1();
                        }
                    } else {
                        Utils.showToast(getContext(), response.getResult().getMsg());
                    }
                } catch (Exception e) {
                    Utils.showToast(getContext(), "解析数据失败");
                }
            }
        });
    }

    private void requestPro() {
        if (proList != null) {
            showProList();
            return;
        }
        showLoadImg();
        getHttpClient().post().url(Constant.PROVINCE_CITY_LIST).params(Constant.makeParam(new HashMap<String, String>())).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                Type type = new TypeToken<BaseObjResponse<ProvinceCity.ProvinceCityList>>() {
                }.getType();
                BaseObjResponse<ProvinceCity.ProvinceCityList> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    proMap = response.getResult().proMap();
                    proList = response.getResult().getProList();
                    showProList();
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }

    private void showProList() {
        new TwDialogBuilder(getContext()).setItems(proList.toArray(new String[proList.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String pro = proList.get(i);
                proET.setText(pro);
                cityET.setText("");
            }
        }, "").create().show();
    }

    private void showCityList() {
        final String pro = proET.getText().toString();
        if (TextUtils.isEmpty(pro)) {
            Utils.showToast(getContext(), "请先选择省份");
            return;
        }
        if (proMap.containsKey(pro) && proMap.get(pro).size() > 0) {
            new TwDialogBuilder(getContext()).setItems(proMap.get(pro).toArray(new String[proMap.get(pro).size()]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String city = proMap.get(pro).get(i);
                    cityET.setText(city);
                }
            }, "").create().show();
        }
    }

    private void showType() {
        if (typeArray != null) {
            new TwDialogBuilder(getContext()).setItems(typeArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    typeET.setText(typeArray[i]);
                }
            }, "").create().show();
            ;
        } else {
            requestChoose(0);
        }
    }

    private void showClass() {
        if (classArray != null) {
            new TwDialogBuilder(getContext()).setItems(classArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    classET.setText(classArray[i]);
                }
            }, "").create().show();
            ;
        } else {
            requestChoose(1);
        }
    }

    private void showLevel() {
        if (levelArray != null) {
            new TwDialogBuilder(getContext()).setItems(levelArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    levelET.setText(levelArray[i]);
                }
            }, "").create().show();
        } else {
            requestChoose(2);
        }
    }

    private void showLevel1() {
        if (level1Array != null) {
            new TwDialogBuilder(getContext()).setItems(level1Array, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    level1ET.setText(level1Array[i]);
                }
            }, "").create().show();
            ;
        } else {
            requestChoose(3);
        }
    }
}
