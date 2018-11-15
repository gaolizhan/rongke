package com.rongke.enums;

/**
 * Created by mifan on 2017/4/21.
 */
public enum OrderStatusEnum {

    WZF(0, "未支付"),
    ZFCG(1, "支付成功"),
    QRSH(2, "确认收货"),
    YPJ(3, "已评价"),
    HSC(4, "客户删除"),
    SJSC(5, "商家删除"),
    DSC(6, "买卖都删除"),
    DDQX(7, "订单取消"),
    SQTUZ(8, "订单申请退款中"),
    TKSB(9, "退款失败"),
    TKCG(10, "退款成功"),
    NULLDATA(11, "");


    OrderStatusEnum(Integer type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    private Integer type;

    private String typeName;

    /**
     * 根据type 获取typeName
     *
     * @param type
     * @return
     */
    public static OrderStatusEnum getTypeNameByType(Integer type) {
        for (OrderStatusEnum typeEnum : values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return NULLDATA;
    }

    public static OrderStatusEnum[] getAllAwards() {
        return values();
    }

    public Integer getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
