package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.iot.device.mapper.sql.ProductSqlProvider;
import com.iot.device.model.*;
import com.iot.device.vo.req.product.ProductAuditPageReq;
import com.iot.device.vo.req.product.ProductConfirmReleaseReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.product.ProductAuditListResp;
import com.iot.device.vo.rsp.product.ProductMinimumSubsetResp;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Mapper
public interface ProductConfigNetmodeMapper extends BaseMapper<ProductConfigNetmode> {

}