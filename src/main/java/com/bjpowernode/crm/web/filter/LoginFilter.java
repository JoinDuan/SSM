package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        System.out.println("进入到验证有没有登录过的过滤器");

        //判断session域对象中有没有user对象
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //这俩资源不要拦截，防止无限拦截，登录相关
        String path = request.getServletPath();
        if("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            chain.doFilter(req,resp);

        //其他资源必须验证有没有登录过
        }else {

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            //如果user不为空，说明登录过，放行
            if(user != null){
                chain.doFilter(req,resp);

                //没有登录过，
            }else {
               /* 重定向到登录页

                重定向的路径怎么写？
                在实际开发中，对于路径的使用，不论操作是前端还是后端，一律使用绝对路径
                转发和重定向路径的写法如下：
                    转发：
                        使用的是一种特殊的绝对路径使用方式，这种路径前面不加 /项目名，称之为内部路径

                    重定向：
                        使用的是传统的绝对路径的写法，前年必须以 /项目名开头，后面跟具体的绝对路径
                        /crm/login.jsp

                为什么要使用重定向？转发不行吗
                    转发之后，路径会停留在老路径上，而不是跳转资源之后的最新路径
                    我们应该为用户跳转到登录页的同时，将浏览器的地址栏设置为当前的登录页的路径*/


                //动态的拿到项目名
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }

        }

    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
