package org.tmall.dao;

import org.tmall.entity.Order;
import org.tmall.entity.User;
import org.tmall.utils.DBUtil;
import org.tmall.utils.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OrderDAO 用于建立对于 order 对象的 ORM 映射
 */
public class OrderDAO {

    // 获得订单总数
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "select count(*) from Order_";

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    // 增加订单记录
    public void add(Order bean) {

        String sql = "insert into order_ values(null,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            ps.setString(1, bean.getOrderCode());
            ps.setString(2, bean.getAddress());
            ps.setString(3, bean.getPost());
            ps.setString(4, bean.getReceiver());
            ps.setString(5, bean.getMobile());
            ps.setString(6, bean.getUserMessage());

            ps.setTimestamp(7,  DateUtil.d2t(bean.getCreateDate()));
            ps.setTimestamp(8,  DateUtil.d2t(bean.getPayDate()));
            ps.setTimestamp(9,  DateUtil.d2t(bean.getDeliveryDate()));
            ps.setTimestamp(10,  DateUtil.d2t(bean.getConfirmDate()));
            ps.setInt(11, bean.getUser().getId());
            ps.setInt(12, bean.getStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // 更新订单记录
    public boolean update(Order bean) {

        boolean result = false;
        String sql = "update order_ set address= ?, post=?, receiver=?,mobile=?,userMessage=? ,createDate = ? , " +
                "payDate =? , deliveryDate =?, confirmDate = ? , orderCode =?, uid=?, status=? where id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setString(1, bean.getAddress());
            ps.setString(2, bean.getPost());
            ps.setString(3, bean.getReceiver());
            ps.setString(4, bean.getMobile());
            ps.setString(5, bean.getUserMessage());
            ps.setTimestamp(6, DateUtil.d2t(bean.getCreateDate()));;
            ps.setTimestamp(7, DateUtil.d2t(bean.getPayDate()));;
            ps.setTimestamp(8, DateUtil.d2t(bean.getDeliveryDate()));;
            ps.setTimestamp(9, DateUtil.d2t(bean.getConfirmDate()));;
            ps.setString(10, bean.getOrderCode());
            ps.setInt(11, bean.getUser().getId());
            ps.setInt(12, bean.getStatus());
            ps.setInt(13, bean.getId());
            if(ps.executeUpdate() > 0)
                result = true;

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    // 删除订单
    public void delete(int id) {

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "delete from Order_ where id = " + id;

            s.execute(sql);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public Order getOrderById(int id) {
        Order bean = new Order();

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "select * from Order_ where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                String orderCode =rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                int status = rs.getInt("status");
                int uid =rs.getInt("uid");
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d( rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d( rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d( rs.getTimestamp("confirmDate"));

                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserMessage(userMessage);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                User user = new UserDAO().getUserById(uid);
                bean.setUser(user);
                bean.setStatus(status);

                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    // 获得所有订单
    public List<Order> listOrder() {
        return listOrder(0, Short.MAX_VALUE);
    }

    // 根据偏移量获得订单列表
    public List<Order> listOrder(int start, int count) {
        List<Order> beans = new ArrayList<Order>();

        String sql = "select * from Order_ order by id desc limit ?,? ";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, start);
            ps.setInt(2, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order bean = new Order();
                String orderCode =rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                int status = rs.getInt("status");
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d( rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d( rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d( rs.getTimestamp("confirmDate"));
                int uid =rs.getInt("uid");

                int id = rs.getInt("id");
                bean.setId(id);
                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserMessage(userMessage);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                User user = new UserDAO().getUserById(uid);
                bean.setUser(user);
                bean.setStatus(status);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    // 获得除某种状态之外的所有订单
    public List<Order> listOrderExcept(int uid,int excludedStatus) {
        return listOrderExcept(uid,excludedStatus,0, Short.MAX_VALUE);
    }

    // 获得偏移量下除某种状态之外的所有订单
    public List<Order> listOrderExcept(int uid, int excludedStatus, int start, int count) {
        /*TODO*/
        List<Order> beans = new ArrayList<Order>();

        String sql = "select * from Order_ where uid = ? and status != ? order by id desc limit ?,? ";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, uid);
            ps.setInt(2, excludedStatus);
            ps.setInt(3, start);
            ps.setInt(4, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order bean = new Order();
                String orderCode =rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                int status = rs.getInt("status");
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d( rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d( rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d( rs.getTimestamp("confirmDate"));

                int id = rs.getInt("id");
                bean.setId(id);
                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserMessage(userMessage);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                User user = new UserDAO().getUserById(uid);
                bean.setStatus(status);
                bean.setUser(user);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }
}
