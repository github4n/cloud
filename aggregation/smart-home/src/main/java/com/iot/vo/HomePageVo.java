package com.iot.vo;

import com.iot.shcs.space.vo.SpacePageResp;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @description: home list
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/08/02 11:06
 **/
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class HomePageVo implements Serializable {

    private Long totalCount;

    private List<SpacePageResp> homes;
}
