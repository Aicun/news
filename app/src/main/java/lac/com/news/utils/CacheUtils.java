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

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sf = context.getSharedPreferences("com.lac.news", context.MODE_PRIVATE);
        sf.edit().putBoolean(key,value).commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sf = context.getSharedPreferences("com.lac.news", context.MODE_PRIVATE);
        return sf.getString(key, null);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sf = context.getSharedPreferences("com.lac.news", context.MODE_PRIVATE);
        sf.edit().putString(key,value).commit();
    }
}
