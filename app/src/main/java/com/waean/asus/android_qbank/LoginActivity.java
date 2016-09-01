package com.waean.asus.android_qbank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(value = R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {
    /*登录url*/
    private static final String url = "http://115.29.136.118:8080/web-question/app/login";
    @ViewInject(value = R.id.et_username)
    private EditText username;
    @ViewInject(value = R.id.et_password)
    private EditText password;
    @ViewInject(value = R.id.btn_login)
    private Button login;
    @ViewInject(value = R.id.tv_forget)
    private TextView forget;
    @ViewInject(value = R.id.tv_register)
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        x.view().inject(this);
    }

    @Event(value = R.id.btn_login, type = View.OnClickListener.class)
    private void Onclick(View view) {
        /*登录*/
        String mUsername = username.getText().toString();
        String mPassword = password.getText().toString();
        loginAccount(mUsername, mPassword);
    }

    private void loginAccount(String mUsername, String mPassword) {



        RequestParams requestParams = new RequestParams(url);
        requestParams.addParameter("username", mUsername);
        requestParams.addParameter("password", mPassword);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.i("LoginActivity", jsonObject.toString());

                    String success = jsonObject.getString("success");
                    switch (success) {
                        case "true":
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            break;
                        case "false":
                            String reason = jsonObject.getString("reason");
                            Toast.makeText(LoginActivity.this, "登录失败," + reason, Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    @Event(value = {R.id.tv_forget, R.id.tv_register}, type = View.OnClickListener.class)
    private void clickTextView(View view) {
        switch (view.getId()) {
            case R.id.tv_forget:
                /*忘记密码点击事件*/
                break;
            case R.id.tv_register:
                /*注册账号事件*/
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

}
