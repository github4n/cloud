package com.iot.shcs.module.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: lucky
 * @Descrpiton: if 事件方法属性集合
 * @Date: 11:44 2018/10/25
 * @Modify by:
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListIfInfoResp {
    private Long id;

    //event/action/property
    private String type;

    //
    private String name;

    //
    private String code;
}
