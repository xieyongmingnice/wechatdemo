package com.blue.wechatdemo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
    private StringRedisTemplate stringRedisTemplate;


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
                    stringRedisTemplate.delete(keys[0]);
                } else {
                    stringRedisTemplate.delete(CollectionUtils.arrayToList(keys));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
        }
    }

    public void deleteByKey(String key) {
        try {
            if (key != null) {
                stringRedisTemplate.delete(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
        }
    }

    public String getStr(String key) {
        String value = "";
        try {
            value = stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
        }
        return value;
    }

    public void add(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value, REDIS_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
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
            stringRedisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
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
            stringRedisTemplate.opsForHash().putAll(key, map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
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
            stringRedisTemplate.opsForList().rightPushAll(key, list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
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
            stringRedisTemplate.opsForSet().add(key, o.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
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
            SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
            members = setOperations.members(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
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
                stringRedisTemplate.opsForZSet().add(key, o.toString(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
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
            stringRedisTemplate.opsForZSet().add(key, o.toString(), score);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
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
            ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
            range = zSetOperations.rangeByScore(key, Double.MIN_VALUE, Double.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
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
            ListOperations<String, String> listOperation = stringRedisTemplate.opsForList();
            dataList = listOperation.range(key, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
        }
        return dataList;
    }
}
