package com.iot.tenant.service.impl;

import com.iot.tenant.entity.*;
import com.iot.tenant.enums.DeviceNetworkHelpEnum;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.mapper.DeviceNetworkStepTenantMapper;
import com.iot.tenant.mapper.LangInfoBaseMapper;
import com.iot.tenant.mapper.LangInfoTenantMapper;
import com.iot.tenant.service.IDeviceNetworkStepTenantService;
import com.iot.tenant.vo.req.lang.LangInfoReq;
import com.iot.tenant.vo.req.network.*;
import com.iot.tenant.vo.resp.network.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/10/9 11:48
 * 修改人： yeshiyuan
 * 修改时间：2018/10/9 11:48
 * 修改描述：
 */
@Service
public class DeviceNetworkStepTenantServiceImpl implements IDeviceNetworkStepTenantService {

    @Autowired
    private DeviceNetworkStepTenantMapper deviceNetworkStepTenantMapper;

    @Autowired
    private LangInfoTenantMapper langInfoTenantMapper;

    @Autowired
    private LangInfoBaseMapper langInfoBaseMapper;

    /**
     * @despriction：拷贝配网步骤文案
     * @author  yeshiyuan
     * @created 2018/10/9 11:31
     * @return
     */
    @Override
    @Transactional
    public void copyNetworkStep(CopyNetworkStepReq copyNetworkStepReq) {
        if (copyNetworkStepReq.getNetworkTypeIds() == null || copyNetworkStepReq.getNetworkTypeIds().isEmpty()) {
            return;
        }
        deviceNetworkStepTenantMapper.deleteByTenantAndAppAndProduct(copyNetworkStepReq.getTenantId(),copyNetworkStepReq.getAppId(),copyNetworkStepReq.getProductId());
        String objectId = copyNetworkStepReq.getAppId()+"_"+copyNetworkStepReq.getProductId();
        langInfoTenantMapper.deleteByObjectTypeAndObjectIdAndTenantId(LangInfoObjectTypeEnum.deviceNetwork.toString(), objectId,copyNetworkStepReq.getTenantId());
        deviceNetworkStepTenantMapper.copyNetworkStep(copyNetworkStepReq, new Date());
        String objectType = LangInfoObjectTypeEnum.deviceNetwork.toString();
        //拷贝对应的文案，需转化key格式
        List<LangInfoBase> langInfoBases = new ArrayList<>();
        copyNetworkStepReq.getNetworkTypeIds().forEach(id->{
            List<LangInfoBase> list = langInfoBaseMapper.findByObjectIdAndObjectTypeAndLikeKey(LangInfoObjectTypeEnum.deviceNetwork.toString(), copyNetworkStepReq.getDeviceTypeId(),
                    copyNetworkStepReq.getDeviceTypeId()+"_"+id);
            langInfoBases.addAll(list);
        });
        List<LangInfoTenant> langInfoTenants = new ArrayList<>();
        langInfoBases.forEach(langInfoBase -> {
            //模板key的格式： （设备类型id_配网模式_步骤下标:button eg: 1_1_1:button）,需转成 appId_productId_配网模式_步骤下标:button
            String oldKey = langInfoBase.getLangKey();
            String langKey = objectId + oldKey.substring(oldKey.indexOf("_"));
            LangInfoTenant lang = new LangInfoTenant(copyNetworkStepReq.getTenantId(), objectType, objectId,
                    langInfoBase.getLangType(), langKey , langInfoBase.getLangValue(), copyNetworkStepReq.getUserId(), new Date(), langInfoBase.getBelongModule());
            langInfoTenants.add(lang);
        });
        if (langInfoTenants != null && !langInfoTenants.isEmpty()) {
            langInfoTenantMapper.batchInsert(langInfoTenants);
        }
    }

    /**
     * @despriction：拷贝配网步骤
     * @author  yeshiyuan
     * @created 2018/10/26 9:30
     * @return
     */
    @Transactional
    @Override
    public void copyNetworkStepTenant(CopyNetworkStepReq copyNetworkStepReq, Long oldAppId) {
        deviceNetworkStepTenantMapper.copyNetworkStepTenant(copyNetworkStepReq, new Date(), oldAppId);
        String objectType = LangInfoObjectTypeEnum.deviceNetwork.toString();
        //拷贝对应的文案，需转化key格式
        String copyObjectId = oldAppId +"_"+ copyNetworkStepReq.getProductId();
        List<LangInfoTenant> langInfoTenants = langInfoTenantMapper.findByObjectTypeAndObjectIdAndLikeKey(LangInfoObjectTypeEnum.deviceNetwork.toString(), copyObjectId,
                copyNetworkStepReq.getTenantId(), copyObjectId);
        List<LangInfoTenant> newLangInfoList = new ArrayList<>();
        langInfoTenants.forEach(langInfoBase -> {
            //模板key的格式： （设备类型id_配网模式_步骤下标:button eg: 1_1_1:button）,需转成 appId_productId_配网模式_步骤下标:button
            String oldKey = langInfoBase.getLangKey();
            String langKey = copyNetworkStepReq.getAppId() + "_"+copyNetworkStepReq.getProductId() + oldKey.replace(copyObjectId,"");
            LangInfoTenant lang = new LangInfoTenant(copyNetworkStepReq.getTenantId(), objectType, copyNetworkStepReq.getAppId()+"_"+copyNetworkStepReq.getProductId(),
                    langInfoBase.getLangType(), langKey , langInfoBase.getLangValue(), copyNetworkStepReq.getUserId(), new Date(), langInfoBase.getBelongModule());
            newLangInfoList.add(lang);
        });
        langInfoTenantMapper.batchInsert(newLangInfoList);
    }

    /**
     * @despriction：保存配网步骤文案（先删后插）
     * @author  yeshiyuan
     * @created 2018/10/9 14:02
     * @return
     */
    @Override
    @Transactional
    public void save(SaveNetworkStepTenantReq req) {
        String objectId = req.getAppId() + "_" + req.getProductId();
        String objectType = LangInfoObjectTypeEnum.deviceNetwork.toString();
        List<DeviceNetworkStepTenant> list = new ArrayList<>();
        List<LangInfoTenant> langInfoTenants = new ArrayList<>();
        for (NetworkInfoReq info : req.getNetworkInfos()) {
            for (NetworkStepReq step: info.getSteps()) {
                DeviceNetworkStepTenant stepTenant = new DeviceNetworkStepTenant();
                stepTenant.setAppId(req.getAppId());
                stepTenant.setProductId(req.getProductId());
                stepTenant.setCreateBy(req.getUserId());
                stepTenant.setCreateTime(new Date());
                stepTenant.setNetworkTypeId(info.getNetworkTypeId());
                stepTenant.setTenantId(req.getTenantId());
                stepTenant.setStep(step.getStep());
                stepTenant.setIcon(step.getIcon());
                stepTenant.setIsHelp(DeviceNetworkHelpEnum.N.name());
                String preKey = NetworkStepKey.getTenantKeyPre(req.getAppId(), req.getProductId(), info.getNetworkTypeId(),step.getStep());
                for (LangInfoReq lang : step.getLangInfos()) {
                    for (Map.Entry<String,String> entry: lang.getVal().entrySet()) {
                        LangInfoTenant langInfoTenant = new LangInfoTenant(req.getTenantId(), objectType, objectId,
                                entry.getKey(), preKey + lang.getKey(), entry.getValue(), req.getUserId(), new Date(), null);
                        langInfoTenants.add(langInfoTenant);
                    }
                }
                //帮助文档
                NetworkStepHelpReq helpReq = step.getHelp();
                if (helpReq != null) {
                    DeviceNetworkStepTenant helpBase = new DeviceNetworkStepTenant(req.getTenantId(), req.getAppId(), req.getProductId(), info.getNetworkTypeId(),
                            DeviceNetworkHelpEnum.Y.name(), step.getStep(), helpReq.getIcon(),req.getUserId(), new Date());
                    list.add(helpBase);
                    String preHelpKey = NetworkStepKey.getTenantHelpKeyPre(req.getAppId(), req.getProductId(), info.getNetworkTypeId(),step.getStep());
                    for (LangInfoReq lang : helpReq.getLangInfos()) {
                        for (Map.Entry<String,String> entry: lang.getVal().entrySet()) {
                            LangInfoTenant langInfoTenant = new LangInfoTenant(req.getTenantId(), objectType, objectId,
                                    entry.getKey(), preHelpKey + lang.getKey(), entry.getValue(), req.getUserId(), new Date(), null);
                            langInfoTenants.add(langInfoTenant);
                        }
                    }
                }
                list.add(stepTenant);
            }
        }
        //先删除旧的步骤、文案
        deviceNetworkStepTenantMapper.deleteByTenantAndAppAndProduct(req.getTenantId(), req.getAppId(), req.getProductId());
        langInfoTenantMapper.deleteByObjectTypeAndObjectIdAndTenantId(LangInfoObjectTypeEnum.deviceNetwork.toString(),objectId, req.getTenantId());
        //再插入新的数据
        deviceNetworkStepTenantMapper.batchInsert(list);
        langInfoTenantMapper.batchInsert(langInfoTenants);
    }

    /**
     * @despriction：查询配网步骤详情
     * @author  yeshiyuan
     * @created 2018/10/9 16:17
     * @return
     */
    @Override
    public DeviceNetworkStepTenantResp queryNetworkStep(Long tenantId, Long appId, Long productId) {
        DeviceNetworkStepTenantResp deviceNetworkStepTenantResp = new DeviceNetworkStepTenantResp();
        deviceNetworkStepTenantResp.setAppId(appId);
        deviceNetworkStepTenantResp.setProductId(productId);
        List<DeviceNetworkStepTenant> deviceNetworkStepTenantList = deviceNetworkStepTenantMapper.findByTenantIdAndAppIdAndProductId(tenantId, appId, productId);
        if (!deviceNetworkStepTenantList.isEmpty()) {
            List<LangInfoTenant> langInfoTenantList = langInfoTenantMapper.queryLangInfos(tenantId, appId+"_"+productId, LangInfoObjectTypeEnum.deviceNetwork.toString(), null);
            Map<String, LangInfoReq> langInfoMap = LangInfoTenantServiceImpl.toLangInfoReqMap(langInfoTenantList);
            //配网步骤对应的help
            List<DeviceNetworkStepTenant> helpStepList = deviceNetworkStepTenantList.stream().filter(o ->
                    o.getIsHelp().equals(DeviceNetworkHelpEnum.Y.toString())
            ).collect(Collectors.toList());
            Map<String,DeviceNetworkStepTenant> helpStepMap = new HashMap<>();
            helpStepList.forEach(o -> {
                helpStepMap.put(o.getNetworkTypeId()+"_"+o.getStep(),o);
            });
            //配网步骤
            List<DeviceNetworkStepTenant> networkStepTenants = deviceNetworkStepTenantList.stream().filter(o ->
                    o.getIsHelp().equals(DeviceNetworkHelpEnum.N.toString())
            ).collect(Collectors.toList());
            Map<Long, List<NetworkStepResp>> stepMap = new HashMap<>();  //key为配网模式，value为配网步骤
            networkStepTenants.forEach(step -> {
                List<NetworkStepResp> steps = stepMap.get(step.getNetworkTypeId());
                if (steps == null) {
                    steps = new ArrayList<>();
                }
                NetworkStepResp stepResp = new NetworkStepResp();
                stepResp.setIcon(step.getIcon());
                stepResp.setStep(step.getStep());
                List<LangInfoReq> langInfoReqs = new ArrayList<>();
                String preKey = NetworkStepKey.getTenantKeyPre(appId, productId, step.getNetworkTypeId(), step.getStep());
                String buttonKey = preKey + NetworkStepKey.next; //配网按钮key
                String descKey = preKey + NetworkStepKey.desc; //配网文案key
                String helpKey = preKey + NetworkStepKey.help; // 帮助文档key
                LangInfoReq buttonInfoReq = langInfoMap.get(buttonKey);
                if (buttonInfoReq != null) {
                    NetworkStepKey.removePre(preKey, buttonInfoReq);
                    langInfoReqs.add(buttonInfoReq);
                }
                LangInfoReq descInfoReq = langInfoMap.get(descKey);
                if (descInfoReq != null) {
                    NetworkStepKey.removePre(preKey, descInfoReq);
                    langInfoReqs.add(descInfoReq);
                }
                LangInfoReq helpInfoReq = langInfoMap.get(helpKey);
                if (helpInfoReq != null) {
                    NetworkStepKey.removePre(preKey, helpInfoReq);
                    langInfoReqs.add(helpInfoReq);
                }
                stepResp.setLangInfos(langInfoReqs);
                //是否有帮助文案
                DeviceNetworkStepTenant helpStep = helpStepMap.get(step.getNetworkTypeId() + "_" + step.getStep());
                if (helpStep!=null) {
                    NetworkStepHelpReq stepHelpReq = new NetworkStepHelpReq();
                    stepHelpReq.setIcon(helpStep.getIcon());
                    String preHelpKey = NetworkStepKey.getTenantHelpKeyPre(appId, productId, step.getNetworkTypeId(), step.getStep());
                    String buttonHelpKey = preHelpKey + NetworkStepKey.next; //配网按钮key
                    String descHelpKey = preHelpKey + NetworkStepKey.desc; //配网文案key
                    String helpHelpKey = preHelpKey + NetworkStepKey.help; // 帮助文档key
                    List<LangInfoReq> langInfoHelpReqs = new ArrayList<>();
                    if (langInfoMap.containsKey(descHelpKey)) {
                        langInfoHelpReqs.add(NetworkStepKey.removePre(preHelpKey, langInfoMap.get(descHelpKey)));
                    }
                    if (langInfoMap.containsKey(buttonHelpKey)) {
                        langInfoHelpReqs.add(NetworkStepKey.removePre(preHelpKey, langInfoMap.get(buttonHelpKey)));
                    }
                    if (langInfoMap.containsKey(helpHelpKey)) {
                        langInfoHelpReqs.add(NetworkStepKey.removePre(preHelpKey, langInfoMap.get(helpHelpKey)));
                    }
                    stepHelpReq.setLangInfos(langInfoHelpReqs);
                    stepResp.setHelp(stepHelpReq);
                }
                steps.add(stepResp);
                stepMap.put(step.getNetworkTypeId(),steps);
            });
            List<NetworkInfoResp> networkInfos = new ArrayList<>();
            stepMap.forEach((networkTypeId,steps) -> {
                NetworkInfoResp resp = new NetworkInfoResp();
                resp.setNetworkTypeId(networkTypeId);
                resp.setSteps(steps);
                networkInfos.add(resp);
            });
            deviceNetworkStepTenantResp.setNetworkInfos(networkInfos);
        }
        return deviceNetworkStepTenantResp;
    }

    /**
     * @despriction：删除配网步骤及对应文案
     * @author  yeshiyuan
     * @created 2018/10/10 13:50
     * @return
     */
    @Override
    @Transactional
    public void deleteNetworkStep(Long tenantId, Long appId, Long productId) {
        //先删除旧的步骤、文案
        deviceNetworkStepTenantMapper.deleteByTenantAndAppAndProduct(tenantId, appId, productId);
        langInfoTenantMapper.deleteByObjectTypeAndObjectIdAndTenantId(LangInfoObjectTypeEnum.deviceNetwork.toString(),appId+"_"+productId, tenantId);
    }

    /**
     * @despriction：获取配网步骤文件格式
     * @author  yeshiyuan
     * @created 2018/10/18 14:57
     * @return
     */
    @Override
    public Map<String, NetworkFileFormat> getNetworkFileFormat(Long tenantId, Long appId, Long productId) {
        Map<String, NetworkFileFormat> map = new HashMap<>();
        List<DeviceNetworkStepTenant> deviceNetworkStepTenantList = deviceNetworkStepTenantMapper.findByTenantIdAndAppIdAndProductId(tenantId, appId, productId);
        if (!deviceNetworkStepTenantList.isEmpty()) {
            //寻找对应的配网文案，并转换为key为语言类型，value为（key：语言key，value：文案）
            List<LangInfoTenant> langInfoTenantList = langInfoTenantMapper.queryLangInfos(tenantId, appId+"_"+productId, LangInfoObjectTypeEnum.deviceNetwork.toString(), null);
            Map<String, Map<String, String>> langInfoMap = LangInfoTenantServiceImpl.toLangKeyValueRespMap(langInfoTenantList);
            //配网步骤对应的help
            List<DeviceNetworkStepTenant> helpStepList = deviceNetworkStepTenantList.stream().filter(o ->
                    o.getIsHelp().equals(DeviceNetworkHelpEnum.Y.toString())
            ).collect(Collectors.toList());
            //配网步骤
            List<DeviceNetworkStepTenant> networkStepTenants = deviceNetworkStepTenantList.stream().filter(o ->
                    o.getIsHelp().equals(DeviceNetworkHelpEnum.N.toString())
            ).collect(Collectors.toList());

            langInfoMap.forEach((langType, langInfo) -> {
                Set<String> fileIds = new HashSet<>();
                NetworkFileFormat fileFormat = new NetworkFileFormat();
                fileFormat.setName(productId.toString());
                Map<String, NetworkTabsFormat> tabsFormatMap = new HashMap<>(); //key为配网类型
                //配网步骤文案
                networkStepTenants.forEach(step -> {
                    NetworkTabsFormat tabsFormat = tabsFormatMap.get(step.getNetworkTypeId().toString());
                    if (tabsFormat == null) {
                        tabsFormat = new NetworkTabsFormat();
                        tabsFormat.setNetworkTypeId(step.getNetworkTypeId());
                        tabsFormatMap.put(step.getNetworkTypeId().toString(), tabsFormat);
                    }
                    List<NetworkStepFormat> steps = tabsFormat.getSteps();
                    if (steps == null) {
                        steps = new ArrayList<>();
                        tabsFormat.setSteps(steps);
                    }
                    String preKey = NetworkStepKey.getTenantKeyPre(appId, productId, step.getNetworkTypeId(), step.getStep());
                    NetworkStepFormat stepFormat = new NetworkStepFormat();
                    stepFormat.setHelp(langInfo.get(preKey + NetworkStepKey.help));
                    stepFormat.setDescription(langInfo.get(preKey + NetworkStepKey.desc));
                    stepFormat.setNext(langInfo.get(preKey + NetworkStepKey.next));
                    steps.add(stepFormat);
                    if (step.getIcon()!=null) {
                        String icon = step.getIcon().replace(NetworkStepKey.preIcon, "");
                        stepFormat.setIcon(icon);
                        fileIds.add(icon);
                    }
                });
                //帮助步骤文案
                helpStepList.forEach(help -> {
                    NetworkTabsFormat tabsFormat = tabsFormatMap.get(help.getNetworkTypeId().toString());
                    if (tabsFormat == null) {
                        tabsFormat = new NetworkTabsFormat();
                        tabsFormat.setNetworkTypeId(help.getNetworkTypeId());
                        tabsFormatMap.put(help.getNetworkTypeId().toString(), tabsFormat);
                    }
                    List<NetworkHelpFormat> helps = tabsFormat.getHelps();
                    if (helps == null) {
                        helps = new ArrayList<>();
                        tabsFormat.setHelps(helps);
                    }
                    String preHelpKey = NetworkStepKey.getTenantHelpKeyPre(appId, productId, help.getNetworkTypeId(), help.getStep());
                    NetworkHelpFormat helpFormat = new NetworkHelpFormat();
                    helpFormat.setDescription(langInfo.get(preHelpKey + NetworkStepKey.desc));
                    helpFormat.setNext(langInfo.get(preHelpKey + NetworkStepKey.next));
                    helps.add(helpFormat);
                    if (help.getIcon()!=null) {
                        String icon = help.getIcon().replace(NetworkStepKey.preIcon, "");
                        helpFormat.setIcon(icon);
                        fileIds.add(icon);
                    }
                });
                fileFormat.setTabs(tabsFormatMap.values().stream().collect(Collectors.toList()));
                fileFormat.setFileIds(fileIds);
                map.put(langType, fileFormat);
            });
        }
        return map;
    }

    /**
     * @despriction：校验是否存在配网步骤
     * @author  yeshiyuan
     * @created 2018/11/29 13:46
     */
    @Override
    public boolean checkExist(Long appId, Long productId, Long tenantId) {
        boolean result = false;
        int i = deviceNetworkStepTenantMapper.checkExist(tenantId, appId, productId);
        if (i>0) {
            return true;
        }
        return result;
    }

    /**
     * @despriction：删除app下的产品配网
     * @author  yeshiyuan
     * @created 2018/10/10 13:50
     * @return
     */
    @Override
    public void deleteAppNetwork(Long appId, Long tenantId) {
        deviceNetworkStepTenantMapper.deleteAppNetworkData(tenantId, appId);
        langInfoTenantMapper.deleteAppNetworkData(tenantId, appId, LangInfoObjectTypeEnum.deviceNetwork.name());
    }
}
