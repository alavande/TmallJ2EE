package org.tmall.dao;

import org.tmall.entity.Product;
import org.tmall.entity.ProductImage;
import org.tmall.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  ProductImageDAO 用于建立对于 productImage 对象的 ORM 映射
 */
public class ProductImageDAO {

    // 商品图片分类，商品单个图片，商品详情图片
    public static final String type_single = "type_single";
    public static final String type_detail = "type_detail";

    // 获得图片记录总数
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "select count(*) from ProductImage";

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    // 添加商品图片记录
    public void add(ProductImage bean) {

        String sql = "insert into ProductImage values(null,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, bean.getProduct().getId());
            ps.setString(2, bean.getType());
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

    // 更新图片记录
    public void update(ProductImage bean) {

    }

    // 删除图片记录
    public void delete(int id) {

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "delete from ProductImage where id = " + id;

            s.execute(sql);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // 通过ID获得商品图片
    public ProductImage getProductImageById(int id) {
        ProductImage bean = new ProductImage();

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "select * from ProductImage where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                int pid = rs.getInt("pid");
                String type = rs.getString("type");
                Product product = new ProductDAO().getProductById(pid);
                bean.setProduct(product);
                bean.setType(type);
                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    // 获得某一类的所有商品图片
    public List<ProductImage> listProductImage(Product p, String type) {
        return listProductImage(p, type,0, Short.MAX_VALUE);
    }

    // 根据偏移量，获得某一类的商品图片
    public List<ProductImage> listProductImage(Product p, String type, int start, int count) {
        List<ProductImage> beans = new ArrayList<ProductImage>();

        String sql = "select * from ProductImage where pid =? and type =? order by id desc limit ?,? ";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, p.getId());
            ps.setString(2, type);

            ps.setInt(3, start);
            ps.setInt(4, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ProductImage bean = new ProductImage();
                int id = rs.getInt(1);

                bean.setProduct(p);
                bean.setType(type);
                bean.setId(id);

                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }
}
