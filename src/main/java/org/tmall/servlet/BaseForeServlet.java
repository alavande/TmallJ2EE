package org.tmall.servlet;

import org.apache.commons.lang3.StringUtils;
import org.tmall.utils.Page;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseForeServlet extends HttpServlet {

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int start = 0;
        int count = 10;
        try{
            start = Integer.parseInt(request.getParameter("page.start"));
            count = Integer.parseInt(request.getParameter("page.count"));
        } catch (Exception e){
            e.printStackTrace();
        }

        Page page = new Page(start, count);
        String method = (String) request.getAttribute("method");
        try {
            Method m = this.getClass().getMethod(method, HttpServletRequest.class, HttpServletResponse.class, Page.class);
            String redirect = m.invoke(this, request, response, page).toString();

            if (redirect.startsWith("@")){
                response.sendRedirect(redirect.substring(1));
            } else if (redirect.startsWith("%")){
                response.getWriter().print(redirect.substring(1));
            } else {
                request.getRequestDispatcher(redirect).forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
