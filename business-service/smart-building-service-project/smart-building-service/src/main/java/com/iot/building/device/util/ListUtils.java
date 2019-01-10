package com.iot.building.device.util;

import org.springframework.util.CollectionUtils;

import java.util.List;


public class ListUtils {

    public static<T> String changeListToStrBySeparator(List<T> dataList,String separator){
        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(dataList)) {
            for (T data : dataList
                 ) {
                Object temp=null;
                if (data instanceof String) {
                    temp = "'"+data+"'";
                }else{
                    temp = data;
                }
                sb.append(temp).append(separator);
            }
        }
        return sb.toString().substring(0,sb.toString().length()-1);
    }
}
