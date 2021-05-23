package com.platform.cdcs.fragment.stock;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;

/**
 * Created by holytang on 2017/9/28.
 */
public class AddStockFragment extends BaseFragment {

    @Override
    public void initView(View view) {
        setTitle("新增库位");
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout rootView = (LinearLayout) view.findViewById(R.id.root);
        EditText nameET = ViewTool.createEditItem(inflater, "库位名称", rootView, true, false);
        nameET.setHint("请输入库位名称");
        EditText codeET = ViewTool.createEditItem(inflater, "库位代码", rootView, true, false);
        codeET.setHint("请输入库位代码");
        EditText addressET = ViewTool.createEditItem(inflater, "库位地址", rootView, true, false);
        addressET.setHint("请输入库位地址");

        final ImageView setImg = ViewTool.createSwitchItem(inflater, rootView,"设为主库", true, false);
        setImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setImg.isSelected()){
                    setImg.setSelected(false);
                }else{
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
