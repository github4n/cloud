package com.iot.tenant.service.impl;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.enums.DataStatusEnum;
import com.iot.tenant.entity.DeviceNetworkStepBase;
import com.iot.tenant.entity.LangInfoBase;
import com.iot.tenant.entity.NetworkStepKey;
import com.iot.tenant.enums.DeviceNetworkHelpEnum;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.mapper.DeviceNetworkStepBaseMapper;
import com.iot.tenant.mapper.LangInfoBaseMapper;
import com.iot.tenant.service.IDeviceNetworkStepBaseService;
import com.iot.tenant.vo.req.lang.LangInfoReq;
import com.iot.tenant.vo.req.network.NetworkInfoReq;
import com.iot.tenant.vo.req.network.NetworkStepHelpReq;
import com.iot.tenant.vo.req.network.NetworkStepReq;
import com.iot.tenant.vo.req.network.SaveNetworkStepBaseReq;
import com.iot.tenant.vo.resp.network.DeviceNetworkStepBaseResp;
import com.iot.tenant.vo.resp.network.NetworkInfoResp;
import com.iot.tenant.vo.resp.network.NetworkStepResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目名称：cloud
 * 功能描述：设备配网步骤模板（BOSS使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 15:41
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 15:41
 * 修改描述：
 */
@Service
public class DeviceNetworkStepBaseServiceImpl implements IDeviceNetworkStepBaseService{

    @Autowired
    private DeviceNetworkStepBaseMapper deviceNetworkStepBaseMapper;

    @Autowired
    private LangInfoBaseMapper langInfoBaseMapper;

    /**
     * @despriction：保存步骤并保存步骤对应的文案至lang_info_base
     * @author  yeshiyuan
     * @created 2018/10/8 16:50
     * @return
     */
    @Transactional
    @Override
    public void saveStepAndInsertLangInfo(SaveNetworkStepBaseReq req) {
        List<DeviceNetworkStepBase> list = new ArrayList<>();
        List<LangInfoBase> langInfoBaseList = new ArrayList<>();
        for (NetworkInfoReq info : req.getNetworkInfos()) {
            langInfoBaseMapper.deleteNetworkStepLang(LangInfoObjectTypeEnum.deviceNetwork.toString(), req.getDeviceTypeId(), req.getDeviceTypeId()+"_"+info.getNetworkTypeId()+"_");
            deviceNetworkStepBaseMapper.deleteByDeviceTypeId(req.getDeviceTypeId(), info.getNetworkTypeId());
            for (NetworkStepReq step: info.getSteps()) {
                DeviceNetworkStepBase base = new DeviceNetworkStepBase();
                base.setDeviceTypeId(req.getDeviceTypeId());
                base.setCreateBy(req.getUserId());
                base.setCreateTime(new Date());
                base.setNetworkTypeId(info.getNetworkTypeId());
                base.setDataStatus(DataStatusEnum.VALID.getDesc());
                base.setStep(step.getStep());
                base.setIcon(step.getIcon());
                base.setIsHelp(DeviceNetworkHelpEnum.N.name());
                list.add(base);
                String preKey = NetworkStepKey.getBaseKeyPre(req.getDeviceTypeId(), info.getNetworkTypeId(),step.getStep());
                for (LangInfoReq lang : step.getLangInfos()) {
                    for (Map.Entry<String,String> entry: lang.getVal().entrySet()) {
                        String langKey = preKey + lang.getKey();
                        LangInfoBase langInfoBase = new LangInfoBase(-1L, LangInfoObjectTypeEnum.deviceNetwork.toString(), req.getDeviceTypeId()
                                ,entry.getKey(), langKey,entry.getValue(),req.getUserId(),new Date(), null);
                        langInfoBaseList.add(langInfoBase);
                    }
                }
                //帮助文档
                NetworkStepHelpReq helpReq = step.getHelp();
                if (helpReq != null) {
                    String preHelpKey = NetworkStepKey.getBaseHelpKeyPre(req.getDeviceTypeId(), info.getNetworkTypeId(),step.getStep());
                    DeviceNetworkStepBase helpBase = new DeviceNetworkStepBase(req.getDeviceTypeId(), info.getNetworkTypeId(),
                            DeviceNetworkHelpEnum.Y.name(), step.getStep(), helpReq.getIcon(),DataStatusEnum.VALID.getDesc(),req.getUserId(), new Date());
                    list.add(helpBase);
                    for (LangInfoReq lang : helpReq.getLangInfos()) {
                        for (Map.Entry<String,String> entry: lang.getVal().entrySet()) {
                            LangInfoBase langInfoBase = new LangInfoBase(-1L, LangInfoObjectTypeEnum.deviceNetwork.toString(), req.getDeviceTypeId()
                                    ,entry.getKey(), preHelpKey + lang.getKey(),entry.getValue(),req.getUserId(),new Date(), null);
                            langInfoBaseList.add(langInfoBase);
                        }
                    }
                }
            }
        }

        deviceNetworkStepBaseMapper.batchInsert(list);
        langInfoBaseMapper.batchInsert(langInfoBaseList);
    }

    /**
     * @despriction：查询某设备类型对应的配网步骤
     * @author  yeshiyuan
     * @created 2018/10/8 17:48
     * @return
     */
    @Override
    public DeviceNetworkStepBaseResp queryNetworkStep(Long deviceTypeId, Long networkTypeId) {
        DeviceNetworkStepBaseResp resp = new DeviceNetworkStepBaseResp();
        resp.setDeviceTypeId(deviceTypeId);
        List<DeviceNetworkStepBase> stepBases = deviceNetworkStepBaseMapper.queryNetworkStepBase(deviceTypeId, networkTypeId);
        if (!stepBases.isEmpty()) {
            //搜索配网步骤对应的文案
            List<LangInfoBase> infoBases = langInfoBaseMapper.findByObjectIdAndObjectType(LangInfoObjectTypeEnum.deviceNetwork.toString(),deviceTypeId);
            Map<String,LangInfoReq> langInfoMap = LangInfoBaseServiceImpl.toLangInfoReqMap(infoBases);
            //配网步骤对应的help
            List<DeviceNetworkStepBase> helpStepList = stepBases.stream().filter(o ->
                    o.getIsHelp().equals(DeviceNetworkHelpEnum.Y.toString())
            ).collect(Collectors.toList());
            Map<String,DeviceNetworkStepBase> helpStepMap = new HashMap<>();
            helpStepList.forEach(o -> {
                if(o.getIsHelp().equals(DeviceNetworkHelpEnum.Y.toString())) {
                    helpStepMap.put(o.getNetworkTypeId()+"_"+o.getStep(),o);
                }
            });
            //配网步骤
            List<DeviceNetworkStepBase> networkStepBases = stepBases.stream().filter(o ->
                    o.getIsHelp().equals(DeviceNetworkHelpEnum.N.toString())
            ).collect(Collectors.toList());
            Map<Long,List<NetworkStepResp>> stepsMap = new HashMap<>(); //用于存放步骤详情（key 为 配网模式）
            networkStepBases.forEach(stepBase -> {
                String preKey = NetworkStepKey.getBaseKeyPre(deviceTypeId, stepBase.getNetworkTypeId(), stepBase.getStep());
                String buttonKey = preKey + NetworkStepKey.next; //配网按钮key
                String descKey = preKey + NetworkStepKey.desc; //配网文案key
                String helpKey = preKey + NetworkStepKey.help; // 帮助文档key
                List<NetworkStepResp> setps = stepsMap.get(stepBase.getNetworkTypeId());
                if (setps == null) {
                    setps = new ArrayList<>();
                }
                NetworkStepResp networkStepReq = new NetworkStepResp();
                networkStepReq.setIcon(stepBase.getIcon());
                networkStepReq.setStep(stepBase.getStep());
                List<LangInfoReq> langInfoReqs = new ArrayList<>();
                if (langInfoMap.containsKey(buttonKey)) {
                    langInfoReqs.add(NetworkStepKey.removePre(preKey, langInfoMap.get(buttonKey)));
                }
                if (langInfoMap.containsKey(descKey)) {
                    langInfoReqs.add(NetworkStepKey.removePre(preKey, langInfoMap.get(descKey)));
                }
                if (langInfoMap.containsKey(helpKey)) {
                    langInfoReqs.add(NetworkStepKey.removePre(preKey, langInfoMap.get(helpKey)));
                }
                networkStepReq.setLangInfos(langInfoReqs);
                //是否有帮助文案
                DeviceNetworkStepBase helpStep = helpStepMap.get(stepBase.getNetworkTypeId()+"_"+stepBase.getStep());
                if (helpStep!=null) {
                    NetworkStepHelpReq stepHelpReq = new NetworkStepHelpReq();
                    stepHelpReq.setIcon(helpStep.getIcon());
                    String preHelpKey = NetworkStepKey.getBaseHelpKeyPre(deviceTypeId, stepBase.getNetworkTypeId(), stepBase.getStep());
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
                    networkStepReq.setHelp(stepHelpReq);
                }
                setps.add(networkStepReq);
                stepsMap.put(stepBase.getNetworkTypeId(),setps);
            });
            List<NetworkInfoResp> networkInfos = new ArrayList<>();
            stepsMap.forEach((ntId,steps) ->{
                NetworkInfoResp req = new NetworkInfoResp();
                req.setNetworkTypeId(ntId);
                req.setSteps(steps);
                networkInfos.add(req);
            });
            resp.setNetworkInfos(networkInfos);
        }
        return resp;
    }

    /**
     * @despriction：查询某设备类型支持的配网类型
     * @author  yeshiyuan
     * @created 2018/12/4 13:53
     */
    @Override
    public List<Long> supportNetworkType(Long deviceTypeId) {
        return deviceNetworkStepBaseMapper.supportNetworkType(deviceTypeId);
    }

    /**
     * @despriction：删除设备类型的某种配网类型
     * @author  yeshiyuan
     * @created 2018/12/4 14:04
     */
    @Override
    @Transactional
    public void deleteByNetworkTypes(Long deviceTypeId, List<Long> networkTypeIds) {
        for (Long networkTypeId : networkTypeIds) {
            int i = deviceNetworkStepBaseMapper.deleteByDeviceTypeId(deviceTypeId, networkTypeId);
            if (i > 0) {
                langInfoBaseMapper.deleteNetworkStepLang(LangInfoObjectTypeEnum.deviceNetwork.name(), deviceTypeId, deviceTypeId + "_" + networkTypeId + "_");
            }
        }
    }
}
