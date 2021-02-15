package org.mcnichol.utility;

import java.util.function.Predicate;

public class PredicateUtility {
    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }
}
