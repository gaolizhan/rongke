package com.rongke.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * @author <a href="mailto:rplees.i.ly@gmail.com">rplees</a> date 2010-04-06
 *         {@code} 一些常用的文件操作有关的帮助方法
 */
public class FileUtils {
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 将文件转成base64 字符串
     *
     * @param file 文件
     * @return BASE64
     */
    public static String encodeBase64(File file) {
        // 其进行Base64编码处理
        byte[] data;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(file);
            data = new byte[in.available()];
            int read = in.read(data);
            if (read <= 0) {
                throw new RuntimeException("读取流失败");
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return "data:image/jpg;base64," + encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    /**
     * 文件删除
     *
     * @param file 文件
     * @param thr  删除失败是否抛出异常
     */
    public static void deleteFile(File file, boolean thr) {
        if (file != null) {
            String msg = file + "删除失败!";
            if (!file.delete()) if (thr) {
                throw new RuntimeException(msg);
            } else {
                log.error(msg);
            }
        }
    }

    /**
     * 文件删除
     *
     * @param file 文件
     */
    public static void deleteFile(File file) {
        deleteFile(file, false);
    }

    /**
     * 将文件读出来转化为字符串
     *
     * @param file 源文件，不能是文件夹
     * @return 字符串
     */
    public static String loadFileToString(File file)
            throws FileNotFoundException, IOException {
        BufferedReader br = null;
        // 字符缓冲流，是个装饰流，提高文件读取速度
        br = new BufferedReader(new FileReader(file));
        String fileToString = buffReaderToString(br);
        br.close();
        return fileToString;
    }

    /**
     * 将文件读出来转化为字符串
     *
     * @return 字符串
     * @throws IOException read fail
     */
    private static String buffReaderToString(BufferedReader br)
            throws IOException {
        StringBuffer sb = new StringBuffer();
        String line = br.readLine();
        while (null != line) {
            sb.append(line);
            line = br.readLine();
        }
        return sb.toString();
    }

    /**
     * 流拷贝文件
     *
     * @param tempFile input
     * @param newFile  out
     * @return date
     */
    public static long copyFile(File tempFile, File newFile) throws IOException {
        return copyFile(new FileInputStream(tempFile), newFile);
    }

    /**
     * 流拷贝文件
     */
    private static long copyFile(InputStream is, File newFile)
            throws IOException {
        OutputStream os = new FileOutputStream(newFile);
        long s = IOUtils.copyLarge(is, os);
        os.flush();
        os.close();
        is.close();
        return s;
    }


    /**
     * 删除该目录下所有文件及文件夹
     *
     * @param dir 文件夹
     */
    public static void deleteDir(File... dir) {
        if (null != dir) {
            for (File file : dir) {
                if (null != file && file.isDirectory()) {
                    String[] children = file.list();
                    // 递归删除目录中的子目录下
                    for (int i = 0; i < children.length; i++) {
                        deleteDir(new File(file, children[i]));
                    }
                }
                // 目录此时为空，可以删除
                file.delete();
            }

        }
    }

    public static void saveToFile(String fileName, InputStream in)
            throws IOException {
        FileOutputStream fos;
        BufferedInputStream bis;
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        int size;
        bis = new BufferedInputStream(in);
        fos = new FileOutputStream(fileName);
        //
        while ((size = bis.read(buf)) != -1)
            fos.write(buf, 0, size);
        fos.close();
        bis.close();
    }

    private static boolean isPathFlagEnd(String path) {
        return path.endsWith("/") || path.endsWith("\\");
    }

    private static boolean isPathFlagStart(String path) {
        return path.startsWith("/") || path.startsWith("\\");
    }

    /**
     * 拼接路径，目的是为了保持路径的完整性, 首尾不会验证
     * 如 splicePaths("root", "erji", "iii.jpg") == root/erji/iii.jpg
     * splicePaths("/root/", "/erji/", "/iii.jpg") == /root/erji/iii.jpg
     *
     * @param path 路径
     */
    public static String splicePaths(String... path) {
        if (CollectionUtils.isEmpty(path)) {
            return "";
        }

        StringBuilder buffer = new StringBuilder(path[0]);

        for (int i = 1; i <= path.length - 1; i++) {
            String curr = path[i], pre = path[i - 1];
            boolean slefFlag = isPathFlagStart(curr);//本身取 start
            boolean preFlag = isPathFlagEnd(pre);//前一个取 end

            if (slefFlag && preFlag) {//2个都有
                buffer.append(curr.substring(1, curr.length()));
            } else if (!slefFlag && !preFlag) {
                buffer.append(File.separator).append(curr);
            } else {
                buffer.append(curr);
            }
        }
        return buffer.toString();

    }

    /**
     * 检测第一个文件是否在第二二目录内
     *
     * @param file   文件
     * @param fileIn 目录
     * @return 是=true
     */
    static boolean isInSubDirectory(File file, File fileIn) {
        return file.getPath().startsWith(fileIn.getPath());
    }
}
