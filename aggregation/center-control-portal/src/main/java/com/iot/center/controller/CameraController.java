package com.iot.center.controller;

import com.iot.building.camera.api.CameraApi;
import com.iot.building.camera.vo.CameraVo;
import com.iot.building.camera.vo.req.CameraPropertyReq;
import com.iot.building.camera.vo.req.CameraReq;
import com.iot.building.camera.vo.resp.CameraPropertyResp;
import com.iot.building.camera.vo.resp.CameraResp;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController()
@RequestMapping("/CameraController")
public class CameraController {

    @Autowired
    private CameraApi cameraApi;

    /**
     * 获得人脸的集合
     * @param request
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/findCameraList", method = RequestMethod.POST)
    public CommonResponse<Page<CameraVo>> findCameraList(HttpServletRequest request, @RequestParam(value = "name",required = false) String name, @RequestParam(value = "pageNumber") int pageNumber, @RequestParam(value = "pageSize")  int pageSize)  throws ParseException {
        CameraReq cameraReq = new CameraReq();
        cameraReq.setName(name);
        Page<CameraVo> pageVO = new Page<>();
        Page<CameraResp> page = cameraApi.findCameraList(cameraReq, pageNumber, pageSize);
        BeanUtil.copyProperties(page, pageVO);
        List<CameraVo> voList = new ArrayList<CameraVo>();
        changeResultPage(page, pageVO, voList);
        return CommonResponse.success(pageVO);
    }

    /**
     * 统计一整天的 数据
     * @param request
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    public CommonResponse<CameraPropertyResp> count(HttpServletRequest request) throws ParseException {
        Calendar cal = Calendar.getInstance();
        String monthStr = null;
        String dayStr = null;
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH)+1;//获取月份
        int day=cal.get(Calendar.DATE);//获取日
        CameraPropertyReq cameraPropertyReq = new CameraPropertyReq();
        if(month<10){
            monthStr = "0"+month;
        }else {
            monthStr = month+"";
        }
        if(day<10){
            dayStr = "0"+day;
        }else {
            dayStr = day + "";
        }
        cameraPropertyReq.setTime(year+""+monthStr+dayStr);
        //一整天的数据
        CameraPropertyResp cameraPropertyResp = cameraApi.count(cameraPropertyReq);
        return CommonResponse.success(cameraPropertyResp);
    }

    /*public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        String monthStr = null;
        String dayStr = null;
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH)+1;//获取月份
        int day=cal.get(Calendar.DATE);//获取日
        if(month<10){
            monthStr = "0"+month;
        }else {
            monthStr = month+"";
        }
        if(day<10){
            dayStr = "0"+day;
        }else {
            dayStr = day + "";
        }
        System.out.println(year);
        System.out.println(month);
        System.out.println(day);
        System.out.println(year+""+monthStr+dayStr);
    }*/


    /**
     * 统计最新 人群密度 的 数据
     * @param request
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/newCount", method = RequestMethod.POST)
    public CommonResponse<CameraPropertyResp> newCount(HttpServletRequest request) throws ParseException {
        //统计最新 的 数据
        CameraPropertyResp cameraPropertyResp = cameraApi.newCount();
        return CommonResponse.success(cameraPropertyResp);
    }

    /**
     * 获取最新的人脸的接口
     * @param request
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/newFace", method = RequestMethod.POST)
    public CommonResponse<List<CameraVo>> newFace(HttpServletRequest request) throws ParseException {
        //统计最新 的 数据
        List<CameraVo> list = cameraApi.newFace();
        return CommonResponse.success(list);
    }

    /**
     * 统计一整天每个小时的 数据
     * @param request
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/countEveryHour", method = RequestMethod.POST)
    public CommonResponse<List<CameraPropertyResp>> countEveryHour(HttpServletRequest request) throws ParseException {
        Calendar cal = Calendar.getInstance();
        String monthStr = null;
        String dayStr = null;
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH)+1;//获取月份
        int day=cal.get(Calendar.DATE);//获取日
        if(month<10){
            monthStr = "0"+month;
        }else {
            monthStr = month+"";
        }
        if(day<10){
            dayStr = "0"+day;
        }else {
            dayStr = day + "";
        }
        CameraPropertyReq cameraPropertyReq = new CameraPropertyReq();
        cameraPropertyReq.setTime(year+""+monthStr+dayStr);
        //一整天每个小时的数据
        List<CameraPropertyResp> resutlList = cameraApi.countEveryHour(cameraPropertyReq);
        CommonResponse<List<CameraPropertyResp>> result = new CommonResponse<>(ResultMsg.SUCCESS,resutlList);
        return result;
    }


    private void changeResultPage(Page<CameraResp> page, Page<CameraVo> pageVO, List<CameraVo> voList) {
        if (!page.getResult().isEmpty()) {
            for (CameraResp deviceResp : (List<CameraResp>) page.getResult()) {
                CameraVo locationSceneVo = new CameraVo();
                changeDeviceRespToDeviceVO(deviceResp, locationSceneVo);
                voList.add(locationSceneVo);
            }
        }
        pageVO.setResult(voList);
    }

    private CameraResp changeDeviceRespToDeviceVO(CameraResp deviceResp,CameraVo locationSceneVo) {
        if (deviceResp != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            BeanUtil.copyProperties(deviceResp, locationSceneVo);
           /* if (deviceResp.getCreateTime() != null) {
                String createTime = simpleDateFormat.format(deviceResp.getCreateTime());
                locationSceneVo.setCreateTime(deviceResp.getCreateTime());
            }*/
			/*if (deviceResp.getLastUpdateDate() != null) {
				String updateTime = simpleDateFormat.format(deviceResp.getLastUpdateDate());
				deviceVO.setLastUpdateDate(updateTime);
			}*/
        }
        return deviceResp;
    }

}
