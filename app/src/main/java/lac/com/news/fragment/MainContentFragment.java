package lac.com.news.fragment;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import lac.com.news.R;
import lac.com.news.activity.MainActivity;
import lac.com.news.contents.BasePage;
import lac.com.news.contents.GoPage;
import lac.com.news.contents.HomePage;
import lac.com.news.contents.NewsCenterPage;
import lac.com.news.contents.ServicePage;
import lac.com.news.contents.SettingsPage;

/**
 * Created by Aicun on 11/1/2017.
 */

public class MainContentFragment extends BaseFragment {

    @ViewInject(R.id.main_viewpager)
    private ViewPager viewPager;
    @ViewInject(R.id.rg_content)
    private RadioGroup radioGroup;

    private ArrayList<BasePage> pagesList;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.main_content_fragment,null);
        //viewPager = (ViewPager) view.findViewById(R.id.main_viewpager);
        //radioGroup = (RadioGroup) view.findViewById(R.id.rg_content);

        //inject view into xutils,so maincontentFragment and view is linked
        x.view().inject(MainContentFragment.this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        pagesList = new ArrayList<>();
        pagesList.add(new HomePage(context));
        pagesList.add(new NewsCenterPage(context));
        pagesList.add(new GoPage(context));
        pagesList.add(new ServicePage(context));
        pagesList.add(new SettingsPage(context));

        viewPager.setAdapter(new ContentFragmentAdapter());

        radioGroup.setOnCheckedChangeListener(new MyCheckedChangedListener());

        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        pagesList.get(0).initData();

        radioGroup.check(R.id.rb_home);
    }

    private void enableSlidingMenu(int touchmodeNone) {
        MainActivity activity = (MainActivity) context;
        activity.getSlidingMenu().setTouchModeAbove(touchmodeNone);
    }

    private class ContentFragmentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pagesList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePage page = pagesList.get(position);

            //disable data to be loaded when page is initialed, data should be loaded when page is selected
            //page.initData();
            View view = page.rootView;
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class MyCheckedChangedListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_home:
                    viewPager.setCurrentItem(0,true);
                    enableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_news:
                    viewPager.setCurrentItem(1,true);
                    enableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                case R.id.rb_govaffair:
                    viewPager.setCurrentItem(2,true);
                    enableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_smartservice:
                    viewPager.setCurrentItem(3,true);
                    enableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_setting:
                    viewPager.setCurrentItem(4,true);
                    enableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            pagesList.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
