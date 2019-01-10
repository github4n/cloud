package com.iot.building.index.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.iot.building.index.vo.IndexDetailReq;
import com.iot.building.index.vo.IndexDetailResp;
import com.iot.building.index.vo.IndexReq;
import com.iot.building.index.vo.IndexResp;
import com.iot.common.helper.Page;


public interface IIndexService {
	
    Long saveIndexContent(IndexReq indexReq);

    void updateIndexContent(IndexReq indexReq);

    void saveIndexDetail(IndexDetailReq indexDetailReq);

    void updateIndexDetail(IndexDetailReq indexDetailReq);

    Page<IndexResp> findCustomIndex(IndexReq indexReq);

    List<IndexDetailResp> findIndexDetailByIndexId(Long indexContentIdStr);

    void deleteIndexDatailByIndexId(Long indexContentId);

    void deleteIndexContent(IndexReq indexReq);

    IndexResp findIndexContentById(Long indexContentIdStr);

    void setAllEnableOff(Long tenantId,Long orgId,Long locationId,int enable);

    IndexResp getEnableIndex(Long tenantId,Long orgId,Long locationId);
    
    public List<IndexResp> getInexInfoByLocationId(Long tenantId,Long orgId,Long locationId);

}
