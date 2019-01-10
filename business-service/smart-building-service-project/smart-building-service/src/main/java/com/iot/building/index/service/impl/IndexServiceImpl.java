package com.iot.building.index.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iot.common.helper.Page;
import com.iot.user.api.UserApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.iot.building.index.domain.Index;
import com.iot.building.index.mapper.IndexMapper;
import com.iot.building.index.service.IIndexService;
import com.iot.building.index.vo.IndexDetailReq;
import com.iot.building.index.vo.IndexDetailResp;
import com.iot.building.index.vo.IndexReq;
import com.iot.building.index.vo.IndexResp;
import com.iot.common.beans.BeanUtil;

@Service("indexService")
@Transactional
public class IndexServiceImpl  implements IIndexService {

	private final static Logger LOGGER = LoggerFactory.getLogger(IndexServiceImpl.class);

	@Autowired
	IndexMapper indexMapper;
	@Autowired
    UserApi userApi;

    @Override
    public Long saveIndexContent(IndexReq indexReq) {
        return indexMapper.saveIndexContent(indexReq);
    }

    @Override
    public void updateIndexContent(IndexReq indexReq) {
    	indexMapper.updateIndexContent(indexReq);
    }

    @Override
    public void saveIndexDetail(IndexDetailReq indexDetailReq) {
    	indexMapper.saveIndexDetail(indexDetailReq);
    }

    @Override
    public void updateIndexDetail(IndexDetailReq indexDetailReq) {
    	indexMapper.updateIndexDetail(indexDetailReq);
    }

    @Override
    public Page<IndexResp> findCustomIndex(IndexReq indexReq) {
        PageHelper pageHelper = new PageHelper();
        pageHelper.startPage(indexReq.getPageNumber(), indexReq.getPageSize());
        List<IndexResp> list = indexMapper.findCustomIndex(indexReq);
        for(IndexResp indexResp : list){
            indexResp.setUserName(userApi.getUser(indexResp.getCreateBy()).getUserName());
        }
        PageInfo<IndexResp> info = new PageInfo(list);
        Page<IndexResp> pageResult = new Page<>();
        BeanUtil.copyProperties(info, pageResult);
        pageResult.setResult(info.getList());
        return pageResult;
    }

    @Override
    public List<IndexDetailResp> findIndexDetailByIndexId(Long indexContentIdStr) {
        List<IndexDetailResp> list = indexMapper.findIndexDetailByIndexId(indexContentIdStr);
        return list;
    }

    @Override
    public void deleteIndexDatailByIndexId(Long indexContentId) {
    	indexMapper.deleteIndexDatailByIndexId(indexContentId);
    }

    @Override
    public void deleteIndexContent(IndexReq indexReq) {
    	indexMapper.deleteIndexContent(indexReq);
    }

    @Override
    public IndexResp findIndexContentById(Long indexContentIdStr) {
        IndexResp indexResp = indexMapper.findIndexContentById(indexContentIdStr);
        return indexResp;
    }

    @Override
    public void setAllEnableOff(Long tenantId,Long orgId,Long locationId,int enable) {
    	indexMapper.setAllEnableOff(tenantId,orgId,locationId,enable);
    }

    @Override
    public IndexResp getEnableIndex(Long tenantId,Long orgId,Long locationId) {
        IndexResp indexResp = indexMapper.getEnableIndex(tenantId,orgId,locationId);
        return indexResp;
    }
    
    @Override
    public List<IndexResp> getInexInfoByLocationId(Long tenantId,Long orgId,Long locationId) {
		List<IndexResp> indexList = indexMapper.getInexInfoByLocationId(tenantId,orgId,locationId);
		return indexList;
	}

	private IndexResp indexToIndexResp(Index index) {
		IndexResp resp = new IndexResp();
		BeanUtil.copyProperties(index, resp);
		return resp;
	}
}
