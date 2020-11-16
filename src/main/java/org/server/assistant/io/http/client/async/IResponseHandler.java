package org.server.assistant.io.http.client.async;

import org.apache.http.HttpResponse;
import org.server.assistant.secret.IEncrypt;

public interface IResponseHandler<T> {

  void handleResponse(T t);

  T from(IEncrypt encrypt, HttpResponse response) throws Exception;

}
