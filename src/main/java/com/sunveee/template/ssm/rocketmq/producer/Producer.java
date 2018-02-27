package com.sunveee.template.ssm.rocketmq.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunveee.template.ssm.util.LogUtil;

/**
 * 生产者
 * 
 * @author 51
 * @version $Id: Producer.java, v 0.1 2018年2月26日 下午2:45:40 51 Exp $
 */
public class Producer {
    private static final Logger          logger            = LoggerFactory.getLogger(Producer.class);

    private static DefaultMQProducer     producer          = new DefaultMQProducer("ProducerGroupName");
    private static int                   initialState      = 0;

    private static TransactionMQProducer tran_producer     = new TransactionMQProducer("TranProducerGroupName");
    private static int                   tran_initialState = 0;

    private Producer() {
    }

    /**
     * 默认生产者
     * 
     * @return
     */
    public static DefaultMQProducer getDefaultMQProducer() {
        if (producer == null) {
            producer = new DefaultMQProducer("ProducerGroupName");
        }

        if (initialState == 0) {
            //            producer.setNamesrvAddr("168.33.66.151:9876");
            producer.setNamesrvAddr("168.33.51.218:9876");
            try {
                producer.start();
            } catch (MQClientException e) {
                LogUtil.error(e, logger, "");
                return null;
            }
            initialState = 1;
        }

        return producer;
    }

    /**
     * 事务消息生产者
     * 
     * @return
     */
    public static TransactionMQProducer getTransactionMQProducer() {
        if (tran_producer == null) {
            tran_producer = new TransactionMQProducer("TranProducerGroupName");
        }

        if (tran_initialState == 0) {
            tran_producer.setNamesrvAddr("168.33.66.151:9876");
            try {
                tran_producer.start();
            } catch (MQClientException e) {
                LogUtil.error(e, logger, "");
                return null;
            }
            tran_initialState = 1;
        }

        // 当RocketMQ发现Prepared消息时，会根据这个Listener实现的策略来决断事务
        TransactionCheckListener transactionCheckListener = new TransactionCheckListenerImpl();
        tran_producer.setTransactionCheckListener(transactionCheckListener);

        return tran_producer;
    }
}
