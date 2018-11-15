package com.rongke.web.upload;

import com.rongke.commons.FailException;
import com.rongke.commons.JsonResp;
import com.rongke.enums.FileType;
import com.rongke.enums.FileType1;
import com.rongke.utils.ArrayUtils;
import com.rongke.utils.FileUtils;
import com.rongke.web.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/attachment1")
public class RestoreFileController {

    @Autowired
    public Config config;

    /**
     * @param files 多文件
     * @param type  文件类型
     * @throws Exception 加密失败
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @CrossOrigin
    public JsonResp<String[]> batch(@RequestParam(value = "files", required = false) MultipartFile[] files, FileType1 type, HttpServletRequest request)
            throws Exception {
        if (ArrayUtils.isEmpty(files)) {
            throw new FailException("文件至少要有一个");
        }
       // String path1[]=;
        String[] paths = FileType1.getPath(type, files.length, request);
        String[] urls = new String[files.length];
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            MultipartFile multipartFile = files[i];
            // 获取文件的后缀
            String suffix;
            try {
                suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
            } catch (Exception e) {
                suffix = "";
            }
          String p= System.getProperty("user.dir");
            String fileName = UUID.randomUUID().toString();
            fileName += suffix;
            File saveFile = new File(path + File.separator + fileName);
            paths[i]=p.concat("\\").concat(fileName);
            multipartFile.transferTo(saveFile);
            urls[i] = config.getUploadHost(request, type, saveFile.getParentFile().getName(), saveFile.getName());
        }
        return new JsonResp<String[]>().data(paths);
    }

    /**
     * @param files 多文件
     * @param type  文件类型
     * @throws Exception 加密失败
     */
    @RequestMapping(method = RequestMethod.POST)
    @CrossOrigin
    public JsonResp<String[]> batch2(File[] files, FileType1 type, HttpServletRequest request)
            throws Exception {
        if (ArrayUtils.isEmpty(files)) {
            throw new FailException("文件至少要有一个");
        }
        String[] paths = FileType1.getPath(type, files.length, request);
        String[] urls = new String[files.length];
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            File file = files[i];
            // 获取文件的后缀
            String suffix;
            try {
                suffix = file.getName().substring(file.getName().lastIndexOf("."));
            } catch (Exception e) {
                suffix = "";
            }

            String fileName = UUID.randomUUID().toString();
            fileName += suffix;
            File saveFile = new File(path + File.separator + fileName);
            FileUtils.copyFile(file, saveFile);
            urls[i] = config.getUploadHost(request, type, saveFile.getParentFile().getName(), saveFile.getName());
        }
        return new JsonResp<String[]>().data(paths);
    }

}
