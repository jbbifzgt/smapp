package com.platform.cdcs.activity;

import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.platform.cdcs.R;
import com.platform.cdcs.adapter.ScanItemAdapter;
import com.platform.cdcs.fragment.TicketAddFragment;
import com.platform.cdcs.fragment.stock.AddProductFragment;
import com.platform.cdcs.fragment.stock.EditProductFragment;
import com.platform.cdcs.model.ProductList;
import com.platform.cdcs.tool.CacheTool;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.platform.cdcs.widget.CodeDemoWindow;
import com.platform.cdcs.widget.FormDialog;
import com.trueway.app.uilib.activity.BaseActivity;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/30.
 */
public class StockScanActivity extends BaseActivity implements QRCodeView.Delegate {

    private QRCodeView mQRCodeView;

    private int model;
    private int position;
    private int[] demo = new int[]{R.mipmap.unique_demo, R.mipmap.box_demo, R.mipmap.gs1_demo};
    private TextView titleView;
    private String pos2Code1 = "", pos2Code2 = "";
    private String pos3Code1 = "", pos3Code2 = "";
    private String pos1Code;
    private View.OnClickListener tipListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.button2) {
                new CodeDemoWindow(getActivity()).setImg(demo[position], view);
            } else {
                String[] forms = null;
                String title = null;
                int type = 0;
                if (position == 0) {
                    pos1Code = "";
                    forms = new String[]{"唯一码"};
                    title = "唯一码";
                } else if (position == 1) {
                    pos2Code1 = "";
                    pos2Code2 = "";
                    forms = new String[]{"运单号", "箱号"};
                    title = "箱码";
                } else {
                    pos3Code1 = "";
                    pos3Code2 = "";
                    title = "GS1码";
                    type = 1;
                    forms = new String[]{"01-[GTIN]", "10-[批号]", "11-[生产日期]", "17-[失效日期]", "20-[变种]", "20-[变种]"};
                }
                new FormDialog(getActivity(), new FormDialog.FormListener() {
                    @Override
                    public void okClick(String[] value) {
//                        addStock(value);
                        if (position == 0) {
                            pos1Code = value[0];
                            postScan();
                        } else if (position == 1) {
                            pos2Code1 = value[0];
                            pos2Code2 = value[1];
                            addStock(null);
                        } else {
                            pos3Code1 = value[0];
                            pos3Code2 = value[1];
                            addStock(null);
                        }
                    }
                }, title).addView(forms, type, "").show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = getIntent().getIntExtra("model", 0);
    }

    @Override
    protected void initLayout() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.stock_scan);
    }

    private void initTip(TextView textViews1, TextView textView2, String[] tip) {
        titleView.setText(Constant.SCAN_TITLES[position + 1]);
        textViews1.setText(tip[0]);
        textView2.setText(tip[1]);
    }

    @Override
    protected void initView() {
        initLoadImg(findViewById(R.id.load));
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setTitle("扫码添加");
        titleView = (TextView) findViewById(R.id.text);
        mQRCodeView = (QRCodeView) findViewById(R.id.zbarview);
        mQRCodeView.setDelegate(this);
        GridView tabView = (GridView) findViewById(R.id.grid);
        final ScanItemAdapter tabAdapter = new ScanItemAdapter(getActivity());
        String[] titles = new String[]{"唯一码", "箱码", "GS1码"};
        final Map<Integer, String[]> tipMap = new HashMap<>();
        tipMap.put(0, new String[]{"唯一码示例", "无法识别？"});
        tipMap.put(1, new String[]{"箱码示例", "无法识别？"});
        tipMap.put(2, new String[]{"GS1码示例", "无法识别？"});
        int[] icons = new int[]{R.mipmap.icon_code_1_normal, R.mipmap.icon_code_1_normal, R.mipmap.icon_code_2_normal};
        int[] selectedIcons = new int[]{R.mipmap.icon_code_1_press, R.mipmap.icon_code_1_press, R.mipmap.icon_code_2_press};

        for (int i = 0; i < icons.length; i++) {
            ChooseItem item = new ChooseItem();
            item.setTitle(titles[i]);
            item.setDrawable(icons[i]);
            item.setType(selectedIcons[i]);
            tabAdapter.addItem(item);
        }
        tabView.setAdapter(tabAdapter);
        final TextView tip1 = (TextView) findViewById(R.id.button2);
        final TextView tip2 = (TextView) findViewById(R.id.button3);
        tip1.setOnClickListener(tipListener);
        tip2.setOnClickListener(tipListener);
        initTip(tip1, tip2, tipMap.get(0));
        tabView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tabAdapter.setChoose(i);
                tabAdapter.notifyDataSetChanged();
                position = i;
                initTip(tip1, tip2, tipMap.get(position));
            }
        });
    }

    private void finishAdd() {
        Bundle bundle = new Bundle();
        bundle.putInt("model", model);
        FragmentUtil.navigateToInNewActivity(this, EditProductFragment.class, bundle);
        finish();
    }

    @Override
    protected void initMenu(Menu menu) {
        menu.add(0, 0, 0, "手动添加").setTitle("手动添加").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Bundle bundle = new Bundle();
                bundle.putInt("model", model);
                FragmentUtil.navigateToInNewActivity(getActivity(), AddProductFragment.class, bundle);
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
        scanResult(result);
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
        mQRCodeView.changeToScanBarcodeStyle();

        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    private void addStock(ProductList.ProductItem item) {
        LinearLayout rootView = (LinearLayout) findViewById(R.id.root);
        rootView.setVisibility(View.VISIBLE);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        rootView.removeAllViews();
        findViewById(R.id.bar).setVisibility(View.VISIBLE);
        findViewById(R.id.customPanel).setVisibility(View.GONE);
        if (position == 0) {
            ViewTool.setFourItem(inflater, rootView, new String[]{"产品名称", item.getMaterialName()});
            ViewTool.setFourItem(inflater, rootView, new String[]{"产品序列号", "产品线"}, new String[]{item.getSerialNumber(), item.getSubBU()});
            ViewTool.setFourItem(inflater, rootView, new String[]{"产品批号", "数量"}, new String[]{item.getItemCode(), item.getQty() + " " + item.getUom()});
            TextView titleView = (TextView) findViewById(R.id.bottom).findViewById(R.id.title);
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(String.format("已添加%s张", CacheTool.getInputCount(getActivity())));
            TextView btn = (TextView) findViewById(R.id.button1);
            btn.setText("结束添加");
            findViewById(R.id.bar).setBackgroundColor(getResources().getColor(R.color.orange));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int margin = Utils.convertDIP2PX(getActivity(), 12);
            lp.setMargins(margin, margin, margin, margin);
            btn.setLayoutParams(lp);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishAdd();
                }
            });
        } else if (position == 1) {
            final TextView[] tv = ViewTool.setFourItem(inflater, rootView, new String[]{"运单号", pos2Code1});
            tv[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FormDialog(getActivity(), new FormDialog.FormListener() {
                        @Override
                        public void okClick(String[] value) {
                            tv[1].setText(value[0]);
                            pos2Code1 = value[0];
                            setBtnEnable(pos2Code1, pos2Code2);
                        }
                    }, "运单号").addView(new String[]{""}, 0, pos2Code1).show();
                }
            });
            final TextView[] tv1 = ViewTool.setFourItem(inflater, rootView, new String[]{"箱号", pos2Code2});
            tv1[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FormDialog(getActivity(), new FormDialog.FormListener() {
                        @Override
                        public void okClick(String[] value) {
                            tv[1].setText(value[0]);
                            pos2Code2 = value[0];
                            setBtnEnable(pos2Code1, pos2Code2);
                        }
                    }, "箱号").addView(new String[]{""}, 0, pos2Code2).show();
                }
            });
            TextView titleView = (TextView) findViewById(R.id.bottom).findViewById(R.id.title);
            titleView.setVisibility(View.GONE);
            TextView btn = (TextView) findViewById(R.id.button1);
            btn.setText("扫描完成");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int margin = Utils.convertDIP2PX(getActivity(), 12);
            lp.setMargins(margin, margin, margin, margin);
            btn.setLayoutParams(lp);
            setBtnEnable(pos2Code1, pos2Code2);
        } else {
            final TextView[] tv = ViewTool.setFourItem(inflater, rootView, new String[]{"01-[GTIN]", "10-[批号]"}, new String[]{pos3Code1, pos3Code2}, true);
            tv[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FormDialog(getActivity(), new FormDialog.FormListener() {
                        @Override
                        public void okClick(String[] value) {
                            tv[0].setText(value[0]);
                            pos3Code1 = value[0];
                            setBtnEnable(pos3Code1, pos3Code2);
                        }
                    }, "GTIN").addView(new String[]{"GTIN"}, 0, pos3Code1).show();
                }
            });
            tv[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FormDialog(getActivity(), new FormDialog.FormListener() {
                        @Override
                        public void okClick(String[] value) {
                            tv[1].setText(value[0]);
                            pos3Code2 = value[0];
                            setBtnEnable(pos3Code1, pos3Code2);
                        }
                    }, "批号").addView(new String[]{"批号"}, 0, pos3Code2).show();
                }
            });
            ViewTool.setFourItem(inflater, rootView, new String[]{"11-[生产日期]", "17-[失效日期]"}, new String[]{"", ""});
            ViewTool.setFourItem(inflater, rootView, new String[]{"20-[变种]", "21-[唯一标识]"}, new String[]{"", ""});
            TextView titleView = (TextView) findViewById(R.id.bottom).findViewById(R.id.title);
            titleView.setVisibility(View.GONE);
            TextView btn = (TextView) findViewById(R.id.button1);
            btn.setText("扫描完成");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int margin = Utils.convertDIP2PX(getActivity(), 12);
            lp.setMargins(margin, margin, margin, margin);
            btn.setLayoutParams(lp);
            setBtnEnable(pos3Code1, pos3Code2);
        }
    }

    private void scanResult(String code) {
        if (position == 0) {
            pos1Code = code;
            postScan();
        } else if (position == 1) {
            if (code.length() == 10) {
                pos2Code1 = code;
//                Utils.showToast(getActivity(), "已扫描运单号");
            } else if (code.length() == 4) {
                pos2Code2 = code;

//                Utils.showToast(getActivity(), "已扫描箱号");
            }
//            if (!TextUtils.isEmpty(pos2Code1) && !TextUtils.isEmpty(pos2Code2)) {
//                postScan();
//            } else {
            addStock(null);
            mQRCodeView.startSpot();
//            }
        } else {
            if (code.startsWith("01")) {
                pos3Code1 = code;
            } else if (code.startsWith("17")) {
                pos3Code2 = code;
            }
            addStock(null);
            mQRCodeView.startSpot();
        }
    }

    private void postScan() {
        showLoadImg();
        Map<String, String> param = new HashMap<>();
        param.put("type", model == 0 ? "2" : "3");
        if (position == 0) {
            param.put("serialNumber", pos1Code);
        } else if (position == 1) {
            param.put("packageNumber", pos2Code2);
            param.put("deliveryNumber", pos2Code1);
        } else {
            param.put("gtin", pos3Code1);
            param.put("lots", pos3Code2);
        }
        getHttpClient().post().url(Constant.SCAN_PRODUCT).params(Constant.makeParam(param)).build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                mQRCodeView.startSpot();
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                mQRCodeView.startSpot();
                try {
//                    System.out.println(s);
                    JSONObject data = new JSONObject(s);
                    String code = "";
                    String msg = "";
                    JSONArray array = null;
                    if (data.has("result")) {
                        code = data.getJSONObject("result").getString("code");
                        msg = data.getJSONObject("result").getString("msg");
                        array = data.getJSONObject("result").getJSONArray("objList");
                    } else {
                        code = data.getString("code");
                        msg = data.has("msg") ? data.getString("msg") : "";
                        array = data.getJSONArray("objList");
                    }

                    if (!"1".endsWith(code)) {
                        Utils.showToast(getActivity(), msg);
                        return;
                    }
//                    if (position == 0) {
//                        parseOne(data.getJSONObject("result").getJSONArray("objList"));
//                    } else if (position == 1) {
//                        parseBox();
//                    } else {
//                        parseGS();
//                    }
                    parseOne(array);
                } catch (Exception e) {
                    Utils.showToast(getActivity(), "解析数据失败！");
                }
            }
        });
        resetCode();
    }

    private void resetCode() {
        pos1Code = "";
        pos2Code1 = "";
        pos2Code2 = "";
        pos3Code1 = "";
        pos3Code2 = "";
    }

    private void parseOne(JSONArray array) throws JSONException {
        ProductList.ProductItem item = null;
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            item = new ProductList.ProductItem();
            item.setMaterialName(obj.getString("materialName"));
            item.setMaterialNumber(obj.getString("materialNumber"));
            item.setSerialNumber(obj.getString("serialNumber"));
            item.setItemCode(obj.getString("batchNumber"));
            item.setSubBU(obj.getString("subBU"));
            item.setUom(obj.getString("saleUnit"));
            item.setQty(obj.getString("qty"));
//            item.setQty("1");
            item = CacheTool.append(getActivity(), item, model);
            if (i == array.length() - 1) {
                CacheTool.saveCache(getActivity(), model);
            }
        }
        if (position == 0 && item != null) {
            addStock(item);
        } else if (position == 1) {
            finishAdd();
        } else if (position == 2) {
            finishAdd();
        }
    }

    private void parseBox() {

    }

    private void parseGS() {

    }

    private void setBtnEnable(String value1, String value2) {
        if (TextUtils.isEmpty(value1) || TextUtils.isEmpty(value2)) {
            findViewById(R.id.bar).setEnabled(false);
            findViewById(R.id.bar).setBackgroundColor(getResources().getColor(R.color.text_gray));
        } else {
            findViewById(R.id.bar).setEnabled(true);
            findViewById(R.id.bar).setBackgroundResource(R.drawable.shape_corner);
        }
        findViewById(R.id.bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postScan();
            }
        });
    }
}
