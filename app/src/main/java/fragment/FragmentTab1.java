package fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.waean.asus.android_qbank.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 2016/08/30.
 */
public class FragmentTab1 extends Fragment {

    private View view;

    private GridView gridView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_layout,null);
        initView();
        return view;
    }

    private void initView() {
        gridView = (GridView) view.findViewById(R.id.mGridview);

        List<String> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            list.add(new String("list:"+i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        gridView.setAdapter(adapter);
    }
}
