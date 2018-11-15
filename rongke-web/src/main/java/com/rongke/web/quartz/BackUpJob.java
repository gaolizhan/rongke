package com.rongke.web.quartz;

import com.rongke.commons.JsonResp;
import com.rongke.model.BackupCopy;
import com.rongke.service.BackupCopyService;
import com.rongke.utils.PubLib;
import com.rongke.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mifan on 2017/9/18.
 */



@Component("BackUpJob")
@Transactional
@CrossOrigin
public class BackUpJob {

    @Autowired
    private BackupCopyService backupCopyService;


    public void backUp(HttpServletRequest request) throws InterruptedException {


        BackupCopy backupCopy = new BackupCopy();
/*   String savePath=System.getProperty("user.dir");
        File saveFile = new File(savePath+="\\backup");
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if (!savePath.endsWith(File.separator)) {
            savePath = savePath + File.separator;
        }
        System.out.println(new Date());
        System.out.println(savePath); String address=savePath.concat(name);
*/
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String date1 = format.format(date);
        String name = date1.concat(".sql");

        // String address0=PubLib.getFileDir(request);

//        String address1=config.getUploadHost(request);

        String address1 = PubLib.getFileDir(request) + "/" + name;
        //String address=address1.replace("\\\\","/");
        String host = RequestUtils.getHostPath(request);
        String upUrl = host + "/ATTACHMENT/" + name;
        System.out.println(host);
        //System.out.println(address);
        backupCopy.setAddress(upUrl);
        backupCopy.setName(name);


        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(address1), "utf8"));
            Process process = Runtime.getRuntime().exec("mysqldump -h139.224.190.8 -umifan -pMifan1983 --set-charset=UTF8 ryj1");
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                printWriter.println(line);
            }
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(new Date());
        backupCopyService.insert(backupCopy);
    }
}
