package com.iot.redis;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 项目名称：IOT云平台
 * 模块名称：redis
 * 功能描述：redis工具类
 * 创建人： laiguiming
 * 创建时间：2018年3月16日 下午9:38:59
 */
@Component
public class RedisCacheUtil {

    /**
     * 分割每页数据大小
     */
    private static final int SPLIT_PAGE_SIZE = 10000;
    /**
     * 日志
     */
    private static Logger LOGGER = LoggerFactory.getLogger(RedisCacheUtil.class);
    /**
     * 操作模板
     */
    private static RedisTemplate<String, Object> redisTemplate;
    /**
     * 模块module
     */
    private static String module;
    /**
     * 版本ver
     */
    private static String ver;

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("StringRedisTemplate")
    private RedisTemplate stringRedisTemplate;

    /**
     * 组装keys
     *
     * @param keys
     * @return
     */
    private static Collection<String> serializeKeys(Collection<String> keys) {
        Set<String> sets = new HashSet<String>();
        for (String key : keys) {
            sets.add(serializeKey(key));
        }
        return sets;
    }

    /**
     * 序列化key
     *
     * @param key
     * @return
     */
    private static String serializeKey(String key) {
        if (StringUtil.isBlank(key)) {
            LOGGER.warn("Redis缓存数据，key不能为空！");
            return "";
        }
        String newKey = "";
        if (StringUtils.isNotBlank(module)) {
            newKey += module + ":";
        }
        newKey += key;
        if (StringUtils.isNotBlank(ver)) {
            newKey += ":" + ver;
        }
        return newKey;
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public static Boolean hasKey(String key) {
        return redisTemplate.hasKey(serializeKey(key));
    }

    /**
     * 缓存自增长值
     * （自增长，并缓存值）
     *
     * @param key
     * @param liveTime
     * @return
     */
    public static Long incr(String key, long liveTime) {
        Long increment = redisTemplate.opsForValue().increment(key, 1);
        if (liveTime > 0) {
            redisTemplate.expire(key, liveTime, TimeUnit.SECONDS);
        }
        return increment;
    }

    /**
     * 获取自增长缓存值
     * （只获取值，不自增）
     *
     * @param key
     * @return
     */
    public static Long getIncr(String key) {
        Object increment = redisTemplate.opsForValue().get(key);
        if (increment != null) {
            return Long.parseLong(increment.toString());
        }
        return 0L;
    }

    /**
     * 获取字符串缓存
     *
     * @param key 键
     * @return
     */
    public static String valueGet(String key) {
        return (String) redisTemplate.opsForValue().get(serializeKey(key));
    }

    /**
     * 缓存对象
     *
     * @param key   键
     * @param value 值
     */
    public static void valueSet(String key, String value) {
        if (value != null) {
            redisTemplate.opsForValue().set(serializeKey(key), value);
        }
    }

    /***************************************对外提供方法***************************************/

    /**
     * 存对象数据
     *
     * @param key   键
     * @param value 值
     * @param time  失效时间（秒）
     */
    public static void valueSet(String key, String value, Long time) {
        redisTemplate.opsForValue().set(serializeKey(key), value, time, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存对象
     *
     * @param key        键
     * @param valueClass 类
     * @param <T>
     * @return
     */
    public static <T> T valueObjGet(String key, Class<T> valueClass) {
        String valueJson = (String) redisTemplate.opsForValue().get(serializeKey(key));
        if (StringUtils.isNotBlank(valueJson)) {
            if (valueClass.equals(String.class)) {
                return (T) valueJson;
            }
            return JSON.parseObject(valueJson, valueClass);
        }
        return null;
    }
    /**
     * 获取缓存对象
     *
     * @param key        键
     * @param valueClass 类
     * @param newVal 新值
     * @param <T>
     * @return
     */
    public static <T> T valueObjGetAndSet(String key, Class<T> valueClass, String newVal) {
        String valueJson = (String) redisTemplate.opsForValue().getAndSet(serializeKey(key),newVal);
        if (StringUtils.isNotBlank(valueJson)) {
            if (valueClass.equals(String.class)) {
                return (T) valueJson;
            }
            return JSON.parseObject(valueJson, valueClass);
        }
        return null;
    }

    /**
     * 缓存对象
     *
     * @param key   键
     * @param value 值
     * @param <T>
     */
    public static <T> void valueObjSet(String key, T value) {
        redisTemplate.opsForValue().set(serializeKey(key), JSON.toJSONString(value));
    }

    /**
     * 存对象数据
     *
     * @param key   键
     * @param value 值
     * @param time  失效时间（秒）
     */
    public static <T> void valueObjSet(String key, T value, Long time) {
        redisTemplate.opsForValue().set(serializeKey(key), JSON.toJSONString(value), time, TimeUnit.SECONDS);
    }

    /**
     * 取Map类型数据
     *
     * @param key        键
     * @param valueClass 类
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> hashGetAll(String key, Class<T> valueClass) {
        Map<Object, Object> rmap = redisTemplate.opsForHash().entries(serializeKey(key));
        Map<String, T> resMap = Maps.newHashMap();
        for (Map.Entry<Object, Object> entry : rmap.entrySet()) {
            resMap.put(entry.getKey().toString(), JSON.parseObject(entry.getValue().toString(), valueClass));
        }
        return resMap;
    }

    /**
     * 取Map类型数据
     *
     * @param key        键
     * @param valueClass 类
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> hashGetAll(String key, Class<T> valueClass, Boolean json) {
        Map<Object, Object> rmap = redisTemplate.opsForHash().entries(serializeKey(key));
        Map<String, T> resMap = Maps.newHashMap();
        for (Map.Entry<Object, Object> entry : rmap.entrySet()) {
            if (json) {
                resMap.put(entry.getKey().toString(), JSON.parseObject(entry.getValue().toString(), valueClass));
            } else {
                resMap.put(entry.getKey().toString(), (T) entry.getValue());
            }
        }
        return resMap;
    }
    /**
     * 从hsah中根据key批量获取数组
     *
     * @param key
     * @param valueClass
     * @return
     */
    public static <T> Map<String, List<T>> hashGetAllArray(String key, Class<T> valueClass) {
        Map<Object, Object> rmap = redisTemplate.opsForHash().entries(serializeKey(key));
        Map<String, List<T>> resMap = Maps.newHashMap();
        if(null != rmap && !rmap.isEmpty()){
            for (Map.Entry<Object, Object> entry : rmap.entrySet()) {
                if(null != entry.getValue()){
                    resMap.put(entry.getKey().toString(), JSON.parseArray(entry.getValue().toString(), valueClass) );
                }
            }
        }
        return resMap;
    }

    /**
     * @param key
     * @param hashKey
     * @param hashValueClass
     * @param <T>
     * @return
     */
    public static <T> T hashGet(String key, String hashKey, Class<T> hashValueClass) {
        String hashValueJson = (String) redisTemplate.opsForHash().get(serializeKey(key), hashKey);
        if (StringUtils.isNotBlank(hashValueJson)) {
            return JSON.parseObject(hashValueJson, hashValueClass);
        }
        return null;
    }
    /**
     * 从hsah中根据key获取数组
     *
     * @param key
     * @param hashKey
     * @return
     */
    public static <T> List<T> hashGetArray(String key, String hashKey, Class<T> hashValueClass) {
        String hashValueJson = (String) redisTemplate.opsForHash().get(serializeKey(key), hashKey);
        if (StringUtils.isNotBlank(hashValueJson)) {
            return  JSON.parseArray(hashValueJson, hashValueClass);
        }
        return null;
    }

    /**
     * 从hsah中根据key获取字符串
     *
     * @param key
     * @param hashKey
     * @return
     */
    public static String hashGetString(String key, String hashKey) {
        return (String) redisTemplate.opsForHash().get(serializeKey(key), hashKey);
    }

    /**
     * 存Map类型数据
     *
     * @param key 键
     * @param map 值
     * @param <T>
     */
    public static <T> void hashPutAll(String key, Map<String, T> map) {
        //循环添加
        Map<String, String> smap = Maps.newHashMap();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            smap.put(entry.getKey(), JSON.toJSONString(entry.getValue()));
        }
        redisTemplate.opsForHash().putAll(serializeKey(key), smap);
    }

    /**
     * 存Map类型数据
     *
     * @param key 键
     * @param map 值
     * @param <T>
     */
    public static <T> void hashPutAll(String key, Map<String, T> map, boolean toJson) {
        //循环添加
        Map<String, String> smap = Maps.newHashMap();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            if (toJson) {
                smap.put(entry.getKey(), JSON.toJSONString(entry.getValue()));
            } else {
                smap.put(entry.getKey(), entry.getValue().toString());
            }
        }
        redisTemplate.opsForHash().putAll(serializeKey(key), smap);
    }

    /**
     * 存Map类型数据
     *
     * @param key 键
     * @param map 值
     * @param <T>
     */
    public static <T> void hashPutAll(String key, Map<String, T> map, boolean toJson, Long time) {
        //循环添加
        Map<String, String> smap = Maps.newHashMap();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            if (toJson) {
                smap.put(entry.getKey(), JSON.toJSONString(entry.getValue()));
            } else {
                smap.put(entry.getKey(), entry.getValue().toString());
            }
        }
        redisTemplate.opsForHash().putAll(serializeKey(key), smap);
        redisTemplate.expire(serializeKey(key), time, TimeUnit.SECONDS);
    }

    /**
     * map插入缓存
     *
     * @param key       键
     * @param hashKey   hash键
     * @param hashValue hash值
     * @param <T>
     */
    public static <T> void hashPut(String key, String hashKey, T hashValue) {
        if (hashValue != null) {
            redisTemplate.opsForHash().put(serializeKey(key), hashKey, JSON.toJSONString(hashValue));
        }
    }

    /**
     * map插入缓存
     *
     * @param key       键
     * @param hashKey   hash键
     * @param hashValue hash值
     * @param <T>
     */
    public static <T> void hashPut(String key, String hashKey, T hashValue, boolean toJson) {
        if (hashValue != null) {
            if (toJson) {
                redisTemplate.opsForHash().put(serializeKey(key), hashKey, JSON.toJSONString(hashValue));
            } else {
                redisTemplate.opsForHash().put(serializeKey(key), hashKey, hashValue.toString());
            }
        }
    }

    /**
     * map插入缓存，可设置过期时间
     *
     * @param key       键
     * @param hashKey   hash键
     * @param hashValue hash值
     * @param timeout   过期时间（秒）
     * @param toJson    是否需要json转化
     * @param <T>
     */
    public static <T> void hashPut(String key, String hashKey, T hashValue, Long timeout, boolean toJson) {
        if (hashValue != null) {
            if (toJson) {
                redisTemplate.opsForHash().put(serializeKey(key), hashKey, JSON.toJSONString(hashValue));
            } else {
                redisTemplate.opsForHash().put(serializeKey(key), hashKey, hashValue);
            }
            if (timeout != null && timeout != 0) {
                redisTemplate.expire(serializeKey(key), timeout, TimeUnit.SECONDS);
            }
        }
    }

    /**
     * map元素移除
     *
     * @param key
     * @param hashKey
     */
    public static void hashRemove(String key, String hashKey) {
        redisTemplate.opsForHash().delete(serializeKey(key), hashKey);
    }

    /**
     * map元素移除
     *
     * @param key
     * @param hashKeys
     */
    public static void hashRemove(String key, List<String> hashKeys) {
        redisTemplate.opsForHash().delete(serializeKey(key), hashKeys.toArray());
    }
    /**
     * 缓存list数据
     *
     * @param key  键
     * @param list 类型值
     * @param <T>
     */
    public static <T> void listSet(String key, List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        //先清除
        if (hasKey(key)) {
            delete(key);
        }

        //循环添加
        for (T t : list) {
            redisTemplate.opsForList().rightPush(serializeKey(key), JSON.toJSONString(t));
        }
    }

    /**
     * list插入缓存，可设置过期时间
     *
     * @param key     键
     * @param list    类型值
     * @param timeout 过期时间（秒）
     * @param toJson  是否需要json转化
     * @param <T>
     */
    public static <T> void listSet(String key, List<T> list, Long timeout, boolean toJson) {
        if (CollectionUtils.isNotEmpty(list)) {
            //先清除
            if (hasKey(key)) {
                delete(key);
            }
            if (toJson) {
                //循环添加
                for (T t : list) {
                    redisTemplate.opsForList().rightPush(serializeKey(key), JSON.toJSONString(t));
                }
            } else {
                //循环添加
                for (T t : list) {
                    redisTemplate.opsForList().rightPush(serializeKey(key), t);
                }
            }
            if (timeout != null && timeout != 0) {
                redisTemplate.expire(serializeKey(key), timeout, TimeUnit.SECONDS);
            }
        }
    }

    /**
     * 获取list缓存
     *
     * @param key   键
     * @param <T>
     * @param start 开始索引
     * @param end   结束索引
     * @return
     */
    public static <T> List<T> listGet(String key, Class<T> valueClass, int start, int end) {
        List<Object> list = redisTemplate.opsForList().range(serializeKey(key), start, end);
        List<T> rlist = Lists.newArrayList();
        for (Object obj : list) {
            rlist.add(JSON.parseObject(obj.toString(), valueClass));
        }
        return rlist;
    }


    /**
     * 批量key 获取值
     *
     * @param keys 批量keys
     * @return
     * @author lucky
     * @date 2018/6/15 16:49
     */
    public static List<String> mget(List<String> keys) {
        Jedis jedis = null;
        try {
            if (!CollectionUtils.isEmpty(keys)) {
                JedisPool jedisPool = ApplicationContextHelper.getBean(JedisPool.class);
                jedis = jedisPool.getResource();
                List<String> values = jedis.mget(keys.toArray(new String[keys.size()]));
                if (!CollectionUtils.isEmpty(values)) {
                    List<String> listTemp = Lists.newArrayList();
                    for (String value : values) {
                        if (StringUtils.isBlank(value)) {
                            continue;
                        }
                        listTemp.add(value);
                    }
                    return listTemp;
                }
                jedis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 批量key 获取返回对象值
     *
     * @param keys
     * @param returnObjClass
     * @param <T>
     * @return
     */
    public static <T> List<T> mget(List<String> keys, Class<T> returnObjClass) {
        Jedis jedis = null;
        try {
            if (!CollectionUtils.isEmpty(keys)) {
                JedisPool jedisPool = ApplicationContextHelper.getBean(JedisPool.class);
                jedis = jedisPool.getResource();
                List<String> jsonList = jedis.mget(keys.toArray(new String[keys.size()]));
                if (!CollectionUtils.isEmpty(jsonList)) {
                    List<T> returnList = Lists.newArrayList();
                    for (String value : jsonList) {
                        if (StringUtils.isEmpty(value)) {
                            continue;
                        }
                        returnList.add(JSON.parseObject(value, returnObjClass));
                    }
                    return returnList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 描述：hash 批量key 获取值
     * @author maochengyuan
     * @created 2018/7/16 9:48
     * @param key key
     * @param keySet hash-keys
     * @return java.util.List<java.lang.Object>
     */
    public static Map<String, String> hashMultiGet(String key, Collection<String> keySet) {
        if(CommonUtil.isEmpty(key) || CommonUtil.isEmpty(keySet)){
            return new HashMap<>();
        }
        List<Object> keys = keySet.stream().collect(Collectors.toList());
        List<Object> list = redisTemplate.opsForHash().multiGet(key, keys);
        Map<String, String> temp = new HashMap<>();
        for(int i=0; i<keySet.size(); i++){
            Object k = keys.get(i);
            Object v = list.get(i);
            if(!CommonUtil.isEmpty(k) && !CommonUtil.isEmpty(v)){
                temp.put(k.toString(), v.toString());
            }
        }
        return temp;
    }

    /**
     * 描述：hash批量key 获取值 json  返List
     * @author 490485964@qq.com
     * @date 2018/8/6 17:50
     * @param keySet
     * @param key
     * @return
     */
    public static <T> List<T> hashMultiGetArray(String key, Collection<String> keySet, Class<T> returnObjClass) {
        if(CommonUtil.isEmpty(key) || CommonUtil.isEmpty(keySet)){
            return Collections.emptyList();
        }
        List<Object> keys = keySet.stream().collect(Collectors.toList());
        List<Object> list = redisTemplate.opsForHash().multiGet(key, keys);
        List<T> returnList = Lists.newArrayList();
        if(!org.springframework.util.CollectionUtils.isEmpty(list)){
            for (Object value : list) {
                if (CommonUtil.isEmpty(value)) {
                    continue;
                }
                returnList.add(JSON.parseObject(value.toString(), returnObjClass));
            }
        }
        return returnList;
    }

    /**
     * hash 批量key 获取值
     *
     * @param key
     * @param fields
     * @return
     * @author lucky
     * @date 2018/6/15 16:54
     */
    public static List<String> hashMget(String key, String... fields) {
        Jedis jedis = null;
        List<String> returnList = null;
        try {
            JedisPool jedisPool = ApplicationContextHelper.getBean(JedisPool.class);
            jedis = jedisPool.getResource();
            List<String> jsonList = jedis.hmget(key, fields);
            if (!CollectionUtils.isEmpty(jsonList)) {
                returnList = Lists.newArrayList();
                for (String value : jsonList) {
                    if (StringUtils.isEmpty(value)) {
                        continue;
                    }
                    returnList.add(value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return returnList;
    }


    /**
     * 获取list缓存
     *
     * @param key 键
     * @param <T>
     * @return
     */
    public static <T> List<T> listGetAll(String key, Class<T> valueClass) {
        return listGet(key, valueClass, 0, -1);
    }

    /**
     * 获取list缓存
     * @param key 键
     * @return
     */
    public static List<String> listGetAll(String key) {
        List<Object> list = redisTemplate.opsForList().range(serializeKey(key), 0, -1);
        List<String> result = new ArrayList<>();
        if (list != null && !list.isEmpty()){
            list.forEach(o -> {
                result.add(o.toString());
            });
        }
        return result;
    }

    /**
     * list插入数据
     *
     * @param key   键
     * @param value 值
     * @param <T>
     */
    public static <T> void listPush(String key, T value) {
        if (value == null) {
            return;
        }
        redisTemplate.opsForList().rightPush(serializeKey(key), JSON.toJSONString(value));
    }

    /**
     * list插入数据
     *
     * @param key   键
     * @param value 值
     * @param <T>
     */
    public static <T> void listPush(String key, T value, boolean toJson) {
        if (value == null) {
            return;
        }
        if (toJson) {
            listPush(key,value);
        }else {
            redisTemplate.opsForList().rightPush(serializeKey(key), value);
        }
    }

    public static <T> void listLeftPushAll(String key, List<T> values) {
        if (StringUtil.isEmpty(key)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(values)) {
            values.forEach(value -> {
                redisTemplate.opsForList().rightPush(serializeKey(key), JSON.toJSONString(value));
            });
        } else {
            redisTemplate.opsForList().rightPush(serializeKey(key), JSON.toJSONString(null));
        }
    }
    /**
     * list左插入数据
     *
     * @param key   键
     * @param value 值
     * @param <T>
     */
    public static <T> void listLeftPush(String key, T value) {
        if (StringUtil.isEmpty(key)) {
            return;
        }
        redisTemplate.opsForList().leftPush(serializeKey(key), JSON.toJSONString(value));
    }

    /**
     * list移除数据
     *
     * @param key   键
     * @param value 值
     * @param <T>
     */
    public static <T> void removeListValue(String key, Long count, T value) {
        if (value == null) {
            return;
        }
        if (count == null || count == 0) {
            count = 1L;
        }
        redisTemplate.opsForList().remove(serializeKey(key), count, JSON.toJSONString(value));
    }

    /**
     * 设置set缓存
     *
     * @param key
     * @param value
     * @param toJson 是否json解析
     * @param <T>
     */
    public static <T> void setAdd(String key, Set<T> value, boolean toJson) {
        if (toJson) {
            Set<String> set = new HashSet<>();
            for (T t : value) {
                set.add(JSON.toJSONString(t));
            }
            List<List<String>> list = CommonUtil.splitCollectionToList(set, SPLIT_PAGE_SIZE);
            list.forEach(sub -> {
                redisTemplate.opsForSet().add(serializeKey(key), sub.toArray());
            });
        } else {
            List<List<T>> list = CommonUtil.splitCollectionToList(value, SPLIT_PAGE_SIZE);
            list.forEach(sub -> {
                redisTemplate.opsForSet().add(serializeKey(key), sub.toArray());
            });
        }
    }

    /**
     * 描述：获取Hash集合大小
     * @author maochengyuan
     * @created 2018/11/15 14:40
     * @param key key
     * @return java.lang.Long
     */
    public static Long getHashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 描述：获取Set集合大小
     * @author maochengyuan
     * @created 2018/11/15 14:40
     * @param key key
     * @return java.lang.Long
     */
    public static Long getSetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 描述：获取ZSet集合大小
     * @author maochengyuan
     * @created 2018/11/15 14:40
     * @param key key
     * @return java.lang.Long
     */
    public static Long getZSetSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 描述：获取List集合大小
     * @author maochengyuan
     * @created 2018/11/15 14:40
     * @param key key
     * @return java.lang.Long
     */
    public static Long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 描述：删除Set元素并返回Set大小
     * @created 2018/11/15 14:40
     * @param key key
     * @return java.lang.Long
     */
    public static Long setDelAndGetSize(String key, Collection<String> ids) {
        redisTemplate.opsForSet().remove(key, ids.toArray());
        return getSetSize(key);
    }

    /**
     * 描述：向Set中添加元素并返回Set大小
     * @created 2018/11/15 14:40
     * @param key key
     * @return java.lang.Long
     */
    public static Long setAddAndGetSize(String key, Collection<String> ids) {
        redisTemplate.opsForSet().add(key, ids.toArray());
        return getSetSize(key);
    }

    /**
     * 描述：批量添加Key-Hash数据
     *
     * @param maps 数据
     * @return void
     * @author mao2080@sina.com
     * @created 2018/6/15 09:51
     */
    public static void hashPutBatch(Map<String, Map<String, String>> maps) {
        if (maps == null || maps.isEmpty()) {
            return;
        }
        List<Map<String, Map<String, String>>> list = CommonUtil.splitMap(maps, SPLIT_PAGE_SIZE);
        list.forEach(sub -> {
            redisTemplate.executePipelined((RedisCallback<Object>) connect -> {
                sub.forEach((key, map) -> {
                    if (map != null && !map.isEmpty()) {
                        map.forEach((skey, sval) -> {
                            connect.hSet(key.getBytes(), skey.getBytes(), sval.getBytes());
                        });
                    }
                });
                return null;
            });
        });
    }

    /**
     * 描述：批量添加Key-Hash数据
     * @author maochengyuan
     * @created 2018/8/7 17:16
     * @param mainKey key
     * @param maps hash
     * @param toJson 是否序列化
     * @return void
     */
    public static <T> void hashPutBatch(String mainKey, Map<String, T> maps, Long timeout, boolean toJson) {
        if (maps == null || maps.isEmpty()) {
            return;
        }
        List<Map<String, T>> list = CommonUtil.splitMap(maps, SPLIT_PAGE_SIZE);
        list.forEach(sub ->{
            redisTemplate.executePipelined((RedisCallback<Object>) connect -> {
                sub.forEach((key, val) ->{
                    if(toJson){
                        connect.hSet(mainKey.getBytes(), key.getBytes(), JsonUtil.toJson(val).getBytes());
                    }else{
                        connect.hSet(mainKey.getBytes(), key.getBytes(), StringUtil.toString(val).getBytes());
                    }
                });
                return null;
            });
        });
        if (timeout != null && timeout != 0) {
            redisTemplate.expire(serializeKey(mainKey), timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 描述：批量添加Key-Set数据
     *
     * @param maps 数据
     * @return void
     * @author mao2080@sina.com
     * @created 2018/6/14 10:51
     */
    public static void setAddBatch(Map<String, Set<String>> maps) {
        if (maps == null || maps.isEmpty()) {
            return;
        }
        List<Map<String, Set<String>>> list = CommonUtil.splitMap(maps, SPLIT_PAGE_SIZE);
        list.forEach(sub -> {
            redisTemplate.executePipelined((RedisCallback<Object>) connect -> {
                sub.forEach((k, v) -> {
                    List<byte[]> vs1 = v.stream().map(e -> ((StringRedisSerializer) redisTemplate.getValueSerializer()).serialize(e)).collect(Collectors.toList());
                    connect.sAdd(k.getBytes(), vs1.toArray(new byte[][]{}));
                });
                return null;
            });
        });
    }

    /**
     * 描述：批量添加Key-String数据
     *
     * @param maps 数据
     * @return void
     * @author mao2080@sina.com
     * @created 2018/6/14 10:51
     */
    public static void valueSetBatch(Map<String, String> maps) {
        if (maps == null || maps.isEmpty()) {
            return;
        }
        List<Map<String, String>> list = CommonUtil.splitMap(maps, SPLIT_PAGE_SIZE);
        list.forEach(sub -> {
            redisTemplate.executePipelined((RedisCallback<Object>) connect -> {
                sub.forEach((k, v) -> {
                    connect.set(k.getBytes(), v.getBytes());
                });
                return null;
            });
        });
    }

    /**
     * 获取set所有缓存
     *
     * @param key    健
     * @param <T>
     * @param toJson
     * @return
     */
    public static <T> Set<T> setGetAll(String key, Class<T> valueClass, boolean toJson) {
        Set<Object> jsonValue = redisTemplate.opsForSet().members(serializeKey(key));
        Set<T> setValue = new HashSet<T>();
        if (toJson) {
            for (Object o : jsonValue) {
                setValue.add(JSON.parseObject(o.toString(), valueClass));
            }
        } else {
            if (valueClass.equals(String.class)) {
                for (Object o : jsonValue) {
                    setValue.add((T) o);
                }
            }else{
                for (Object o : jsonValue) {
                    setValue.add(JSON.parseObject(o.toString(), valueClass));
                }
            }
        }

        return setValue;
    }

    /**
     * 存入单个set元素
     *
     * @param key
     * @param value
     * @param <T>
     * @param toJson
     */
    public static <T> void setPush(String key, T value, boolean toJson) {
        if (toJson) {
            redisTemplate.opsForSet().add(serializeKey(key), JSON.toJSONString(value));
        } else {
            redisTemplate.opsForSet().add(serializeKey(key), value);
        }
    }

    /**
     * 取出单个set元素，并移除
     *
     * @param key
     * @param <T>
     * @param toJson
     * @return
     */
    public static <T> T setPop(String key, Class<T> valueClass, boolean toJson) {
        String value = (String) redisTemplate.opsForSet().pop(serializeKey(key));
        if (toJson) {
            return JSON.parseObject(value, valueClass);
        } else {
            return (T) value;
        }
    }

    /**
     * 移除单个set元素
     *
     * @param key
     * @param value
     * @param toJson
     * @param <T>
     * @return
     */
    public static <T> Long setRemove(String key, T value, boolean toJson) {
        Long res;
        if (toJson) {
            String jsonValue = JSON.toJSONString(value);
            res = redisTemplate.opsForSet().remove(serializeKey(key), jsonValue);
        } else {
            res = redisTemplate.opsForSet().remove(serializeKey(key), value);
        }

        return res;
    }

    /**
     * 单个删除
     *
     * @param key 键
     */
    public static void delete(String key) {
        try {
            redisTemplate.delete(serializeKey(key));
        } catch (Exception e) {
            LOGGER.info("delete-key" + key + " error", e);
        }
    }
    
    /**
     * 模糊删除
     *
     * @param key 键
     */
    public static void deleteBlurry(String key) {
        try {
        	redisTemplate.delete(redisTemplate.keys(key+"*"));
        } catch (Exception e) {
            LOGGER.info("delete-key" + key + " error", e);
        }
    }
    
    /**
     * 批量删除
     *
     * @param keys 键组
     */
    public static void delete(Collection<String> keys) {
        try {
            if (CollectionUtils.isEmpty(keys)) {
                return;
            }
            redisTemplate.delete(serializeKeys(keys));
        } catch (Exception e) {
            LOGGER.info("delete-key error", e);
        }
    }

    /**
     * @param keyStr
     * @return
     * @despriction：模糊搜索key
     * @author yeshiyuan
     * @created 2018/5/30 15:31
     */
    public static Set<String> keys(String keyStr) {
        return redisTemplate.keys(keyStr);
    }

    /**
     * 设置key 超时
     *
     * @param key
     * @param timeout
     * @return
     * @author lucky
     * @date 2018/6/27 17:29
     */
    public static void expireKey(String key, long timeout) {
        redisTemplate.expire(serializeKey(key), timeout, TimeUnit.SECONDS);
    }

    /**
     * @param map
     * @despriction：value的批量插入
     * @author yeshiyuan
     * @created 2018/6/26 11:13
     */
    public static void mset(Map<String, String> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * @param keys         rediskey
     * @param toRemoveNull 是否去空
     * @return
     * @despriction：string类型批量获取value
     * @author yeshiyuan
     * @created 2018/6/26 13:39
     */
    public static List<String> mget(List<String> keys, boolean toRemoveNull) {
        List<String> result = null;
        if (!CollectionUtils.isEmpty(keys)) {
            if (toRemoveNull) {
                result = mget(keys);
            } else {
                result = new ArrayList<>();
                List<Object> list = redisTemplate.opsForValue().multiGet(keys);
                for (Object object : list) {
                    if (object == null) {
                        result.add("");
                    } else {
                        result.add(object.toString());
                    }
                }
            }
        }
        return result;
    }

    /**
     * @param map
     * @param expired 有效时长
     * @despriction：value的批量插入
     * @author yeshiyuan
     * @created 2018/6/26 11:13
     */
    public static void mset(Map<String, String> map, Long expired) {
        if (expired.longValue() == 0) {
            redisTemplate.opsForValue().multiSet(map);
        } else {
            RedisSerializer<String> stringSerializer = redisTemplate.getStringSerializer();
            redisTemplate.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    map.forEach((k, v) -> {
                        byte[] bk = stringSerializer.serialize(k);
                        byte[] bv = stringSerializer.serialize(v);
                        redisConnection.setEx(bk, expired, bv);
                    });
                    return null;
                }
            });
        }
    }

    public static void multiSet(Map<String, Object> map, Long expired) {
        Map<String, String> smap = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            smap.put(serializeKey(entry.getKey()), JSON.toJSONString(entry.getValue()));
        }

        if (expired == 0) {
            redisTemplate.opsForValue().multiSet(smap);
        } else {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                smap.put(serializeKey(entry.getKey()), JSON.toJSONString(entry.getValue()));
            }

            RedisSerializer<String> stringSerializer = redisTemplate.getStringSerializer();
            redisTemplate.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    smap.forEach((k, v) -> {
                        byte[] bk = stringSerializer.serialize(k);
                        byte[] bv = stringSerializer.serialize(v);
                        redisConnection.setEx(bk, expired, bv);
                    });
                    return null;
                }
            });
        }
    }

    /** 
     * 描述：向指定队列发送消息
     * @author maochengyuan
     * @created 2018/7/25 17:18
     * @param channel 队列名称
     * @param message 消息内容
     * @return void
     */
    public static void convertAndSend(String channel, String message){
        redisTemplate.convertAndSend(channel, message);
    }

    public static boolean zSetAdd(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(serializeKey(key), value, score);
    }

    public static void zSetAdd(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        redisTemplate.opsForZSet().add(serializeKey(key), tuples);
    }

    public static Double zSetGetScore(String key, Object valueKey) {
        return redisTemplate.opsForZSet().score(serializeKey(key), valueKey.toString());
    }

    public static void zSetRemove(String key, String value) {
        redisTemplate.opsForZSet().remove(serializeKey(key), value);
    }

    public static void zSetRemoveMultiValue(String key, String[] value) {
        redisTemplate.opsForZSet().remove(serializeKey(key), (Object[]) value);
    }

    public static void zSetRemoveRangeByRank(String key, long start, long end) {
        redisTemplate.opsForZSet().removeRange(serializeKey(key), start, end);
    }

    public static Set<String> zSetRangeByScore(String key, double min, double max) {
        Set<Object> objectSet = redisTemplate.opsForZSet().rangeByScore(serializeKey(key), min, max);
        return convertToStringSet(objectSet);
    }

    private static Set<String> convertToStringSet(Set<Object> objectSet) {
        TreeSet<String> treeSet = new TreeSet<>();
        if (objectSet == null || objectSet.size() == 0) {
            return treeSet;
        }

        for (Object object : objectSet) {
            if (object != null) {
                treeSet.add(object.toString());
            }
        }
        return treeSet;
    }

    public static Set<String> zSetRangeByPage(String key, int index, int size) {
        return zSetRangeByPage(key, 0, Double.MAX_VALUE, index, size);
    }

    public static Set<String> zSetRangeByPage(String key, double min, double max, int index, int size) {
        Set<Object> objectSet = redisTemplate.opsForZSet().rangeByScore(serializeKey(key), min, max, index, size);
        return convertToStringSet(objectSet);
    }

    public static Set<String> zSetReverseRangeByPage(String key, int index, int size) {
        Set<Object> objectSet = redisTemplate.opsForZSet().reverseRangeByScore(serializeKey(key), 0, Double.MAX_VALUE, index, size);
        return convertToStringSet(objectSet);
    }

    public static Set<String> zSetReverseRangeByPage(String key, double min, double max, int index, int size) {
        Set<Object> objectSet = redisTemplate.opsForZSet().reverseRangeByScore(serializeKey(key), min, max, index, size);
        return convertToStringSet(objectSet);
    }

    public static Set<String> zSetReverseRange(String key, long start, long end) {
        Set<Object> objectSet = redisTemplate.opsForZSet().reverseRange(serializeKey(key), start, end);
        return convertToStringSet(objectSet);
    }

    public static Set<String> zSetReverseRangeByScore(String key, double min, double max) {
        Set<Object> objectSet = redisTemplate.opsForZSet().reverseRangeByScore(serializeKey(key), min, max);
        return convertToStringSet(objectSet);
    }

    public static Set<String> zSetRange(String key, long start, long end) {
        Set<Object> objectSet = redisTemplate.opsForZSet().range(serializeKey(key), start, end);
        return convertToStringSet(objectSet);
    }

    public static Set<Object> zSetReverseRangeAll(String key) {
        Set<Object> objectSet = redisTemplate.opsForZSet().reverseRange(serializeKey(key), 0, -1);
        return objectSet;
    }

    public static Integer zSetReverseRank(String key, String zSetKey) {
        return redisTemplate.opsForZSet().reverseRank(serializeKey(key), zSetKey).intValue();
    }

    public static Long zSetRank(String key, String member) {
        return redisTemplate.opsForZSet().rank(serializeKey(key), member);
    }

    public static Long zSetSize(String key) {
        return redisTemplate.opsForZSet().size(serializeKey(key));
    }

    /**
     * @param key
     * @param delValue 待删除的元素
     * @param count    删除数量
     * @return
     * @despriction：list类型的删除操作
     * @author yeshiyuan
     * @created 2018/7/16 19:47
     */
    public static Long listRemove(String key, String delValue, int count) {
        return redisTemplate.opsForList().remove(serializeKey(key), count, delValue);
    }

    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在. 若给定的 key 已经存在，则 SETNX 不做任何动作。
     *
     * @param key
     * @param value
     * @param expireTime 过期时间(单位秒)
     * @return 若key不存在, 返回true; 若key已经存在,返回false
     */
    public static boolean setNx(final String key, final String value, final long expireTime) {
        if (StringUtil.isEmpty(key)) {
            LOGGER.debug("setNx(), key is empty, return false.");
            return false;
        }

        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                //定义序列化方式
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] valueArray = serializer.serialize(value.toString());
                boolean flag = redisConnection.setNX(key.getBytes(), valueArray);
                // 设置超时时间
                redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
                return flag;
            }
        });
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        module = env.getProperty("spring.redis.module");
        ver = env.getProperty("spring.redis.ver");
        this.connect();
    }

    /**
     * 描述：连接信息
     *
     * @author mao2080@sina.com
     * @created 2017年3月30日 上午10:10:05
     */
    private void connect() {
        LOGGER.info(CommonUtil.getSystemLog("Redis服务正在启动"));
        LOGGER.info(CommonUtil.getSystemLog("Redis服务配置信息"));
        LOGGER.info(getProperty("spring.redis.module"));
        LOGGER.info(getProperty("spring.redis.ver"));
        LOGGER.info(getProperty("spring.redis.host"));
        LOGGER.info(getProperty("spring.redis.port"));
        LOGGER.info(CommonUtil.getSystemLog("Redis服务启动成功"));
        RedisCacheUtil.redisTemplate = this.stringRedisTemplate;
    }

    /**
     * 获取配置信息
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        if (key.contains("pass")) {
            return "*************************** " + key + ": ******";
        } else {
            return "*************************** " + key + ": " + env.getProperty(key);
        }
    }

    /**
     * @param key     key
     * @param value   当前值
     * @param timeout 超时时间
     * @Description: 初始化自增长值.
     */
    public void setIncr(String key, int value, long timeout) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.set(value);
        counter.expire(timeout, TimeUnit.SECONDS);
    }

    public static RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }
}
