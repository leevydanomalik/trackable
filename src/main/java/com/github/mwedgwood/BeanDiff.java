package com.github.mwedgwood;

import com.google.common.collect.Sets;
import org.apache.commons.beanutils.BeanMap;

import java.util.*;

public class BeanDiff<T> {

    private final Map<Object, Object> oldValues;
    private final Map<Object, Object> newValues;
    private final Set<String> changed = new HashSet<>();

    public BeanDiff(T before, T after) {
        this(before, after, Collections.emptySet(), Collections.emptySet());
    }

    public BeanDiff(T before, T after, Set<String> included, Set<String> excluded) {
        oldValues = new BeanMap(before);
        newValues = new BeanMap(after);

        computeChanges(included, excluded);
    }

    void computeChanges(Set<String> included, Set<String> excluded) {
        if (included.isEmpty() && excluded.isEmpty()) {
            oldValues.keySet().forEach(this::addIfChanged);
            return;
        }
        if (!included.isEmpty()) {
            included.forEach(this::addIfChanged);
            return;
        }
        Sets.difference(oldValues.keySet(), excluded).stream().forEach(this::addIfChanged);
    }

    void assertValidField(Object fieldName) {
        BeanMap beanMap = BeanMap.class.cast(oldValues);
        if (!beanMap.containsKey(fieldName)) {
            throw new IllegalArgumentException(
                    "Invalid field specified '" + fieldName + "' for bean " + beanMap.getBean().getClass().getName()
            );
        }
    }

    void addIfChanged(Object fieldName) {
        assertValidField(fieldName);

        Object oldValue = this.oldValues.get(fieldName);
        Object newValue = this.newValues.get(fieldName);

        if (!Objects.equals(oldValue, newValue)) {
            changed.add(fieldName.toString());
        }
    }

    public Set<String> getChangedFields() {
        return Collections.unmodifiableSet(changed);
    }

    public Object getOldValue(String fieldName) {
        assertValidField(fieldName);
        return oldValues.get(fieldName);
    }

    public Object getNewValue(String fieldName) {
        assertValidField(fieldName);
        return newValues.get(fieldName);
    }

}
