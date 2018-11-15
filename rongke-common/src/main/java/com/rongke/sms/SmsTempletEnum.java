//package com.rongke.sms;
//
///**
// * Created by mifan on 2017/4/21.
// */
//public enum SmsTempletEnum {
//    LOOKSMS(1,"您推荐的客户已看房成功，详情请查看好房多一我的推荐一已带看，好房多感谢您的推荐！"),
//    BUYSMS(2,"恭喜您推荐的客户已购房签约成功，详情请查看房荐宝一我的推荐一已签约，好房多感谢您的推荐！"),
//    PAYSMS(3,"您的推荐奖励已发放，12小时未到账请致电客服经理13615712338，详情请查看好房多一我的奖励一已发奖励，再次感谢您的推荐！");
//
//
//    SmsTempletEnum(Integer type, String typeName) {
//        this.type = type;
//        this.typeName = typeName;
//    }
//
//    private Integer type;
//
//    private String typeName;
//
//    /**
//     * 根据type 获取typeName
//     * @param type
//     * @return
//     */
//    public static SmsTempletEnum getSmsTempletNameByType(Integer type) {
//        for (SmsTempletEnum userTypeEnum: values()) {
//            if (userTypeEnum.getType().equals(type)) {
//                return userTypeEnum;
//            }
//        }
//        return null;
//    }
//
//    public static SmsTempletEnum[] getAllAwards() {
//        return values();
//    }
//
//    public Integer getType() {
//        return type;
//    }
//
//    public String getTypeName() {
//        return typeName;
//    }
//}
