package dominic.mybatis.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by herongxing on 2017/2/23 20:43.
 */
@Slf4j
public class TransformUtils {

    public static <T> T hashMapToBean(HashMap<String, Object> map, Class<T> tClass) {
        if (MapUtils.isEmpty(map)) {
            return null;
        }
        try {
            T t = tClass.newInstance();
            for (Field field : tClass.getDeclaredFields()) {
                String fieldName = SqlBuildUtils.camelCaseToUnderscore(field.getName(), tClass);
                Object value = map.get(fieldName);
                if (null != value) {
                    field.setAccessible(true);
                    field.set(t, value);
                }
            }
            return t;
        } catch (InstantiationException e) {
            throw new UnsupportedOperationException("InstantiationException:", e);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException("IllegalAccessException:", e);
        }
    }

    public static < T> List<T> hashMapToBean(List<HashMap<String, Object>> mapList, Class<T> tClass) {
        if (CollectionUtils.isEmpty(mapList)) {
            return Collections.emptyList();
        }
        ArrayList<T> arrayList = new ArrayList<>();
        for (HashMap<String, Object> map : mapList) {
            T t = TransformUtils.hashMapToBean(map, tClass);
            arrayList.add(t);
        }
        return arrayList;
    }
}
