package com.waean.asus.android_qbank;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.List;

import adapter.QuestionAdapter;
import pojo.QuestionInfo;

@ContentView(value = R.layout.activity_question)
public class QuestionActivity extends AppCompatActivity {
    private static final String TAG = "QuestionActivity";

    @ViewInject(value = R.id.question_toolbar)
    private Toolbar mToolBar;

    @ViewInject(value = R.id.question_listview)
    private ListView mLisview;
    private List<QuestionInfo> questionInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_question);
        x.view().inject(this);
        Intent it = getIntent();
        String title = it.getStringExtra("name");
        mToolBar.setTitle(title);
        mToolBar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        questionInfos = (List<QuestionInfo>) it.getSerializableExtra("question");
        if (questionInfos == null) {
            Toast.makeText(QuestionActivity.this, "没有相关问题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (questionInfos.size() != 0) {
            QuestionAdapter adapter = new QuestionAdapter(this, questionInfos);
            mLisview.setAdapter(adapter);
            mLisview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(QuestionActivity.this, QuestionItemActivity.class);
                    int id = questionInfos.get(i).getId();
                    intent.putExtra("size", questionInfos.size());
                    intent.putExtra("where", i);
                    intent.putExtra("id", id);
                    startActivity(intent);

                }
            });


        }


        Log.i(TAG, "onCreate: " + questionInfos.get(0).toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
