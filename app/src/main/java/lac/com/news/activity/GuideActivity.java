package lac.com.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import lac.com.news.R;
import lac.com.news.utils.CacheUtils;
import lac.com.news.utils.DensityUtil;

public class GuideActivity extends Activity {

    private ViewPager viewPager;
    private Button start;
    private LinearLayout guide;
    private ImageView redPoint;

    private int spaceBetweenDots;

    private int widthdpi ;

    private List<ImageView> guideImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        viewPager = (ViewPager) findViewById(R.id.guide_viewpager);
        start = (Button) findViewById(R.id.btn_start_main);
        guide = (LinearLayout) findViewById(R.id.ll_guide_group);
        redPoint = (ImageView) findViewById(R.id.guide_red_point);

        int[] ids = {R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
        widthdpi = DensityUtil.dip2px(this,10);
        for(int i=0; i<ids.length;i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            guideImages.add(imageView);

            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthdpi,widthdpi);
            if(i != 0)
                params.leftMargin = widthdpi;
            point.setLayoutParams(params);
            guide.addView(point);
        }

        viewPager.setAdapter(new guideImageAdapter());

        redPoint.getViewTreeObserver().addOnGlobalLayoutListener(new guidePageGlobalLayoutListener());

        viewPager.addOnPageChangeListener(new guidePageChangeListener());

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacheUtils.putBoolean(GuideActivity.this,SplashActivity.MAIN_VISITED,true);

                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    class guideImageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return guideImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = guideImages.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class guidePageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int leftmargin = (int) (position * spaceBetweenDots + positionOffset * spaceBetweenDots);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) redPoint.getLayoutParams();
            params.leftMargin = leftmargin;
            redPoint.setLayoutParams(params);
        }

        @Override
        public void onPageSelected(int position) {
            if(position == guideImages.size()-1) {
                start.setVisibility(View.VISIBLE);
            }else {
                start.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class guidePageGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            redPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            spaceBetweenDots = guide.getChildAt(1).getLeft() - guide.getChildAt(0).getLeft();
        }
    }
}
