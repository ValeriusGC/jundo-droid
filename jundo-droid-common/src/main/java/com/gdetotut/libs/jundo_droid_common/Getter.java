package com.gdetotut.libs.jundo_droid_common;

import java.io.Serializable;

@FunctionalInterface
public interface Getter<V extends Serializable> extends Serializable{
    V get();
}
