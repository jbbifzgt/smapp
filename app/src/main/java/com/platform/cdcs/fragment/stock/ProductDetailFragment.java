package com.platform.cdcs.fragment.stock;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.model.ProductList;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.activity.BaseActivity;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwSegmentWidget;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

/**
 * Created by holytang on 2017/9/30.
 */
public class ProductDetailFragment extends BaseFragment {

    private ItemAdapter adapter1;
    private ItemAdapter adapter2;
    private ObservableXListView listView1;
    private ObservableXListView listView2;
    private ProductList.ProductItem item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = (ProductList.ProductItem) getArguments().getSerializable("model");
        adapter1 = new ItemAdapter(getContext());
        adapter1.setSlide(true);

        adapter2 = new ItemAdapter(getContext());
        ChooseItem item = new ChooseItem();
        item.setTitle("批号");
        adapter2.addItem(item);

        item = new ChooseItem();
        item.setType(1);
        item.setTitle("KKH921");
        item.setTime("0.50 EA");
        adapter2.addItem(item);

        item = new ChooseItem();
        item.setType(1);
        item.setTitle("KKH921");
        item.setText("失效期 2022-08-01");
        item.setTime("0.50 EA");
        adapter2.addItem(item);

        item = new ChooseItem();
        item.setTitle("序列号");
        adapter1.addItem(item);

        item = new ChooseItem();
        item.setType(1);
        item.setTitle("KKH921");
        item.setText("失效期 2022-08-01");
        item.setTime("0.50 EA");
        adapter1.addItem(item);

        item = new ChooseItem();
        item.setType(1);
        item.setTitle("SUT60002935653");
        item.setText("失效期 2022-08-01");
        item.setTime("0.50 EA");
        adapter1.addItem(item);

        item = new ChooseItem();
        item.setType(1);
        item.setTitle("SUT60002935653");
        item.setText("失效期 2022-08-01");
        item.setTime("0.50 EA");
        adapter1.addItem(item);

    }

    @Override
    public void initView(View view) {
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
        numView.setText("28");

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

        listView2 = (ObservableXListView) view.findViewById(R.id.ics_listView);
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
                } else {
                    listView1.setVisibility(View.GONE);
                    listView2.setVisibility(View.VISIBLE);
                }
                widget.choose(i);
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.product_detail;
    }

    private class ItemAdapter extends EnhancedAdapter<ChooseItem> {

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
            ChooseItem item = getItem(position);
            if (type == 0) {
                holder.titleView.setText(item.getTitle());
            } else {
                holder.titleView.setText(item.getTitle());
                if (TextUtils.isEmpty(item.getText())) {
                    holder.textView.setVisibility(View.INVISIBLE);
                } else {
                    holder.textView.setText(item.getText());
                    holder.textView.setVisibility(View.VISIBLE);
                }
                holder.descView.setText(item.getTime());
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
                SwipeHorizontalMenuLayout layout=(SwipeHorizontalMenuLayout)rootView;
                if(!slide){
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
