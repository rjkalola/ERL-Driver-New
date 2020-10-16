package com.app.utilities.utils;

import java.util.Collection;

/**
 * Created by Dhaval on 21-02-2018.
 */

public class CollectionUtils {

    /**
     * Null-safe check if the specified collection is empty.
     * <p>
     * Null returns true.
     *
     * @param coll  the collection to check, may be null
     * @return true if empty or null
     *
     */
    public static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * Null-safe check if the specified collection is not empty.
     * <p>
     * Null returns false.
     *
     * @param coll  the collection to check, may be null
     * @return true if non-null and non-empty
     *
     */
    public static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }


    /**
     * Delegates to {@link Collection#remove}. Returns {@code false} if the {@code remove} method
     * throws a {@code ClassCastException} or {@code NullPointerException}.
     */
    static boolean safeRemove(Collection<?> collection, Object object) {
        isNotEmpty(collection);
        try {
            return collection.remove(object);
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    /**
     * Delegates to {@link Collection#contains}. Returns {@code false} if the {@code contains} method
     * throws a {@code ClassCastException} or {@code NullPointerException}.
     */
    static boolean safeContains(Collection<?> collection, Object object) {
        isNotEmpty(collection);
        try {
            return collection.contains(object);
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

}
