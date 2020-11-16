package org.server.assistant.io.http.client.async;

import static com.alibaba.fastjson.JSON.parseObject;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSONObject;
import org.server.assistant.secret.IEncrypt;

public interface IResponseToJsonHandler extends IResponseHandler<JSONObject> {

  @Override
  default JSONObject from(IEncrypt encrypt, HttpResponse response) throws Exception {
    return toJson(encrypt, response);
  }

  static JSONObject toJson(IEncrypt encrypt, HttpResponse response) throws Exception {
    return bytesToJson(IResponseToBytesHandler.toBytes(encrypt, response));
  }

  static JSONObject bytesToJson(byte[] bytes) throws UnsupportedEncodingException {
    return parseObject(new String(bytes, "utf-8"));
  }

}
