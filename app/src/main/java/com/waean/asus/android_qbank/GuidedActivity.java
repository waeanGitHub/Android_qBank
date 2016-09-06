package com.waean.asus.android_qbank;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import pojo.UserInfo;

@ContentView(value = R.layout.activity_guided)
public class GuidedActivity extends AppCompatActivity {
    private static final String url = "http://115.29.136.118:8080/web-question/app/login";
    boolean isFristIn;/*是否第一次打开*/
    private SharedPreferences sharedPreferences;

    Handler handler = new Handler();
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_guided);

        x.view().inject(this);

        sharedPreferences = getSharedPreferences("isFristIn", MODE_PRIVATE);
        isFristIn = sharedPreferences.getBoolean("isFristIn", true);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFristIn) {
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putBoolean("isFristIn", false);
                    edit.commit();

                    startActivity(new Intent(GuidedActivity.this, GuidanceActivity.class));
                    finish();
                } else {
                    preferences = getSharedPreferences("user", MODE_PRIVATE);
                    String mUsername = preferences.getString("username", null);
                    String mPassword = preferences.getString("password", null);
                    Log.i("info", "onCreate: " + mUsername + ":" + mPassword);
                    if (!TextUtils.isEmpty(mUsername) && !TextUtils.isEmpty(mPassword)) {
                        Log.i("info", "onCreate: is called");
                        loginAccount(mUsername, mPassword);
                    } else {
                        startActivity(new Intent(GuidedActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }
        }, 3000);
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
//                            recodeLogin(username, password);
                            Toast.makeText(GuidedActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(GuidedActivity.this, MainActivity.class));
                            finish();
                            break;
                        case "false":
                            String reason = jsonObject.getString("reason");

                            Toast.makeText(GuidedActivity.this, "登录失败,账号密码过期" + reason, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(GuidedActivity.this, LoginActivity.class));
                            finish();
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
}
