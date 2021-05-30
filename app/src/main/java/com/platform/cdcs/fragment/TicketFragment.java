package com.platform.cdcs.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.activity.TicketScanActivity;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.DocNo;
import com.platform.cdcs.model.InvoiceList;
import com.platform.cdcs.model.MockObj;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.sherchen.slidetoggleheader.views.XListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.tubb.smrv.SwipeHorizontalMenuLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Call;

/**
 * Created by holytang on 2017/9/20.
 */
public class TicketFragment extends BaseFragment {

    private ObservableXListView slideListView;
    private ItemAdapter adapter;
    private int pageIndex = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        adapter = new ItemAdapter(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(View view) {
        initSelfLoadImg(view.findViewById(R.id.load));
        initToolBar(view);
        toolbar.setNavigationIcon(R.mipmap.icon_search);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.navigateToInNewActivity(getActivity(), TicketSearchFragment.class, null);
            }
        });
        toolbar.getMenu().add(0, 0, 0, "新增发票").setTitle("新增发票").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(getContext(), TicketScanActivity.class));
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        setFragmentTitle("发票");
        RelativeLayout headerView = new RelativeLayout(getContext());
        headerView.setBackgroundColor(getResources().getColor(R.color.header_bg));
        AbsListView.LayoutParams llp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        headerView.setLayoutParams(llp);
        int padding = Utils.convertDIP2PX(getContext(), 7);
        headerView.setPadding(padding * 2, padding, padding * 2, padding);
        TextView textView = new TextView(getContext());
        textView.setText("发票号码/客户");
        textView.setTextSize(12);
        textView.setTextColor(getResources().getColor(R.color.text_gray));
        headerView.addView(textView);

        textView = new TextView(getContext());
        RelativeLayout.LayoutParams tLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        textView.setLayoutParams(tLP);
        textView.setText("发票日期");
        textView.setTextSize(12);
        textView.setTextColor(getResources().getColor(R.color.text_gray));
        headerView.addView(textView);
        slideListView = (ObservableXListView) view.findViewById(android.R.id.list);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InvoiceList.Invoice item = adapter.getItem((int) view.getTag(R.id.button1));
                Bundle bundle = new Bundle();
                bundle.putString("id", item.getInvoiceId());
                bundle.putString("cusCode", item.getCusCode());
                FragmentUtil.navigateToInNewActivity(getActivity(), TicketDetailFragment.class, bundle);
            }
        });
        slideListView.setPullLoadEnable(true);
        slideListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                adapter.clear();
                adapter.notifyDataSetChanged();
                request(false, false);
            }

            @Override
            public void onLoadMore() {
                request(false, true);
            }
        });
        slideListView.addHeaderView(headerView);
        slideListView.setAdapter(adapter);
        request(true, false);
    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    private void removeTicket(final InvoiceList.Invoice item) {
        showSelfLoadImg();
        Map<String, String> param = new HashMap<>();
        param.put("id", item.getInvoiceId());
        getHttpClient().post().url(Constant.CANCLE_INVOICE_URL).params(Constant.makeParam(param)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Utils.showToast(getContext(), R.string.server_error);
                dismissSelfLoadImg();
            }

            @Override
            public void onResponse(String s, int i) {
                dismissSelfLoadImg();
                Type type = new TypeToken<BaseObjResponse<MockObj>>() {
                }.getType();
                BaseObjResponse<MockObj> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    pageIndex = 1;
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    request(false, false);
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
    }

    private void request(boolean showLoad, final boolean more) {
        if (showLoad) {
            showSelfLoadImg();
        }
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        map.put("pageIndex", String.valueOf(pageIndex));
        getHttpClient().post().url(Constant.INVOICE_LST).params(Constant.makeParam(map)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissSelfLoadImg();
                Utils.showToast(getContext(), R.string.server_error);
                slideListView.stopLoadMore();
                slideListView.stopRefresh();
            }

            @Override
            public void onResponse(String s, int i) {
                slideListView.stopLoadMore();
                slideListView.stopRefresh();
                dismissSelfLoadImg();
                Type type = new TypeToken<BaseObjResponse<InvoiceList>>() {
                }.getType();
                BaseObjResponse response = new Gson().fromJson(s, type);
                InvoiceList list = (InvoiceList) response.getResult();
                if ("1".equals(list.getCode())) {
                    adapter.addAll(((InvoiceList) response.getResult()).getInvoiceList());
                    adapter.notifyDataSetChanged();
                    if (((InvoiceList) response.getResult()).getInvoiceList().size() == Constant.PAGE_SIZE) {
                        pageIndex++;
                    }
                } else {
                    Utils.showToast(getContext(), list.getMsg());
                }

            }
        });
    }

    @Subscribe
    public void onEventMainThread(RefershEvent event) {
        if (event.mclass == this.getClass()) {
            pageIndex = 1;
            adapter.clear();
            adapter.notifyDataSetChanged();
            request(true, false);
        }
    }

    private class ItemAdapter extends EnhancedAdapter<InvoiceList.Invoice> implements View.OnClickListener {

        private AdapterView.OnItemClickListener onItemClickListener;

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            ViewHoder holder = (ViewHoder) paramView.getTag();
            InvoiceList.Invoice item = getItem(position);
            holder.titleView.setText(item.getInNO());
            holder.textView.setText(item.getCusName());
            holder.timeView.setText(item.getInDate());
            if (1 == item.getHavePic()) {
                holder.descView.setTextColor(getResources().getColor(R.color.color_green));
                holder.descView.setText("已拍照");
            } else {
                holder.descView.setTextColor(getResources().getColor(R.color.text_hint));
                holder.descView.setText("未拍照");
            }
            holder.removeView.setTag(position);
            paramView.setTag(R.id.button1, position);
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            SwipeHorizontalMenuLayout rootView = (SwipeHorizontalMenuLayout) inflater.inflate(R.layout.ticket_item_group, viewgroup, false);
            ViewHoder holder = new ViewHoder();
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            holder.timeView = (TextView) rootView.findViewById(R.id.time);
            holder.descView = (TextView) rootView.findViewById(R.id.desc);
            holder.removeView = (TextView) rootView.findViewById(R.id.smMenuViewRight);
            holder.removeView.setOnClickListener(this);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(null, view, 0, 0);
                }
            });
            rootView.setTag(holder);
            return rootView;
        }

        @Override
        public void onClick(final View view) {
            new TwDialogBuilder(getContext()).setTitle(R.string.attention).setMessage("确定要作废吗？").setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    removeTicket(adapter.getItem((int) view.getTag()));
                }
            }).setPositiveButton(R.string.cancel_text, null).create().show();
        }

        public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    private class ViewHoder {
        TextView titleView, textView, timeView, descView, removeView;
    }

}
