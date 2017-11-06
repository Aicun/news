package lac.com.news.contents;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by Aicun on 11/5/2017.
 */

public class ServicePage extends BasePage {
    public ServicePage(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        titleView.setText("Service Center Page");

        TextView tv = new TextView(context);
        tv.setText("Service Center Content");
        tv.setTextSize(25);
        tv.setTextColor(Color.RED);
        tv.setGravity(Gravity.CENTER);
        frameLayout.addView(tv);
    }
}
