package com.trueway.app.uilib.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.trueway.app.uilib.R;
import com.trueway.app.uilib.adapter.EnhancedAdapter;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.Utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * @author 汤海泉
 * @date 2013-3-19
 */
public class TwDialogBuilder {

    private final Dialog mD;
    private Context mContext;
    private View parentPanel;
    private String defalutIndex;

    public TwDialogBuilder(Context mContext) {
        this.mContext = mContext;
        mD = new Dialog(mContext, R.style.IgDialog);
        this.mD.setContentView(R.layout.alert_dialog);
        parentPanel = mD.findViewById(R.id.parentPanel);
    }

    public TwDialogBuilder setRotate() {
        ImageView rotateImg = (ImageView) mD.findViewById(R.id.refresh_icon);
        rotateImg.setVisibility(View.VISIBLE);
        Animation localAnimation = AnimationUtils.loadAnimation(mContext,
                R.anim.rotate_spinner);
        localAnimation.reset();
        rotateImg.startAnimation(localAnimation);
        return this;
    }

    public TwDialogBuilder showProgress() {
        ProgressBar progress = (ProgressBar) mD.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        return this;
    }

    public ProgressBar getProgressBar() {
        ProgressBar progress = (ProgressBar) mD.findViewById(R.id.progress);
        return progress;
    }

    private void bindButton(int i,
                            final DialogInterface.OnClickListener onClickListener, int j,
                            final int buttonPositive) {
        View view = mD.findViewById(j);
        ((Button) view).setText(i);
        view.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view1) {
                if (onClickListener != null)
                    onClickListener.onClick(mD, buttonPositive);
                mD.dismiss();
            }

        });
        view.setVisibility(View.VISIBLE);
    }

    public Dialog create() {
        return this.mD;
    }

    public void show() {
        this.mD.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        this.mD.show();
    }

    public TwDialogBuilder setCancelable(boolean flag) {
        mD.setCancelable(flag);
        return this;
    }

    public TwDialogBuilder setItems(String[] args,
                                    final DialogInterface.OnClickListener listener, String defalutIndex) {
//      setTitle(R.string.attention);
        mD.findViewById(R.id.header).setVisibility(View.GONE);
        mD.findViewById(R.id.line).setVisibility(View.GONE);
        mD.findViewById(R.id.bar).setVisibility(View.GONE);
        this.defalutIndex = defalutIndex;
        ItemAdapter adapter = new ItemAdapter(mContext, Arrays.asList(args));
        ListView listview = (ListView) mD.findViewById(android.R.id.list);
        listview.setVisibility(View.VISIBLE);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (listener != null)
                    listener.onClick(mD, position);
                mD.dismiss();
            }
        });
        listview.setAdapter(adapter);
        return this;
    }

    public TwDialogBuilder setCheckItems(final List<ChooseItem> args,
                                         final DialogInterface.OnClickListener listener) {
        mD.findViewById(R.id.bar).setVisibility(View.GONE);
        final ChooseAdapter adapter = new ChooseAdapter(mContext, args);
        ListView listview = (ListView) mD.findViewById(android.R.id.list);
        listview.setVisibility(View.VISIBLE);
        Button button = new Button(mContext);
        button.setText("确定");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for (ChooseItem item : args) {
//                    if (item.getId().equals(defalutIndex)) {
//                        mD.dismiss();
//                        listener.onClick(null,-1);
//                        return;
//                    }
//                    i++;
//                }
                //隐藏键盘

                listener.onClick(null, -1);
                mD.dismiss();
            }
        });
        listview.addFooterView(button);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return this;
    }

    public TwDialogBuilder setItems( String title,String[] args,
                                    final DialogInterface.OnClickListener listener) {
        setTitle(title);
        ItemAdapter adapter = new ItemAdapter(mContext, Arrays.asList(args));
        ListView listview = (ListView) mD.findViewById(android.R.id.list);
        listview.setVisibility(View.VISIBLE);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (listener != null)
                    listener.onClick(mD, position);
                mD.dismiss();
            }
        });
        listview.setAdapter(adapter);
        mD.findViewById(android.R.id.list).setVisibility(View.VISIBLE);
        return this;
    }

    public TwDialogBuilder setItems(
            EnhancedAdapter<Map<String, String>> adapter,
            final DialogInterface.OnClickListener listener) {
        setTitle(R.string.attention);
        ListView listview = (ListView) mD.findViewById(android.R.id.list);
        listview.setVisibility(View.VISIBLE);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Map<String, String> item = (Map<String, String>) parent
                        .getItemAtPosition(position);
                if (listener != null) {
                    listener.onClick(mD, Integer.parseInt(item.get("nid")));
                }
                mD.dismiss();
            }
        });
        listview.setAdapter(adapter);
        mD.findViewById(android.R.id.list).setVisibility(View.VISIBLE);
        return this;
    }

    public TwDialogBuilder setMessage(int i) {
        View view = mD.findViewById(R.id.message);
        ((TextView) view).setText(i);
        view.setVisibility(View.VISIBLE);
        return this;
    }

    public TwDialogBuilder setMessage(String s) {
        View view = mD.findViewById(R.id.message);
        ((TextView) view).setText(s);
        view.setVisibility(View.VISIBLE);
        return this;
    }

    public TwDialogBuilder setStateMessage(int i) {
        View view = mD.findViewById(R.id.message);
        ((TextView) view).setText(mContext.getString(i));
        ((TextView) view).setMovementMethod(ScrollingMovementMethod
                .getInstance());
        view.setVisibility(View.VISIBLE);
        return this;
    }

    public TwDialogBuilder setNegativeButton(int i,
                                             DialogInterface.OnClickListener onclicklistener) {
        bindButton(i, onclicklistener, R.id.button2, -2);
        return this;
    }

    public TwDialogBuilder setNeutralButton(int i,
                                            DialogInterface.OnClickListener onclicklistener) {
        bindButton(i, onclicklistener, R.id.button3, -3);
        return this;
    }

    public TwDialogBuilder setPositiveButton(int i,
                                             DialogInterface.OnClickListener onclicklistener) {
        bindButton(i, onclicklistener, R.id.button1, -1);
        return this;
    }

    public TwDialogBuilder setTitle(int i) {
        View view = mD.findViewById(R.id.alertTitle);
        ((TextView) view).setText(i);
        view.setVisibility(View.VISIBLE);
        return this;
    }

    public TwDialogBuilder setTitle(String s) {
        View view = mD.findViewById(R.id.alertTitle);
        ((TextView) view).setText(s);
        view.setVisibility(View.VISIBLE);
        return this;
    }

    public EditText setEditView(String hint) {
        EditText viewgroup = (EditText) mD.findViewById(R.id.custom_bar);
        viewgroup.setVisibility(View.VISIBLE);
        viewgroup.setHint(hint);
        return viewgroup;
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
            TextView titleView = (TextView) ((LinearLayout) paramView)
                    .getChildAt(0);
            titleView.setText(title);
            if (title.equals(defalutIndex)) {
                titleView.setPressed(true);
            } else {
                titleView.setPressed(false);
            }
        }

        @Override
        protected View newView(Context context, int i, ViewGroup viewgroup) {
            TextView textView = new TextView(context);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            LinearLayout ll = new LinearLayout(context);
            ll.setLayoutParams(lp);
            int size = Utils.convertDIP2PX(context, 10);
            LinearLayout.LayoutParams textLp = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, size * 4);
            textLp.setMargins(0, size / 2, 0, size / 2);
            textView.setLayoutParams(textLp);
            try {
                int statePressed = android.R.attr.state_pressed;
                int[][] state = {{statePressed}, {-statePressed}};
                int color1 = mContext.getResources().getColor(R.color.color_green_normal);
                int color2 = mContext.getResources().getColor(R.color.text_dark);
                int[] color = {color1, color2};
                ColorStateList listColor = new ColorStateList(state, color);
                textView.setTextColor(listColor);
            } catch (Exception e) {
                textView.setTextColor(Color.BLACK);
            }

            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView.setSingleLine(true);
            textView.setGravity(Gravity.CENTER);
            ll.addView(textView);
            return ll;
        }
    }

    private class ChooseAdapter extends EnhancedAdapter<ChooseItem> {


        public ChooseAdapter(Context context, List<ChooseItem> items) {
            super(context);
            addAll(items);
        }

        @Override
        protected void bindView(View paramView, Context paramContext, int position) {
            final ViewHolder holder = (ViewHolder) paramView.getTag();
            ChooseItem item = getItem(position);
            if (item.isCheck()) {
                holder.checkBox.setSelected(true);
            } else {
                holder.checkBox.setSelected(false);
            }
            holder.checkBox.setTag(position);
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer position = (Integer) v.getTag();
                    if (getItem(position).isCheck()) {
                        getItem(position).setIsCheck(false);
                    } else {
                        getItem(position).setIsCheck(true);
                    }
                    notifyDataSetChanged();
                }
            });
            holder.checkBox.setText(item.getTitle());
            if (position == getCount() - 1) {
                holder.editText.setVisibility(View.VISIBLE);
                holder.headerView.setText(item.getTitle());
                holder.headerView.setVisibility(View.VISIBLE);
                holder.checkBox.setVisibility(View.GONE);
            } else {
                holder.editText.setVisibility(View.GONE);
                holder.headerView.setVisibility(View.GONE);
                holder.checkBox.setVisibility(View.VISIBLE);
            }
            holder.editText.setTag(position);
            holder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Integer position = (Integer) holder.editText.getTag();
                    getItem(position).setText(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            holder.editText.setText(item.getText());
        }

        @Override
        protected View newView(Context context, int position, ViewGroup viewgroup) {
            View rootView = LayoutInflater.from(context).inflate(R.layout.check_layout, null);
            ViewHolder holder = new ViewHolder();
            holder.checkBox = (TextView) rootView.findViewById(R.id.check);
            holder.editText = (EditText) rootView.findViewById(R.id.content);
            holder.headerView = (TextView) rootView.findViewById(R.id.title);
            rootView.setTag(holder);
            return rootView;
        }

        private class ViewHolder {
            TextView checkBox;
            EditText editText;
            TextView headerView;
        }
    }
}
