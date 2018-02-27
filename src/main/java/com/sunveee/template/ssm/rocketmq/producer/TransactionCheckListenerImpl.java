package com.sunveee.template.ssm.rocketmq.producer;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunveee.template.ssm.util.LogUtil;

public class TransactionCheckListenerImpl implements TransactionCheckListener {
    private static final Logger logger = LoggerFactory.getLogger(TransactionCheckListenerImpl.class);

    @Override
    public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
        LogUtil.info(logger, "check本地事务状态：COMMIT_MESSAGE");
        return LocalTransactionState.COMMIT_MESSAGE;
    }

}
