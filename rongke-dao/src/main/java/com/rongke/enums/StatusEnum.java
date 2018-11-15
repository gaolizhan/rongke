package com.rongke.enums;

/**
 * Created by mifan on 2017/4/21.
 */
public enum StatusEnum {

    ZC(1, "正常"),
    YC(2, "异常"),
    NULLDATA(3, "");;


    StatusEnum(Integer type, String typeName) {
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
    public static StatusEnum getTypeNameByType(Integer type) {
        for (StatusEnum typeEnum : values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return NULLDATA;
    }

    public static StatusEnum[] getAllAwards() {
        return values();
    }

    public Integer getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
