package com.dtmy.vertx.main;

import com.dtmy.vertx.verticle.PageVerticle;
import com.dtmy.vertx.verticle.WebSocketVerticle;
import io.vertx.core.Vertx;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * <ul>
 * <li>文件包名 : com.dtmy.vertx</li>
 * <li>创建时间 : 2017/11/24 16:40</li>
 * <li>修改记录 : 无</li>
 * </ul>
 * 类说明：
 *
 * @author zhonghao
 * @version 2.0.0
 */
public class Main {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(WebSocketVerticle.class.getName());
        vertx.deployVerticle(PageVerticle.class.getName());
    }
}
