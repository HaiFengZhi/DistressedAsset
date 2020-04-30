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

import redis.clients.jedis.Jedis;

import java.util.Date;

/**
 * 缓存存储接口。
 *
 * <p>
 *     实际项目中，可能会有很多第三方的缓存产品，建立统一接口支持缓存的存储访问。
 * </p>
 *
 * @author Yelin.G at 2015/08/03
 */
public interface CacheStorage<T> {

    /**
     * 设置键值，有过期时间。
     *
     * @param key 键。
     * @param value 值。
     * @param expiredTime 过期时间。
     */
    void set(String key, T value, int expiredTime, Jedis jedis);

    /**
     * 设置键值，有过期时间。
     *
     * @param key 键。
     * @param value 值。
     */
    void set(String key, T value, Jedis jedis);

    /**
     * 设置键值，有过期时间。
     *
     * @param key 键。
     * @param value 值。
     * @param expiredTime 过期时间。
     */
    void setAndClose(String key, T value, int expiredTime);

    /**
     * 设置键值，有过期时间。
     *
     * @param key 键。
     * @param value 值。
     */
    void setAndClose(String key, T value);

    /**
     * 设置键值，有过期时间。
     *
     * @param key 键。
     * @param value 值。
     * @param expiredTime 过期时间。
     */
    void set(String key, T value, Date expiredTime, Jedis jedis);

    /**
     * 设置键值，有过期时间。
     *
     * @param key 键。
     * @param value 值。
     * @param expiredTime 过期时间。
     */
    void setAndClose(String key, T value, Date expiredTime);

    /**
     * 会话中获取键值。
     *
     * @param key 键。
     * @return  值。
     */
    public T get(String key, Jedis jedis);

    /**
     * 会话中获取键值。
     *
     * @param key 键。
     * @return  值。
     */
    public T getAndClose(String key);

    /**
     * 会话中获取键值。
     *
     * @param key 键。
     * @return  值。
     */
    public T get(byte[] key, Jedis jedis);

    /**
     * 会话中获取键值。
     *
     * @param key 键。
     * @return  值。
     */
    public T getAndClose(byte[] key);

    /**
     * 从缓存有效时间。
     *
     * @param key 键。
     * @return 缓存中数据。
     */
    public T ttl(String key, Jedis jedis);

    /**
     * 从缓存有效时间。
     *
     * @param key 键。
     * @return 缓存中数据。
     */
    public T ttlAndClose(String key);

    /**
     * 设置缓存有效时间。
     *
     * @param key 键。
     * @param seconds 设置有效时间，秒数
     * @return 缓存中数据。
     */
    public T expire(String key, int seconds, Jedis jedis);

    /**
     * 设置缓存有效时间。
     *
     * @param key 键。
     * @param seconds 设置有效时间，秒数
     * @return 缓存中数据。
     */
    public T expireAndClose(String key, int seconds);

    /**
     * 从缓存中移除。
     *
     * @param key 键。
     * @return {@link Long}。
     */
    public Long remove(String key, Jedis jedis);

    /**
     * 从缓存中移除。
     *
     * @param key 键。
     * @return {@link Long}。
     */
    public Long removeAndClose(String key);

    /**
     * 关闭连接。
     *
     */
    public void close(Jedis jedis);

}
