package com.iot.tenant.controller;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.tenant.api.LangInfoBaseApi;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.tenant.service.ILangInfoBaseService;
import com.iot.tenant.vo.req.lang.DelLangInfoBaseReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoBasePageReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoBaseReq;
import com.iot.tenant.vo.req.lang.SaveLangInfoBaseReq;
import com.iot.tenant.vo.resp.lang.LangInfoBaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：文案基础信息管理
 * 功能描述：文案基础信息管理
 * 创建人： 李帅
 * 创建时间：2018年9月11日 下午2:31:37
 * 修改人：李帅
 * 修改时间：2018年9月11日 下午2:31:37
 */
@RestController
public class LangInfoBaseController implements LangInfoBaseApi {

	@Autowired
	private ILangInfoBaseService langInfoBaseService;



	/**
	 * @despriction：保存基础文案信息(全删全插)
	 * @author  yeshiyuan
	 * @created 2018/9/29 16:44
	 * @return
	 */
	@Override
	public void saveLangInfoBase(@RequestBody SaveLangInfoBaseReq req) {
		SaveLangInfoBaseReq.checkParam(req);
		langInfoBaseService.saveLangInfoBase(req);
	}

	/**
	 * @despriction：删除基础文案信息
	 * @author  yeshiyuan
	 * @created 2018/9/30 9:41
	 * @return
	 */
	@Override
	public void delLangInfoBase(@RequestBody DelLangInfoBaseReq req) {
		DelLangInfoBaseReq.checkParam(req);
		langInfoBaseService.deleteByObjectIdAndTypeAndKeys(req);
	}

	/**
	 * @despriction：添加基础文案信息（有重复的key会报错）
	 * @author  yeshiyuan
	 * @created 2018/9/30 9:54
	 * @return
	 */
	@Override
	public void addLangInfoBase(@RequestBody SaveLangInfoBaseReq req) {
		SaveLangInfoBaseReq.checkParam(req);
		langInfoBaseService.addLangInfoBase(req);
	}

	/**
	 * @despriction：修改基础文案信息
	 * @author  yeshiyuan
	 * @created 2018/9/30 10:00
	 * @return
	 */
	@Override
	public void updateLangInfoBase(@RequestBody SaveLangInfoBaseReq req) {
		SaveLangInfoBaseReq.checkParam(req);
		langInfoBaseService.updateLangInfoBase(req);
	}

	/**
	 * @despriction：查询对应的基础文案
	 * @author  yeshiyuan
	 * @created 2018/9/30 11:55
	 * @return
	 */
	@Override
	public LangInfoBaseResp queryLangInfoBase(@RequestBody QueryLangInfoBaseReq queryLangInfoBaseReq) {
		if (!LangInfoObjectTypeEnum.checkObjectType(queryLangInfoBaseReq.getObjectType())) {
			throw new BusinessException(TenantExceptionEnum.LANGINFO_OBJECTTYPE_ERROR);
		}
		return langInfoBaseService.queryLangInfoBase(queryLangInfoBaseReq);
	}

	/**
	 * @despriction：分页查询对应的基础文案
	 * @author  yeshiyuan
	 * @created 2018/10/12 13:52
	 * @return
	 */
	@Override
	public Page<LangInfoBaseResp> pageQuery(@RequestBody QueryLangInfoBasePageReq pageReq) {
		if (!LangInfoObjectTypeEnum.checkObjectType(pageReq.getObjectType())) {
			throw new BusinessException(TenantExceptionEnum.LANGINFO_OBJECTTYPE_ERROR);
		}
		return langInfoBaseService.pageQuery(pageReq);
	}
}
