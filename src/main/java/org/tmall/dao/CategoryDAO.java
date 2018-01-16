package org.tmall.dao;

import org.tmall.entity.Category;
import org.tmall.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  CategoryDAO 用于建立对于 category 对象的 ORM 映射
 *  包含基本CRUD方法
 */
public class CategoryDAO {

    // 获取 category 总数
    public int getTotal(){
        int total = 0;
        try(// 获取数据库连接
            Connection c = DBUtil.getConnection();
            // 创建Statement
            Statement s = c.createStatement();){

            String sql = "select count(*) from category";
            // 获取结果集
            ResultSet rs = s.executeQuery(sql);
            while (rs.next())
                // 获得第一列信息，统计数
                total = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    // 增加新category
    public void add (Category bean){
        String sql = "insert into category values(null, ?)";
        try(Connection c = DBUtil.getConnection();
            // 创建预编译Statement
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);){

            ps.setString(1, bean.getName());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                // 获得第一列信息，类别ID
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 更新 category 信息
    public void update(Category bean){

        String sql = "update category set name = ? where id = ?";
        try(Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);){

            ps.setString(1, bean.getName());
            ps.setInt(2, bean.getId());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除某一类别
    public void delete(int id){

        try(Connection c = DBUtil.getConnection();
            Statement s = c.createStatement();){

            String sql = "delete from category where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 通过 ID 获得类别信息
    public Category getCategoryById(int id){

        Category bean = null;
        try(Connection c = DBUtil.getConnection();
            Statement s = c.createStatement();){

            String sql = "select * from category where id = " + id;
            ResultSet rs = s.executeQuery(sql);

            if (rs.next()){
                bean = new Category();
                // 获得第二列信息，类别名称
                String name = rs.getString(2);
                bean.setName(name);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    // 获得所有类别列表
    public List<Category> listAllCategory(){
        return listCategory(0, Short.MAX_VALUE);
    }

    // 获得指定偏移量的类别列表
    public List<Category> listCategory(int offset, int count){
        List<Category> beans = new ArrayList<>();

        String sql = "select * from category order by id desc limit ?,?";
        try(Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);){
            ps.setInt(1, offset);
            ps.setInt(2, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Category bean = new Category();
                bean.setId(rs.getInt(1));
                bean.setName(rs.getString(2));
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return beans;
    }
}
