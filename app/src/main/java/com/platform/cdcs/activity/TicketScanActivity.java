package com.platform.cdcs.activity;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.MyApp;
import com.platform.cdcs.R;
import com.platform.cdcs.fragment.TicketAddFragment;
import com.platform.cdcs.fragment.TicketFragment;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.InvoiceList;
import com.platform.cdcs.model.MockObj;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.tool.CacheTool;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.activity.BaseActivity;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/27.
 */
public class TicketScanActivity extends BaseActivity implements QRCodeView.Delegate {

    private QRCodeView mQRCodeView;
    private TextView titleView;
    private LinearLayout rootView;

    @Override
    protected void initLayout() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.ticket_scan);

    }

    @Override
    protected void initView() {
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setTitle("扫码添加");
        mQRCodeView = (QRCodeView) findViewById(R.id.zbarview);
        mQRCodeView.setDelegate(this);
        TextView tipView = (TextView) findViewById(R.id.text);
        tipView.setText(Constant.SCAN_TITLES[0]);
        rootView = (LinearLayout) findViewById(R.id.root);
        rootView.setVisibility(View.GONE);
        titleView = (TextView) findViewById(R.id.bottom).findViewById(R.id.title);
        titleView.setText(String.format("已添加%s张", CacheTool.invoiceCount(getActivity())));
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postInvoice();
            }
        });
    }

    @Override
    protected void initMenu(Menu menu) {
        menu.add(0, 0, 0, "手动添加").setTitle("手动添加").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentUtil.navigateToInNewActivity(getActivity(), TicketAddFragment.class, null);
                finish();
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        try {
            String[] array = result.split(",");
            if (array.length >= 8) {
                InvoiceList.Invoice invoice = new InvoiceList.Invoice();
                invoice.setInCode(array[2]);
                invoice.setInNO(array[3]);
                invoice.setNonetaxTotal(array[4]);
                invoice.setInDate(array[5]);
                invoice.setVerifyCode(array[6]);
                showInvoice(invoice);
                mQRCodeView.startSpotDelay(1000);
            }
        } catch (Exception e) {
            Utils.showToast(this, "识别发票二维码失败!");
            mQRCodeView.startSpotDelay(1000);
        }
    }

    private void showInvoice(InvoiceList.Invoice invoice) {
        rootView.setVisibility(View.VISIBLE);
        rootView.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        ViewTool.setFourItem(inflater, rootView, new String[]{"发票代码", "发票号码"}, new String[]{invoice.getInCode(), invoice.getInNO()});
        ViewTool.setFourItem(inflater, rootView, new String[]{"发票日期", "发票验证码"}, new String[]{invoice.getInNO(), invoice.getVerifyCode()});
        ViewTool.setFourItem(inflater, rootView, new String[]{"不含税金额", "税率"}, new String[]{"¥" + invoice.getNonetaxTotal(), invoice.getTax() + "%"});

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, "相机启动失败，请确认相机是否正常！", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        mQRCodeView.showScanRect();
        mQRCodeView.changeToScanQRCodeStyle();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    private void postInvoice() {
        List<InvoiceList.Invoice> list = CacheTool.getInvoiceList(getActivity());
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
                    Utils.showToast(getActivity(), R.string.server_error);
                }

                @Override
                public void onResponse(String s, int i) {
                    dismissLoadImg();
                    Type type = new TypeToken<BaseObjResponse<MockObj>>() {
                    }.getType();
                    BaseObjResponse<MockObj> response = new Gson().fromJson(s, type);
                    if ("1".equals(response.getResult().getCode())) {
                        CacheTool.clearInvoice(MyApp.getInstance().getApplication());
                        Utils.showToast(getActivity(), "发票添加成功");
                        rootView.setVisibility(View.GONE);
                        rootView.removeAllViews();
                        titleView.setText(String.format("已添加%s张", CacheTool.invoiceCount(getActivity())));
                        //刷新下主页
                        RefershEvent event = new RefershEvent();
                        event.mclass = TicketFragment.class;
                        EventBus.getDefault().post(event);
                    } else {
                        Utils.showToast(getActivity(), response.getResult().getMsg());
                    }
                }
            });
        } catch (Exception e) {
            dismissLoadImg();
        }
    }
}
