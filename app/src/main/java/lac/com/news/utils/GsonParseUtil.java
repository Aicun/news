package lac.com.news.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import lac.com.news.beans.News;

/**
 * Created by Aicun on 11/16/2017.
 */

public class GsonParseUtil {

    private static Gson gson = new Gson();
    private static Gson gonWithNulls = new GsonBuilder().serializeNulls().create();

    public static <T>  T convertFromJson (String json, Class clazz) {
        try {
            T instance = (T) gson.fromJson(json,clazz);
            return instance;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
