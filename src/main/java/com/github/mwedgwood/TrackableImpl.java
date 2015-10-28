package com.github.mwedgwood;

import com.google.common.collect.Sets;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class TrackableImpl<T extends Cloneable> implements Trackable<T>, Serializable {

    private final T model;
    private final Set<String> watchedFields = new HashSet<>();
    private final Set<String> ignoredFields = new HashSet<>();

    private T snapshot = null;

    public TrackableImpl(T model) {
        this.model = model;
        this.snapshot = cloneModel();
    }

    @Override
    public BeanDiff<T> diff() {
        if (snapshot == null) {
            throw new RuntimeException("Trackable was never initialized -- cannot compute diff");
        }
        return new BeanDiff<>(snapshot, model, watchedFields, ignoredFields);
    }

    @Override
    public boolean hasChanges() {
        return !diff().getChangedFields().isEmpty();
    }

    @Override
    public Trackable<T> watching(String... fields) {
        this.watchedFields.addAll(Sets.newHashSet(fields));
        return this;
    }

    @Override
    public Trackable<T> ignoring(String... fields) {
        this.ignoredFields.addAll(Sets.newHashSet(fields));
        return this;
    }

    /**
     * Java doesn't allow clone() to be called on generics, so need to use reflection to clone model.
     */
    @SuppressWarnings("unchecked")
    private T cloneModel() {
        try {
            Method clone = model.getClass().getDeclaredMethod("clone");
            return (T) clone.invoke(model);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
