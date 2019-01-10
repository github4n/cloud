package com.iot.portal.config.security;

import com.iot.common.exception.ExceptionResult;
import com.iot.common.util.JsonUtil;
import com.iot.locale.LocaleMessageSourceService;
import com.iot.portal.exception.BusinessExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 项目名称：cloud
 * 功能描述：权限拒绝抛出的异常处理
 * 创建人： yeshiyuan
 * 创建时间：2018/7/16 9:18
 * 修改人： yeshiyuan
 * 修改时间：2018/7/16 9:18
 * 修改描述：
 */
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Autowired
    private LocaleMessageSourceService localeMessageSourceService;

    /**
      * @despriction：处理spring security权限不足时的异常
      * @author  yeshiyuan
      * @created 2018/7/16 9:54
      * @param null
      * @return
      */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        if (!httpServletResponse.isCommitted()) {
            String message = localeMessageSourceService.getMessage(BusinessExceptionEnum.ACCESS_DENIED.getMessageKey(), null, null);
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            //这句话的意思，是让浏览器用utf8来解析返回的数据
            httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
            //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.getWriter().write(JsonUtil.toJson(new ExceptionResult<>(HttpServletResponse.SC_FORBIDDEN, message)));
        }
    }
}
