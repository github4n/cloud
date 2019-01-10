package com.iot.common.util;

import com.iot.common.beans.SearchParam;
import com.iot.common.config.PropertyConfigurerUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ExceptionEnum;
import org.apache.commons.codec.binary.Base64;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 项目名称：IOT云平台
 * 模块名称：常用工具
 * 功能描述：日期工具类
 * 创建人： mao2080@sina.com
 * 创建时间： 2017年3月20日 上午11:13:00
 * 修改人： mao2080@sina.com
 * 修改时间： 2017年3月20日 上午11:13:00
 */
public class CommonUtil {

    /**
     * 描述：判断对象是否为空
     *
     * @param obj
     * @return
     * @author mao2080@sina.com
     * @created 2017年3月20日 上午11:33:55
     * @since
     */
    public static boolean isEmpty(Object obj) {
        return obj == null || "".equals(obj.toString());
    }

    /**
     * 描述：判断集合是否为空
     *
     * @param coll
     * @return
     * @author mao2080@sina.com
     * @created 2017年3月20日 上午11:33:55
     * @since
     */
    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * 描述：获取本机ip地址
     *
     * @return ip地址
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:12:05
     * @since
     */
    public static String getLocalIp() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostAddress().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 描述：转Array
     *
     * @param str   字符串
     * @param regex 分隔符
     * @return 返回Integer[]
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:50:58
     * @since
     */
    public static Integer[] toArray(String str, String regex) {
        if (isEmpty(str)) {
            return new Integer[0];
        }
        String[] strArray = str.split(regex);
        Integer[] intArry = new Integer[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            intArry[i] = Integer.valueOf(strArray[i]);
        }
        return intArry;
    }

    /**
     * 描述：字符串转Set
     *
     * @param str   字符串
     * @param regex 分隔符
     * @return 返回Set<String>
     * @author mao2080@sina.com
     * @created 2017年4月24日 下午2:25:01
     * @since
     */
    public static Set<String> stringToSet(String str, String regex) {
        if (isEmpty(str)) {
            return new HashSet<String>();
        }
        String[] strArray = str.split(regex);
        Set<String> set = new HashSet<String>();
        for (String s : strArray) {
            set.add(s);
        }
        return set;
    }

    /**
     * 描述：转Integer（用于不严格转换，比如接收可以默认为空的参数）
     *
     * @param object     要转的对象
     * @param defaultVal 默认值
     * @return
     * @author mao2080@sina.com
     * @created 2017年4月18日 下午4:46:20
     * @since
     */
    public static Integer toInteger(Object object, Integer defaultVal) {
        try {
            return Integer.valueOf(object.toString());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    /**
     * 描述：转Long（用于不严格转换，比如接收可以默认为空的参数）
     *
     * @param object     要转的对象
     * @param defaultVal 默认值
     * @return
     * @author mao2080@sina.com
     * @created 2017年4月21日 下午6:22:32
     * @since
     */
    public static Long toLong(Object object, Long defaultVal) {
        try {
            return Long.valueOf(object.toString());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    /**
     * 描述：
     *
     * @param log 日志信息
     * @return 封装后的日期信息
     * @author mao2080@sina.com
     * @created 2017年3月30日 上午10:05:30
     * @since
     */
    public static String getSystemLog(String log) {
        return "*************************** " + log + " ***************************";
    }

    /**
     * 描述：
     *
     * @param key 配置文件key
     * @return 封装后的日期信息
     * @author mao2080@sina.com
     * @created 2017年3月30日 上午10:05:30
     * @since
     */
    public static String getProperty(String key) {
        if (key.contains("pass")) {
            return "*************************** " + key + ": ******";
        } else {
            return "*************************** " + key + ": " + PropertyConfigurerUtil.PROPERTY_MAP.get(key);
        }
    }

    /**
     * 描述：
     *
     * @param key 配置文件key
     * @param val 配置文件val
     * @return 封装后的日期信息
     * @author mao2080@sina.com
     * @created 2017年3月30日 上午10:05:30
     * @since
     */
    public static String getProperty(String key, String val) {
        if (key.contains("pass")) {
            return "*************************** " + key + ": ******";
        } else {
            return "*************************** " + key + ": " + val;
        }
    }

    /**
     * 描述：生成随机数
     *
     * @param min
     * @param max
     * @return
     * @author 李帅
     * @created 2017年4月6日 下午3:43:43
     * @since
     */
    public static Integer getRandNum(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    /**
     * 描述：Base64编码
     *
     * @param source
     * @return
     * @throws Exception
     * @author mao2080@sina.com
     * @created 2017年4月10日 上午9:18:27
     * @since
     */
    public static String encodeBase64(byte[] source) throws Exception {
        return new String(Base64.encodeBase64(source), "UTF-8");
    }

    /**
     * 描述：Base64解码
     *
     * @param target
     * @return
     * @throws Exception
     * @author mao2080@sina.com
     * @created 2017年4月10日 上午9:18:43
     * @since
     */
    public static byte[] decodeBase64(String target) throws Exception {
        return Base64.decodeBase64(target.getBytes("UTF-8"));
    }

    /**
     * 描述：将二进制转换成16进制字符串
     *
     * @param buf 二进制数组
     * @return
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月7日 上午10:55:48
     * @since
     */
    public static String parseByte2HexStr(byte buf[]) throws BusinessException {
        if (buf == null) {
            throw new BusinessException(ExceptionEnum.ARGUMENT_EMPTY_EXCEPTION);
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 描述：将16进制转换为二进制
     *
     * @param hexStr 10进制字符串
     * @return
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月7日 下午2:16:42
     * @since
     */
    public static byte[] parseHexStr2Byte(String hexStr) throws BusinessException {
        if (hexStr.length() < 1) {
            throw new BusinessException(ExceptionEnum.ARGUMENT_SIGNATURE_EXCEPTION);
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int hig = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (hig * 16 + low);
        }
        return result;
    }

    /**
     * 描述：获取服务器IP地址
     *
     * @return
     * @author 李帅
     * @created 2017年5月12日 下午3:58:02
     * @since
     */
    @SuppressWarnings({"rawtypes"})
    public static String getServerIp() {
        String serverIp = null;
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip instanceof Inet4Address) {
                        serverIp = ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return serverIp;
    }

    /**
     * 描述：将jsonString转化为对象列表
     *
     * @param jsonString 字符串
     * @param clazz      对象
     * @return
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年8月25日 上午9:02:40
     * @since
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toObjectList(String jsonString, Class<T> clazz) throws BusinessException {
        List<T> temp = (List<T>) JsonUtil.fromJson(jsonString, List.class);
        if (temp == null || temp.isEmpty()) {
            return new ArrayList<T>();
        }
        List<T> list = new ArrayList<T>();
        for (Object object : temp) {
            if (object == null) {
                continue;
            }
            list.add(JsonUtil.fromJson(object.toString(), clazz));
        }
        temp.clear();
        return list;
    }

    /**
     * 描述：获取页码
     * @author mao2080@sina.com
     * @created 2018/5/18 9:46
     * @param searchParam
     * @return int
     */
    public static int getPageNum(SearchParam searchParam){
        Integer pageNum = searchParam.getPageNum();
        if (null == pageNum) {
            return 1;
        }
        return pageNum;
    }

    /**
     * 描述：获取每页条数
     * @author mao2080@sina.com
     * @created 2018/5/18 9:46
     * @param searchParam
     * @return int
     */
    public static int getPageSize(SearchParam searchParam){
        Integer pageSize = searchParam.getPageSize();
        if (null == pageSize || 0 == pageSize) {
            return 10;
        }
        return pageSize;
    }

    /**
     * 描述：分割Collection
     * @author mao2080@sina.com
     * @created 2018/5/18 9:46
     * @param collection 原始数据
     * @param pageSize 每个set数量
     * @return ListList<T>
     */
    public static <T> List<List<T>> splitCollectionToList(Collection<T> collection, int pageSize){
        if(collection == null || collection.isEmpty()){
            return Collections.emptyList();
        }
        pageSize = pageSize == 0?10000:pageSize;
        List<T> list = new ArrayList(collection);
        List<List<T>> newSet = new ArrayList<>();
        int st = 0;
        int size = collection.size();
        while(size>pageSize){
            newSet.add(new ArrayList(list.subList(st, st+pageSize)));
            st+=pageSize;
            size-=pageSize;
        }
        newSet.add(new ArrayList(list.subList(st, collection.size())));
        return newSet;
    }

    /**
     * 描述：分割Map
     * @author mao2080@sina.com
     * @created 2018/5/18 9:46
     * @param map 原始数据
     * @param pageSize 每个map数量
     * @return ListList<Map<K, V>>
     */
    public static <K, V> List<Map<K, V>> splitMap(Map<K, V> map, int pageSize){
        if(map == null || map.isEmpty()){
            return Collections.emptyList();
        }
        pageSize = pageSize == 0?10000:pageSize;
        List<Map<K, V>> newList = new ArrayList<>();
        int j = 0;
        for(K k :map.keySet()){
            if(j%pageSize == 0) {
                newList.add(new HashMap<>());
            }
            newList.get(newList.size()-1).put(k, map.get(k));
            j++;
        }
        return newList;
    }

    /**
     *
     * 描述：通过list的 subList(int fromIndex, int toIndex)方法实现
     * @author 李帅
     * @created 2017年11月2日 上午10:44:35
     * @since
     * @param sourList
     * @param batchCount
     */
    public static List<List<String>> dealBySubList(List<String> sourList, int batchCount) {
        int sourListSize = sourList.size();
        int subCount = sourListSize % batchCount == 0 ? sourListSize / batchCount : sourListSize / batchCount + 1;
        int startIndext = 0;
        int stopIndext = 0;
        int endIndext = sourListSize % batchCount == 0 ? batchCount : sourListSize % batchCount;
        List<List<String>> tempListList = new ArrayList<List<String>>();
        List<String> tempList = null;
        for (int i = 0; i < subCount; i++) {
            stopIndext = (i == subCount - 1) ? stopIndext + endIndext : stopIndext + batchCount;
            tempList = new ArrayList<String>(sourList.subList(startIndext, stopIndext));
            tempListList.add(tempList);
            startIndext = stopIndext;
        }
        return tempListList;
    }

    /**
     * 描述：获取自增数组(字符)
     * @author maochengyuan
     * @created 2018/8/7 16:12
     * @param sta 开始下标
     * @param size 数组大小
     * @return java.util.Set<java.lang.String>
     */
    public static Set<String> getStrKeys(int sta, int size){
        return Stream.iterate(sta, item -> item+1).limit(size).map(Object::toString).collect(Collectors.toSet());
    }

    /**
     * 描述：获取自增数组(数字)
     * @author maochengyuan
     * @created 2018/8/7 16:12
     * @param sta 开始下标
     * @param size 数组大小
     * @return java.util.Set<java.lang.String>
     */
    public static Set<Integer> getIntKeys(int sta, int size){
        return Stream.iterate(sta, item -> item+1).limit(size).collect(Collectors.toSet());
    }

    public static void main(String[] args) {
        System.out.println(isEmpty(""));
        System.out.println(getLocalIp());
        System.out.println(toArray("2,3,4,8", ",").length);
    }

}
