package org.server.assistant.secret;

public interface IEncrypt {

  byte[] encrypt(byte[] src);

  byte[] deEncrypt(byte[] src);

}
