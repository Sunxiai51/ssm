package com.sunveee.template.ssm.rocketmq.test;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunveee.template.ssm.rocketmq.producer.Producer;
import com.sunveee.template.ssm.util.LogUtil;

public class SendTest {
    private static final Logger logger = LoggerFactory.getLogger(SendTest.class);

    public static void main(String[] args) {
        sendMsg();
        sendTranMsg();
    }

    /**
     * 模拟发送普通消息
     */
    public static void sendMsg() {
        // 获取消息生产者
        DefaultMQProducer producer = Producer.getDefaultMQProducer();

        Message msg = new Message("topic1", "tag1", "key1", ("body1").getBytes());
        try {
            LogUtil.info(logger, "发送消息:{0}", msg);
            SendResult sendResult = producer.send(msg);
            LogUtil.info(logger, "发送消息结果:{0}", sendResult);
        } catch (Exception e) {
            LogUtil.error(e, logger, "");
        }
        producer.shutdown();
    }

    /**
     * 模拟发送事务消息
     */
    public static void sendTranMsg() {
        // 构造事务消息的生产者
        TransactionMQProducer tran_producer = Producer.getTransactionMQProducer();

        // 本地事务的处理逻辑
        TransactionExecuterImpl tranExecuter = new TransactionExecuterImpl();

        Message msg = new Message("topic2", "tag2", "key2", ("body2").getBytes());

        // 发送消息
        try {
            LogUtil.info(logger, "发送tran消息:{0}", msg);
            SendResult sendResult = tran_producer.sendMessageInTransaction(msg, tranExecuter, null);
            LogUtil.info(logger, "发送tran消息结果:{0}", sendResult);
        } catch (MQClientException e) {
            LogUtil.error(e, logger, "");
        }

        //      tran_producer.shutdown();

        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            LogUtil.error(e, logger, "");
        }

    }

}
