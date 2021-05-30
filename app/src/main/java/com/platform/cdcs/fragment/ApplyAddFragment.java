package com.platform.cdcs.fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.fragment.custom.ProSearchListFragment;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by holytang on 2017/9/28.
 */
public class ApplyAddFragment extends BaseFragment {

    private String orgType;
    private String distApplyName;
    private EditText bakET;
    private ImageView setImg;

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
        EditText nameET = ViewTool.createEditItem(inflater, "所属机构类型", rootView, false, true);
        nameET.setInputType(InputType.TYPE_NULL);
        nameET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        nameET.setHint("请选择所属机构类型");

        EditText addressET = ViewTool.createEditItem(inflater, "所属机构", rootView, true, true);
        addressET.setInputType(InputType.TYPE_NULL);
        addressET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        showLoadImg();
        Map<String, String> param = new HashMap<>();
        param.put("orgType", "");
        param.put("distApplyName", "");
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
                //TODO
            }
        });

    }

    @Override
    public int layoutId() {
        return R.layout.two_layout;
    }
}
