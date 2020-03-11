package com.github.irvifa.commons.logging.stackdriver;

// CHECKSTYLE.OFF: IllegalImport - necessary to use the logger implementation

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.pattern.RootCauseFirstThrowableProxyConverter;
import ch.qos.logback.classic.pattern.ThrowableHandlingConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.github.irvifa.commons.logging.stackdriver.layout.EventJsonStackdriverLayout;
import io.dropwizard.logging.json.AbstractJsonLayoutBaseFactory;
import io.dropwizard.logging.json.layout.TimestampFormatter;
import java.util.TimeZone;

// CHECKSTYLE.ON: IllegalImport

@JsonTypeName("json-stackdriver")
public class EventJsonStackdriverLayoutBaseFactory
    extends AbstractJsonLayoutBaseFactory<ILoggingEvent> {

  @Override
  protected TimestampFormatter createTimestampFormatter(TimeZone timeZone) {
    return new TimestampFormatter("yyyy-MM-dd' 'HH:mm:ss.SSS", timeZone.toZoneId());
  }

  @Override
  @SuppressWarnings("unchecked")
  public LayoutBase<ILoggingEvent> build(LoggerContext context, TimeZone timeZone) {
    final EventJsonStackdriverLayout jsonLayout =
        new EventJsonStackdriverLayout(
            createDropwizardJsonFormatter(),
            createTimestampFormatter(timeZone),
            createThrowableProxyConverter());
    jsonLayout.setContext(context);
    return jsonLayout;
  }

  private ThrowableHandlingConverter createThrowableProxyConverter() {
    return new RootCauseFirstThrowableProxyConverter();
  }
}
