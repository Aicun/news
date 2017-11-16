package lac.com.news.contents;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import lac.com.news.activity.MainActivity;
import lac.com.news.beans.News;
import lac.com.news.fragment.LeftMenuFragment;
import lac.com.news.news.InteractDetail;
import lac.com.news.news.NewsContentBase;
import lac.com.news.news.NewsDetail;
import lac.com.news.news.PicsDetail;
import lac.com.news.news.TopicDetail;
import lac.com.news.utils.CacheUtils;
import lac.com.news.utils.Constant;

/**
 * Created by Aicun on 11/5/2017.
 */

public class NewsCenterPage extends BasePage {

    private ArrayList<NewsContentBase> contentPages;
    List<News.DataBean> data;
    private long startTime;

    public NewsCenterPage(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        ibMenu.setVisibility(View.VISIBLE);

        titleView.setText("News Center Page");

        String saveJson = CacheUtils.getString(context,Constant.NEWS_CENTER_URL);//""

        if(!TextUtils.isEmpty(saveJson)){
            //processData(saveJson);
        }

        startTime = SystemClock.uptimeMillis();

        loadNewsDataFromServer();
    }

    public void switchPager(int position) {
        if(position < contentPages.size()) {
            titleView.setText(data.get(position).getTitle());
            frameLayout.removeAllViews();

            NewsContentBase detailContent = contentPages.get(position);
            View rootView = detailContent.rootView;
            detailContent.initData();

            frameLayout.addView(rootView);
        }
    }

    private void loadNewsDataFromServer() {
        RequestParams params = new RequestParams(Constant.NEWS_CENTER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("NewsCenterPage","News Datga Loaded");

                CacheUtils.putString(context,Constant.NEWS_CENTER_URL,result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("NewsCenterPage",ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("NewsCenterPage",cex.getMessage());
            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processData(String result) {
        News news = parseJson(result);

        MainActivity activity = (MainActivity) context;
        LeftMenuFragment leftMenuFragment = activity.getLeftMenuFragment();
        data = news.getData();

        Log.d("",data.get(0).getTitle());

        contentPages = new ArrayList<>();

        contentPages.add(new NewsDetail(context,data.get(0)));
        contentPages.add(new TopicDetail(context,data.get(1)));
        contentPages.add(new PicsDetail(context,data.get(2)));
        contentPages.add(new InteractDetail(context,data.get(3)));

        leftMenuFragment.setData(data);
    }

    private News parseJson(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result, News.class);
    }
}
