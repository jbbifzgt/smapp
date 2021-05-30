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

import com.platform.cdcs.R;
import com.platform.cdcs.fragment.TicketAddFragment;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.activity.BaseActivity;

import cn.bingoogolapple.qrcode.core.QRCodeView;

/**
 * Created by holytang on 2017/9/27.
 */
public class TicketScanActivity extends BaseActivity implements QRCodeView.Delegate {

    private QRCodeView mQRCodeView;

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
        LinearLayout rootView = (LinearLayout) findViewById(R.id.root);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        ViewTool.setFourItem(inflater, rootView, new String[]{"发票代码", "发票号码"}, new String[]{"--", "TG1022406"});
        ViewTool.setFourItem(inflater, rootView, new String[]{"发票日期", "发票验证码"}, new String[]{"2017-03-22", "24546345634245454"});
        ViewTool.setFourItem(inflater, rootView, new String[]{"不含税金额", "税率"}, new String[]{"¥22.00", "17%"});
        TextView titleView = (TextView) findViewById(R.id.bottom).findViewById(R.id.title);
        titleView.setText(String.format("已添加%s张", 3));
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAdd();
            }
        });
    }

    private void finishAdd() {

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
        System.out.println(result + "---------------------");
        mQRCodeView.startSpot();
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
        // mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        mQRCodeView.showScanRect();
        mQRCodeView.changeToScanQRCodeStyle();

        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }
}
