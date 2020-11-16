package org.server.assistant.coder.stream;

import org.server.assistant.utils.SerializaUtil;

import java.io.Serializable;

class SerialableCoder implements IStreamCoder {

  @Override
  public byte[] write(Object value) throws Exception {
    return SerializaUtil.serializable((Serializable) value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T read(byte[] stream) throws Exception {
    return (T) SerializaUtil.deserializable(stream);
  }

}
