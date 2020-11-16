package org.server.assistant.io.message;

import org.server.assistant.io.dispatch.ISender;

/**
 * 消息接收器
 * @author 	fuhuiyuan
 */
public interface IMessageHandler {

  /**
   * 接收消息
   * @throws 	Exception
   */
  void handle(byte[] datas, ISender sender) throws Exception;

}
