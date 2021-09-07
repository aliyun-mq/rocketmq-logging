package io.github.aaronai.logback.extensions;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * Make it possible to fetch process id in logback.
 */
public class ProcessIdConverter extends ClassicConverter {
    private static final long PROCESS_ID_NOT_SET = -2;
    private static final long PROCESS_ID_NOT_FOUND = -1;
    private static long PROCESS_ID = PROCESS_ID_NOT_SET;

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        return String.valueOf(processId());
    }

    private long processId() {
        if (PROCESS_ID != PROCESS_ID_NOT_SET) {
            return PROCESS_ID;
        }
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        // format: "pid@hostname"
        String name = runtime.getName();
        try {
            PROCESS_ID = Integer.parseInt(name.substring(0, name.indexOf('@')));
        } catch (Throwable ignore) {
            PROCESS_ID = PROCESS_ID_NOT_FOUND;
        }
        return PROCESS_ID;
    }
}