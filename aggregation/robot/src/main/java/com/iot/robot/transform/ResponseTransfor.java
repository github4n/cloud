package com.iot.robot.transform;

import java.util.List;

import com.iot.robot.vo.ResponsePost;
/**
 * 创建人：chenxiaolin
 * 创建时间：2018年4月26日 下午5:11:08
 * 修改人： chenxiaolin
 * 修改时间：2018年4月26日 下午5:11:08
 */
public interface ResponseTransfor {

	ResponsePost getResponse(List<Object>[] resources);
}
