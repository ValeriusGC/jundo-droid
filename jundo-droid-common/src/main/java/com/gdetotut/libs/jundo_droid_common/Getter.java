package com.gdetotut.libs.jundo_droid_common;

import java.io.Serializable;

/**
 * Interface for value's getter. Used in {@link RefCmd}
 * @param <V> the type of this value.
 */
@FunctionalInterface
public interface Getter<V extends Serializable> extends Serializable{
    V get();
}
