package org.server.assistant.coder.stream;

public interface IStreamable {

  byte[] toByteArray() throws Exception;

  void readFromByteArray(byte[] bytes) throws Exception;

}
