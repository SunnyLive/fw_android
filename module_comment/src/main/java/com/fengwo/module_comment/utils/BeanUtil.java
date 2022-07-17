/*
 * Created by sunny on 17-9-1.
 *----------Dragon be here!----------/
 *      ┏┓       ┏┓
 *     ┏┛┻━━━━━━━┛┻┓
 *     ┃　　　　    ┃
 *     ┃     ━     ┃
 *     ┃   ┳┛ ┗┳   ┃
 *     ┃　　　　    ┃
 *     ┃     ┻     ┃
 *     ┃　　　　    ┃
 *     ┗━━━┓   ┏━━━┛
 *         ┃   ┃神兽保佑
 *         ┃   ┃代码无BUG！
 *         ┃   ┗━━━━━━━━━━┓
 *         ┃   　　　 　   ┣┓
 *         ┃              ┏┛
 *         ┗┓┓┏━━━━━━━━┳┓┏┛
 *          ┃┫┫        ┃┫┫
 *          ┗┻┛        ┗┻┛
 * ━━━━━━神兽出没━━━━━━by:coder-yang
 * @工具类 copy实体类
 * @Author sunny
 */

package com.fengwo.module_comment.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class BeanUtil {

    private static Map<String, Object> srcMaps = new HashMap<>();

    public static <T> T copy(Object srcObject, Class<T> targClass) {
        srcMaps.clear();
        try {
            getValue(srcObject);
            return setValue(targClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static void getValue(Object srcObject) throws IllegalAccessException {
        Class srcClass = srcObject.getClass();
        Field[] declaredFields = srcClass.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            srcMaps.put(field.getName(), field.get(srcObject));
        }
    }


    private static <T> T setValue(Class<T> targClass) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        T obj;
        try {
            obj = targClass.newInstance();
        } catch (Exception e) {
            Class<?> enclosingClass = targClass.getEnclosingClass();
            Constructor con = targClass.getDeclaredConstructor(enclosingClass);
            con.setAccessible(true);
            obj = (T) con.newInstance(enclosingClass.newInstance());
        }

        Class<?> objClass = obj.getClass();

        for (Map.Entry<String, Object> entry : srcMaps.entrySet()) {

            String key = entry.getKey();
            Map<String, Field> targMaps = arrayToGather(objClass.getDeclaredFields());
            if (targMaps.containsKey(key)) {
                Field field = targMaps.get(key);
                field.setAccessible(true);
                Object value = entry.getValue();
                String simpleName = field.getType().getSimpleName();

                switch (simpleName) {
                    case "char":
                        field.setChar(obj, value != null ? value.toString().charAt(0) : null);
                        break;
                    case "String":
                        field.set(obj, value != null ? value : "");
                        break;
                    case "short":
                    case "Short":
                        field.set(obj, Short.valueOf(value != null ? value.toString() : "0"));
                        break;
                    case "int":
                    case "Integer":
                        field.set(obj, Integer.valueOf(value != null ? value.toString() : "0"));
                        break;
                    case "float":
                    case "Float":
                        field.set(obj, Float.valueOf(value != null ? value.toString() : "0"));
                        break;
                    case "double":
                    case "Double":
                        field.set(obj, Double.valueOf(value != null ? value.toString() : "0"));
                        break;
                    case "long":
                    case "Long":
                        field.set(obj, Long.valueOf(value != null ? value.toString() : "0"));
                        break;
                    default:
                        field.set(obj, value);
                        break;
                }


            }
        }
        return obj;
    }


    private static Map<String, Field> arrayToGather(Field[] fields) {

        Map<String, Field> maps = new HashMap<>();
        for (Field field : fields) {
            maps.put(field.getName(), field);
        }
        return maps;
    }

}
