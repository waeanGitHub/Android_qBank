package adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.waean.asus.android_qbank.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import pojo.QuestionInfo;

/**
 * Created by Asus on 2016/08/31.
 */
public class QuestionAdapter extends BaseAdapter {
    Context context;
    List<QuestionInfo> list;

    public QuestionAdapter(Context context, List<QuestionInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
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
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_question, null);
            viewHolder.mContent = (TextView) view.findViewById(R.id.tv_content);
            viewHolder.mType = (TextView) view.findViewById(R.id.tv_type);
            viewHolder.mTime = (TextView) view.findViewById(R.id.tv_time);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        /*设置相应显示内容*/
        viewHolder.mContent.setText(list.get(i).getContent());
        String questionType = getQuestionType(list.get(i).getTypeid());
        viewHolder.mType.setText(questionType);

        SimpleDateFormat format = new SimpleDateFormat("MMMM dd,yyyy", Locale.ENGLISH);
        String questionTime = format.format(Long.parseLong(list.get(i).getPubTime()));
        viewHolder.mTime.setText(questionTime);

        return view;
    }

    private String getQuestionType(int typeid) {
        String type = null;
        switch (typeid) {
            case 1:
                type = "单选题";
                break;
            case 2:
                type = "多选题";
                break;
            case 3:
                type = "判断题";
                break;
            case 4:
                type = "简答题";
                break;
        }
        return type;
    }

    public class ViewHolder {
        private TextView mContent;
        private TextView mType;
        private TextView mTime;

    }

}
