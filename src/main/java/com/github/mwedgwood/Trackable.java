package com.github.mwedgwood;

public interface Trackable<T extends Cloneable> {

    BeanDiff<T> diff();

    boolean hasChanges();

    Trackable<T> watching(String... fields);

    Trackable<T> ignoring(String... fields);

    static <S extends Cloneable> Trackable<S> track(S element) {
        return new TrackableImpl<>(element);
    }

}
