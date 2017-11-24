package com.dtmy.vertx.verticle;

import io.vertx.core.AbstractVerticle;

/**
 * <ul>
 * <li>文件包名 : com.dtmy.vertx.verticle</li>
 * <li>创建时间 : 2017/11/24 17:01</li>
 * <li>修改记录 : 无</li>
 * </ul>
 * 类说明：
 *
 * @author zhonghao
 * @version 2.0.0
 */
public class WebSocketVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        vertx.createHttpServer().websocketHandler(ws -> ws.handler(ws::writeBinaryMessage)).requestHandler(req ->{
            if(req.uri().equals("/")){
                req.response().sendFile("ws.html");
            }
        }).listen(8080);
    }
}
