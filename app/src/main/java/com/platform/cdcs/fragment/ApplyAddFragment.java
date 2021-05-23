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
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;

/**
 * Created by holytang on 2017/9/28.
 */
public class ApplyAddFragment extends BaseFragment {

    @Override
    public void initView(View view) {
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
        nameET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    return true;
                }
                return false;
            }
        });
        nameET.setEnabled(false);
        nameET.setHint("请选择所属机构类型");

        EditText addressET = ViewTool.createEditItem(inflater, "所属机构", rootView, true, true);
        addressET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    return true;
                }
                return false;
            }
        });
        addressET.setEnabled(false);
        addressET.setHint("请选择所属机构");
        final ImageView setImg = ViewTool.createSwitchItem(inflater, rootView, "合并库存", true, false);
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
        EditText bakET = ViewTool.createEditItemNoLine(inflater, "备注", rootView, false, false);
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
    }

    @Override
    public int layoutId() {
        return R.layout.two_layout;
    }
}
