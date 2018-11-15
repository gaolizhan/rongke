package com.rongke.utils;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * excel工具类
 * Created by mifan on 2017/5/4.
 */
public class ExcelUtils {

    /**
     * 导出数据
     *
     * @param filename 文件名
     * @param headList excel头标题
     * @param dataList excel数据
     * @param response
     * @throws Exception
     */
    public static void exportData(String filename, List<String> headList, List<List<String>> dataList, HttpServletResponse response) throws Exception {
        if (null == filename || "".equals(filename)) {
            filename = "导出数据";
        }
        OutputStream os = response.getOutputStream();
        response.reset();
        filename = new String(filename.getBytes("GBK"), "ISO8859_1");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xls");
        response.setContentType("application/msexcel");
        WritableWorkbook workbook = Workbook.createWorkbook(os);
        WritableSheet sheet = workbook.createSheet("sheet01", 0);

        int rowIdx = 1;
        for (int i = 0; i < dataList.size(); i++) {
            //设置表头
            if (i % 50000 == 0) {
                if (i != 0) {
                    sheet = workbook.createSheet("sheet0" + (i / 50001 + 1), i / 50001);
                }
                for (int m = 0; m < headList.size(); m++) {
                    jxl.write.Label label = new jxl.write.Label(m, 0, headList.get(m));
                    //设置列宽100
                    sheet.setColumnView(m, 15);
                    sheet.addCell(label);
                }
                rowIdx = 1;
            }

            //填充数据
            for (int m = 0; m < dataList.get(i).size(); m++) {
                List<String> list = dataList.get(i);
                jxl.write.Label label = new jxl.write.Label(m, rowIdx, list.get(m));
                sheet.addCell(label);
            }
            rowIdx++;
        }
        workbook.write();
        workbook.close();
        os.close();
    }

    public static void main(String args[]) {
        List<String> headList = new ArrayList<>();
        headList.add("测试1");
        headList.add("测试2");
        headList.add("测试3");
        headList.add("测试4");

        List<List<String>> dataList = new ArrayList<>();
        for (int i = 0; i < 60000; i++) {
            List<String> list = new ArrayList<>();
            list.add("数据1" + i);
            list.add("数据2" + i);
            list.add("数据3" + i);
            list.add("数据4" + i);
            dataList.add(list);
        }

        System.out.println("Hello World!");
    }
    //下载路径
    public static void download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(
                    response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
