package lac.com.news.news;

import android.content.Context;
import android.view.View;

/**
 * Created by Aicun on 11/7/2017.
 */

public abstract class NewsContentBase {

    public Context context;
    public View rootView;

    public NewsContentBase(Context context) {
        this.context = context;
        rootView = initView();
    }

    public abstract View initView();

    public void initData() {}

}
