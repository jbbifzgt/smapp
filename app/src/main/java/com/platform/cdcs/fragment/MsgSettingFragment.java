package com.platform.cdcs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.MockObj;
import com.platform.cdcs.tool.Constant;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by holytang on 2017/9/28.
 */
public class MsgSettingFragment extends BaseFragment {

    private ItemAdapter adapter;
    private ObservableXListView listView;
    private ChooseItem inoutMsgItem, invoiceMsgItem, commonMsgItem,arriveMsgItem;
    private ChooseItem currentItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(getContext());
        arriveMsgItem = new ChooseItem();
        arriveMsgItem.setTitle("到货提醒");
        arriveMsgItem.setType(1);
        inoutMsgItem = new ChooseItem();
        inoutMsgItem.setTitle("出入库上报");
        inoutMsgItem.setType(1);
        adapter.addItem(inoutMsgItem);
        invoiceMsgItem = new ChooseItem();
        invoiceMsgItem.setTitle("发票上报");
        invoiceMsgItem.setType(1);
        adapter.addItem(invoiceMsgItem);
        commonMsgItem = new ChooseItem();
        commonMsgItem.setTitle("日常提醒");
        commonMsgItem.setType(1);
        adapter.addItem(commonMsgItem);
    }

    @Override
    public void initView(View view) {
        initLoadImg(view.findViewById(R.id.load));
        hideThisToolBar(view);
        setTitle("消息设置");
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        listView = (ObservableXListView) view.findViewById(android.R.id.list);
        listView.setPullRefreshEnable(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentItem = (ChooseItem) adapterView.getItemAtPosition(i);
                if (currentItem != null) {
                    if (currentItem.getType() == 1) {
                        currentItem.setType(0);
                    } else {
                        currentItem.setType(1);
                    }
                    post();
                }
            }
        });
        listView.setAdapter(adapter);
        request();
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    private void request() {
        showLoadImg();
        getHttpClient().post().url(Constant.DIST_MSGINFO).params(Constant.makeParam(new HashMap<String, String>())).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                try {
                    JSONObject obj = new JSONObject(s).getJSONObject("result");
                    if ("1".equals(obj.getString("code"))) {
                        obj = obj.getJSONObject("distMdmudInfo");
                        inoutMsgItem.setType(obj.getInt("inoutMsg"));
                        invoiceMsgItem.setType(obj.getInt("invoiceMsg"));
                        commonMsgItem.setType(obj.getInt("commonMsg"));
                        arriveMsgItem.setType(obj.getInt("arriveMsg"));
                        adapter.notifyDataSetChanged();
                    } else {
                        Utils.showToast(getContext(), obj.getString("msg"));
                    }
                } catch (Exception e) {

                }

            }
        });
    }

    private void post() {
        showLoadImg();
        Map<String, String> param = new HashMap<>();
        param.put("inoutMsg", String.valueOf(inoutMsgItem.getType()));
        param.put("invoiceMsg", String.valueOf(invoiceMsgItem.getType()));
        param.put("commonMsg", String.valueOf(commonMsgItem.getType()));
        param.put("arriveMsg", String.valueOf(arriveMsgItem.getType()));
        getHttpClient().post().url(Constant.EDIT_DIST_MSGINFO).params(Constant.makeParam(param)).build().execute(new StringCallback() {
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
                    Utils.showToast(getContext(), "设置成功！");
                    adapter.notifyDataSetChanged();
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }

    private class ItemAdapter extends EnhancedAdapter<ChooseItem> {

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            ViewHoder holder = (ViewHoder) paramView.getTag();
            ChooseItem item = getItem(position);
            holder.titleView.setText(item.getTitle());
            if (item.getType() == 1) {
                holder.textView.setText("推送给我");
            } else {
                holder.textView.setText("已推送");
            }
            View rootView = (View) holder.titleView.getParent();
            if (position == 0) {
                rootView.setBackgroundResource(R.drawable.shape_corner_up);
            } else if (position == getCount() - 1) {
                rootView.setBackgroundResource(R.drawable.shape_corner_down);
            } else {
                rootView.setBackgroundResource(R.drawable.shape_corner_center);
            }
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.margin_item, viewgroup, false);
            ViewHoder holder = new ViewHoder();
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            rootView.setTag(holder);
            return rootView;
        }
    }

    private class ViewHoder {
        TextView titleView, textView;
    }
}
