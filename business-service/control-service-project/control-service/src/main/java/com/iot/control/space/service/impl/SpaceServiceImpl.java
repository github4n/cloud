package com.iot.control.space.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.control.space.domain.Space;
import com.iot.control.space.exception.SpaceExceptionEnum;
import com.iot.control.space.mapper.SpaceMapper;
import com.iot.control.space.service.ISpaceDeviceService;
import com.iot.control.space.service.ISpaceService;
import com.iot.control.space.service.SpaceCoreServiceUtils;
import com.iot.control.space.util.*;
import com.iot.control.space.vo.*;
import com.iot.file.api.FileApi;
import com.iot.util.AssertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service("spaceService")
@Transactional
public class SpaceServiceImpl extends ServiceImpl<SpaceMapper,Space> implements ISpaceService {

	private final static Logger LOGGER = LoggerFactory.getLogger(SpaceServiceImpl.class);

	@Autowired
	SpaceMapper spaceMapper;

	@Autowired
	ISpaceDeviceService spaceDeviceService;

	@Autowired
	private FileApi fileApi;

	/**
	 * 新建空间
	 *
	 * @param spaceReq
	 * @return
	 * @author wanglei
	 */
	@Override
	public Long save(SpaceReq spaceReq) {
			Space space = BeanCopyUtil.spaceReqToSpace(spaceReq);
			super.insert(space);
			// 放入space对象缓存,有效期7天
			Long spaceId = space.getId();
			// 删除space列表，保存对象
			SpaceCacheCoreUtils.saveOrDeleteCacheSpace(space, true);
			return spaceId;
	}

	/**
	 * 更新空间
	 *
	 * @param spaceReq
	 * @return
	 * @author wanglei
	 */
	@Override
	public void update(SpaceReq spaceReq) {
		Space space = BeanCopyUtil.spaceReqToSpace(spaceReq);
		String Icon=spaceReq.getIcon();
		String oldIcon = null;
		Long id = spaceReq.getId();
		Long tenantId = spaceReq.getTenantId();
		Space spaceCache = SpaceBaseCacheUtils.getCacheSpace(tenantId, id);
		List list = new ArrayList();
		list.add(space.getId());
		Long parentId = space.getParentId();
		if (parentId != null && parentId != -1){
			list.add(space.getParentId());
		}
		if (spaceCache != null) {
			oldIcon = spaceCache.getIcon();
		} else {
            oldIcon = super.selectById(spaceReq.getId()).getIcon();
		}
		//删除旧的Icom
		if(StringUtil.isNotEmpty(Icon)&&!"null".equals(Icon)) {
			try {
				fileApi.deleteObject(oldIcon);
			} catch (Exception e) {
				LOGGER.error("delete is erro");
			}
		}
        SpaceBaseCacheUtils.deleteCacheSpaceDevices(tenantId, list);
		SpaceCacheCoreUtils.saveOrDeleteCacheSpace(space, false);
        super.updateById(space);
	}

	/**
	* @Description: 条件更新space
	* setValueParam; //需要修改的对象
     * requstParam;//where查找条件
	* @param reqVo
	* @return:
	* @author: chq
	* @date: 2018/10/15 16:10
	**/
	@Override
    public boolean updateSpaceByCondition(SpaceReqVo reqVo){

        AssertUtils.notNull(reqVo, "spaceDeviceReq.notnull");
        AssertUtils.notNull(reqVo.getSetValueParam(), "setValueParam.notnull");
        AssertUtils.notNull(reqVo.getRequstParam(), "requestParam.notnull");
        SpaceReq setValueParam = reqVo.getSetValueParam();
        SpaceReq space = reqVo.getRequstParam();
        Space setValue = BeanCopyUtil.spaceReqToSpace(setValueParam);
        Long tenantId = space.getTenantId();
        EntityWrapper wrapper = new EntityWrapper();
        if(space.getId() != null){
            wrapper.eq("id", space.getId());
        }
        if(space.getSpaceIds() != null){
            wrapper.in("id", space.getSpaceIds());
        }
		if(space.getPosition() != null){
			wrapper.eq("position", space.getPosition());
		}
		if(space.getName() != null){
			wrapper.like("name", space.getName());
		}
        if(space.getParentId() != null){
            wrapper.eq("parent_id", space.getParentId());
        }
        if(space.getUserId() != null){
            wrapper.eq("user_id", space.getUserId());
        }
        if(space.getCreateBy() != null){
            wrapper.eq("create_by", space.getCreateBy());
        }
        if(space.getLocationId() != null){
            wrapper.eq("location_id", space.getLocationId());
        }
        if(space.getType() != null){
            wrapper.eq("type", space.getType());
        }
        if(space.getSort() != null){
            wrapper.eq("sort", space.getSort());
        }
        if(space.getStyle() != null){
            wrapper.eq("style", space.getStyle());
        }
        if(space.getTenantId() != null){
            wrapper.eq("tenant_id", space.getTenantId());
        }
        if(space.getDefaultSpace() != null){
            wrapper.eq("default_space", space.getDefaultSpace());
        }
        if(space.getOrgId() != null){
            wrapper.eq("org_id", space.getOrgId());
        }
        if(space.getModel() != null){
            wrapper.eq("model", space.getModel());
        }
        if(space.getSeq() != null){
            wrapper.eq("seq", space.getSeq());
        }
        if(space.getDeployId() != null){
            wrapper.eq("deploy_id", space.getDeployId());
        }
        List<Space> spaces = super.selectList(wrapper);
        List<Long> spaceIds = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(spaces)){
            spaces.forEach( s-> {
                spaceIds.add(s.getId());
                Long parentId = s.getParentId();
				if (parentId != null && parentId != -1) {
					spaceIds.add(s.getParentId());
				}
            });

            //可能是修改了房间 所以也要清原有的房间缓存
            if (setValueParam.getId() != null) {
                spaceIds.add(setValueParam.getId());
            }
            Long setParentId = setValueParam.getParentId();
            if(setParentId != null && setParentId != -1){
                spaceIds.add(setValueParam.getParentId());
            }

        }
        Space setSpce = BeanCopyUtil.spaceReqToSpace(setValueParam);
        spaces.add(setSpce);
        SpaceBaseCacheUtils.deleteCacheSpaceDevices(tenantId, spaceIds);
		SpaceCacheCoreUtils.saveOrDeleteCacheSpaces(spaces, false);
		return super.update(setValue,wrapper);
    }

	/**
	 * @Description: 通过Id删除spsce
	 *@param tenantId
	 * @param spaceId
	 * @return: boolean
	 * @author: chq
	 * @date: 2018/10/11 16:22
	 **/
	@Override
	public boolean deleteSpaceBySpaceId(Long tenantId, Long spaceId){
		AssertUtils.notNull(spaceId, "spaceId.notnull");
		AssertUtils.notNull(tenantId, "tenantId.notnull");
		//查找是否存在，并删除相对应的缓存
		SpaceResp resp = findSpaceInfoBySpaceId(tenantId, spaceId);
		if (resp == null) {
			throw new BusinessException(SpaceExceptionEnum.SPACE_IS_NULL);
		}
		Space space = BeanCopyUtil.spaceRespToSpace(resp);
		// 将space缓存从redis删除
		SpaceCacheCoreUtils.saveOrDeleteCacheSpace(space, false);
		return super.deleteById(space.getId());
	}

	/**
	 * @Description: 批量删除space
	 *
	 * @param req
	 * @return: boolean
	 * @author: chq
	 * @date: 2018/10/9 15:05
	 **/
	@Override
	public boolean deleteSpaceByIds(SpaceAndSpaceDeviceVo req){
		AssertUtils.notNull(req, "spaceIds.notnull");
		List<Long> spaceIds = req.getSpaceIds();
		Long tenantId = req.getTenantId();
		//查找是否存在，并删除相对应的缓存
		List<SpaceResp> resps = findSpaceInfoBySpaceIds(req);
		if (resps == null) {
			throw new BusinessException(SpaceExceptionEnum.SPACE_IS_NULL);
		}
		List<Space> spaces = BeanCopyUtil.spaceRespListToSpaceList(resps);
		// 将space缓存从redis删除
		SpaceCacheCoreUtils.saveOrDeleteCacheSpacesAndList(spaces, false);
		SpaceBaseCacheUtils.deleteCacheSpaceDevices(tenantId, spaceIds);
		return super.deleteBatchIds(spaceIds);
	}
	/**
	 * @Description: 通过spaceId获取space详情
	 *@param tenantId
	 * @param spaceId
	 * @return: com.iot.control.space.vo.SpaceResp
	 * @author: chq
	 * @date: 2018/10/11 17:18
	 **/
	@Override
	public SpaceResp findSpaceInfoBySpaceId(Long tenantId, Long spaceId) {
		// 缓存获取
		Space space = SpaceBaseCacheUtils.getCacheSpace(tenantId, spaceId);
		if (space == null) {
			space = super.selectById(spaceId);
			SpaceBaseCacheUtils.updateCacheSpace(space);
		}
		SpaceResp spaceResp = BeanCopyUtil.spaceToSpaceResp(space);
		return spaceResp;
	}

	@Override // 房间列表获取
	public List<SpaceResp> findSpaceByParentId(SpaceReq space) {
		AssertUtils.notNull(space, "space.notnull");
		AssertUtils.notNull(space.getParentId(), "space.parentId.notnull");
		AssertUtils.notNull(space.getTenantId(), "space.tenantId.notnull");
		Long parentId = space.getParentId();
		int offset = space.getOffset(); // 接口协议：从第0页开始传
		int pageSize = space.getPageSize();
		Long tenantId = space.getTenantId();
		Long userId = space.getUserId();
		// 缓存房间列表的获取
		List<Long> roomIdlist = SpaceCacheCoreUtils.getCacheSpaceListByPage(offset, pageSize, tenantId, parentId, userId);
		// 房间详情列表的获取
		List<Space> spaceList = SpaceCoreServiceUtils.getSpaceInfoListBySpaceIds(tenantId, roomIdlist);

		List<SpaceResp> spaceRespList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(spaceList)) {
			Collections.sort(spaceList, new AesSpaceComparator());
			for (Space spaceVo : spaceList) {
				// 一间房间的设备列表
				List<SpaceDeviceVo> deviceList = spaceDeviceService.findSpaceDeviceVOBySpaceId(tenantId, spaceVo.getId());
				Integer devNum = 0;
				if (CollectionUtils.isNotEmpty(deviceList)) {
					devNum = deviceList.size();
				}
				SpaceResp spaceResp = BeanCopyUtil.spaceToSpaceResp(spaceVo);
				spaceResp.setDevNum(devNum);
				spaceRespList.add(spaceResp);
			}
		}
		// 从数据库查数据，并保存至redis
		if (CollectionUtils.isEmpty(spaceRespList)) {
			com.github.pagehelper.PageHelper.startPage(space.getOffset(), space.getPageSize());
			spaceRespList = spaceMapper.findSpaceByParentId(tenantId, parentId);// 房间详情列表

			List<Space> spacelist = BeanCopyUtil.spaceRespListToSpaceList(spaceRespList);
			// 保存space对象，并将相对应的spaceId加入相对应的spaceList,将devicelist放入相对应的spaceDevice
			SpaceCacheCoreUtils.saveOrDeleteCacheSpacesAndList(spacelist, true);
		}
		return spaceRespList;
	}

	/**
	 * @Description: 批量获取空间详细信息
	 *
	 * @param req
	 * @return: java.util.List<com.iot.control.space.vo.SpaceResp>
	 * @author: chq
	 * @date: 2018/10/9 15:31
	 **/
	@Override
	public List<SpaceResp> findSpaceInfoBySpaceIds(SpaceAndSpaceDeviceVo req){
		AssertUtils.notNull(req, "SpaceAndSpaceDeviceVo.notnull");
		AssertUtils.notNull(req.getSpaceIds(), "spaceIds.notnull");
		List<Long> spaceIds = req.getSpaceIds();
		Long tenantId = req.getTenantId();
		List<Space> spaceList = SpaceCoreServiceUtils.getSpaceInfoListBySpaceIds(tenantId, spaceIds);
		return BeanCopyUtil.spaceListToSpaceRespList(spaceList);
	}

	/**
	 * @Description: 条件查询space
	 *
	 * @param spaceReq
	 * @return: java.util.List<com.iot.control.space.vo.SpaceResp>
	 * @author: chq
	 * @date: 2018/10/9 17:29
	 **/
	@Override
	public List<SpaceResp> findSpaceByCondition(SpaceReq spaceReq){
		AssertUtils.notNull(spaceReq, "spaceReq.notnull");
		EntityWrapper<Space> wrapper = new EntityWrapper<>();
		if(spaceReq.getId() != null){
			wrapper.eq("id", spaceReq.getId());
		}
		if (CollectionUtils.isNotEmpty(spaceReq.getSpaceIds())) {
			wrapper.in("id", spaceReq.getSpaceIds());
		}
		if(spaceReq.getParentId() != null){
			wrapper.eq("parent_id", spaceReq.getParentId());
		}
		if(spaceReq.getName() != null){
			wrapper.like("name", spaceReq.getName());
		}
		if(spaceReq.getPosition() != null){
			wrapper.eq("position", spaceReq.getPosition());
		}
		if(spaceReq.getUserId() != null){
			wrapper.eq("user_id", spaceReq.getUserId());
		}
		if(spaceReq.getLocationId() != null){
			wrapper.eq("location_id", spaceReq.getLocationId());
		}
		if(spaceReq.getType() != null){
			wrapper.eq("type", spaceReq.getType());
		}
		if(spaceReq.getSort() != null){
			wrapper.eq("sort", spaceReq.getSort());
		}
		if(spaceReq.getTenantId() != null){
			wrapper.eq("tenant_id", spaceReq.getTenantId());
		}
		if(spaceReq.getDefaultSpace() != null){
			wrapper.eq("default_space", spaceReq.getDefaultSpace());
		}
		if(spaceReq.getOrgId() != null){
			wrapper.eq("org_id", spaceReq.getOrgId());
		}
		LOGGER.info("spaceReq.getReorder"+spaceReq.getReorder());
		if(spaceReq.getReorder() != null && spaceReq.getReorder()){
			wrapper.orderBy(true, "`name`",true);
		}
		List<Space> spaces = super.selectList(wrapper);
		return BeanCopyUtil.spaceListToSpaceRespList(spaces);
	}

	@Override
    public PageInfo findSpacePageByCondition(SpaceReq spaceReq){
        AssertUtils.notNull(spaceReq, "spaceReq.notnull");
        int pageSize = spaceReq.getPageSize();
		int pageNum = spaceReq.getPageNumber();
		PageInfo page = new PageInfo<>();
		page.setPageSize(pageSize);
		page.setPageNum(pageNum);
        if (pageNum < 1 || pageSize <= 0 || pageSize > 100) {
            throw new BusinessException(SpaceExceptionEnum.SPACE_PAGE_ILLEGAL);
        }
        EntityWrapper<Space> wrapper = new EntityWrapper<>();
        if(spaceReq.getId() != null){
            wrapper.eq("id", spaceReq.getId());
        }
        if(spaceReq.getParentId() != null){
            wrapper.eq("parent_id", spaceReq.getParentId());
        }
        if(spaceReq.getName() != null){
            wrapper.like("name", spaceReq.getName());
        }
        if(spaceReq.getPosition() != null){
            wrapper.eq("position", spaceReq.getPosition());
        }
        if(spaceReq.getUserId() != null){
            wrapper.eq("user_id", spaceReq.getUserId());
        }
        if(spaceReq.getLocationId() != null){
            wrapper.eq("location_id", spaceReq.getLocationId());
        }
        if(spaceReq.getType() != null){
            wrapper.eq("type", spaceReq.getType());
        }
        if(spaceReq.getSort() != null){
            wrapper.eq("sort", spaceReq.getSort());
        }
        if(spaceReq.getTenantId() != null){
            wrapper.eq("tenant_id", spaceReq.getTenantId());
        }
        if(spaceReq.getDefaultSpace() != null){
            wrapper.eq("default_space", spaceReq.getDefaultSpace());
        }
        if(spaceReq.getOrgId() != null){
            wrapper.eq("org_id", spaceReq.getOrgId());
        }

        if(spaceReq.getReorder() != null && spaceReq.getReorder()){
			wrapper.orderBy(true, "type,`name`",true);
		}
		com.baomidou.mybatisplus.plugins.Page selectPage = new com.baomidou.mybatisplus.plugins.Page(pageNum, pageSize);

        selectPage  = super.selectPage(selectPage, wrapper);
		List<SpaceResp> resps = BeanCopyUtil.spaceListToSpaceRespList(selectPage.getRecords());
		page.setList(resps);
		page.setPages(selectPage.getPages());
		page.setTotal(selectPage.getTotal());
        return page;
    }

	@Override
	public int countSpaceByCondition(SpaceReq spaceReq){
		AssertUtils.notNull(spaceReq, "spaceReq.notnull");
		EntityWrapper<Space> wrapper = new EntityWrapper<>();
		if(spaceReq.getId() != null){
			wrapper.eq("id", spaceReq.getId());
		}
		if(spaceReq.getParentId() != null){
			wrapper.eq("parent_id", spaceReq.getParentId());
		}
		if(spaceReq.getName() != null){
			wrapper.eq("name", spaceReq.getName());
		}
		if(spaceReq.getUserId() != null){
			wrapper.eq("user_id", spaceReq.getUserId());
		}
		if(spaceReq.getLocationId() != null){
			wrapper.eq("location_id", spaceReq.getLocationId());
		}
		if(spaceReq.getType() != null){
			wrapper.eq("type", spaceReq.getType());
		}
		if(spaceReq.getSort() != null){
			wrapper.eq("sort", spaceReq.getSort());
		}
		if(spaceReq.getTenantId() != null){
			wrapper.eq("tenant_id", spaceReq.getTenantId());
		}
		if(spaceReq.getDefaultSpace() != null){
			wrapper.eq("default_space", spaceReq.getDefaultSpace());
		}
		if(spaceReq.getOrgId() != null){
			wrapper.eq("org_id", spaceReq.getOrgId());
		}
		return super.selectCount(wrapper);
	}


	// 家庭列表获取
	public List<SpaceResp> findSpaceByType(SpaceReq spaceReq) {
		// 先从缓存去取数据，若为空，则从数据库取
		int offset = spaceReq.getOffset();
		int pageSize = spaceReq.getPageSize();
		Long userId = spaceReq.getUserId();
		Long tenantId = spaceReq.getTenantId();
		// 缓存家庭列表的获取
		List<Long> homeIdlist = SpaceCacheCoreUtils.getCacheSpaceListByPage(offset, pageSize, tenantId,null, userId);
		// 家庭详情列表的获取
		List<Space> spaceList = SpaceCoreServiceUtils.getSpaceInfoListBySpaceIds(tenantId, homeIdlist);
		// 从数据库查数据，并保存至redis
		if (CollectionUtils.isEmpty(spaceList)) {
			Space space = new Space();
			space.setUserId(userId);
			space.setType(spaceReq.getType());
			space.setTenantId(spaceReq.getTenantId());
			if (spaceReq.getDefaultSpace() != null) {
				space.setDefaultSpace(spaceReq.getDefaultSpace());
			}
			spaceList = spaceMapper.findSpaceByCondition(space);
			// 保存至redis
			SpaceCacheCoreUtils.saveOrDeleteCacheSpacesAndList(spaceList, true);
		}
		return BeanCopyUtil.spaceListToSpaceRespList(spaceList);
	}

	@Override
	public List<Map<String, Object>> findTree(Long locationId, Long tenantId) {
		List<SpaceResp> spaceList = findRootByLocationIdAndTenantId(locationId, tenantId);
		List<Map<String, Object>> mapList = BeanChangeUtil.spaceToMapList(spaceList);
		findChild(tenantId, mapList);
		return mapList;
	}
	
	/**
	 * 根据用户查询顶级空间
	 *
	 * @param locationId
	 * @return
	 * @author wanglei
	 */
	public List<SpaceResp> findRootByLocationIdAndTenantId(Long locationId, Long tenantId) {
		SpaceReq spaceReq = new SpaceReq();
		spaceReq.setLocationId(locationId);
		spaceReq.setTenantId(tenantId);
		spaceReq.setParentId(-1L);
		spaceReq.setReorder(true);
		List<SpaceResp> spaceList = findSpaceByCondition(spaceReq);
		return spaceList;
	}
	
	/**
	 * 根据space 查询子空间，包括下级的下级 撸到底
	 */
	public List<SpaceResp> findChild(Long tenantId, Long spaceId) {
		SpaceReq space=new SpaceReq();
		space.setParentId(spaceId);
		space.setTenantId(tenantId);
		List<SpaceResp> list=findSpaceByCondition(space);
		if(spaceId.compareTo(-1L)!=0) {
			SpaceResp resp=findSpaceInfoBySpaceId(tenantId, spaceId);
			list.add(resp);
		}
		List<SpaceResp> backList = new ArrayList<>();
		findSpaceChild(backList, list);
		return backList;
	}
	
	/**
	 * 递归查询子节点
	 *
	 * @param list
	 * @return
	 */
	public void findSpaceChild(List<SpaceResp> backList, List<SpaceResp> list) {
		if (!list.isEmpty()) {
			for (SpaceResp resp: list) {
				Long id = resp.getId();
				SpaceReq space = new SpaceReq();
				space.setTenantId(resp.getTenantId());
				space.setParentId(id);
				List<SpaceResp> childList = findSpaceByCondition(space);
				backList.addAll(childList);
				findSpaceChild(backList,childList);
			}
		}
	}
	
	/**
	 * 递归查询子节点
	 *
	 * @param list
	 * @return
	 */
	public void findChild(Long tenantId, List<Map<String, Object>> list) {
		if (!list.isEmpty()) {
			for (Map<String, Object> mapSpace : list) {
				Long id = Long.parseLong(mapSpace.get("id").toString());
				SpaceReq space = new SpaceReq();
				space.setTenantId(tenantId);
				space.setParentId(id);
				List<Map<String, Object>> childList = spaceListToListMap(findSpaceByCondition(space));
				mapSpace.put("child", childList);
				findChild(tenantId, childList);
			}
		}
	}

	private List<Map<String, Object>> spaceListToListMap(List<SpaceResp> spaceList) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		String deployName = null;
		if (CollectionUtils.isNotEmpty(spaceList)) {
			for (SpaceResp resp : spaceList) {
				mapList.add(BeanChangeUtil.spaceToMap(resp, deployName));
			}
		}
		return mapList;
	}

}
