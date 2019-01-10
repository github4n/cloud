package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.iot.common.util.CommonUtil;
import com.iot.device.mapper.GenerateModuleMapper;
import com.iot.device.model.GenerateModule;
import com.iot.device.service.IGenerateModuleService;
import com.iot.device.vo.req.GenerateModuleReq;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 模组表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Component
public class GenerateModuleServiceImpl extends ServiceImpl<GenerateModuleMapper, GenerateModule> implements IGenerateModuleService {


    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateModuleServiceImpl.class);

    @Autowired
    private GenerateModuleMapper generateModuleMapper;


    @Override
    public String generateModuleId(GenerateModuleReq generateModuleReq) {
        Integer check = 32 - generateModuleReq.getDeviceTypeInfo().length();
        String checkCode = generateModuleReq.getDeviceTypeInfo();
        for (int i = 0; i < check; i++){
            checkCode += "0";
        }
        Long tem = Long.parseLong(checkCode, 2);
        String checkCodeResult = Long.toHexString(tem).toUpperCase();
        GenerateModule generateModule = new GenerateModule();
        generateModule.setCustomer(generateModuleReq.getCustomer());
        generateModule.setDeviceTypeName(generateModuleReq.getDeviceTypeName());
        generateModule.setAgreement(generateModuleReq.getAgreement());
        generateModule.setGF("F");
        generateModule.setDeviceTypeInfo(generateModuleReq.getDeviceTypeInfo());
        generateModule.setGN("N");
        String code = generateModule.getCustomer() + generateModule.getDeviceTypeName() + generateModule.getAgreement() + generateModule.getGF() + checkCodeResult +
                generateModule.getGN();
        generateModule.setCode(code);
        List<GenerateModule> list = generateModuleMapper.listMaxByCode(generateModule.getCode());
        if (list.size()>0 && list!=null){
            list.forEach(m->{
                generateModule.setNumber(m.getNumber()+1);
            });
        } else {
            generateModule.setNumber(new Long(1));
        }
        generateModule.setCreateBy(SaaSContextHolder.getCurrentUserId());
        generateModule.setCreateTime(new Date());
        generateModule.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        generateModule.setUpdateTime(new Date());
        String result = "";
        if (3 - generateModule.getNumber().toString().length() == 0){
            result = generateModule.getNumber().toString();
        } else {
            result = String.format("%03d",generateModule.getNumber());
        }
        generateModule.setCodeNumber(generateModule.getCode()+result);
        generateModule.setDescribes(generateModuleReq.getDescribes());
        if (generateModule.getCodeNumber().length()>32){
            return "0";
        } else {
            super.insert(generateModule);
            return generateModule.getCodeNumber();
        }
    }


    @Override
    public PageInfo generateModuleList(GenerateModuleReq generateModuleReq) {
        Page<GenerateModule> page = new Page<>(CommonUtil.getPageNum(generateModuleReq),CommonUtil.getPageSize(generateModuleReq));
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.orderDesc(Arrays.asList("create_time"));
        List list = generateModuleMapper.selectPage(page,wrapper);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(list);
        pageInfo.setTotal(page.getTotal());
        pageInfo.setPages(page.getPages());
        pageInfo.setPageSize(page.getSize());
        pageInfo.setPageNum(generateModuleReq.getPageNum());
        return pageInfo;
    }
}
