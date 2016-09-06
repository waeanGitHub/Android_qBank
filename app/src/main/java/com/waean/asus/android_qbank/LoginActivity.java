package com.waean.asus.android_qbank;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
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

import pojo.UserInfo;

@ContentView(value = R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {
    /*登录url*/
    private static final String url = "http://115.29.136.118:8080/web-question/app/login";
    @ViewInject(value = R.id.login_toolbar)
    private Toolbar mToolbar;
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
    private SharedPreferences preferences;
    //    ClearReceiver receiver;
    private AlertDialog show;
    private PopupWindow pWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        x.view().inject(this);

        mToolbar.setTitle("登录");
        setSupportActionBar(mToolbar);


//        registerClearReceiver();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receiver);
    }

//    /*注册receiver */
//    private void registerClearReceiver() {
//        receiver = new ClearReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("remember");
//        filter.addAction("clear");
//        LoginActivity.this.registerReceiver(receiver, filter);
//    }


    @Event(value = R.id.btn_login, type = View.OnClickListener.class)
    private void Onclick(View view) {
        /*登录*/
        String mUsername = username.getText().toString();
        String mPassword = password.getText().toString();
        loginAccount(mUsername, mPassword);

    }

    private void loginAccount(String mUsername, String mPassword) {

        if (flag) {
            showpupWin();
        }
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
                            UserInfo userInfo = UserInfo.getUserInfo();
                            JSONObject object = new JSONObject(jsonObject.getString("user"));
                            int id = object.getInt("id");
                            String username = object.getString("username");
                            String nickname = object.getString("nickname");
                            String password = object.getString("password");
                            String telephone = object.getString("telephone");
                            userInfo.setId(id);
                            userInfo.setUsername(username);
                            userInfo.setNickname(nickname);
                            userInfo.setPassword(password);
                            userInfo.setTelephone(telephone);

                            Log.i("LoginActivity", "onSuccess: " + userInfo.toString());
                            recodeLogin(username, password);
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
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
                pWindow.dismiss();
            }
        });

    }

   /* private void showPrograss() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_item, null);
        builder.setView(view);
        show = builder.show();
    }*/

    private void recodeLogin(String username, String password) {
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("username", username);
        edit.putString("password", password);
        edit.commit();
    }


//    class ClearReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals("clear")) {
//                preferences.edit().clear().commit();
//                Log.i("info", "onReceive: is called ================");
//            } else if (intent.getAction().equals("remember")) {
//                recodeLogin(UserInfo.getUserInfo().getUsername(), UserInfo.getUserInfo().getPassword());
//            }
//        }
//    }

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

    private void showpupWin() {
        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_item, null);
        pWindow = new PopupWindow(view,
                400, 400, true);
        pWindow.setBackgroundDrawable(new BitmapDrawable());// 为了让对话框点击空白处消失，必须有这条语句
        pWindow.showAtLocation((ViewGroup) LoginActivity.this.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }

    /**
     * 这个函数在Activity创建完成之后会调用。悬浮窗需要依附在Activity上，如果Activity还没有完全建好就去
     * 调用showCartFloatView()，则会抛出异常
     *
     * @param hasFocus
     */
    boolean flag = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            flag = true;
        }
    }
}
