package com.platform.cdcs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.model.DocNo;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.ViewTool;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/26.
 */
public class TicketChooseFragment extends BaseFragment {

    private ItemAdapter adapter;
    private DocNo docNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        docNo = (DocNo) getArguments().getSerializable("model");
//        int pos = getArguments().getInt("pos");
        docNo.getDocNoList().get(0).setCheck(true);
    }

    @Override
    public void initView(View view) {
        adapter = new ItemAdapter(getContext());
        adapter.addAll(docNo.getDocNoList());
        initLoadImg(view.findViewById(R.id.load));
        hideThisToolBar(view);
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("选择出入库单");
//        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.ticket_choose, null);
//        LinearLayout rootView = (LinearLayout) headerView.findViewById(R.id.root);
//        prepareHeader(rootView);
        TextView btnView = (TextView) view.findViewById(R.id.button1);
        btnView.setText("确定关联");
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });
        initLoadImg(view.findViewById(R.id.load));
        ObservableXListView listView = (ObservableXListView) view.findViewById(android.R.id.list);
        listView.setPullRefreshEnable(false);
//        listView.addHeaderView(headerView);
        listView.setAdapter(adapter);
    }

//    private void prepareHeader(LinearLayout rootView) {
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        EditText codeET = ViewTool.createEditItem(inflater, "出入库单号", rootView,false,false);
//        codeET.setHint("请填写出入库单号");
//        EditText clientET = ViewTool.createEditItem(inflater, "客户", rootView,false,true);
//        clientET.setHint("请选择客户");
//        clientET.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        EditText date1ET = ViewTool.createEditItem(inflater, "单据日期", rootView,false,true);
//        date1ET.setText("6个月内");
//        date1ET.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        EditText date2ET = ViewTool.createEditItem(inflater, "上报日期", rootView,false,true);
//        date2ET.setText("7天内");
//        date2ET.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        EditText typeET = ViewTool.createEditItem(inflater, "单据类型", rootView,false,true);
//        typeET.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        typeET.setInputType(InputType.TYPE_NULL);
//        typeET.setHint("请选择单据类型");
//        TextView[] btns = ViewTool.createTwoBtnItem(inflater, rootView,false);
//        btns[0].setText("重置");
//        btns[0].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        btns[1].setText("查询");
//        btns[1].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    private void post() {
        showLoadImg();
        getHttpClient().post().url(Constant.ADD_INVOICE_PRODUCT).params(new HashMap<String, String>()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getContext(),R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();

            }
        });
    }

    private class ItemAdapter extends EnhancedAdapter<DocNo.DocItem> {

        private View.OnClickListener checkListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                adapter.unCheck();
                adapter.getItem(pos).setCheck(true);
                adapter.notifyDataSetChanged();
            }
        };

        private View.OnClickListener itemListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                DocNo.DocItem item = adapter.getItem(pos);
                item.setPosition(pos);
                EventBus.getDefault().post(item);
                getActivity().finish();
            }
        };

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            ViewHolder holder = (ViewHolder) paramView.getTag();
            DocNo.DocItem item = getItem(position);
            holder.titleView.setText(item.getDocNo());
            holder.textView.setText(item.getDocDate());
            if (item.isCheck()) {
                holder.checkView.setVisibility(View.VISIBLE);
            } else {
                holder.checkView.setVisibility(View.INVISIBLE);
            }
            holder.rootView.setTag(position);
            holder.checkView.setTag(position);
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.ticket_choose_item, viewgroup, false);
            ViewHolder holder = new ViewHolder();
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            holder.checkView = (ImageView) rootView.findViewById(R.id.img);
            holder.checkView.setOnClickListener(checkListener);
            holder.rootView = rootView.findViewById(R.id.root);
            holder.rootView.setOnClickListener(itemListener);
            rootView.setTag(holder);
            return rootView;
        }

        public void unCheck() {
            for (DocNo.DocItem item : dataList) {
                if (item.isCheck()) {
                    item.setCheck(false);
                    break;
                }
            }
        }
    }

    private class ViewHolder {
        TextView titleView, textView;
        View rootView;
        ImageView checkView;
    }
}
