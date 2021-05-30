package com.platform.cdcs.fragment.stock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.activity.StockScanActivity;
import com.platform.cdcs.fragment.choose.PHFragment;
import com.platform.cdcs.fragment.choose.ProductTypeFragment;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.HouseItem;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.platform.cdcs.widget.ChooseWindow;
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
 * Created by holytang on 2017/9/30.
 */
public class AddProductFragment extends BaseFragment {

    private int model;

    private EditText unitET;
    private View titleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = getArguments().getInt("model");
        }
    }

    @Override
    public void initView(View view) {
        titleView = view.findViewById(R.id.title);
        setHasOptionsMenu(true);
        initLoadImg(view.findViewById(R.id.load));
        if (model == 0) {
            setTitle("手动添加入库产品");
        } else {
            setTitle("手动添加出库产品");
        }
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout rootView1 = (LinearLayout) view.findViewById(R.id.button1);
        EditText nameET = ViewTool.createEditItem(inflater, "产品型号", rootView1, true, true);
        nameET.setHint("请选择产品型号");
        nameET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.navigateToInNewActivity(getActivity(), ProductTypeFragment.class, null);
            }
        });

        if (model == 1) {
            EditText posET = ViewTool.createEditItem(inflater, "库位", rootView1, true, true);
            posET.setHint("请选择库位");
            posET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestStock();
                }
            });
        }

        EditText codeET = ViewTool.createEditItemNoLine(inflater, "批号", rootView1, false, true);
        codeET.setHint("请选择产品批号");
        codeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.navigateToInNewActivity(getActivity(), PHFragment.class, null);
            }
        });

        LinearLayout rootView2 = (LinearLayout) view.findViewById(R.id.button2);
        EditText beginET = ViewTool.createEditItem(inflater, "生产日期", rootView2, false, true);

        EditText endET = ViewTool.createEditItem(inflater, "失效日期", rootView2, false, true);

        unitET = ViewTool.createEditItemNoLine(inflater, "销售单位", rootView2, false, true);
        unitET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestUnit();
            }
        });
        LinearLayout rootView3 = (LinearLayout) view.findViewById(R.id.button3);
        EditText numET = ViewTool.createEditItemNoLine(inflater, model == 0 ? "入库数量" : "出库数量", rootView3, true, false);
        numET.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

        view.findViewById(R.id.button4).setVisibility(View.GONE);
        view.findViewById(R.id.button5).setVisibility(View.GONE);

        TextView btn2 = (TextView) view.findViewById(R.id.text);
        btn2.setText("完成");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });
    }

    private void post() {

    }

    @Override
    protected void initMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, "扫码添加").setTitle("扫码添加").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getContext(), StockScanActivity.class);
                startActivity(intent);
                getActivity().finish();
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public int layoutId() {
        return R.layout.five_layout;
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
            }
        });
    }

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
                    chooseItem = new ChooseItem();
                    chooseItem.setTitle("所有库位");
                    chooseItem.setTime("");
                    items.add(0, chooseItem);
                    showChooseStock(items);
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }

            }
        });
    }

    private void showChooseStock(List<ChooseItem> items) {
        final ChooseWindow window = new ChooseWindow(getContext());
        window.setData(items, "选择库位", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                String choosed = window.getChoose();
            }
        });
        window.show((View) titleView.getParent());
    }
}
