package org.tmall.filter;

import org.apache.commons.lang3.StringUtils;
import org.tmall.dao.CategoryDAO;
import org.tmall.dao.OrderItemDAO;
import org.tmall.entity.Category;
import org.tmall.entity.OrderItem;
import org.tmall.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ForeServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String contextPath = request.getServletContext().getContextPath();
        String uri = request.getRequestURI();
        uri = StringUtils.remove(uri, contextPath);

        request.setAttribute("contextPath", contextPath);

        User user = (User) request.getSession().getAttribute("user");
        int cartTotalItemNumber = 0;
        if (null != user){
            List<OrderItem> ois = new OrderItemDAO().listOrderItemByUser(user.getId());
            for (OrderItem oi : ois){
                cartTotalItemNumber += oi.getNumber();
            }
        }
        request.setAttribute("cartTotalItemNumber", cartTotalItemNumber);

        List<Category> cs = (List<Category>) request.getSession().getAttribute("cs");
        if (null == cs){
            cs = new CategoryDAO().listAllCategory();
            request.setAttribute("cs", cs);
        }

        if (uri.startsWith("/fore") && !uri.startsWith("foreServlet")){
            String method = StringUtils.substringAfter(uri, "/fore");
            request.setAttribute("method", method);

            servletRequest.getRequestDispatcher("/foreServlet").forward(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
