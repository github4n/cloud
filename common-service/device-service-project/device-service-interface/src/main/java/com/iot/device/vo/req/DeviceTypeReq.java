package com.iot.device.vo.req;

import com.iot.device.vo.req.DataPointReq.SmartWraper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Data
@ToString
public class DeviceTypeReq {

	private Integer pageNum = 1;

	private Integer pageSize = 10;

	private Long id;
	/**
	 * 类型名称
	 */
    @NotBlank(message = "deviceType.name.notblank")
    private String name;
	/**
	 * 类型描述
	 */
	private String description;
	/**
	 * 设备分类id  extend device_catalog table  id
	 * device_catalog_id
	 */
    @NotNull(message = "deviceType.catalog.notnull")
    private Long deviceCatalogId;

	private String deviceCatalogName;

	/**
	 * create_by
	 */
	private Long createBy;

	/**
	 * update_by
	 */
	private Long updateBy;

	/**
	 * 厂商标识
	 * vender_flag
	 */
	private String venderFlag;

	/**
	 * 圖片Id
	 */
    @NotBlank(message = "deviceType.img.notblank")
    private String img;

	private Long tenantId;

	/**
	 * 设备真实类型
	 */
	private String type;

	@ApiModelProperty(value = "ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)")
	private Integer iftttType;

	private ArrayList<SmartWraper> smart;

	/**
	 * 可以检索 类型名称、deviceCatalogName、设备类型
	 */
	private String searchValues;

	private List<SaveDeviceTypeToGoodsReq> goodsList;


}
