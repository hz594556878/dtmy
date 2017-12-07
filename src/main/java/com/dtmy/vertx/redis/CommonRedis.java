package com.dtmy.vertx.redis;

import com.dtmy.vertx.common.Consts;
import io.vertx.core.Vertx;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * <ul>
 * <li>文件包名 : com.dtmy.vertx.redis</li>
 * <li>创建时间 : 2017/12/7 14:40</li>
 * <li>修改记录 : 无</li>
 * </ul>
 * 类说明：
 *
 * @author zhonghao
 * @version 2.0.0
 */
public class CommonRedis {
    private RedisOptions redisOptions;
    private Vertx vertx;
    private RedisClient client;

    public CommonRedis(Vertx vertx){
        this.vertx = vertx;
        redisOptions = new RedisOptions();
        redisOptions.setHost(Consts.REDIS_HOSTS).setPort(Consts.REDIS_PORT);
        client = RedisClient.create(vertx, redisOptions);
    }

    public void set(String key, String value){
        client.set(key, value, res -> res.result());
    }

    public void get(String key){
        client.get(key, res -> {
            if(res.succeeded()){
                System.out.println(res.result());
            }
        });
    }
}
