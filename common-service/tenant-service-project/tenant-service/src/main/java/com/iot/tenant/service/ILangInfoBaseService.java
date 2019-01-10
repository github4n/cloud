package com.iot.tenant.service;

import com.iot.common.helper.Page;
import com.iot.tenant.vo.req.lang.DelLangInfoBaseReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoBasePageReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoBaseReq;
import com.iot.tenant.vo.req.lang.SaveLangInfoBaseReq;
import com.iot.tenant.vo.resp.lang.LangInfoBaseResp;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：文案基础信息service
 * 创建人： yeshiyuan
 * 创建时间：2018/9/29 16:48
 * 修改人： yeshiyuan
 * 修改时间：2018/9/29 16:48
 * 修改描述：
 */
public interface ILangInfoBaseService {

    /**
      * @despriction：保存文案基础信息（模板）
      * @author  yeshiyuan
      * @created 2018/9/29 16:49
      * @return
      */
    void saveLangInfoBase(SaveLangInfoBaseReq req);

    /**
      * @despriction：删除文案通过主键集合
      * @author  yeshiyuan
      * @created 2018/9/30 9:45
      * @return
      */
    int deleteByIds(List<Long> ids);

    /**
     * @despriction：添加基础文案信息（重复key则报错）
     * @author  yeshiyuan
     * @created 2018/9/30 9:54
     * @return
     */
    void addLangInfoBase(SaveLangInfoBaseReq req);

    /**
      * @despriction：修改基础文案信息
      * @author  yeshiyuan
      * @created 2018/9/30 10:37
      * @return
      */
    void updateLangInfoBase(SaveLangInfoBaseReq req);

    /**
      * @despriction：描述
      * @author  yeshiyuan
      * @created 2018/9/30 13:38
      * @return
      */
    LangInfoBaseResp queryLangInfoBase(QueryLangInfoBaseReq queryLangInfoBaseReq);

    /**
      * @despriction：删除基础文案
      * @author  yeshiyuan
      * @created 2018/10/10 15:35
      * @return
      */
    int deleteByObjectIdAndTypeAndKeys(DelLangInfoBaseReq delLangInfoBaseReq);

    /**
     * @despriction：分页查询对应的基础文案
     * @author  yeshiyuan
     * @created 2018/10/12 13:52
     * @return
     */
    Page<LangInfoBaseResp> pageQuery(QueryLangInfoBasePageReq pageReq);
}
