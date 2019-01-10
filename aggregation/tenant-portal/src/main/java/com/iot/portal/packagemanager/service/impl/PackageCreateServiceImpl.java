package com.iot.portal.packagemanager.service.impl;

import com.alibaba.fastjson.JSON;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.JsonUtil;
import com.iot.control.packagemanager.api.*;
import com.iot.control.packagemanager.enums.AttrTypeEnum;
import com.iot.control.packagemanager.enums.PackageTypeEnum;
import com.iot.control.packagemanager.enums.SecurityDeviceTypeEnum;
import com.iot.control.packagemanager.utils.CheckPropertyUtil;
import com.iot.control.packagemanager.vo.req.*;
import com.iot.control.packagemanager.vo.req.rule.*;
import com.iot.control.packagemanager.vo.req.scene.SceneConfigReq;
import com.iot.control.packagemanager.vo.req.scene.SceneDetailInfoReq;
import com.iot.control.packagemanager.vo.resp.PackageResp;
import com.iot.control.packagemanager.vo.resp.SceneInfoResp;
import com.iot.control.packagemanager.vo.resp.TemplateRuleResp;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.api.ProductApi;
import com.iot.device.api.ProductToServiceModuleApi;
import com.iot.device.enums.ModuleIftttTypeEnum;
import com.iot.device.enums.product.ProductAuditStatusEnum;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.servicemodule.ActionResp;
import com.iot.device.vo.rsp.servicemodule.EventResp;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import com.iot.device.vo.rsp.servicemodule.PropertyResp;
import com.iot.payment.enums.goods.TechnicalSchemeEnum;
import com.iot.portal.exception.BusinessExceptionEnum;
import com.iot.portal.exception.PackageExceptionEnum;
import com.iot.portal.packagemanager.service.IPackageCreateService;
import com.iot.portal.packagemanager.vo.req.CreatePackageReq;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: cloud
 * @description: 套包创建service
 * @author: yeshiyuan
 * @create: 2018-12-10 19:46
 **/
@Service
public class PackageCreateServiceImpl implements IPackageCreateService{

    private static Logger logger = LoggerFactory.getLogger(PackageCreateServiceImpl.class);

    @Autowired
    private PackageProductApi packageProductApi;

    @Autowired
    private ProductToServiceModuleApi productToServiceModuleApi;

    @Autowired
    private PackageApi packageApi;

    @Autowired
    private PackageDeviceTypeApi packageDeviceTypeApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private DeviceTypeApi deviceTypeApi;

    @Autowired
    private TemplateRuleApi templateRuleApi;

    @Autowired
    private SceneInfoApi sceneInfoApi;


    /**
     *  1、添加套包前校验：
     *   1）创建套包必须选择产品,而且产品必须是审核通过;
     *   2）必须且只能选择一个网关产品（技术方案为1）；
     *   3）选择的产品的设备类型必能得支持if/then/ifthen
     *   4）产品只能被一个套包关联，不能重复关联至其他套包
     *  2、保存套包基本信息，获得新的套包id;
     *  3、如果选择的是boss创建好的套包模板，需要做以下校验：
     *   3.1 模板套包校验
     *     1）套包是否存在；
     *     2）选中的产品是否满足套包的设备类型；
     *   3.2 如果套包不是安防套包类型，需复制场景
     *     1）如果套包不是安防套包类型，需先加载套包模板的场景；
     *     2）根据场景详情中设备类型的方法、属性，对比产品的支持then/ifthen属性、方法，如果有对不上（是否含有对应的方法、属性；是否值越界）的则抛出异常；
     *     3）校验通过，则把场景存进数据库；
     *   3.3 策略校验、复制
     *     1）加载套包模板的所有策略
     *     2）校验策略中if配置
     *       a.校验设备类型的属性、事件，对比产品的支持if/ifthen属性、事件，如果有对不上（是否含有对应的事件、属性；值是否越界）的则抛出异常，否则根据根据产品的功能组，组装成新的数据；
     *       b.校验第三方接入api;
     *     3）校验策略中then配置
     *       a.校验设备类型的方法、属性，对比产品的支持then/ifthen属性、方法，如果有对不上（是否含有对应的方法、属性；值是否越界）的则抛出异常，否则根据根据产品的功能组，组装成新的数据；
     *       b.根据模板场景id，找到刚刚存进去的场景id并替换；
     *       c.直接复制执行配置
     *     4）数据组装后，把策略存进数据库；
     *  4、如果是自定义套包，需要做以下校验：
     *    1）如果是安防套包类型，则选中的产品必须至少包含keypad、keyfor设备类型的其中一种
     *  5、保存套包选中的产品
     *  注意：如果创建失败，数据将回滚，删除对应的数据，再抛出异常
     */
    /**
     * @despriction：保存套包基础信息
     * @author  yeshiyuan
     * @created 2018/11/24 10:01
     */
    @Override
    public Long createPackage(CreatePackageReq packageReq){
        Long tenantId = SaaSContextHolder.currentTenantId();
        /************* 添加套包前校验 start *************/
        //1、创建套包必须选择产品,而且产品必须是审核通过,
        if (packageReq.getProductIds() == null || packageReq.getProductIds().isEmpty()){
            throw new BusinessException(PackageExceptionEnum.PACKAGE_PRODUCT_NULL);
        }
        Map<Long,ProductResp> productRespMap = new HashMap<>();
        Map<Long, List<Long>> deviceProductMap = new HashMap<>(); //key：deviceTypeId, value: productId集合,用于后面策略替换
        Set<Long> deviceTypeIdSet = new HashSet<>();
        int countHubProduct = 0; //统计网关数量
        for(Long productId : packageReq.getProductIds()) {
            ProductResp productResp = productApi.getProductById(productId);
            if (productResp == null) {
                throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "product not exist");
            }
            //审核状态校验
            if (productResp.getAuditStatus() == null) {
                throw new BusinessException(PackageExceptionEnum.PACKAGE_PRODUCT_NOT_AUDITPASS);
            }
            if (ProductAuditStatusEnum.AUDIT_SUCCESS.getValue() != productResp.getAuditStatus()) {
                throw new BusinessException(PackageExceptionEnum.PACKAGE_PRODUCT_NOT_AUDITPASS);
            }
            if (!tenantId.equals(productResp.getTenantId())) {
                throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "some product not belong to your tenant");
            }
            //统计技术方案为1（直接wifi方案）的产品数量
            if (productResp.getCommunicationMode().equals(TechnicalSchemeEnum.wifi.getCode().intValue())) {
                countHubProduct++;
            }
            Long deviceTypeId = productResp.getDeviceTypeId();
            List<Long> productIdList = deviceProductMap.get(deviceTypeId);
            if (productIdList == null) {
                productIdList = new ArrayList<>();
                deviceProductMap.put(deviceTypeId, productIdList);
            }
            productIdList.add(productId);
            productRespMap.put(productId, productResp);
            deviceTypeIdSet.add(deviceTypeId);
        }
        //2.至少且只能选择一个网关产品
        if (countHubProduct == 0) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_MUST_CHOOSE_HUB);
        }else if (countHubProduct>1) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_ONLY_ONE_HUB);
        }
        //3.校验选中的产品是否支持联动
        boolean hadSecurityProduct = false;
        List<DeviceTypeResp> deviceTypeResps = deviceTypeApi.getByIds(new ArrayList<>(deviceTypeIdSet));
        for (DeviceTypeResp deviceTypeResp : deviceTypeResps) {
            //顺便校验设备类型是否支持联动
            if (ModuleIftttTypeEnum.NO.getValue().equals(deviceTypeResp.getIftttType())) {
                Long productId = deviceProductMap.get(deviceTypeResp.getId()).get(0);
                String productName = productRespMap.get(productId).getProductName();
                throw new BusinessException(PackageExceptionEnum.PACKAGE_PRODUCT_NOT_IFTTT, productName);
            }
            if (SecurityDeviceTypeEnum.isSatisfy(deviceTypeResp.getType())) {
                hadSecurityProduct = true;
            }
        }

        //4.产品只能被一个套包关联，不能重复关联
        List<Long> hadAddIds = packageProductApi.chcekProductHadAdd(packageReq.getProductIds(), tenantId);
        if (hadAddIds!=null && !hadAddIds.isEmpty()) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_PRODUCT_HAD_ADD, productRespMap.get(hadAddIds.get(0)).getProductName());
        }
        /************* 添加套包前校验 end *************/

        Long newPackageId = null;
        Long userId = SaaSContextHolder.getCurrentUserId();
        if (packageReq.getPackageId() == null) {
            logger.debug("自定义创建套包");
            //自定义套包，如果套包类型为安防套包，则至少要包含keypad、keyfor其中一种产品
            if (!hadSecurityProduct) {
                throw new BusinessException(PackageExceptionEnum.PACKAGE_SECURITY_DEVICE_NOT_ENOUGH);
            }
            PackageReq addPackageReq = new PackageReq(packageReq.getPackageName(), tenantId, userId, new Date(), packageReq.getIcon(), packageReq.getPackageType(), packageReq.getPackageId());
            newPackageId = packageApi.addPackage(addPackageReq);
        } else {
            logger.debug("选择boss套包模板创建套包");
            Map<Long, PackageServiceModuleDetailResp> productIfModuleMap = new HashMap<>();
            Map<Long, PackageServiceModuleDetailResp> productThenModuleMap = new HashMap<>();
            //选择模板套包
            //1.校验套包是否存在
            Long packageId = packageReq.getPackageId();
            PackageResp packageResp = packageApi.getPackageById(packageId, tenantId);
            if (packageResp == null) {
                throw new BusinessException(PackageExceptionEnum.PACKAGE_NOT_EXIST);
            }
            packageReq.setPackageType(packageResp.getPackageType());
            //2.校验选择产品是否已满足套包必选设备类型（比如套包配了三种设备类型，则portal至少选择每种设备类型对应的产品一个，不满足不给创建；)
            List<Long> deviceTypeIds = packageDeviceTypeApi.getDeviceTypesByPackageId(packageId);
            if (deviceTypeIds == null || deviceTypeIds.isEmpty()) {
                throw new BusinessException(PackageExceptionEnum.PACKAGE_NOT_USED);
            }
            Iterator<Long> iterator = deviceTypeIds.iterator();
            while (iterator.hasNext()){
                Long deviceTypeId = iterator.next();
                for (Long productType: deviceTypeIdSet) {
                    if (productType.equals(deviceTypeId)){
                        iterator.remove();
                        break;
                    }
                }
            }
            if (!deviceTypeIds.isEmpty()) {
                throw new BusinessException(PackageExceptionEnum.PACKAGE_DEVICE_TYPE_NOT_ENOUGH);
            }
            logger.debug("保存场景基本信息");
            //2.先保存套包基本信息，因为后续需要使用到新套包id
            PackageReq addPackageReq = new PackageReq(packageReq.getPackageName(), tenantId, userId, new Date(), packageReq.getIcon(), packageResp.getPackageType(), packageReq.getPackageId());
            newPackageId = packageApi.addPackage(addPackageReq);

            try{
                if (PackageTypeEnum.AUTOMATION.getValue() == packageResp.getPackageType()) {
                    /******************** 场景start ********************/
                    logger.debug("场景复制开始");
                    List<SceneInfoResp> sceneInfoList = sceneInfoApi.getSceneInfoPage(packageId, -1L);
                    if (sceneInfoList != null && !sceneInfoList.isEmpty()) {
                        List<SceneInfoSaveReq> newSceneInfoList = new ArrayList<>();
                        for (SceneInfoResp demoScene : sceneInfoList) {
                            SceneInfoSaveReq newSceneInfo = new SceneInfoSaveReq(newPackageId, tenantId, demoScene.getSceneName(),userId, new Date(), demoScene.getId());
                            SceneDetailInfoReq newSceneDetail = new SceneDetailInfoReq();
                            List<Long> newProductIdList = new ArrayList<>();
                            SceneConfigReq newSceneConfig = new SceneConfigReq();

                            SceneDetailInfoReq sceneDetailInfoReq = JSON.parseObject(demoScene.getJson(), SceneDetailInfoReq.class);
                            //记录场景的产品
                            List<Long> deviceTypeIdList = sceneDetailInfoReq.getDevList();
                            deviceTypeIdList.forEach(deviceTypeId -> {
                                newProductIdList.addAll(deviceProductMap.get(deviceTypeId));
                            });
                            SceneConfigReq config = sceneDetailInfoReq.getConfig();
                            //2.1 解析设备配置
                            List<ThenDevInfoReq> thenDevInfoReqs = config.getDev();
                            List<ThenDevInfoReq> newThenProductList = this.getThenProductList(thenDevInfoReqs, deviceProductMap, productRespMap, productThenModuleMap);
                            newSceneConfig.setDev(newThenProductList);
                            //2.2 解析执行者（产品未定义，直接赋值）
                            newSceneConfig.setActuator(config.getActuator());
                            //2.3 场景赋值
                            newSceneDetail.setDevList(newProductIdList);
                            newSceneDetail.setConfig(newSceneConfig);
                            newSceneInfo.setJson(JsonUtil.toJson(newSceneDetail));
                            newSceneInfoList.add(newSceneInfo);
                        }
                        //插入数据库
                        newSceneInfoList.forEach(newSceneInfo->{
                            sceneInfoApi.insertSceneInfo(newSceneInfo);
                        });
                        logger.debug("场景复制结束");
                    }
                    /******************** 场景end ********************/
                }


                /************** 策略 start ********************/
                logger.debug("策略组装开始");
                //3.加载套包策略
                SearchTemplateRuleReq searchTemplateRuleReq = new SearchTemplateRuleReq();
                searchTemplateRuleReq.setPackageId(packageId);
                List<TemplateRuleResp> templateRuleResps = templateRuleApi.getTemplateRuleBy(searchTemplateRuleReq);
                if (templateRuleResps== null || templateRuleResps.isEmpty()) {
                    throw new BusinessException(PackageExceptionEnum.PACKAGE_NOT_USED);
                }

                //4.开始解析策略，校验产品是否有对应的属性、方法、事件(比如light设备类型有onoff属性，但是portal的产品light1没有onoff，则不能创建)
                List<TemplateRuleInfoReq> newTemplateRuleInfoReqs = new ArrayList<>();
                List<SceneInfoResp> newSceneInfos = sceneInfoApi.getSceneInfoPage(newPackageId ,tenantId);
                //key：boss的场景id， value:portal的场景id
                Map<Long, Long> sceneIdMap = newSceneInfos.stream().collect(Collectors.toMap(SceneInfoResp::getDemoSceneId,SceneInfoResp::getId, (a, b) -> a));
                templateRuleResps.forEach(templateRuleResp -> {
                    RuleDetailInfoReq rule = JSON.parseObject(templateRuleResp.getJson(), RuleDetailInfoReq.class);

                    TemplateRuleInfoReq newTemplateRuleInfoReq = new TemplateRuleInfoReq();
                    newTemplateRuleInfoReq.setRuleName(templateRuleResp.getRuleName());
                    newTemplateRuleInfoReq.setRuleType(templateRuleResp.getType());

                    RuleDetailInfoReq newRule = new RuleDetailInfoReq();
                    newRule.setSecurity(rule.getSecurity());
                    //-----------   if start ---------------
                    logger.debug("解析、组装策略this开始");
                    RuleIfInfoReq newIf = new RuleIfInfoReq();
                    TriggerInfoReq newIfTrigger = new TriggerInfoReq();

                    RuleIfInfoReq ifDemoReq = rule.getRuleIf();
                    TriggerInfoReq triggerInfoReq = ifDemoReq.getTrigger();
                    //4.1.1 if产品判断
                    List<IfDevInfoReq> ifDevList = triggerInfoReq.getDev();
                    List<IfDevInfoReq> newIfProductList = this.getIfProductList(ifDevList,deviceProductMap, productRespMap, productIfModuleMap);
                    newIfTrigger.setDev(newIfProductList);
                    //4.1.2 if第三方接入判断，产品经理暂时不清楚，直接复制
                    newIfTrigger.setThreeApi(triggerInfoReq.getThreeApi());
                    //4.1.3 if赋值
                    newIf.setTrigger(newIfTrigger);
                    newIf.setLogic(ifDemoReq.getLogic());
                    newRule.setRuleIf(newIf);
                    logger.debug("解析、组装策略this结束");
                    //------------  if end  ------------------------

                    //---------------- then start ------------------
                    logger.debug("解析、组装策略that开始");
                    RuleThenInfoReq newThen = new RuleThenInfoReq();
                    RuleThenInfoReq then = rule.getThen();
                    //4.2.1 then 产品
                    List<ThenDevInfoReq> thenDevInfoReqs = then.getDev();
                    List<ThenDevInfoReq> newThenProductList = this.getThenProductList(thenDevInfoReqs, deviceProductMap, productRespMap, productThenModuleMap);
                    newThen.setDev(newThenProductList);
                    //4.2.1 then 场景
                    List<Long> demoSceneIds = then.getScene();
                    if (demoSceneIds!=null && !demoSceneIds.isEmpty()) {
                        List<Long> newSceneIds = new ArrayList<>();
                        demoSceneIds.forEach(demoSceneId -> {
                            newSceneIds.add(sceneIdMap.get(demoSceneId));
                        });
                        newThen.setScene(newSceneIds);
                    }
                    //4.2.3 then 执行者(产品暂未定义，直接复制就好了)
                    List<ActuatorInfoVo> actuatorInfoVos = then.getActuator();
                    newThen.setActuator(actuatorInfoVos);
                    newRule.setThen(newThen);
                    logger.debug("解析、组装策略that结束");
                    //---------------- then end --------------------
                    newTemplateRuleInfoReq.setDetail(newRule);
                    newTemplateRuleInfoReqs.add(newTemplateRuleInfoReq);
                });
                logger.debug("策略组装结束");
                //4.3 保存策略
                if (newTemplateRuleInfoReqs != null && !newTemplateRuleInfoReqs.isEmpty()) {
                    List<TemplateRuleReq> templateRuleReqs = new ArrayList<>();
                    for (TemplateRuleInfoReq rule : newTemplateRuleInfoReqs) {
                        TemplateRuleReq templateRuleReq = new TemplateRuleReq(newPackageId, tenantId, rule.getRuleType(), JsonUtil.toJson(rule.getDetail()), new Date(), new Date(), rule.getRuleName());
                        templateRuleReqs.add(templateRuleReq);
                    }
                    templateRuleApi.batchInsert(templateRuleReqs);
                }
                logger.debug("策略保存完成");
                /************** 策略 end ********************/
            }catch (Exception e) {
                //数据回滚，再抛出异常
                packageApi.deletePackageRelateData(newPackageId, tenantId);
                throw e;
            }
        }
        /**  校验成功处理   */
        //记录套包关联产品
        SavePackageProductReq savePackageProductReq = new SavePackageProductReq(newPackageId, packageReq.getProductIds(),tenantId, userId);
        packageProductApi.save(savePackageProductReq);
        return newPackageId;
    }

    /**
     * @despriction：生成if中product配置信息
     * @author  yeshiyuan
     * @created 2018/12/5 21:13
     */
    private List<IfDevInfoReq> getIfProductList(List<IfDevInfoReq> ifDevList, Map<Long, List<Long>> deviceProductMap, Map<Long, ProductResp> productRespMap,
                                                Map<Long, PackageServiceModuleDetailResp> productIfModuleMap) {
        List<IfDevInfoReq> newIfProductList = new ArrayList<>();
        if (ifDevList!=null && !ifDevList.isEmpty()) {
            for (IfDevInfoReq ifDevInfoReq : ifDevList) {
                Long deviceTypeId = ifDevInfoReq.getId();
                List<IfAttrInfoReq> ifAttrReq = ifDevInfoReq.getAttrInfo();
                //先根据设备类型找到对应的产品，并加载其模组功能
                List<Long> productIdList = deviceProductMap.get(deviceTypeId);
                productIdList.forEach(pid -> {
                    IfDevInfoReq newIfProduct = new IfDevInfoReq();
                    newIfProduct.setId(pid);
                    newIfProduct.setIdx(ifDevInfoReq.getIdx());
                    newIfProduct.setAttrLogic(ifDevInfoReq.getAttrLogic());
                    String productName = productRespMap.get(pid).getProductName();
                    PackageServiceModuleDetailResp moduleDetailResp = productIfModuleMap.get(pid);
                    if (moduleDetailResp == null) {
                        moduleDetailResp = productToServiceModuleApi.queryServiceModuleDetailByIfttt(pid, ModuleIftttTypeEnum.IF.getDesc());
                        productIfModuleMap.put(pid, moduleDetailResp);
                    }
                    //开始校验套包策略的属性、事件、产品的模组
                    List<IfAttrInfoReq> productIfAttrs = this.checkIfDevAttrInfo(ifAttrReq, moduleDetailResp, productName);
                    newIfProduct.setAttrInfo(productIfAttrs);
                    newIfProductList.add(newIfProduct);
                });
            }
        }
        return newIfProductList;
    }


    /**
     * @despriction：生成then中product配置信息
     * @author  yeshiyuan
     * @created 2018/12/5 21:02
     */
    private List<ThenDevInfoReq> getThenProductList(List<ThenDevInfoReq> thenDevInfoReqs, Map<Long, List<Long>> deviceProductMap, Map<Long, ProductResp> productRespMap,
                                                    Map<Long, PackageServiceModuleDetailResp> productThenModuleMap){
        List<ThenDevInfoReq> newThenProductList = new ArrayList<>();
        if (thenDevInfoReqs!=null && !thenDevInfoReqs.isEmpty()) {
            for (ThenDevInfoReq thenDevInfoReq : thenDevInfoReqs) {
                Long deviceTypeId = thenDevInfoReq.getId();
                List<ThenAttrInfoReq> thenAttrReqs = thenDevInfoReq.getAttrInfo();
                //先根据设备类型找到对应的产品，并加载其模组功能
                List<Long> productIdList = deviceProductMap.get(deviceTypeId);
                productIdList.forEach(pid -> {
                    ThenDevInfoReq newThenProduct = new ThenDevInfoReq(pid, thenDevInfoReq.getIdx());
                    String productName = productRespMap.get(pid).getProductName();
                    //查找产品的then功能模组
                    PackageServiceModuleDetailResp moduleDetailResp = productThenModuleMap.get(pid);
                    if (moduleDetailResp == null) {
                        moduleDetailResp = productToServiceModuleApi.queryServiceModuleDetailByIfttt(pid, ModuleIftttTypeEnum.THEN.getDesc());
                        productThenModuleMap.put(pid, moduleDetailResp);
                    }
                    List<ThenAttrInfoReq> newThenAttrs = this.checkThenDevAttrInfo(thenAttrReqs, moduleDetailResp, productName);
                    newThenProduct.setAttrInfo(newThenAttrs);
                    newThenProductList.add(newThenProduct);
                });
            }
        }
        return newThenProductList;
    }


    /**
     * @despriction：校验if中dev的属性、事件
     * @author  yeshiyuan
     * @created 2018/12/5 11:02
     */
    private List<IfAttrInfoReq> checkIfDevAttrInfo(List<IfAttrInfoReq> ifAttrReqs, PackageServiceModuleDetailResp moduleDetailResp, String productName) {
        List<IfAttrInfoReq> productIfAttrs = new ArrayList<>();
        for(IfAttrInfoReq attrInfo : ifAttrReqs) {
            IfAttrInfoReq productAttr = null;
            String attrType = attrInfo.getAttrType();
            if (AttrTypeEnum.PROPERTY.getValue().equals(attrType)) {
                List<PropertyResp> properties = moduleDetailResp.getProperties();
                if (properties == null || properties.isEmpty()) {
                    throw new BusinessException(PackageExceptionEnum.PACKAGE_PROPERTY_ACTION_NOTEXIST, productName);
                }
                for(PropertyResp propertyResp : properties) {
                    if (propertyResp.getParentId().equals(attrInfo.getAttrId())) {
                        productAttr = new IfAttrInfoReq(propertyResp.getId(), attrType, attrInfo.getValue(), attrInfo.getCompOp(), null);
                        break;
                    }
                }
                if (productAttr == null) {
                    logger.debug("property not exist(copy propertyId:{}, productProperties:{})", attrInfo.getAttrId(), JsonUtil.toJson(moduleDetailResp.getProperties()));
                    throw new BusinessException(PackageExceptionEnum.PACKAGE_PROPERTY_ACTION_NOTEXIST, productName);
                }
            } else if (AttrTypeEnum.EVENT.getValue().equals(attrType)) {
                List<EventResp> events = moduleDetailResp.getEvents();
                if (events == null || events.isEmpty()) {
                    throw new BusinessException(PackageExceptionEnum.PACKAGE_PROPERTY_EVENT_NOTEXIST, productName);
                }
                for (EventResp eventResp : events) {
                    if (eventResp.getParentId().equals(attrInfo.getAttrId())) {
                        productAttr = new IfAttrInfoReq(eventResp.getId(), attrType, attrInfo.getValue(), attrInfo.getCompOp(), null);
                        //校验事件下的属性
                        List<PropertyInfoVo> eventProperties = attrInfo.getEventProperties();
                        List<PropertyInfoVo> newEventProperties = this.getAttrProperty(eventProperties, eventResp.getProperties(), productName);
                        productAttr.setEventProperties(newEventProperties);
                        break;
                    }
                }
                if (productAttr == null) {
                    logger.debug("event not exist(copy eventId:{}, productEvents:{})", attrInfo.getAttrId(), JsonUtil.toJson(moduleDetailResp.getEvents()));
                    throw new BusinessException(PackageExceptionEnum.PACKAGE_PROPERTY_EVENT_NOTEXIST, productName);
                }
            }
            productIfAttrs.add(productAttr);
        }
        return productIfAttrs;
    }

    /**
     * @despriction：校验then的dev属性、方法
     * @author  yeshiyuan
     * @created 2018/12/5 10:38
     */
    private List<ThenAttrInfoReq> checkThenDevAttrInfo(List<ThenAttrInfoReq> thenAttrReqs, PackageServiceModuleDetailResp moduleDetailResp, String productName) {
        if (moduleDetailResp == null) {
            throw new BusinessException(PackageExceptionEnum.PACKAGE_PRODUCT_NOT_IFTTT);
        }
        List<ThenAttrInfoReq> newThenAttrs = new ArrayList<>();
        if (thenAttrReqs != null && !thenAttrReqs.isEmpty()) {
            Map<Long, PropertyResp> propertyRespMap = new HashMap<>();
            List<PropertyResp> properties = moduleDetailResp.getProperties();
            if (properties != null && !properties.isEmpty()) {
                propertyRespMap = properties.stream().collect(Collectors.toMap(PropertyResp::getParentId, a->a, (a, b)->a));
            }
            Map<Long, ActionResp> actionRespMap = new HashMap<>();
            List<ActionResp> actions = moduleDetailResp.getActions();
            if (actions != null && !actions.isEmpty()) {
                actionRespMap = actions.stream().collect(Collectors.toMap(ActionResp::getParentId, a->a, (a,b)->a));
            }
            for(ThenAttrInfoReq attrInfo : thenAttrReqs) {
                ThenAttrInfoReq productAttr = null;
                String attrType = attrInfo.getAttrType();
                if (AttrTypeEnum.PROPERTY.getValue().equals(attrType)) {
                    PropertyResp propertyResp = propertyRespMap.get(attrInfo.getAttrId());
                    if (propertyResp == null ) {
                        logger.debug("property not exist(copy propertyId:{}, productProperties:{})", attrInfo.getAttrId(), JsonUtil.toJson(moduleDetailResp.getProperties()));
                        throw new BusinessException(PackageExceptionEnum.PACKAGE_PROPERTY_PROPERTY_NOTEXIST, productName);
                    }
                    //校验值
                    CheckPropertyUtil.compareProperty(attrInfo.getValue(), propertyResp);
                    productAttr = new ThenAttrInfoReq(propertyResp.getId(), attrType, attrInfo.getValue(), attrInfo.getCompOp(), null);
                } else if (AttrTypeEnum.ACTION.getValue().equals(attrType)) {
                    ActionResp actionResp = actionRespMap.get(attrInfo.getAttrId());
                    if (actionResp == null ) {
                        logger.debug("action not exist(copy actionId:{}, productActions:{})", attrInfo.getAttrId(), JsonUtil.toJson(moduleDetailResp.getActions()));
                        throw new BusinessException(PackageExceptionEnum.PACKAGE_PROPERTY_ACTION_NOTEXIST, productName);
                    }
                    //校验方法下的属性
                    List<PropertyInfoVo> actionProperties = attrInfo.getActionProperties();
                    List<PropertyInfoVo> newActionProperties = this.getAttrProperty(actionProperties, actionResp.getProperties(), productName);
                    productAttr = new ThenAttrInfoReq(actionResp.getId(), attrType, attrInfo.getValue(), attrInfo.getCompOp(), newActionProperties);
                }
                newThenAttrs.add(productAttr);
            }
        }
        return newThenAttrs;
    }

    /**
     * @despriction：校验配置中的属性
     * @author  yeshiyuan
     * @created 2018/12/6 9:57
     */
    private List<PropertyInfoVo> getAttrProperty(List<PropertyInfoVo> actionProperties, List<PropertyResp> propertyResps, String productName) {
        List<PropertyInfoVo> newActionProperties = new ArrayList<>();
        if (actionProperties!=null && !actionProperties.isEmpty()) {
            if (propertyResps == null || propertyResps.isEmpty()) {
                throw new BusinessException(PackageExceptionEnum.PACKAGE_PROPERTY_PROPERTY_NOTEXIST, productName);
            }
            Map<Long, PropertyResp> propertyRespMap = propertyResps.stream().collect(Collectors.toMap(PropertyResp::getParentId, a->a, (a,b)->a));
            actionProperties.forEach(vo -> {
                Long propertyId = vo.getPropertyId();
                if (propertyRespMap.containsKey(propertyId)) {
                    PropertyResp propertyResp = propertyRespMap.get(propertyId);
                    CheckPropertyUtil.compareProperty(vo.getValue(), propertyResp);
                    PropertyInfoVo propertyInfoVo = new PropertyInfoVo(propertyResp.getId(), vo.getValue(), vo.getCompOp());
                    newActionProperties.add(propertyInfoVo);
                }else {
                    logger.debug("property not exist(copy propertyId:{}, productProperties:{})", propertyId, JsonUtil.toJson(propertyResps));
                    throw new BusinessException(PackageExceptionEnum.PACKAGE_PROPERTY_PROPERTY_NOTEXIST, productName);
                }
            });
        }
        return newActionProperties;
    }
}
