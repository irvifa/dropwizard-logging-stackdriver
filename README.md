# dropwizard-logging-stackdriver

## Configuration Example

```aidl
logging:
  appenders:
    - type: console
      layout:
        type: json-stackdriver
```

## Example log

[LogEntry](https://cloud.google.com/logging/docs/reference/v2/rest/v2/LogEntry) will parse
the `SEVERITY` filter based on `severity` mentioned in the log json payload.

```aidl
{"severity":"INFO","message":"INFO  [2020-04-20 01:40:41.127] main - org.eclipse.jetty.server.handler.ContextHandler: Started i.d.j.MutableServletContextHandler@726a8729{/,null,AVAILABLE}"}
```