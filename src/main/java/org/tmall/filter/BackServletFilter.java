package org.tmall.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  拦截所有请求
 */
public class BackServletFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取根目录，如 127.0.0.1:8080/tmall
        String contextPath = request.getServletContext().getContextPath();
        // 获取完整uri地址，如 127.0.0.1:8080/tmall/admin_category_list
        String uri = request.getRequestURI();
        // 截取servlet 地址
        uri = StringUtils.remove(uri, contextPath);

        /**
         * 拦截所有以 admin_ 开头的请求
         * 例：admin_category_list
         */
        if (uri.startsWith("/admin_")){
            // 提取 uri 中间值，对应的servlet, 如 category
            String servletPath = StringUtils.substringBetween(uri, "_", "_") + "Servlet";
            // 获取最后需要调用的对应 servlet 中的方法，如 list
            String method = StringUtils.substringAfterLast(uri, "_");
            request.setAttribute("method", method);
            servletRequest.getRequestDispatcher("/" + servletPath).forward(request, response);
            return;
        }

        // 上述方法没有拦截，下一个拦截器
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
