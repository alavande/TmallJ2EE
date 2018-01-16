package org.tmall.dao;

import org.tmall.entity.Category;
import org.tmall.entity.Product;
import org.tmall.entity.ProductImage;
import org.tmall.utils.DBUtil;
import org.tmall.utils.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  ProductDAO 用于建立对于 product 对象的 ORM 映射
 */
public class ProductDAO {

    // 获得商品总量
    public int getTotal(int cid) {
        int total = 0;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "select count(*) from Product where cid = " + cid;

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    // 新增商品信息
    public void add(Product bean) {

        String sql = "insert into Product values(null,?,?,?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            ps.setString(1, bean.getName());
            ps.setString(2, bean.getSubTitle());
            ps.setFloat(3, bean.getOriginalPrice());
            ps.setFloat(4, bean.getPromotePrice());
            ps.setInt(5, bean.getStock());
            ps.setInt(6, bean.getCategory().getId());
            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
            ps.setTimestamp(8, null);
            ps.execute();

            // 获得自动递增主键，即商品ID
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // 更新商品信息
    public void update(Product bean) {

        String sql = "update Product set name= ?, subTitle=?, originalPrice=?," +
                "promotePrice=?,stock=?, cid = ?, updateDate=? where id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setString(1, bean.getName());
            ps.setString(2, bean.getSubTitle());
            ps.setFloat(3, bean.getOriginalPrice());
            ps.setFloat(4, bean.getPromotePrice());
            ps.setInt(5, bean.getStock());
            ps.setInt(6, bean.getCategory().getId());
            ps.setTimestamp(7, DateUtil.d2t(bean.getUpdateDate()));
            ps.setInt(8, bean.getId());
            ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    // 删除商品信息
    public void delete(int id) {

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "delete from Product where id = " + id;

            s.execute(sql);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // 通过ID查找某一商品
    public Product getProductById(int id) {
        Product bean = new Product();

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();) {

            String sql = "select * from Product where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {

                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float originalPrice = rs.getFloat("originalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                int cid = rs.getInt("cid");
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOriginalPrice(originalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);
                Category category = new CategoryDAO().getCategoryById(cid);
                bean.setCategory(category);
                bean.setCreateDate(createDate);
                bean.setId(id);
                setFirstProductImage(bean);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    // 查找某一分类下的所有商品
    public List<Product> listProductByCategory(int cid) {
        return listProductByCategory(cid,0, Short.MAX_VALUE);
    }

    // 根据偏移量查找某一类别下的所有商品
    public List<Product> listProductByCategory(int cid, int start, int count) {
        List<Product> beans = new ArrayList<Product>();
        Category category = new CategoryDAO().getCategoryById(cid);
        String sql = "select * from Product where cid = ? order by id desc limit ?,? ";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, cid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product bean = new Product();
                int id = rs.getInt(1);
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float originalPrice = rs.getFloat("originalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOriginalPrice(originalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);
                bean.setCreateDate(createDate);
                bean.setId(id);
                bean.setCategory(category);
                setFirstProductImage(bean);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    // 获得所有商品列表
    public List<Product> listProduct() {
        return listProduct(0,Short.MAX_VALUE);
    }

    // 根据偏移量获得商品列表
    public List<Product> listProduct(int start, int count) {
        List<Product> beans = new ArrayList<Product>();

        String sql = "select * from Product limit ?,? ";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, start);
            ps.setInt(2, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product bean = new Product();
                int id = rs.getInt(1);
                int cid = rs.getInt("cid");
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float originalPrice = rs.getFloat("originalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOriginalPrice(originalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);
                bean.setCreateDate(createDate);
                bean.setId(id);

                Category category = new CategoryDAO().getCategoryById(cid);
                bean.setCategory(category);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    // 为分类填充商品集合
    public void fill(List<Category> cs) {
        for (Category c : cs)
            fill(c);
    }

    // 为分类填充商品集合
    public void fill(Category c) {
        List<Product> ps = this.listProductByCategory(c.getId());
        c.setProducts(ps);
    }

    // 为分类按行填充商品集合
    public void fillByRow(List<Category> cs){
        // 竖状导航页中每行所列商品数
        int productNumberEachRow = 8;

        for (Category c : cs){
            // 分类下所有商品
            List<Product> products = c.getProducts();
            // 按行收录的商品集
            List<List<Product>> productsByRow = new ArrayList<>();
            for (int i = 0; i < products.size(); i += productNumberEachRow){
                int size = i + productNumberEachRow;
                size = size > products.size() ? products.size() : size;
                // 按行收录商品子集, 一行收录8个商品
                List<Product> productsOfEachRow = products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            c.setProductsByRow(productsByRow);
        }
    }

    // 设置首页商品图片
    public void setFirstProductImage(Product p) {
        List<ProductImage> pis = new ProductImageDAO().listProductImage(p, ProductImageDAO.type_single);
        if(!pis.isEmpty())
            p.setFirstProductImage(pis.get(0));
    }

    // 设置销量及评价数
    public void setSaleAndReviewNumber(Product p) {
        int saleCount = new OrderItemDAO().getSaleCountByProduct(p.getId());
        p.setSaleCount(saleCount);

        int reviewCount = new ReviewDAO().getTotal(p.getId());
        p.setReviewCount(reviewCount);

    }

    // 为商品集设置销量及评价数
    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product p : products) {
            setSaleAndReviewNumber(p);
        }
    }

    // 根据关键字和偏移量搜索一定商品集
    public List<Product> search(String keyword, int start, int count) {
        List<Product> beans = new ArrayList<Product>();

        if(null==keyword||0==keyword.trim().length())
            return beans;
        String sql = "select * from Product where name like ? limit ?,? ";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1, "%" + keyword.trim() + "%");
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product bean = new Product();
                int id = rs.getInt(1);
                int cid = rs.getInt("cid");
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float originalPrice = rs.getFloat("originalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOriginalPrice(originalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);
                bean.setCreateDate(createDate);
                bean.setId(id);

                Category category = new CategoryDAO().getCategoryById(cid);
                bean.setCategory(category);
                setFirstProductImage(bean);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }
}
