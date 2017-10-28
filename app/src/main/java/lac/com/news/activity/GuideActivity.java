package lac.com.news.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import lac.com.news.R;

public class GuideActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Button start;
    private LinearLayout guide;

    private List<ImageView> guideImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        viewPager = (ViewPager) findViewById(R.id.guide_viewpager);
        start = (Button) findViewById(R.id.btn_start_main);
        guide = (LinearLayout) findViewById(R.id.ll_guide_group);

        int[] ids = {R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};

        for(int i=0; i<ids.length;i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            guideImages.add(imageView);

            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10,10);
            if(i != 0)
                params.leftMargin = 10;
            point.setLayoutParams(params);
            guide.addView(point);
        }

        viewPager.setAdapter(new guideImageAdapter());
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
}
