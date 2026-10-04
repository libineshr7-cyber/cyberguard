package com.cyberguard.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Very small brute-force protection: 5 failed logins lock an account email for 5 minutes. */
public final class LoginThrottle {
    private static final int MAX_FAILURES = 5;
    private static final long LOCK_MS = 5 * 60 * 1000L;
    private static final Map<String, int[]> FAILURES = new ConcurrentHashMap<>();
    private static final Map<String, Long> LOCKED_UNTIL = new ConcurrentHashMap<>();

    private LoginThrottle() { }

    public static boolean isLocked(String key) {
        Long until = LOCKED_UNTIL.get(key);
        if (until == null) return false;
        if (System.currentTimeMillis() > until) { reset(key); return false; }
        return true;
    }

    public static void recordFailure(String key) {
        int[] c = FAILURES.computeIfAbsent(key, k -> new int[1]);
        if (++c[0] >= MAX_FAILURES) LOCKED_UNTIL.put(key, System.currentTimeMillis() + LOCK_MS);
    }

    public static void reset(String key) { FAILURES.remove(key); LOCKED_UNTIL.remove(key); }
}
