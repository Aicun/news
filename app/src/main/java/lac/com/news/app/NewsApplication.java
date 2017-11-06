package lac.com.news.app;

import android.app.Application;

import org.xutils.x;

import lac.com.news.BuildConfig;

/**
 * Created by Aicun on 11/3/2017.
 */

public class NewsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
