package com.phhc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JackSon Json工具类
 *
 * @author xieym
 * @date 2020/6/12
 * @since 1.0.0
 **/
public class JacksonUtil {

    private ObjectMapper objectMapper;

    private JacksonUtil() {
        objectMapper = new ObjectMapper();
    }

    public static JacksonUtil getInstance() {
        return JacksonHolder.INSTANCE.getInstance();
    }

    private enum JacksonHolder {
        /**
         * 对象实例
         */
        INSTANCE;

        private JacksonUtil instance = null;

        JacksonHolder() {
            instance = new JacksonUtil();
        }

        private JacksonUtil getInstance() {
            return instance;
        }
    }

    /**
     * 将json字符串转为map
     *
     * @param jsonStr json字符串
     * @return Map
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> parseJsonToMap(String jsonStr) {
        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(jsonStr, Map.class);
        } catch (IOException e) {
            // 处理异常
        }
        if (null == map) {
            map = new HashMap<>();
        }
        return map;
    }

    /**
     * 将对象转换为json字符串
     *
     * @param object 对象实体
     * @return json字符串
     * @throws Exception
     */
    public String parseToJson(Object object) {
        String result = "";
        try {
            result = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            // 处理异常
        }
        return result;
    }

    /**
     * json转换成对象
     *
     * @param obj     目标对象实体
     * @param jsonStr json字符串
     * @return Object
     */
    public Object json2Obj(Object obj, String jsonStr) {
        Object result = null;
        try {
            result = objectMapper.readValue(jsonStr, obj.getClass());
        } catch (IOException e) {
            // 处理异常
        }
        return result;
    }


    /**
     * json转换成对象(支持列表)
     *
     * @param jsonStr           json字符串
     * @param jsonTypeReference 数据类型
     * @return Object
     */
    public <T> T json2Obj(String jsonStr, TypeReference<T> jsonTypeReference) {
        if (StringUtils.isBlank(jsonStr)){
            return null;
        }
        try {
            return objectMapper.readValue(jsonStr, jsonTypeReference);
        } catch (IOException e) {
            // 处理异常
        }
        return null;
    }

}
