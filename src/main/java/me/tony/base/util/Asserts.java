package me.tony.base.util;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by tony on 2017/2/28.
 */
public final class Asserts {
    private Asserts() {
    }

    public static <T> void must(Predicate<T> predicate, T t) {
        if (!predicate.test(Objects.requireNonNull(t))) {
            throw new AssertException();
        }
    }

    public static class AssertException extends RuntimeException {

        private static final long serialVersionUID = 2123833866434906103L;

        public AssertException() {
        }

        public AssertException(String message) {
            super(message);
        }

        public AssertException(String message, Throwable cause) {
            super(message, cause);
        }

        public AssertException(Throwable cause) {
            super(cause);
        }

        public AssertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
