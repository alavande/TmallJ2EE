package org.tmall.servlet;

import org.tmall.entity.Category;
import org.tmall.entity.Product;
import org.tmall.entity.Property;
import org.tmall.entity.PropertyValue;
import org.tmall.utils.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class ProductServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.getCategoryById(cid);

        String name = request.getParameter("name");
        String subTitle = request.getParameter("subTitle");
        float originalPrice = Float.parseFloat(request.getParameter("originalPrice"));
        float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
        int stock = Integer.parseInt(request.getParameter("stock"));

        Product p = new Product();
        p.setName(name);
        p.setSubTitle(subTitle);
        p.setOriginalPrice(originalPrice);
        p.setPromotePrice(promotePrice);
        p.setStock(stock);
        p.setCategory(c);
        p.setCreateDate(new Date());
        productDAO.add(p);

        return "@admin_product_list?cid=" + cid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Product p = productDAO.getProductById(id);
        productDAO.delete(id);
        return "@admin_product_list?cid=" + p.getCategory().getId();
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Product p = productDAO.getProductById(id);
        request.setAttribute("p", p);
        return "jsp/editProduct.jsp";
    }

    public String editPropertyValue(HttpServletRequest request, HttpServletResponse response, Page page){

        int id = Integer.parseInt(request.getParameter("id"));
        Product p = productDAO.getProductById(id);
        request.setAttribute("p", p);

        propertyValueDAO.init(p);

        List<PropertyValue> pvs = propertyValueDAO.list(p.getId());
        request.setAttribute("pvs", pvs);

        return "jsp/editProductValue.jsp";
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {

        int id = Integer.parseInt(request.getParameter("id"));
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.getCategoryById(cid);

        String name = request.getParameter("name");
        String subTitle = request.getParameter("subTitle");
        float originalPrice = Float.parseFloat(request.getParameter("originalPrice"));
        float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
        int stock = Integer.parseInt(request.getParameter("stock"));

        Product p = new Product();
        p.setId(id);
        p.setName(name);
        p.setOriginalPrice(originalPrice);
        p.setPromotePrice(promotePrice);
        p.setStock(stock);
        p.setCategory(c);
        p.setUpdateDate(new Date());

        productDAO.update(p);
        return "@admin_product_list?cid=" + cid;
    }

    public String updatePropertyValue(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pvid = Integer.parseInt(request.getParameter("pvid"));
        String value = request.getParameter("value");

        PropertyValue pv =propertyValueDAO.getPropertyValueById(pvid);
        pv.setValue(value);
        propertyValueDAO.update(pv);
        return "%success";
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.getCategoryById(cid);
        List<Product> ps = productDAO.listProductByCategory(cid, page.getStart(), page.getCount());

        int total = productDAO.getTotal(cid);
        page.setTotal(total);
        page.setParam("&cid="+cid);

        request.setAttribute("c", c);
        request.setAttribute("ps", ps);
        request.setAttribute("page", page);
        return "jsp/listProduct.jsp";
    }
}
