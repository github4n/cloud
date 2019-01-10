package com.iot.robot.interceptors;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ReqCacheFilter implements Filter {


    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1,
            FilterChain chain) throws IOException, ServletException {
        RequestWraper req = new RequestWraper((HttpServletRequest) arg0);
        chain.doFilter(req, arg1);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

    @Override
    public void destroy() {
        
    }
    
    static class RequestWraper extends HttpServletRequestWrapper {

        private byte[] body = null;
        public RequestWraper(HttpServletRequest request) {
            super(request);
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            InputStream in;
            try {
                in = request.getInputStream();
                byte[] buf = new byte[2048];
                int len = 0;
                while((len = in.read(buf)) != -1) {
                    bo.write(buf, 0, len);
                }
                bo.flush();
                body = bo.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
           
        }
        
        @Override
        public ServletInputStream getInputStream() throws IOException {
            ByteArrayInputStream bi = new ByteArrayInputStream(body);
            return new CacheInputStream(bi);
        }
        
        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }
    }
    
    static class CacheInputStream extends ServletInputStream {

        private InputStream in;
        public CacheInputStream(InputStream in) {
            this.in = in;
        }
        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener arg0) {
        }

        @Override
        public int read() throws IOException {
            return in.read(); 
        }
    }

}
