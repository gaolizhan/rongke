package com.rongke.enums;

/**
 * Created by mifan on 2017/4/21.
 */
public enum RefundStatusEnum {

    TKZ(1, "退款中"),
    TKCG(2, "退款成功"),
    TKSB(3, "退款失败"),
    NULLDATA(4, "");

    RefundStatusEnum(Integer type, String typeName) {
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
    public static RefundStatusEnum getTypeNameByType(Integer type) {
        for (RefundStatusEnum typeEnum : values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return NULLDATA;
    }

    public static RefundStatusEnum[] getAllAwards() {
        return values();
    }

    public Integer getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
