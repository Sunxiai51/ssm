package com.sunveee.template.ssm.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sunveee.template.ssm.util.LogUtil;

/**
 * Kafka监听服务
 * 
 * @author 51
 * @version $Id: KafkaConsumerServer.java, v 0.1 2018年3月6日 下午5:03:53 51 Exp $
 */
public class KafkaConsumerServer implements MessageListener<String, String> {
    protected static final Logger logger = LoggerFactory.getLogger(KafkaConsumerServer.class);

    @Override
    public void onMessage(ConsumerRecord<String, String> record) {
        LogUtil.info(logger, "KafkaConsumerServer开始消费");
        LogUtil.info(logger, "KafkaConsumerServer接收到record={0}", JSON.toJSONString(record, SerializerFeature.UseSingleQuotes));
        LogUtil.info(logger, "KafkaConsumerServer消费完成");
    }

}