package org.tmall.servlet;

import org.tmall.entity.Category;
import org.tmall.entity.Property;
import org.tmall.utils.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class PropertyServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {

        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.getCategoryById(cid);
        String name = request.getParameter("name");
        Property p = new Property();
        p.setCategory(c);
        p.setName(name);
        propertyDAO.add(p);
        return "@admin_property_list?cid=" + cid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {

        int id = Integer.parseInt(request.getParameter("id"));
        Property p = propertyDAO.getPropertyById(id);
        propertyDAO.delete(id);
        return "@admin_property_list?cid=" + p.getCategory().getId();
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {

        int id = Integer.parseInt(request.getParameter("id"));
        Property p = propertyDAO.getPropertyById(id);
        request.setAttribute("p", p);
        return "jsp/editProperty.jsp";
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {

        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.getCategoryById(cid);

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        Property p = new Property();
        p.setId(id);
        p.setName(name);
        p.setCategory(c);
        propertyDAO.update(p);

        return "@admin_property_list?cid=" + c.getId();
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {

        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.getCategoryById(cid);

        List<Property> ps = propertyDAO.listPropertyByCategroy(cid, page.getStart(), page.getCount());
        int total = propertyDAO.getTotal(cid);
        page.setTotal(total);
        page.setParam("&cid=" + cid);

        request.setAttribute("ps", ps);
        request.setAttribute("c", c);
        request.setAttribute("page", page);

        return "jsp/listProperty.jsp";
    }
}
