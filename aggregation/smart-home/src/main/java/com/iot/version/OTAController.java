package com.iot.version;

import com.iot.version.vo.DevVersionPageVo;
import com.iot.version.vo.DevVersionVo;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:33 2018/4/28
 * @Modify by:
 */
@Api("ota接口")
@RestController
@RequestMapping("/ota")
public class OTAController {

    /**
     * 添加ota设备版本信息
     *
     * @param versionVo
     */
    @RequestMapping(value = "/addDevVersion", method = RequestMethod.POST)
    public void addDevVersion(@RequestBody DevVersionVo versionVo) {

    }

    /**
     * 获取ota版本 分页列表
     *
     * @param pageVo
     */
    @RequestMapping(value = "/findDevVersionPage", method = RequestMethod.POST)
    public void findDevVersionPage(@RequestBody DevVersionPageVo pageVo) {

    }

    /**
     * 下载ota信息
     */
    @RequestMapping(value = "/downloadVersion", method = RequestMethod.GET)
    public void downloadVersion(HttpServletRequest request, HttpServletResponse response) {

    }
}
