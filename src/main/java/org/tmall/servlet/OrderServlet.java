package org.tmall.servlet;

import org.tmall.dao.OrderDAO;
import org.tmall.entity.Order;
import org.tmall.enums.OrderStateEnum;
import org.tmall.utils.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class OrderServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    public String delivery(HttpServletRequest request, HttpServletResponse response, Page page) {

        int id = Integer.parseInt(request.getParameter("id"));
        Order o = orderDAO.getOrderById(id);
        o.setDeliveryDate(new Date());
        o.setStatus(OrderStateEnum.WAIT_CONFIRM.getId() + "");
        orderDAO.update(o);
        return "@admin_order_list";
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {

        List<Order> os = orderDAO.listOrder(page.getStart(),page.getCount());
        orderItemDAO.fill(os);
        int total = orderDAO.getTotal();
        page.setTotal(total);

        request.setAttribute("os", os);
        request.setAttribute("page", page);

        return "jsp/listOrder.jsp";
    }
}
