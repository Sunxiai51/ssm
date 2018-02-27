package com.sunveee.template.ssm.kafka;

/**
 * 从Kafka接受消息
 * 
 * @author 51
 * @version $Id: TestReceive.java, v 0.1 2018年2月28日 上午10:22:25 51 Exp $
 */
public class TestReceive {

    public static void main(String[] args) {
        KafkaUtils.getMsgFromKafka();
    }

}
