package com.waean.asus.android_qbank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import pojo.QuestionInfo;

@ContentView(value = R.layout.activity_question_item)
public class QuestionItemActivity extends AppCompatActivity {
    private static final String url = "http://115.29.136.118:8080/web-question/app/question?method=findone";
    @ViewInject(value = R.id.questionitem_toolbar)
    private Toolbar mToolBar;
    @ViewInject(value = R.id.question_tv_title)
    private TextView mTitle;
    @ViewInject(value = R.id.querstion_tv_answer)
    private TextView mAnswer;

    @ViewInject(value = R.id.question_tv_last)
    private TextView mLast;
    @ViewInject(value = R.id.question_tv_collect)
    private TextView mCollect;
    @ViewInject(value = R.id.question_tv_next)
    private TextView mNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_question_item);
        x.view().inject(this);

        Intent it = getIntent();
        int id = it.getIntExtra("id", -1);
        getQuestionDetail(id);


        mToolBar.setTitle("Question");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*获取问题详情*/
    private void getQuestionDetail(int id) {
        RequestParams params = new RequestParams(url);
        params.addParameter("id", id);
        params.addParameter("user_id", 2);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("info", "onSuccess: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String content = jsonObject.getString("content");
                    int id = jsonObject.getInt("id");
                    String pubTime = jsonObject.getString("pubTime");
                    int typeid = jsonObject.getInt("typeid");
                    String answer = jsonObject.getString("answer");
                    int cataid = jsonObject.getInt("cataid");
//                    boolean isCollect = jsonObject.getBoolean("isCollect");

                    QuestionInfo questionInfo = new QuestionInfo(content, id, pubTime, typeid, answer, cataid);
                    Log.i("info", "onSuccess:== " + questionInfo.toString());

                    showText(questionInfo);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            private void showText(QuestionInfo questionInfo) {
                mTitle.setText(questionInfo.getContent());
                mAnswer.setText(questionInfo.getAnswer());

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

    @Event(value = {R.id.question_tv_last, R.id.question_tv_collect, R.id.question_tv_next}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.question_tv_last:
                Toast.makeText(QuestionItemActivity.this, "上一题", Toast.LENGTH_SHORT).show();
                break;
            case R.id.question_tv_collect:
                Toast.makeText(QuestionItemActivity.this, "收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.question_tv_next:
                Toast.makeText(QuestionItemActivity.this, "下一题", Toast.LENGTH_SHORT).show();
                break;
        }
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
