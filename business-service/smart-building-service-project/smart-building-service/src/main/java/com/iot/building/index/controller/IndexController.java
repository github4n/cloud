package com.iot.building.index.controller;

import java.util.List;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.iot.common.helper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.iot.building.index.api.IndexApi;
import com.iot.building.index.service.IIndexService;
import com.iot.building.index.vo.IndexDetailReq;
import com.iot.building.index.vo.IndexDetailResp;
import com.iot.building.index.vo.IndexPageVo;
import com.iot.building.index.vo.IndexReq;
import com.iot.building.index.vo.IndexResp;

/**
 * @Author: wl
 * @Date: 2018/10/12
 * @Description: *
 */
@RestController
public class IndexController implements IndexApi {

	@Autowired
	private IIndexService indexService;
	
	@Override
	public Long saveIndexContent(@RequestBody IndexReq indexReq) {
		if(indexReq.getId() == null){
			//执行保存
			indexService.saveIndexContent(indexReq);
			return indexReq.getId();
		}else {
			//执行修改
			indexService.updateIndexContent(indexReq);
			return indexReq.getId();
		}
	}

	@Override
	public void updateIndexContent(@RequestBody IndexReq indexReq) {
		indexService.updateIndexContent(indexReq);
	}

	@Override
	public void saveIndexDetail(@RequestBody IndexDetailReq indexDetailReq) {
		indexService.saveIndexDetail(indexDetailReq);
	}

	@Override
	public void updateIndexDetail(@RequestBody IndexDetailReq indexDetailReq) {
		indexService.updateIndexDetail(indexDetailReq); 
	}

	/*@Override
	public List<IndexResp> findCustomPage(@RequestBody IndexPageVo indexPageVo) {
		return indexService.findCustomIndex(indexPageVo.getIndexReq());
	}*/

	@Override
	public List<IndexDetailResp> findIndexDetailByIndexId(Long tenantId,Long orgId,Long indexContentIdStr) {
		return indexService.findIndexDetailByIndexId(indexContentIdStr);
	}

	@Override
	public void deleteIndexDatailByIndexId(Long tenantId,Long orgId,Long indexContentId) {
		indexService.deleteIndexDatailByIndexId(indexContentId);
	}

	@Override
	public void deleteIndexContent(@RequestBody  IndexReq indexReq) {
		indexService.deleteIndexContent(indexReq);
	}

	@Override
	public IndexResp findIndexContentById(Long tenantId,Long orgId,Long indexContentIdStr) {
		return indexService.findIndexContentById(indexContentIdStr);
	}

	@Override
	public void setAllEnableOff(Long tenantId,Long orgId,Long locationId,int enable) {
		indexService.setAllEnableOff(tenantId,orgId,locationId,enable);
	}

	@Override
	public IndexResp getEnableIndex(Long tenantId,Long orgId,Long locationId) {
		return indexService.getEnableIndex(tenantId,orgId,locationId);
	}

	@Override
	public List<IndexResp> getInexInfoByLocationId(Long tenantId,Long orgId,Long locationId) {
		return indexService.getInexInfoByLocationId(tenantId,orgId,locationId);
	}

	@Override
	public Page<IndexResp> findCustomIndex(@RequestBody IndexReq indexReq) {
		return indexService.findCustomIndex(indexReq);
	}

}
