package com.trueway.app.uilib.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trueway.app.uilib.R;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.tool.Utils;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Trueway on 2016/3/21.
 */
public class TwSegmentWidget {

    private int numColumn;
    private int[] icons;
    private String[] imgs;
    private int height;
    private boolean arrorFlag = true;
    private ItemAdapter adapter;
    private boolean upFlag;
    private boolean showLineFlag = true;
    private int padding;
    private int currentIndex;
    private int type;
    private String[] choose;

    public TwSegmentWidget(GridView gridView, String[] menus) {
        numColumn = menus.length;
        gridView.setNumColumns(numColumn);
        adapter = new ItemAdapter(gridView.getContext(), Arrays.asList(menus));
        gridView.setAdapter(adapter);
        arrorFlag = false;
    }

    public TwSegmentWidget(GridView gridView, String[] menus, int[] drawables) {
        this(gridView,menus,drawables,false);
    }

    public TwSegmentWidget(GridView gridView, String[] menus, int[] drawables,boolean showLineFlag,int type) {
        this(gridView,menus,drawables,showLineFlag);
        this.type=type;
    }

    public TwSegmentWidget(GridView gridView, String[] menus, int[] drawables,boolean showLineFlag) {
        numColumn = menus.length;
        gridView.setNumColumns(numColumn);
        adapter = new ItemAdapter(gridView.getContext(), Arrays.asList(menus));
        gridView.setAdapter(adapter);
        arrorFlag = false;
        this.showLineFlag=showLineFlag;
        icons = drawables;
    }

    public TwSegmentWidget(GridView gridView, String[] menus, String[] drawables) {
        numColumn = menus.length;
        gridView.setNumColumns(numColumn);
        adapter = new ItemAdapter(gridView.getContext(), Arrays.asList(menus));
        gridView.setAdapter(adapter);
        arrorFlag = false;
        showLineFlag=false;
        imgs = drawables;
    }

    public TwSegmentWidget(GridView gridView, String[] menus, int[] icons, int height) {
        numColumn = menus.length;
        gridView.setNumColumns(numColumn);
        this.height = height;
        this.icons = icons;
        ItemAdapter adapter = new ItemAdapter(gridView.getContext(), Arrays.asList(menus));
        gridView.setAdapter(adapter);
    }

    public TwSegmentWidget(GridView gridView, String[] menus, int[] icons, int height, int column) {
        numColumn = column;
        gridView.setNumColumns(numColumn);
        this.height = height;
        this.icons = icons;
        ItemAdapter adapter = new ItemAdapter(gridView.getContext(), Arrays.asList(menus));
        gridView.setAdapter(adapter);
    }

    public TwSegmentWidget(GridView gridView, String[] menus, int[] icons, int height, boolean showLine) {
        numColumn = menus.length;
        gridView.setNumColumns(numColumn);
        this.height = height;
        this.icons = icons;
        ItemAdapter adapter = new ItemAdapter(gridView.getContext(), Arrays.asList(menus));
        gridView.setAdapter(adapter);
        this.showLineFlag = showLine;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void changeUpFlag() {
        upFlag = upFlag ? false : true;
        adapter.notifyDataSetChanged();
    }

    public void changeTitle(int position, String text) {
        adapter.changeTitle(position, text);
        adapter.notifyDataSetChanged();
    }

    public void close() {

    }

    public void choose(int i) {
        currentIndex = i;
        adapter.notifyDataSetChanged();
    }

    public void setChoose(String[] choose) {
        this.choose = choose;
    }

    private class ItemAdapter extends EnhancedAdapter<String> {

        public ItemAdapter(Context context, Collection<String> collection) {
            super(context);
            this.dataList.addAll(collection);
        }

        @Override
        protected void bindView(View paramView, Context paramContext,
                                int position) {
            String title = getItem(position);
            ViewHolder vHolder = (ViewHolder) paramView.getTag();
            vHolder.titleView.setText(title);

            if (showLineFlag) {
                if (((position + 1) % numColumn) == 0) {
                } else {
                    vHolder.lineView.setVisibility(View.VISIBLE);
                }
            } else {
                vHolder.lineView.setVisibility(View.GONE);
            }
            if (icons != null) {
                if(type==1){
                    if(icons[position]!=0){
                        vHolder.titleView.setCompoundDrawablesWithIntrinsicBounds(icons[position],0,0,0);
                    }else{
                        vHolder.titleView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    }
                }else if(type==2){
                    if(icons[position]!=0){
                        vHolder.titleView.setCompoundDrawablesWithIntrinsicBounds(0,icons[position],0,0);
                    }else{
                        vHolder.titleView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    }
                }else{
                    vHolder.imgView.setImageResource(icons[position]);
                }
            }else if(imgs!=null){
//                if(choose!=null&&currentIndex==position){
//                    vHolder.imgView.setImageURI(choose[position]);
//                }else{
//                    vHolder.imgView.setImageURI(imgs[position]);
//                }
            } else if (arrorFlag) {
            }
            if(currentIndex==position){
                vHolder.titleView.setSelected(true);
                vHolder.imgView.setSelected(true);
                vHolder.titleView.setTextColor(mContext.getResources().getColor(R.color.title_bg));
                if(type==1||type==2) {
                    vHolder.line1View.setVisibility(View.VISIBLE);
                }
            }else{
                vHolder.titleView.setSelected(false);
                vHolder.imgView.setSelected(false);
                vHolder.titleView.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                if(type==1||type==2) {
                    vHolder.line1View.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        protected View newView(Context context, int i, ViewGroup viewgroup) {
            View view = LayoutInflater.from(context).inflate(R.layout.top_grid_item, null);
            ViewHolder holder = new ViewHolder();
            holder.titleView = (TextView) view.findViewById(R.id.title);
            holder.lineView = view.findViewById(R.id.line);
            holder. line1View= view.findViewById(R.id.line1);
            holder.imgView=(ImageView) view.findViewById(R.id.img);
            if (height != 0) {
                View mainView = view.findViewById(R.id.main);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                mainView.setLayoutParams(lp);
                lp = new RelativeLayout.LayoutParams(1, height - Utils.convertDIP2PX(context, 15));
                lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                holder.lineView.setLayoutParams(lp);
            }
            if(type==1||type==2){
                holder.imgView.setVisibility(View.GONE);
                holder. line1View.setVisibility(View.VISIBLE);
            }
            view.setPadding(0, padding, 0, padding);
            view.setTag(holder);
            return view;
        }


        public void changeTitle(int position, String text) {
            dataList.set(position, text);
        }

        private class ViewHolder {
            TextView titleView;
            View lineView,line1View;
            ImageView imgView;
        }
    }

}
