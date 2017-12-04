package lac.com.news.news;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import lac.com.news.R;
import lac.com.news.activity.MainActivity;
import lac.com.news.beans.News;
import lac.com.news.news.newstabdetail.NewsSubTopicDetail;
import lac.com.news.news.newstabdetail.TabDetailPager;

/**
 * Created by Aicun on 11/7/2017.
 */

public class NewsDetail extends NewsContentBase {

    @ViewInject(R.id.news_detail_tab_indicator)
    private TabPageIndicator tabPageIndicator;

    @ViewInject(R.id.news_detail_viewpager)
    private ViewPager newsDetailViewPager;

    @ViewInject(R.id.ib_tab_next)
    private ImageButton ibTabNext;

    private List<News.DataBean.ChildrenBean> children;

    private List<NewsContentBase> topics;
    //private List<TabDetailPager> topics;

    public NewsDetail(Context context, News.DataBean dataBean) {
        super(context);
        this.children = dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.news_detail,null);
        x.view().inject(NewsDetail.this,view);

        ibTabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsDetailViewPager.setCurrentItem(newsDetailViewPager.getCurrentItem() + 1);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        topics = new ArrayList<>();
        for(News.DataBean.ChildrenBean topic: children) {
            //NewsContentBase newsSubTopicDetail = new NewsSubTopicDetail(context, topic);
            NewsContentBase newsSubTopicDetail = new TabDetailPager(context,topic);
            topics.add(newsSubTopicDetail);
        }

        newsDetailViewPager.setAdapter(new NewsDetailViewAdapter());

        tabPageIndicator.setViewPager(newsDetailViewPager);

        tabPageIndicator.setOnPageChangeListener(new MyTabChangeListener());
    }

    private void enableSlidingMenu(int touchmodeNone) {
        MainActivity activity = (MainActivity) context;
        activity.getSlidingMenu().setTouchModeAbove(touchmodeNone);
    }

    private class NewsDetailViewAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return topics.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NewsContentBase topic = topics.get(position);
            View rootView = topic.rootView;
            topic.initData();
            container.addView(rootView);
            return rootView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class MyTabChangeListener implements ViewPager.OnPageChangeListener {



        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position == 0) {
                enableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }else {
                enableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
