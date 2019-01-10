package com.iot.design.project.controller.base;

import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/14
 */
public class BaseController {
    private Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    /**
     * 成功时候的返回
     * @param data
     * @param <T>
     * @return
     */
    public <T> CommonResponse<T> success(T data) {
        return new CommonResponse<T>(ResultMsg.SUCCESS).data(data);
    }

    /**
     * DTO 转 VO
     * @param soucreList
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> coypList(List soucreList, Class<T> clazz) {
        List<T> targerList = new ArrayList<T>();
        if (soucreList != null && soucreList.size() > 0) {
            soucreList.stream().forEach(one -> {
                try {
                    T t = clazz.newInstance();
                    BeanUtils.copyProperties(one, t);
                    targerList.add(t);
                } catch (Exception e) {
                    LOGGER.error("list  change  error");
                    LOGGER.error(e.getMessage());
                }
            });
        }
        return targerList;
    }
}
