package com.iot.boss.service.technicalrelate.impl;

import com.iot.boss.exception.BossExceptionEnum;
import com.iot.boss.service.technicalrelate.TechnicalRelateService;
import com.iot.boss.vo.technicalrelate.BossNetworkTypeInfoResp;
import com.iot.boss.vo.technicalrelate.TechnicalAndNetworkResp;
import com.iot.boss.vo.technicalrelate.TechnicalListResp;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.CommonUtil;
import com.iot.device.api.NetworkTypeApi;
import com.iot.device.api.TechnicalRelateApi;
import com.iot.device.dto.NetworkTypeDto;
import com.iot.device.vo.NetworkTypeVo;
import com.iot.device.vo.req.NetworkTypePageReq;
import com.iot.device.vo.req.technical.SaveDeviceTechnicalReq;
import com.iot.device.vo.rsp.NetworkTypeResp;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.enums.goods.GoodsTypeEnum;
import com.iot.saas.SaaSContextHolder;
import com.iot.system.api.DictApi;
import com.iot.system.api.LangApi;
import com.iot.system.vo.DictItemResp;
import com.iot.tenant.api.DeviceNetworkStepBaseApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：技术方案相关功能
 * 功能描述：技术方案相关功能
 * 创建人： 李帅
 * 创建时间：2018年10月15日 上午10:02:15
 * 修改人：李帅
 * 修改时间：2018年10月15日 上午10:02:15
 */
@Service("technicalRelateService")
public class TechnicalRelateServiceImpl implements TechnicalRelateService {

    @Autowired
    private TechnicalRelateApi technicalRelateApi;

    @Autowired
    private NetworkTypeApi networkTypeApi;

    @Autowired
    private GoodsServiceApi goodsServiceApi;

    @Autowired
    private LangApi langApi;

    @Autowired
    private DictApi dictApi;

    @Autowired
    private DeviceNetworkStepBaseApi deviceNetworkStepBaseApi;

    /**
     * 
     * 描述：查询配网模式信息
     * @author 李帅
     * @created 2018年10月15日 上午10:01:28
     * @since 
     * @param networkTypeId
     * @return
     */
    @Override
    public NetworkTypeDto getNetworkInfo(Long networkTypeId) {
        //在Header中获取数据
//        Long userId = SaaSContextHolder.getCurrentUserId();
//        Long tenantId = SaaSContextHolder.currentTenantId();
        return technicalRelateApi.getNetworkInfo(networkTypeId);
    }
    
    /**
     * 
     * 描述：更新配网模式信息
     * @author 李帅
     * @created 2018年10月15日 上午10:04:53
     * @since 
     * @param networkTypeVo
     */
    @Override
    public void updateNetworkInfo(NetworkTypeVo networkTypeVo) {
    	networkTypeVo.setCreateBy(SaaSContextHolder.getCurrentUserId());
    	technicalRelateApi.updateNetworkInfo(networkTypeVo);
    }

    /**
     * @despriction：分页查询配网类型
     * @author  yeshiyuan
     * @created 2018/10/15 17:26
     * @return
     */
    @Override
    public Page<BossNetworkTypeInfoResp> queryNetworkType(NetworkTypePageReq pageReq) {
        Page<NetworkTypeResp> page = networkTypeApi.page(pageReq);
        List<BossNetworkTypeInfoResp> list = new ArrayList<>();
        if (page.getResult() != null) {
            List<GoodsInfo> goodsInfos = this.goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.UUID.getCode());
            Map<Long, GoodsInfo> goodsInfoMap = goodsInfos.stream().collect(Collectors.toMap(GoodsInfo::getId, a->a));
            Set<String> goodsName = new HashSet<>();
            goodsInfos.forEach(o ->{
                goodsName.add(o.getGoodsName());
            });
            Map<String, String> nameMap =  this.langApi.getLangValueByKey(goodsName, LocaleContextHolder.getLocale().toString());
            page.getResult().forEach(networkTypeResp -> {
                List<Long> technicalIds = networkTypeResp.getTechnicalIds();
                BossNetworkTypeInfoResp resp = new BossNetworkTypeInfoResp(networkTypeResp.getId(), networkTypeResp.getNetworkName(), networkTypeResp.getDescription(), networkTypeResp.getTypeCode());
                List<String> names = new ArrayList<>();
                technicalIds.forEach(id -> {
                    names.add(nameMap.get(goodsInfoMap.get(id).getGoodsName()));
                });
                resp.setTechnicalNameList(names);
                list.add(resp);
            });
        }
        Page<BossNetworkTypeInfoResp> mypage = new Page<>();
        BeanUtil.copyProperties(page, mypage);
        mypage.setResult(list);
        return mypage;
    }

    /**
     * @despriction：加载所有的技术方案
     * @author  yeshiyuan
     * @created 2018/10/15 17:44
     * @return
     */
    @Override
    public List<TechnicalListResp> getAllTechnical() {
        List<TechnicalListResp> resps = new ArrayList<>();
        List<GoodsInfo> list = this.goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.UUID.getCode());
        if(CommonUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        Set<String> goodsName = new HashSet<>();
        list.forEach(o ->{
            goodsName.add(o.getGoodsName());
        });
        Map<String, String> nameMap =  this.langApi.getLangValueByKey(goodsName, LocaleContextHolder.getLocale().toString());
        list.forEach(o -> {
            TechnicalListResp resp = new TechnicalListResp();
            resp.setId(o.getId());
            resp.setName(nameMap.get(o.getGoodsName()));
            resps.add(resp);
        });
        return resps;
    }

    /**
     * @despriction：保存设备支持的技术方案
     * @author  yeshiyuan
     * @created 2018/10/15 19:44
     * @return
     */
    @Override
    public void saveDeviceTechnical(SaveDeviceTechnicalReq req) {
        technicalRelateApi.saveDeviceTechnical(req);
        List<Long> oldNetworkTypeIds = deviceNetworkStepBaseApi.supportNetworkType(req.getDeviceTypeId());
        List<Long> newNetworkTypeIds = req.getNetworkTypeIds();
        List<Long> delNetworkTypeIds = oldNetworkTypeIds.stream().filter(networkTypeId -> !newNetworkTypeIds.contains(networkTypeId)).collect(Collectors.toList());
        if (delNetworkTypeIds!=null && !delNetworkTypeIds.isEmpty()) {
            deviceNetworkStepBaseApi.deleteByNetworkTypes(req.getDeviceTypeId(), delNetworkTypeIds);
        }
    }

    /**
     * @despriction：查询设备支持的技术方案
     * @author  yeshiyuan
     * @created 2018/10/15 20:10
     * @return
     */
    @Override
    public TechnicalAndNetworkResp queryTechnicalAndNetwork(Long deviceTypeId) {
        TechnicalAndNetworkResp technicalAndNetworkResp = new TechnicalAndNetworkResp();
        List<TechnicalListResp> resps = new ArrayList<>();
        List<Long> ids = technicalRelateApi.queryDeviceTechnicalIds(deviceTypeId);
        if (ids != null && !ids.isEmpty()) {
            List<GoodsInfo> list = this.goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.UUID.getCode());
            Map<Long, GoodsInfo> goodsInfoMap = list.stream().collect(Collectors.toMap(GoodsInfo::getId, a->a));
            Set<String> goodsName = new HashSet<>();
            ids.forEach(id -> {
                goodsName.add(goodsInfoMap.get(id).getGoodsName());
            });
            Map<String, String> nameMap =  this.langApi.getLangValueByKey(goodsName, LocaleContextHolder.getLocale().toString());
            ids.forEach(id -> {
                TechnicalListResp resp = new TechnicalListResp();
                resp.setId(id);
                resp.setName(nameMap.get(goodsInfoMap.get(id).getGoodsName()));
                resps.add(resp);
            });
        }
        List<NetworkTypeResp> networkTypeResps = technicalRelateApi.deviceSupportNetworkType(deviceTypeId);
        technicalAndNetworkResp.setNetworkTypes(networkTypeResps);
        technicalAndNetworkResp.setTechnicals(resps);
        return technicalAndNetworkResp;
    }

    /**
     * @despriction：查询设备类型支持的配网模式
     * @author  yeshiyuan
     * @created 2018/10/16 10:41
     * @return
     */
    @Override
    public List<NetworkTypeResp> deviceSupportNetwork(Long deviceTypeId) {
        return technicalRelateApi.deviceSupportNetworkType(deviceTypeId);
    }

    /**
     * @despriction：技术方案支持的配网模式
     * @author  yeshiyuan
     * @created 2018/10/16 16:15
     * @return
     */
    @Override
    public List<NetworkTypeResp> findNetworkByTechnicalIds(List<Long> technicalIds) {
        return technicalRelateApi.findNetworkByTechnicalIds(technicalIds);
    }

    /**
     * 描述：加载所有配网code
     * @author maochengyuan
     * @created 2018/11/22 19:58
     * @param
     * @return java.util.List<com.iot.system.vo.DictItemResp>
     */
    @Override
    public List<DictItemResp> getAllTypeCode(){
        Map<String, DictItemResp> temp = this.dictApi.getDictItem((short) 15);
        return new ArrayList<>(temp.values());
    }

    @Override
    public List<TechnicalListResp> getDeviceTechnical(Long deviceTypeId) {
        List<Long> technicalIds = technicalRelateApi.queryDeviceTechnicalIds(deviceTypeId);
        if (technicalIds.isEmpty()) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "this device type no config technical scheme");
        }
        List<GoodsInfo> list = this.goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.UUID.getCode());
        if(CommonUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        Map<Long, GoodsInfo> goodsInfoMap = list.stream().collect(Collectors.toMap(GoodsInfo::getId, a->a));
        Set<String> goodsName = new HashSet<>();
        technicalIds.forEach(id -> {
            if (goodsInfoMap.containsKey(id)) {
                goodsName.add(goodsInfoMap.get(id).getGoodsName());
            }
        });
        if (goodsName.size() == 0) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "this device type support technical scheme not exists");
        }
        Map<String, String> nameMap = this.langApi.getLangValueByKey(goodsName, LocaleContextHolder.getLocale().toString());
        List<TechnicalListResp> temp = new ArrayList<>();
        technicalIds.forEach(id -> {
            GoodsInfo o = goodsInfoMap.get(id);
            TechnicalListResp resp = new TechnicalListResp(o.getId(),nameMap.get(o.getGoodsName()));
            temp.add(resp);
        });
        return temp;
    }
}
