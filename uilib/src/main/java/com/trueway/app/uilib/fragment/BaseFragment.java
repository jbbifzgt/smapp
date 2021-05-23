package com.trueway.app.uilib.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.trueway.app.uilib.R;
import com.trueway.app.uilib.activity.BackListener;
import com.trueway.app.uilib.activity.BaseActivity;

/**
 * Created by holy on 16-11-17.
 */

public abstract class BaseFragment extends Fragment {

    protected Toolbar toolbar;
    protected EditText searchET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Toolbar getToolBar() {
        return ((BaseActivity) getActivity()).getToolBar();
    }

    public void initToolBar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.activity_main_toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setTitle("");
        }
    }

    public abstract void initView(View view);

    public void initView(View view, LayoutInflater inflater) {
        initView(view);
    }

    public abstract int layoutId();

    public void setTitle(String title) {
        ((BaseActivity) getActivity()).setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (layoutId() == 0) {
            return null;
        }
        View view = inflater.inflate(layoutId(), container, false);
        initView(view, inflater);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        initMenu(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected void initMenu(Menu menu, MenuInflater inflater) {

    }

    public void initLoadImg(View loadView) {
        ((BaseActivity) getActivity()).initLoadImg(loadView);
    }

    public void showLoadImg() {
        ((BaseActivity) getActivity()).showLoadImg();
    }

    public void dismissLoadImg() {
        ((BaseActivity) getActivity()).dismissLoadImg();
    }

    public void addBackListener(BackListener listener) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).addBackListener(listener);
        }
    }

    public void removeBackListener() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).removeBackListener();
        }
    }

    public void setFragmentTitle(String fragmentTitle) {
        ((TextView) toolbar.findViewById(R.id.title)).setText(fragmentTitle);
    }

    public void addRightView(View view){
        ( (ViewGroup)toolbar.findViewById(R.id.action_menu_view)).addView(view);
    }

    public void initSearch(String hint,final SearchListener listener) {
        if(toolbar==null){
            toolbar=getToolBar();
        }
        searchET = (EditText) toolbar.findViewById(R.id.search);
        toolbar.findViewById(R.id.title).setVisibility(View.GONE);
        searchET.setVisibility(View.VISIBLE);
        searchET.setHint(hint);
        searchET.setHintTextColor(getResources().getColor(R.color.text_gray));
        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (event != null && event.getAction() == KeyEvent.ACTION_UP) {
                        listener.search(searchET.getText().toString().trim());
                    } else if (actionId == EditorInfo.IME_ACTION_SEARCH && event == null) {
                        listener.search(searchET.getText().toString().trim());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void hideSoftInput(View view){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void hideThisToolBar(View view){
        view.findViewById(R.id.activity_main_toolbar).setVisibility(View.GONE);
    }
}
