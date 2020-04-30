/*************************************************************************
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *
 *                COPYRIGHT (C) HONGLING CAPITAL CORPORATION 2012
 *    ALL RIGHTS RESERVED BY HONGLING CAPITAL CORPORATION. THIS PROGRAM
 * MUST BE USED  SOLELY FOR THE PURPOSE FOR WHICH IT WAS FURNISHED BY
 * HONGLING CAPITAL CORPORATION. NO PART OF THIS PROGRAM MAY BE REPRODUCED
 * OR DISCLOSED TO OTHERS,IN ANY FORM, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF HONGLING CAPITAL CORPORATION. USE OF COPYRIGHT NOTICE
 * DOES NOT EVIDENCE PUBLICATION OF THE PROGRAM.
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *************************************************************************/

package com.distressed.asset.common.cache;

import com.distressed.asset.common.exception.CacheException;
import com.distressed.asset.common.utils.CommonUtils;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 缓存中存储会话。
 *
 * @author Yelin.G at 2015/08/03
 */
public class CacheSessionStorage extends CacheStorageAdapter {

    protected static final Logger LOG = LogManager.getLogger(CacheSessionStorage.class);

    protected JedisClient jedisClient;

    public  static final String USER_LIMT_LOCK_KEY="USER_LIMT_LOCK_";

    public CacheSessionStorage() {
        super();
    }

    public CacheSessionStorage(JedisClient jedisClient) {
        super();
        this.jedisClient = jedisClient;
    }

    /**
     * 保存数据在缓存中。
     *
     * @param key 键。
     * @param value 值。
     * @param expiredTime 过期时间。
     */
    @Override
    public void set(String key, Object value, int expiredTime, Jedis jedis) {
        if(value instanceof String) {
            jedis.set(key, (String) value);
            jedis.expire(key, expiredTime);
        } else {
            try {
                jedis.set(key.getBytes("utf8"), CommonUtils.objectToByte(value));
                jedis.expire(key, expiredTime);
            } catch (Exception ex) {
                LOG.error("设置kv-store值失败：{}={}", key, value);
                throw new CacheException(ex);
            }
        }
    }

    /**
     * 保存数据在缓存中。
     *
     * @param key 键。
     * @param value 值。
     */
    @Override
    public void set(String key, Object value, Jedis jedis) {
        if(value instanceof String) {
            jedis.set(key, (String) value);
        } else {
            try {
                jedis.set(key.getBytes("utf8"), CommonUtils.objectToByte(value));
            } catch (Exception ex) {
                LOG.error("设置kv-store值失败：{}={}", key, value);
                throw new CacheException(ex);
            }
        }
    }

    /**
     * 保存数据在缓存中。
     *
     * @param key 键。
     * @param value 值。
     * @param expiredTime 过期时间。
     */
    @Override
    public void setAndClose(String key, Object value, int expiredTime) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            set(key, value, expiredTime, jedis);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 保存数据在缓存中。
     *
     * @param key 键。
     * @param value 值。
     */
    @Override
    public void setAndClose(String key, Object value) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            set(key, value, jedis);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 保存数据在缓存中。
     *
     * @param key 键。
     * @param value 值。
     * @param expiredTime 过期时间。
     */
    @Override
    public void set(String key, Object value, Date expiredTime, Jedis jedis) {
        Long expire = expiredTime.getTime() - System.currentTimeMillis();
        if(expire > Integer.MAX_VALUE * 1000){
            set(key, value, Integer.MAX_VALUE, jedis);
        }
        set(key, value, expire.intValue()/1000, jedis);
    }

    /**
     * 保存数据在缓存中。
     *
     * @param key 键。
     * @param value 值。
     * @param expiredTime 过期时间。
     */
    @Override
    public void setAndClose(String key, Object value, Date expiredTime) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            set(key, value, expiredTime, jedis);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 从缓存中获取数据。
     *
     * @param key 键。
     * @return 缓存中数据。
     */
    @Override
    public String get(String key, Jedis jedis) {
        return jedis.get(key);
    }

    /**
     * 从缓存中获取数据。
     *
     * @param key 键。
     * @return 缓存中数据。
     */
    @Override
    public String getAndClose(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            return jedis.get(key);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 从缓存有效时间。
     *
     * @param key 键。
     * @return 缓存中数据。
     */
    @Override
    public Long ttl(String key, Jedis jedis) {
        return jedis.ttl(key);
    }

    /**
     * 从缓存有效时间。
     *
     * @param key 键。
     * @return 缓存中数据。
     */
    @Override
    public Long ttlAndClose(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            return jedis.ttl(key);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 设置缓存有效时间。
     *
     * @param key 键。
     * @return 缓存中数据。
     */
    @Override
    public Long expire(String key, int seconds, Jedis jedis) {
        return jedis.expire(key, seconds);
    }

    /**
     * 设置缓存有效时间。
     *
     * @param key 键。
     * @return 缓存中数据。
     */
    @Override
    public Long expireAndClose(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            return jedis.expire(key, seconds);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 从缓存中获取数据。
     *
     * @param key 键。
     * @return 缓存中数据。
     */
    @Override
    public Object get(byte[] key, Jedis jedis) {
        try {
            return CommonUtils.byteToObject(jedis.get(key));
        } catch (Exception ex) {
            LOG.error("获取kv-store值失败：key={}", new String(key, Charsets.UTF_8));
            throw new CacheException(ex);
        }
    }

    /**
     * 从缓存中获取数据。
     *
     * @param key 键。
     * @return 缓存中数据。
     */
    @Override
    public Object getAndClose(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            return get(key, jedis);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将缓存中的数据增加1。
     *
     * @param key 键。
     * @param jedis {@link Jedis}
     * @return 计算后缓存的值。
     */
    public Long increase(String key, Jedis jedis) {
        return jedis.incr(key);
    }

    /**
     * 将缓存中的数据增加1。
     *
     * @param key 键。
     * @return 计算后缓存的值。
     */
    public Long increaseAndClose(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            return jedis.incr(key);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将缓存中的数据减去1。
     *
     * @param key 键。
     * @param jedis {@link Jedis}
     * @return 计算后缓存的值。
     */
    public Long decrease(String key, Jedis jedis) {
        return jedis.decr(key);
    }

    /**
     * 将缓存中的数据减去1。
     *
     * @param key 键。
     * @return 计算后缓存的值。
     */
    public Long decreaseAndClose(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            return jedis.decr(key);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将缓存中的数据增加指定值。
     *
     * @param key 键。
     * @param num 增加的数值。
     * @param jedis {@link Jedis}
     * @return 计算后缓存的值。
     */
    public Long increaseBy(String key, long num, Jedis jedis) {
        return jedis.incrBy(key, num);
    }

    /**
     * 将缓存中的数据增加指定值。
     *
     * @param key 键。
     * @param num 增加的数值。
     * @return 计算后缓存的值。
     */
    public Long increaseByAndClose(String key, long num) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            return jedis.incrBy(key, num);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将缓存中的数据减去指定值。
     *
     * @param key 键。
     * @param num 增加的数值。
     * @param jedis {@link Jedis}
     * @return 计算后缓存的值。
     */
    public Long decreaseBy(String key, long num, Jedis jedis) {
        return jedis.decrBy(key, num);
    }

    /**
     * 将缓存中的数据减去指定值。
     *
     * @param key 键。
     * @param num 增加的数值。
     * @return 计算后缓存的值。
     */
    public Long decreaseByAndClose(String key, long num) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            return jedis.decrBy(key, num);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 从缓存中移除数据。
     *
     * @param key 键。
     * @return {@link Long}。
     */
    @Override
    public Long remove(String key, Jedis jedis) {
        return jedis.del(key);
    }

    /**
     * 从缓存中移除数据。
     *
     * @param key 键。
     * @return {@link Long}。
     */
    @Override
    public Long removeAndClose(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            return jedis.del(key);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /************************************SET集合操作start************************************************/
    /**
     * 将字符串数据放入SET集合中。
     *
     * @param key       键。
     * @param values    数据集合。
     * @return {@link Long}。
     */
    @Override
    public Long addToSet(String key, String... values) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            return jedis.sadd(key, values);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将字符串数据放入SET集合中。
     *
     * @param key       键。
     * @param expiredTime 过期时间。
     * @param values    数据集合。
     * @return {@link Long}。
     */
    @Override
    public Long addToSet(String key, int expiredTime, String... values) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            Long sadd = jedis.sadd(key, values);
            jedis.expire(key, expiredTime);
            return sadd;
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将指定字符串数据移除SET集合中。
     *
     * @param key       键。
     * @param values    数据集合。
     * @return {@link Long}。
     */
    @Override
    public Long removeFromSet(String key, String... values) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            return jedis.srem(key, values);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 检测指定字符串数据是否在SET集合中。
     *
     * @param key       键。
     * @param value    数据集合。
     * @return {@link Boolean}。
     */
    @Override
    public Boolean isMemberInSet(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            return jedis.sismember(key, value);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 检测指定字符串数据是否在SET集合中。
     *
     * @param key       键。
     * @return {@link Set}。
     */
    @Override
    public Set<String> getAllMembersInSet(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            return jedis.smembers(key);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 查询指定Key的SET集合数量。
     *
     * @param key       键。
     * @return {@link Long}。
     */
    @Override
    public Long getMemberCountInSet(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            return jedis.scard(key);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }


    /************************************SET集合操作end************************************************/

    /**
     * 关闭连接。
     *
     */
    @Override
    public void close(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }

    /**
     * jedis获取锁。
     *
     * @param key  键。
     * @return
     */
    public boolean lock(String key,String value){
        Jedis jedis = null;
        try {
            jedis = jedisClient.getJedis();
            if(jedis.setnx(key,value) == 1){
                jedis.expire(key,60*5); //默认5分钟
                return true;
            }else {
                return false;
            }
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * jedis获取锁。
     *
     * @param key  键。
     * @return
     */
    public boolean lock(String key){
        return this.lock(key,"1");
    }

    /**
     * 从jedis释放锁
     *
     * @param key  键。
     * @return
     */
    public Long unlock(String key){
       return removeAndClose(key);
    }


    /**
     * 设置{@link com.distressed.asset.common.cache.JedisClient}。
     *
     * @param jedisClient {@link com.distressed.asset.common.cache.JedisClient}。
     */
    public void setJedisClient(JedisClient jedisClient) {
        this.jedisClient = jedisClient;
    }

    /**
     * 获取{@link Jedis}。
     *
     */
    public Jedis getJedis() {
        return jedisClient.getJedis();
    }

    /**
     * 获取缓存池的连接信息。
     * @return  {@link Map}。
     */
    public Map<String, Object> getJedisPoolInfo () {
        return jedisClient.getJedisPoolInfo();
    }
}
