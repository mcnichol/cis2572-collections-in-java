package org.mcnichol.concurrency.counter;

public enum FILE_COUNTER {
    INSTANCE;
    private static long count = 0;

    public synchronized static void count() {
        count++;
    }

    public static void reset() {
        count = 0;
    }

    public static void set(long newCount) {
        count = newCount;
    }

    public static long toLong() {
        return count;
    }


}
