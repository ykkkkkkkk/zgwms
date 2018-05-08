package ykk.xc.com.wms.util;

import com.solidfire.gson.Gson;
import com.solidfire.gson.JsonArray;
import com.solidfire.gson.JsonElement;
import com.solidfire.gson.JsonParser;
import com.solidfire.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private static Gson mGson = new Gson();
//    对象转为字符串
//
//    String ps =gson.toJson(person);
//
//
//    字符串转为对象
//
//    Person person =gson.fromJson(json, Person.class);
//
//
//    字符串为为list
//
//    List<Person> persons =gson.fromJson(json, new TypeToken<List<Person>>() {}.getType());


    /**
     * 将json字符串转化成实体对象
     *
     * @param json
     * @param classOfT
     * @return
     */
    public static <T> T stringToObject(String json, Class<T> classOfT) {
        return mGson.fromJson(json, classOfT);

    }

    /**
     * 将对象准换为json字符串 或者 把list 转化成json
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String objectToString(T object) {

        return mGson.toJson(object);

    }

//    /**
//     * 把json 字符串转化成list
//     *
//     * @param json
//     * @param cls
//     * @param <T>
//     * @return
//     */
    public static <T> List<T> stringToList(String json, Class<T> cls) {
        // 当为一个对象的时候，返回的是json对象，而不是数组
        if(json.indexOf("[") == -1) {
            json = "[" +json + "]";
        }
        Gson gson = new Gson();

        List<T> list = new ArrayList<T>();

        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }

        return list;

    }

}
