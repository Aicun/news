package lac.com.news.contents;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import lac.com.news.activity.MainActivity;

/**
 * Created by Aicun on 11/5/2017.
 */

public class HomePage extends BasePage {
    public HomePage(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        titleView.setText("Home Page");

        TextView tv = new TextView(context);
        tv.setText("Home Page Content");
        tv.setTextSize(25);
        tv.setTextColor(Color.RED);
        tv.setGravity(Gravity.CENTER);
        frameLayout.addView(tv);
    }

    private void enableSlideMenu(int touchmodeNone) {
        MainActivity activity = (MainActivity) context;
        activity.getSlidingMenu().setTouchModeAbove(touchmodeNone);
    }
}
