package com.waean.asus.android_qbank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import pojo.Option;
import pojo.QuestionInfo;
import pojo.UserInfo;

@ContentView(value = R.layout.activity_question_item)
public class QuestionItemActivity extends AppCompatActivity {
    private static final String mUrl = "http://115.29.136.118:8080/web-question/app/question?method=findone";
    private static final String lastUrl = "http://115.29.136.118:8080/web-question/app/question?method=prev";
    private static final String nextUrl = "http://115.29.136.118:8080/web-question/app/question?method=next";

    @ViewInject(value = R.id.questionitem_toolbar)
    private Toolbar mToolBar;
    @ViewInject(value = R.id.question_tv_title)
    private TextView mTitle;
    @ViewInject(value = R.id.querstion_tv_answer)
    private TextView mAnswer;
    @ViewInject(value = R.id.question_scrollview)
    private ScrollView mScrollView;


    @ViewInject(value = R.id.question_tv_last)
    private TextView mLast;
    @ViewInject(value = R.id.question_tv_collect)
    private TextView mCollect;
    @ViewInject(value = R.id.question_tv_next)
    private TextView mNext;
    private int mId;
    private int where;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_question_item);
        x.view().inject(this);

        Intent it = getIntent();
        mId = it.getIntExtra("id", -1);
        where = it.getIntExtra("where", -1);
        size = it.getIntExtra("size", 0);
        getQuestionDetail(mId, mUrl);


        /*mToolBar.setTitle("Question");*/
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("第" + (where + 1) + "/" + size + "道题");
    }

    /*获取问题详情*/
    private void getQuestionDetail(int id, String url) {
        RequestParams params = new RequestParams(url);
        params.addParameter("id", id);
        params.addParameter("user_id", UserInfo.getUserInfo().getId());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("info", "onSuccess: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String content = jsonObject.getString("content");
                    int id = jsonObject.getInt("id");
                    mId = id;/*记录当前题目id*/
                    String pubTime = jsonObject.getString("pubTime");
                    int typeid = jsonObject.getInt("typeid");
                    String answer = jsonObject.getString("answer");
                    int cataid = jsonObject.getInt("cataid");
//                    boolean isCollect = jsonObject.getBoolean("isCollect");

                    QuestionInfo questionInfo = new QuestionInfo(content, id, pubTime, typeid, answer, cataid);

                    Log.i("info", "onSuccess:== " + questionInfo.toString());
                    if (questionInfo.getTypeid() == 1 || questionInfo.getTypeid() == 2) {
                        String options = jsonObject.getString("options");
                        JSONArray jsonArray = new JSONArray(options);
                        List<Option> optionList = new ArrayList<Option>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String title = jsonArray.getJSONObject(i).getString("title");
                            boolean checked = jsonArray.getJSONObject(i).getBoolean("checked");
                            optionList.add(new Option(title, checked));
                        }
                        showSelectText(questionInfo, optionList);

                    } else {
                        showText(questionInfo);
                        Log.i("info", "onSuccess: =========");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("info", "onSuccess: " + e.toString());
                }
            }

            private void showSelectText(QuestionInfo questionInfo, List<Option> optionList) {
                mTitle.setText(questionInfo.getContent());
                mScrollView.removeAllViews();
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.select_item, null);
                CheckBox check01 = (CheckBox) view.findViewById(R.id.check01);
                CheckBox check02 = (CheckBox) view.findViewById(R.id.check02);
                CheckBox check03 = (CheckBox) view.findViewById(R.id.check03);
                CheckBox check04 = (CheckBox) view.findViewById(R.id.check04);
                CheckBox[] checkBoxes = {check01, check02, check03, check04};
                /*check01.setText(optionList.get(0).getTitle());
                check02.setText(optionList.get(1).getTitle());
                check03.setText(optionList.get(2).getTitle());
                check04.setText(optionList.get(3).getTitle());*/
                Log.i("info", "showSelectText: " + optionList.size());
                for (int i = 0; i < optionList.size(); i++) {
                    Log.i("info", "showSelectText: " + optionList.get(i).getTitle());
                    checkBoxes[i].setText(optionList.get(i).getTitle());
                    boolean checked = optionList.get(i).isChecked();
                    if (checked) {
                        checkBoxes[i].setChecked(true);
                    }
                }
                mScrollView.addView(view);
                mScrollView.invalidate();

            }

            private void showText(QuestionInfo questionInfo) {
                mTitle.setText(questionInfo.getContent());
                mScrollView.removeAllViews();
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.answer_item, null);
                TextView mQuerstion_tv_answer = (TextView) view.findViewById(R.id.mQuerstion_tv_answer);
                mQuerstion_tv_answer.setText(questionInfo.getAnswer());
//                mAnswer.setText(questionInfo.getAnswer());
                mScrollView.addView(view);
                mScrollView.invalidate();

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
                if (where > 0) {
                    where--;
                    getSupportActionBar().setTitle("第" + (where + 1) + "/" + size + "道题");
                    getQuestionDetail(mId, lastUrl);
//                    Toast.makeText(QuestionItemActivity.this, "上一题", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuestionItemActivity.this, "已经是第一道题", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.question_tv_collect:
                Toast.makeText(QuestionItemActivity.this, "收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.question_tv_next:
                if (where < size - 1) {
                    where++;
                    getSupportActionBar().setTitle("第" + (where + 1) + "/" + size + "道题");
                    getQuestionDetail(mId, nextUrl);
//                    Toast.makeText(QuestionItemActivity.this, "下一题", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuestionItemActivity.this, "已经是最后一题", Toast.LENGTH_SHORT).show();
                }
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
