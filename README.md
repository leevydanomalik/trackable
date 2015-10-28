# Trackable
Java utility for tracking changes to a bean.

## Basic usage:
To start recording changes to a bean, simply call `Trackable::track`
```java
SomeBean someBean = new SomeBean()
    .setProperty("foo");

Trackable.track(someBean);

// make a change
someBean.setProperty("bar");

Trackable.hasChanges(); => true
Trackable.diff().getChangedFields() => ["someProperty"]

Trackable.diff().getOldValue("someProperty") => "foo"
Trackable.diff().getNewValue("someProperty") => "bar"
```

To include or exclude certain fields, use `Trackable::watching` and `Trackable::ignoring` respectively
```java
Trackable.track(someBean).watching("someProperty");
Trackable.track(someBean).ignoring("someProperty");
```
