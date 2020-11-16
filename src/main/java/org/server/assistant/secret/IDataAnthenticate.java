package org.server.assistant.secret;

public interface IDataAnthenticate<I, O> {

  void write(O out) throws Exception;

  boolean read(I in);

  int getAnthenticateLength();

}
