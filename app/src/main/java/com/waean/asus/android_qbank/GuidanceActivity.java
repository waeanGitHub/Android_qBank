package com.waean.asus.android_qbank;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import adapter.GuidViewPagerAdapter;

@ContentView(value = R.layout.activity_guidance)
public class GuidanceActivity extends AppCompatActivity implements View.OnClickListener {
    @ViewInject(value = R.id.txt_gogoing)
    private TextView going;
    @ViewInject(value = R.id.dot1)
    private ImageView dot1;
    @ViewInject(value = R.id.dot2)
    private ImageView dot2;
    @ViewInject(value = R.id.dot3)
    private ImageView dot3;
    @ViewInject(value = R.id.guided_viewpager)
    private ViewPager viewPager;
    @ViewInject(value = R.id.ll_dot)
    private LinearLayout ll_dot;
    private List<View> viewList;
    private GuidViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_guidance);


        x.view().inject(this);

        initViewList();/*初始化视图*/
        pagerAdapter = new GuidViewPagerAdapter(viewList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < ll_dot.getChildCount(); i++) {
                    if (i == position) {
                        ll_dot.getChildAt(i).setSelected(true);
                    } else {
                        ll_dot.getChildAt(i).setSelected(false);
                    }
                    if (position == ll_dot.getChildCount() - 1) {
                        going.setVisibility(View.VISIBLE);
                    } else {
                        going.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /*按钮点击事件*/
        going.setOnClickListener(this);
    }

    private void initViewList() {
        viewList = new ArrayList<View>();
        View view1 = LayoutInflater.from(this).inflate(R.layout.guid_view1, null);
        viewList.add(view1);
        View view2 = LayoutInflater.from(this).inflate(R.layout.guid_view2, null);
        viewList.add(view2);
        View view3 = LayoutInflater.from(this).inflate(R.layout.guid_view3, null);
        viewList.add(view3);
        dot1.setSelected(true);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(GuidanceActivity.this, LoginActivity.class));
        finish();
    }
}
