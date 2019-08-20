package com.cn.jm.listener.order;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.jfinal.plugin.redis.Redis;

import redis.clients.jedis.Jedis;

public class OrderTimer {

    static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    static Jedis jedis = Redis.use().getJedis();

    public static void start() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
            	jedis.configSet("notify-keyspace-events", "Ex");
                jedis.psubscribe(new KeyExpiredListener(), "__keyevent@1__:expired");
            }
        };
        
        service.schedule(runnable, 1, TimeUnit.SECONDS);
        System.out.println("订单监听已经启动……");
    }

    public static void stop() {
        jedis.close();
        service.shutdown();
    }

}
