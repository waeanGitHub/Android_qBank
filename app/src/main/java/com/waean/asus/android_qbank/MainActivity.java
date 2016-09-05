package com.waean.asus.android_qbank;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import fragment.FragmentTab01;
import fragment.FragmentTab04;

@ContentView(value = R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(value = R.id.mFrameLayout)
    private GridView mFrameLayout;

    @ViewInject(value = R.id.main_toolbar)
    private Toolbar mToolbar;
    @ViewInject(value = R.id.dl_mine)
    private DrawerLayout mDrawerLayout;


    @ViewInject(value = R.id.tv_sort)
    private TextView mSort;
    @ViewInject(value = R.id.tv_search)
    private TextView mSearch;
    @ViewInject(value = R.id.tv_achievement)
    private TextView mAchievement;
    @ViewInject(value = R.id.tv_collect)
    private TextView mCollect;
    @ViewInject(value = R.id.tv_setting)
    private TextView mSetting;
    @ViewInject(value = R.id.tv_exit)
    private TextView mExit;

    private ActionBarDrawerToggle toggle;

    MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        x.view().inject(this);

        /*注册退出登录广播*/
        registerBroadcast();
        mToolbar.setTitle("分类练习");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        toggle.syncState();
        mDrawerLayout.setDrawerListener(toggle);


        setDefultFragment();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);


    }

    private void registerBroadcast() {
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("exit");
        MainActivity.this.registerReceiver(myReceiver, filter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.search:
                searching();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*关联fragment布局*/
    private void setDefultFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FragmentTab01 tab01 = new FragmentTab01();
        transaction.add(R.id.mFrameLayout, tab01, "tab01");
        transaction.commit();
    }

    /*替换fragment布局*/
    private void replaceFragment(Fragment tab) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mFrameLayout, tab);
        transaction.commit();

    }

    @Event(value = {R.id.tv_sort, R.id.tv_search, R.id.tv_achievement,
            R.id.tv_collect, R.id.tv_setting, R.id.tv_exit}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        switch (view.getId()) {

            case R.id.tv_sort:
                FragmentTab01 tab01 = new FragmentTab01();
                replaceFragment(tab01);
                mToolbar.setTitle("分类练习");
                Toast.makeText(MainActivity.this, "分类练习", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_search:
                searching();
                Toast.makeText(MainActivity.this, "题目查找", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_achievement:
                Toast.makeText(MainActivity.this, "我的成就", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_collect:
                FragmentTab04 tab04 = new FragmentTab04();
                replaceFragment(tab04);
                mToolbar.setTitle("我的收藏");
                Toast.makeText(MainActivity.this, "我的收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_exit:
                Toast.makeText(MainActivity.this, "退出", Toast.LENGTH_SHORT).show();
                finish();
                break;


        }
    }

    /*跳转查找页面*/
    private void searching() {
        startActivity(new Intent(MainActivity.this, SearchActivity.class));
    }


    /*打开或关闭侧滑菜单*/
    private void openOrcloseDrawerLayout() {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(Gravity.LEFT);
        if (drawerOpen) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }


    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("exit")) {
                finish();
            }
        }
    }


}
