package lac.com.news.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aicun on 10/25/2017.
 */

public class CacheUtils {
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sf = context.getSharedPreferences("com.lac.news", context.MODE_PRIVATE);
        return sf.getBoolean(key, false);
    }
}
