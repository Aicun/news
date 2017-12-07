package lac.com.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import lac.com.news.R;
import lac.com.news.enmu.NewsRefreshStatus;

/**
 * Created by Aicun on 11/24/2017.
 */

public class RefreshListView extends ListView {

    private LinearLayout headerLinearLayout;
    private ImageView refreshArrow;
    private TextView refreshStatus;
    private TextView refreshTime;
    private ProgressBar refreshingProgressBar;

    private Animation pullDownAnimation;
    private Animation releaseAnimation;
    private OnRefreshListener onRefreshListener;

    private LinearLayout footerLinearLayout;
    private int footerViewHeight;

    private float startY;
    private int refreshViewHeight;
    private NewsRefreshStatus newsRefreshStatus;

    private int listViewPositionY = -1;

    private View topView;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        startY = -1;
        newsRefreshStatus = NewsRefreshStatus.PULLDOWN;
        initHeaderView(context);
        initAnimation();
        initFooterView(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                if(!isTopNewsDisplayed())
                    break;

                if(startY == -1) {
                    startY = ev.getY();
                }
                float endY = ev.getY();
                float moveDistanceY = endY - startY;
                if(moveDistanceY > 0) {
                    int paddingToTop = (int) (-refreshViewHeight + moveDistanceY);
                    headerLinearLayout.setPadding(0,paddingToTop,0,0);
                    changeRefreshStatus(paddingToTop);
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if(newsRefreshStatus == NewsRefreshStatus.PULLDOWN) {
                    headerLinearLayout.setPadding(0,-refreshViewHeight,0,0);
                }else if (newsRefreshStatus == NewsRefreshStatus.RELEASE) {
                    headerLinearLayout.setPadding(0,0,0,0);
                    newsRefreshStatus = NewsRefreshStatus.REFRESHING;
                    refreshViewStatus();
                    if(onRefreshListener != null) {
                        onRefreshListener.onPullDownRefresh();
                    }
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    public void updateRefreshStatus(boolean updateRefreshDate) {
        refreshStatus.setText("pull down to refresh...");
        refreshArrow.setVisibility(VISIBLE);
        refreshingProgressBar.setVisibility(GONE);
        refreshArrow.clearAnimation();
        newsRefreshStatus = NewsRefreshStatus.PULLDOWN;
        headerLinearLayout.setPadding(0,-refreshViewHeight,0,0);
        if(updateRefreshDate) {
            refreshTime.setText("last update time: " + getSystemTime());
        }
    }

    public void updateFooter(){
        footerLinearLayout.setPadding(0,-footerViewHeight,0,0);
    }

    public interface OnRefreshListener{
        public void onPullDownRefresh();
        public void onLoadMore();
    }

    public void SetOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public void addTopNewsView(View topView) {
        if(topView != null) {
            this.topView = topView;
            headerLinearLayout.addView(topView);
        }
    }

    private void initHeaderView(Context context) {
        headerLinearLayout = (LinearLayout) View.inflate(context, R.layout.news_header_refresh,null);

        refreshArrow = (ImageView) headerLinearLayout.findViewById(R.id.iv_refresh_arrow_down);
        refreshingProgressBar = (ProgressBar) headerLinearLayout.findViewById(R.id.pb_refresh_progress);
        refreshStatus = (TextView) headerLinearLayout.findViewById(R.id.tv_refresh);
        refreshTime = (TextView) headerLinearLayout.findViewById(R.id.tv_last_refresh_time);

        headerLinearLayout.measure(0,0);
        refreshViewHeight = headerLinearLayout.getMeasuredHeight();

        headerLinearLayout.setPadding(0,-refreshViewHeight,0,0);

        addHeaderView(headerLinearLayout);
    }

    private void initAnimation() {
        releaseAnimation = new RotateAnimation(0,-180,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        releaseAnimation.setDuration(500);
        releaseAnimation.setFillAfter(true);

        pullDownAnimation = new RotateAnimation(-180,-360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        pullDownAnimation.setDuration(500);
        pullDownAnimation.setFillAfter(true);
    }

    private void initFooterView(Context context) {
        footerLinearLayout = (LinearLayout) View.inflate(context,R.layout.news_footer_more,null);
        footerLinearLayout.measure(0,0);
        footerViewHeight = footerLinearLayout.getHeight();
        footerLinearLayout.setPadding(0,-footerViewHeight,0,0);

        addFooterView(footerLinearLayout);

        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
                    if(getLastVisiblePosition() >= getCount() -1) {
                        footerLinearLayout.setPadding(0,0,0,0);
                        if(onRefreshListener != null)
                            onRefreshListener.onLoadMore();
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void changeRefreshStatus(int paddingToTop) {
        if(paddingToTop < 0 && newsRefreshStatus != NewsRefreshStatus.PULLDOWN) {
            newsRefreshStatus = NewsRefreshStatus.PULLDOWN;
            refreshViewStatus();
        }else if( paddingToTop > 0 && newsRefreshStatus != NewsRefreshStatus.RELEASE) {
            newsRefreshStatus = NewsRefreshStatus.RELEASE;
            refreshViewStatus();
        }
    }

    private void refreshViewStatus() {
        switch (newsRefreshStatus) {
            case PULLDOWN:
                refreshArrow.setAnimation(pullDownAnimation);
                refreshStatus.setText("pull down to refresh...");
                break;
            case RELEASE:
                refreshArrow.setAnimation(releaseAnimation);
                refreshStatus.setText("release to refresh...");
                break;
            case REFRESHING:
                refreshArrow.clearAnimation();
                refreshArrow.setVisibility(GONE);
                refreshStatus.setText("refreshing...");
                refreshingProgressBar.setVisibility(VISIBLE);
                break;
        }
    }

    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    private boolean isTopNewsDisplayed() {

        if(topView != null) {

            int[] positions = new int[2];
            getLocationOnScreen(positions);

            if(listViewPositionY == -1) {
                listViewPositionY = positions[1];
            }

            topView.getLocationOnScreen(positions);

            int topViewY = positions[1];

            return listViewPositionY <= topViewY;
        }

        return false;
    }
}
