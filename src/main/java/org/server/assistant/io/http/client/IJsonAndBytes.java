package org.server.assistant.io.http.client;

import com.alibaba.fastjson.JSONObject;

public interface IJsonAndBytes {

  JSONObject getJson();

  byte[] getBytes();

}
