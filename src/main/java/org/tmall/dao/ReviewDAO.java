package org.tmall.dao;

import org.tmall.entity.Product;
import org.tmall.entity.Review;
import org.tmall.entity.User;
import org.tmall.utils.DBUtil;
import org.tmall.utils.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  ReviewDAO 用于建立对于 review 对象的 ORM 映射
 */
public class ReviewDAO {

    // 获取评价总数
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "select count(*) from Review";

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    // 获得某一商品下评价总数
    public int getTotal(int pid) {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

            String sql = "select count(*) from Review where pid = " + pid;

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    // 增加评价记录
    public void add(Review bean) {

        String sql = "insert into Review values(null,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setString(1, bean.getContent());
            ps.setInt(2, bean.getUser().getId());
            ps.setInt(3, bean.getProduct().getId());
            ps.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));

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

    // 更新评价
    public void update(Review bean) {

        String sql = "update Review set content= ?, uid=?, pid=? , createDate = ? where id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1, bean.getContent());
            ps.setInt(2, bean.getUser().getId());
            ps.setInt(3, bean.getProduct().getId());
            ps.setTimestamp(4, DateUtil.d2t( bean.getCreateDate()) );
            ps.setInt(5, bean.getId());
            ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    // 删除评价
    public void delete(int id) {

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "delete from Review where id = " + id;

            s.execute(sql);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // 根据评价ID获得具体评价
    public Review getReviewById(int id) {
        Review bean = new Review();

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "select * from Review where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                int pid = rs.getInt("pid");
                int uid = rs.getInt("uid");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                String content = rs.getString("content");

                Product product = new ProductDAO().getProductById(pid);
                User user = new UserDAO().getUserById(uid);

                bean.setContent(content);
                bean.setCreateDate(createDate);
                bean.setProduct(product);
                bean.setUser(user);
                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    // 获得某一商品下所有评价
    public List<Review> listReview(int pid) {
        return listReview(pid, 0, Short.MAX_VALUE);
    }

    // 根据偏移量获得某一商品下所有评价
    public List<Review> listReview(int pid, int start, int count) {
        List<Review> beans = new ArrayList<Review>();

        String sql = "select * from Review where pid = ? order by id desc limit ?,? ";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, pid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Review bean = new Review();
                int id = rs.getInt(1);

                int uid = rs.getInt("uid");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                String content = rs.getString("content");

                Product product = new ProductDAO().getProductById(pid);
                User user = new UserDAO().getUserById(uid);

                bean.setContent(content);
                bean.setCreateDate(createDate);
                bean.setId(id);
                bean.setProduct(product);
                bean.setUser(user);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    // 判断某个商品下某条评价是否存在
    public boolean isExist(String content, int pid) {

        String sql = "select * from Review where content = ? and pid = ?";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, content);
            ps.setInt(2, pid);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }
}
