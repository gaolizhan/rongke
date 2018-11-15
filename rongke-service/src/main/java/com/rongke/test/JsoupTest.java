package com.rongke.test;

import okhttp3.OkHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JsoupTest {

    @Test
    public void test() throws IOException {
        String url = "https://tenant.51datakey.com/carrier/report_data?data=aX92rOy5T06V01cBryVnWGiEbXnY3nvt3XWY5ERzTAONpgQXkQgIIDLQbf25vl1WYoKAB2G8nnDw5dy4VUhVXy6izCgM4MTKEUOPLfjyhckxjAY4ir3G3JkVFQgSN0deZJpUMoAgOFeDN%2Bt7XFX%2BMuMKSDOM4gtv3rjSh9hPLN4jLJxKY%2BFyuRaVP017YO8d";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpUriRequest request = RequestBuilder.get()//get请求
                .setUri(url)
                .build();
        CloseableHttpResponse response = null;
        String body_response = "";

        try {
            response = client.execute(request);
            body_response = EntityUtils.toString(response.getEntity());

            if (!StringUtils.isEmpty(body_response)) {
                Document document = Jsoup.parse(body_response);
                this.dealWithDocument(document);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dealWithDocument(Document document) {
        System.out.println("------------------- dealWithDocument -------------------");

        Element ele_basic = document.getElementsByTag("table").get(0); // 基本信息
        Element ele_mx = document.getElementsByTag("table").get(12); // 魔蝎数据
        Element ele_sj = document.getElementsByTag("table").get(5); // 社交分析摘要
        Element ele_thsj = document.getElementsByTag("table").get(19); // 通话社交
        Element ele_fx = document.getElementsByTag("table").get(26); // 风险状况
        Element ele_cx = document.getElementsByTag("table").get(1); // 用户信息检测(联系人数据)

        Element ele_thhy = document.getElementsByTag("table").get(30); //  通话活跃
        Element ele_black = document.getElementsByTag("table").get(3); // 黑名单

        Boolean nameCardInCourt = null; // 申请人姓名+身份证号码是否出现在法院黑名单
        Boolean nameCardInFinance = null; // 申请人姓名+身份证号码是否出现在金融机构黑名单
        Boolean namePhoneInFinance = null; // 申请人姓名+手机号码是否出现在金融机构黑名单

        int inTime = 0; // 入网时长
        int eachOtherCount = 0; // 互通电话的号码数目

        int eachOtherNear1MonthCount = 0; // 近1个月通话次数
        int eachOtherNear3MonthCount = 0; // 近3个月通话次数

        int withCourtCount = 0; // 与法院通话次数
        int withLawyerCount = 0; // 与律师通话次数

        int searchCount = 0; // 查询过该用户的相关企业数量
        int registCount = 0; // 电话号码注册过的相关企业数量

        String doc_name = ele_basic.getElementsByTag("tr").get(0).getElementsByTag("td").get(0).text().trim().replace("姓名：", "");
        String doc_idCard = ele_basic.getElementsByTag("tr").get(0).getElementsByTag("td").get(1).text().trim().replace("身份证号:", "");
        inTime = Integer.valueOf(ele_basic.getElementsByTag("tr").get(2).getElementsByTag("td").get(1).text().trim().replace("入网时长：", "").replace("月", "").trim());
        String doc_native = ele_basic.getElementsByTag("tr").get(4).getElementsByTag("td").get(0).text().trim().replace("籍贯：", "");
        String doc_phoneFrom = ele_basic.getElementsByTag("tr").get(4).getElementsByTag("td").get(1).text().trim().replace("手机号码归属地：", "");

        Elements ele_mxs = ele_mx.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        for (Element e_mx : ele_mxs) {
            if ("申请人姓名+身份证号码是否出现在法院黑名单".equals(e_mx.getElementsByTag("td").get(0).text().trim())) {
                if ("否".equals(e_mx.getElementsByTag("td").get(1).text().trim())) {
                    nameCardInCourt = Boolean.FALSE;
                } else {
                    nameCardInCourt = Boolean.TRUE;
                }
                continue;
            }
            if ("申请人姓名+身份证号码是否出现在金融机构黑名单".equals(e_mx.getElementsByTag("td").get(0).text().trim())) {
                if ("否".equals(e_mx.getElementsByTag("td").get(1).text().trim())) {
                    nameCardInFinance = Boolean.FALSE;
                } else {
                    nameCardInFinance = Boolean.TRUE;
                }
                continue;
            }
            if ("申请人姓名+手机号码是否出现在金融机构黑名单".equals(e_mx.getElementsByTag("td").get(0).text().trim())) {
                if ("否".equals(e_mx.getElementsByTag("td").get(1).text().trim())) {
                    namePhoneInFinance = Boolean.FALSE;
                } else {
                    namePhoneInFinance = Boolean.TRUE;
                }
                continue;
            }
        }

        Elements ele_sjs = ele_sj.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        for (Element e_sj : ele_sjs) {
            if ("互通电话的号码数目".equals(e_sj.getElementsByTag("td").get(0).text().trim())) {
                eachOtherCount = Integer.valueOf(e_sj.getElementsByTag("td").get(1).text().trim());
                break;
            }
        }

        Elements ele_thsjs = ele_thsj.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        for (Element e_sj : ele_thsjs) {
            if ("通话号码数目".equals(e_sj.getElementsByTag("td").get(0).text().trim())) {
                eachOtherNear1MonthCount = Integer.valueOf(e_sj.getElementsByTag("td").get(1).text().trim());
                eachOtherNear3MonthCount = Integer.valueOf(e_sj.getElementsByTag("td").get(2).text().trim());
                break;
            }
        }

        Integer with110Near60Count = null;
        Integer withLoanNear60Count = null;

        Elements ele_fxs = ele_fx.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        for (Element e_fx : ele_fxs) {
            if ("与法院通话次数".equals(e_fx.getElementsByTag("td").get(0).text().trim())) {
                withCourtCount = Integer.valueOf(e_fx.getElementsByTag("td").get(3).text().trim());
                continue;
            }
            if ("与律师通话次数".equals(e_fx.getElementsByTag("td").get(0).text().trim())) {
                withLawyerCount = Integer.valueOf(e_fx.getElementsByTag("td").get(3).text().trim());
                continue;
            }

            if ("与110通话次数".equals(e_fx.getElementsByTag("td").get(0).text().trim())) {
                with110Near60Count = Integer.valueOf(e_fx.getElementsByTag("td").get(2).text().trim());
                continue;
            }

            if ("与催收公司通话次数".equals(e_fx.getElementsByTag("td").get(0).text().trim())) {
                withLoanNear60Count = Integer.valueOf(e_fx.getElementsByTag("td").get(2).text().trim());
                continue;
            }
        }

        Elements ele_cxs = ele_cx.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        for (Element e_cx : ele_cxs) {
            if ("查询过该用户的相关企业数量".equals(e_cx.getElementsByTag("td").get(1).text().trim())) {
                searchCount = Integer.valueOf(e_cx.getElementsByTag("td").get(2).text().trim());
                continue;
            }
            if ("电话号码注册过的相关企业数量".equals(e_cx.getElementsByTag("td").get(0).text().trim())) {
                registCount = Integer.valueOf(e_cx.getElementsByTag("td").get(1).text().trim());
                continue;
            }
        }

        Integer blackScore = null;
        Elements ele_blacks = ele_black.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        for (Element e_black : ele_blacks) {
            if ("黑中介分数".equals(e_black.getElementsByTag("td").get(1).text().trim())) {
                blackScore = Integer.valueOf(e_black.getElementsByTag("td").get(2).text().trim().replace("(分数范围0-100，40分以下为高危人群）", "").trim());
                continue;
            }
        }

        Integer near30Off = null;
        Integer near60Off = null;
        Integer near90Off = null;

        Elements ele_thhys = ele_thhy.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        for (Element e_thhy : ele_thhys) {
            if ("关机天数".equals(e_thhy.getElementsByTag("td").get(0).text().trim())) {
                near30Off = Integer.valueOf(e_thhy.getElementsByTag("td").get(1).text().trim());
                near60Off = Integer.valueOf(e_thhy.getElementsByTag("td").get(2).text().trim());
                near90Off = Integer.valueOf(e_thhy.getElementsByTag("td").get(3).text().trim());
                continue;
            }
        }

        System.out.println("姓名 ： " + doc_name);
        System.out.println("身份证号 ： " + doc_idCard);
        System.out.println("申请人姓名+身份证号码是否出现在法院黑名单 ： " + nameCardInCourt);
        System.out.println("申请人姓名+身份证号码是否出现在金融机构黑名单 ： " + nameCardInFinance);
        System.out.println("申请人姓名+手机号码是否出现在金融机构黑名单 ： " + namePhoneInFinance);
        System.out.println("入网时长 ： " + inTime);
        System.out.println("互通电话的号码数目 ： " + eachOtherCount);
        System.out.println("近1个月互通电话的号码数目 ： " + eachOtherNear1MonthCount);
        System.out.println("近3个月互通电话的号码数目 ： " + eachOtherNear3MonthCount);
        System.out.println("与法院通话次数 ： " + withCourtCount);
        System.out.println("与律师通话次数 ： " + withLawyerCount);
        System.out.println("查询过该用户的相关企业数量 ： " + searchCount);
        System.out.println("电话号码注册过的相关企业数量 ： " + registCount);

        System.out.println("黑中介分数 ： " + blackScore);
        System.out.println("籍贯 ： " + doc_native);
        System.out.println("归属地 ： " + doc_phoneFrom);

        System.out.println("30天关机 ： " + near30Off);
        System.out.println("60天关机 ： " + near60Off);
        System.out.println("90天关机 ： " + near90Off);

        System.out.println("1106个月 ： " + with110Near60Count);
        System.out.println("催收6个月 ： " + withLoanNear60Count);

        System.out.println("--------------------------------------------");
    }
}