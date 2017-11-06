package lac.com.news.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Aicun on 11/1/2017.
 */

public class LeftMenuFragment extends BaseFragment {

    private TextView textView;
    @Override
    public View initView() {

        textView = new TextView(context);
        textView.setTextSize(15);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("Left Menu Content");
    }
}
