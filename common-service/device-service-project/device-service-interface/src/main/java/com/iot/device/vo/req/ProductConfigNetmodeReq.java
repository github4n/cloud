package com.iot.device.vo.req;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Data
@ToString
public class ProductConfigNetmodeReq {


	private Long id;

	private Long productId;
	
	private String name;

	private Long createBy;

	private Date createTime;

	private Long updateBy;

	private Date updateTime;


}
