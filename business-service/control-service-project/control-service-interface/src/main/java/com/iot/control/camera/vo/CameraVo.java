package com.iot.control.camera.vo;

import java.util.Date;
import java.util.List;

public class CameraVo {

	private Long id;
	
	private String ip;
	
	private String name;
	
	private String title;
	
	private String time;
	
	private String register;
	
	private String url;

	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	private List<CameraPropertyVo> voList;
	
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
