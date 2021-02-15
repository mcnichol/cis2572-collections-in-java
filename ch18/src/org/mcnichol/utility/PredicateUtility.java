package org.mcnichol.utility;

import java.util.function.Predicate;

/**
 * Convenience utility for negating predicates in stream without needing to expand method references
 */
public class PredicateUtility {
    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }
}
