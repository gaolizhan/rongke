package com.rongke.enums;


import com.rongke.utils.PubLib;
import com.rongke.utils.URLUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

/**
 * 文件 <br>
 */
public enum FileType {
    USER_LOGO("用户图片", "userPic"),

    CAROUSEL_FIGURE("轮播图","carouselPic"),

    OTHER("其他文件", "otherPic");
    /**
     * 文件系统路径
     */
    public static String filePath = null;

    /**
     * 说明
     */
    private String explain;

    /**
     * 绝对路径
     */
    private String absolutePath;

    /**
     * 文件请求地址
     */
    private String fileUrl;


    /**
     * 说明
     */
    private String desc;

    /**
     * 子路径
     */
    private String path;

    public String getPath() {
        return path;
    }

    /*FileType(String explain, String path) {
        this.explain = explain;
        this.path = path;
    }
*/
    FileType(String desc, String path) {
        this.desc = desc;
        this.path = path;

        String classRootPath = FileType.class.getClassLoader().getResource(".").getFile();
        File classFile = new File(classRootPath);
        this.absolutePath = classFile.getParentFile().getParent() + File.separator + "pic"+ File.separator + path;
        this.fileUrl = "/pic/" + path;
    }


    public void setPath(String path) {
        this.path = path;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 获取文件上传路径
     *
     * @param fileType 文件类型
     * @param path     路径
     * @param name     名称
     * @return 文件
     */
    public static File getUploadFile(FileType fileType, String path, String name, HttpServletRequest request) {
        return new File(PubLib.getFileDir(request) + File.separator + path + File.separator + name);
    }


    /**
     * 获取文件路径
     *
     * @param fileType 文件类型
     * @param size     文件数量
     * @return 绝对路径
     */
    public static String[] getPath(FileType fileType, int size, HttpServletRequest request) {
        String genderPath = genderPath(fileType);
        if (genderPath == null || genderPath.equals("") || genderPath.equals("null\\files") || genderPath.equals("null/files")) {
            genderPath = PubLib.getFileDir(request);
        }
        String[] listDir = new String[size];
        for (int i = 0; i < size; i++) {
            String finalDir = null;
            File baseFile = new File(genderPath);
            if (!baseFile.exists()) {
                boolean mkdirs = baseFile.mkdirs();
                if (!mkdirs) {
                    throw new RuntimeException("文件夹创建失败");
                }
            }
            File[] baseFileList = baseFile.listFiles();
            boolean isFree = false;
            assert baseFileList != null;
            if (baseFileList.length > 0) {
                for (File file : baseFileList) {
                    if (file.isDirectory()) {
                        baseFile = file;
                        finalDir = genderPath + File.separator + file.getName();
                        isFree = true;
                        break;
                    }
                }
            }
            if (!isFree) {
                String newDirName = UUID.randomUUID().toString();
                baseFile = new File(baseFile.getPath(), newDirName);
                boolean mkdirs = baseFile.mkdirs();
                if (!mkdirs) {
                    throw new RuntimeException("文件夹创建失败");
                }
                finalDir = genderPath + File.separator + newDirName;
            }
            listDir[i] = finalDir;
        }
        return listDir;
    }

    /**
     * 生产基本路径
     *
     * @param path 子路径
     * @return 路径地址
     */
    public static String genderPath(String path) {
        return filePath + File.separator + path;
    }

    /**
     * 生产基本路径
     *
     * @param fileType 子路径
     * @return 路径地址
     */
    public static String genderPath(FileType fileType) {
        fileType = fileTypeInit(fileType);
        return genderPath(fileType.getPath());
    }

    /**
     * 获取最终路径的URl地址
     *
     * @param finalDir 文件夹路径
     * @return URI
     */
    public static String getUriPath(String finalDir) {
        finalDir = finalDir.substring(filePath.length());
        return URLUtils.filePathParseUri(finalDir);
    }

    /**
     * 文件类型初始化
     *
     * @param fileType 文件类型
     * @return 初始化后
     */
    public static FileType fileTypeInit(FileType fileType) {
        if (fileType == null) {
            fileType = FileType.OTHER;
        }
        return fileType;
    }
}