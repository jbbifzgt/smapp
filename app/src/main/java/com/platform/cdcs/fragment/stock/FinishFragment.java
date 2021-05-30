package com.platform.cdcs.fragment.stock;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.trueway.app.uilib.fragment.BaseFragment;

/**
 * Created by holytang on 2017/10/6.
 */
public class FinishFragment extends BaseFragment {

    private TextView textView;
    private int num = 3;
    private Runnable clock = new Runnable() {
        @Override
        public void run() {
            textView.setText(String.format("%s 秒后返回产品首页", num));
            num--;
            if (num != 0) {
                textView.postDelayed(this, 1000);
            }else{
                textView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                }, 1000);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        setTitle("出库成功");
        textView = (TextView) view.findViewById(R.id.text);
        textView.post(clock);
    }

    @Override
    public int layoutId() {
        return R.layout.success;
    }
}
