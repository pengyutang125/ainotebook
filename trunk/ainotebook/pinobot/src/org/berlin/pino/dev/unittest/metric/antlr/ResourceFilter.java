package org.berlin.pino.dev.unittest.metric.antlr;

public interface ResourceFilter {

    boolean match(String packageName, String resourceName);

  }
