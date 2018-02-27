package com.sunveee.template.ssm.rocketmq.test;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunveee.template.ssm.rocketmq.consumer.Consumer;
import com.sunveee.template.ssm.util.LogUtil;

public class ReceiveTest {
    private static final Logger logger = LoggerFactory.getLogger(ReceiveTest.class);

    public static void main(String[] args) {
        receiveMsg();
    }

    public static void receiveMsg() {
        // 获取消息消费者
        DefaultMQPushConsumer consumer = Consumer.getDefaultMQPushConsumer();
        // 订阅主体
        try {
            consumer.subscribe("topic1", "*");
            consumer.subscribe("topic2", "*");
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    LogUtil.info(logger, "currentThreadName:{0} and Receive New Messages:\n==={1}", Thread.currentThread().getName(), msgs);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });

            consumer.start();

            LogUtil.info(logger, "Consumer Started.");
        } catch (MQClientException e) {
            LogUtil.error(e, logger, "");
        }
    }
}
