package org.tmall.entity;

/**
 * 用户类
 */
public class User {

    private String name;
    private String password;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // 返回匿名用户名，用于商品评价时
    public String getAnonymousName(){
        if (null == name)
            return null;
        if (name.length() <= 1)
            return "*";
        if (name.length() == 2)
            return name.substring(0, 1) + "*";
        char[] chars = name.toCharArray();
        for (int i = 1; i < chars.length - 1; i++)
            chars[i] = '*';
        return new String(chars);
    }
}
