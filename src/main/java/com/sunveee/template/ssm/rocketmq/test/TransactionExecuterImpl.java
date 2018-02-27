package com.sunveee.template.ssm.rocketmq.test;

import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunveee.template.ssm.util.LogUtil;

public class TransactionExecuterImpl implements LocalTransactionExecuter {
    private static final Logger logger = LoggerFactory.getLogger(TransactionExecuterImpl.class);

    private int                 n      = 1;

    @Override
    public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
        LogUtil.info(logger, "查询本地事务状态第{0}次", n);
        if (n < 5) { // 前4次使其返回UNKNOW
            n++;
            LogUtil.info(logger, "本地事务状态：UNKNOW");
            return LocalTransactionState.UNKNOW;
        }
        LogUtil.info(logger, "本地事务状态：COMMIT_MESSAGE");
        return LocalTransactionState.COMMIT_MESSAGE;
    }

}
