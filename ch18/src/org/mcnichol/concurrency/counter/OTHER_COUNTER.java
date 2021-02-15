package org.mcnichol.concurrency.counter;

public enum OTHER_COUNTER {
    INSTANCE;
    private static long count = 0;

    public static void count() {
        count++;
    }

    public static void reset() {
        count = 0;
    }

    public static long toLong() {
        return count;
    }
}
