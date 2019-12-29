package main.java.utils.observer;

import main.java.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
