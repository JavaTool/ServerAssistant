package org.server.assistant.io.proto;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.function.Consumer;

import org.server.assistant.io.dispatch.ISender;
import org.server.assistant.io.message.*;
import org.server.assistant.thread.*;
import org.server.assistant.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.trove.impl.unmodifiable.TUnmodifiableIntObjectMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public abstract class AbstractMessageHandler extends MessageHandler {

  private static final Logger log = LoggerFactory.getLogger(AbstractMessageHandler.class);

  private static final String METHOD_HEAD = "PROCESS";

  private static final String REQUEST_HEAD = "MI_CS";

  private static final String LOG_RECEIVED = "Receive [SessionId : {}] [Ip : {}] message[{}] : {}.";

  private static final IThreadType DEFAULT_THREAD_ID = () -> "DEFAULT_THREAD";

  private final IMessageIdTransform messageIdTransform;

  private Consumer<IMessagePackage> messageProcessor;

  private TIntObjectMap<ProcessorMethod> methods;

  private TIntObjectMap<IThreadType> threadTypes;

  private IErrorHandler errorHandler;

  public AbstractMessageHandler(IMessageIdTransform messageIdTransform) {
    this.messageIdTransform = messageIdTransform;
  }

  public void load(Collection<Object> objects) throws Exception {
    loadMethods(objects);
  }

  private void loadMethods(Collection<Object> objects) throws Exception {
    TIntObjectMap<ProcessorMethod> methods = new TIntObjectHashMap<>();
    for (Object bean : objects) {
      for (Method method : bean.getClass().getMethods()) {
        String key = StringUtil.uppercaseTo_(method.getName()).replace(METHOD_HEAD, REQUEST_HEAD);
        int messageId = messageIdTransform.transform(key);
        methods.put(messageId, new ProcessorMethod(bean, method));
      }
    }
    this.methods = new TUnmodifiableIntObjectMap<>(methods);

    IMessageProcessorFactory<IMessagePackage> factory = createMessageProcessorFactory(this::handleMessagePackage);
    messageProcessor = new MessageProcessorGroup(getThreadTypes(), factory);
  }

  abstract IMessageProcessorFactory<IMessagePackage> createMessageProcessorFactory(IMessagePackageHandler handler);

  private void handleMessagePackage(IMessagePackage messagePackage) {
    int messageId = messagePackage.getMessageId();
    int serial = messagePackage.getSerial();
    byte[] data = messagePackage.getData();
    IMessageSender messageSender = messagePackage.getMessageSender();
    if (methods.containsKey(messageId)) {
      methods.get(messageId).invoke(messageId, serial, data, messageSender);
    } else {
      log.error("Do not have processor handle message {}.", messageId);
    }
  }

  private class ProcessorMethod {

    private final Object processor;

    private final Method method;

    private final Method fromMethod;

    ProcessorMethod(Object processor, Method method) throws Exception {
      this.processor = processor;
      this.method = method;
      Class<?>[] types = method.getParameterTypes();
      String firstType = types[0].getName();
      fromMethod = firstType.equals("int") ? null : Class.forName(firstType.replace("interfaces.I", "proto.")).getMethod("from", byte[].class);
    }

    void invoke(int messageId, int serial, byte[] data, IMessageSender sender) {
      try {
        IMessage message = fromMethod == null ? null : createMessage(serial, data);
        String content = message == null ? String.format(IMessageSender.LOG_SERIAL, serial) : message.toString();
        log.info(LOG_RECEIVED, sender.getSessionId(), sender.getIp(), messageIdTransform.transform(messageId), content);
        method.invoke(processor, message == null ? serial : message, sender);
      } catch (Exception e) {
        log.error("", e);
        String error = e.getCause() == null ? null : e.getCause().getMessage();
        error = error == null || error.length() == 0 ? "Unknow exception." : error;
        sender.send(errorHandler.createErrorResponse(messageId, serial, error));
      }
    }

    private IMessage createMessage(int serial, byte[] data) throws Exception {
      return ((IMessage) fromMethod.invoke(null, data)).setSerial(serial);
    }

  }

  @Override
  protected void handle(int messageId, int serial, byte[] data, IMessageSender messageSender) {
    messageProcessor.accept(new IMessagePackage() {

      @Override
      public int getSerial() {
        return serial;
      }

      @Override
      public IMessageSender getMessageSender() {
        return messageSender;
      }

      @Override
      public int getMessageId() {
        return messageId;
      }

      @Override
      public byte[] getData() {
        return data;
      }

    });
  }

  /**
   * 设置错误处理器
   * @param 	errorHandler
   * 			错误处理器
   */
  public void setErrorHandler(IErrorHandler errorHandler) {
    this.errorHandler = errorHandler;
  }

  void setThreadTypes(TIntObjectMap<IThreadType> threadTypes) {
    this.threadTypes = new TUnmodifiableIntObjectMap<>(threadTypes);
  }

  private TIntObjectMap<IThreadType> getThreadTypes() {
    if (threadTypes == null) {
      TIntObjectMap<IThreadType> threadTypes = new TIntObjectHashMap<>();
      methods.forEachKey(messageId -> {
        threadTypes.put(messageId, DEFAULT_THREAD_ID);
        return true;
      });
      setThreadTypes(threadTypes);
    }
    return threadTypes;
  }

  @Override
  protected IMessageSender createMessageSender(ISender sender) {
    return new MessageSender(sender, errorHandler, messageIdTransform);
  }

}
