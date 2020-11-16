package org.server.assistant.cache.redis;

import redis.clients.jedis.Jedis;

public interface IJedisReources {

  interface RedisExecutor<T> {

    T exec(Jedis jedis) throws Exception;

  }

  interface VoidRedisExecutor {

    void exec(Jedis jedis) throws Exception;

  }

  <T> T exec(RedisExecutor<T> run, T param);

  void exec(VoidRedisExecutor run);

  void changeDB(int index);

  boolean ping();

}
