package org.server.assistant.io.http.client.async;

import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSONObject;
import org.server.assistant.io.IOParam;
import org.server.assistant.io.http.client.IJsonAndBytes;
import org.server.assistant.secret.IEncrypt;

import static org.server.assistant.io.http.client.async.IResponseToJsonAndBytesHandler.toJsonAndBytes;

public interface IResponseToTrueJsonAndBytesHandler extends IResponseHandler<IJsonAndBytes>, IOParam {

  @Override
  default IJsonAndBytes from(IEncrypt encrypt, HttpResponse response) throws Exception {
    IJsonAndBytes s = toJsonAndBytes(encrypt, response);
    JSONObject json = s.getJson();
    if (json.getInteger(RET_CODE) == CODE_OK) {
      return s;
    } else {
      throw new Exception(json.getString(RET_MSG));
    }
  }

}
