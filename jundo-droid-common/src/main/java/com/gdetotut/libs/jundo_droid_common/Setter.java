package com.gdetotut.libs.jundo_droid_common;

import java.io.Serializable;

@FunctionalInterface
public interface Setter<V extends Serializable> extends Serializable{
    void set(V v);
}
