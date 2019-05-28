package com.yunlsp.saas.service.api.portal.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * JedisUtil
 * <p>
 * Redis client base on jedis 根据继承类的不同,
 * jedis实例方式不用:JedisSimpleFactry/JedisPoolFactry/ShardedJedisPoolFactry
 * # for redis (sharded.jedis.address=host01:port,host02:port)
 * sharded.jedis.address=127.0.0.1:6379,127.0.0.1:6379,127.0.0.1:6379
 *
 * @author rodxu
 * @date 2018/12/14
 */
public class JedisUtil {

    private static Logger logger = LoggerFactory.getLogger(JedisUtil.class);

    /**
     * redis address, like "{ip}"、"{ip}:{port}"、"{redis/rediss}://path:{password}@{ip}:{port:6379}/{db}"；Multiple "," separated
     */
    private static String address;

    public static void init(String address) {
        JedisUtil.address = address;
        getInstance();
    }

    // ------------------------ ShardedJedisPool ------------------------
    /**
     * 方式01: Redis单节点 + Jedis单例 : Redis单节点压力过重, Jedis单例存在并发瓶颈 》》不可用于线上
     * new Jedis("127.0.0.1", 6379).get("cache_key");
     * 方式02: Redis单节点 + JedisPool单节点连接池 》》 Redis单节点压力过重，负载和容灾比较差
     * new JedisPool(new JedisPoolConfig(), "127.0.0.1", 6379, 10000).getResource().get("cache_key");
     * 方式03: Redis集群(通过client端集群,一致性哈希方式实现) + Jedis多节点连接池 》》Redis集群,负载和容灾较好, ShardedJedisPool一致性哈希分片,读写均匀，动态扩充
     * new ShardedJedisPool(new JedisPoolConfig(), new LinkedList<JedisShardInfo>());
     */
    private static ShardedJedisPool shardedJedisPool;

    private static ReentrantLock INSTANCE_INIT_LOCK = new ReentrantLock(false);

    private static JedisPoolConfig getConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        // 最大连接数, 默认8个
        config.setMaxTotal(200);
        // 最大空闲连接数, 默认8个
        config.setMaxIdle(50);
        // 设置最小空闲数
        config.setMinIdle(8);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        config.setMaxWaitMillis(10000);
        // 在获取连接的时候检查有效性, 默认false
        config.setTestOnBorrow(true);
        // 调用returnObject方法时，是否进行有效检查
        config.setTestOnReturn(true);
        // Idle时进行连接扫描
        config.setTestWhileIdle(true);
        //表示idle object evitor两次扫描之间要sleep的毫秒数
        config.setTimeBetweenEvictionRunsMillis(30000);
        //表示idle object evitor每次扫描的最多的对象数
        config.setNumTestsPerEvictionRun(10);
        //表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
        config.setMinEvictableIdleTimeMillis(60000);
        return config;
    }

    /**
     * 获取ShardedJedis实例
     *
     * @return
     */
    private static ShardedJedis getInstance() {
        if (shardedJedisPool == null) {
            try {
                if (INSTANCE_INIT_LOCK.tryLock(2, TimeUnit.SECONDS)) {
                    try {
                        if (shardedJedisPool == null) {
                            // JedisPoolConfig
                            JedisPoolConfig config = getConfig();
                            // JedisShardInfo List
                            List<JedisShardInfo> jedisShardInfos = new LinkedList<>();

                            String[] addressArr = address.split(",");
                            for (int i = 0; i < addressArr.length; i++) {
                                JedisShardInfo jedisShardInfo = new JedisShardInfo(addressArr[i]);
                                jedisShardInfos.add(jedisShardInfo);
                            }
                            shardedJedisPool = new ShardedJedisPool(config, jedisShardInfos);
                            logger.info(">>>>>>>>>>> sso, JedisUtil.ShardedJedisPool init success.");
                        }
                    } finally {
                        INSTANCE_INIT_LOCK.unlock();
                    }
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
        if (shardedJedisPool == null) {
            throw new NullPointerException(">>>>>>>>>>> sso, JedisUtil.ShardedJedisPool is null.");
        }
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        return shardedJedis;
    }

    // ------------------------ serialize and unserialize ------------------------

    /**
     * 将对象-->byte[] (由于jedis中不支持直接存储object所以转换成byte[]存入)
     *
     * @param object
     * @return
     */
    private static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            logger.error("{}", e);
        } finally {
            try {
                oos.close();
                baos.close();
            } catch (IOException e) {
                logger.error("{}", e);
            }
        }
        return null;
    }

    /**
     * 将byte[] -->Object
     *
     * @param bytes
     * @return
     */
    private static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            logger.error("{}", e);
        } finally {
            try {
                bais.close();
            } catch (IOException e) {
                logger.error("{}", e);
            }
        }
        return null;
    }

    // ------------------------ jedis util ------------------------
    /**
     * 存储简单的字符串或者是Object 因为jedis没有分装直接存储Object的方法，所以在存储对象需斟酌下
     * 存储对象的字段是不是非常多而且是不是每个字段都用到，如果是的话那建议直接存储对象，
     * 否则建议用集合的方式存储，因为redis可以针对集合进行日常的操作很方便而且还可以节省空间
     */

    /**
     * Set String
     *
     * @param key
     * @param value
     * @param seconds 存活时间,单位/秒
     * @return
     */
    public static String setStringValue(String key, String value, int seconds) {
        String result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.setex(key, seconds, value);
        } catch (Exception e) {
            logger.error("{}", e);
        } finally {
            client.close();
        }
        return result;
    }

    public static String setStringValue(String key, String value) {
        String result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.set(key, value);
        } catch (Exception e) {
            logger.error("{}", e);
        } finally {
            client.close();
        }
        return result;
    }

    /**
     * Set Object
     *
     * @param key
     * @param obj
     * @param seconds 存活时间,单位/秒
     */
    public static String setObjectValue(String key, Object obj, int seconds) {
        String result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.setex(key.getBytes(), seconds, serialize(obj));
        } catch (Exception e) {
            logger.error("{}", e);
        } finally {
            client.close();
        }
        return result;
    }

    public static String setObject(String key, Object obj) {
        String result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.set(key.getBytes(), serialize(obj));
        } catch (Exception e) {
            logger.error("{}", e);
        } finally {
            client.close();
        }
        return result;
    }

    public static String getBlockListValue(String key, int timeout) {
        ShardedJedis client = getInstance();
        String value = null;
        try {
            List<String> res = client.brpop(timeout, key);
            if (res != null && !res.isEmpty()) {
                value = res.get(1);
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            client.close();
        }
        return value;
    }

    public static long setListValue(String key, String... value) {
        ShardedJedis client = getInstance();
        long index = 0L;
        try {
            index = client.lpush(key, value);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            client.close();
        }
        return index;
    }

    public static long getListLength(String key) {
        ShardedJedis client = getInstance();
        long length = 0L;
        try {
            length = client.llen(key);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            client.close();
        }
        return length;
    }

    /**
     * Get String
     *
     * @param key
     * @return
     */
    public static String getStringValue(String key) {
        String value = null;
        ShardedJedis client = getInstance();
        try {
            value = client.get(key);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            client.close();
        }
        return value;
    }

    /**
     * Delete
     *
     * @param key
     * @return Integer reply, specifically:
     * an integer greater than 0 if one or more keys were removed
     * 0 if none of the specified key existed
     */
    public static Long del(String key) {
        Long result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.del(key);
            logger.info("删除redis结果：" + result + ",key" + key);
        } catch (Exception e) {
            logger.error("{}", e);
        } finally {
            client.close();
        }
        return result;
    }

    /**
     * incrBy	value值加i
     *
     * @param key
     * @param i
     * @return new value after incr
     */
    public static Long incrBy(String key, int i) {
        Long result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.incrBy(key, i);
        } catch (Exception e) {
            logger.error("{}", e);
        } finally {
            client.close();
        }
        return result;
    }

    /**
     * exists
     *
     * @param key
     * @return Boolean reply, true if the key exists, otherwise false
     */
    public static Boolean exists(String key) {
        Boolean result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.exists(key);
        } catch (Exception e) {
            logger.error("{}", e);
        } finally {
            client.close();
        }
        return result;
    }

    /**
     * expireSurplus
     *
     * @param key
     * @return 剩余的存活时间 单位/秒
     */
    public static Long expireSurplus(String key) {
        Long result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.ttl(key);
        } catch (Exception e) {
            logger.error("{}", e);
        } finally {
            client.close();
        }
        return result;
    }

    /**
     * expire	重置存活时间
     *
     * @param key
     * @param seconds 存活时间,单位/秒
     * @return Integer reply, specifically:
     * 1: the timeout was set.
     * 0: the timeout was not set since the key already has an associated timeout (versions lt 2.1.3), or the key does not exist.
     */
    public static Long expire(String key, int seconds) {
        Long result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.expire(key, seconds);
        } catch (Exception e) {
            logger.error("{}", e);
        } finally {
            client.close();
        }
        return result;
    }

    /**
     * expireAt		设置存活截止时间
     *
     * @param key
     * @param unixTime 存活截止时间戳
     * @return
     */
    public static Long expireAt(String key, long unixTime) {
        Long result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.expireAt(key, unixTime);
        } catch (Exception e) {
            logger.error("{}", e);
        } finally {
            client.close();
        }
        return result;
    }

    public static void main(String[] args) {
        //init("redis://:RedisAdmin@192.168.2.75:6379/3");
        //init("218.4.219.84:61183", "tc,,,123redis");
        //=====================阻塞队列测试开始========================
        /*for (int i = 0; i < 5; i++) {
            long index = setListValue("index01", "345" + i);
            System.out.println(index);
        }
        long length = getListLength("index01");
        System.out.println(length);
        String val = getBlockListValue("index01", 10);
        System.out.println(val);*/
        //=====================阻塞队列测试结束========================
        /*String key = "sso_sessionid#TCUSR233232434254*";
        Set<String> keys = getKeys(key);
        System.out.println("................" + keys.size() + ":" + keys);
        String kkk = String.join(" ", keys);
        System.out.println(kkk);*/
        //delAllMatch(key);

        /*UserModel user = UserModel.newBuilder("99", "").userName("许路路").build();
        String key = "key_xululu";
        user = (UserModel) getObjectValue(key);
        System.out.println(user);*/
    }

}
