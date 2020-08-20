package com.blue.wechatdemo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private static RedisUtil instance = null;
    /**
     * redis过期秒数
     */
    private static final Integer REDIS_TIMEOUT_SECONDS = 60 * 60 * 5;

    /**
     * accessToken过期时间 2小时
     */
    private Long ACCESS_TOKEN_SECONDS = 60L * 60L * 2;

    @Autowired
    private StringRedisTemplate redisTemplate;


    private RedisUtil() {

    }

    public static RedisUtil getInstance() {
        if (null == instance) {
            synchronized (RedisUtil.class) {
                if (null == instance) {
                    instance = new RedisUtil();
                }
            }
        }
        return instance;
    }

    public void del(String... keys) {
        try {
            if (keys != null && keys.length > 0) {
                if (keys.length == 1) {
                    redisTemplate.delete(keys[0]);
                } else {
                    redisTemplate.delete(CollectionUtils.arrayToList(keys));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    public void deleteByKey(String key) {
        try {
            if (key != null) {
                redisTemplate.delete(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    public String getStr(String key) {
        String value = "";
        try {
            value = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return value;
    }

    public void add(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value, REDIS_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * key value 设置过期时间
     *
     * @param key
     * @param value
     * @param seconds
     */
    public void add(String key, String value, Long seconds) {
        try {
            redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }




    /**
     * 添加hash数据类型
     *
     * @param key
     * @param map
     */
    public void addHash(String key, Map map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 添加list数据类型
     *
     * @param key
     * @param list
     */
    public void addList(String key, List list) {
        try {
            redisTemplate.opsForList().rightPushAll(key, list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 添加 Set数据类型
     *
     * @param key
     * @param set
     */
    public void addSet(String key, Set set) {
        for (Object s : set) {
            this.addSetByOne(key, s);
        }
    }

    /**
     * 向 Set集合添加一个元素
     *
     * @param key
     * @param o
     */
    public void addSetByOne(String key, Object o) {
        try {
            redisTemplate.opsForSet().add(key, o.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 获取 当前key下的Set集合
     *
     * @param key
     * @return
     */
    public Set getSet(String key) {
        Set<String> members = new HashSet<>();
        try {
            SetOperations<String, String> setOperations = redisTemplate.opsForSet();
            members = setOperations.members(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return members;
    }

    /**
     * 添加 ZSet数据类型
     *
     * @param key
     * @param zSet
     */
    @Deprecated
    public void addZSet(String key, Set zSet) {
        try {
            for (Object o : zSet) {
                redisTemplate.opsForZSet().add(key, o.toString(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 向 Zset 集合中添加元素  score 排序
     *
     * @param key
     * @param o
     * @param score
     */
    public void addZSetByOne(String key, Object o, Double score) {
        try {
            redisTemplate.opsForZSet().add(key, o.toString(), score);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 获取 Zset 集合中所有的元素
     *
     * @param key
     * @return
     */
    public Set getZSet(String key) {
        Set<String> range = new HashSet<>();
        try {
            ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
            range = zSetOperations.rangeByScore(key, Double.MIN_VALUE, Double.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return range;
    }

    /**
     * 获取 List 集合中所有元素
     *
     * @param key
     * @return
     */
    public List getList(String key) {
        List<String> dataList = new ArrayList<>();
        try {
            ListOperations<String, String> listOperation = redisTemplate.opsForList();
            dataList = listOperation.range(key, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return dataList;
    }
}
