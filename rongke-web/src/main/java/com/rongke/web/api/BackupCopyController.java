package com.rongke.web.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.BooleanArraySerializer;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.mapper.BackupCopyMapper;
import com.rongke.model.MsgModel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.rongke.model.BackupCopy;
import com.rongke.service.BackupCopyService;

import com.rongke.utils.PubLib;
import com.rongke.utils.RequestUtils;
import com.rongke.web.config.Config;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;

/**
 * @BackupCopyController
 * @Controller
 * @version : Ver 1.0
 */

@Component
@RestController
@RequestMapping(value="/api/backupCopy")
@Transactional
@CrossOrigin
public class BackupCopyController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private BackupCopyService backupCopyService;


    /**
     * @param backupCopy
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addBackupCopy(@RequestBody BackupCopy backupCopy) {
        log.debug("添加");
        backupCopyService.insert(backupCopy);
        return JsonResp.ok(backupCopy);
    }

    /**
     * @param backupCopy
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateBackupCopy(@RequestBody BackupCopy backupCopy) {
        log.debug("修改");
        backupCopyService.updateById(backupCopy);
        return JsonResp.ok(backupCopy);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectBackupCopy(Long id) {
        log.debug("查找");
        BackupCopy backupCopy = backupCopyService.selectById(id);
        return JsonResp.ok(backupCopy);
    }


    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id删除
     */
    @RequestMapping(value = "/deleteOne", method = RequestMethod.GET)
    public JsonResp deleteOne(Long id) {
        log.debug("查找");
        Boolean bool = backupCopyService.deleteById(id);
        return JsonResp.ok(bool);
    }


   /* @Scheduled(cron="0/5 * * * * ? ")
    public JsonResp testTimer(){
        MsgModel msgModel=new MsgModel();
        msgModel.setType(1);
        msgModel.setContent("哈哈哈哈哈哈哈");
        return JsonResp.ok();
    }*/





    /**
     * 备份
     *
     * @return
     */
//    @Scheduled(cron = "0 54 09 * * ? ")
    @RequestMapping(value = "/backup", method = RequestMethod.GET)
    public JsonResp backUp(HttpServletRequest request) throws InterruptedException {


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
        return JsonResp.ok(host);
    }

    /**
     * 查询所有的备份文件
     *
     * @return
     */
    @RequestMapping(value = "/selectBackupFile", method = RequestMethod.GET)
    public JsonResp selectBackupFile(Integer pageNo, Integer pageSize) {
        EntityWrapper<BackupCopy> backupCopyEntityWrapper = new EntityWrapper<>();
        Page page = new Page(pageNo, pageSize);
        Page page1 = backupCopyService.selectPage(page, backupCopyEntityWrapper);
        return JsonResp.ok(new PageDto(pageNo, pageSize, page1.getRecords(), page1.getTotal()));

    }

    /**
     * 还原数据库
     *
     * @param path
     * @return
     */
    @RequestMapping(value = "/recover", method = RequestMethod.GET)
    public JsonResp recover(String path) {

/*      String fPath=path.replaceAll("\\\\","\\\\\\\\");

      Boolean bool=false;
      try {

          Runtime rt = Runtime.getRuntime();

          Process child = rt.exec("mysql -h 139.224.190.8 -umifan -pMifan1983 ryjdk");

          OutputStream out = child.getOutputStream();
          String inStr;
          StringBuffer sb = new StringBuffer("");
          String outStr;
          BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fPath), "utf-8"));
          while ((inStr = br.readLine()) != null) {
              sb.append(inStr + "\r\n");
          }
          outStr = sb.toString();
          OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");
          writer.write(outStr);
          writer.flush();
          out.close();
          br.close();
          writer.close();
          bool=true;
      } catch (Exception e) {
          e.printStackTrace();
          bool=false;
      }
      return JsonResp.ok(bool);

  }*/


//String[] path1=path.split("9092");


        String fPath = path.replaceAll("\\\\", "\\\\\\\\");

//String[] path1=path.split("9092");
        String[] p = path.split("9092");

        // String f1Path = p[1].replaceAll("\\\\", "\\\\\\\\");

        Boolean bool = false;
        try {
            Runtime rt = Runtime.getRuntime();

            Process child = rt.exec("mysql -h 139.224.190.8 -umifan -pMifan1983 ryj1");

            OutputStream out = child.getOutputStream();
            String inStr;
            StringBuffer sb = new StringBuffer("");
            String outStr;
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(p[1]), "utf-8"));
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr + "\r\n");
            }
            outStr = sb.toString();
            OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");
            writer.write(outStr);
            writer.flush();
            out.close();
            br.close();
            writer.close();
            bool = true;
        } catch (Exception e) {
            e.printStackTrace();
            bool = false;
        }

        return JsonResp.ok(bool);

    }
}
