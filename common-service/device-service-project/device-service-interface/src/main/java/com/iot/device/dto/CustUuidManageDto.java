package com.iot.device.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * UUID管理表
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
public class CustUuidManageDto implements Serializable {

	/***/
	private static final long serialVersionUID = -7598165726202105408L;
	
	private Long id;
    /**
     * 批次号
     */
	private String batchNumId;
    /**
     * 生成时间
     */
	private Date createTime;
    /**
     * 客户代码
     */
	private String custCode;
    /**
     * 客户名称
     */
	private String custName;
    /**
     * uuid 类型
     */
	private String uuidType;
    /**
     * 下载次数
     */
	private Integer downLoadedNum;
    /**
     * uuid生成数量
     */
	private Integer uuidAmount;
    /**
     * 生成状态(0：进行中;1：已完成;2：生成失败;3：P2PID不足)
     */
	private Integer status;
    /**
     * UUID列表CVS文件ID
     */
	private String fileId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBatchNumId() {
		return batchNumId;
	}

	public void setBatchNumId(String batchNumId) {
		this.batchNumId = batchNumId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getUuidType() {
		return uuidType;
	}

	public void setUuidType(String uuidType) {
		this.uuidType = uuidType;
	}

	public Integer getDownLoadedNum() {
		return downLoadedNum;
	}

	public void setDownLoadedNum(Integer downLoadedNum) {
		this.downLoadedNum = downLoadedNum;
	}

	public Integer getUuidAmount() {
		return uuidAmount;
	}

	public void setUuidAmount(Integer uuidAmount) {
		this.uuidAmount = uuidAmount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}



}
