package com.iot.robot.utils.alexa;

import com.alibaba.fastjson.JSONObject;
import com.iot.robot.vo.GrantAuthVo;
import com.iot.util.HttpsUtil;
//暂时弃用
@Deprecated
public class AuthUtil {

	private static final String AUTH_API = "https://api.amazon.com/auth/o2/token";
	private static final String GRANTINFO = "grant_type=authorization_code&code=%s&client_id=%s&client_secret=%s";
	public static GrantAuthVo getAccessToken(String code, String clientId, String secret) {
		String grantInfo = String.format(GRANTINFO, code, clientId, secret);
		GrantAuthVo vo = null;
		try {
			String info = HttpsUtil.sendPostForForm(AUTH_API, grantInfo);
			JSONObject json = (JSONObject) JSONObject.parse(info);
			String accessToken = json.getString("access_token");
			String refreshToken = json.getString("refresh_token");
			int expiresIn = json.getIntValue("expires_in");
			vo = new GrantAuthVo();
			vo.setAccessToken(accessToken);
			vo.setExpiresIn(expiresIn);
			vo.setRefreshToken(refreshToken);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
}
