package org.tmall.filter;

import org.apache.commons.lang3.StringUtils;
import org.tmall.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class ForeAuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String contextPath = request.getServletContext().getContextPath();

        String[] noNeedAuthPage = {"home", "checkLogin", "register", "login", "product", "loginModal", "category", "search"};

        String uri = request.getRequestURI();
        uri = StringUtils.remove(contextPath, uri);
        if(uri.startsWith("/fore") && !uri.startsWith("/foreServlet")){
            String method = StringUtils.substringAfterLast(uri, "/fore");
            if(!Arrays.asList(noNeedAuthPage).contains(method)){
                User user = (User) request.getSession().getAttribute("user");
                if (null == user){
                    response.sendRedirect("login.jsp");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
