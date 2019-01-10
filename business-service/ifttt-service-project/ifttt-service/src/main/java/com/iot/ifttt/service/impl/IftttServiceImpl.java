package com.iot.ifttt.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.ifttt.channel.base.*;
import com.iot.ifttt.common.IftttConstants;
import com.iot.ifttt.common.IftttServiceEnum;
import com.iot.ifttt.common.IftttStatusEnum;
import com.iot.ifttt.common.IftttTypeEnum;
import com.iot.ifttt.entity.Applet;
import com.iot.ifttt.entity.AppletItem;
import com.iot.ifttt.entity.AppletThat;
import com.iot.ifttt.entity.AppletThis;
import com.iot.ifttt.service.*;
import com.iot.ifttt.util.RedisKeyUtil;
import com.iot.ifttt.vo.*;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class IftttServiceImpl implements IIftttService {
    private final Logger logger = LoggerFactory.getLogger(IftttServiceImpl.class);

    @Autowired
    private IAppletService appletService;
    @Autowired
    private IAppletThisService appletThisService;
    @Autowired
    private IAppletThatService appletThatService;
    @Autowired
    private IAppletItemService appletItemService;
    @Autowired
    private Adapter adapter;


    /**
     * 触发检测
     * <p>
     * 1、dev 设备状态触发 && 时间范围内
     * 2、timer 定时执行
     *
     * @param req
     */
    public void check(CheckAppletReq req) {
        //类型
        IftttTypeEnum type = IftttTypeEnum.getEnum(req.getType());
        String msg = req.getMsg();
        //获取关联applet
        List<Long> appletIds = adapter.getApplet(type, msg);
        //校验
        if (CollectionUtils.isEmpty(appletIds)) {
            //校验结束
            logger.debug("没有关联程序，校验结束！");
            return;
        }

        //循环校验
        for (Long appletId : appletIds) {
            //校验
            //1、校验状态
            String status = appletService.getStatus(appletId);
            if (IftttStatusEnum.OFF.getCode().equals(status)) {
                //禁用状态，不校验，直接跳过
                logger.debug("程序【" + appletId + "】为禁用状态，不执行!");
                continue;
            }
            //2、分服务校验
            List<AppletThis> thisList = appletThisService.getByAppletId(appletId);
            Boolean flag = true;
            if (CollectionUtils.isNotEmpty(thisList)) {
                for (AppletThis thisVo : thisList) {
                    //调用校验器校验
                    List<AppletItem> thisItems = appletItemService.getByEventId(thisVo.getId(), IftttConstants.IFTTT_TYPE_THIS);
                    //获取服务code
                    ICalculator iCalculator = Adapter.getCalculator(IftttServiceEnum.getEnum(thisVo.getServiceCode()));
                    if (iCalculator != null) {
                        CheckVo vo = new CheckVo();
                        vo.setLogic(thisVo.getLogic());
                        vo.setItems(thisItems);
                        vo.setMsg(msg);
                        Boolean flag1 = iCalculator.check(vo);
                        flag = flag && flag1; //this之间 默认and关系
                    }
                }
            } else {
                flag = false;
            }


            //执行
            if (flag) {
                logger.debug("程序【" + appletId + "】达到执行条件，执行!");
                //取出that
                List<AppletThat> thatList = appletThatService.getByAppletId(appletId);
                if (CollectionUtils.isNotEmpty(thatList)) {
                    for (AppletThat thatVo : thatList) {
                        //调用校验器校验
                        List<AppletItem> thatItems = appletItemService.getByEventId(thatVo.getId(), IftttConstants.IFTTT_TYPE_THAT);
                        //获取服务code
                        IExecutor iExecutor = Adapter.getExecutor(IftttServiceEnum.getEnum(thatVo.getServiceCode()));
                        iExecutor.execute(thatItems);
                    }
                }
            } else {
                logger.debug("程序【" + appletId + "】未达到执行条件，不执行!");
            }
        }
    }

    /**
     * 保存规则
     *
     * @param req
     * @return
     */
    public Long save(AppletVo req) {
        //保存applet
        Applet applet = new Applet();
        BeanUtils.copyProperties(req, applet);

        if (req.getId() != null) {
            //横向越权检测
            checkOverPower(req.getId());

            //修改 先删除再新增
            delRule(req.getId());
            appletService.update(applet);
        } else {
            appletService.insert(applet);
        }

        Long appletId = applet.getId();
        List<AppletItem> appletItems = Lists.newArrayList();
        //保存this
        List<AppletThisVo> thisVoList = req.getThisList();
        if (CollectionUtils.isNotEmpty(thisVoList)) {
            for (AppletThisVo vo : thisVoList) {
                AppletThis appletThis = new AppletThis();
                BeanUtils.copyProperties(vo, appletThis);
                appletThis.setAppletId(appletId);
                appletThisService.insert(appletThis);

                Long thisId = appletThis.getId();
                if (CollectionUtils.isNotEmpty(vo.getItems())) {
                    for (AppletItemVo itemVo : vo.getItems()) {
                        AppletItem item = new AppletItem();
                        BeanUtils.copyProperties(itemVo, item);
                        item.setEventId(thisId);
                        item.setAppletId(appletId);
                        item.setType(IftttConstants.IFTTT_TYPE_THIS);
                        //先保存条件
                        appletItemService.insert(item);
                        //新增逻辑
                        ILogic iLogic = Adapter.getLogic(IftttServiceEnum.getEnum(vo.getServiceCode()));
                        if (iLogic != null) {
                            iLogic.add(item);
                        }
                    }
                }
            }
        }

        //保存that
        List<AppletThatVo> thatVoList = req.getThatList();
        if (CollectionUtils.isNotEmpty(thatVoList)) {
            for (AppletThatVo vo : thatVoList) {
                AppletThat appletThat = new AppletThat();
                BeanUtils.copyProperties(vo, appletThat);
                appletThat.setAppletId(appletId);
                appletThatService.insert(appletThat);

                Long thatId = appletThat.getId();
                if (CollectionUtils.isNotEmpty(vo.getItems())) {
                    for (AppletItemVo itemVo : vo.getItems()) {
                        AppletItem item = new AppletItem();
                        BeanUtils.copyProperties(itemVo, item);
                        item.setEventId(thatId);
                        item.setAppletId(appletId);
                        item.setType(IftttConstants.IFTTT_TYPE_THAT);
                        appletItems.add(item);
                    }
                }
            }
        }

        //保存items
        if (CollectionUtils.isNotEmpty(appletItems)) {
            //批量新增
            appletItemService.insertBatch(appletItems);
        }

        return applet.getId();
    }

    /**
     * 设置使能
     *
     * @param req
     */
    public void setEnable(SetEnableReq req) {
        Applet applet = new Applet();
        applet.setId(req.getId());
        applet.setStatus(req.getStatus());
        appletService.updateById(applet);

        //删除rule缓存
        RedisCacheUtil.delete(RedisKeyUtil.getAppletRuleKey(req.getId()));
        //删除status缓存
        RedisCacheUtil.delete(RedisKeyUtil.getAppletStatusKey(req.getId()));
    }


    /**
     * 获取规则
     *
     * @param id
     * @return
     */
    public AppletVo get(Long id) {
        String key = RedisKeyUtil.getAppletRuleKey(id);
        AppletVo appletVo = RedisCacheUtil.valueObjGet(key, AppletVo.class);

        //缓存空
        if (appletVo == null) {
            appletVo = getData(id);

            //赋空值
            if (appletVo == null) {
                appletVo = new AppletVo();
                appletVo.setId(-1l);
            }
            //存缓存
            RedisCacheUtil.valueObjSet(key, appletVo, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_7);
        }

        //真实空
        if (appletVo.getId() == -1) {
            appletVo = null;
        }
        return appletVo;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public Boolean delete(Long id) {
        //横向越权检测
        checkOverPower(id);

        //applet
        appletService.delete(id);

        //删除规则
        delRule(id);
        return true;
    }

    @Override
    public void delItem(Long itemId) {
        appletItemService.deleteByItemId(itemId);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 从数据库中获取
     *
     * @param id
     * @return
     */
    private AppletVo getData(Long id) {
        AppletVo appletVo = new AppletVo();
        //applet
        Applet applet = appletService.selectById(id);
        if (applet == null) {
            return null;
        }
        BeanUtils.copyProperties(applet, appletVo);

        //this
        List<AppletThisVo> thisVos = Lists.newArrayList();
        List<AppletThis> thisList = appletThisService.getByAppletId(id);
        if (CollectionUtils.isNotEmpty(thisList)) {
            for (AppletThis vo : thisList) {
                AppletThisVo appletThisVo = new AppletThisVo();
                BeanUtils.copyProperties(vo, appletThisVo);

                //item
                List<AppletItem> items = appletItemService.getByEventId(vo.getId(), IftttConstants.IFTTT_TYPE_THIS);
                List<AppletItemVo> appletItemVos = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(items)) {
                    for (AppletItem item : items) {
                        AppletItemVo itemVo = new AppletItemVo();
                        BeanUtils.copyProperties(item, itemVo);
                        appletItemVos.add(itemVo);
                    }
                }

                appletThisVo.setItems(appletItemVos);
                thisVos.add(appletThisVo);
            }
        }
        appletVo.setThisList(thisVos);

        //that
        List<AppletThatVo> thatVos = Lists.newArrayList();
        List<AppletThat> thatList = appletThatService.getByAppletId(id);
        if (CollectionUtils.isNotEmpty(thatList)) {
            for (AppletThat vo : thatList) {
                AppletThatVo appletThatVo = new AppletThatVo();
                BeanUtils.copyProperties(vo, appletThatVo);

                //item
                List<AppletItem> items = appletItemService.getByEventId(vo.getId(), IftttConstants.IFTTT_TYPE_THAT);
                List<AppletItemVo> appletItemVos = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(items)) {
                    for (AppletItem item : items) {
                        AppletItemVo itemVo = new AppletItemVo();
                        BeanUtils.copyProperties(item, itemVo);
                        appletItemVos.add(itemVo);
                    }
                }
                appletThatVo.setItems(appletItemVos);
                thatVos.add(appletThatVo);
            }
        }
        appletVo.setThatList(thatVos);

        return appletVo;
    }

    /**
     * 删除规则
     *
     * @param id
     */
    private void delRule(Long id) {
        List<AppletItem> itemList = Lists.newArrayList();
        //删除this
        List<AppletThis> thisList = appletThisService.getByAppletIdFromDB(id);
        if (CollectionUtils.isNotEmpty(thisList)) {
            for (AppletThis vo : thisList) {
                Long thisId = vo.getId();
                List<AppletItem> items = appletItemService.getByEventId(vo.getId(), IftttConstants.IFTTT_TYPE_THIS);
                String code = vo.getServiceCode();
                List<String> delRelationKeys = Lists.newArrayList();
                //删除逻辑
                if (CollectionUtils.isNotEmpty(items)) {
                    itemList.addAll(items);
                    for (AppletItem item : items) {
                        ILogic iLogic = Adapter.getLogic(IftttServiceEnum.getEnum(vo.getServiceCode()));
                        if (iLogic != null) {
                            iLogic.delete(item);
                        }

                        //设备和appletId的关系缓存key
                        if (code.equals(IftttServiceEnum.DEV_STATUS.getCode())) {
                            String json = item.getJson();
                            Map<String, Object> map = JSON.parseObject(json, Map.class);
                            String devId = String.valueOf(map.get("devId"));
                            if (!StringUtils.isEmpty(devId)) {
                                String key = RedisKeyUtil.getAppletRelationKey(IftttTypeEnum.DEV_STATUS.getType(), devId);
                                delRelationKeys.add(key);
                            }
                        }
                    }
                }

                //批量删除设备和appletId的关系缓存
                if (CollectionUtils.isNotEmpty(delRelationKeys)) {
                    RedisCacheUtil.delete(delRelationKeys);
                }
                appletItemService.deleteByEventId(thisId, IftttConstants.IFTTT_TYPE_THIS);
            }
        }
        appletThisService.deleteByAppletId(id);

        //删除that
        List<AppletThat> thatList = appletThatService.getByAppletIdFromDB(id);
        if (CollectionUtils.isNotEmpty(thatList)) {
            for (AppletThat vo : thatList) {
                List<AppletItem> items = appletItemService.getByEventId(vo.getId(), IftttConstants.IFTTT_TYPE_THAT);
                if (CollectionUtils.isNotEmpty(items)) {
                    itemList.addAll(items);
                }

                appletItemService.deleteByEventId(vo.getId(), IftttConstants.IFTTT_TYPE_THAT);
            }
        }
        appletThatService.deleteByAppletId(id);

        //删除缓存
        RedisCacheUtil.delete(RedisKeyUtil.getAppletRuleKey(id));

        //删除item缓存
        if (CollectionUtils.isNotEmpty(itemList)) {
            List<String> keys = Lists.newArrayList();
            for (AppletItem vo : itemList) {
                String key = RedisKeyUtil.getAppletItemKey(vo.getId());
                keys.add(key);
            }
            RedisCacheUtil.delete(keys);
        }
    }

    /**
     * 横向越权检测
     *
     * @param id
     */
    private void checkOverPower(Long id) {
        //横向越权处理
        Long userId = SaaSContextHolder.getCurrentUserId();
        Applet entity = appletService.selectById(id);
        Long realUserId = entity.getCreateBy();
        if (realUserId == null || userId == null || userId.longValue() != realUserId.longValue()) {
            logger.warn("update applet error, lateral ultra vires!");
            //throw new BusinessException(IftttExceptionEnum.LATERAL_ULTRA_VIRES);
        }
    }
}
