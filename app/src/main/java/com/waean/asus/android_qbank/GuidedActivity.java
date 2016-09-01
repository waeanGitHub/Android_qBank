package com.waean.asus.android_qbank;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

@ContentView(value = R.layout.activity_guided)
public class GuidedActivity extends AppCompatActivity {
    boolean isFristIn;/*是否第一次打开*/
    private SharedPreferences sharedPreferences;

    Handler handler = new Handler();
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
                    startActivity(new Intent(GuidedActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 3000);
    }

}
