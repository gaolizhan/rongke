package com.rongke.enums;

/**
 * Created by mifan on 2017/4/21.
 */
public enum UserTypeEnum {
    HOUSESALE(1, "售楼处置业顾问"),
    HOUSEMAN(2, "房地产职业者"),
    INTERMEDIARY(3, "中介经纪人"),
    INSURANCESALES(4, "保险销售顾问"),
    CARSALES(5, "汽车销售顾问"),
    FINANCE(6, "金融职业者"),
    OTHERMANS(7, "其他工作者"),
    NULLDATA(8, "");


    UserTypeEnum(Integer type, String typeName) {
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
    public static UserTypeEnum getUserTypeNameByType(Integer type) {
        for (UserTypeEnum userTypeEnum : values()) {
            if (userTypeEnum.getType().equals(type)) {
                return userTypeEnum;
            }
        }
        return NULLDATA;
    }

    public static UserTypeEnum[] getAllAwards() {
        return values();
    }

    public Integer getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
