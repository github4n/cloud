package com.iot.building.camera.vo.req;

import com.iot.building.camera.vo.CameraPropertyVo;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import java.util.List;

public class CameraReq {

	private Long id;
	
	private String ip;
	
	private String name;
	
	private String title;
	
	private String time;
	
	private String register;
	
	private String url;

	private String path;
	
	private MultipartFile multipartFile;
	
	private List<CameraPropertyVo> voList;

	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	public List<CameraPropertyVo> getVoList() {
		return voList;
	}

	public void setVoList(List<CameraPropertyVo> voList) {
		this.voList = voList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRegister() {
		return register;
	}

	public void setRegister(String register) {
		this.register = register;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
