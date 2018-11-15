package com.rongke.enums;

/**
 * Created by mifan on 2017/4/21.
 */
public enum AddressStatusEnum {

    ZCZT(1, "正常状态"),
    QY(2, "弃用"),
    NULLDATA(3, "");;


    AddressStatusEnum(Integer type, String typeName) {
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
    public static AddressStatusEnum getTypeNameByType(Integer type) {
        for (AddressStatusEnum typeEnum : values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return NULLDATA;
    }

    public static AddressStatusEnum[] getAllAwards() {
        return values();
    }

    public Integer getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
