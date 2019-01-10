package com.iot.building.index.api.callback;

import java.util.List;

import com.iot.common.helper.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.iot.building.index.api.IndexApi;
import com.iot.building.index.vo.IndexDetailReq;
import com.iot.building.index.vo.IndexDetailResp;
import com.iot.building.index.vo.IndexPageVo;
import com.iot.building.index.vo.IndexReq;
import com.iot.building.index.vo.IndexResp;

import feign.hystrix.FallbackFactory;

/**
 * @Author: Xieby
 * @Date: 2018/9/4
 * @Description: *
 */
@Component
public class IndexApiFallbackFactory implements FallbackFactory<IndexApi> {


    @Override
    public IndexApi create(Throwable throwable) {
        return new IndexApi() {

			@Override
			public void updateIndexContent(IndexReq indexReq) {
			}

			@Override
			public void saveIndexDetail(IndexDetailReq indexDetailReq) {
				
			}

			@Override
			public void updateIndexDetail(IndexDetailReq indexDetailReq) {
				
			}

		/*	@Override
			public List<IndexResp> findCustomPage(IndexPageVo indexPageVo) {
				return null;
			}*/

			@Override
			public void deleteIndexContent(IndexReq indexReq) {
				
			}

			@Override
			public Long saveIndexContent(IndexReq indexReq) {
				return null;
			}

			@Override
			public Page<IndexResp> findCustomIndex(IndexReq indexReq) {
				return null;
			}

			@Override
			public List<IndexDetailResp> findIndexDetailByIndexId(Long tenantId, Long orgId, Long indexContentIdStr) {
				return null;
			}

			@Override
			public void deleteIndexDatailByIndexId(Long tenantId, Long orgId, Long indexContentId) {
				
			}

			@Override
			public IndexResp findIndexContentById(Long tenantId, Long orgId, Long indexContentIdStr) {
				return null;
			}

			@Override
			public void setAllEnableOff(Long tenantId, Long orgId, Long locationId, int enable) {
				
			}

			@Override
			public IndexResp getEnableIndex(Long tenantId, Long orgId, Long locationId) {
				return null;
			}

			@Override
			public List<IndexResp> getInexInfoByLocationId(Long tenantId, Long orgId, Long locationId) {
				return null;
			}

		};
    }
}
