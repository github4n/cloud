package com.iot.robot.transform;

import com.alibaba.fastjson.JSONObject;
import com.iot.robot.norm.KeyValue;
/**
 * 创建人：chenxiaolin
 * 创建时间：2018年4月26日 下午5:11:08
 * 修改人： chenxiaolin
 * 修改时间：2018年4月26日 下午5:11:08
 */
public interface KeyValTransfor {

	Object toCommonKeyVal(JSONObject commond);
	JSONObject getSelfKeyVal(KeyValue kv);
}
