package com.px.business.app.order.enums;

/*
 * @author 品讯科技
 * @date 2024-08
 * @Description 订单状态
 */

public enum OrderStateEnum {

    /*
    * 订单状态 1-待付款 2-付款中 3-支付超时 4-取消支付
    * 5-已付款 6-已发货 7-已退款 8-确认收货 9-待评价 10-已完成
    * ps: 1、如果是拼团, 未成功失败的需要退款  2、如果是购物车, 需多订单一起支付


 */


    TO_BE_PAID("待付款",1),
    IN_PAYMENT("付款中",2),
    PAYMENT_FAILED("支付超时",3),
    CANCEL_PAYMENT("取消支付",4),
    PAID("已付款",5),
    DELIVERD("已发货",6),
    REFUNDED("已退款",7),
    CONFIRM_RECEIPT("确认收货",8),
    TO_BE_EVALUATED("待评价",9),
    COMPLETED("已完成",10),
    SALE("已售后",11);


    private String type;
    private int typeId;

    OrderStateEnum(String type,int typeId){
        this.type = type;
        this.typeId = typeId;
    }

    public String getType(){
        return type;
    }
    public int getTypeId(){
        return typeId;
    }
}
