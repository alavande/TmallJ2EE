package org.tmall.dao;

import org.tmall.entity.Order;
import org.tmall.entity.OrderItem;
import org.tmall.entity.Product;
import org.tmall.entity.User;
import org.tmall.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  OrderItemDAO 用于建立对于 orderItem 对象的 ORM 映射
 */
public class OrderItemDAO {

    // 获得订单项总数
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "select count(*) from Order_Item";

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    // 增加订单项
    public void add(OrderItem bean) {

        String sql = "insert into Order_Item values(null,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            ps.setInt(1, bean.getProduct().getId());

            //订单项在创建的时候，没有带订单信息的
            if(null==bean.getOrder())
                ps.setInt(2, -1);
            else
                ps.setInt(2, bean.getOrder().getId());

            ps.setInt(3, bean.getUser().getId());
            ps.setInt(4, bean.getNumber());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // 更新订单项
    public void update(OrderItem bean) {

        String sql = "update Order_Item set pid= ?, oid=?, uid=?,number=?  where id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, bean.getProduct().getId());
            if(null==bean.getOrder())
                ps.setInt(2, -1);
            else
                ps.setInt(2, bean.getOrder().getId());
            ps.setInt(3, bean.getUser().getId());
            ps.setInt(4, bean.getNumber());

            ps.setInt(5, bean.getId());
            ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    // 删除订单项
    public void delete(int id) {

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "delete from Order_Item where id = " + id;

            s.execute(sql);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // 根据ID获得某一订单项
    public OrderItem getOrderItemById(int id) {
        OrderItem bean = new OrderItem();

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "select * from Order_Item where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().getProductById(pid);
                User user = new UserDAO().getUserById(uid);
                bean.setProduct(product);
                bean.setUser(user);
                bean.setNumber(number);

                if( -1 != oid){
                    Order order= new OrderDAO().getOrderById(oid);
                    bean.setOrder(order);
                }

                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    // 查询某一用户的所有订单项
    public List<OrderItem> listOrderItemByUser(int uid) {
        return listOrderItemByUser(uid, 0, Short.MAX_VALUE);
    }

    // 在某一偏移量下，根据用户查询订单项列表
    public List<OrderItem> listOrderItemByUser(int uid, int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();

        String sql = "select * from Order_Item where uid = ? and oid=-1 order by id desc limit ?,? ";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, uid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem bean = new OrderItem();
                int id = rs.getInt(1);

                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");

                Product product = new ProductDAO().getProductById(pid);
                if(-1!=oid){
                    Order order= new OrderDAO().getOrderById(oid);
                    bean.setOrder(order);
                }

                User user = new UserDAO().getUserById(uid);
                bean.setProduct(product);

                bean.setUser(user);
                bean.setNumber(number);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    // 查询某一订单下所有订单项
    public List<OrderItem> listOrderItemByOrder(int oid) {
        return listOrderItemByOrder(oid, 0, Short.MAX_VALUE);
    }

    // 根据偏移量查询某一订单下所有订单项
    public List<OrderItem> listOrderItemByOrder(int oid, int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();

        String sql = "select * from Order_Item where oid = ? order by id desc limit ?,? ";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, oid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem bean = new OrderItem();
                int id = rs.getInt(1);

                int pid = rs.getInt("pid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");

                Product product = new ProductDAO().getProductById(pid);
                if(-1!=oid){
                    Order order= new OrderDAO().getOrderById(oid);
                    bean.setOrder(order);
                }

                User user = new UserDAO().getUserById(uid);
                bean.setProduct(product);

                bean.setUser(user);
                bean.setNumber(number);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    /*TODO*/
    public void fill(List<Order> os) {
        for (Order o : os) {
            List<OrderItem> ois = listOrderItemByOrder(o.getId());
            float total = 0;
            int totalNumber = 0;
            for (OrderItem oi : ois) {
                total += oi.getNumber() * oi.getProduct().getPromotePrice();
                totalNumber += oi.getNumber();
            }
            o.setTotal(total);
            o.setOrderItems(ois);
            o.setTotalNumber(totalNumber);
        }
    }

    // 为订单设置订单集合
    public void fill(Order o) {
        List<OrderItem> ois = listOrderItemByOrder(o.getId());
        float total = 0;
        for (OrderItem oi : ois) {
            total += oi.getNumber()*oi.getProduct().getPromotePrice();
        }
        // 设置订单中商品总件数与总金额
        o.setTotal(total);
        o.setOrderItems(ois);
    }

    // 获得某一商品的所有订单项
    public List<OrderItem> listOrderItemByProduct(int pid) {
        return listOrderItemByProduct(pid, 0, Short.MAX_VALUE);
    }

    // 根据偏移量获得某一商品的所有订单项
    public List<OrderItem> listOrderItemByProduct(int pid, int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();

        String sql = "select * from Order_Item where pid = ? order by id desc limit ?,? ";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, pid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem bean = new OrderItem();
                int id = rs.getInt(1);

                int uid = rs.getInt("uid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");

                Product product = new ProductDAO().getProductById(pid);
                if(-1!=oid){
                    Order order= new OrderDAO().getOrderById(oid);
                    bean.setOrder(order);
                }

                User user = new UserDAO().getUserById(uid);
                bean.setProduct(product);

                bean.setUser(user);
                bean.setNumber(number);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    // 获得某一商品销售总量
    public int getSaleCountByProduct(int pid) {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

            String sql = "select sum(number) from Order_Item where pid = " + pid + " and oid != -1";

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }
}
