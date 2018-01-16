package org.tmall.enums;

/**
 *  订单状态枚举类
 */
public enum OrderStateEnum {
    WAIT_PAY(0, "待付款"),
    WAIT_DELIVERY(1, "待发货"),
    WAIT_CONFIRM(2, "待收货"),
    WAIT_REVIEW(3, "待评价"),
    FINISH(4, "完成"),
    DELETE(5, "删除"),
    UNKNOW(-1, "未知");

    private int id;
    private String stateInfo;

    OrderStateEnum(int id, String stateInfo) {
        this.id = id;
        this.stateInfo = stateInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    // 返回对应状态
    public static OrderStateEnum stateOf(int index){
        for (OrderStateEnum state : values()){
            if (state.getId() == index)
                return state;
        }
        return null;
    }
}
