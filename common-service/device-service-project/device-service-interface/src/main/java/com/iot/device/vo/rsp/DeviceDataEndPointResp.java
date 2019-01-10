package com.iot.device.vo.rsp;

import java.io.Serializable;

/**
 * 模块名称：设备
 * 功能描述：设备数据端点（自定义属性）
 * 创建人： zhouzongwei
 * 创建时间：2017年4月10日 上午10:51:52
 * 修改人： zhouzongwei
 * 修改时间：2017年4月10日 上午10:51:52
 */
public class DeviceDataEndPointResp implements Serializable{
	
	/***/
	private static final long serialVersionUID = 1833123561370800976L;

	//数据端点id
	private String epid;
	
	//产品id
	private String productId;
	
	//索引
	private String dataIndex;
	
	//属性
	private String property;
	
	//数据类型
	private String dataType;

	//属性描述
	private String dataDes;
	
	//数据来源（0：设备上报，1：公式计算，2：应用设置）可能对应生成不同JSON
	private String dataSource;
	
	//计算公式
	private String formula;
	
	//取值范围（0-100）
	private String dataRange;
	
	//数据符号，比如（C）
	private String dataSign;
	
	//是否只读（Y,N）
	private String readonly;
	
	
	//是否数组类型
	private String isArrayType;

	public String getEpid() {
		return epid;
	}

	public void setEpid(String epid) {
		this.epid = epid;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDataIndex() {
		return dataIndex;
	}

	public void setDataIndex(String dataIndex) {
		this.dataIndex = dataIndex;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataDes() {
		return dataDes;
	}

	public void setDataDes(String dataDes) {
		this.dataDes = dataDes;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getDataRange() {
		return dataRange;
	}

	public void setDataRange(String dataRange) {
		this.dataRange = dataRange;
	}

	public String getDataSign() {
		return dataSign;
	}

	public void setDataSign(String dataSign) {
		this.dataSign = dataSign;
	}

	public String getReadonly() {
		return readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	public String getIsArrayType() {
		return isArrayType;
	}

	public void setIsArrayType(String isArrayType) {
		this.isArrayType = isArrayType;
	}

	@Override
	public String toString() {
		return "DeviceDataEndPoint [epid=" + epid + ", productId=" + productId
				+ ", dataIndex=" + dataIndex + ", property=" + property
				+ ", dataType=" + dataType + ", dataDes=" + dataDes
				+ ", dataSource=" + dataSource + ", formula=" + formula
				+ ", dataRange=" + dataRange + ", dataSign=" + dataSign
				+ ", readonly=" + readonly + ", isArrayType=" + isArrayType
				+ "]";
	}
}
