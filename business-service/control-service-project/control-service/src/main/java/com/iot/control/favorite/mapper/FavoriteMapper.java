package com.iot.control.favorite.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.control.favorite.entity.FavoriteEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 最喜爱设备及场景表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2019-01-02
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<FavoriteEntity> {
}
