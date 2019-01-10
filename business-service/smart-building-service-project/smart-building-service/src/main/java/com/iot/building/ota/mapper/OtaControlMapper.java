package com.iot.building.ota.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.ota.OtaFileInfoReq;
import com.iot.device.vo.rsp.ota.OtaFileInfoResp;

public interface OtaControlMapper {

	@SelectProvider(type = OtaSqlProvider.class,method = "getOtaFileList")
	List<OtaFileInfoResp> getOtaFileList(@Param("pageReq") OtaPageReq pageReq);
	
	@Insert({
        "insert into ota_file_info (",
        "id, 			    ",
        "version,    ",
        "create_time, 		",
        "update_time,		",
        "create_by, 		",
        "update_by,			",
        "tenant_id,         ",
		"product_id,        ",
		"download_url,         ",
		"md5,         ",
		"org_id,         ",
		"location_id        ",
        ")				    ",
        "values				",
        "( 	    			",
        "#{id},		    	",
        "#{version},	    ",
        "#{createTime},	    ",
        "#{updateTime},	    ",
        "#{createBy},	    ",
        "#{updateBy},	    ",
        "#{tenantId},		",
		"#{productId},		",
		"#{downloadUrl},		",
		"#{md5},		",
		"#{orgId},		",
		"#{locationId}		",
        ")                  "
	})
	int saveOtaFileInfo(OtaFileInfoReq otaFileInfoReq);

	@SelectProvider(type = OtaSqlProvider.class,method = "findOtaFileInfoByProductId")
	OtaFileInfoResp findOtaFileInfoByProductId(OtaFileInfoReq otaFileInfoReq);

	/**
	 * 	更新OTA文件信息
	 *
	 * @param otaFileInfoReq
	 * @return
	 */
	@UpdateProvider(type = OtaSqlProvider.class, method = "updateOtaFileInfo")
	int updateOtaFileInfo(@Param("otaFileInfoReq") OtaFileInfoReq otaFileInfoReq);
}
