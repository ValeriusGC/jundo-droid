package com.gdetotut.libs.jundo_droidsample.model;

import java.io.Serializable;
import java.util.Objects;

import static java.util.UUID.randomUUID;

/**
 * Created by valerius on 25.01.18.
 */

public class TypeOf<V> implements Serializable {
    final V value;

    public TypeOf(V value) {
        this.value = value;
    }

    public static class Oid implements Serializable {
        private final TypeOf<String> type;
        public Oid(String value) {
            this.type = new TypeOf<>(value);
        }

        public static Oid generate() {
            return new Oid(randomUUID().toString());
        }

        public String getValue() {
            return type.value;
        }

        @Override
        public String toString() {
            return "Oid{" +
                    "value=" + type.value +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Oid oid = (Oid) o;
            return Objects.equals(type.value, oid.type.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type.value);
        }
    }
}
