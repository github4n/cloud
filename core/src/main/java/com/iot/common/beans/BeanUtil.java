package com.iot.common.beans;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>bean转换工具</p>
 *
 * @author xiangyitao
 * @version 1.00
 * @dateTime 2017年12月1日 下午14:39:29
 */
public class BeanUtil extends BeanUtils {

    /**
     * 将T泛型的list转换成L泛型的list
     *
     * @param sourceList
     * @param targetClass
     * @return
     */
    public static <T, L> List<L> listTranslate(List<T> sourceList, Class<L> targetClass) throws Exception {
        List<L> targetList = new ArrayList<>();
        if (sourceList == null) {
            return targetList;
        }
        for (T source : sourceList) {
            L target = targetClass.newInstance();
            copyProperties(source, target);
            targetList.add(target);
        }
        return targetList;
    }

    /**
     * 将PageInfo<Dto> 转为 PageInfo<Vo>
     *
     * @param pageInfo
     * @param clazz
     * @param <T>
     * @param <R>
     * @return
     * @throws Exception
     */
    public static <T, R> PageInfo<R> pageInfoDto2PageInfoVo(PageInfo<T> pageInfo, Class<R> clazz) throws Exception {
        PageInfo<R> voInfo = new PageInfo<>();
        List<T> dtoList = pageInfo.getList();
        List<R> voList = BeanUtil.listTranslate(dtoList, clazz);
        BeanUtil.copyProperties(pageInfo, voInfo, "list");
        voInfo.setList(voList);
        return voInfo;
    }

    /**
      * @despriction：实体类转为map
      * @author  yeshiyuan
      * @created 2018/6/12 15:00
      * @param null
      * @return
      */
    public static Map<String,Object> convertBeanToMap(Object bean) throws IntrospectionException,IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map<String,Object> returnMap = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                }
            }
        }
        return returnMap;
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param clazz 要转化的类型
     * @param map 包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InstantiationException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings("rawtypes")
    public static <T> T convertMapToBean(Class<T> clazz, Map map) {
        T obj = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            obj = clazz.newInstance(); // 创建 JavaBean 对象

            // 给 JavaBean 对象的属性赋值
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                    Object value = map.get(propertyName);
                    if ("".equals(value)) {
                        value = null;
                    }
                    Object[] args = new Object[1];
                    args[0] = value;
                    try {
                        descriptor.getWriteMethod().invoke(obj, args);
                    } catch (InvocationTargetException e) {
                        System.out.println("字段映射失败");
                    }
                }
            }
        } catch (IllegalAccessException e) {
            System.out.println("实例化 JavaBean 失败");
        } catch (IntrospectionException e) {
            System.out.println("分析类属性失败");
        } catch (IllegalArgumentException e) {
            System.out.println("映射错误");
        } catch (InstantiationException e) {
            System.out.println("实例化 JavaBean 失败");
        }
        return (T) obj;
    }


}
