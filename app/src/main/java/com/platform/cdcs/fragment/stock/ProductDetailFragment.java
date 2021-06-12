package com.platform.cdcs.fragment.stock;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.R;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.HouseItem;
import com.platform.cdcs.model.ProductList;
import com.platform.cdcs.tool.CacheTool;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.sherchen.slidetoggleheader.views.XListView;
import com.trueway.app.uilib.activity.BaseActivity;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwSegmentWidget;
import com.tubb.smrv.SwipeHorizontalMenuLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by holytang on 2017/9/30.
 */
public class ProductDetailFragment extends BaseFragment {

    private ItemAdapter adapter1;
    private ItemAdapter adapter2;
    private ObservableXListView listView1;
    private ObservableXListView listView2;
    private String whName;
    private ProductList.ProductItem item;
    private int pageIndex1 = 1, pageIndex2 = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = (ProductList.ProductItem) getArguments().getSerializable("model");
            whName = getArguments().getString("whName");
        }
        adapter1 = new ItemAdapter(getContext());
        adapter1.setSlide(true);
        adapter2 = new ItemAdapter(getContext());
    }

    @Override
    public void initView(View view) {
        initLoadImg(view.findViewById(R.id.load));
        ((BaseActivity) getActivity()).getToolBar().setVisibility(View.GONE);
        final TextView titleView = (TextView) view.findViewById(R.id.title);
        final String code = item.getMaterialNumber();
        titleView.setText(code + "\n▽");
        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        view.findViewById(R.id.right_side).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.navigateToInNewActivity(getActivity(), EditProductFragment.class, null);

            }
        });
        TextView numView = (TextView) view.findViewById(R.id.num);
        numView.setText(String.valueOf(CacheTool.getInputCount(getContext()) + CacheTool.getOutputCount(getContext())));

        final TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setVisibility(View.GONE);
        textView.setText(item.getMaterialName());

        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleView.getText().toString().contains("▽")) {
                    textView.setVisibility(View.VISIBLE);
                    titleView.setText(code + "\n△");
                } else {
                    titleView.setText(code + "\n▽");
                    textView.setVisibility(View.GONE);
                }
            }
        });
        listView1 = (ObservableXListView) view.findViewById(android.R.id.list);
        listView1.setPullRefreshEnable(false);
        listView1.setAdapter(adapter1);
        listView1.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex1 = 1;
                adapter1.clear();
                ProductList.ProductItem item = new ProductList.ProductItem();
                item.setItemName("序列号");
                adapter1.addItem(item);
                adapter1.notifyDataSetChanged();
                requestList(false, 0);
            }

            @Override
            public void onLoadMore() {
                requestList(false, 0);
            }
        });
        listView2 = (ObservableXListView) view.findViewById(R.id.ics_listView);
        listView2.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex2 = 1;
                adapter2.clear();
                ProductList.ProductItem item = new ProductList.ProductItem();
                item.setItemName("批号");
                adapter2.addItem(item);
                adapter2.notifyDataSetChanged();
                requestList(false, 1);
            }

            @Override
            public void onLoadMore() {
                requestList(false, 1);
            }
        });
        listView2.setPullRefreshEnable(false);
        listView2.setAdapter(adapter2);
        listView2.setVisibility(View.GONE);

        GridView gridView = (GridView) view.findViewById(R.id.seg);
        final TwSegmentWidget widget = new TwSegmentWidget(gridView, new String[]{"有码", "无码"}, null, true, 1);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    listView1.setVisibility(View.VISIBLE);
                    listView2.setVisibility(View.GONE);
                    if (adapter1.getCount() == 0) {
                        requestList(true, 0);
                    }
                } else {
                    listView1.setVisibility(View.GONE);
                    listView2.setVisibility(View.VISIBLE);
                    if (adapter2.getCount() == 0) {
                        ProductList.ProductItem item = new ProductList.ProductItem();
                        item.setItemName("批号");
                        adapter2.addItem(item);
                        adapter2.notifyDataSetChanged();
                        requestList(true, 1);
                    }
                }
                widget.choose(i);
            }
        });
        requestList(true, 0);
    }

    @Override
    public int layoutId() {
        return R.layout.product_detail;
    }

    private void requestList(boolean show, final int type) {
        if (show) {
            showLoadImg();
        }
        Map<String, String> param = new HashMap<>();
        param.put("itemCode", item.getItemCode());
        param.put("itemType", String.valueOf(type));
        param.put("whName", whName);
        param.put("pageIndex", type == 0 ? String.valueOf(pageIndex1) : String.valueOf(pageIndex2));
        param.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        getHttpClient().post().url(Constant.PRODUCT_COUNTINFO_LIST).params(Constant.makeParam(param)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                if (type == 0) {
                    listView1.stopRefresh();
                    listView1.stopLoadMore();
                } else {
                    listView2.stopRefresh();
                    listView2.stopLoadMore();
                }
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                if (type == 0) {
                    listView1.stopRefresh();
                    listView1.stopLoadMore();
                } else {
                    listView2.stopRefresh();
                    listView2.stopLoadMore();
                }
                //TODO
                if (type == 0) {

                } else {

                }
//                Type type = new TypeToken<BaseObjResponse<HouseItem.HouseList>>() {
//                }.getType();
            }
        });
    }


    private class ItemAdapter extends EnhancedAdapter<ProductList.ProductItem> {

        private boolean slide;
        private View.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showToast(getContext(), "btn click");
            }
        };
        private View.OnClickListener itemListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showToast(getContext(), "item click");
            }
        };

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            ViewHolder holder = (ViewHolder) paramView.getTag();
            int type = getItemViewType(position);
            ProductList.ProductItem item = getItem(position);
            if (type == 0) {
                holder.titleView.setText(item.getItemName());
            } else {
                holder.titleView.setText(item.getSerialNumber());
                if (TextUtils.isEmpty(item.getId())) {
                    holder.textView.setVisibility(View.INVISIBLE);
                } else {
                    holder.textView.setText(item.getId());
                    holder.textView.setVisibility(View.VISIBLE);
                }
                holder.descView.setText(String.format("%s %s", item.getQty(), item.getUom()));
                holder.btnView.setTag(item);
                paramView.setTag(R.id.button1, item);
            }
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            int type = getItemViewType(position);
            View rootView = null;
            if (type == 0) {
                TextView textView = new TextView(context);
                textView.setTextColor(getResources().getColor(R.color.text_gray));
                textView.setTextSize(12);
                int padding = Utils.convertDIP2PX(context, 15);
                textView.setPadding(padding, padding / 2, padding, padding / 2);
                rootView = textView;
                ViewHolder holder = new ViewHolder();
                holder.titleView = textView;
                rootView.setTag(holder);
            } else {
                rootView = inflater.inflate(R.layout.product_detail_item, viewgroup, false);
                SwipeHorizontalMenuLayout layout = (SwipeHorizontalMenuLayout) rootView;
                if (!slide) {
                    layout.setSwipeEnable(false);
                }
                ViewHolder holder = new ViewHolder();
                holder.titleView = (TextView) rootView.findViewById(R.id.title);
                holder.descView = (TextView) rootView.findViewById(R.id.desc);
                holder.textView = (TextView) rootView.findViewById(R.id.text);
                holder.btnView = (TextView) rootView.findViewById(R.id.smMenuViewLeft);
                holder.btnView.setOnClickListener(btnListener);
                rootView.setOnClickListener(itemListener);
                rootView.setTag(holder);
            }
            return rootView;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).getType();
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        public void setSlide(boolean slide) {
            this.slide = slide;
        }
    }

    private class ViewHolder {
        TextView titleView, descView, textView, btnView;
    }
}
