package com.sunveee.template.ssm.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

/**
 * 消费者
 * 
 * @author 51
 * @version $Id: Consumer.java, v 0.1 2018年2月26日 下午2:47:14 51 Exp $
 */
public class Consumer {
    private static DefaultMQPushConsumer consumer     = new DefaultMQPushConsumer("ConsumerGroupName");
    private static int                   initialState = 0;

    private Consumer() {
    }

    public static DefaultMQPushConsumer getDefaultMQPushConsumer() {
        if (consumer == null) {
            consumer = new DefaultMQPushConsumer("ConsumerGroupName");
        }

        if (initialState == 0) {
            //            consumer.setNamesrvAddr("168.33.66.151:9876");
            consumer.setNamesrvAddr("168.33.51.218:9876");
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            initialState = 1;
        }

        return consumer;
    }

}
