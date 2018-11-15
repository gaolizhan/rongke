package com.rongke.web.upload;

import com.rongke.enums.FileType;
import com.rongke.enums.FileType1;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@CrossOrigin
@Controller
@RequestMapping(value = "/file/see")
public class FileSeeController {

    @RequestMapping("/download/{filePath}/{fileName:.*}")
    public void download(@PathVariable("type") FileType type, @PathVariable("filePath") String filePath, @PathVariable("fileName") String fileName, HttpServletResponse response, HttpServletRequest request) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        InputStream inputStream = null;
        OutputStream os = null;
        try {
            inputStream = new FileInputStream(FileType.getUploadFile(type, filePath, fileName, request));

            os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close(); // 关闭流
                } catch (IOException ignored) {
                }
            }
            if (os != null) {
                try {
                    os.close(); // 关闭流
                } catch (IOException ignored) {
                }
            }
        }

    }

    public static String getPath(FileType1 type, String filePath, String fileName) {
        return "/ATTACHMENT/" + filePath + "/" + fileName;
    }
}
