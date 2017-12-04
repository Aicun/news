package lac.com.news.news.newstabdetail;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import lac.com.news.R;
import lac.com.news.beans.News;
import lac.com.news.beans.NewsTopic;
import lac.com.news.news.NewsContentBase;
import lac.com.news.utils.CacheUtils;
import lac.com.news.utils.Constant;
import lac.com.news.utils.DensityUtil;
import lac.com.news.utils.GsonParseUtil;
import lac.com.news.utils.LogUtil;
import lac.com.news.view.HorizontalScrollViewPager;

/**
 * Created by Aicun on 11/12/2017.
 */

public class NewsSubTopicDetail extends NewsContentBase {


    private RecyclerView newsRecyclerView;

    private News.DataBean.ChildrenBean topic;
    private String url;
    private List<NewsTopic.DataBean.TopnewsBean> topNews;

    private static final String TAG = NewsSubTopicDetail.class.getName();
    private int previous;
    private List<NewsTopic.DataBean.NewsBean> newsList;
    private ImageOptions imageOptions;

    public NewsSubTopicDetail(Context context, News.DataBean.ChildrenBean topic) {
        super(context);
        this.topic = topic;
        imageOptions = new ImageOptions.Builder()
                .setSize(org.xutils.common.util.DensityUtil.dip2px(120), org.xutils.common.util.DensityUtil.dip2px(120))
                .setRadius(org.xutils.common.util.DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.news_pic_default)
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();
    }

    @Override
    public View initView() {
        View view = View.inflate(context,R.layout.news_topic_detail,null);

        newsRecyclerView = (RecyclerView) view.findViewById(R.id.news_recycler_view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Constant.BASE_URL + topic.getUrl();
        String result = CacheUtils.getString(context,url);
        if(!TextUtils.isEmpty(result)) {
            processJsonData(result);
        }
        LogUtil.i(url);
        getDataFromServer();
    }

    private void getDataFromServer() {

        previous = 0;

        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(context,url,result);
                processJsonData(result);
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

    private void processJsonData(String result) {

        NewsTopic newsTopic = GsonParseUtil.convertFromJson(result, NewsTopic.class);
        LogUtil.e(newsTopic.getData().getNews().get(0).getTitle());

        topNews = newsTopic.getData().getTopnews();

        newsList = newsTopic.getData().getNews();

        newsRecyclerView.setAdapter(new NewsRecyclerViewAdapter());
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private class NewsTopicAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return topNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY );
            container.addView(imageView);

            NewsTopic.DataBean.TopnewsBean topic = topNews.get(position);

            String url = Constant.BASE_URL + topic.getTopimage();

            x.image().bind(imageView, url,imageOptions);

            return imageView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_NEWS_HEADER = 0;
        private static final int TYPE_NEWS_LIST = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == TYPE_NEWS_HEADER) {
                View view = View.inflate(context, R.layout.news_header_detail, null);
                return new NewsHeaderHoder(view);
            }else if(viewType == TYPE_NEWS_LIST) {
                View view = View.inflate(context, R.layout.news_list_detail, null);
                return new NewsListHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof NewsHeaderHoder) {
                NewsHeaderHoder viewHolder = (NewsHeaderHoder)holder;
                viewHolder.bind();
            }else if (holder instanceof NewsListHolder) {
                NewsListHolder viewHolder = (NewsListHolder)holder;
                NewsTopic.DataBean.NewsBean news = newsList.get(position-1);
                viewHolder.bind(news);
            }
        }

        @Override
        public int getItemCount() {
            return newsList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0) {
                return TYPE_NEWS_HEADER;
            }
            return TYPE_NEWS_LIST;
        }
    }

    private class NewsListHolder extends RecyclerView.ViewHolder {

        private ImageView newsImage;
        private TextView newsTitleView;
        private TextView pubDateView;

        public NewsListHolder(View itemView) {
            super(itemView);
            newsImage = (ImageView) itemView.findViewById(R.id.news_detail_image);
            newsTitleView = (TextView) itemView.findViewById(R.id.news_detail_title);
            pubDateView = (TextView) itemView.findViewById(R.id.news_detail_pubDate);
        }

        public void bind(NewsTopic.DataBean.NewsBean news) {
            String imageUrl = Constant.BASE_URL + news.getListimage();
            //x.image().bind(newsImage,imageUrl,imageOptions);

            Glide.with(context)
                    .load(imageUrl)
                    .into(newsImage);

            newsTitleView.setText(news.getTitle());
            pubDateView.setText(news.getPubdate());
        }
    }

    private class NewsHeaderHoder extends RecyclerView.ViewHolder {

        private HorizontalScrollViewPager newsTopicViewPager;
        private TextView newsTopicTitle;
        private LinearLayout newsTopicTopGroup;

        public NewsHeaderHoder(View itemView) {
            super(itemView);
            newsTopicViewPager = (HorizontalScrollViewPager) itemView.findViewById(R.id.news_topic_view_pager);
            newsTopicTitle = (TextView) itemView.findViewById(R.id.news_topic_title);
            newsTopicTopGroup = (LinearLayout) itemView.findViewById(R.id.news_topic_top_group);
        }

        public void bind() {
            newsTopicViewPager.setAdapter(new NewsTopicAdapter());

            newsTopicTopGroup.removeAllViews();

            for (int i = 0; i < topNews.size(); i++) {
                ImageView imageView = new ImageView(context);
                imageView.setBackgroundResource(R.drawable.point_selector);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        DensityUtil.dip2px(context, 8),
                        DensityUtil.dip2px(context, 8));

                if (i == 0) {
                    imageView.setEnabled(true);
                } else {
                    imageView.setEnabled(false);
                    params.leftMargin = DensityUtil.dip2px(context, 8);
                }

                imageView.setLayoutParams(params);

                newsTopicTopGroup.addView(imageView);
            }

            newsTopicViewPager.addOnPageChangeListener(new NewsTopicChangeListner());
            newsTopicTitle.setText(topNews.get(previous).getTitle());
        }

        private class NewsTopicChangeListner implements ViewPager.OnPageChangeListener {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                newsTopicTitle.setText(topNews.get(position).getTitle());
                newsTopicTopGroup.getChildAt(previous).setEnabled(false);
                newsTopicTopGroup.getChildAt(position).setEnabled(true);
                previous = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        }
    }
}
