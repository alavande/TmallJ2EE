package org.tmall.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.tmall.dao.*;
import org.tmall.utils.Page;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 */
public abstract class BaseBackServlet extends HttpServlet {

    // 基本CRUD方法
    public abstract String add(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String delete(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String edit(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String update(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String list(HttpServletRequest request, HttpServletResponse response, Page page);

    // 所有DAO对象，供子类使用
    protected CategoryDAO categoryDAO = new CategoryDAO();
    protected OrderDAO orderDAO = new OrderDAO();
    protected OrderItemDAO orderItemDAO = new OrderItemDAO();
    protected ProductDAO productDAO = new ProductDAO();
    protected ProductImageDAO productImageDAO = new ProductImageDAO();
    protected PropertyDAO propertyDAO = new PropertyDAO();
    protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
    protected ReviewDAO reviewDAO = new ReviewDAO();
    protected UserDAO userDAO = new UserDAO();

    public void service(HttpServletRequest request, HttpServletResponse response){

        // 获取分页信息
        int start = 0;
        int count = 5;
        try {
            start = Integer.parseInt(request.getParameter("page.start"));
            count = Integer.parseInt(request.getParameter("page.count"));
        } catch (Exception e){
            e.printStackTrace();
        }
        Page page = new Page(start, count); // TODO

        /**
         *  借助反射调用对应方法(add, delete, edit, update, list)
         */
        // 获得过滤器返回的方法名
        String method = (String) request.getAttribute("method");
        try {
            // 获得对应方法
            Method m = this.getClass().getMethod(method,
                    javax.servlet.http.HttpServletRequest.class, javax.servlet.http.HttpServletResponse.class, Page.class);
            String redirect = m.invoke(this, request, response, page).toString();

            // 根据方法返回值进行跳转
            // 以 @ 开头，进行客户端跳转
            if (redirect.startsWith("@"))
                response.sendRedirect(redirect.substring(1));
            // 以 % 开头，直接在控制台输出
            else if (redirect.startsWith("%"))
                response.getWriter().print(redirect.substring(1));
            // 否则，进行服务端跳转
            else
                request.getRequestDispatcher(redirect).forward(request, response); // TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public InputStream parseUpload(HttpServletRequest request, Map<String, String> params){
        InputStream is = null;
        try{
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 设置上传文件的大小限制为10M
            factory.setSizeThreshold(1024 * 10240);

            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();
            while (iter.hasNext()){
                FileItem item = (FileItem) iter.next();
                if (!item.isFormField()){
                    // 获取上传文件的输入流
                    is = item.getInputStream();
                } else{
                    String paramName = item.getFieldName();
                    String paramValue = item.getString();
                    paramValue = new String(paramValue.getBytes("ISO-8859-1"), "UTF-8");
                    params.put(paramName, paramValue);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return is;
    }
}
