package adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waean.asus.android_qbank.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import pojo.SortInfo;

/**
 * Created by Asus on 2016/08/30.
 */
public class GridViewAdapter extends BaseAdapter {
    private static final String url = "http://115.29.136.118/web-question/";
    private List<SortInfo> list;

    LayoutInflater inflater;

    public GridViewAdapter(LayoutInflater inflater, List<SortInfo> list) {
        this.inflater = inflater;
        this.list = list;
        Log.i("info", "GridViewAdapter: " + list.size());
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        Log.i("info", "getView: " + list.size());
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_gridview, null);
            holder.mTextView = (TextView) view.findViewById(R.id.tv_item_text);
            holder.mImageView = (ImageView) view.findViewById(R.id.item_pic);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

//        System.out.println(list.get(i).getName());

        if (holder != null) {
            holder.mTextView.setText(list.get(i).getName());
            x.image().bind(holder.mImageView, url + list.get(i).getIcon());
//             holder.mImageView.setImageResource(R.drawable.ic_launcher1);
        }
//        getUrlImage(url + list.get(i).getIcon(), holder);

//        holder.mImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("点击了" + view.getId() + "项");
//            }
//        });
        return view;
    }

//    private void getUrlImage(String mUrl, final ViewHolder viewHolder) {
//
//        x.http().get(new RequestParams(mUrl), new Callback.CommonCallback<InputStream>() {
//            @Override
//            public void onSuccess(InputStream result) {
//                Log.i("GridViewAdapter", result.toString());
//                final Bitmap bitmap = BitmapFactory.decodeStream(result);
//                viewHolder.mImageView.setImageBitmap(bitmap);
//
//                try {
//                    result.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
////        return null;
//    }

    public class ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;
    }
}
