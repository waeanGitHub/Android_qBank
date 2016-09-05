package fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.GridView;
import android.widget.Toast;

import com.waean.asus.android_qbank.QuestionActivity;
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

import adapter.GridViewAdapter;
import pojo.QuestionInfo;
import pojo.SortInfo;

/**
 * Created by Asus on 2016/08/30.
 */
public class FragmentTab01 extends Fragment {
    private static final String TAG = "FragmentTab01";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GridView gridView;
    private List<SortInfo> list;

    private static final String sUrl = "http://115.29.136.118:8080/web-question/app/catalog?method=list";
    private static final String qUrl = "http://115.29.136.118:8080/web-question/app/question?method=list";
    private View view;

    private Handler handler = new Handler();
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_layout, null);
        gridView = (GridView) view.findViewById(R.id.mGridview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);
        getData();
        return view;
    }

    @Override
    public void onStart() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = list.get(i).getName();
                getListQuestion(list.get(i).getId(), name);

                System.out.println("点击了第" + (i + 1) + "项");
            }


        });

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

    private void getListQuestion(int mId, final String name) {
        RequestParams params = new RequestParams(qUrl);
        params.addParameter("catalogId", mId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                List<QuestionInfo> questionInfos;
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
                    intent = new Intent(getActivity(), QuestionActivity.class);
                    intent.putExtra("question", (Serializable) questionInfos);
                    intent.putExtra("name", name);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    intent = new Intent(getActivity(), QuestionActivity.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
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

    private void getData() {
        RequestParams params = new RequestParams(sUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                list = new ArrayList<SortInfo>();

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));
                        int id = object.getInt("id");
                        String icon = object.getString("icon");
                        String name = object.getString("name");
                        list.add(new SortInfo(id, icon, name));

                    }
                    Log.i("FragmentTab01", list.toString() + "====" + list.size());
                    bindAdapter();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            private void bindAdapter() {
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                System.out.println(list.get(0).getIcon());
                GridViewAdapter adapter = new GridViewAdapter(layoutInflater, list);
//
                gridView.setAdapter(adapter);
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
