package lac.com.news.news.newstabdetail;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
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
import lac.com.news.utils.GsonParseUtil;
import lac.com.news.utils.LogUtil;
import lac.com.news.view.HorizontalScrollViewPager;
import lac.com.news.view.RefreshListView;

public class TabDetailPager extends NewsContentBase {

    public static final String READ_ARRAY_ID = "read_array_id";
    private HorizontalScrollViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private RefreshListView listview;
    private TabDetailPagerListAdapter adapter;
    private ImageOptions imageOptions;
    private final News.DataBean.ChildrenBean childrenData;
    private String url;
    private List<NewsTopic.DataBean.TopnewsBean> topnews;
    private List<NewsTopic.DataBean.NewsBean> news;
    private String moreUrl;
    private boolean isLoadMore = false;
    private InternalHandler internalHandler;

    public TabDetailPager(Context context, News.DataBean.ChildrenBean childrenData) {
        super(context);
        this.childrenData = childrenData;
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(100), DensityUtil.dip2px(100))
                .setRadius(DensityUtil.dip2px(5))
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
        View view = View.inflate(context, R.layout.tabdetail_pager, null);
        listview = (RefreshListView) view.findViewById(R.id.lv_refresh_list_view);


        View topNewsView = View.inflate(context, R.layout.news_header_detail, null);
        viewpager = (HorizontalScrollViewPager) topNewsView.findViewById(R.id.news_topic_view_pager);
        tv_title = (TextView) topNewsView.findViewById(R.id.news_topic_title);
        ll_point_group = (LinearLayout) topNewsView.findViewById(R.id.news_topic_top_group);

        listview.addHeaderView(topNewsView);

        //listview.addTopNewsView(topNewsView);

        listview.SetOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onPullDownRefresh() {
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                getMoreFromServer();
            }
        });

        listview.setOnItemClickListener(new MyOnItemClickListener());

        return view;
    }


    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            int realPosition = position - 1;
            NewsTopic.DataBean.NewsBean newsData = news.get(realPosition);

            LogUtil.e("newsData==id==" + newsData.getId() + ",newsData_title==" + newsData.getTitle() + ",url===" + newsData.getUrl());
            String idArray = CacheUtils.getString(context, READ_ARRAY_ID);//"3511,"
            if (!idArray.contains(newsData.getId() + "")) {//3512

                CacheUtils.putString(context, READ_ARRAY_ID, idArray + newsData.getId() + ",");//"3511,3512,"

                adapter.notifyDataSetChanged();//getCount-->getView

            }

            /*Intent intent = new Intent(context,NewsDetailActivity.class);
            intent.putExtra("url",Constants.BASE_URL+newsData.getUrl());
            context.startActivity(intent);*/
        }
    }

    @Override
    public void initData() {
        super.initData();
        url = Constant.BASE_URL + childrenData.getUrl();
        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        prePosition = 0;
        LogUtil.e("url地址===" + url);
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(context, url, result);
                LogUtil.e(childrenData.getTitle() + "-页面数据请求成功==" + result);
                processData(result);
                listview.updateRefreshStatus(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(childrenData.getTitle() + "-页面数据请求失败==" + ex.getMessage());
                listview.updateRefreshStatus(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(childrenData.getTitle() + "-页面数据请求onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e(childrenData.getTitle() + "-onFinished==");
            }
        });
    }

    private void getMoreFromServer() {
        RequestParams params = new RequestParams(moreUrl);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                listview.updateFooter();
                isLoadMore = true;
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("loading more failed==" + ex.getMessage());
                listview.updateFooter();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("load more onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e(childrenData.getTitle() + "-onFinished==");
            }
        });
    }

    private int prePosition;

    private void processData(String json) {
        NewsTopic newsTopic = GsonParseUtil.convertFromJson(json, NewsTopic.class);
        LogUtil.e(childrenData.getTitle() + "解析成功==" + newsTopic.getData().getNews().get(0).getTitle());

        if (TextUtils.isEmpty(newsTopic.getData().getMore())) {
            moreUrl = "";
        } else {
            moreUrl = Constant.BASE_URL + newsTopic.getData().getMore();
        }

        LogUtil.e("加载更多的地址===" + moreUrl);
        //默认和加载更多
        if (!isLoadMore) {
            //默认
            //顶部轮播图数据
            topnews = newsTopic.getData().getTopnews();
            //设置ViewPager的适配器
            viewpager.setAdapter(new TabDetailPagerTopNewsAdapter());


            //添加红点
            addPoint();

            //监听页面的改变，设置红点变化和文本变化
            viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
            tv_title.setText(topnews.get(prePosition).getTitle());

            //准备ListView对应的集合数据
            news = newsTopic.getData().getNews();
            //设置ListView的适配器
            adapter = new TabDetailPagerListAdapter();
            listview.setAdapter(adapter);
        } else {
            //加载更多
            isLoadMore = false;
            //添加到原来的集合中
            news.addAll(newsTopic.getData().getNews());
            //刷新适配器
            adapter.notifyDataSetChanged();
        }

        //发消息每隔4000切换一次ViewPager页面
        if (internalHandler == null) {
            internalHandler = new InternalHandler();
        }

        //是把消息队列所有的消息和回调移除
        internalHandler.removeCallbacksAndMessages(null);
        internalHandler.postDelayed(new MyRunnable(), 4000);
    }

    class InternalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //切换ViewPager的下一个页面
            int item = (viewpager.getCurrentItem() + 1) % topnews.size();
            viewpager.setCurrentItem(item);
            internalHandler.postDelayed(new MyRunnable(), 4000);

        }
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            internalHandler.sendEmptyMessage(0);
        }
    }

    class TabDetailPagerListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.news_list_detail, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.news_detail_image);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.news_detail_title);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.news_detail_pubDate);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }

            //根据位置得到数据
            NewsTopic.DataBean.NewsBean newsData = news.get(position);
            String imageUrl = Constant.BASE_URL + newsData.getListimage();
            //请求图片XUtils3
//            x.image().bind( viewHolder.iv_icon,imageUrl,imageOptions);
            //请求图片使用glide
            Glide.with(context)
                    .load(imageUrl)
                    .into(viewHolder.iv_icon);
            //设置标题
            viewHolder.tv_title.setText(newsData.getTitle());

            //设置更新时间
            viewHolder.tv_time.setText(newsData.getPubdate());

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }

    private void addPoint() {
        ll_point_group.removeAllViews();//移除所有的红点
        for (int i = 0; i < topnews.size(); i++) {

            ImageView imageView = new ImageView(context);
            //设置背景选择器
            imageView.setBackgroundResource(R.drawable.point_selector);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(5), DensityUtil.dip2px(5));

            if (i == 0) {
                imageView.setEnabled(true);
            } else {
                imageView.setEnabled(false);
                params.leftMargin = DensityUtil.dip2px(8);
            }


            imageView.setLayoutParams(params);

            ll_point_group.addView(imageView);

        }
    }


    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //1.设置文本
            tv_title.setText(topnews.get(position).getTitle());
            //2.对应页面的点高亮-红色
            //把之前的变成灰色
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            //把当前设置红色
            ll_point_group.getChildAt(position).setEnabled(true);

            prePosition = position;

        }

        private boolean isDragging = false;

        @Override
        public void onPageScrollStateChanged(int state) {

            if (state == ViewPager.SCROLL_STATE_DRAGGING) {//拖拽
                isDragging = true;
                LogUtil.e("拖拽");
                //拖拽要移除消息
                internalHandler.removeCallbacksAndMessages(null);
            } else if (state == ViewPager.SCROLL_STATE_SETTLING && isDragging) {//惯性
                //发消息
                LogUtil.e("惯性");
                isDragging = false;
                internalHandler.removeCallbacksAndMessages(null);
                internalHandler.postDelayed(new MyRunnable(), 4000);

            } else if (state == ViewPager.SCROLL_STATE_IDLE && isDragging) {//静止状态
                //发消息
                LogUtil.e("静止状态");
                isDragging = false;
                internalHandler.removeCallbacksAndMessages(null);
                internalHandler.postDelayed(new MyRunnable(), 4000);
            }

        }
    }

    class TabDetailPagerTopNewsAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            //设置图片默认北京
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            //x轴和Y轴拉伸
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //把图片添加到容器(ViewPager)中
            container.addView(imageView);

            NewsTopic.DataBean.TopnewsBean topnewsData = topnews.get(position);
            //图片请求地址
            String imageUrl = Constant.BASE_URL + topnewsData.getTopimage();

            //联网请求图片
            x.image().bind(imageView, imageUrl, imageOptions);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN://按下
                            LogUtil.e("按下");
                            //是把消息队列所有的消息和回调移除
                            internalHandler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP://离开
                            LogUtil.e("离开");
                            //是把消息队列所有的消息和回调移除
                            internalHandler.removeCallbacksAndMessages(null);
                            internalHandler.postDelayed(new MyRunnable(), 4000);
                            break;
                        case MotionEvent.ACTION_CANCEL://取消
                            LogUtil.e("取消");
                            //是把消息队列所有的消息和回调移除
                            internalHandler.removeCallbacksAndMessages(null);
                            internalHandler.postDelayed(new MyRunnable(), 4000);
                            break;
                    }
                    return true;
                }
            });

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}