package org.tmall.utils;

/**
 *  分页功能工具类
 *  为分页提供必要信息
 */
public class Page {

    // 当前页开始位置
    int start;
    // 每页显示数量
    int count;
    // 总共有多少数据
    int total;
    // 参数 TODO
    String param;

    // 是否有前一页
    public boolean isHasPreviouse(){
        if (start == 0)
            return false;
        return true;
    }

    // 是否有下一页
    public boolean isHasNext(){
        if (start == getLast())
            return false;
        return true;
    }

    // 获得总分页数
    public int getTotalPage(){
        int totalPage;
        // 根据一页显示条目数(count)确定总页数
        if (0 == total % count)
            totalPage = total / count;
        else
            totalPage = total / count + 1;
        if (0 == totalPage)
            totalPage = 1;

        return totalPage;
    }

    // 最后一页第一条记录的序号
    public int getLast(){
        int last;
        if (0 == total % count)
            last = total - count;
        else
            last = total - total % count;
        last = last < 0 ? 0 : last;

        return last;
    }

    public Page(int start, int count) {
        this.start = start;
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

}
