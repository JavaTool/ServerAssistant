package org.server.assistant.coder.stream;

import org.server.assistant.pubsub.impl.ByteArrayMessage;
import org.server.assistant.pubsub.impl.IByteArrayMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

class ByteArrayMessageCoder implements IStreamCoder {

  @Override
  public byte[] write(Object value) throws Exception {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(bout);
    dos.writeUTF(((IByteArrayMessage) value).getIp());
    dos.writeInt(((IByteArrayMessage) value).getMessageId());
    byte[] array = ((IByteArrayMessage) value).getBytes();
    dos.writeInt(array.length);
    dos.write(array);
    return bout.toByteArray();
  }

  @SuppressWarnings("unchecked")
  @Override
  public IByteArrayMessage read(byte[] stream) throws Exception {
    ByteArrayInputStream bais = new ByteArrayInputStream(stream);
    DataInputStream dis = new DataInputStream(bais);
    String ip = dis.readUTF();
    int messageId = dis.readInt();
    byte[] datas = new byte[dis.readInt()];
    dis.read(datas);
    return new ByteArrayMessage(datas, messageId, ip);
  }

}
