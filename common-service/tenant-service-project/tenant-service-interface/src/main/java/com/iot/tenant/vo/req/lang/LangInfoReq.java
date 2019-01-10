package com.iot.tenant.vo.req.lang;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.tenant.enums.LangTypeEnum;
import com.iot.tenant.exception.TenantExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：文案信息服务实例
 * 功能描述：文案信息服务实例
 * 创建人： 李帅
 * 创建时间：2018年9月11日 下午2:03:20
 * 修改人：李帅
 * 修改时间：2018年9月11日 下午2:03:20
 */
@ApiModel(description = "文案信息实例")
public class LangInfoReq implements Serializable {

	private static final long serialVersionUID = -3013623855825681845L;

	@ApiModelProperty(name = "key", value = "国际化键", dataType = "String")
    private String key;

	@ApiModelProperty(name = "val", value = "国际化内容(key:语言类型，value：对应文字)", dataType = "Map")
    private Map<String, String> val;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, String> getVal() {
		return val;
	}

	public void setVal(Map<String, String> val) {
		this.val = val;
	}

	/**
	  * @despriction：校验语言类型（只能是默认语言类型）
	  * @author  yeshiyuan
	  * @created 2018/10/8 16:08
	  * @return
	  */
	public static void checkDefaultVal(LangInfoReq req){
		for (Map.Entry<String,String> entry: req.getVal().entrySet()) {
			if (!LangTypeEnum.checkDefaultLang(entry.getKey())){
				throw new BusinessException(TenantExceptionEnum.LANGINFO_LANGTYPE_ERROR, "(key: "+ req.getKey()+")");
			}
			if (StringUtil.isBlank(entry.getValue())) {
				throw new BusinessException(TenantExceptionEnum.LANGINFO_CONTENT_NULL, "(key: "+ req.getKey()+")");
			}
		}
	}

	public static void checkTenantVal(LangInfoReq req){
		for (Map.Entry<String,String> entry: req.getVal().entrySet()) {
			if (!LangTypeEnum.checkLangType(entry.getKey())){
				throw new BusinessException(TenantExceptionEnum.LANGINFO_LANGTYPE_ERROR, "(key: "+ req.getKey()+")");
			}
		}
	}

}
