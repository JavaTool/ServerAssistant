package org.server.assistant.sequence;

/**
 * id管理器
 * @author	fuhuiyuan
 */
public interface IInstanceIdManager {

  /**
   * 创建生成规则
   * @param 	name
   * 			名称
   * @param 	baseValue
   * 			基础值
   */
  void create(String name, long baseValue);
  /**
   * 生成新的id
   * @param 	name
   * 			名称
   * @return	id
   */
  long next(String name);

}
