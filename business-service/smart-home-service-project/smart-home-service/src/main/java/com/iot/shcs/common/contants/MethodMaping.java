package com.iot.shcs.common.contants;

import java.util.HashMap;
import java.util.Map;

public class MethodMaping {
	
	private static Map<String,String> methodMap=new HashMap<String,String>();

	//网关返回方法名称
	private static final String PROPS_NOTIFY="props_notify";//子设备属性上报
	private static final String EVENT_NOTIFY="event_notify";//子设备事件上报
	private static final String STATUS_NOTIFY="status_notify";//子设备状态上报
	private static final String GET_PROP_RESP="get_prop_resp";//获取子设备属性回调
	private static final String QUERY_RESP="query_resp";//查询子设备列表回调
	private static final String LIST_NOTIFY="list_notify";//子设备列表变更上报回调
	private static final String CREATE_RESP="create_resp";//创建分组回调
	private static final String ADD_RESP="add_resp";//添加分组成员回调
	private static final String REMOVE_RESP="remove_resp";//删除分组成员回调
	private static final String OPERATE_RESP="operate_resp";//分组操作回调  //操作场景回调
	private static final String ADD_ACTION_RESP="add_action_resp";//添加场景项回调
	private static final String DEL_ACTION_RESP="del_action_resp";//删除场景项回调
	private static final String UPDATE_ACTION_RESP="update_action_resp";//修改场景项回调
	private static final String QUERY_BASE_RESP="query_base_resp";//修改场景项回调
	private static final String DELETE_RESP="delete_resp";//删除分组回调
	private static final String UPDATE_BASE_RESP="update_base_resp";//编辑分组回调
	private static final String QUERY_MEMBERS_RESP="query_members_resp";//查询分组成员回调
	private static final String LIST_RESP="list_resp";//查询分组成员回调
	private static final String RENAME_RESP="rename_resp";//编辑子设备名称回调
	private static final String SET_PROP_RESP="set_prop_resp";//
	private static final String STATE_NOTIFY="state_notify";//OTA升级状态上报
	
	//需要转换为实际的方法名称
	private static final String R_PROPS_NOTIFY="propsNotify";//子设备属性上报
	private static final String R_EVENT_NOTIFY="eventNotify";//子设备事件上报
	private static final String R_STATUS_NOTIFY="statusNotify";//子设备状态上报
	private static final String R_GET_PROP_RESP="getPropResp";//获取子设备属性回调
	private static final String R_QUERY_RESP="queryResp";//查询子设备列表回调
	private static final String R_LIST_NOTIFY="listNotify";//子设备列表变更上报回调
	private static final String R_CREATE_RESP="createResp";//创建分组回调
	private static final String R_ADD_RESP="addResp";//添加分组成员回调
	private static final String R_REMOVE_RESP="removeResp";//删除分组成员回调
	private static final String R_OPERATE_RESP="operateResp";//分组操作回调  //操作场景回调
	private static final String R_ADD_ACTION_RESP="addActionResp";//添加场景项回调
	private static final String R_DEL_ACTION_RESP="delActionResp";//删除场景项回调
	private static final String R_UPDATE_ACTION_RESP="updateActionResp";//修改场景项回调
	private static final String R_QUERY_BASE_RESP="queryBaseResp";//修改场景项回调
	private static final String R_DELETE_RESP="deleteResp";//删除分组回调
	private static final String R_UPDATE_BASE_RESP="updateBaseResp";//编辑分组回调
	private static final String R_QUERY_MEMBERS_RESP="queryMemberResp";//查询分组成员回调
	private static final String R_LIST_RESP="listResp";//查询分组成员回调
	private static final String R_RENAME_RESP="renameResp";//编辑子设备名称回调
	private static final String R_SET_PROP_RESP="setPropResp";//
	private static final String R_STATE_NOTIFY="stateNotify";//OTA升级状态上报
	
	public static void init() {
		methodMap.put(PROPS_NOTIFY, R_PROPS_NOTIFY);
		methodMap.put(EVENT_NOTIFY, R_EVENT_NOTIFY);
		methodMap.put(STATUS_NOTIFY, R_STATUS_NOTIFY);
		methodMap.put(GET_PROP_RESP, R_GET_PROP_RESP);
		methodMap.put(QUERY_RESP, R_QUERY_RESP);
		methodMap.put(LIST_NOTIFY, R_LIST_NOTIFY);
		methodMap.put(CREATE_RESP, R_CREATE_RESP);
		methodMap.put(ADD_RESP, R_ADD_RESP);
		methodMap.put(REMOVE_RESP, R_REMOVE_RESP);
		methodMap.put(OPERATE_RESP, R_OPERATE_RESP);
		methodMap.put(ADD_ACTION_RESP, R_ADD_ACTION_RESP);
		methodMap.put(DEL_ACTION_RESP, R_DEL_ACTION_RESP);
		methodMap.put(UPDATE_ACTION_RESP, R_UPDATE_ACTION_RESP);
		methodMap.put(QUERY_BASE_RESP, R_QUERY_BASE_RESP);
		methodMap.put(DELETE_RESP, R_DELETE_RESP);
		methodMap.put(UPDATE_BASE_RESP, R_UPDATE_BASE_RESP);
		methodMap.put(QUERY_MEMBERS_RESP, R_QUERY_MEMBERS_RESP);
		methodMap.put(LIST_RESP, R_LIST_RESP);
		methodMap.put(RENAME_RESP, R_RENAME_RESP);
		methodMap.put(SET_PROP_RESP, R_SET_PROP_RESP);
		methodMap.put(STATE_NOTIFY, R_STATE_NOTIFY);
	}
	
	public static String getRealMapingMethod(String method) {
		init();
		return methodMap.get(method)==null?method:methodMap.get(method);
	}
}
