package com.platform.cdcs.fragment.choose;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.adapter.LetterAcountAdapter;
import com.platform.cdcs.fragment.account.AccountRegFragment;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.DistCustomerList;
import com.platform.cdcs.model.ProvinceCity;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.model.XtbmItem;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.widget.PullToRefreshRecycleView;
import com.platform.cdcs.widget.TwoWheelDialog;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.fragment.SearchListener;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.LetterBar;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/28.
 */
public class AccountChooseFragment extends BaseFragment {

    private PullToRefreshRecycleView slideListView;
    private LetterBar letterBar;
    private LetterAcountAdapter adapter;
    private String custName = "";
    private String cusType = "", provience = "", city = "";
    private TextView typeTV, proET;
    private String mClass = "";
    private int model;
    private String[] typeArray;
    private Map<String, List<String>> proMap;
    private List<String> proList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new LetterAcountAdapter(getContext());
        if (getArguments() != null) {
            mClass = getArguments().getString("class");
            model = getArguments().getInt("model", 0);
            cusType = getArguments().getString("cusType", "");
        }
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
        setHasOptionsMenu(true);
        setTitle("????????????");
        if (model != 0) {
            view.findViewById(R.id.search_layout).setVisibility(View.VISIBLE);
            initSearch(view.findViewById(R.id.search), "???????????????????????????", new SearchListener() {
                @Override
                public void search(String s) {
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    custName = s;
                    requestList();
                }
            }, view.findViewById(R.id.cancel));
            view.findViewById(R.id.bar).setVisibility(View.VISIBLE);
            typeTV = (TextView) view.findViewById(R.id.title1);
            if(!TextUtils.isEmpty(cusType)){
                typeTV.setText(cusType+" ???");
            }
            typeTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestType();
                }
            });
            proET = (TextView) view.findViewById(R.id.title2);
            proET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPro();

                }

            });
        }

        final GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        adapter.setLayoutManager(manager);
        letterBar = (LetterBar) view.findViewById(R.id.letter_bar);
        letterBar.setOnLetterSelectListener(new LetterBar.OnLetterSelectListener() {
            @Override
            public void onLetterSelect(int position, String letter, boolean confirmed) {
                Integer sectionPosition = adapter.getSectionPosition(position);
                if (sectionPosition != null)
                    manager.scrollToPositionWithOffset(sectionPosition, 0);
            }
        });
        slideListView = (PullToRefreshRecycleView) view.findViewById(android.R.id.list);
        slideListView.setLayoutManager(manager);
        slideListView.setAdapter(adapter);


        adapter.setOnItemClickLitener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DistCustomerList.Customer item = adapter.get(i).getSubList().get((int) l);
                if (item == null) {
                    return;
                }
                RefershEvent event = new RefershEvent();
                try {
                    event.mclass = Class.forName(mClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                event.oclass = AccountChooseFragment.class;
                event.bundle = new Bundle();
                event.bundle.putString("cusType", item.getCusType());
                event.bundle.putString("code", item.getCustCode());
                event.bundle.putString("name", item.getCustName());
                EventBus.getDefault().post(event);
                getActivity().finish();
            }
        });
        slideListView.setAdapter(adapter);
        if (model == 0) {
            requestList();
        }
    }

    private void requestType() {
        showLoadImg();
        Map<String, String> param = new HashMap<>();
        param.put("type", "distType");
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
                    Utils.showToast(getContext(), "??????????????????");
                }
            }
        });
    }

    @Override
    protected void initMenu(Menu menu, MenuInflater inflater) {
//        menu.add(0, 0, 0, "????????????").setTitle("????????????").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                FragmentUtil.navigateToInNewActivity(getActivity(), AccountRegFragment.class, null);
//                return false;
//            }
//        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public int layoutId() {
        return R.layout.letter_list_view;
    }

    private void requestList() {
        showLoadImg();
        Map<String, String> param = new HashMap<>();
        if (model == 0) {
            getHttpClient().post().url(Constant.DIST_CUSTOMER_LST).params(Constant.makeParam(param)).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    dismissLoadImg();
                    Utils.showToast(getActivity(), R.string.server_error);
                }

                @Override
                public void onResponse(String s, int i) {
                    dismissLoadImg();
                    try {
                        Type type = new TypeToken<BaseObjResponse<DistCustomerList>>() {
                        }.getType();
                        BaseObjResponse<DistCustomerList> response = new Gson().fromJson(s, type);
                        if ("1".equals(response.getResult().getCode())) {
                            adapter.setDataModel(response.getResult().getLetterModel());
                            adapter.notifyDataSetChanged();
                        } else {
                            Utils.showToast(getContext(), response.getResult().getMsg());
                        }
                    } catch (Exception e) {
                        Utils.showToast(getContext(), "??????????????????");
                    }
                }
            });
        } else {
            if (TextUtils.isEmpty(city)) {
                Utils.showToast(getContext(), "??????????????????");
                dismissLoadImg();
                return;
            }
            param.put("custName", custName);
            param.put("cusType", cusType);
            param.put("provience", provience);
            param.put("city", city);
            getHttpClient().post().url(Constant.CUST_LST).params(Constant.makeParam(param)).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    dismissLoadImg();
                    Utils.showToast(getActivity(), R.string.server_error);
                }

                @Override
                public void onResponse(String s, int i) {
                    dismissLoadImg();
                    try {
                        JSONObject object = new JSONObject(s).getJSONObject("result");
                        String code = object.getString("code");
                        if ("1".equals(code)) {
                            JSONArray array = object.getJSONArray("distList");
                            adapter.setDataModel(DistCustomerList.parse(array));
                            adapter.notifyDataSetChanged();
                        } else {
                            Utils.showToast(getContext(), object.getString("msg"));
                        }
                    } catch (Exception e) {
                        Utils.showToast(getContext(), "??????????????????");
                    }
                }
            });
        }

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

    private void showType() {
        if (typeArray != null) {
            new TwDialogBuilder(getContext()).setItems(typeArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    typeTV.setText(typeArray[i] + " ???");
                    cusType = typeArray[i];
                    requestList();
                }
            }, "").create().show();
        } else {
            requestType();
        }
    }

    private void showProList() {
        new TwoWheelDialog(getContext(), "????????????", proList, proMap, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = (String) view.getTag();
                proET.setText(msg.split(",")[1] + " ???");
                provience = msg.split(",")[0];
                city = msg.split(",")[1];
                requestList();
            }
        }).show();
    }


}
