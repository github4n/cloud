package com.iot.portal.comm.interceptor;

import com.github.pagehelper.util.StringUtil;
import com.iot.common.exception.BusinessException;
import com.iot.user.exception.UserExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Order(1)
@WebFilter(filterName = "portalFilter", urlPatterns = "/*")
public class PortalFilter implements Filter {
	
	 private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
			 "/info",
			 "/app/uploadImg",
			 "/portal/file/uploadImg",
			 "/portal/file/uploadFile",
			 "/OTAManageController/uploadFirmwareOtaFile",
			 "/app/setMsgSend",
			 "/app/appPackNotify",
			 "/webjars/springfox-swagger-ui/lib/jsoneditor.min.js",
			 "/webjars/springfox-swagger-ui/springfox.js",
			 "/webjars/springfox-swagger-ui/images/logo_small.png",
			 "/webjars/springfox-swagger-ui/css/print.css",
			 "/webjars/springfox-swagger-ui/images/throbber.gif",
			 "/webjars/springfox-swagger-ui/images/favicon-16x16.png",
			 "/swagger-ui.html",
			 "/webjars/springfox-swagger-ui/css/typography.css",
			 "/webjars/springfox-swagger-ui/css/reset.css",
			 "/webjars/springfox-swagger-ui/lib/object-assign-pollyfill.js",
			 "/webjars/springfox-swagger-ui/lib/jquery-1.8.0.min.js",
			 "/webjars/springfox-swagger-ui/css/screen.css",
			 "/webjars/springfox-swagger-ui/lib/jquery.slideto.min.js",
			 "/webjars/springfox-swagger-ui/lib/jquery.wiggle.min.js",
			 "/webjars/springfox-swagger-ui/lib/jquery.ba-bbq.min.js",
			 "/webjars/springfox-swagger-ui/lib/handlebars-4.0.5.js",
			 "/webjars/springfox-swagger-ui/lib/lodash.min.js",
			 "/webjars/springfox-swagger-ui/lib/backbone-min.js",
			 "/webjars/springfox-swagger-ui/swagger-ui.min.js",
			 "/webjars/springfox-swagger-ui/lib/highlight.9.1.0.pack.js",
			 "/webjars/springfox-swagger-ui/lib/highlight.9.1.0.pack_extended.js",
			 "/webjars/springfox-swagger-ui/lib/highlight.9.1.0.pack_extended.js",
			 "/webjars/springfox-swagger-ui/lib/marked.js",
			 "/webjars/springfox-swagger-ui/lib/swagger-oauth.js",
			 "/swagger-resources/configuration/ui",
			 "/webjars/springfox-swagger-ui/images/favicon-32x32.png",
			 "/swagger-resources",
			 "/v2/api-docs",
			 "/swagger-resources/configuration/security",
			 "/portal/product/confirmRelease",
			 "/portal/file/uploadButNoSaveToDb",
			 "/app/testGoogleJsonConfig",
			 "/app/testP12PushConfig",
			 "/app/testP12CertConfig",
			 "/app/testMobileprovisionConfig",
			 "/downAppPackageFile",
			 "/langInfo/exportToExcel")));
	 
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Autowired(required = false)
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		String path = req.getRequestURI().substring(req.getContextPath().length()).replaceAll("[/]+$", "");
		boolean allowedPath = ALLOWED_PATHS.contains(path);

		if (allowedPath) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			String param = "";
			String method = "GET";
			ServletRequest requestWrapper = null;
			if (req instanceof HttpServletRequest) {
				method = ((HttpServletRequest) req).getMethod();
				requestWrapper = new PortalHttpServletRequestWrapper((HttpServletRequest) req); // 替换
			}
			if (method.equalsIgnoreCase("GET")) {
				param = req.getQueryString();
				// System.out.println("filter读取GET参数>>>>>>>>>" + param);
			} else {// if ("POST".equalsIgnoreCase(method)) {
				param = this.getBodyString(requestWrapper.getReader());
				// System.out.println("filter读取POST参数>>>>>>>>>" + param);
			}
			if (!StringUtil.isEmpty(param)
					&& (param.contains("%3C") || param.contains("%3E") || isSpecialChar(param))) {
				throw new BusinessException(UserExceptionEnum.PARAM_CONTAIN_SPECIAL_CHAR); // 请求头参数非法 ,含有特殊字符
			}
			HttpServletResponse resp = (HttpServletResponse) servletResponse;
			PortalServletResponseWrapper mResp = new PortalServletResponseWrapper(resp); // 包装响应对象 resp 并缓存响应数据

			filterChain.doFilter(requestWrapper, mResp);

			byte[] bytes = mResp.getBytes(); // 获取缓存的响应数据
			String outString = new String(bytes);
			// System.out.println("filter读取出参>>>>>>>>>" + outString);
			if (!StringUtil.isEmpty(outString) && outString.contains("\"code\"") && outString.contains("\"desc\"")
					&& outString.contains("\"data\"") && isSpecialChar(outString)) {
				outString = outString.replaceAll("<", "&lt;");
				outString = outString.replaceAll(">", "&gt;");
			}
			// System.out.println("filter读取出参>>>>>>>>>" + outString);
			bytes = outString.getBytes();
			resp.getOutputStream().write(bytes); // 将转义数据响应给客户端
		}

	}

	/**
	 * 
	 * 描述：获取request请求body中参数
	 * 
	 * @author 李帅
	 * @created 2018年8月9日 下午6:41:41
	 * @since
	 * @param br
	 * @return
	 */
	public static String getBodyString(BufferedReader br) {
		String inputLine;
		String str = "";
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
			br.close();
		} catch (IOException e) {
			System.out.println("IOException: " + e);
		}
		return str;
	}

	/**
	 * 判断是否含有特殊字符
	 *
	 * @param str
	 * @return true为包含，false为不包含
	 */
	public static boolean isSpecialChar(String str) {
		String regEx = "[<>]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}

	@Override
	public void destroy() {

	}
}
