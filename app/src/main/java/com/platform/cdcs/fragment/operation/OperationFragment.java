package com.platform.cdcs.fragment.operation;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TimeDialogBuilder;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by holytang on 2017/10/6.
 */
public class OperationFragment extends BaseFragment {

    private EditText timeET;

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
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("填写手术信息");

        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout rootView1 = (LinearLayout) view.findViewById(R.id.button1);
        timeET = ViewTool.createEditItem(inflater, "手术日期", rootView1, false, true);
        timeET.setHint("请选择日期");
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

        EditText nameET = ViewTool.createEditItem(inflater, "科室名称", rootView1, false, false);
        nameET.setHint("请填写科室名称");

        EditText doctorET = ViewTool.createEditItem(inflater, "医生姓名", rootView1, false, false);
        doctorET.setHint("请填写医生姓名");

        EditText codeET = ViewTool.createEditItem(inflater, "病人住院号", rootView1, false, false);
        codeET.setHint("请填写病人住院号");

        EditText typeET = ViewTool.createEditItem(inflater, "手术类型", rootView1, false, true);
        typeET.setHint("请选择手术类型");
        typeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.navigateToInNewActivity(getActivity(), OperationSearchFragment.class, null);
            }
        });

        EditText numET = ViewTool.createEditItemNoLine(inflater, "手术台数", rootView1, false, false);
        numET.setHint("请输入手术台数");
        numET.setInputType(InputType.TYPE_CLASS_NUMBER);

        view.findViewById(R.id.button2).setVisibility(View.GONE);
        view.findViewById(R.id.button3).setVisibility(View.GONE);
        view.findViewById(R.id.button4).setVisibility(View.GONE);
        view.findViewById(R.id.button5).setVisibility(View.GONE);
        TextView btn = (TextView) view.findViewById(R.id.text);
        btn.setText("完成");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.five_layout;
    }


    @Subscribe
    public void onEventMainThread() {
    }
}
