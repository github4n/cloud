package com.iot.shcs.common.util;

import java.util.Date;


public class ToolUtils {

    /**
     * 有毫秒
     * @param s
     * @return
     * @throws Exception
     */
    public static Date timestampToDate(Long s) throws Exception {
    	return new Date(s); 
    }
}
