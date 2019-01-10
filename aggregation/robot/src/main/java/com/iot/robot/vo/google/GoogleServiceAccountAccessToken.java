package com.iot.robot.vo.google;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/8/20 17:20
 * @Modify by:
 */
public class GoogleServiceAccountAccessToken implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4338091672834770695L;

    private String access_token;
    // 创建的时间(毫秒)
    private long createTimeMillis;
    // 有效时长(秒)
    private Long expires_in;
    private String token_type;

    public GoogleServiceAccountAccessToken() {
        this.createTimeMillis = System.currentTimeMillis();
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public long getCreateTimeMillis() {
        return createTimeMillis;
    }

    public void setCreateTimeMillis(long createTimeMillis) {
        this.createTimeMillis = createTimeMillis;
    }
}
