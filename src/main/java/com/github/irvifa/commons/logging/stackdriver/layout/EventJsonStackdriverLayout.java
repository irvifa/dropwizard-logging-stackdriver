package com.github.irvifa.commons.logging.stackdriver.layout;

// CHECKSTYLE.OFF: IllegalImport - necessary to use the logger implementation

import ch.qos.logback.classic.pattern.ThrowableHandlingConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.logging.json.layout.AbstractJsonLayout;
import io.dropwizard.logging.json.layout.JsonFormatter;
import io.dropwizard.logging.json.layout.MapBuilder;
import io.dropwizard.logging.json.layout.TimestampFormatter;
import java.util.Map;

// CHECKSTYLE.ON: IllegalImport

public class EventJsonStackdriverLayout extends AbstractJsonLayout<ILoggingEvent> {

  private final ThrowableHandlingConverter throwableProxyConverter;
  private final TimestampFormatter timestampFormatter;
  private final Map<String, Object> additionalFields = ImmutableMap.of();
  private final Map<String, String> customFieldNames = ImmutableMap.of();

  public EventJsonStackdriverLayout(
      JsonFormatter jsonFormatter,
      TimestampFormatter timestampFormatter,
      ThrowableHandlingConverter throwableProxyConverter) {
    super(jsonFormatter);
    this.throwableProxyConverter = throwableProxyConverter;
    this.timestampFormatter = timestampFormatter;
  }

  @Override
  public void start() {
    throwableProxyConverter.start();
    super.start();
  }

  @Override
  public void stop() {
    super.stop();
    throwableProxyConverter.stop();
  }

  private String getFormattedMessage(ILoggingEvent event, String severity) {
    StringBuilder messageBuilder = new StringBuilder();

    messageBuilder.append(String.format("%-5s", severity));
    messageBuilder.append(" [");
    messageBuilder.append(timestampFormatter.format(event.getTimeStamp()));
    messageBuilder.append("] ");
    messageBuilder.append(event.getThreadName());
    messageBuilder.append(" - ");
    messageBuilder.append(event.getLoggerName());
    messageBuilder.append(": ");
    messageBuilder.append(event.getFormattedMessage());

    if (event.getThrowableProxy() != null) {
      messageBuilder.append("\n");
      messageBuilder.append(throwableProxyConverter.convert(event));
    }

    return messageBuilder.toString();
  }

  @Override
  protected Map<String, Object> toJsonMap(ILoggingEvent event) {
    String severity = String.valueOf(event.getLevel());
    String message = getFormattedMessage(event, severity);
    final MapBuilder mapBuilder =
        new MapBuilder(timestampFormatter, customFieldNames, additionalFields, 16)
            .add("severity", true, severity)
            .add("message", true, message);

    return mapBuilder.build();
  }
}

