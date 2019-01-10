package com.iot.service.Impl;

import com.iot.BusinessExceptionEnum;
import com.iot.common.exception.BusinessException;
import com.iot.domain.SpaceDate;
import com.iot.domain.SpaceDateVO;
import com.iot.domain.SpaceTemplate;
import com.iot.mapper.SpaceDateMapper;
import com.iot.mapper.UserSpaceSubscribeMapper;
import com.iot.mapper.UserSpaceUsedMapper;
import com.iot.service.SpaceDateService;
import com.iot.service.UserSpaceService;
import com.iot.util.ToolUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service
public class UserSpaceServiceImpl implements UserSpaceService {

    @Autowired
    UserSpaceSubscribeMapper userSpaceSubscribeMapper;

    @Autowired
    UserSpaceUsedMapper userSpaceUsedMapper;

    @Autowired
    SpaceDateService spaceDateService;

    @Autowired
    SpaceDateMapper spaceDateMapper;

    @Override
    public Map<String, Object> findUserSubscribeByUserId(String userId) throws BusinessException {
        List<SpaceDate> spaceDateList = new ArrayList<SpaceDate>();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (!userId.isEmpty()) {
            spaceDateList = userSpaceSubscribeMapper.findSpaceDateByUserId(userId);
            for (SpaceDate spaceDate : spaceDateList) {
                List<SpaceDate> selectspaceDateList = new ArrayList<SpaceDate>();
                if (!resultMap.containsKey(spaceDate.getSpaceId())) {
                    selectspaceDateList.add(spaceDate);
                    resultMap.put(spaceDate.getSpaceId(), selectspaceDateList);
                } else {
                    selectspaceDateList = (List<SpaceDate>) resultMap.get(spaceDate.getSpaceId());
                    selectspaceDateList.add(spaceDate);
                    resultMap.replace(spaceDate.getSpaceId(), selectspaceDateList);
                }
            }
        }
        return resultMap;
    }

    public int deleteUserSubscribe(Map<String, Object> map) {
        int result = 0;
        try {
            if (map.get("userId") != null && map.get("spaceId") != null && map.get("settingDate") != null
                    && map.get("beginTime") != null && map.get("endTime") != null && map.get("tenantId") != null) {
                result = userSpaceSubscribeMapper.deleteUserSubscribe(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);
        }

        return result;

    }

    public int saveUserSubscribe(Map<String, Object> map) throws BusinessException {
        int result = 0;
        if (map.get("userId") != null && map.get("spaceId") != null && map.get("settingDate") != null
                && map.get("beginTime") != null && map.get("endTime") != null && map.get("tenantId") != null) {
            List<String> spaceDateIdList = new ArrayList<String>();
            spaceDateIdList = userSpaceSubscribeMapper.findSpaceDateId(map);

            if (CollectionUtils.isNotEmpty(spaceDateIdList)) {
                for (String spaceDateId : spaceDateIdList) {
                    String userSubscribeId = ToolUtils.getUUID();
                    map.put("userSubscribeId", userSubscribeId);
                    map.put("spaceDateId", spaceDateId);
                    result = userSpaceSubscribeMapper.saveUserSubscribe(map);
                }
            }
        } else {
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);
        }
        return result;
    }

    public int saveUserUsed(Map<String, Object> map) throws BusinessException {
        int result = 0;
        if (map.get("userId") != null && map.get("spaceId") != null && map.get("settingDate") != null
                && map.get("beginTime") != null && map.get("endTime") != null && map.get("tenantId") != null) {
            List<String> spaceDateIdList = new ArrayList<String>();
            spaceDateIdList = userSpaceSubscribeMapper.findSpaceDateId(map);
            String userUsedId = ToolUtils.getUUID();
            if (CollectionUtils.isNotEmpty(spaceDateIdList)) {
                for (String spaceId : spaceDateIdList) {
                    map.put("spaceDateId", spaceId);
                    map.put("userUsedId", userUsedId);
                    result = userSpaceUsedMapper.saveUserUsed(map);
                }
            }
        } else {
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);
        }
        return result;
    }

    @Override
    public Map<String, Object> findBookableSpaceDateByTenantId(String tenantId) throws ParseException {
        List<SpaceTemplate> spaceTemplateList = new ArrayList<SpaceTemplate>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<SpaceDate> spaceDateList = new ArrayList<SpaceDate>();
        List<SpaceDate> tomorrowSpaceDateList = new ArrayList<SpaceDate>();
        if (!tenantId.isEmpty()) {
            // 约束显示时间
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat strFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            spaceTemplateList = userSpaceSubscribeMapper.findBookableSpaceDateByTenantId(tenantId);
            Date date = new Date();
            String dateStr = dateFormat.format(date);
            date = dateFormat.parse(dateStr);

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
            Date tomorrow = calendar.getTime();
            String tomorrowStr = dateFormat.format(tomorrow);

            spaceDateList = changeSpaceDate(spaceTemplateList, strFormat, timeFormat, date, dateStr);
            tomorrowSpaceDateList = changeSpaceDate(spaceTemplateList, strFormat, timeFormat, tomorrow, tomorrowStr);
            spaceDateList.addAll(tomorrowSpaceDateList);

            // 添加分组
            for (SpaceDate spaceDate : spaceDateList) {
                // 上午antemeridiem
                String antemeridiemStr = dateFormat.format(spaceDate.getSettingDate()) + " 12:00:00";
                Date antemeridiem = strFormat.parse(antemeridiemStr);

                // 下午afternoon
                String afternoonStr = dateFormat.format(spaceDate.getSettingDate()) + " 18:00:00";
                Date afternoon = strFormat.parse(afternoonStr);
                // 今天today
                String todayStr = dateFormat.format(spaceDate.getSettingDate());
                Date istoday = dateFormat.parse(todayStr);

                if (istoday.equals(date)) {
                    matchTime(resultMap, date, spaceDate, antemeridiem, afternoon);
                } else if (istoday.equals(tomorrow)) {
                    matchTime(resultMap, tomorrow, spaceDate, antemeridiem, afternoon);
                }
            }
        }
        return resultMap;
    }

    private List<SpaceDate> changeSpaceDate(List<SpaceTemplate> spaceTemplateList, SimpleDateFormat strFormat,
                                            SimpleDateFormat timeFormat, Date date, String dateStr) throws ParseException {
        List<SpaceDate> spaceDateList = new ArrayList<>();

        for (SpaceTemplate spaceTemplate : spaceTemplateList) {
            SpaceDate spaceDate = new SpaceDate();
            BeanUtils.copyProperties(spaceTemplate, spaceDate);

            String beginTimeStr = dateStr + " " + timeFormat.format(spaceDate.getBeginTime());
            Date beginTime = strFormat.parse(beginTimeStr);
            spaceDate.setBeginTime(beginTime);

            String endTimeStr = dateStr + " " + timeFormat.format(spaceDate.getEndTime());
            Date endTime = strFormat.parse(endTimeStr);
            spaceDate.setEndTime(endTime);
            spaceDate.setSettingDate(date);

            if (beginTime.after(new Date())) {
                spaceDateList.add(spaceDate);
            }
        }
        return spaceDateList;
    }

    private Map<String, Object> matchTime(Map<String, Object> resultMap, Date date, SpaceDate spaceDate,
                                          Date antemeridiem, Date afternoon) {
        List<SpaceDate> dateList = new ArrayList<SpaceDate>();
        if (spaceDate.getBeginTime().before(antemeridiem)) {
            if (!resultMap.containsKey("antemeridiem " + date)) {
                dateList.add(spaceDate);
                resultMap.put("antemeridiem " + date, dateList);
            } else {
                dateList = (List<SpaceDate>) resultMap.get("antemeridiem " + date);
                dateList.add(spaceDate);
                resultMap.replace("antemeridiem " + date, dateList);
            }
        } else if (spaceDate.getBeginTime().before(afternoon)) {
            if (!resultMap.containsKey("afternoon " + date)) {
                dateList.add(spaceDate);
                resultMap.put("afternoon " + date, dateList);
            } else {
                dateList = (List<SpaceDate>) resultMap.get("afternoon " + date);
                dateList.add(spaceDate);
                resultMap.replace("afternoon " + date, dateList);
            }
        } else {
            if (!resultMap.containsKey("night " + date)) {
                dateList.add(spaceDate);
                resultMap.put("night " + date, dateList);
            } else {
                dateList = (List<SpaceDate>) resultMap.get("night " + date);
                dateList.add(spaceDate);
                resultMap.replace("night " + date, dateList);
            }
        }
        return resultMap;
    }

    /**
     * 通过用户时间表找教室list
     *
     * @param DateMapList [{userId:"",settingDate:"",beginTime:"",endTime:"",tenantId:""}]
     * @return
     * @throws ParseException
     */
    public List<SpaceDate> findSpaceByDate(List<Map<String, Object>> DateMapList) throws ParseException {
        List<SpaceDate> spaceDateList = new ArrayList<SpaceDate>();
        Map<String, Object> param = new HashMap<String, Object>();
        List<SpaceDate> AllSpaceDateList = new ArrayList<SpaceDate>();
        Map<String, Object> map = new HashMap<String, Object>();

        for (Map<String, Object> dateMap : DateMapList) {

            String settingDate = ToolUtils.stampToDate(Long.valueOf(dateMap.get("settingDate").toString()));
            param.put("settingDate", settingDate);
            param.put("tenantId", dateMap.get("tenantId"));
            spaceDateList = spaceDateMapper.findSpaceDateByDate(param);// 只需要教室和时间
            if (CollectionUtils.isNotEmpty(spaceDateList)) {
                for (SpaceDate spaceDate : spaceDateList) {
                    String key = spaceDate.getSpaceId() + spaceDate.getSettingDate();
                    if (!map.containsKey(key)) {
                        map.put(key, spaceDate);
                        AllSpaceDateList.add(spaceDate);
                    }
                }
            }
        }

        return AllSpaceDateList;

    }

    /**
     * 根据预约时间分配空间
     *
     * @param DateMapList MapList[{userId:"",date:"",beginTime:"",endTime:"",tenantId:""}]
     * @return
     * @throws ParseException
     */
    public List<SpaceDate> distributeSpaceBySubscribeTime(List<Map<String, Object>> DateMapList, List<SpaceDate> ResultspaceDateList) throws ParseException {
        Map<String, Object> spaceMap = new HashMap<String, Object>();
        List<SpaceDate> spaceDateList = new ArrayList<SpaceDate>();
        List<SpaceDateVO> DateVOList = new ArrayList<SpaceDateVO>();
        List<SpaceDate> DateList = new ArrayList<SpaceDate>();

        spaceDateList = findSpaceByDate(DateMapList);// 通过用户时间表找教室list

        for (SpaceDate space : spaceDateList) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (space != null)
                try {
                    Long settingDate = space.getSettingDate().getTime();
                    map.put("spaceId", space.getSpaceId());
                    map.put("settingDate", settingDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            DateList = spaceDateService.findSpaceDateBySpaceIdAndDate(map);// 教室1可用时间List

            if (spaceMap.containsKey(space.getSpaceId())) {
                List<SpaceDate> addDateList = (List<SpaceDate>) spaceMap.get(space.getSpaceId());
                addDateList.addAll(DateList);
                spaceMap.put(space.getSpaceId(), addDateList);
            } else {
                spaceMap.put(space.getSpaceId(), DateList);// 所有教室及其可用的时间[spaceDateVO1,spaceDateVO2]
            }

        }
        Map<String, Object> spaceMatchMap = new HashMap<String, Object>();

        for (Map.Entry<String, Object> entry : spaceMap.entrySet()) {// 用教室的时间点去匹配用户的时间，遍历所有教室
            List<SpaceDate> spaceDateVOList = (List<SpaceDate>) entry.getValue();
            String spaceId = entry.getKey();
            List<SpaceDate> spaceMatchDateList = new ArrayList<SpaceDate>();
            // 查找space可以匹配用户预约的时间列表
            spaceMatchDateList = findSpaceMatchDateList(DateMapList, spaceDateVOList);
            if (CollectionUtils.isNotEmpty(spaceMatchDateList)) {
                spaceMatchMap.put(spaceId, spaceMatchDateList);// 教室及其匹配时间表
            }

        }

        Map<String, Object> resultspaceMatchMap = sortMapByValue(spaceMatchMap);// 排序后的map

        // TODO 添加往人多的地方进行分配的原则；
        if (resultspaceMatchMap != null) {
            // 获取map第一个值{"spaceId",list<SpaceDate>}
            for (Map.Entry<String, Object> entry : resultspaceMatchMap.entrySet()) {
                if (CollectionUtils.isNotEmpty(ResultspaceDateList)) {
                    ResultspaceDateList.addAll((List<SpaceDate>) entry.getValue());
                } else {
                    ResultspaceDateList = (List<SpaceDate>) entry.getValue();
                }
                String spaceId = entry.getKey();
                // 判断取出的value值是否满足客户需求
                if (ResultspaceDateList.size() < DateMapList.size()) {
                    List<Map<String, Object>> UserDateMapList = new ArrayList<>();
                    UserDateMapList = deleteSpaceMatchDateList(DateMapList, ResultspaceDateList);
                    distributeSpaceBySubscribeTime(UserDateMapList, ResultspaceDateList);
                }
                break;
            }
        }
        return ResultspaceDateList;
    }

    /**
     * 查找space可以匹配用户预约的时间列表
     *
     * @param DateMapList
     * @param spaceDateList
     * @param isAdd
     * @param UserDateMapList 用户时间列表
     * @return
     */
    private List<SpaceDate> findSpaceMatchDateList(List<Map<String, Object>> DateMapList, List<SpaceDate> spaceDateList) {
        List<SpaceDate> spaceMatchDateList = new ArrayList<SpaceDate>();
        Iterator<Map<String, Object>> iterator = DateMapList.iterator();
//		Iterator<Map<String, Object>> iterator = DateMapList.iterator();
        for (SpaceDate spaceDate : spaceDateList) {// 遍历教室1时间列表[spaceDateVO1,spaceDateVO2]
            while (iterator.hasNext()) {
                Map<String, Object> dateMap = iterator.next();
                SimpleDateFormat strFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {

                    Date settingDate = ToolUtils.timestampToDate((String) dateMap.get("settingDate"));
                    Date beginTime = ToolUtils.timestampToDate((String) dateMap.get("beginTime"));
                    Date endTime = ToolUtils.timestampToDate((String) dateMap.get("endTime"));

//					Date settingDate = strFormat.parse(dateMap.get("settingDate").toString());
//					Date beginTime = strFormat.parse((String) dateMap.get("beginTime"));
//					Date endTime = strFormat.parse((String) dateMap.get("endTime"));
                    if (settingDate.equals(spaceDate.getSettingDate()) && beginTime.equals(spaceDate.getBeginTime())
                            && endTime.equals(spaceDate.getEndTime())) {
                        spaceMatchDateList.add(spaceDate);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);
                }

            }
        }
        return spaceMatchDateList;
    }

    private List<Map<String, Object>> deleteSpaceMatchDateList(List<Map<String, Object>> DateMapList, List<SpaceDate> spaceDateList) {
        List<Map<String, Object>> UserDateMapList = DateMapList;
        Iterator<Map<String, Object>> iterator = UserDateMapList.iterator();

        for (SpaceDate spaceDate : spaceDateList) {// 遍历教室1时间列表[spaceDateVO1,spaceDateVO2]
            while (iterator.hasNext()) {
                Map<String, Object> dateMap = iterator.next();

                try {

                    Date settingDate = ToolUtils.timestampToDate((String) dateMap.get("settingDate"));
                    Date beginTime = ToolUtils.timestampToDate((String) dateMap.get("beginTime"));
                    Date endTime = ToolUtils.timestampToDate((String) dateMap.get("endTime"));

                    if (settingDate.equals(spaceDate.getSettingDate()) && beginTime.equals(spaceDate.getBeginTime())
                            && endTime.equals(spaceDate.getEndTime())) {

                        iterator.remove();

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);
                }

            }
        }
        return UserDateMapList;
    }


    /**
     * 使用 Map按value进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByValue(Map<String, Object> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Object> sortedMap = new LinkedHashMap<String, Object>();
        // 将map<String,Object> 转化为ArryList,但list里面的元素为Entry<String,Object>
        List<Map.Entry<String, Object>> entryList = new ArrayList<Map.Entry<String, Object>>(oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());

        Iterator<Map.Entry<String, Object>> iter = entryList.iterator();
        Map.Entry<String, Object> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }
}

// comparator比较器接口
class MapValueComparator implements Comparator<Map.Entry<String, Object>> {

    @Override
    public int compare(Entry<String, Object> temp1, Entry<String, Object> temp2) {
        List<SpaceDate> spaceDateList1 = (ArrayList<SpaceDate>) temp1.getValue();
        List<SpaceDate> spaceDateList2 = (ArrayList<SpaceDate>) temp2.getValue();
        Integer it1 = new Integer(spaceDateList1.size());
        Integer it2 = new Integer(spaceDateList2.size());
        return it2.compareTo(it1);
    }
}
