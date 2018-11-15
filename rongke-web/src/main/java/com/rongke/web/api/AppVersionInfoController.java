package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.model.AppVersionInfo;
import com.rongke.service.AppVersionInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/appVersionInfo")
@CrossOrigin
public class AppVersionInfoController {
    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private AppVersionInfoService appVersionInfoService;

    /**
     * @param
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addAppVersionInfo(@RequestBody AppVersionInfo appVersionInfo) {
        log.debug("添加");
        appVersionInfoService.insert(appVersionInfo);
        return JsonResp.ok(appVersionInfo);


    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateAppVersionInfo(@RequestBody AppVersionInfo appVersionInfo) {
        log.debug("添加");
        appVersionInfoService.updateById(appVersionInfo);
        return JsonResp.ok(appVersionInfo);
    }

    @RequestMapping(value = "/checkUpdate", method = RequestMethod.GET)
    public JsonResp checkUpdate(String versionNo,String app) {
//        if(versionNo == null){
//            return JsonResp.toFail("版本号不能为空");
//        }
//        log.debug("检查是否需要更新");
//        AppVersionInfo appVersionInfo = appVersionInfoService.getLatestVersionInfo(app);
//        if(appVersionInfo !=null){
//            if(!appVersionInfo.getVersionNum().equals(versionNo)){
//                return JsonResp.ok(appVersionInfo);
//            }
//        }
        return JsonResp.ok("最新版本");
    }

}
