package org.berlin.pino.dev.unittest.metric.antlr;

import java.io.InputStream;

public interface ClassPath {

    boolean isResource(String resource);

    boolean isPackage(String packageName);

    String[] listPackages(String string);

    InputStream getResourceAsStream(String resource);

    String[] listResources(String packageName);

    String[] findResources(String rootPackageName, ResourceFilter resourceFilter);

}
