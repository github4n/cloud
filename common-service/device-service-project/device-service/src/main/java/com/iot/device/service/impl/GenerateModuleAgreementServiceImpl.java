package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.iot.common.util.CommonUtil;
import com.iot.device.mapper.GenerateModuleAgreementMapper;
import com.iot.device.mapper.GenerateModuleMapper;
import com.iot.device.model.GenerateModule;
import com.iot.device.model.GenerateModuleAgreement;
import com.iot.device.service.IGenerateModuleAgreementService;
import com.iot.device.service.IGenerateModuleService;
import com.iot.device.vo.req.GenerateModuleReq;
import com.iot.device.vo.rsp.GenerateModuleAgreementRsp;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
public class GenerateModuleAgreementServiceImpl extends ServiceImpl<GenerateModuleAgreementMapper, GenerateModuleAgreement> implements IGenerateModuleAgreementService {


    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateModuleAgreementServiceImpl.class);

    @Autowired
    private GenerateModuleAgreementMapper generateModuleAgreementMapper;

    @Override
    public List<GenerateModuleAgreementRsp> generateModuleAgreementList() {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.orderDesc(Arrays.asList("create_time"));
        List<GenerateModuleAgreement> list = super.selectList(wrapper);
        List<GenerateModuleAgreementRsp> result = new ArrayList<>();
        list.forEach(m->{
            GenerateModuleAgreementRsp generateModuleAgreementRsp = new GenerateModuleAgreementRsp();
            generateModuleAgreementRsp.setId(m.getId());
            generateModuleAgreementRsp.setName(m.getName());
            generateModuleAgreementRsp.setAbbreviationName(m.getAbbreviationName());
            generateModuleAgreementRsp.setCreateBy(m.getCreateBy());
            generateModuleAgreementRsp.setCreateTime(m.getCreateTime());
            generateModuleAgreementRsp.setUpdateBy(m.getUpdateBy());
            generateModuleAgreementRsp.setUpdateTime(m.getUpdateTime());
            generateModuleAgreementRsp.setDescription(m.getDescription());
            result.add(generateModuleAgreementRsp);
        });
        return result;
    }
}
