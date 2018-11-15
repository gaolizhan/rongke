package com.rongke.commons;

import java.io.Serializable;

/**
 * 常量类
 *
 * @author weishengbin
 */
public class FrameConstant implements Serializable {
    private static final long serialVersionUID = -8230596451590400669L;

    // 初始化的管理员账号密码
    public static final String USERID = "402880fe56aac5850156aac5999a0000";
    public static final String USER_SUPER_ADMIN_USERNAME = "admin";
    public static final String USER_SUPER_ADMIN_PASSWORD = "admin";

    // 超级管理员
    public static final String USER_SUPER_ADMIN = "超级管理员";
    // 管理员
    public static final String USER_ADMIN = "管理员";
    // 普通用户
    public static final String USER_MEMBER = "普通用户";

	/*----------------------附件---------------------*/

    /* app */
    // 附件类型：app图标
    public static final String ATTACHMENT_TYPE_APP_LOGO = "APP_LOGO";
    // 附件类型：app简介图
    public static final String ATTACHMENT_TYPE_APP_IMAGE = "APP_IMAGE";
    // 附件类型：apk文件
    public static final String ATTACHMENT_TYPE_APP_APK = "APP_APK";

    /* ad */
    // 附件类型：广告图&广告视频
    public static final String ATTACHMENT_TYPE_AD_IMAGE_VIDEO = "AD_IMAGE_VIDEO";

    /* dynamic */
    // 附件类型：动态（分享）图
    public static final String ATTACHMENT_TYPE_DYNAMIC_IMAGE = "DYNAMIC_IMAGE";

}
