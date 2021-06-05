package com.platform.cdcs.fragment.stock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.activity.StockScanActivity;
import com.platform.cdcs.fragment.ProductFragment;
import com.platform.cdcs.model.ProductList;
import com.platform.cdcs.model.RefershEvent;
import com.platform.cdcs.tool.CacheTool;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.platform.cdcs.widget.FormDialog;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import de.greenrobot.event.EventBus;

/**
 * Created by holytang on 2017/9/30.
 */
public class EditProductFragment extends BaseFragment {

    private ItemAdapter adapter;
    private int model;
    private TextView titleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = getArguments().getInt("model", 0);
        adapter = new ItemAdapter(getContext());
        if (model == 0) {
            adapter.addAll(CacheTool.getInputList(getContext()));
        } else {
            adapter.addAll(CacheTool.getOutputList(getContext()));
        }
    }

    @Override
    public void initView(View view) {
        hideThisToolBar(view);
        setHasOptionsMenu(true);
        setTitle(model == 0 ? "编辑入库产品" : "编辑出库产品");
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        ObservableXListView listView = (ObservableXListView) view.findViewById(android.R.id.list);
        TextView btnView = (TextView) view.findViewById(R.id.button1);
        btnView.setVisibility(View.VISIBLE);
        btnView.setText("下一步");
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model == 0) {
                    FragmentUtil.navigateToInNewActivity(getActivity(), EditOutFragment.class, null);
                } else {
                    FragmentUtil.navigateToInNewActivity(getActivity(), EditTicketFragment.class, null);
                }
            }
        });
        View header = LayoutInflater.from(getContext()).inflate(R.layout.left_right, null);
        titleView = (TextView) header.findViewById(R.id.title);
        titleView.setText(String.format("已添加 %s 件产品", model == 0 ? CacheTool.getInputCount(getContext()) : CacheTool.getOutputCount(getContext())));
        TextView textView = (TextView) header.findViewById(R.id.text);
        textView.setText("清空");
        textView.setTextColor(getResources().getColor(R.color.text_red));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TwDialogBuilder(getContext()).setTitle(R.string.attention).setMessage("确定是否清空吗？").setPositiveButton(R.string.cancel_text, null).setNegativeButton(R.string.clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (model == 0) {
                            CacheTool.clearInputCache(getContext());
                        } else {
                            CacheTool.clearOutputCache(getContext());
                        }
                    }
                }).create().show();
            }
        });
        listView.addHeaderView(header);
        listView.setPullRefreshEnable(false);
        listView.setAdapter(adapter);

    }

    @Override
    public int layoutId() {
        return R.layout.listview;
    }

    @Override
    protected void initMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, "扫码添加").setTitle("扫码添加").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getContext(), StockScanActivity.class);
                startActivity(intent);
                getActivity().finish();
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    private class ItemAdapter extends EnhancedAdapter<ProductList.ProductItem> {

        private View.OnClickListener leftListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProductList.ProductItem item = (ProductList.ProductItem) view.getTag();
                new FormDialog(getContext(), new FormDialog.FormListener() {
                    @Override
                    public void okClick(String[] value) {
                        item.setQty(value[0]);
                        CacheTool.changeNum(getContext(), model, item.getSerialNumber(), value[0]);
                        adapter.notifyDataSetChanged();
                    }
                }, "编辑产品数量").addView(new String[]{"产品数量"}, 0, "").show();
            }
        };
        private View.OnClickListener rightListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProductList.ProductItem item = (ProductList.ProductItem) view.getTag();
                new TwDialogBuilder(getContext()).setTitle(R.string.attention).setMessage("确定是否移除吗？").setPositiveButton(R.string.cancel_text, null).setNegativeButton(R.string.remove, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.remove(item);
                        adapter.notifyDataSetChanged();
                        CacheTool.remove(getContext(), model, item.getSerialNumber());
                        titleView.setText(String.format("已添加 %s 件产品", model == 0 ? CacheTool.getInputCount(getContext()) : CacheTool.getOutputCount(getContext())));
                        RefershEvent event = new RefershEvent();
                        event.mclass = ProductFragment.class;
                        EventBus.getDefault().post(event);
                    }
                }).create().show();
            }
        };

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            ViewHolder holder = (ViewHolder) paramView.getTag();
            ProductList.ProductItem item = getItem(position);
            holder.titleView.setText(item.getSerialNumber());
            holder.textView.setText(item.getMaterialName());
            holder.dateView.setText(ViewTool.makeQtyAndUnit(getContext(), item.getNowqty(), item.getQty(), item.getSaleUnit()));
            holder.leftBtn.setTag(item);
            holder.rightBtn.setTag(item);
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = inflater.inflate(R.layout.stock_item_group, viewgroup, false);
            ViewHolder holder = new ViewHolder();
            holder.titleView = (TextView) rootView.findViewById(R.id.title);
            holder.dateView = (TextView) rootView.findViewById(R.id.time);
            holder.textView = (TextView) rootView.findViewById(R.id.text);
            holder.textView.setSingleLine(false);
            holder.textView.setMaxLines(2);
            holder.leftBtn = (TextView) rootView.findViewById(R.id.smMenuViewLeft);
            holder.rightBtn = (TextView) rootView.findViewById(R.id.smMenuViewRight);
            holder.leftBtn.setOnClickListener(leftListener);
            holder.rightBtn.setOnClickListener(rightListener);
            rootView.setTag(holder);
            return rootView;
        }
    }

    private class ViewHolder {
        TextView titleView, dateView, textView, leftBtn, rightBtn;
    }
}
