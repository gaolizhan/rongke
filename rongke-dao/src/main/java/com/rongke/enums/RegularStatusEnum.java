package com.rongke.enums;

/**
 * Created by mifan on 2017/4/21.
 */
public enum RegularStatusEnum {
    ZC(1, "正常"),
    SC(2, "删除"),
    NULLDATA(3, "");


    RegularStatusEnum(Integer type, String typeName) {
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
    public static RegularStatusEnum getUserTypeNameByType(Integer type) {
        for (RegularStatusEnum userTypeEnum : values()) {
            if (userTypeEnum.getType().equals(type)) {
                return userTypeEnum;
            }
        }
        return NULLDATA;
    }

    public static RegularStatusEnum[] getAllAwards() {
        return values();
    }

    public Integer getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
