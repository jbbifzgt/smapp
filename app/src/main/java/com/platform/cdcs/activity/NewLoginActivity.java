package com.platform.cdcs.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.platform.cdcs.MyApp;
import com.platform.cdcs.R;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.PersonModel;
import com.platform.cdcs.tool.Constant;
import com.trueway.app.uilib.activity.BaseActivity;
import com.trueway.app.uilib.tool.Md5;
import com.trueway.app.uilib.tool.PermissionsUtil;
import com.trueway.app.uilib.tool.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

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
            if (MyApp.getInstance().getAccount().isLogin()) {
                gotoMain(true);
            }
        } else {
            if (MyApp.getInstance().getAccount().isLogin()) {
                gotoMain(true);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PermissionsUtil.PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.showToast(getActivity(), "权限已设置");
                if (MyApp.getInstance().getAccount().isLogin()) {
                    gotoMain(true);
                }
            }
        }
    }


    @Override
    protected void initView() {
        initLoadImg(findViewById(R.id.load));
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
        nameET.setText("MDMUD022406-1");
        passET.setText("qs123456");
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
        if (MyApp.getInstance().getAccount() != null && !TextUtils.isEmpty(MyApp.getInstance().getAccount().getUserId())) {
            nameET.setText(MyApp.getInstance().getAccount().getUserId());
            nameET.setSelection(nameET.getText().length());
            passET.setText(MyApp.getInstance().getAccount().getPassword());
            passET.setSelection(passET.getText().length());
        }
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeLogin();
            }
        });
        if (!TextUtils.isEmpty(nameET.getText().toString())) {
            cancel1.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(passET.getText().toString())) {
            cancel2.setVisibility(View.VISIBLE);
        }
    }

    private Map<String, String> getParam(String name, String pwd) {
        Map<String, String> param = new HashMap<>();
        param.put("userId", name);
        param.put("password", Md5.encode(pwd));
        param.put("phoneImei", "");
        param.put("sdkVersion", "");
        param.put("phoneModel", "");
        param.put("macAddress", "");
        param.put("release", "");
        param.put("sysType", "2");
        param.put("softVercode", "");
        Map<String, String> params = Constant.makeLoginParam(new JSONObject(param).toString());
        return params;
    }

    private void executeLogin() {

        final String name = nameET.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Utils.showToast(getActivity(), "用户名不能为空");
            return;
        }
        final String pwd = passET.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Utils.showToast(getActivity(), "密码不能为空");
            return;
        }
        showLoadImg();

        getHttpClient().post().params(getParam(name, pwd)).url(Constant.LOGIN_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getActivity(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                LoginResponse response = new Gson().fromJson(s, LoginResponse.class);
                if ("1".equals(response.getResult().getCode())) {
                    response.getResult().setToken(response.getApptoken());
                    response.getResult().setLogin(true);
                    response.getResult().setPassword(passET.getText().toString());
                    MyApp.getInstance().setAccount(response.getResult());
                    gotoMain(false);
                } else {
                    Utils.showToast(getActivity(), response.getResult().getMsg());
                }
            }
        });

    }

    private void gotoMain(boolean flag) {
        if (flag) {
            showLoadImg();
            getHttpClient().post().params(getParam(MyApp.getInstance().getAccount().getUserId(), MyApp.getInstance().getAccount().getPassword())).url(Constant.LOGIN_URL).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    dismissLoadImg();
                    Utils.showToast(getActivity(), R.string.server_error);
                }

                @Override
                public void onResponse(String s, int i) {
                    dismissLoadImg();
                    LoginResponse response = new Gson().fromJson(s, LoginResponse.class);
                    if ("1".equals(response.getResult().getCode())) {
                        response.getResult().setToken(response.getApptoken());
                        response.getResult().setLogin(true);
                        response.getResult().setPassword(passET.getText().toString());
                        MyApp.getInstance().setAccount(response.getResult());
                        gotoMain(false);
                    } else {
                        Utils.showToast(getActivity(), response.getResult().getMsg());
                    }
                }
            });
        } else {
            Intent intent = new Intent(NewLoginActivity.this, MainTabActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class LoginResponse extends BaseObjResponse<PersonModel> {

    }
}
