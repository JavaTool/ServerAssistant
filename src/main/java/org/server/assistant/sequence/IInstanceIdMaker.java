package org.server.assistant.sequence;

/**
 * id生成器
 * @author	fuhuiyuan
 */
public interface IInstanceIdMaker {

  /**
   * 生成新的id
   * @return	id
   */
  long nextInstanceId();

  long currentInstanceId();

}
