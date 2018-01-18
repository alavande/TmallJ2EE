package org.tmall.servlet;

import org.springframework.web.util.HtmlUtils;
import org.tmall.comparator.*;
import org.tmall.dao.*;
import org.tmall.entity.*;
import org.tmall.utils.Page;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ForeServlet extends BaseForeServlet {

    public String home(HttpServletRequest request, HttpServletResponse response, Page page){

        List<Category> cs = new CategoryDAO().listAllCategory();
        new ProductDAO().fill(cs);
        new ProductDAO().fillByRow(cs);
        request.setAttribute("cs", cs);
        return "home.jsp";
    }

    public String register(HttpServletRequest request, HttpServletResponse response, Page page){

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        name = HtmlUtils.htmlEscape(name);
        System.out.println(name);
        boolean exist = new UserDAO().isExist(name);

        if(exist){
            request.setAttribute("msg", "用户名已经被使用,不能使用");
            return "register.jsp";
        }

        User user = new User();
        user.setName(name);
        user.setPassword(password);
        System.out.println(user.getName());
        System.out.println(user.getPassword());
        new UserDAO().add(user);

        return "@registerSuccess.jsp";
    }

    public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        name = HtmlUtils.htmlEscape(name);
        String password = request.getParameter("password");

        User user = new UserDAO().getUserByNameAndPassword(name,password);

        if(null==user){
            request.setAttribute("msg", "账号密码错误");
            return "login.jsp";
        }
        request.getSession().setAttribute("user", user);
        return "@forehome";
    }

    public String logout(HttpServletRequest request, HttpServletResponse response, Page page){
        request.getSession().removeAttribute("user");
        return "@forehome";
    }

    public String product(HttpServletRequest request, HttpServletResponse response, Page page){
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = new ProductDAO().getProductById(pid);

        List<ProductImage> pisSingle = new ProductImageDAO().listProductImage(p, ProductImageDAO.type_single);
        List<ProductImage> pisDetails = new ProductImageDAO().listProductImage(p, ProductImageDAO.type_detail);
        p.setProductSingleImages(pisSingle);
        p.setProductDetailImages(pisDetails);

        List<PropertyValue> pvs = new PropertyValueDAO().list(pid);
        List<Review> reviews = new ReviewDAO().listReview(pid);
        new ProductDAO().setSaleAndReviewNumber(p);

        request.setAttribute("reviews", reviews);
        request.setAttribute("pvs", pvs);
        request.setAttribute("p", p);

        return "product.jsp";
    }

    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page){

        String result = "false";
        User user = (User) request.getSession().getAttribute("user");

        user = new UserDAO().getUserByNameAndPassword(user.getName(), user.getPassword());
        if (null != user){
            result = "true";
        }
        return "%" + result;
    }

    public String loginModal(HttpServletRequest request, HttpServletResponse response, Page page){
        String result = "false";

        String name = request.getParameter("name");
        name = HtmlUtils.htmlEscape(name);
        String password = request.getParameter("password");

        User user = new UserDAO().getUserByNameAndPassword(name, password);
        if (null != user) {
            request.getSession().setAttribute("user", user);
            result = "true";
        }

        return "%" + result;
    }

    public String category(HttpServletRequest request, HttpServletResponse response, Page page){

        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = new CategoryDAO().getCategoryById(cid);
        new ProductDAO().fill(c);
        new ProductDAO().setSaleAndReviewNumber(c.getProducts());

        String sort = request.getParameter("sort");
        if(null != sort){
            switch (sort){
                case "all":
                    Collections.sort(c.getProducts(), new ProductAllComparator());
                    break;
                case "date":
                    Collections.sort(c.getProducts(), new ProductDateComparator());
                    break;
                case "price":
                    Collections.sort(c.getProducts(), new ProductPriceComparator());
                    break;
                case "review":
                    Collections.sort(c.getProducts(), new ProductReviewComparator());
                    break;
                case "saleCount":
                    Collections.sort(c.getProducts(), new ProductSaleCountComparator());
                    break;
                default:
                    break;
            }
        }
        request.setAttribute("c", c);
        return "category.jsp";
    }

    public String addCart(HttpServletRequest request, HttpServletResponse response, Page page){
        String result = "false";
        try {
            int pid = Integer.parseInt(request.getParameter("pid"));
            int num = Integer.parseInt(request.getParameter("num"));

            Product p = new ProductDAO().getProductById(pid);
            User user = (User) request.getSession().getAttribute("user");

            boolean exist = false;
            List<OrderItem> ois = new OrderItemDAO().listOrderItemByUser(user.getId());
            for (OrderItem o : ois){
                if (o.getProduct().getId() == p.getId()){
                    o.setNumber(o.getNumber() + num);
                    exist = true;
                    new OrderItemDAO().update(o);
                    result = "true";
                    break;
                }
            }

            if (!exist) {
                OrderItem oi = new OrderItem();
                oi.setProduct(p);
                oi.setUser(user);
                oi.setNumber(num);
                new OrderItemDAO().add(oi);

                if (oi.getId() > 0)
                    result = "true";
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "%" + result;
    }

    public String buyone(){

        return "";
    }
}
