package lac.com.news.news;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Aicun on 11/7/2017.
 */

public class NewsDetail extends NewsContentBase {

    private TextView tv;

    public NewsDetail(Context context) {
        super(context);
    }

    @Override
    public View initView() {

        tv = new TextView(context);
        tv.setText("News Content Detail Page");
        tv.setTextSize(25);

        return tv;
    }

    @Override
    public void initData() {
        super.initData();
    }
}
