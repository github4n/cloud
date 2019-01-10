package com.iot.boss.vo.product.req;

import com.iot.util.ToolUtils;
import lombok.Data;
import lombok.ToString;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class ProductExportReq {

    private Long tenantId;

    private Long productId;

    private List<Long> productIds;

    private String filePath = "/tmp";

    private String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis()) + ".json";

}
