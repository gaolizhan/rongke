package com.rongke.enums;

/**
 * @description:
 * @author: zero
 * @date: 2018-11-06
 * @time: 20:09
 */
public class EveryDayDataType {

    public  enum TYPE
    {
        APPLY_NUM(1,"已申请用户数"),
        LENDING_NUM(2,"已放贷用户数量"),
        REG_NUM(3,"注册用户数量");
        private int key;
        private String value;

        TYPE(int key,String value){
            this.key=key;
            this.value=value;
        }

        public int getKey()
        {
            return this.key;
        }

    }

}
