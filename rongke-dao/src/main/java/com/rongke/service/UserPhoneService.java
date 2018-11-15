package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.dto.MxDTO;
import com.rongke.model.UserPhone;

import java.util.List;
import java.util.Map;

/**
 * @UserPhoneService
 * @Service
 * @version : Ver 1.0
 */
public interface UserPhoneService extends IService<UserPhone>{
    List<UserPhone> selectByPage(Map<String,Object> map);
    Integer selectCount(Map<String,Object> map);

    @Deprecated
    void mxCallbackRealWoker(MxDTO dto, String realPath);

    /**
     * 可以拉取原始数据
     请求URL：https://api.51datakey.com/carrier/v3/mobiles/" + "13429679833" + "/mxdata-ex
     若拉取成功，表user_phone获取数据，若report_status =1 则修改bill_status = 1,status = 1
     若report_status =0 则修改bill_status = 1
     * @param dto
     * @param realPath
     */
    void mxBillNoticeWorker(MxDTO dto, String realPath);

    /**
     * 可以拉取资信报告
     请求URL：https://api.51datakey.com/carrier/v3/mobiles/" + mobile + "/mxreport
     若拉取成功，表user_phone获取数据，若bill_status =1 则修改report_status = 1,status = 1
     若report_status =0 则修改report_status = 1
     * @param dto
     * @param realPath
     */
    void mxReportWorker(MxDTO dto, String realPath);

}
