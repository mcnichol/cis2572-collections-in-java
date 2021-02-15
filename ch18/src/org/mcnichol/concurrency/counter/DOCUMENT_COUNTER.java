package org.mcnichol.concurrency.counter;

public enum DOCUMENT_COUNTER {
    INSTANCE;
    private static long count = 0;

    public synchronized static void count() {
        count++;
    }

    public static void reset() {
        count = 0;
    }

    public static long toLong() {
        return count;
    }
}
