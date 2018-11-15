package com.rongke.model;



import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: zero
 * @date: 2018-11-06
 * @time: 19:21
 */
@Data
public class EveryDayData implements Serializable {

    private int id;
    private int num;
    private int decrementNum;
    private int type;
    private long channelId;


    private String dateTime;
    private Date createTime;

}
