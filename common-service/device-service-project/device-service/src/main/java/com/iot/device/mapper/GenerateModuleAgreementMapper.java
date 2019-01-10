package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.GenerateModule;
import com.iot.device.model.GenerateModuleAgreement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 模组表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Mapper
public interface GenerateModuleAgreementMapper extends BaseMapper<GenerateModuleAgreement> {

}
