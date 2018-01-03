package com.sunveee.template.ssm.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sunveee.template.ssm.util.LogUtil;

/**
 * FastjsonRedisSerializer
 * 
 * TODO 未投入使用,还需要改进
 * 
 * @author 51
 * @version $Id: FastjsonRedisSerializer.java, v 0.1 2018年1月3日 下午8:02:41 51 Exp $
 */
public class FastjsonRedisSerializer implements RedisSerializer<Object> {

    private static final Logger logger           = LoggerFactory.getLogger(FastjsonRedisSerializer.class);

    private static final String CLASS_NAME_FIELD = "c";
    private static final String VALUE_FIELD      = "v";

    public FastjsonRedisSerializer() {
    }

    @Override
    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        String _className = t.getClass().getCanonicalName();
        String _str = JSON.toJSONString(t, SerializerFeature.UseSingleQuotes);
        JSONObject obj = new JSONObject();
        obj.put(CLASS_NAME_FIELD, _className);
        obj.put(VALUE_FIELD, _str);

        LogUtil.info(logger, "序列化以缓存的jsonObject={0}", obj);
        try {
            return JSON.toJSONBytes(obj);
        } catch (Exception ex) {
            throw new SerializationException("Could not parse JSON: " + ex.getMessage(), ex);
        }

    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        try {
            JSONObject jsonObj = (JSONObject) JSON.parse(bytes);
            String _className = jsonObj.getString(CLASS_NAME_FIELD);
            String _str = jsonObj.getString(VALUE_FIELD);

            LogUtil.info(logger, "反序列化读取的className={0},str={1}", _className, _str);

            Class<?> clazz = Class.forName(_className);
            Object obj = JSON.parse(_str);
            if (obj.getClass() == clazz) {
                return obj;
            } else {
                return null;
            }
        } catch (Exception ex) {
            throw new SerializationException("Could not parse JSON: " + ex.getMessage(), ex);
        }
    }

}
