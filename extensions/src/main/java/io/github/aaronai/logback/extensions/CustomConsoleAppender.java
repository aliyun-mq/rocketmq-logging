package io.github.aaronai.logback.extensions;

import ch.qos.logback.core.ConsoleAppender;

/**
 * Custom the default console appender in logback to archive the goal of conditional on/off.
 *
 * <p> Actual logback provides the similar feature, but it introduces janino. See
 * <a href="http://logback.qos.ch/manual/configuration.html#conditional">Logback Conditional</a>
 * for more details.
 */
public class CustomConsoleAppender<E> extends ConsoleAppender<E> {
    public static final String ENABLE_CONSOLE_APPENDER_KEY = "mq.consoleAppender.enabled";
    private final boolean enabled;

    public CustomConsoleAppender() {
        this.enabled = Boolean.parseBoolean(System.getenv(ENABLE_CONSOLE_APPENDER_KEY)) ||
                       Boolean.parseBoolean(System.getProperty(ENABLE_CONSOLE_APPENDER_KEY));
    }

    @Override
    protected void append(E eventObject) {
        if (enabled) {
            super.append(eventObject);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
}