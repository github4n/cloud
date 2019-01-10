package com.iot.control.space.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.iot.common.helper.Page;
import com.iot.control.space.domain.Space;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceReqVo;
import com.iot.control.space.vo.SpaceResp;

import java.util.List;
import java.util.Map;

public interface ISpaceService extends IService<Space> {

    /**
     * 新建空间
     *
     * @return
     * @author wanglei
     */
    Long save(SpaceReq spaceReq);

    /**
     * 更新空间
     *
     * @return
     * @author wanglei
     */
    void update(SpaceReq spaceReq);

    /**
     * @Description: 条件更新space
     * setValueParam; //需要修改的对象
     * requstParam;//where查找条件
     * @param reqVo
     * @return:
     * @author: chq
     * @date: 2018/10/15 16:10
     **/
    boolean updateSpaceByCondition(SpaceReqVo reqVo);

    /**
    * @Description: 根据spaceId删除space
    *@param tenantId
    * @param spaceId
    * @return:
    * @author: chq
    * @date: 2018/10/11 19:48
    **/
    boolean deleteSpaceBySpaceId(Long tenantId, Long spaceId);

    /**
    * @Description: 批量删除space
    *
    * @param req
    * @return:
    * @author: chq
    * @date: 2018/10/11 19:49
    **/
    boolean deleteSpaceByIds(SpaceAndSpaceDeviceVo req);

    /**
    * @Description: 通过spaceId查找space详情
    *
    * @param spaceId
    * @return:
    * @author: chq
    * @date: 2018/10/11 19:50
    **/
    SpaceResp findSpaceInfoBySpaceId(Long tenantId, Long spaceId);

    /**
    * @Description: 通过spaceIds查找space详情
    *
    * @param req
    * @return:
    * @author: chq
    * @date: 2018/10/11 19:51
    **/
    List<SpaceResp> findSpaceInfoBySpaceIds(SpaceAndSpaceDeviceVo req);

    /**
    * @Description: 根据父级Id查询space
    *
    * @param space
    * @return:
    * @author: chq
    * @date: 2018/10/12 15:48
    **/
    List<SpaceResp> findSpaceByParentId(SpaceReq space);
    /**
    * @Description: 条件查询space
    *（可选择查询条件id、parent_id、name、user_id、location_id、
     * type、sort、tenant_id、default_space、org_id）
    * @param spaceReq
    * @return:
    * @author: chq
    * @date: 2018/10/11 19:52
    **/
    List<SpaceResp> findSpaceByCondition(SpaceReq spaceReq);


    PageInfo findSpacePageByCondition(SpaceReq spaceReq);

    /**
    * @Description: 通过条件统计space数量
    *（可选择查询条件id、parent_id、name、user_id、location_id、
     * type、sort、tenant_id、default_space、org_id）
    * @param spaceReq
    * @return:
    * @author: chq
    * @date: 2018/10/11 19:54
    **/
    int countSpaceByCondition(SpaceReq spaceReq);
    
    List<Map<String, Object>> findTree(Long locationId, Long tenantId);

    public List<SpaceResp> findChild(Long tenantId, Long spaceId);

}
