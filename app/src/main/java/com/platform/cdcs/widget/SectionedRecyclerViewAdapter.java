package com.platform.cdcs.widget;

import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

public abstract class SectionedRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected final static int VIEW_TYPE_HEADER = -2;
    protected final static int VIEW_TYPE_ITEM = -1;
    protected final static int VIEW_TYPE_FOODER = 0;

    private final ArrayMap<Integer, Integer> mHeaderLocationMap;
    private GridLayoutManager mLayoutManager;
    private ArrayMap<Integer, Integer> mSpanMap;
    private boolean mShowHeadersForEmptySections;

    public SectionedRecyclerViewAdapter() {
        mHeaderLocationMap = new ArrayMap<>();
    }

    public abstract int getSectionCount();

    public abstract int getItemCount(int section);

    public abstract void onBindHeaderViewHolder(VH holder, int section);

    public abstract void onBindFooterViewHolder(VH holder, int index);

    public abstract void onBindViewHolder(VH holder, int section, int relativePosition, int absolutePosition);

    public final boolean isHeader(int position) {
        return mHeaderLocationMap.get(position) != null;
    }

    /**
     * Instructs the list view adapter to whether show headers for empty sections or not.
     *
     * @param show flag indicating whether headers for empty sections ought to be shown.
     */
    public final void shouldShowHeadersForEmptySections(boolean show) {
        mShowHeadersForEmptySections = show;
    }

    public final void setLayoutManager(GridLayoutManager lm) {
        mLayoutManager = lm;
        if (lm == null) return;
        lm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (isHeader(position))
                    return mLayoutManager.getSpanCount();
                else if(isFooter(position)){
                    return mLayoutManager.getSpanCount();
                }
                final int[] sectionAndPos = getSectionIndexAndRelativePosition(position);
                final int absPos = position - (sectionAndPos[0] + 1);
                return getRowSpan(mLayoutManager.getSpanCount(),
                        sectionAndPos[0], sectionAndPos[1], absPos);
            }
        });
    }

    @SuppressWarnings("UnusedParameters")
    protected int getRowSpan(int fullSpanSize, int section, int relativePosition, int absolutePosition) {
        return 1;
    }

    // returns section along with offsetted position
    private int[] getSectionIndexAndRelativePosition(int itemPosition) {
        synchronized (mHeaderLocationMap) {
            Integer lastSectionIndex = -1;
            for (final Integer sectionIndex : mHeaderLocationMap.keySet()) {
                if (itemPosition > sectionIndex) {
                    lastSectionIndex = sectionIndex;
                } else {
                    break;
                }
            }
            return new int[]{mHeaderLocationMap.get(lastSectionIndex), itemPosition - lastSectionIndex - 1};
        }
    }


    @Override
    public final int getItemCount() {
        int count = 0;
        mHeaderLocationMap.clear();
        for (int s = 0; s < getSectionCount(); s++) {
            int itemCount = getItemCount(s);
            if (mShowHeadersForEmptySections || (itemCount > 0)) {
                mHeaderLocationMap.put(count, s);
                count += itemCount + 1;
            }
        }
        return count;
    }

    protected boolean isFooter(int position) {
        return false;
    }

    /**
     * @hide
     * @deprecated
     */
    @Override
    @Deprecated
    public final int getItemViewType(int position) {
        if (isHeader(position)) {
            return getHeaderViewType(mHeaderLocationMap.get(position));
        } else if (isFooter(position)) {
            return VIEW_TYPE_FOODER;
        } else {
            final int[] sectionAndPos = getSectionIndexAndRelativePosition(position);
            return getItemViewType(sectionAndPos[0],
                    // offset section view positions
                    sectionAndPos[1],
                    position - (sectionAndPos[0] + 1));
        }
    }

    public int getHeaderViewType(int section) {
        //noinspection ResourceType
        return VIEW_TYPE_HEADER;
    }

    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        //noinspection ResourceType
        return VIEW_TYPE_ITEM;
    }

    /**
     * @hide
     * @deprecated
     */
    @Override
    @Deprecated
    public final void onBindViewHolder(VH holder, int position) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = null;
        if (holder.itemView.getLayoutParams() instanceof GridLayoutManager.LayoutParams)
            layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        else if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams)
            layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        if (isHeader(position)) {
            if (layoutParams != null) layoutParams.setFullSpan(true);
            onBindHeaderViewHolder(holder, mHeaderLocationMap.get(position));
        } else if (isFooter(position)) {
            if (layoutParams != null) layoutParams.setFullSpan(true);
            onBindFooterViewHolder(holder, position);
        } else {
            if (layoutParams != null) layoutParams.setFullSpan(false);
            final int[] sectionAndPos = getSectionIndexAndRelativePosition(position);
            final int absPos = position - (sectionAndPos[0] + 1);
            onBindViewHolder(holder, sectionAndPos[0],
                    // offset section view positions
                    sectionAndPos[1], absPos);
        }
        if (layoutParams != null)
            holder.itemView.setLayoutParams(layoutParams);
    }

}