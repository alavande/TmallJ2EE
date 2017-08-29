package org.tmall.dao;

import org.tmall.entity.Product;
import org.tmall.entity.Property;
import org.tmall.entity.PropertyValue;
import org.tmall.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PropertyValueDAO 用于建立对于 propertyValue 对象的 ORM 映射
 */
public class PropertyValueDAO {

    // 获得商品属性值总数
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "select count(*) from PropertyValue";

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    // 新增属性值
    public void add(PropertyValue bean) {

        String sql = "insert into PropertyValue values(null,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, bean.getProduct().getId());
            ps.setInt(2, bean.getProperty().getId());
            ps.setString(3, bean.getValue());
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

    // 更新属性值信息
    public void update(PropertyValue bean) {

        String sql = "update PropertyValue set pid= ?, ptid=?, value=?  where id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, bean.getProduct().getId());
            ps.setInt(2, bean.getProperty().getId());
            ps.setString(3, bean.getValue());
            ps.setInt(4, bean.getId());
            ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    // 删除属性值
    public void delete(int id) {

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "delete from PropertyValue where id = " + id;

            s.execute(sql);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // 通过ID获得属性值
    public PropertyValue getPropertyValueById(int id) {
        PropertyValue bean = new PropertyValue();

        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

            String sql = "select * from PropertyValue where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                int pid = rs.getInt("pid");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                // pid: 商品ID， ptid: 属性ID
                Product product = new ProductDAO().getProductById(pid);
                Property property = new PropertyDAO().getPropertyById(ptid);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);
                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    // 通过商品ID和属性ID获得属性值
    public PropertyValue getPropertyValueByProductAndProperty(int ptid, int pid ) {
        PropertyValue bean = null;

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "select * from PropertyValue where ptid = " + ptid + " and pid = " + pid;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                bean= new PropertyValue();
                int id = rs.getInt("id");

                String value = rs.getString("value");

                Product product = new ProductDAO().getProductById(pid);
                Property property = new PropertyDAO().getPropertyById(ptid);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);
                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    // 获得所有属性值
    public List<PropertyValue> listPropertyValue() {
        return listPropertyValue(0, Short.MAX_VALUE);
    }

    // 根据偏移量获得属性值
    public List<PropertyValue> listPropertyValue(int start, int count) {
        List<PropertyValue> beans = new ArrayList<PropertyValue>();

        String sql = "select * from PropertyValue order by id desc limit ?,? ";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, start);
            ps.setInt(2, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PropertyValue bean = new PropertyValue();
                int id = rs.getInt(1);

                int pid = rs.getInt("pid");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                Product product = new ProductDAO().getProductById(pid);
                Property property = new PropertyDAO().getPropertyById(ptid);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    // 获取某一商品的所有属性值
    public List<PropertyValue> list(int pid) {
        List<PropertyValue> beans = new ArrayList<PropertyValue>();

        String sql = "select * from PropertyValue where pid = ? order by ptid desc";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, pid);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PropertyValue bean = new PropertyValue();
                int id = rs.getInt(1);

                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                Product product = new ProductDAO().getProductById(pid);
                Property property = new PropertyDAO().getPropertyById(ptid);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    // 初始化某个商品对应的属性值
    public void init(Product p) {
        // 获取某个类别的所有属性
        List<Property> pts= new PropertyDAO().listPropertyByCategory(p.getCategory().getId());

        // 遍历属性值
        for (Property pt: pts) {
            // 根据商品ID和属性ID获得某一属性值
            PropertyValue pv = getPropertyValueByProductAndProperty(pt.getId(), p.getId());
            // 如果属性值不存在，增加属性值
            if(null == pv){
                pv = new PropertyValue();
                pv.setProduct(p);
                pv.setProperty(pt);
                this.add(pv);
            }
        }
    }
}
