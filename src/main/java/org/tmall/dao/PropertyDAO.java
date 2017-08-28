package org.tmall.dao;

import org.tmall.entity.Category;
import org.tmall.entity.Property;
import org.tmall.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  PropertyDAO 用于建立对于 property 对象的 ORM 映射
 */
public class PropertyDAO {

    // 获取 property 总数
    public int getTotal(int cid) {
        int total = 0;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "select count(*) from Property where cid =" + cid;

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    // 新增 property
    public void add(Property bean) {

        String sql = "insert into Property values(null,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, bean.getCategory().getId());
            ps.setString(2, bean.getName());
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

    // 更新 property
    public void update(Property bean) {

        String sql = "update Property set cid= ?, name=? where id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, bean.getCategory().getId());
            ps.setString(2, bean.getName());
            ps.setInt(3, bean.getId());
            ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    // 删除 property
    public void delete(int id) {

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "delete from Property where id = " + id;

            s.execute(sql);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // 通过所属类别和属性名获取属性信息
    public Property getPropertyByNameAndCategory(String name, int cid) {
        Property bean =null;

        String sql = "select * from Property where name = ? and cid = ?";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, cid);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                bean = new Property();
                bean.setName(name);
                Category category = new CategoryDAO().getCategoryById(cid);
                bean.setCategory(category);
                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    // 通过属性ID获取属性信息
    public Property getPropertyById(int id) {
        Property bean = new Property();

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "select * from Property where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {

                String name = rs.getString("name");
                int cid = rs.getInt("cid");
                bean.setName(name);
                Category category = new CategoryDAO().getCategoryById(cid);
                bean.setCategory(category);
                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    // 获取特定类别下的所有属性
    public List<Property> listPropertyByCategory(int cid) {
        return listPropertyByCategroy(cid, 0, Short.MAX_VALUE);
    }

    // 获取特定类别下一定偏移量的属性
    public List<Property> listPropertyByCategroy(int cid, int start, int count) {
        List<Property> beans = new ArrayList<Property>();

        String sql = "select * from Property where cid = ? order by id desc limit ?,? ";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, cid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Property bean = new Property();
                int id = rs.getInt(1);
                String name = rs.getString("name");
                bean.setName(name);
                Category category = new CategoryDAO().getCategoryById(cid);
                bean.setCategory(category);
                bean.setId(id);

                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }
}
