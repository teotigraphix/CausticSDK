package org.androidtransfuse.event;

public interface EventObserver<T>
{

    String TRIGGER = "trigger";

    /**
     * Triggers when a T event is triggered on the EventManager which this
     * Observer is registered with.
     * 
     * @param object event
     */
    void trigger(T object);
}
