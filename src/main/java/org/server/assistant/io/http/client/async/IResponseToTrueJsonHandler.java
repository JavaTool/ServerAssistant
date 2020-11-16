package org.server.assistant.io.http.client.async;


import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSONObject;
import org.server.assistant.io.IOParam;
import org.server.assistant.secret.IEncrypt;

import static org.server.assistant.io.http.client.async.IResponseToJsonHandler.toJson;

public interface IResponseToTrueJsonHandler extends IResponseHandler<JSONObject>, IOParam {

  @Override
  default JSONObject from(IEncrypt encrypt, HttpResponse response) throws Exception {
    JSONObject json = toJson(encrypt, response);
    if (json.getInteger(RET_CODE) == CODE_OK) {
      return json;
    } else {
      throw new Exception(json.getString(RET_MSG));
    }
  }

}
