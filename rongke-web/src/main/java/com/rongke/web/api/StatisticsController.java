package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.dto.IndexDTO;
import com.rongke.service.StatisticsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(value = "/api/statistics")
@CrossOrigin
public class StatisticsController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private StatisticsService statisticsService;
    /**
     * @param
     * @return 返回值JsonResp
     * @修改qq
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public JsonResp home() {
        IndexDTO indexDTO = statisticsService.statisticsHomeData();
        return JsonResp.ok();
    }
}
