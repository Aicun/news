package lac.com.news.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import lac.com.news.R;
import lac.com.news.fragment.LeftMenuFragment;
import lac.com.news.fragment.MainContentFragment;
import lac.com.news.utils.DensityUtil;

public class MainActivity extends SlidingFragmentActivity {

    public static final String MAIN_CONTENT_TAG = "main_content_tag";
    public static final String LEFTMENU_TAG = "leftmenu_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initSlidingMenu();
        initFragment();
    }

    private void initSlidingMenu() {
        //set main page
        setContentView(R.layout.activity_main);

        //set left menu
        setBehindContentView(R.layout.activity_leftmenu);

        //set right menu
        SlidingMenu menu = getSlidingMenu();
        //menu.setSecondaryMenu(R.layout.activity_rightmenue);

        // set display mode:left+main, left+main+right, main+right
        menu.setMode(SlidingMenu.LEFT);

        // set move mode: margin, full screen, none
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        // set main page size
        menu.setBehindOffset(DensityUtil.dip2px(MainActivity.this, 200));
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.fl_leftmenu,new LeftMenuFragment(),LEFTMENU_TAG);
        ft.replace(R.id.fl_main_content, new MainContentFragment(),MAIN_CONTENT_TAG);

        ft.commit();
    }

    public LeftMenuFragment getLeftMenuFragment() {
        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LEFTMENU_TAG);
    }

    public MainContentFragment getMainContentFragment() {
        return (MainContentFragment) getSupportFragmentManager().findFragmentByTag(MAIN_CONTENT_TAG);
    }
}
