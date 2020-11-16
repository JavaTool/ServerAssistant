package org.server.assistant.io.http.client.async;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSONObject;
import org.server.assistant.io.http.client.IJsonAndBytes;
import org.server.assistant.secret.IEncrypt;

import static org.server.assistant.io.http.client.async.IResponseToBytesHandler.toBytes;
import static org.server.assistant.io.http.client.async.IResponseToJsonHandler.bytesToJson;

public interface IResponseToJsonAndBytesHandler extends IResponseHandler<IJsonAndBytes> {

  @Override
  default IJsonAndBytes from(IEncrypt encrypt, HttpResponse response) throws Exception {
    return toJsonAndBytes(encrypt, response);
  }

  static IJsonAndBytes toJsonAndBytes(IEncrypt encrypt, HttpResponse response) throws Exception {
    ByteArrayInputStream bais = new ByteArrayInputStream(toBytes(encrypt, response));
    DataInputStream dis = new DataInputStream(bais);
    int size = dis.readInt();
    byte[] jsonBytes = new byte[size];
    dis.read(jsonBytes);
    JSONObject jsonObject = bytesToJson(jsonBytes);
    byte[] datas = new byte[dis.available()];
    dis.read(datas);
    return new JsonAndBytes(jsonObject, datas);
  }

}

class JsonAndBytes implements IJsonAndBytes {

  private final JSONObject jsonObject;

  private final byte[] bytes;

  public JsonAndBytes(JSONObject jsonObject, byte[] bytes) {
    this.jsonObject = jsonObject;
    this.bytes = bytes;
  }

  @Override
  public JSONObject getJson() {
    return jsonObject;
  }

  @Override
  public byte[] getBytes() {
    return bytes;
  }

}
