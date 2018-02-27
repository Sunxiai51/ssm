package com.sunveee.template.ssm.kafka;

import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sunveee.template.ssm.util.LogUtil;

/**
 * Kafka工具类
 * <p>封装了生产者、消费者的配置和一些基本操作</p>
 * 
 * @author 51
 * @version $Id: KafkaUtils.java, v 0.1 2018年2月28日 上午10:03:42 51 Exp $
 */
public class KafkaUtils {
    private final static Logger             logger            = LoggerFactory.getLogger(KafkaUtils.class);

    /**
     * 生产者
     */
    private static Producer<String, String> producer;
    /**
     * 消费者
     */
    private static Consumer<String, String> consumer;
    /**
     * 服务器地址，多个地址以英文半角逗号隔开
     */
    private static final String             BOOTSTRAP_SERVERS = "168.33.66.151:9092";
    /**
     * 所使用的topic，注意：该topic无法在客户端创建，需要在服务器端通过脚本创建
     */
    private static final String             TOPIC_NAME        = "test";

    private KafkaUtils() {
    }

    /** 
     * 生产者配置
     */
    static {
        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);
    }

    /** 
     * 消费者配置
     */
    static {
        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(TOPIC_NAME)); // 订阅相关主题
    }

    /**
     * 发送消息
     * 
     * @param topic
     * @param key
     * @param msg
     * @return
     */
    public static Future<RecordMetadata> sendMsgToKafka(String topic, String key, String msg) {
        // 发送消息，这之前可能需要对输入的topic进行校验：该topic是否配置在了TOPIC_NAMES中
        Future<RecordMetadata> result = producer.send(new ProducerRecord<String, String>(topic, String.valueOf(new Date().getTime()), msg));

        LogUtil.info(logger, "发送消息返回:{0}", JSON.toJSONString(result));
        return result;
    }

    /** 
     * 接收消息
     */
    public static void getMsgFromKafka() {
        // 建议使用线程池方式实现，这里仅作示例
        while (true) {
            ConsumerRecords<String, String> records = KafkaUtils.getKafkaConsumer().poll(100);
            if (records.count() > 0) {
                for (ConsumerRecord<String, String> record : records) {
                    LogUtil.info(logger, "从kafka接收到消息：" + record.value());
                }
            }
        }
    }

    public static Consumer<String, String> getKafkaConsumer() {
        return consumer;
    }

    public static void closeKafkaProducer() {
        producer.close();
    }

    public static void closeKafkaConsumer() {
        consumer.close();
    }
}
