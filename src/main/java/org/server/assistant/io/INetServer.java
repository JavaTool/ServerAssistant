package org.server.assistant.io;

import org.server.assistant.shutdown.IShutdown;

/**
 * A server of net connect.
 * @author 	fuhuiyuan
 */
public interface INetServer extends IShutdown {

  /**
   * Binding an ip and port, start this server.
   * @throws 	Exception
   */
  void bind() throws Exception;
  /**
   * The port of this server.
   * @return	port of this server
   */
  int getPort();

}
