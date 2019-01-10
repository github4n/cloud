package com.iot.oauth.util;

import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

public final class OauthResponseUtil {

	private OauthResponseUtil() { }
	
	public static OAuthResponse getSuccessResponse(String accessToken, String refreshToken, String expiresIn) throws OAuthSystemException {
		return OAuthASResponse
				.tokenResponse(HttpServletResponse.SC_OK)
				.setTokenType("bearer")
				.setAccessToken(accessToken)
				.setRefreshToken(refreshToken).setExpiresIn(expiresIn)
				.buildJSONMessage();
	}
	
	public static OAuthResponse getFailResponse(String errorCode, String errorDesc) throws OAuthSystemException {
		return OAuthResponse.errorResponse(HttpServletResponse.SC_OK)
				.setError(errorCode)
                .setErrorDescription(errorDesc)  
                .buildJSONMessage();
	}
}
