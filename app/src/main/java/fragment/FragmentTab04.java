package fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.waean.asus.android_qbank.R;

import java.util.List;

/**
 * Created by Asus on 2016/08/31.
 */
public class FragmentTab04 extends Fragment {
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collect_layout, null);
        mListView = (ListView) view.findViewById(R.id.colect_listview);
//        getData();
//        bindAdater();
        return view;
    }



}
