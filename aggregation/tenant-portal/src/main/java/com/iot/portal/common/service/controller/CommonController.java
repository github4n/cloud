package com.iot.portal.common.service.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.NumberUtil;
import com.iot.common.util.SecurityUtil;
import com.iot.common.util.StringUtil;
import com.iot.portal.constant.TenantConstant;
import com.iot.portal.exception.BusinessExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：公共服务控制层
 * 创建人： maochengyuan
 * 创建时间：2018/7/24 15:20
 * 修改人： maochengyuan
 * 修改时间：2018/7/24 15:20
 * 修改描述：
 */
@Controller
public class CommonController {

    private final static Logger logger = LoggerFactory.getLogger(CommonController.class);

    /**
     * 描述：加密主键
     * @author maochengyuan
     * @created 2018/7/26 10:30
     * @param primaryKey 主键
     * @return java.lang.String
     */
    protected String encryptKey(Long primaryKey) {
        if(primaryKey == null){
            return "";
        }
        return SecurityUtil.EncryptByAES(primaryKey.toString(), TenantConstant.SECURITY_KEY);
    }

    /**
     * 描述：解密主键
     * @author maochengyuan
     * @created 2018/7/26 10:30
     * @param primaryKey 主键
     * @param filedName 字段名
     * @return java.lang.Long
     */
    protected Long decryptKey(String primaryKey, String filedName) {
        if(StringUtil.isEmpty(primaryKey)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, filedName+" is null");
        }
        try {
            String key = SecurityUtil.DecryptAES(primaryKey, TenantConstant.SECURITY_KEY);
            return NumberUtil.toLong(key);
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, filedName+" incorrect format");
        }
    }

    /**
     * 描述：判断产品id是否为空
     * @author maochengyuan
     * @created 2018/9/14 10:37
     * @param productId 产品ID
     * @return void
     */
    protected void checkProductId(Long productId) {
        if(CommonUtil.isEmpty(productId)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, " productId is null");
        }
    }

    /**
     * 描述：判断appId是否为空
     * @author maochengyuan
     * @created 2018/9/14 10:37
     * @param appId appId
     * @return void
     */
    protected void checkAppId(Long appId) {
        if(CommonUtil.isEmpty(appId)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, " appId is null");
        }
    }

    /**
     * 描述：判断appId是否为空
     * @author maochengyuan
     * @created 2018/9/14 10:37
     * @param goodsId appId
     * @return void
     */
    protected Long checkAppId(String goodsId) {
        return this.decryptKey(goodsId, "goodsId");
    }

    /** 
     * 描述：提供二维码下载接口
     * @author maochengyuan
     * @created 2018/9/21 16:34
     * @param req HttpServletRequest
     * @param map 参数
     * @return java.lang.String
     */
    @LoginRequired(value = Action.Skip)
    @RequestMapping("/download")
    public String downloadApp(HttpServletRequest req, HashMap<String, Object> map) {
        map.put("ios", req.getParameter("ios"));
        map.put("android", req.getParameter("android"));
        return "download";
    }

}
