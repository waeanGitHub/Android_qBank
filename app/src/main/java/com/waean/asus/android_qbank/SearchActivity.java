package com.waean.asus.android_qbank;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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

import adapter.QuestionAdapter;
import pojo.QuestionInfo;

@ContentView(value = R.layout.activity_search)
public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private static final String url = "http://115.29.136.118:8080/web-question/app/question?method=list";
    @ViewInject(value = R.id.search_listview)
    private ListView mListView;
    @ViewInject(value = R.id.et_search)
    private EditText et_search;
    @ViewInject(value = R.id.tv_mSearch)
    private TextView mSearch;
    @ViewInject(value = R.id.search_toolbar)
    private Toolbar mToolBar;
    private List<QuestionInfo> questionInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
        x.view().inject(this);
        mToolBar.setTitle("题目查询");
        mToolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolBar);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Event(value = R.id.tv_mSearch, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_mSearch:
                Toast.makeText(SearchActivity.this, "点击了搜索", Toast.LENGTH_SHORT).show();
                RequestParams requestParams = new RequestParams(url);
                requestParams.addParameter("questionName", et_search.getText().toString());
                x.http().post(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.i(TAG, "onSuccess: " + result.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int totalElements = jsonObject.getInt("totalElements");
                            if (totalElements > 0) {
                                questionInfos = new ArrayList<QuestionInfo>();
                                JSONArray jsonArray = (JSONArray) jsonObject.get("content");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String content = object.getString("content");
                                    int id = object.getInt("id");
                                    String pubTime = object.getString("pubTime");
                                    int typeid = object.getInt("typeid");
                                    String answer = object.getString("answer");
                                    int cataid = object.getInt("cataid");
                                    questionInfos.add(new QuestionInfo(content, id, pubTime, typeid, answer, cataid));
                                    System.out.println(questionInfos.get(i).toString());


                                    bindAdater(questionInfos);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    private void bindAdater(final List<QuestionInfo> questionInfos) {
                        if (questionInfos == null) {
                            Toast.makeText(SearchActivity.this, "没有相关问题", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (questionInfos.size() != 0) {
                            QuestionAdapter adapter = new QuestionAdapter(SearchActivity.this, questionInfos);
                            mListView.setAdapter(adapter);
                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(SearchActivity.this, QuestionItemActivity.class);
                                    int id = questionInfos.get(i).getId();
                                    intent.putExtra("size", questionInfos.size());
                                    intent.putExtra("where", i);
                                    intent.putExtra("id", id);
                                    startActivity(intent);

                                }
                            });


                        }

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.i(TAG, "onError: =========" + ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Log.i(TAG, "onCancelled: =========");

                    }

                    @Override
                    public void onFinished() {
                        Log.i(TAG, "onFinished: =========");
                    }
                });
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
