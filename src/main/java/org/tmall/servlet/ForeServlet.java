package org.tmall.servlet;

import org.springframework.web.util.HtmlUtils;
import org.tmall.comparator.*;
import org.tmall.dao.*;
import org.tmall.entity.*;
import org.tmall.enums.OrderStateEnum;
import org.tmall.utils.Page;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

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
        try {
            user = new UserDAO().getUserByNameAndPassword(user.getName(), user.getPassword());
            if (null != user) {
                result = "true";
            }
        } catch (Exception e){
            e.printStackTrace();
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
        String orientation = "down";
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
                    orientation = request.getParameter("orientation");
                    Collections.sort(c.getProducts(), new ProductPriceComparator());
                    if("up".equals(orientation))
                        Collections.reverse(c.getProducts());
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
        request.setAttribute("orientation", orientation);
        return "category.jsp";
    }

    public String search(HttpServletRequest request, HttpServletResponse response, Page page){
        String keyword = request.getParameter("keyword");
        List<Product> ps = new ProductDAO().search(keyword, page.getStart(), page.getCount());
        new ProductDAO().setSaleAndReviewNumber(ps);
        request.setAttribute("ps", ps);
        return "searchResult.jsp";
    }

    public String addCart(HttpServletRequest request, HttpServletResponse response, Page page){
        String result = "false";
        try {
            int pid = Integer.parseInt(request.getParameter("pid"));
            int num = Integer.parseInt(request.getParameter("num"));

            Product p = new ProductDAO().getProductById(pid);
            User user = (User) request.getSession().getAttribute("user");

            OrderItem oi = new OrderItemDAO().getOrderItemByUserAndProduct(user.getId(), p.getId());
            if (null != oi){
                if(oi.getNumber() + num > p.getStock())
                    oi.setNumber(p.getStock());
                else
                    oi.setNumber(oi.getNumber() + num);
                new OrderItemDAO().update(oi);
                result = "true";
            } else{
                oi = new OrderItem();
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

    public String cart(HttpServletRequest request, HttpServletResponse response, Page page){

        User user = (User) request.getSession().getAttribute("user");
        List<OrderItem> ois = new OrderItemDAO().listOrderItemByUser(user.getId());
        request.setAttribute("ois", ois);
        return "cart.jsp";
    }

    public String cartController(HttpServletRequest request, HttpServletResponse response, Page page){
        String result = "false";
        int oiid = Integer.parseInt(request.getParameter("oiid"));
        String action = request.getParameter("action");

        switch (action){
            case "delete":
                if (new OrderItemDAO().delete(oiid))
                    result = "true";
                break;
            case "update":
                OrderItem oi = new OrderItemDAO().getOrderItemById(oiid);
                int num = Integer.parseInt(request.getParameter("num"));
                oi.setNumber(num);
                if(new OrderItemDAO().update(oi))
                    result = "true";
                break;
            default:
                break;
        }

        return "%" + result;
    }

    public String buyone(HttpServletRequest request, HttpServletResponse response, Page page){
        int pid = Integer.parseInt(request.getParameter("pid"));
        int num = Integer.parseInt(request.getParameter("num"));

        Product p = new ProductDAO().getProductById(pid);
        User user = (User) request.getSession().getAttribute("user");

        OrderItem oi = new OrderItemDAO().getOrderItemByUserAndProduct(user.getId(), p.getId());
        int oiId = -1;
        if (null != oi){
            if(oi.getNumber() + num > p.getStock())
                oi.setNumber(p.getStock());
            else
                oi.setNumber(oi.getNumber() + num);
            new OrderItemDAO().update(oi);
            oiId = oi.getId();

        }else {
            oi = new OrderItem();
            oi.setNumber(num);
            oi.setUser(user);
            oi.setProduct(p);
            new OrderItemDAO().add(oi);
            oiId = oi.getId();
        }
        return "@forebuy?oiid=" + oiId;
    }

    public String buy(HttpServletRequest request, HttpServletResponse response, Page page){

        String[] oiIds = request.getParameterValues("oiid");
        List<OrderItem> ois = new ArrayList<>();
        float total = 0;
        for(String oiId : oiIds){
            int id = Integer.parseInt(oiId);
            OrderItem oi = new OrderItemDAO().getOrderItemById(id);
            total += oi.getNumber() * oi.getProduct().getPromotePrice();
            ois.add(oi);
        }

        request.getSession().setAttribute("ois", ois);
        request.setAttribute("total", total);
        return "buy.jsp";
    }

    public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page){
        User user = (User) request.getSession().getAttribute("user");

        List<OrderItem> ois = (List<OrderItem>) request.getSession().getAttribute("ois");
        if (null == ois || ois.isEmpty())
            return "@login.jsp";

        String address = request.getParameter("address");
        String post = request.getParameter("post");
        String receiver = request.getParameter("receiver");
        String mobile = request.getParameter("mobile");
        String userMessage = request.getParameter("userMessage");

        String orderCode = new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round(new Random().nextDouble()*1000000000);
        Order o = new Order();
        o.setAddress(address);
        o.setMobile(mobile);
        o.setPost(post);
        o.setReceiver(receiver);
        o.setUser(user);
        o.setUserMessage(null == userMessage?"":userMessage);
        o.setOrderCode(orderCode);
        o.setCreateDate(new Date());
        o.setStatus(OrderStateEnum.WAIT_PAY.getId());
        new OrderDAO().add(o);

        float totalPrice = 0;
        for(OrderItem oi : ois){
            oi.setOrder(o);
            new OrderItemDAO().update(oi);
            totalPrice += oi.getProduct().getPromotePrice()*oi.getNumber();
        }

        return "@forealipay?oid=" + o.getId() + "&total=" + totalPrice;
    }

    public String alipay(HttpServletRequest request, HttpServletResponse response, Page page){
        return "alipay.jsp";
    }

    public String payed(HttpServletRequest request, HttpServletResponse response, Page page){
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = new OrderDAO().getOrderById(oid);
        o.setStatus(OrderStateEnum.WAIT_DELIVERY.getId());
        o.setPayDate(new Date());
        new OrderDAO().update(o);
        request.setAttribute("o", o);
        return "payed.jsp";
    }

    public String bought(HttpServletRequest request, HttpServletResponse response, Page page){
        User user = (User) request.getSession().getAttribute("user");
        List<Order> os = new OrderDAO().listOrderExcept(user.getId(), OrderStateEnum.DELETE.getId());
        new OrderItemDAO().fill(os);
        request.setAttribute("os", os);
        return "bought.jsp";
    }

    public String confirmPay(HttpServletRequest request, HttpServletResponse response, Page page){
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = new OrderDAO().getOrderById(oid);
        new OrderItemDAO().fill(o);
        request.setAttribute("o", o);
        return "confirmPay.jsp";
    }

    public String orderConfirmed(HttpServletRequest request, HttpServletResponse response, Page page){
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = new OrderDAO().getOrderById(oid);
        o.setStatus(OrderStateEnum.WAIT_REVIEW.getId());
        o.setConfirmDate(new Date());
        new OrderDAO().update(o);
        return "orderConfirmed";
    }

    public String review(HttpServletRequest request, HttpServletResponse response, Page page){
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = new OrderDAO().getOrderById(oid);
        new OrderItemDAO().fill(o);
        Product p = o.getOrderItems().get(0).getProduct();
        List<Review> reviews = new ReviewDAO().listReview(p.getId());
        new ProductDAO().setSaleAndReviewNumber(p);
        request.setAttribute("p", p);
        request.setAttribute("o", o);
        request.setAttribute("reviews", reviews);
        return "review.jsp";
    }

    public String doreview(HttpServletRequest request, HttpServletResponse response, Page page){

        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = new OrderDAO().getOrderById(oid);
        o.setStatus(OrderStateEnum.FINISH.getId());
        new OrderDAO().update(o);

        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = new ProductDAO().getProductById(pid);

        String content = request.getParameter("content");

        content = HtmlUtils.htmlEscape(content);

        User user =(User) request.getSession().getAttribute("user");
        Review review = new Review();
        review.setContent(content);
        review.setProduct(p);
        review.setCreateDate(new Date());
        review.setUser(user);
        new ReviewDAO().add(review);
        return "@forereview?oid=" + oid + "$showonly=true";
    }

    public String deleteOrder(HttpServletRequest request, HttpServletResponse response, Page page){
        String result = "fail";
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = new OrderDAO().getOrderById(oid);
        o.setStatus(OrderStateEnum.DELETE.getId());
        if(new OrderDAO().update(o))
            result = "success";
        return "%" + result;
    }
}
