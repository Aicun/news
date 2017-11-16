package lac.com.news.news.newstabdetail;

import android.content.Context;
import android.graphics.Color;
import android.nfc.Tag;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import lac.com.news.R;
import lac.com.news.beans.News;
import lac.com.news.news.NewsContentBase;
import lac.com.news.utils.Constant;

/**
 * Created by Aicun on 11/12/2017.
 */

public class NewsSubTopicDetail extends NewsContentBase {

    private News.DataBean.ChildrenBean topic;

    private String url;

    private static final String TAG = NewsSubTopicDetail.class.getName();

    public NewsSubTopicDetail(Context context, News.DataBean.ChildrenBean topic) {
        super(context);
        this.topic = topic;
    }

    @Override
    public View initView() {
        View view = View.inflate(context,R.layout.news_topic_detail,null);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Constant.BASE_URL + topic.getUrl();
        Log.i(TAG,url);
    }
}
