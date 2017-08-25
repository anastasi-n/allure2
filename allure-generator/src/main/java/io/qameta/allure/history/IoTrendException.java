package io.qameta.allure.history;

import java.io.IOException;

/**
 * IO trend exception.
 */
public class IoTrendException extends IOException {

    private static final long serialVersionUID = 7923759338218850553L;

    public IoTrendException(final Throwable cause) {
        super(cause);
    }

    public IoTrendException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
