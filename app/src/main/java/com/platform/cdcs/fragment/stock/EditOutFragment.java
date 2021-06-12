package com.platform.cdcs.fragment.stock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.MyApp;
import com.platform.cdcs.R;
import com.platform.cdcs.fragment.account.AccountListFragment;
import com.platform.cdcs.fragment.choose.AccountChooseFragment;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.HouseItem;
import com.platform.cdcs.model.ProductList;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.model.XtbmItem;
import com.platform.cdcs.tool.CacheTool;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TimeDialogBuilder;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Call;

/**
 * Created by holytang on 2017/10/20.
 */
public class EditOutFragment extends BaseFragment {

    private EditText timeET, typeET, nameET, bakET;
    private EditText stockET, addressET;
    private String[] typeArray, stockarray;
    private List<ChooseItem> items;

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
        initLoadImg(view.findViewById(R.id.load));
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("填写入库单");

        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout rootView1 = (LinearLayout) view.findViewById(R.id.button1);
        nameET = ViewTool.createEditItem(inflater, "入库单号", rootView1, false, false);
        nameET.setHint("请填写出库单号");
        nameET.setText(Constant.makeID(MyApp.getInstance().getAccount().getOrgId()));
        nameET.setEnabled(false);

        addressET = ViewTool.createEditItem(inflater, "发货方", rootView1, false, true);
        addressET.setHint("请选择客户");
        addressET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("class", EditOutFragment.this.getClass().getName());
                bundle.putInt("model", 0);
                FragmentUtil.navigateToInNewActivity(getActivity(), AccountChooseFragment.class, bundle);
            }
        });
        typeET = ViewTool.createEditItem(inflater, "入库类型", rootView1, false, true);
        typeET.setHint("请选择入库类型");
        typeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showType();
            }
        });

        timeET = ViewTool.createEditItem(inflater, "入库时间", rootView1, false, true);
        timeET.setHint("请选择入库时间");
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
        stockET = ViewTool.createEditItem(inflater, "库位", rootView1, false, true);
        stockET.setHint("请选择库位");
        stockET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStock();
            }
        });

        bakET = ViewTool.createEditItemNoLine(inflater, "备注", rootView1, false, false);
        bakET.setHint("请填写备注");

        LinearLayout rootView2 = (LinearLayout) view.findViewById(R.id.button2);

        EditText productET = ViewTool.createEditItemNoLine(inflater, "入库产品", rootView2, false, true);
        productET.setHint("请选择入库产品");
        productET.setText(String.format("已选择%s件产品", CacheTool.getInputCount(getContext())));
        productET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
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
        if (CacheTool.getInputCount(getContext()) == 0) {
            Utils.showToast(getContext(), "请先添加产品");
            return;
        }
        showLoadImg();
        try {
            JSONObject postObj = new JSONObject();
            postObj.put("docNo", nameET.getText().toString());
            postObj.put("docDate", timeET.getText().toString());
            postObj.put("distName", addressET.getText().toString());
            postObj.put("distCode", (String) addressET.getTag());
            if (!TextUtils.isEmpty(stockET.getText().toString())) {
                postObj.put("whName", stockET.getText().toString());
                postObj.put("whCode", (String) stockET.getTag());
            } else {
                postObj.put("whName", "");
                postObj.put("whCode", "");
            }
            postObj.put("docType", typeET.getText().toString());
            postObj.put("sysType", "2");
            JSONArray array = new JSONArray();
            for (ProductList.ProductItem item : CacheTool.getInputList(getContext())) {
                array.put(item.toJSON1());
            }
            postObj.put("productList", array);
            getHttpClient().post().url(Constant.WARE_HOUSE).params(Constant.makeParam(postObj.toString())).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    dismissLoadImg();
                    Utils.showToast(getContext(), R.string.server_error);
                }

                @Override
                public void onResponse(String s, int i) {
                    dismissLoadImg();

                    Bundle bundle=new Bundle();
                    bundle.putInt("model",0);
                    FragmentUtil.navigateToInNewActivity(getActivity(),FinishFragment.class,bundle);
                }
            });
        } catch (Exception e) {
            Utils.showToast(getContext(), "数据错误！");
        }
    }

    /**
     * 库位
     */
    private void requestStock() {
        showLoadImg();
        getHttpClient().post().url(Constant.DIST_WHHOUSE_LST).params(Constant.makeParam(new HashMap<String, String>())).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getActivity(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                Type type = new TypeToken<BaseObjResponse<HouseItem.HouseList>>() {
                }.getType();
                BaseObjResponse<HouseItem.HouseList> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    items = new ArrayList<ChooseItem>();
                    ChooseItem chooseItem = null;
                    for (HouseItem item : response.getResult().getHouseList()) {
                        if ("1".equals(item.getIsMainHouse())) {
                            chooseItem = new ChooseItem();
                            chooseItem.setTitle(item.getWhName());
                            chooseItem.setTime(item.getWhCode());
                            items.add(0, chooseItem);
                        } else {
                            chooseItem = new ChooseItem();
                            chooseItem.setTitle(item.getWhName());
                            chooseItem.setTime(item.getWhCode());
                            items.add(chooseItem);
                        }
                    }
                    stockarray = new String[items.size()];
                    for (int j = 0; j < items.size(); j++) {
                        stockarray[j] = items.get(j).getTitle();
                    }
                    showStock();
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }

            }
        });
    }

    private void showStock() {
        if (stockarray == null) {
            requestStock();
        } else {
            new TwDialogBuilder(getContext()).setItems(stockarray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    stockET.setText(stockarray[i]);
                    stockET.setTag(items.get(i).getTime());
                }
            }, "").create().show();
        }
    }

    /**
     * 选择出库，出库类型
     */
    private void requestType() {
        showLoadImg();
        Map<String, String> param = new HashMap<>();
        param.put("type", "inType");
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
                        typeArray = response.getResult().toArray();
                        showType();
                    } else {
                        Utils.showToast(getContext(), response.getResult().getMsg());
                    }
                } catch (Exception e) {
                    Utils.showToast(getContext(), "解析数据失败");
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(RefershEvent event) {
        if (event.mclass == this.getClass()) {
            if (event.oclass == AccountChooseFragment.class) {
                addressET.setText(event.bundle.getString("name"));
                addressET.setTag(event.bundle.getString("code"));
            }
        }
    }

    private void showType() {
        if (typeArray == null) {
            requestType();
        } else {
            new TwDialogBuilder(getContext()).setItems(typeArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    typeET.setText(typeArray[i]);
                }
            }, "").create().show();
        }
    }
}
