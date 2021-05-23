package com.platform.cdcs.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.platform.cdcs.R;
import com.trueway.app.uilib.activity.BaseActivity;
import com.trueway.app.uilib.tool.PermissionsUtil;
import com.trueway.app.uilib.tool.Utils;

/**
 * Created by holytang on 2017/9/20.
 */
public class NewLoginActivity extends BaseActivity {
    private EditText nameET;
    private EditText passET;

    @Override
    protected void initLayout() {
        setContentView(R.layout.login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = PermissionsUtil.PERMISSION_WRITE_EXTERNAL_STORAGE;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            for (String str : permissions) {
                if (getActivity().checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PermissionsUtil.PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.showToast(getActivity(), "权限已设置");
            }
        }
    }


    @Override
    protected void initView() {
        nameET = (EditText) findViewById(R.id.name);
        passET = (EditText) findViewById(R.id.text);
        final View cancel1 = findViewById(R.id.button1);
        cancel1.setVisibility(View.GONE);
        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameET.setText("");
            }
        });
        nameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(nameET.getText().toString())) {
                    cancel1.setVisibility(View.GONE);
                } else {
                    cancel1.setVisibility(View.VISIBLE);
                }
            }
        });
        final View cancel2 = findViewById(R.id.button2);
        cancel2.setVisibility(View.GONE);
        cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passET.setText("");
            }
        });
        passET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(passET.getText().toString())) {
                    cancel2.setVisibility(View.GONE);
                } else {
                    cancel2.setVisibility(View.VISIBLE);
                }
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeLogin();
            }
        });
    }

    private void executeLogin() {
        Intent intent = new Intent(NewLoginActivity.this, MainTabActivity.class);
        startActivity(intent);
    }
}
