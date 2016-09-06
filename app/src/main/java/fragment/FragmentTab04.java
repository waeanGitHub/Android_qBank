package fragment;

import android.app.Fragment;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waean.asus.android_qbank.QuestionActivity;
import com.waean.asus.android_qbank.QuestionItemActivity;
import com.waean.asus.android_qbank.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import adapter.QuestionAdapter;
import pojo.QuestionInfo;
import pojo.UserInfo;

/**
 * Created by Asus on 2016/08/31.
 */
public class FragmentTab04 extends Fragment {
    public static final String TAG = "FragmentTab04";
    private static final String url = "http://115.29.136.118:8080/web-question/app/mng/store?method=list";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;
    private Intent intent;
    List<QuestionInfo> questionInfos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collect_layout, null);
        mListView = (ListView) view.findViewById(R.id.colect_listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);
        getData();
        return view;
    }

    @Override
    public void onStart() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        super.onStart();
    }

    private void getData() {
        RequestParams requestParams = new RequestParams(url);
        requestParams.addParameter("userId", UserInfo.getUserInfo().getId() + "");

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.i(TAG, "onSuccess:FragmentTab04 " + result.toString());

                        questionInfos = new ArrayList<QuestionInfo>();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
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
                            }

                            bindAdater(questionInfos);


//                            intent = new Intent(getActivity(), QuestionActivity.class);
//                            intent.putExtra("question", (Serializable) questionInfos);
////                    intent.putExtra("name", name);
//                            startActivity(intent);
                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            intent = new Intent(getActivity(), QuestionActivity.class);
////                    intent.putExtra("name", name);
//                            startActivity(intent);
                        }
                    }

                    private void bindAdater(final List<QuestionInfo> questionInfos) {
                        if (questionInfos == null) {
                            Toast.makeText(getActivity(), "没有相关问题", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (questionInfos.size() != 0) {
                            QuestionAdapter adapter = new QuestionAdapter(getActivity(), questionInfos);
                            mListView.setAdapter(adapter);
                            addfoot();
                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(getActivity(), QuestionItemActivity.class);
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
                        Log.i(TAG, "onError: ----------" + ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Log.i(TAG, "onCancelled: ----------");
                    }

                    @Override
                    public void onFinished() {
                        Log.i(TAG, "onFinished: ----------");
                    }
                }

        );

    }

    private void addfoot() {
        TextView view = new TextView(getActivity());
        view.setText("已加载全部");
        view.setGravity(Gravity.CENTER);
        mListView.setFooterDividersEnabled(false);
        mListView.addFooterView(view);
    }

}
