package com.iot.boss.service.module.impl;

import com.google.common.collect.Lists;
import com.iot.boss.service.file.FileService;
import com.iot.boss.service.module.IServiceModuleService;
import com.iot.boss.util.ActionPropertyUtils;
import com.iot.boss.util.ModuleUtils;
import com.iot.boss.vo.FileResp;
import com.iot.boss.vo.module.BossActionInfoResp;
import com.iot.boss.vo.module.BossEventInfoResp;
import com.iot.boss.vo.module.BossPropertyInfoResp;
import com.iot.boss.vo.module.BossServiceModuleResp;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.device.api.*;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.vo.rsp.*;
import com.iot.util.AssertUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/10/26 15:40
 * 修改人： yeshiyuan
 * 修改时间：2018/10/26 15:40
 * 修改描述：
 */
@Service
public class ServiceModuleServiceImpl implements IServiceModuleService {

    private static Logger logger = LoggerFactory.getLogger(ServiceModuleServiceImpl.class);

    @Autowired
    private ServiceModuleApi serviceModuleApi;

    @Autowired
    private ServiceActionApi serviceActionApi;

    @Autowired
    private ServiceEventApi serviceEventApi;

    @Autowired
    private ServicePropertyApi servicePropertyApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private FileService fileService;


    /**
     * @despriction：查询产品对应的模组信息
     * @author  yeshiyuan
     * @created 2018/10/26 15:42
     * @return
     */
    @Override
    public List<BossServiceModuleResp> queryProductModule(Long productId) {
        ProductResp productResp = productApi.getProductById(productId);
        if (productResp == null) {
            throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
        }
        /*Long deviceTypeId = productResp.getDeviceTypeId();*/
        List<BossServiceModuleResp> respList = Lists.newArrayList();
        /*List<ServiceModuleListResp> deviceTypeModuleList = serviceModuleApi.findServiceModuleListByDeviceTypeId(deviceTypeId);
        if (CollectionUtils.isEmpty(deviceTypeModuleList)) {
            logger.debug("boss-未定义功能模组(deviceTypeId:{})", deviceTypeId);
            return respList;
        }*/

        List<ServiceModuleListResp> productModuleList = serviceModuleApi.findServiceModuleListByProductId(productId);
        /*for (ServiceModuleListResp deviceTypeModule : deviceTypeModuleList) {
            boolean whetherCheck = false;
            ServiceModuleListResp tempProductModule = null;
            for (ServiceModuleListResp productModule : productModuleList) {
                if (deviceTypeModule.getId().compareTo(productModule.getParentId()) != 0) {
                    continue;
                }
                whetherCheck = true;
                tempProductModule = productModule;
                break;
            }
            List<BossActionInfoResp> parentActionList = this.findActionListByServiceModuleId(deviceTypeModule.getId());
            List<BossEventInfoResp> parentEventList = this.findEventListByServiceModuleId(deviceTypeModule.getId());
            List<BossPropertyInfoResp> parentPropertyList = this.findPropertyInfoListByServiceModuleId(deviceTypeModule.getId());

            BossServiceModuleResp target = new BossServiceModuleResp();
            if (whetherCheck) {
                BeanUtils.copyProperties(tempProductModule, target);
                //功能组一样比较方法、事件、属性
                //1.1获取功能组方法的比较
                List<BossActionInfoResp> productActionList = this.findActionListByServiceModuleId(tempProductModule.getId());
                target.setActionList(ModuleUtils.parseActions(parentActionList, productActionList));
                //1.2 事件
                List<BossEventInfoResp> productEventList = this.findEventListByServiceModuleId(tempProductModule.getId());
                target.setEventList(ModuleUtils.parseEvents(parentEventList, productEventList));
                //1.3 属性
                List<BossPropertyInfoResp> productPropertyList = this.findPropertyInfoListByServiceModuleId(tempProductModule.getId());
                target.setPropertyList(ModuleUtils.parseProperties(parentPropertyList, productPropertyList));
                respList.add(target);
            }
        }*/
        if (!CollectionUtils.isEmpty(productModuleList)) {
            for (ServiceModuleListResp productModule : productModuleList) {
                BossServiceModuleResp target = new BossServiceModuleResp();
                List<BossActionInfoResp> parentActionList = this.findActionListByServiceModuleId(productModule.getId());
                List<BossEventInfoResp> parentEventList = this.findEventListByServiceModuleId(productModule.getId());
                List<BossPropertyInfoResp> parentPropertyList = this.findPropertyInfoListByServiceModuleId(productModule.getId());
                BeanUtils.copyProperties(productModule, target);
                target.setActionList(parentActionList);
                target.setEventList(parentEventList);
                target.setPropertyList(parentPropertyList);
                respList.add(target);
            }
        }
        return respList;
    }

    /**
      * @despriction：代码是拷贝portal的，有问题请对比portal的，可以找徐福周
      * @author  yeshiyuan
      * @created 2018/10/26 16:00
      * @return
      */
    public List<BossActionInfoResp> findActionListByServiceModuleId(Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        List<BossActionInfoResp> returnList = Lists.newArrayList();
        List<ServiceModuleActionResp> actionRespList =
                serviceActionApi.findActionListByServiceModuleId(serviceModuleId);
        if (CollectionUtils.isEmpty(actionRespList)) {
            return returnList;
        }
        for (ServiceModuleActionResp action : actionRespList) {
            BossActionInfoResp target = new BossActionInfoResp();
            Long actionId = action.getId();
            List<ServiceModulePropertyResp> propertyRespList = servicePropertyApi.findPropertyListByActionId(actionId);
            target.setActionInfo(action);
            target.setParamPropertyList(ActionPropertyUtils.parseParamProperties(propertyRespList)); // 请求参数
            target.setReturnPropertyList(ActionPropertyUtils.parseReturnProperties(propertyRespList)); // 返回参数
            returnList.add(target);
        }
        return returnList;
    }

    /**
      * @despriction：代码是拷贝portal的，有问题请对比portal的，可以找徐福周
      * @author  yeshiyuan
      * @created 2018/10/26 16:02
      * @return
      */
    public List<BossEventInfoResp> findEventListByServiceModuleId(Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        List<BossEventInfoResp> returnList = Lists.newArrayList();
        List<ServiceModuleEventResp> eventRespList = serviceEventApi.findEventListByServiceModuleId(serviceModuleId);
        if (CollectionUtils.isEmpty(eventRespList)) {
            return returnList;
        }
        for (ServiceModuleEventResp event : eventRespList) {
            BossEventInfoResp target = new BossEventInfoResp();
            Long eventId = event.getId();
            List<ServiceModulePropertyResp> propertyRespList = servicePropertyApi.findPropertyListByEventId(eventId);
            target.setEventInfo(event);
            target.setPropertyList(propertyRespList); // 参数
            returnList.add(target);
        }
        return returnList;
    }

    /**
     * @despriction：代码是拷贝portal的，有问题请对比portal的，可以找徐福周
     * @author  yeshiyuan
     * @created 2018/10/26 16:05
     * @return
     */
    public List<BossPropertyInfoResp> findPropertyInfoListByServiceModuleId(Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        List<BossPropertyInfoResp> infoRespList = Lists.newArrayList();
        List<ServiceModulePropertyResp> propertyRespList = findPropertyListByServiceModuleId(serviceModuleId);
        if (CollectionUtils.isEmpty(propertyRespList)) {
            return infoRespList;
        }
        for (ServiceModulePropertyResp property : propertyRespList) {
            BossPropertyInfoResp target = new BossPropertyInfoResp();
            BeanUtils.copyProperties(property, target);
            infoRespList.add(target);
        }
        return infoRespList;
    }

    /**
      * @despriction：代码是拷贝portal的，有问题请对比portal的，可以找徐福周
      * @author  yeshiyuan
      * @created 2018/10/26 16:05
      * @return
      */
    public List<ServiceModulePropertyResp> findPropertyListByServiceModuleId(Long serviceModuleId) {
        AssertUtils.notNull(serviceModuleId, "serviceModuleId.notnull");
        List<ServiceModulePropertyResp> propertyRespList = servicePropertyApi.findPropertyListByServiceModuleId(serviceModuleId);
        return propertyRespList;
    }

    /**
     * @Description: findServiceModuleListByProductId
     * 获取产品对应的功能组定义列表
     * @param productId
     * @return: java.util.List<com.iot.boss.vo.module.BossServiceModuleResp>
     * @author: chq
     * @date: 2018/11/7 18:32
     **/
    @Override
    public List<BossServiceModuleResp> findServiceModuleListByProductId(Long productId) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("search.getServiceModuleList");
        AssertUtils.notNull(productId, "productId.notnull");
        ProductResp productResp = productApi.getProductById(productId);
        if (productResp == null) {
            throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
        }
        Long deviceTypeId = productResp.getDeviceTypeId();
        List<BossServiceModuleResp> respList = Lists.newArrayList();
        List<ServiceModuleListResp> deviceTypeModuleList = serviceModuleApi.findServiceModuleListByDeviceTypeId(deviceTypeId);
        if (CollectionUtils.isEmpty(deviceTypeModuleList)) {
            logger.debug("boss-未定义功能模组(deviceTypeId:{})", deviceTypeId);
            return respList;
        }
        if (deviceTypeModuleList != null) {
            deviceTypeModuleList.forEach(m -> {
                try {
                    if (StringUtils.isNotBlank(m.getImg())) {
                        FileResp fileResp = fileService.getUrl(m.getImg());
                        m.setImg(fileResp.getUrl());
                    }
                    if (StringUtils.isNotBlank(m.getChangeImg())) {
                        FileResp fileResp = fileService.getUrl(m.getChangeImg());
                        m.setChangeImg(fileResp.getUrl());
                    }
                } catch (Exception e) {
                    logger.warn("serviceModule {} img error", m.getName(), e);
                }
            });
        }

        List<ServiceModuleListResp> productModuleList = serviceModuleApi.findServiceModuleListByProductId(productId);
        stopWatch.stop();

        if(CollectionUtils.isEmpty(productModuleList)){
            //产品对应的功能组为空
            for (ServiceModuleListResp source : deviceTypeModuleList) {
                BossServiceModuleResp target = new BossServiceModuleResp();
                BeanUtils.copyProperties(source, target);

                List<BossActionInfoResp> parentActionList = this.findActionListByServiceModuleId(source.getId());
                target.setActionList(parentActionList);

                List<BossEventInfoResp> parentEventList = this.findEventListByServiceModuleId(source.getId());
                target.setEventList(parentEventList);

                List<BossPropertyInfoResp> parentPropertyList = this.findPropertyInfoListByServiceModuleId(source.getId());
                target.setPropertyList(parentPropertyList);

                respList.add(target);
            }
            //获取所有的 boss 定义功能模组给产品
            return respList;
        }

        stopWatch.start("search.getActionByDeviceAndProduct");
        for (ServiceModuleListResp parentModule : deviceTypeModuleList) {
            boolean whetherCheck = false;
            ServiceModuleListResp tempProductModule = null;

            if (!CollectionUtils.isEmpty(productModuleList)) {
                for (ServiceModuleListResp productModule : productModuleList) {
                    if (parentModule.getId().compareTo(productModule.getParentId()) != 0) {
                        continue;
                    }
                    whetherCheck = true;
                    tempProductModule = productModule;
                    break;
                }
            }
            List<BossActionInfoResp> parentActionList = this.findActionListByServiceModuleId(parentModule.getId());
            List<BossEventInfoResp> parentEventList = this.findEventListByServiceModuleId(parentModule.getId());
            List<BossPropertyInfoResp> parentPropertyList = this.findPropertyInfoListByServiceModuleId(parentModule.getId());

            BossServiceModuleResp target = new BossServiceModuleResp();
            if (whetherCheck) {
                BeanUtils.copyProperties(tempProductModule, target);
                //功能组一样比较方法、事件、属性
                //1.1获取功能组方法的比较
                List<BossActionInfoResp> productActionList = this.findActionListByServiceModuleId(tempProductModule.getId());
                target.setActionList(ModuleUtils.parseActions(parentActionList, productActionList));
                //1.2 事件
                List<BossEventInfoResp> productEventList = this.findEventListByServiceModuleId(tempProductModule.getId());
                target.setEventList(ModuleUtils.parseEvents(parentEventList, productEventList));
                //1.3 属性
                List<BossPropertyInfoResp> productPropertyList = this.findPropertyInfoListByServiceModuleId(tempProductModule.getId());
                target.setPropertyList(ModuleUtils.parseProperties(parentPropertyList, productPropertyList));
            } else {
                BeanUtils.copyProperties(parentModule, target);
                target.setActionList(parentActionList);
                target.setEventList(parentEventList);
                target.setPropertyList(parentPropertyList);
            }
            //聚合两者是否被选中
            target.setWhetherCheck(whetherCheck);
            respList.add(target);
        }
        stopWatch.stop();
        logger.info("[耗时：]" + stopWatch.prettyPrint());
        return respList;
    }
}
