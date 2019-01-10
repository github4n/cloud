package com.iot.portal.packagemanager.service;

import com.iot.portal.packagemanager.vo.req.CreatePackageReq;

/**
  * @despriction：套包创建service
  * @author  yeshiyuan
  * @created 2018/12/10 19:45
  */
public interface IPackageCreateService {

    /**
     * @despriction：保存套包基础信息
     * @author  yeshiyuan
     * @created 2018/11/24 10:01
     */
    Long createPackage(CreatePackageReq packageReq);

}
