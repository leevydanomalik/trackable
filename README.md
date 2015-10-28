# Trackable
Java utility for tracking changes to a bean.

## Basic usage:
To start tracking changes to your bean, simply call `Trackable::track`
```
SomeBean someBean = new SomeBean()
    .setProperty("foo");

Trackable.track(someBean);

// make some change
someBean.setProperty("bar");

Trackable.hasChanges(); => true
Trackable.diff().getOldValue("someProperty") => "foo"

```

To include or exclude certain fields, use `Trackable::watching` and `Trackable::ignoring` respectively
```
Trackable.track(someBean).watching("someProperty");
Trackable.track(someBean).ignoring("someProperty");
```
