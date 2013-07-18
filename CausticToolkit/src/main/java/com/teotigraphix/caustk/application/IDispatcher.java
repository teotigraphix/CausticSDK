
package com.teotigraphix.caustk.application;

import org.androidtransfuse.event.EventObserver;

public interface IDispatcher {
    /**
     * Register the given observer to be triggered if the given event type is
     * triggered.
     * 
     * @param event type
     * @param observer event observer
     * @param <T> relating type
     */
    <T> void register(Class<T> event, EventObserver<T> observer);

    /**
     * Unregisters an EventObserver by equality.
     * 
     * @param observer Event Observer
     */
    void unregister(EventObserver<?> observer);

    /**
     * Triggers an event through the EventManager. This will call the registered
     * EventObservers with the provided event.
     * 
     * @param event object
     */
    void trigger(Object event);
}
