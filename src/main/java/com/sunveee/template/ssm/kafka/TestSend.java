package com.sunveee.template.ssm.kafka;

import java.util.Calendar;

/**
 * 测试发送消息至Kafka
 * 
 * @author 51
 * @version $Id: TestSend.java, v 0.1 2018年2月28日 上午10:15:51 51 Exp $
 */
public class TestSend {

    public static void main(String[] args) {
        KafkaUtils.sendMsgToKafka("test", String.valueOf(Calendar.getInstance().getTimeInMillis()), "message body");
        KafkaUtils.closeKafkaProducer();
    }

}
