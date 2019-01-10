package com.iot.favorite.service;


import com.iot.common.beans.CommonResponse;
import com.iot.control.favorite.vo.FavoriteReq;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Service
public interface FavoriteService {

    Map<String, Object> getFavoriteList(Long tenantId,Long userId,Long homeId);

    void addFavorite(FavoriteReq req);

    Boolean getFavoriteStatus(FavoriteReq req);

    public void cancelFavorite(@RequestBody FavoriteReq req);
}
