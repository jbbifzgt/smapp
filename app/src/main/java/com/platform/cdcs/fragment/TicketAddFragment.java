package com.platform.cdcs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.MyApp;
import com.platform.cdcs.R;
import com.platform.cdcs.activity.TicketScanActivity;
import com.platform.cdcs.fragment.choose.AccountChooseFragment;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.InvoiceList;
import com.platform.cdcs.model.MockObj;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.tool.CacheTool;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TimeDialogBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/26.
 */
public class TicketAddFragment extends BaseFragment {

    private EditText codeET, numberET, dateET, moneyET, taxET, nameET, bakET;
    private TextView titleView;
    private String cusType = "";
    private String cusName = "";
    private String cusCode = "";

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
        setHasOptionsMenu(true);
        initLoadImg(view.findViewById(R.id.load));
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("????????????");
        LinearLayout rootView = (LinearLayout) view.findViewById(R.id.root);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        codeET = ViewTool.createEditItem(inflater, "????????????", rootView, false, false);
        codeET.setHint("?????????????????????");

        numberET = ViewTool.createEditItem(inflater, "????????????", rootView, true, false);
        numberET.setHint("?????????????????????");

        dateET = ViewTool.createEditItem(inflater, "????????????", rootView, true, true);
        dateET.setHint("?????????????????????");
        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = dateET.getText().toString();
                if (TextUtils.isEmpty(time)) {
                    time = Utils.currentTime();
                }
                new TimeDialogBuilder(getContext()).showDate().setTime(time).setCallback(new TimeDialogBuilder.TimePickCallback() {
                    @Override
                    public void setTime(String time) {
                        dateET.setText(time);
                    }
                }).create().show();
            }
        });

        moneyET = ViewTool.createEditItem(inflater, "???????????????????????", rootView, true, false);
        moneyET.setHint("????????????????????????");
        moneyET.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        taxET = ViewTool.createEditItem(inflater, "?????????%)", rootView, true, false);
        taxET.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        taxET.setHint("???????????????");

        nameET = ViewTool.createEditItem(inflater, "????????????", rootView, false, true);
        nameET.setHint("???????????????");
        nameET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("model", 0);
                bundle.putString("class", TicketAddFragment.this.getClass().getName());
                FragmentUtil.navigateToInNewActivity(getActivity(), AccountChooseFragment.class, bundle);
            }
        });
        bakET = ViewTool.createEditItemNoLine(inflater, "??????", rootView, false, false);
        bakET.setHint("???????????????????????????");

        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInvoice();
            }
        });
        titleView = (TextView) view.findViewById(R.id.bottom).findViewById(R.id.title);
        titleView.setText(String.format("?????????%s???", CacheTool.invoiceCount(getContext())));
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postInvoice();
            }
        });

    }

    protected void initMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, "????????????").setTitle("????????????").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(getContext(), TicketScanActivity.class));
                getActivity().finish();
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public int layoutId() {
        return R.layout.ticket_add;
    }

    private void addInvoice() {
        String inCode = codeET.getText().toString().toString();
        String inNO = numberET.getText().toString().toString();
        if (TextUtils.isEmpty(inNO)) {
            Utils.showToast(getContext(), "????????????????????????!");
            return;
        }
        String inDate = dateET.getText().toString().trim();
        if (TextUtils.isEmpty(inDate)) {
            Utils.showToast(getContext(), "????????????????????????!");
            return;
        }
        String nonetaxTotal = moneyET.getText().toString().trim();
        if (TextUtils.isEmpty(nonetaxTotal)) {
            Utils.showToast(getContext(), "???????????????????????????!");
            return;
        }
        String tax = taxET.getText().toString().toString();
        if (TextUtils.isEmpty(tax)) {
            Utils.showToast(getContext(), "??????????????????!");
            return;
        }
        String inRemark = bakET.getText().toString().trim();
        InvoiceList.Invoice invoice = new InvoiceList.Invoice();
        invoice.setInNO(inNO);
        invoice.setInCode(inCode);
        invoice.setInDate(inDate);
        invoice.setNonetaxTotal(nonetaxTotal);
        invoice.setTax(tax);
        invoice.setInRemark(inRemark);
        invoice.setCusType(cusType);
        invoice.setCusCode(cusCode);
        invoice.setCusName(cusName);
        CacheTool.appendInvoice(getContext(), invoice);
        titleView.setText(String.format("?????????%s???", CacheTool.invoiceCount(getContext())));
        //????????????
        codeET.setText("");
        numberET.setText("");
        dateET.setText("");
        moneyET.setText("");
        taxET.setText("");
        bakET.setText("");
    }

    private void postInvoice() {
        List<InvoiceList.Invoice> list = CacheTool.getInvoiceList(getContext());
        if (list.size() == 0) {
            return;
        }
        showLoadImg();
        try {
            JSONObject postObj = new JSONObject();
            postObj.put("sysType", "2");
            JSONArray array = new JSONArray();
            for (InvoiceList.Invoice item : list) {
                array.put(item.toJSON());
            }
            postObj.put("invoiceList", array);
            getHttpClient().post().url(Constant.ADD_INVOICE).params(Constant.makeParam(postObj.toString())).build().execute(new StringCallback() {
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
                        CacheTool.clearInvoice(MyApp.getInstance().getApplication());
                        if (getContext() == null) {
                            return;
                        }
                        Utils.showToast(getContext(), "??????????????????");
                        titleView.setText(String.format("?????????%s???", 0));
                        //???????????????
                        RefershEvent event = new RefershEvent();
                        event.mclass = TicketFragment.class;
                        EventBus.getDefault().post(event);
                    } else {
                        Utils.showToast(getContext(), response.getResult().getMsg());
                    }
                }
            });
        } catch (Exception e) {
            dismissLoadImg();
        }
    }

    @Subscribe
    public void onEventMainThread(RefershEvent event) {
        if (event.mclass == this.getClass()) {
            cusCode = event.bundle.getString("code");
            cusName = event.bundle.getString("name");
            cusType = event.bundle.getString("cusType");
            nameET.setText(cusName);
        }
    }

}
