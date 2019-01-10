package com.iot.control.camera.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.iot.common.beans.BeanUtil;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.helper.Page;
import com.iot.control.camera.utils.Constants;
import com.iot.control.camera.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.iot.control.camera.mapper.CameraMapper;
import com.iot.control.camera.service.ICameraService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class CameraServiceImpl implements ICameraService{

	@Autowired
	private CameraMapper cameraMapper;
	//@Autowired
	/*private static Environment environment = ApplicationContextHelper.getBean(Environment.class);
	public static String savePath = environment.getProperty(Constants.LOCAL_FILE_CURRENT_FACE_PATH);
	private static String urlPath = environment.getProperty(Constants.LOCAL_FILE_URL_PATH);*/
	//private static String savePath = "C:\\Users\\huangxu\\Desktop\\contorlCenter20180321_0.3\\web\\currentFaceFile\\";
	//private static String urlPath = "http://172.25.11.74:8000/currentface.png";
	private final static Logger log = LoggerFactory.getLogger(CameraServiceImpl.class);
	
	public String save(CameraReq cameraReq) throws Exception {
		Environment environment = ApplicationContextHelper.getBean(Environment.class);
		String savePath = environment.getProperty(Constants.LOCAL_FILE_CURRENT_FACE_PATH);
		String urlPath = environment.getProperty(Constants.LOCAL_FILE_URL_PATH);
		String url = download(urlPath,savePath);
		cameraReq.setUrl(url);
		log.info("start--------------------------------------------------start  face-------------------------------------------------------------------------------");
		cameraMapper.save(cameraReq);
		log.info("end=====================================================end  face ====================================================================");
	    return url;
	}
	
	public CameraResp findCameraProperty(CameraReq cameraReq) {
		CameraResp resp=cameraMapper.findLastOneByIp(cameraReq);
		CameraPropertyVo vo=cameraReq.getVoList().get(0);
		List<CameraPropertyVo> voList=cameraMapper.findPropertyVoList(vo);
		resp.setVoList(voList);
		return resp;
	}

	@Override
	public Page<CameraResp> findCameraList(CameraReq cameraReq,int pageNumber,int pageSize) throws ParseException {
		com.github.pagehelper.PageHelper.startPage(pageNumber, pageSize);
		List<CameraResp> listStr = cameraMapper.findCameraList(cameraReq);
		List<CameraResp> list = Lists.newArrayList();
        for(CameraResp cameraResp:listStr){
			cameraResp.setTime(zhuanhua(cameraResp.getTime()));
			//list.add(cameraResp);
		}
		PageInfo<CameraResp> info = new PageInfo(listStr);
		Page<CameraResp> page = new Page<CameraResp>();
		BeanUtil.copyProperties(info, page);
		page.setResult(info.getList());
		return page;


		/*com.github.pagehelper.PageHelper.startPage(pageNumber, pageSize);
		List<SpaceResp> spaceList = spaceMapper.findSpaceByLocationId(locationId, tenantId, name);
		PageInfo<SpaceResp> info = new PageInfo(spaceList);
		Page<SpaceResp> page = new Page<SpaceResp>();
		BeanUtil.copyProperties(info, page);
		page.setResult(info.getList());
		return page;*/
	}

	@Override
	public CameraPropertyResp count(CameraPropertyReq cameraPropertyReq) {
		CameraPropertyResp cameraPropertyResp = cameraMapper.count(cameraPropertyReq);
		return cameraPropertyResp;
	}

	@Override
	public List<CameraPropertyResp> countEveryHour(CameraPropertyReq cameraPropertyReq) {
		List<CameraPropertyResp> list = cameraMapper.countEveryHour(cameraPropertyReq);
		return list;
	}

	@Override
	public void saveDensity(CameraPropertyReq cameraPropertyReq) {
		log.info("--------------------------------------------------start denstiy-------------------------------------------------------------------------------");
		cameraMapper.saveDensity(cameraPropertyReq);
		log.info("--------------------------------------------------end  denstiy-------------------------------------------------------------------------------");
	}

	@Override
	public void saveRegionHourRecord(CameraPropertyReq cameraPropertyReq) {
		System.out.println("--------------------------------------------------start  hour-------------------------------------------------------------------------------");
		String timeFlag = cameraPropertyReq.getTime().substring(8,10);
		cameraPropertyReq.setTimeFlag(timeFlag);
		//判断该小时点是否有数据，没有就添加，有就更新
		CameraPropertyResp cameraPropertyResp = cameraMapper.findOneHourRecord(cameraPropertyReq);
		if(cameraPropertyResp == null){//添加
			cameraMapper.saveRegionHourRecord(cameraPropertyReq);
			log.info("--------------------------------------------------end save  hour-------------------------------------------------------------------------------");
		}else {//更新，条件timeFlag
			cameraMapper.updateRegionHourRecord(cameraPropertyReq);
			log.info("--------------------------------------------------end update  hour-------------------------------------------------------------------------------");
        }

	}

	@Override
	public CameraPropertyResp newCount() {
		CameraPropertyResp cameraPropertyResp = cameraMapper.newCount();
		return cameraPropertyResp;
	}

	@Override
	public  List<CameraVo>  newFace() {
		List<CameraVo>  list = cameraMapper.newFace();
		return list;
	}


	public  String  zhuanhua(String s) throws ParseException {
		StringBuilder sb = new StringBuilder(s);//构造一个StringBuilder对象
		sb.insert(4, "-").insert(7,"-").insert(10," ").insert(13,":").insert(16,":");
		String str1 = sb.toString();
		return str1;
	}

	public static String download(String urlString,String savePath) throws Exception {
		String filename = Calendar.getInstance().getTimeInMillis()+""+".jpg";
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		//设置请求超时为5s
		con.setConnectTimeout(5*10000);
		// 输入流
		InputStream is = con.getInputStream();

		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		File sf=new File(savePath);
		if(!sf.exists()){
			sf.mkdirs();
		}
		OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
		//String localFileUrl = savePath+filename;
		String localFileUrl = filename;
		System.out.println("localFileUrl:"+localFileUrl);
		return localFileUrl;
	}


}
