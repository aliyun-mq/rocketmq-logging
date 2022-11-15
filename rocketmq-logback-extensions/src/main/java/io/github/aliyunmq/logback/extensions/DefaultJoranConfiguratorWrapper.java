package io.github.aliyunmq.logback.extensions;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.util.DefaultJoranConfigurator;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.util.Loader;
import ch.qos.logback.core.util.OptionHelper;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DefaultJoranConfiguratorWrapper extends DefaultJoranConfigurator {

    final public static String TEST_AUTOCONFIG_FILE = "logback-test.xml";
    final public static String AUTOCONFIG_FILE = "logback.xml";

    final public static String PROXY_AUTOCONFIG_FILE = "proxy.logback.xml";
    final public static String BROKER_AUTOCONFIG_FILE = "broker.logback.xml";

    final public static String NAMESRV_AUTOCONFIG_FILE = "namesrv.logback.xml";
    final public static String CONTROLLER_AUTOCONFIG_FILE = "controller.logback.xml";
    final public static String TOOLS_AUTOCONFIG_FILE = "tools.logback.xml";

    final public static String CLIENT_AUTOCONFIG_FILE = "client.logback.xml";

    private final List<String> configFiles;

    public DefaultJoranConfiguratorWrapper() {
        this.configFiles = new ArrayList<>();
        configFiles.add(TEST_AUTOCONFIG_FILE);
        configFiles.add(AUTOCONFIG_FILE);
        configFiles.add(PROXY_AUTOCONFIG_FILE);
        configFiles.add(BROKER_AUTOCONFIG_FILE);
        configFiles.add(NAMESRV_AUTOCONFIG_FILE);
        configFiles.add(CONTROLLER_AUTOCONFIG_FILE);
        configFiles.add(TOOLS_AUTOCONFIG_FILE);
        configFiles.add(CLIENT_AUTOCONFIG_FILE);
    }

    @Override
    public ExecutionStatus configure(LoggerContext loggerContext) {
        URL url = findURLOfDefaultConfigurationFile(true);
        if (url != null) {
            try {
                configureByResource(url);
            } catch (JoranException e) {
                e.printStackTrace();
            }
            // we tried and that counts Mary.
            return ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY;
        } else {
            return ExecutionStatus.INVOKE_NEXT_IF_ANY;
        }
    }

    public void configureByResource(URL url) throws JoranException {
        if (url == null) {
            throw new IllegalArgumentException("URL argument cannot be null");
        }
        final String urlString = url.toString();
        if (urlString.endsWith("xml")) {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            configurator.doConfigure(url);
        } else {
            throw new LogbackException(
                "Unexpected filename extension of file [" + url.toString() + "]. Should be .xml");
        }
    }

    public URL findURLOfDefaultConfigurationFile(boolean updateStatus) {
        ClassLoader myClassLoader = Loader.getClassLoaderOfObject(this);
        URL url = findConfigFileURLFromSystemProperties(myClassLoader, updateStatus);
        if (url != null) {
            return url;
        }

        for (String configFile : configFiles) {
            url = getResource(configFile, myClassLoader, updateStatus);
            if (url != null) {
                return url;
            }
        }
        return null;
    }

    private URL findConfigFileURLFromSystemProperties(ClassLoader classLoader, boolean updateStatus) {
        String logbackConfigFile = OptionHelper.getSystemProperty(ClassicConstants.CONFIG_FILE_PROPERTY);
        if (logbackConfigFile != null) {
            URL result = null;
            try {
                result = new URL(logbackConfigFile);
                return result;
            } catch (MalformedURLException e) {
                // so, resource is not a URL:
                // attempt to get the resource from the class path
                result = Loader.getResource(logbackConfigFile, classLoader);
                if (result != null) {
                    return result;
                }
                File f = new File(logbackConfigFile);
                if (f.exists() && f.isFile()) {
                    try {
                        result = f.toURI().toURL();
                        return result;
                    } catch (MalformedURLException e1) {
                    }
                }
            } finally {
                if (updateStatus) {
                    statusOnResourceSearch(logbackConfigFile, classLoader, result);
                }
            }
        }
        return null;
    }

    private URL getResource(String filename, ClassLoader myClassLoader, boolean updateStatus) {
        URL url = Loader.getResource(filename, myClassLoader);
        if (updateStatus) {
            statusOnResourceSearch(filename, myClassLoader, url);
        }
        return url;
    }

    private void statusOnResourceSearch(String resourceName, ClassLoader classLoader, URL url) {
        StatusManager sm = context.getStatusManager();
        if (url == null) {
            sm.add(new InfoStatus("Could NOT find resource [" + resourceName + "]", context));
        } else {
            sm.add(new InfoStatus("Found resource [" + resourceName + "] at [" + url.toString() + "]", context));
            multiplicityWarning(resourceName, classLoader);
        }
    }

    private void multiplicityWarning(String resourceName, ClassLoader classLoader) {
        Set<URL> urlSet = null;
        try {
            urlSet = Loader.getResources(resourceName, classLoader);
        } catch (IOException e) {
            addError("Failed to get url list for resource [" + resourceName + "]", e);
        }
        if (urlSet != null && urlSet.size() > 1) {
            addWarn("Resource [" + resourceName + "] occurs multiple times on the classpath.");
            for (URL url : urlSet) {
                addWarn("Resource [" + resourceName + "] occurs at [" + url.toString() + "]");
            }
        }
    }
}
