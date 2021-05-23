package com.platform.cdcs.fragment.account;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.platform.cdcs.fragment.custom.AddRegNumberFragment;
import com.platform.cdcs.tool.FragmentUtil;
import com.sherchen.slidetoggleheader.views.ObservableXListView;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.LetterBar;

/**
 * Created by holytang on 2017/9/28.
 */
public class AccountListFragment extends BaseFragment {

    private ObservableXListView slideListView;
    private LetterBar letterBar;
    private ItemAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(getContext());
    }

    @Override
    public void initView(View view) {
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setHasOptionsMenu(true);
        setTitle("我的客户");
        view.findViewById(R.id.search_layout).setVisibility(View.VISIBLE);
        letterBar = (LetterBar) view.findViewById(R.id.letter_bar);
        letterBar.setOnLetterSelectListener(new LetterBar.OnLetterSelectListener() {
            @Override
            public void onLetterSelect(int position, String letter, boolean confirmed) {
                Integer sectionPosition = adapter.getSectionPosition(position);
                if (sectionPosition != null)
                    slideListView.setSelection(sectionPosition);
            }
        });
        slideListView = (ObservableXListView) view.findViewById(android.R.id.list);
        slideListView.setAdapter(adapter);
    }

    @Override
    protected void initMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, "新增客户").setTitle("新增客户").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentUtil.navigateToInNewActivity(getActivity(), AccountRegFragment.class, null);
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public int layoutId() {
        return R.layout.letter_list_view;
    }

    private class ItemAdapter extends EnhancedAdapter<ChooseItem> {

        private int mLineNumber = 0;
        private SparseArray<Integer> mKeyPositionMap = new SparseArray<>();

        public ItemAdapter(Context context) {
            super(context);
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
//            for (PatientModel.PatientGroup group : dataModel.getGroupList()) {
//                mKeyPositionMap.put(group.getTitle().charAt(0) - 'A' + 1, i);
//                i += group.getPatientList().size();
//                i++;
//            }
            mLineNumber = pos;
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            TextView textView = (TextView) paramView;
            textView.setBackgroundResource(R.drawable.shape_corner_center);
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            TextView textView = new TextView(context);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
            textView.setTextSize(14);
            int padding = Utils.convertDIP2PX(getContext(), 12);
            textView.setPadding(padding, padding, padding, padding);
            textView.setTextColor(getResources().getColor(R.color.text_black));
            textView.setLayoutParams(lp);
            return textView;
        }

    }
}
