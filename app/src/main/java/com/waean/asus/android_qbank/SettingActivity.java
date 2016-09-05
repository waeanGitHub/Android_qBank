package com.waean.asus.android_qbank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import pojo.UserInfo;

@ContentView(value = R.layout.activity_setting)
public class SettingActivity extends AppCompatActivity {
    @ViewInject(value = R.id.setting_toolbar)
    private Toolbar mToolBar;
    @ViewInject(value = R.id.set_nickname)
    private TextView set_nickname;
    @ViewInject(value = R.id.login_auto)
    private LinearLayout login_auto;
    @ViewInject(value = R.id.login_auto_txt)
    private TextView login_auto_txt;
    @ViewInject(value = R.id.login_auto_check)
    private CheckBox login_auto_check;

    @ViewInject(value = R.id.show_pic)
    private LinearLayout show_pic;
    @ViewInject(value = R.id.show_pic_txt)
    private TextView show_pic_txt;
    @ViewInject(value = R.id.show_pic_check)
    private CheckBox show_pic_check;

    @ViewInject(value = R.id.exit_login)
    private TextView exit_login;
    @ViewInject(value = R.id.delete_cache)
    private TextView delete_cache;
    @ViewInject(value = R.id.about)
    private TextView about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
        x.view().inject(this);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setTitle("设置");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Event(value = {R.id.set_nickname, R.id.login_auto, R.id.show_pic, R.id.exit_login, R.id.delete_cache, R.id.about}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.set_nickname:

                break;
            case R.id.login_auto:
                if (login_auto_check.isChecked()) {
                    login_auto_check.setChecked(false);
                    login_auto_txt.setText("未开启");
                    clearSharedPreference();
                } else {
                    login_auto_check.setChecked(true);
                    login_auto_txt.setText("已开启");
                    rememberSharedPreference();
                }
                break;
            case R.id.show_pic:
                if (show_pic_check.isChecked()) {
                    show_pic_check.setChecked(false);
                    show_pic_txt.setText("仅通过WIFI");
                } else {
                    show_pic_check.setChecked(true);
                    show_pic_txt.setText("在3G/4G和WIFI下都显示图片");
                }
                break;
            case R.id.exit_login:
                Intent intent = new Intent();
                intent.setAction("exit");
                sendBroadcast(intent);
//                sendClear();
                clearSharedPreference();

                finish();

                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                break;
            case R.id.delete_cache:
                Toast.makeText(SettingActivity.this, "执行清除缓存的操作", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                Toast.makeText(SettingActivity.this, "启动“关于我们”页面...", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    private void rememberSharedPreference() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("username", UserInfo.getUserInfo().getUsername());
        edit.putString("password", UserInfo.getUserInfo().getPassword());
        edit.commit();

    }

    private void clearSharedPreference() {

        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

//    private void sendRemember() {
//        Intent intent = new Intent();
//        intent.setAction("remember");
//        sendBroadcast(intent);
//    }
//
//    /*清除首选项*/
//    private void sendClear() {
//        Log.i("info", "sendClear: is called");
//        Intent intent = new Intent();
//        intent.setAction("clear");
//        sendBroadcast(intent);
//    }
}
