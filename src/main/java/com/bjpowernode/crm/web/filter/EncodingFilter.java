package com.bjpowernode.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入到过滤字符编码的过滤器");

        //过滤post请求中文参数乱码的问题
        req.setCharacterEncoding("UTF-8");
        //过滤响应流响应中文乱码的问题
        res.setContentType("text/html;charset=utf-8");
        //将请求放行
        chain.doFilter(req,res);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("...");
    }

    @Override
    public void destroy() {

    }
}
