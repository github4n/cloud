package com.iot.system.api;

import com.iot.system.api.fallback.DictApiFallbackFactory;
import com.iot.system.vo.DictItemResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Map;

@Api("字典表接口")
@FeignClient(value = "system-service", fallbackFactory = DictApiFallbackFactory.class)
@RequestMapping("/dict")
public interface DictApi {

    /**
     * 描述：依据字典表类别获取字典表明细
     * @author maochengyuan
     * @created 2018/7/5 11:27
     * @param typeId 字典表类别
     * @return java.util.Map<java.lang.Short,com.iot.user.vo.DictItemResp>
     */
    @ApiOperation("依据字典表类别获取字典表明细")
    @RequestMapping(value = "/getDictItem", method = RequestMethod.GET)
    Map<String, DictItemResp> getDictItem(@RequestParam("typeId") Short typeId);

    /**
     * 描述：依据字典表类别获取字典表名称
     * @author maochengyuan
     * @created 2018/7/5 9:53
     * @param typeId 字典表类别
     * @return java.util.Map<java.lang.Short,java.lang.String>
     */
    @ApiOperation("依据字典表类别获取字典表名称")
    @RequestMapping(value = "/getDictItemNames", method = RequestMethod.GET)
    Map<String, String> getDictItemNames(@RequestParam("typeId") Short typeId);

    /**
     * 描述：依据字典表类别获取字典表描述
     * @author maochengyuan
     * @created 2018/7/5 9:53
     * @param typeId 字典表类别
     * @return java.util.Map<java.lang.Short,java.lang.String>
     */
    @ApiOperation("依据字典表类别获取字典表描述")
    @RequestMapping(value = "/getDictItemDescs", method = RequestMethod.GET)
    Map<String, String> getDictItemDescs(@RequestParam("typeId") Short typeId);

    /**
     * 描述：依据字典表类别获取字典表名称（批量）
     * @author maochengyuan
     * @created 2018/7/5 9:53
     * @param typeIds 字典表类别
     * @return java.util.Map<java.lang.Short,java.util.Map<java.lang.Short,java.lang.String>>
     */
    @ApiOperation("依据字典表类别获取字典表名称（批量）")
    @RequestMapping(value = "/getDictItemNamesBatch", method = RequestMethod.GET)
    Map<Short, Map<String, String>> getDictItemNamesBatch(@RequestParam("typeIds") Collection<Short> typeIds);

}
