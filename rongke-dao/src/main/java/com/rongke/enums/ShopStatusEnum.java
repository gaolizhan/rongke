package com.rongke.enums;

/**
 * Created by mifan on 2017/4/21.
 */
public enum ShopStatusEnum {

    RZZ(0, "认证中"),
    RZCG(1, "认证成功"),
    RZSB(2, "认证失败"),
    NULLDATA(3, "");;

    ShopStatusEnum(Integer type, String typeName) {
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
    public static ShopStatusEnum getTypeNameByType(Integer type) {
        for (ShopStatusEnum typeEnum : values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return NULLDATA;
    }

    public static ShopStatusEnum[] getAllAwards() {
        return values();
    }

    public Integer getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
