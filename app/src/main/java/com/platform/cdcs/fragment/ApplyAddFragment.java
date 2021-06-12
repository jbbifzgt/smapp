package com.platform.cdcs.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.fragment.choose.AccountChooseFragment;
import com.platform.cdcs.fragment.choose.DealerChooseFragment;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.MockObj;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.model.XtbmItem;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/28.
 */
public class ApplyAddFragment extends BaseFragment {

    private EditText bakET, nameET, addressET;
    private ImageView setImg;
    private String[] typeArray;

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
        setTitle("申请表");
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout rootView = (LinearLayout) view.findViewById(R.id.root);
        nameET = ViewTool.createEditItem(inflater, "所属机构类型", rootView, false, true);
        nameET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showType();
            }
        });
        nameET.setHint("请选择所属机构类型");

        addressET = ViewTool.createEditItem(inflater, "所属机构", rootView, true, true);
        addressET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("class", ApplyAddFragment.this.getClass().getName());
                FragmentUtil.navigateToInNewActivity(getActivity(), DealerChooseFragment.class, bundle);

            }
        });
        addressET.setHint("请选择所属机构");
        setImg = ViewTool.createSwitchItem(inflater, rootView, "合并库存", true, false);
        setImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setImg.isSelected()) {
                    setImg.setSelected(false);
                } else {
                    setImg.setSelected(true);
                }
            }
        });
        bakET = ViewTool.createEditItemNoLine(inflater, "备注", rootView, false, false);
        bakET.setHint("请输入备注");
        LinearLayout bottomView = (LinearLayout) view.findViewById(R.id.bottom);
        bottomView.addView(inflater.inflate(R.layout.item_two_btn1, null));
        bottomView.findViewById(R.id.button1).setVisibility(View.GONE);
        TextView btn2 = (TextView) bottomView.findViewById(R.id.button2);
        btn2.setText("提交");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });

    }

    private void post() {
        if (TextUtils.isEmpty(addressET.getText().toString())) {
            Utils.showToast(getContext(), "所属机构不能为空");
            return;
        }
        showLoadImg();
        Map<String, String> param = new HashMap<>();
        param.put("orgType", nameET.getText().toString());
        param.put("distApplyCode", (String) addressET.getTag());
        param.put("isMerge", setImg.isSelected() ? "1" : "0");
        param.put("remark", bakET.getText().toString().trim());
        getHttpClient().post().url(Constant.INSERT_OTHERR_EPORT_INFO).params(Constant.makeParam(param)).build().execute(new StringCallback() {
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
                    Utils.showToast(getContext(), "添加成功！");
                    RefershEvent event = new RefershEvent();
                    event.mclass = ApplyAddFragment.class;
                    EventBus.getDefault().post(event);
                    getActivity().finish();
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });

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
                    Utils.showToast(getContext(), "解析数据失败");
                }
            }
        });
    }

    private void showType() {
        if (typeArray != null) {
            new TwDialogBuilder(getContext()).setItems(typeArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    nameET.setText(typeArray[i]);
                }
            }, "").create().show();
        } else {
            requestType();
        }
    }

    @Override
    public int layoutId() {
        return R.layout.two_layout;
    }

    @Subscribe
    public void onEventMainThread(RefershEvent event) {
        if (event.mclass == getClass()) {
            String code = event.bundle.getString("code");
            String name = event.bundle.getString("name");
            addressET.setTag(code);
            addressET.setText(name);
        }
    }
}
