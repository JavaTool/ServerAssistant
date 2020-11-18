package org.server.assistant.io.proto;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import org.server.assistant.io.message.IMessageIdTransform;
import org.server.assistant.thread.AsynchronousMessageProcessorFactory;
import org.server.assistant.thread.IMessagePackage;
import org.server.assistant.thread.IMessagePackageHandler;
import org.server.assistant.thread.IMessageProcessorFactory;

public final class AsynchronousMessageHandler extends AbstractMessageHandler {

  private static final int LIMIT = 1000;

  private static final int EACH_COUNT = 10;

  private Function<String, List<Runnable>> listSupplier;

  public AsynchronousMessageHandler(IMessageIdTransform messageIdTransform) {
    super(messageIdTransform);
  }

  @Override
  protected IMessageProcessorFactory<IMessagePackage> createMessageProcessorFactory(IMessagePackageHandler handler) {
    return new AsynchronousMessageProcessorFactory(LIMIT, EACH_COUNT, handler, listSupplier == null ? name -> ImmutableList.of() : listSupplier);
  }

  public void setListSupplier(Function<String, List<Runnable>> listSupplier) {
    this.listSupplier = listSupplier;
  }

}
