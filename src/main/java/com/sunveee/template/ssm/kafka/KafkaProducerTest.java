package com.sunveee.template.ssm.kafka;

import java.util.Map;

import com.sunveee.template.ssm.kafka.producer.KafkaProducerServer;

public class KafkaProducerTest {
    public static void main(String[] args) {
        KafkaProducerServer kafkaProducer = new KafkaProducerServer();
        String topic = "orderTopic";
        String value = "test";
        boolean ifPartition = false;
        Integer partitionNum = 3;
        String role = "test";//用来生成key
        Map<String, Object> res = kafkaProducer.sendMessage(topic, value, ifPartition, partitionNum, role);

        System.out.println("测试结果如下：===============");
        String message = (String) res.get("message");
        String code = (String) res.get("code");

        System.out.println("code:" + code);
        System.out.println("message:" + message);
    }
}
