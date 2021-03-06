package org.server.assistant.io.http.client;

import com.alibaba.fastjson.JSONObject;
import org.server.assistant.io.IOParam;

class HttpTrueJsonConnector extends DecorateHttpConnector<JSONObject, JSONObject> implements IOParam {

  public HttpTrueJsonConnector(IHttpConnector<JSONObject> connector) {
    super(connector);
  }

  @Override
  protected JSONObject from(JSONObject s) throws Exception {
    if (s.getInteger(RET_CODE) == CODE_OK) {
      return s;
    } else {
      throw new Exception(s.getString(RET_MSG));
    }
  }

}
