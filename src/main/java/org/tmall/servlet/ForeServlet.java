package org.tmall.servlet;

import org.tmall.dao.CategoryDAO;
import org.tmall.dao.ProductDAO;
import org.tmall.entity.Category;
import org.tmall.utils.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ForeServlet extends BaseForeServlet {

    public String home(HttpServletRequest request, HttpServletResponse response, Page page){

        List<Category> cs = new CategoryDAO().listAllCategory();
        new ProductDAO().fill(cs);
        new ProductDAO().fillByRow(cs);
        request.setAttribute("cs", cs);
        return "home.jsp";
    }
}
