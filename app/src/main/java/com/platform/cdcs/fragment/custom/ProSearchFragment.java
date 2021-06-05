package com.platform.cdcs.fragment.custom;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.model.SubBUItem;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/28.
 */
public class ProSearchFragment extends BaseFragment {

    private EditText lineET, codeET, l3ET, l4ET, l5ET;

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
        setTitle("产品查询");
        LinearLayout rootView = (LinearLayout) view.findViewById(R.id.root);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        codeET = ViewTool.createEditItem(inflater, "产品代码", rootView, false, false);
        codeET.setHint("请输入产品代码");

        lineET = ViewTool.createEditItem(inflater, "产品线", rootView, false, true);
        lineET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestSubBU();
            }
        });
        lineET.setHint("请选择产品线");


        l3ET = ViewTool.createEditItem(inflater, "Level3", rootView, false, false);
        l3ET.setHint("请输入Level3");

        l4ET = ViewTool.createEditItem(inflater, "Level4", rootView, false, false);
        l4ET.setHint("请输入Level4");

        l5ET = ViewTool.createEditItemNoLine(inflater, "Level5", rootView, false, false);
        l5ET.setHint("请输入Level5");

        LinearLayout bottomView = (LinearLayout) view.findViewById(R.id.bottom);
        bottomView.addView(inflater.inflate(R.layout.item_two_btn1, null));
        TextView btn1 = (TextView) bottomView.findViewById(R.id.button1);
        btn1.setText("重置");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //重置
                codeET.setText("");
                lineET.setText("");
                lineET.setTag("");
                l3ET.setText("");
                l4ET.setText("");
                l5ET.setText("");
            }
        });
        TextView btn2 = (TextView) bottomView.findViewById(R.id.button2);
        btn2.setText("查询");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查询
                RefershEvent event = new RefershEvent();
                event.mclass = ProSearchListFragment.class;
                Bundle bundle = new Bundle();
                bundle.putString("code", codeET.getText().toString().trim());
                bundle.putString("line", (String) lineET.getTag());
                EventBus.getDefault().post(event);
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.two_layout;
    }

    private void requestSubBU() {
        getHttpClient().post().url(Constant.SUBBU_LST).params(Constant.makeParam(new HashMap<String, String>())).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Utils.showToast(getActivity(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    Type type = new TypeToken<BaseObjResponse<SubBUItem.SubBUList>>() {
                    }.getType();
                    BaseObjResponse<SubBUItem.SubBUList> response = new Gson().fromJson(s, type);
                    if ("1".equals(response.getResult().getCode())) {
                        final List<ChooseItem> subBuList = new ArrayList<ChooseItem>();
                        ChooseItem item = new ChooseItem();
                        item.setTitle("全部产品线");
                        item.setTime("");
                        item.setIsCheck(true);
                        subBuList.add(item);
                        for (SubBUItem sub : response.getResult().getObjList()) {
                            item = new ChooseItem();
                            item.setTitle(sub.getLocalSubBUCode());
                            item.setTime(sub.getSubBU());
                            subBuList.add(item);
                        }
                        String[] array = new String[subBuList.size()];
                        for (int j = 0; j < subBuList.size(); j++) {
                            array[j] = subBuList.get(j).getTitle();
                        }
                        new TwDialogBuilder(getContext()).setItems(array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                lineET.setText(subBuList.get(i).getTitle());
                                lineET.setTag(subBuList.get(i).getText());
                            }
                        }, "").create().show();
                    }
                } catch (Exception e) {

                }
            }
        });
    }
}
