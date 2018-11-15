//package test;
//
//import com.rongke.web.risk.RiskTaskConsumer;
//import com.rongke.web.risk.RiskTaskProducer;
//import com.rongke.web.risk.RiskTaskTest;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//public class RiskTest {
//
//    @Autowired
//    private RiskTaskConsumer taskConsumer;
//    @Autowired
//    private RiskTaskProducer taskProducer;
//
//    @Test
//    public void test() {
//        System.out.println("------------ start ------------");
//
//        new Thread(taskProducer).start();
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        new Thread(taskConsumer).start();
//    }
//}