package com.platform.cdcs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.model.DistCustomerList;
import com.platform.cdcs.model.LetterGroup;
import com.platform.cdcs.widget.SectionedRecyclerViewAdapter;
import com.trueway.app.uilib.tool.Utils;

/**
 * Created by holytang on 2017/10/31.
 */
public class LetterAcountAdapter extends SectionedRecyclerViewAdapter<LetterAcountAdapter.MainVH> {


    private int mLineNumber = 0;
    private SparseArray<Integer> mKeyPositionMap = new SparseArray<>();
    private Context context;
    private LetterGroup dataModel;
    private AdapterView.OnItemClickListener mOnItemClickLitener;

    public LetterAcountAdapter(Context context) {
        this.context = context;
    }

    private void init() {
        calculateSectionPosition();
    }

    public Integer getSectionPosition(int asciiPosition) {
        return mKeyPositionMap.get(asciiPosition);
    }

    private void calculateSectionPosition() {
        int pos = 0;
        int i = 0;
        for (LetterGroup.LetterGroupItem group : dataModel.getGroupList()) {
            if (group.getTitle().equals("#")) {
                mKeyPositionMap.put(0, i);
            } else {
                mKeyPositionMap.put(group.getTitle().charAt(0) - 'A' + 1, i);
            }
            i += group.getSubList().size();
            i++;
        }
        mLineNumber = pos;
    }

    public void setOnItemClickLitener(AdapterView.OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public LetterGroup getDataModel() {
        return dataModel;
    }

    public void setDataModel(LetterGroup dataModel) {
        this.dataModel = dataModel;
        init();
    }


    @Override
    public int getSectionCount() {
        if (dataModel == null) {
            return 0;
        }
        return dataModel.getGroupList().size();
    }

    @Override
    public int getItemCount(int section) {
        return dataModel.getGroupList().get(section).getSubList().size();
    }

    @Override
    public void onBindHeaderViewHolder(MainVH holder, int section) {
        holder.titleView.setText(dataModel.getGroupList().get(section).getTitle());
    }

    @Override
    public void onBindFooterViewHolder(MainVH holder, int index) {

    }

    @Override
    protected boolean isFooter(int position) {
        return false;
    }

    @Override
    public void onBindViewHolder(MainVH holder, final int section, final int relativePosition, int absolutePosition) {
        DistCustomerList.Customer item = (DistCustomerList.Customer) dataModel.getGroupList().get(section).getSubList().get(relativePosition);
        holder.titleView.setText(item.getCustName());
        holder.titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickLitener.onItemClick(null, null, section, relativePosition);
            }
        });
    }


    @Override
    public MainVH onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(context);
        return new MainVH(textView, viewType, parent.getContext());
    }

    public void clear() {
        this.dataModel.getGroupList().clear();
    }

    public LetterGroup.LetterGroupItem get(int section) {
        return this.dataModel.getGroupList().get(section);
    }

    public class MainVH extends RecyclerView.ViewHolder {
        private TextView titleView;

        public MainVH(View itemView, int viewType, Context context) {
            super(itemView);
            titleView = (TextView) itemView;
            if (viewType == VIEW_TYPE_HEADER) {
            } else {
                titleView.setBackgroundResource(R.drawable.shape_corner_center);
            }
            titleView.setTextSize(14);
            int padding = Utils.convertDIP2PX(context, 12);
            titleView.setPadding(padding, padding, padding, padding);
            titleView.setTextColor(context.getResources().getColor(R.color.text_dark));
        }
    }
}
