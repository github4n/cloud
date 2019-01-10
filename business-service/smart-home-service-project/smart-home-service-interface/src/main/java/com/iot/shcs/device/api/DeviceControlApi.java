//package com.iot.shcs.device.api;
//
//import com.alibaba.fastjson.JSONObject;
//import com.iot.common.beans.CommonResponse;
//import com.iot.common.exception.BusinessException;
//import com.iot.device.vo.req.ota.OtaFileInfoReq;
//import com.iot.shcs.device.vo.AttrGetReq;
//import com.iot.shcs.device.vo.ControlReq;
//import com.iot.shcs.device.vo.DevBindNotifVo;
//import com.iot.shcs.device.vo.DeviceVoReq;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//import java.util.Map;
//
//@Api("情景接口")
//@FeignClient(value = "control-service")
//@RequestMapping("/device")
//public interface DeviceControlApi {
//
//	@ApiOperation("alexa设备控制")
//	@RequestMapping(value = "/singleDeviceControlForAlexa", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public CommonResponse singleDeviceControlForAlexa(@RequestBody ControlReq controlVo) throws BusinessException;
//
//	@ApiOperation("设备单控")
//    @RequestMapping(value = "/singleControl", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public CommonResponse singleControl(@RequestBody ControlReq controlVo) throws BusinessException;
//
//	@ApiOperation("报警设备sos模式取消")
//	@RequestMapping(value = "/cancelSos", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public Boolean cancelSos(@RequestBody ControlReq controlVo) throws BusinessException;
//
//	@ApiOperation("获取设备属性值")
//    @RequestMapping(value = "/getDeviceAttr", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public Object getDeviceAttr(@RequestBody AttrGetReq req) throws BusinessException;
//
//	@ApiOperation(value = "获取网关下的所有子设备")
//    @RequestMapping(value = "/getDeviceList", method = RequestMethod.GET)
//    public void getDeviceList(@RequestParam("deviceId") String deviceId) throws Exception;
//
//	@ApiOperation(value = "添加设备记录")
//    @RequestMapping(value = "/saveDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public void saveDevice(@RequestBody DeviceVoReq deviceReq) throws BusinessException;
//
//	@ApiOperation(value = "获取设备内存属性")
//	@RequestMapping(value = "/getDeviceProerty", method = RequestMethod.GET)
//	public Map<String,Object> getDeviceProerty(@RequestParam("deviceId") String deviceId) throws BusinessException;
//
//	@ApiOperation(value = "挂载初始化数据")
//	@RequestMapping(value = "/initMountInfo", method = RequestMethod.GET)
//	public void initMountInfo(@RequestParam("tenantId") Long tenantId) throws BusinessException;
//
//	@ApiOperation("获取设备属性值")
//	@RequestMapping(value = "/getDeviceAttrs", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public Object getDeviceAttrs(@RequestBody AttrGetReq req) throws BusinessException;
//
//	@ApiOperation("添加用户和设备的策略")
//	@RequestMapping(value = "/addAcls", method = RequestMethod.GET)
//	int addAcls(@RequestParam("deviceId") String deviceId);
//
//    @ApiOperation("设备排序")
//    @RequestMapping(value = "/sortDev", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//    Boolean sortDev(@RequestBody List<String> deviceIds);
//
//    @ApiOperation("获取传感器设备属性值")
//    @RequestMapping(value = "/getSensorDevsAttrs", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public JSONObject getSensorDevsAttrs(@RequestBody AttrGetReq req);
//
//	/*@ApiOperation(value = "保存excel的数据")
//	@RequestMapping(value = "/businessTypeDataImport",method = RequestMethod.GET)
//	Boolean businessTypeDataImport(@RequestParam(value = "list")List<String[]> list, @RequestParam(value = "tenantId")Long tenantId);*/
//
//	@ApiOperation("获取用户和设备的绑定消息通知数据")
//	@RequestMapping(value = "/getDevBindNotifMqttMsg", method = RequestMethod.GET)
//	public DevBindNotifVo getDevBindNotifMqttMsg(@RequestParam("userId") String userId, @RequestParam("devId") String devId, @RequestParam("tenantId") Long tenantId);
//
//	@ApiOperation("ToB添加设备的策略")
//	@RequestMapping(value = "/addAclsToB", method = RequestMethod.GET)
//	int addAclsToB(@RequestParam("deviceId") String deviceId);
//
//	@ApiOperation("是否有子设备")
//	@RequestMapping(value = "/deviceId", method = RequestMethod.GET)
//    Boolean hasChlid(String deviceId);
//
//	@ApiOperation(value = "更新ota包")
//    @RequestMapping(value = "/updateOtaVersion", method = RequestMethod.GET)
//    void updateOtaVersion(@RequestParam("deviceId") String deviceId, @RequestParam("fileName") String fileName, @RequestParam("tenantId") Long tenantId, @RequestParam("locationId") Long locationId);
//
//	@ApiOperation("下载云端OTA文件")
//    @RequestMapping(value = "/downLoadOtaFile", method = RequestMethod.POST)
//    void downLoadOtaFile(@RequestBody OtaFileInfoReq otaFileInfoReq);
//
//}
