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
import com.platform.cdcs.R;
import com.platform.cdcs.fragment.account.AccountListFragment;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.HouseItem;
import com.platform.cdcs.model.RefershEvent;
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

    private EditText timeET, typeET;
    private EditText stockET;

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
        EditText nameET = ViewTool.createEditItem(inflater, "入库单号", rootView1, false, false);
        nameET.setHint("请填写出库单号");
        nameET.setText("TODO");

        EditText addressET = ViewTool.createEditItem(inflater, "发货方", rootView1, false, true);
        addressET.setHint("请选择客户");
        addressET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                FragmentUtil.navigateToInNewActivity(getActivity(), AccountListFragment.class, null);
            }
        });
        typeET = ViewTool.createEditItem(inflater, "入库类型", rootView1, false, true);
        typeET.setHint("请选择入货类型");
        typeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestType();
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

        EditText bakET = ViewTool.createEditItemNoLine(inflater, "备注", rootView1, false, false);
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

    }

    /**
     * 库位
     */
    private void showStock() {
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
                    final List<ChooseItem> items = new ArrayList<ChooseItem>();
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
                    String[] array = new String[items.size()];
                    for (int j = 0; j < items.size(); j++) {
                        array[j] = items.get(j).getTitle();
                    }
                    new TwDialogBuilder(getContext()).setItems(array, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            stockET.setText(items.get(i).getTitle());
                        }
                    }, "").create().show();
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }

            }
        });
    }

    /**
     * 选择出库，出库类型
     */
    private void requestType() {
        showLoadImg();
        Map<String, String> param = new HashMap<>();
        param.put("inType", "1");
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
